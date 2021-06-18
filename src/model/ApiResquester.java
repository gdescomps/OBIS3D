package model;

import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.concurrent.TimeUnit;

public abstract class ApiResquester {

	public static JSONObject readJsonFromUrl(String url) {
		String json = "";
		HttpClient client = HttpClient.newBuilder()
				.version(Version.HTTP_1_1)
				.followRedirects(Redirect.NORMAL)
				.connectTimeout(Duration.ofSeconds(20))
				.build();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.timeout(Duration.ofMinutes(2))
				.header("Content-Type", "application/json")
				.GET()
				.build();
		try {
			json = client.sendAsync(request, BodyHandlers.ofString())
					.thenApply(HttpResponse::body).get(10,TimeUnit.SECONDS);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new JSONObject(json);
	}
	
	public static JSONArray readJsonArrayFromUrl(String url) {
		String json = "";
		HttpClient client = HttpClient.newBuilder()
				.version(Version.HTTP_1_1)
				.followRedirects(Redirect.NORMAL)
				.connectTimeout(Duration.ofSeconds(20))
				.build();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.timeout(Duration.ofMinutes(2))
				.header("Content-Type", "application/json")
				.GET()
				.build();
		try {
			json = client.sendAsync(request, BodyHandlers.ofString())
					.thenApply(HttpResponse::body).get(10,TimeUnit.SECONDS);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new JSONArray(json);
	}
	
	//R�cup�rer le nombre de signalement par r�gion pour un nom d�esp�ce pass� en param�tre
	//Species , GeoHash 
	//Ex : https://api.obis.org/v3/occurrence/grid/3?scientificname=Delphinidae
	public static JSONObject getOccurrences(String species, int precision) throws Exception {
		if(species=="") {
			throw new Exception("Nom de l'esp�ce non renseign�");
		}
		else if(precision==0) {
			throw new Exception("Pr�cision 0 non valide");
		}
		JSONObject jsonOccurence = new JSONObject();
		String newSpecies = species.replaceAll(" ", "%20");
		try {
			jsonOccurence= readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/"+precision+"?scientificname="+newSpecies);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return jsonOccurence;
	}
	
	//R�cup�rer le nombre de signalement par r�gion pour un nom d�esp�ce et entre deux dates pass� en param�tre
	//Ex: https://api.obis.org/v3/occurrence/grid/2?scientificname=Morus%20bassanus&startdate=2015-04-13&enddate=2018-01-23
	public static  JSONObject getOccurrences(String species, int precision, LocalDateTime beginDate, Period interval, int intervalCount) {
		LocalDateTime endDate = beginDate.plus(interval);
		JSONObject jsonOccurence = new JSONObject();	
		String newSpecies = species.replaceAll(" ", "%20");
		try {
			jsonOccurence= readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/"+precision+"?scientificname="+newSpecies+"&startdate="+ beginDate.toLocalDate() +"&enddate="+endDate.toLocalDate());
		}catch(Exception e) {
			e.printStackTrace();
		}
		return jsonOccurence;
	}
	
	//R�cup�rer les d�tails d�enregistrement pour un nom d�esp�ce pass� en param�tre et un GeoHash
	//Ex : https://api.obis.org/v3/occurrence?scientificname=Morus%20bassanus&geometry=spd  avec nom
	//Ex: https://api.obis.org/v3/occurrence?geometry=spd  sans nom
	public static JSONObject getOccurrences(String species, String geoHash) {
		JSONObject jsonOccurence = new JSONObject();	
		String newSpeciesName = species.replaceAll(" ", "%20");
		try {
			if(species=="") {
				jsonOccurence= readJsonFromUrl("https://api.obis.org/v3/occurrence?&geometry="+geoHash);
			}
			else{
				jsonOccurence= readJsonFromUrl("https://api.obis.org/v3/occurrence?scientificname="+newSpeciesName+"&geometry="+geoHash);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return jsonOccurence;
	}
	

	/**
	 * R�cup�rer le nombre de signalement par r�gion pour un nom d�esp�ce pass� en param�tre 
	 * Ex: https://api.obis.org/v3/occurrence/grid/3?scientificname=Delphinidae
	 * @throws Exception 
	 */
	public static JSONObject getExhaustiveReport(Species species, int precision) throws Exception {
		System.out.println(species.getScientificName());
		if(species.getScientificName()=="") {
			throw new Exception("Nom de l'esp�ce non renseign�");
		}
		else if(precision==0) {
			throw new Exception("Pr�cision 0 non valide");
		}
		JSONObject jsonOccurence = new JSONObject();
		String newSpecies = species.getScientificName().replaceAll(" ", "%20");
		try {
			jsonOccurence= readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/"+precision+"?scientificname="+newSpecies);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return jsonOccurence;
	}
	
	
	/**
	 * R�cup�rer les 20 premiers noms scientifiques d�esp�ce commen�ant par une cha�ne de caract�res pass�e en param�tre
	 * Ex: https://api.obis.org/v3/taxon/complete/verbose/ma
	 */
	public static  JSONObject getSpeciesNames(String string) {
		JSONObject jsonOccurence = new JSONObject();	
		JSONArray jsonArrayOccurence = new JSONArray();
		String newString = string.replaceAll(" ", "%20");
		try {
			jsonArrayOccurence= readJsonArrayFromUrl("https://api.obis.org/v3/taxon/complete/verbose/"+newString);
		}catch(Exception e) {
			e.printStackTrace();
		}
		jsonOccurence.put("search",jsonArrayOccurence);
		return jsonOccurence;
	}
	
	
	/**
	 * Ex: https://api.obis.org/v3/taxon/morus 
	 */
	public static  JSONObject getSpecies(String nameStart) {
		JSONObject jsonOccurence = new JSONObject();	
		String newNameStart = nameStart.replaceAll(" ", "%20");
		try {
			jsonOccurence= readJsonFromUrl("https://api.obis.org/v3/taxon/"+newNameStart);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return jsonOccurence;
	}
	
}
