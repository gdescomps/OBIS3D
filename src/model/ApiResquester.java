package model;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.Calendar;
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
	
	//Species name, GeoHash zone
	//Ex : https://api.obis.org/v3/occurrence/grid/3?scientificname=Delphinidae
	public static JSONObject getOccurrences(String specie, int precision) throws Exception {
		if(specie=="") {
			throw new Exception("Nom de l'espèce non renseigné");
		}
		else if(precision==0) {
			throw new Exception("Précision 0 non valide");
		}
		JSONObject jsonOccurence = new JSONObject();
		String newUrlString = specie.replaceAll(" ", "%20");
		try {
			jsonOccurence= readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/"+precision+"?scientificname="+newUrlString);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return jsonOccurence;
	}
	
	/*
	public static JSONObject getOccurrences(String name, int zone, LocalDateTime beginDate, LocalDateTime interval, int intervalCount) {
		LocalDateTime endDate = beginDate;
		
		JSONObject jsonOccurence = new JSONObject();	
		String newUrlString = name.replaceAll(" ", "%20");
		try {
			jsonOccurence= readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/"+zone+"?scientificname="+newUrlString+"&startdate="+ beginDate.toLocalDate() +"&enddate="+endDate.toLocalDate());
		}catch(Exception e) {
			e.printStackTrace();
		}
		return jsonOccurence;
	}
	*/
	
	//Ex : https://api.obis.org/v3/occurrence?scientificname=Morus%20bassanus&geometry=spd
	public static JSONObject getOccurrences(String name, String geoHash) {
		JSONObject jsonOccurence = new JSONObject();	
		String newUrlString = name.replaceAll(" ", "%20");
		try {
			jsonOccurence= readJsonFromUrl("https://api.obis.org/v3/occurrence?scientificname="+newUrlString+"&geometry="+geoHash);
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
		//JSONObject jsonOccurence = getOccurrences("Morus bassanus",2, LocalDateTime.of(2015, 04, 13,0,0), LocalDateTime.of(3, 01, 3,0,0),2); //test intervalle

		/*
		JSONArray resultatRecherche = jsonOccurence.getJSONArray("features");
		JSONObject article = resultatRecherche.getJSONObject(0); //Le premier element
		System.out.println(article.getString("type"));
		
		*/
		
		JSONObject jsonOccurence = getOccurrences("Morus bassanus","spd"); //https://api.obis.org/v3/occurrence?scientificname=Morus%20bassanus&geometry=spd
		JSONArray resultatRecherche = jsonOccurence.getJSONArray("results");
		JSONObject article = resultatRecherche.getJSONObject(0); 
		System.out.println(article.getString("country"));

	}
}
