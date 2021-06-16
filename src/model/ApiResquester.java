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

	/**
	 * Get json object from an url
	 * @param url
	 * @return json the json object
	 */
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
	
	/**
	 * Get json array from an url
	 * @param url
	 * @return json the json array
	 */
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
	

	/**
	 * Get the number of records per region for a species name passed in parameter
	 * Ex : https://api.obis.org/v3/occurrence/grid/3?scientificname=Delphinidae
	 * @param species
	 * @param precision
	 * @return jsonOccurence
	 * @throws Exception
	 */
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
	
	/**
	 * Get the number of records per region for a species name and between two dates passed in parameter
	 * Ex: https://api.obis.org/v3/occurrence/grid/2?scientificname=Morus%20bassanus&startdate=2015-04-13&enddate=2018-01-23
	 * @param species
	 * @param precision
	 * @param beginDate
	 * @param interval
	 * @param intervalCount
	 * @return jsonOccurence
	 */
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
	

	/**
	 * Get record details for a parameter species name and a GeoHash
	 * Ex : https://api.obis.org/v3/occurrence?scientificname=Morus%20bassanus&geometry=spd  with name
	 * Ex: https://api.obis.org/v3/occurrence?geometry=spd  without name
	 * @param species
	 * @param geoHash
	 * @return jsonOccurence
	 */
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
	 * Get the number of records per region for a species name passed in parameter 
	 * Ex: https://api.obis.org/v3/occurrence/grid/3?scientificname=Delphinidae
	 * @param species
	 * @param precision
	 * @return jsonOccurence
	 * @throws Exception
	 */
	public static JSONObject getExhaustiveReport(Species species, int precision) throws Exception {
		if(species.getScientificName()=="") {
			throw new Exception("Nom de l'espèce non renseigné");
		}
		else if(precision==0) {
			throw new Exception("Précision 0 non valide");
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
	 * Get the first 20 scientific names of species starting with a string passed in parameter
	 * Ex: https://api.obis.org/v3/taxon/complete/verbose/ma 
	 * @param string
	 * @return jsonOccurence
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
	 * Get the occurences of a species 
	 * Ex: https://api.obis.org/v3/taxon/morus 
	 * @param nameStart
	 * @return jsonOccurence
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
	
	
	public static void main(String[] args) throws Exception {
		
		//Test getSpecies
//		System.out.println(getSpecies("morus bassanus"));
//		Species species =new Species("Delphinidae");
//		System.out.println(getOccurrences(species.getScientificName(), 3));
//		System.out.println(getSpecies("mr"));
//		System.out.println(getSpeciesNames("aa"));
	
	}
}
