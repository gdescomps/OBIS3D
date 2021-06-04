package model;

import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class ApiResquester {

	public static JSONObject readJsonFromUrl(String url) {
		String json = "";
		HttpClient client = HttpClient.newBuilder().version(Version.HTTP_1_1).followRedirects(Redirect.NORMAL)
				.connectTimeout(Duration.ofSeconds(20)).build();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).timeout(Duration.ofMinutes(2))
				.header("Content-Type", "application/json").GET().build();
		try {
			json = client.sendAsync(request, BodyHandlers.ofString()).thenApply(HttpResponse::body).get(10,
					TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new JSONObject(json);
	}

}
