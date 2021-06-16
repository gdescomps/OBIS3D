package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class FileReader {
	
	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}
	
	
	/**
	 * @param pathOfJsonFile
	 * @return JsonObject read from a Jsonfile
	 * @throws IOException
	 */
	public static JSONObject readFile(String pathOfJsonFile) throws IOException {
		Reader reader = new java.io.FileReader(pathOfJsonFile);
		BufferedReader rd = new BufferedReader(reader);
		String jsonText = readAll(rd);
		JSONObject jsonRoot = new JSONObject(jsonText);
		
		return jsonRoot;
	}

}
