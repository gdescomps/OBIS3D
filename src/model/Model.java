package model;


import java.awt.geom.Point2D;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import controller.Controller;

public class Model {
	
	Controller controller;
	
	ArrayList<GlobalReport> history = new ArrayList<GlobalReport>();
	
	
	
/// Constructors :
	
	/**
	 * @Constructor of Model
	 */
	public Model() {
	}

	
	
/// Gettres :
	
	/**
	 * @return the controller
	 */
	public Controller getController() {
		return controller;
	}

	
	
/// Setters :
	
	/**
	 * @param controller the controller to set
	 */
	public void setController(Controller controller) {
		this.controller = controller;
	}
	
	

/// Class functions :
	
	/**
	 * @return the Global Report of a species which is composed of the species it self, max and min Occurences of the species in all Occurence's zones, 
	 */
	public GlobalReport getExhaustiveReport(String speciesName) throws Exception {
		// Test if the species really exist
		JSONObject jsonObject = new JSONObject();
		jsonObject = ApiResquester.getSpecies(speciesName);
		if (jsonObject.getInt("total")==0) {
			throw new Exception(" Le nom de l'espèce n'existe pas");
		}
		else {
			Species species = new Species(speciesName);
			
			// Call the API 
			jsonObject = ApiResquester.getExhaustiveReport(species,1);

			return createExhaustiveReport(jsonObject, species);

		}
	}
	

	/**
	 * @param speciesName
	 * @param filePath
	 * @return the Exhaustive Report of the species from local file using realFile method from the class FileReader
	 * @throws IOException
	 */
	public GlobalReport getExhaustiveReportFromLocal(String speciesName, String filePath) throws IOException {
		Species species = new Species(speciesName);
		GlobalReport globalReport = new GlobalReport(species);
		
		JSONObject jsonObject = new JSONObject();
		jsonObject = FileReader.readFile(filePath);
		
		return createExhaustiveReport(jsonObject, species);
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
	public ArrayList<Occurrence> getOccurrencesDetails(String scientificName, String geoHash){
		ArrayList<Occurrence> occurrenceList = new ArrayList<Occurrence>();
		JSONObject jsonOccurrence = ApiResquester.getOccurrences(scientificName,geoHash);
		JSONArray result = jsonOccurrence.getJSONArray("results");
		Species species = null;
		if(result.getJSONObject(0).has("scientificName")) {
			species = new Species(result.getJSONObject(0).getString("scientificName"));
		}
		Occurrence occurrence = new Occurrence();
		occurrence.setSpecies(species);
		for(int i=0;i<result.length();i++) { 
			JSONObject element = result.getJSONObject(i); 
			
			if(element.has("order")) {
				occurrence.setOrder(element.getString("order"));
			}
			if(element.has("recordedBy")){
				occurrence.setRecordedBy(element.getString("recordedBy"));
			}
			if(element.has("superclass")) {
				occurrence.setSuperclass(element.getString("superclass"));
			}
			if(element.has("bathymetry")) {
				
			}if(element.has("shoredistance")) {
				
			}if(element.has("eventDate")) {
				
			}	
			occurrenceList.add(occurrence);
		}
		return occurrenceList;
	}
	
	
	/**
	 * @param geoHash
	 * @return
	 */
	public ArrayList<String> getScientificNamesByGeoHash(String geoHash){
		ArrayList<String> scientificNames = new ArrayList<>();
		JSONObject jsonOccurrence = ApiResquester.getOccurrences("",geoHash);
		JSONArray result = jsonOccurrence.getJSONArray("results");
		for(int i=0;i<result.length();i++) { 
			JSONObject element = result.getJSONObject(i); 
			if(element.has("scientificName")) {
				scientificNames.add(element.getString("scientificName"));
			}
		}
		return scientificNames;
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

	
	/**
	 * @param scientificName
	 * @param geoHashPrecision
	 * @param beginDate
	 * @param interval
	 * @param intervalCount
	 * @return an ArrayList of Global reports of the species for each period of time 
	 */
	public ArrayList<GlobalReport> getZoneReportsbyTimePeriods(String scientificName, int geoHashPrecision, LocalDateTime beginDate, Period interval, int intervalCount){
		
		Species species = new Species(scientificName);
		
		// Instantiate the JSONObject
		JSONObject jsonObject = new JSONObject();
		
		// Instantiate the ZoneReportsbyTimePeriods
		ArrayList<GlobalReport> zoneReportsbyTimePeriods = new ArrayList<GlobalReport>();
		
		LocalDateTime newBeginDate = beginDate;
		
		for (int i = 0; i<intervalCount; i++) {
			jsonObject = ApiResquester.getOccurrences(scientificName, geoHashPrecision, newBeginDate, interval);
			newBeginDate = newBeginDate.plus(interval);
			
			// Create the Global Report of the Species
			GlobalReport globalReport = createExhaustiveReport(jsonObject, species);
			zoneReportsbyTimePeriods.add(globalReport);
		}
		
		return zoneReportsbyTimePeriods;
	}
	
	/// This function treats the jsonObject and make a Global Report out of it
	/// It's used in : getExhaustiveReport & getExhaustiveReportFromLocal & getZoneReportsbyTimePeriods
	/**
	 * @param jsonObject
	 * @param species
	 * @return Global Report 
	 */
	public GlobalReport createExhaustiveReport(JSONObject jsonObject, Species species) {
		
		GlobalReport globalReport = new GlobalReport(species);
		// treat the jsonObject returned from the API/LocalFile
		JSONArray features = jsonObject.getJSONArray("features");
		int minOccurence = features.getJSONObject(0).getJSONObject("properties").getInt("n");
		int maxOccurence = features.getJSONObject(0).getJSONObject("properties").getInt("n");
		
		for (int i=0; i<3; i++) {
			// Occurence Count :
			int occurenceCount = features.getJSONObject(i).getJSONObject("properties").getInt("n");
			
			//  Research Min occurence & Max occurence :
			if (minOccurence>=occurenceCount){minOccurence=occurenceCount;}
			if (maxOccurence<=occurenceCount){maxOccurence=occurenceCount;}
			
			ArrayList<Point2D> zone = new ArrayList<Point2D>(5);
			
			for (int j=0; j<features.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0).length();j++) {
				// Convert the coordinates of "geometry" from a JSONarray to an ArrayList<Point2D> :
				JSONArray location = features.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0).getJSONArray(j);
				Point2D p = new Point2D.Double(location.getDouble(0), location.getDouble(1));
				zone.add(p);
			}
			ZoneReport zoneReport = new ZoneReport(zone, occurenceCount);
			globalReport.addZoneReport(zoneReport);
		}
		globalReport.setMaxOccurrences(maxOccurence);
		globalReport.setMinOccurrences(minOccurence);
		return globalReport;
		
	}

}










