package model;


import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import controller.Controller;

public class Model {
	
	Controller controller;
	
	ArrayList<GlobalReport> history = new ArrayList<GlobalReport>();
	

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
	
	public GlobalReport getExhaustiveReport(String speciesName) throws Exception {
		// Test si le speciesName existe vraiment
		JSONObject jsonObject = new JSONObject();
		jsonObject = ApiResquester.getSpecies(speciesName);
		if (jsonObject.getInt("total")==0) {
			throw new Exception(" Le nom de l'espèce n'existe pas");
		}
		else {
			Species species = new Species(speciesName);
			// Créer un Global Report pour le species Name recherché
			GlobalReport globalReport = new GlobalReport(species);
			
			// Appel à l'API 
			jsonObject = ApiResquester.getExhaustiveReport(species,3);
			
			// Traitement du JSONObject retourné par l'API
			JSONArray features = jsonObject.getJSONArray("features");
			int minOccurence = features.getJSONObject(0).getJSONObject("properties").getInt("n");
			int maxOccurence = features.getJSONObject(0).getJSONObject("properties").getInt("n");
			
			for (int i=0; i<features.length(); i++) { //features.length()
				// Occurence Count :
				int occurenceCount = features.getJSONObject(i).getJSONObject("properties").getInt("n");
				
				//  Recherche Min occurence & Max occurence :
				if (minOccurence>=occurenceCount){minOccurence=occurenceCount;}
				if (maxOccurence<=occurenceCount){maxOccurence=occurenceCount;}
				ArrayList<Point2D> zone = new ArrayList<Point2D>(5);
				for (int j=0; j<5;j++) {
					// convertion des corrdonnées situées dans "geometry" depuis un JSONarray à un ArrayList<Point2D> :
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

			
	}
	
	/**
	 * Get the details of the different observations for a GeoHash and a scientific name (which can be empty) passed in parameter: 
	 * "scientificName" 
	 * "order" 
	 * "superclass" 
	 * "recordedBy" 
	 * "species"
	 * @param scientificName
	 * @param geoHash
	 * @return occurrenceList
	 */
	public ArrayList<OccurrenceDetail> getOccurrencesDetails(String scientificName, String geoHash){
		ArrayList<OccurrenceDetail> occurrenceList = new ArrayList<OccurrenceDetail>();
		JSONObject jsonOccurrence = ApiResquester.getOccurrences(scientificName,geoHash);
		JSONArray result = jsonOccurrence.getJSONArray("results");
		
		for(int i=1;i<result.length();i++) { 
			JSONObject element = result.getJSONObject(i); 
			
			OccurrenceDetail occurrence = new OccurrenceDetail();
			if(scientificName!="") {
				occurrence.setSpecies(new Species (scientificName));
			}
			if(element.has("order")) {
				occurrence.setOrder(element.getString("order"));
			}
			if(element.has("taxonRemarks")){
				occurrence.setRecordedBy(element.getString("taxonRemarks"));
			}
			if(element.has("superclass")) {
				occurrence.setSuperclass(element.getString("superclass"));
			}
			occurrenceList.add(occurrence);
		}
		return occurrenceList;
	}
	
	/**
	 * Get the first 20 names of species starting with a string passed in parameter
	 * @param nameStart
	 * @return scientificNames
	 */
	public ArrayList<String> get20firstNames(String nameStart){
		ArrayList<String> scientificNames = new ArrayList<String>();
		JSONObject jsonOccurrence = ApiResquester.getSpeciesNames(nameStart);
		JSONArray result = jsonOccurrence.getJSONArray("search");
		for(int i=1;i<result.length();i++) { 
			JSONObject element = result.getJSONObject(i); 
			String scientificName = element.getString("scientificName");
			scientificNames.add(scientificName);
		}
		return scientificNames;
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
		
		Model model = new Model();
//		GlobalReport gb = model.getExhaustiveReport("Delphinidae");
//		System.out.println(gb);
		
	
		//ArrayList<OccurrenceDetail> od1 = model.getOccurrencesDetails("Protozoa", "spd"); //
		ArrayList<OccurrenceDetail> od2 = model.getOccurrencesDetails("Delphinidae", "spd");
		System.out.println(od2);
		//ArrayList<OccurrenceDetail> od3 = model.getOccurrencesDetails("", "spd"); 
		ArrayList<String> names = model.get20firstNames("porcel");
		System.out.println(names);

	}
//	

}
