package model;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class JsonReader {

    private static String readAll(Reader rd) throws IOException {
    	StringBuilder sb = new StringBuilder();
    	int cp;
    	while((cp=rd.read())!=-1) {
    		sb.append((char) cp);
    	}
    	return sb.toString();
    }
    
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
	    try(Reader reader = new FileReader("data.json")){
	    	BufferedReader rd = new BufferedReader(reader);
	    	String jsonText = readAll(rd);
	    	JSONObject jsonRoot = new JSONObject(jsonText);   	
	    	JSONArray resultatRecherche = jsonRoot.getJSONObject("query").getJSONArray("search");
		    JSONObject article = resultatRecherche.getJSONObject(0); //Le premier element
		    System.out.println(article.getString("title"));
		    System.out.println(article.getString("snippet"));
		    System.out.println("NB mot : " + article.getInt("wordcount"));
		    JSONObject article2 = resultatRecherche.getJSONObject(1); //Le second element
		    System.out.println(article2.getString("title"));

		    
	    }catch(IOException e) {
	    	e.printStackTrace();
	    }
	    */
		readJsonFromUrl("https://api.obis.org/v3/occurrence");    
	}
}
