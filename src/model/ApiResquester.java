package model;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
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

	public static void main(String[] args) {
		/*
		JSONObject json= readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/3?scientificname=Delphinidae");    
			JSONArray resultatRecherche = json.getJSONArray("features");
			JSONObject article = resultatRecherche.getJSONObject(0); //Le premier element
			System.out.println(article.getString("type"));
		*/
		try {
			JSONObject jsonOccurence= readJsonFromUrl("https://api.obis.org/v3/occurrence/");
			JSONArray resultat = jsonOccurence.getJSONArray("results");
			JSONObject espece = resultat.getJSONObject(0); //Le premier element
			System.out.println(espece.getString("infraphylum"));
		}catch(Exception e) {
			e.printStackTrace();
		}


	}
}
