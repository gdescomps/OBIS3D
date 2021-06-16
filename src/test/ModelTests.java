package test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.Period;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
//import org.skyscreamer.jsonassert.JSONAssert;

import model.Model;
import model.Species;
import model.ZoneReport;
import model.ApiResquester;
import model.GlobalReport;


class ModelTests {

	@Test
	@DisplayName("Test Hello World")
	void testHelloWorld() {
		Model model = new Model();

		assertEquals("Hello World", model.helloWorld());
	}
	
	
	@Test
	@DisplayName("Test getOccurrences per region for a species name passed in parameter")
	void testOccurencesOfSpeciesInRegion() throws Exception {
		
		//test of https://api.obis.org/v3/occurrence/grid/1?scientificname=Morus%20Bassanus
		JSONObject jsonOccurence = ApiResquester.getOccurrences("Morus bassanus",1); //test name with space	
		JSONArray resultatRecherche = jsonOccurence.getJSONArray("features");
		JSONObject article = resultatRecherche.getJSONObject(0); //the first element
		 assertEquals(article.getString("type"),"Feature");
		
	}
	
	
//	@Test
//	@DisplayName("Test getOccurrences per region for a species without name passed in parameter")
//	void testOccurencesOfSpeciesInRegionWithoutName() throws Exception {
//		
//		//assertEquals(ApiResquester.readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/1?scientificname="), ApiResquester.getOccurrences("",1));
//	}
//	
//	@Test
//	@DisplayName("Test getOccurrences per region for a species with 0 precision passed in parameter")
//	void testOccurencesOfSpeciesInRegionWithoutPrecision() throws Exception {
//		
//		//assertEquals(ApiResquester.readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/0?scientificname=Morus%20Bassanus"), ApiResquester.getOccurrences("Morus Bassanus",0));
//	}
//Test of getSpecies
	
	@Test
	@DisplayName("Test getOccurrences per region for a species name and between two dates passed in parameter")
	void testOccurencesOfSpeciesInRegionWithInterval() throws Exception {
		//Test of https://api.obis.org/v3/occurrence/grid/2?scientificname=Morus%20bassanus&startdate=2015-04-13&enddate=2018-01-23")
		JSONObject jsonOccurence = ApiResquester.getOccurrences("Morus bassanus",2, LocalDateTime.of(2015, 04, 13,0,0), Period.of(3, 01, 3),2); //test interval
		JSONArray resultatRecherche = jsonOccurence.getJSONArray("features");
		JSONObject article = resultatRecherche.getJSONObject(0); //the first element
		assertEquals(article.getString("type"),"Feature");
		
	}
	
	@Test
	@DisplayName("Test getOccurrences for a species name passed in parameter and a GeoHash")
	void testOccurencesOfSpeciesWithSameGeoHash() throws Exception {
		//test of https://api.obis.org/v3/occurrence?scientificname=Morus%20bassanus&geometry=spd"
		JSONObject jsonOccurence = ApiResquester.getOccurrences("Morus bassanus","spd"); 
		JSONArray resultatRecherche = jsonOccurence.getJSONArray("results");
		JSONObject article = resultatRecherche.getJSONObject(0); 
		assertEquals(article.getString("country"),"FR");
	}
	
	@Test
	@DisplayName("Test getOccurrences with a GeoHash")
	void testOccurencesWithSameGeoHash() throws Exception {
		//test of https://api.obis.org/v3/occurrence?geometry=spd
		JSONObject jsonOccurence = ApiResquester.getOccurrences("","spd"); 
		JSONArray resultatRecherche = jsonOccurence.getJSONArray("results");
		JSONObject article = resultatRecherche.getJSONObject(0); 
		assertEquals(article.getString("infraphylum"),"Dinoflagellata");
	}
	
	
	@Test
	@DisplayName("Test getOccurrences of the first 20 names")
	void testOccurrencesOf20firstNames() {
		JSONObject jsonOccurence = ApiResquester.getSpeciesNames("ma");;
		JSONArray resultatRecherche = jsonOccurence.getJSONArray("search");
		boolean notFailed = true;
		if(resultatRecherche.length()<=20) { 
			int i=0;
			while(i<resultatRecherche.length() && notFailed==true) { //we loop on the json objects to see if all the scientific names are correct
				if(!resultatRecherche.getJSONObject(i).getString("scientificName").substring(0, 2).equals("Ma")) { //they all need to have a "Ma" at the beginning
					notFailed = false;
				}
				i++;
			}
		}else { //if the json has a size greater than 20 there is a problem
			notFailed = false;
		}
		assertTrue(notFailed);
	}
	
	@Test
	@DisplayName("Test global report")
	void testGlobalReport() throws Exception {
		Model model = new Model();
		Boolean notFailed = true;
		GlobalReport globalReport = model.getExhaustiveReport("Delphinidae");
		ZoneReport zoneReport = globalReport.getZoneReport().get(0);
		//Test of the values of the global report
		if(globalReport.getMaxOccurences()!=8147) { 
			fail("maxOccurence is wrong");			

		}else if(globalReport.getMinOccurences()!=1) {
			fail("minOccurence is wrong");			
		}
		else if(globalReport.getSpecies().getScientificName()!="Delphinidae") { //if the name is wrong
			fail("the name is wrong");			
		}
		else if(globalReport.getZoneReport().size()<=0) { //if there is no zone
			fail("There is no zone");
		}else if(zoneReport.getOccurenceCount()!=8147) {
			fail("First occurrence count is wrong");
		}else if(zoneReport.getZone().get(0).getX()!=-80.15625 || zoneReport.getZone().get(0).getY()!=32.34375) {
			fail("First zone coordinate is wrong");
		}
	}

	/*
	@Test
	@DisplayName("Fail Test")
	void failTest() {
		if(true) {
			fail("Description of custom failure");
		}
	}
	*/
}

