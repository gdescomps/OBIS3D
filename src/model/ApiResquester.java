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

public class ApiResquester {

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
	
	//Récupérer le nombre de signalement par région pour un nom d’espèce passé en paramètre
	//Species , GeoHash 
	//Ex : https://api.obis.org/v3/occurrence/grid/3?scientificname=Delphinidae
	public static JSONObject getOccurrences(String species, int precision) throws Exception {
		if(species=="") {
			throw new Exception("Nom de l'espèce non renseigné");
		}
		else if(precision==0) {
			throw new Exception("Précision 0 non valide");
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
	
	//Récupérer le nombre de signalement par région pour un nom d’espèce et entre deux dates passé en paramètre
	//Ex: https://api.obis.org/v3/occurrence/grid/2?scientificname=Morus%20bassanus&startdate=2015-04-13&enddate=2018-01-23
	public static JSONObject getOccurrences(String species, int precision, LocalDateTime beginDate, Period interval, int intervalCount) {
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
	
	//Récupérer les détails d’enregistrement pour un nom d’espèce passé en paramètre et un GeoHash
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
	
	//Récupérer les 20 premiers noms scientifiques d’espèce commençant par une chaîne de caractères passée en paramètre
	//Ex: https://api.obis.org/v3/taxon/complete/verbose/ma
	public static JSONObject getOccurrences(String string) {
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
	
	
	//Ex: https://api.obis.org/v3/taxon/morus 
	public static JSONObject getSpeciesNames(String nameStart) {
		JSONObject jsonOccurence = new JSONObject();	
		String newNameStart = nameStart.replaceAll(" ", "%20");
		try {
			jsonOccurence= readJsonFromUrl("https://api.obis.org/v3/taxon/"+newNameStart);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return jsonOccurence;
	}
	
	//Ex: https://api.obis.org/v3/occurrence?scientificname=Morus%20bassanus
	public static JSONObject getSpecies(String speciesScientificName) {
		JSONObject jsonOccurence = new JSONObject();	
		String newSpeciesScientificName = speciesScientificName.replaceAll(" ", "%20");
		try {
			jsonOccurence= readJsonFromUrl("https://api.obis.org/v3/occurrence?scientificname="+newSpeciesScientificName);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return jsonOccurence;
	}
	
	
	public static void main(String[] args) throws Exception {
		/*
		try {
			JSONObject jsonOccurence= readJsonFromUrl("https://api.obis.org/v3/occurrence/");
			JSONArray resultat = jsonOccurence.getJSONArray("results");
			JSONObject espece = resultat.getJSONObject(0); //Le premier element
			System.out.println(espece.getString("infraphylum"));
		}catch(Exception e) {
			e.printStackTrace();
		}
		*/
		
		//JSONObject jsonOccurence = getOccurrences("",2);   //test avec nom vide
		//JSONObject jsonOccurence = getOccurrences("Delphinidae",0);   //test precision 0
		//JSONObject jsonOccurence = getOccurrences("Morus bassanus",1); //test nom avec un espace	
		// JSONObject jsonOccurence = getOccurrences("Morus bassanus",2, LocalDateTime.of(2015, 04, 13,0,0), Period.of(3, 01, 3),2); //test intervalle

		/*
		JSONArray resultatRecherche = jsonOccurence.getJSONArray("features");
		JSONObject article = resultatRecherche.getJSONObject(0); //Le premier element
		System.out.println(article.getString("type"));
		*/
		
		/*
		JSONObject jsonOccurence = getOccurrences("Morus bassanus","spd"); //https://api.obis.org/v3/occurrence?scientificname=Morus%20bassanus&geometry=spd
		JSONArray resultatRecherche = jsonOccurence.getJSONArray("results");
		JSONObject article = resultatRecherche.getJSONObject(0); 
		System.out.println(article.getString("country"));
		*/
		
		//TEST des 20 premiers noms
		/*
		JSONObject jsonOccurence = getOccurrences("ma");;
		JSONArray resultatRecherche = jsonOccurence.getJSONArray("search");
		JSONObject species = resultatRecherche.getJSONObject(0); 
		System.out.println(species.getString("scientificName"));
		*/
		
		//Test getSpecies
		System.out.println(getSpecies("morus bassanus"));
		System.out.println(getSpecies("mr"));
		System.out.println(getSpeciesNames("morus bassanus"));

	}
}
