package model;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonReader {

	public static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while((cp=rd.read())!=-1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}


	public static void main(String[] args) {

		try(Reader reader = new FileReader("apiDelphinedae.json")){
			BufferedReader rd = new BufferedReader(reader);
			String jsonText = JsonReader.readAll(rd);
			JSONObject jsonRoot = new JSONObject(jsonText);   	
			JSONArray resultatRecherche = jsonRoot.getJSONArray("features");
			JSONObject article = resultatRecherche.getJSONObject(0); //Le premier element
			System.out.println(article.getString("type"));

		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
