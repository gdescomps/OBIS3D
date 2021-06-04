package model;


import java.io.IOException;
import java.io.Reader;

public class JsonReader {

    private static String readAll(Reader rd) throws IOException {
    	StringBuilder sb = new StringBuilder();
    	int cp;
    	while((cp=rd.read())!=-1) {
    		sb.append((char) cp);
    	}
    	return sb.toString();
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
	}
}
