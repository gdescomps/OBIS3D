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
	 * Get json from an url
	 * @param url
	 * @return json the json string
	 */
	public static String readJsonFromUrl(String url) {
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
		return json;
	}
	
	/**
	 * Get json object from an url
	 * @param url
	 * @return json the json object
	 */
	public static JSONObject readJsonObjectFromUrl(String url) {
		String json = readJsonFromUrl(url);
		return new JSONObject(json);
	}
	
	/**
	 * Get json array from an url
	 * @param url
	 * @return json the json array
	 */
	public static JSONArray readJsonArrayFromUrl(String url) {
		String json = readJsonFromUrl(url);
		return new JSONArray(json);
	}
	

	/**
	 * Get the number of records per region for a species name passed in parameter
	 * Ex : https://api.obis.org/v3/occurrence/grid/3?scientificname=Delphinidae
	 * @param scientificName
	 * @param precision
	 * @return jsonOccurence
	 * @throws Exception
	 */
	public static JSONObject getOccurrences(String scientificName, int precision) throws Exception {
		if(scientificName=="") {
			throw new Exception("Nom de l'espèce non renseigné");
		}
		else if(precision==0) {
			throw new Exception("Précision 0 non valide");
		}
		JSONObject jsonOccurrence = new JSONObject();
		String newScientificName = scientificName.replaceAll(" ", "%20");
		try {
			jsonOccurrence= readJsonObjectFromUrl("https://api.obis.org/v3/occurrence/grid/"+precision+"?scientificname="+newScientificName);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return jsonOccurrence;
	}
	
	/**
	 * Get the number of records per region for a species name and between two dates passed in parameter
	 * Ex: https://api.obis.org/v3/occurrence/grid/2?scientificname=Morus%20bassanus&startdate=2015-04-13&enddate=2018-01-23
	 * @param scientificName
	 * @param precision
	 * @param beginDate
	 * @param interval
	 * @param intervalCount
	 * @return jsonOccurence
	 */
	public static  JSONObject getOccurrences(String scientificName, int precision, LocalDateTime beginDate, Period interval) {

		LocalDateTime endDate = beginDate.plus(interval);
		JSONObject jsonOccurrence = new JSONObject();	
		String newScientificName = scientificName.replaceAll(" ", "%20");
		try {
			jsonOccurrence= readJsonObjectFromUrl("https://api.obis.org/v3/occurrence/grid/"+precision+"?scientificname="+newScientificName+"&startdate="+ beginDate.toLocalDate() +"&enddate="+endDate.toLocalDate());

		}catch(Exception e) {
			e.printStackTrace();
		}
		return jsonOccurrence;
	}
	

	/**
	 * Get record details for a parameter species name and a GeoHash
	 * Ex : https://api.obis.org/v3/occurrence?scientificname=Morus%20bassanus&geometry=spd  with name
	 * Ex: https://api.obis.org/v3/occurrence?geometry=spd  without name
	 * @param species
	 * @param geoHash
	 * @return jsonOccurence
	 */
	public static JSONObject getOccurrences(String scientificName, String geoHash) {
		JSONObject jsonOccurrence = new JSONObject();	
		String newScientificName = scientificName.replaceAll(" ", "%20");
		try {
			if(scientificName=="") {
				jsonOccurrence= readJsonObjectFromUrl("https://api.obis.org/v3/occurrence?&geometry="+geoHash);
			}
			else{
				jsonOccurrence= readJsonObjectFromUrl("https://api.obis.org/v3/occurrence?scientificname="+newScientificName+"&geometry="+geoHash);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return jsonOccurrence;
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
		System.out.println(species.getScientificName());
		if(species.getScientificName()=="") {
			throw new Exception("Nom de l'espèce non renseigné");
		}
		else if(precision==0) {
			throw new Exception("Précision 0 non valide");
		}

		JSONObject jsonOccurrence = new JSONObject();
		String newSpecies = species.getScientificName().replaceAll(" ", "%20");
		try {
			jsonOccurrence= readJsonObjectFromUrl("https://api.obis.org/v3/occurrence/grid/"+precision+"?scientificname="+newSpecies);

		}catch(Exception e) {
			e.printStackTrace();
		}
		return jsonOccurrence;
	}
	

	/**
	 * Get the first 20 scientific names of species starting with a string passed in parameter
	 * Ex: https://api.obis.org/v3/taxon/complete/verbose/ma 
	 * @param nameStart
	 * @return jsonOccurence
	 */
	public static  JSONObject getSpeciesNames(String nameStart) {
		JSONObject jsonOccurrence = new JSONObject();	
		JSONArray jsonArrayOccurrence = new JSONArray();
		String newScientificName = nameStart.replaceAll(" ", "%20");
		try {
			jsonArrayOccurrence= readJsonArrayFromUrl("https://api.obis.org/v3/taxon/complete/verbose/"+newScientificName);
		}catch(Exception e) {
			e.printStackTrace();
		}
		jsonOccurrence.put("search",jsonArrayOccurrence);
		return jsonOccurrence;
	}
	
	
	/**
	 * Get the occurences of a species 
	 * Ex: https://api.obis.org/v3/taxon/morus 
	 * @param scientificName
	 * @return jsonOccurence
	 */
	public static  JSONObject getSpecies(String scientificName) {
		JSONObject jsonOccurrence = new JSONObject();	
		String newScientificName = scientificName.replaceAll(" ", "%20");
		try {
			jsonOccurrence= readJsonObjectFromUrl("https://api.obis.org/v3/taxon/"+newScientificName);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return jsonOccurrence;
	}
}
