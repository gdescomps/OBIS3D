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
import java.time.Period;
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
		String newUrlString = species.replaceAll(" ", "%20");
		try {
			jsonOccurence= readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/"+precision+"?scientificname="+newUrlString);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return jsonOccurence;
	}
	
	//R�cup�rer le nombre de signalement par r�gion pour un nom d�esp�ce et entre deux dates pass� en param�tre
	//Ex: https://api.obis.org/v3/occurrence/grid/2?scientificname=Morus%20bassanus&startdate=2015-04-13&enddate=2018-01-23
	public static JSONObject getOccurrences(String name, int precision, LocalDateTime beginDate, Period interval, int intervalCount) {
		LocalDateTime endDate = beginDate.plus(interval);
		//System.out.println(beginDate.toLocalDate());
		//System.out.println(endDate.toLocalDate());
		JSONObject jsonOccurence = new JSONObject();	
		String newUrlString = name.replaceAll(" ", "%20");
		try {
			jsonOccurence= readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/"+precision+"?scientificname="+newUrlString+"&startdate="+ beginDate.toLocalDate() +"&enddate="+endDate.toLocalDate());
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
		String newUrlString = species.replaceAll(" ", "%20");
		try {
			if(species=="") {
				jsonOccurence= readJsonFromUrl("https://api.obis.org/v3/occurrence?&geometry="+geoHash);
			}
			else{
				jsonOccurence= readJsonFromUrl("https://api.obis.org/v3/occurrence?scientificname="+newUrlString+"&geometry="+geoHash);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return jsonOccurence;
	}
	
	//R�cup�rer les 20 premiers noms scientifiques d�esp�ce commen�ant par une cha�ne de caract�res pass�e en param�tre
	//Ex: https://api.obis.org/v3/taxon/complete/verbose/ma
	public static JSONObject getOccurrences(String chaine) {
		JSONObject jsonOccurence = new JSONObject();	
		try {
			jsonOccurence= readJsonFromUrl("https://api.obis.org/v3/taxon/complete/verbose/"+chaine);
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
		JSONObject jsonOccurence = getOccurrences("ma");
		JSONArray songsArray = jsonOccurence.toJSONArray(jsonOccurence.names());
		JSONObject article = songsArray.toJSONArray(0); //Le premier element
		System.out.println(article.getString("scientificName"));
		*/
	}
}
