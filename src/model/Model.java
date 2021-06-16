package model;


import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import controller.Controller;

public class Model {
	
	Controller controller;
	
	ArrayList<GlobalReport> history = new ArrayList<GlobalReport>();
	
	
	

	/**
	 * 
	 */
	public Model() {
	}

	/**
	 * @return the controller
	 */
	public Controller getController() {
		return controller;
	}

	/**
	 * @param controller the controller to set
	 */
	public void setController(Controller controller) {
		this.controller = controller;
	}
	
	public String helloWorld() {
		return "Hello World";
	}
	
	/**
	 * Retourne le Global Report d'une espece qui est constitué du type de l'espece, le min et max de ces occurences par zones et d'une liste des zones où l'espèce apparait
	 */
	public GlobalReport getExhaustiveReport(String speciesName) throws Exception {
		// Test si le speciesName existe vraiment
		JSONObject jsonObject = new JSONObject();
		jsonObject = ApiResquester.getSpecies(speciesName);
		if (jsonObject.getInt("total")==0) {
			throw new Exception(" Le nom de l'espèce n'existe pas");
		}
		else {
			Species species = new Species(speciesName);
			
			// Appel à l'API 
			jsonObject = ApiResquester.getExhaustiveReport(species,3);
			
			return createExhaustiveReport(jsonObject, species);
		}
	}
	
	
	/**
	 * Retourne l'exhaustive report de l'espece à partir de d'un fichier local en utilisant la methode readFile du FileReader;
	 * @param speciesName
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public GlobalReport getExhaustiveReportFromLocal(String speciesName, String filePath) throws IOException {
		Species species = new Species(speciesName);
		GlobalReport globalReport = new GlobalReport(species);
		
		JSONObject jsonObject = new JSONObject();
		jsonObject = FileReader.readFile(filePath);
		
		return createExhaustiveReport(jsonObject, species);
	}
	
	
	public GlobalReport createExhaustiveReport(JSONObject jsonObject, Species species) {
		
		GlobalReport globalReport = new GlobalReport(species);
		// Traitement du JSONObject retourné par l'API
		JSONArray features = jsonObject.getJSONArray("features");
		int minOccurence = features.getJSONObject(0).getJSONObject("properties").getInt("n");
		int maxOccurence = features.getJSONObject(0).getJSONObject("properties").getInt("n");
		
		for (int i=0; i<3; i++) {
			// Occurence Count :
			int occurenceCount = features.getJSONObject(i).getJSONObject("properties").getInt("n");
			
			//  Recherche Min occurence & Max occurence :
			if (minOccurence>=occurenceCount){minOccurence=occurenceCount;}
			if (maxOccurence<=occurenceCount){maxOccurence=occurenceCount;}
			
			ArrayList<Point2D> zone = new ArrayList<Point2D>(5);
			
			for (int j=0; j<features.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0).length();j++) {
				// Convertion des corrdonnées situées dans "geometry" depuis un JSONarray à un ArrayList<Point2D> :
				JSONArray location = features.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0).getJSONArray(j);
				Point2D p = new Point2D.Double(location.getDouble(0), location.getDouble(1));
				zone.add(p);
			}
			ZoneReport zoneReport = new ZoneReport(zone, occurenceCount);
			globalReport.addZoneReport(zoneReport);
		}
		globalReport.setMaxOccurences(maxOccurence);
		globalReport.setMinOccurences(minOccurence);
		return globalReport;
		
	}
		
		

	
	public static void main(String[] args) throws Exception {
		
		//test que cette boucle atteint bien les coordonnées de "geometry" :
		
//		JSONObject json = ApiResquester.readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/3?scientificname=Delphinidae");
//		JSONArray features = json.getJSONArray("features");
//		System.out.println("la longueur est : #################" + features.length());
//		for (int i=0; i<3; i++) {
//			for (int j=0; j<5;j++) {
//				JSONArray location = features.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0).getJSONArray(j);
//				Point2D p = new Point2D.Double(location.getDouble(0), location.getDouble(1));
//				System.out.println(p.getX());
//			}
//		}
//		
		
		
		
	}
//	

}
