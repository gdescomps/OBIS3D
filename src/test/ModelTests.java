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
import model.ApiResquester;


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
		
		//assertEquals(ApiResquester.readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/1?scientificname=Morus%20Bassanus"), ApiResquester.getOccurrences("Morus Bassanus",1));
		//JSONAssert.assertEquals(ApiResquester.readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/1?scientificname=Morus%20Bassanus"), ApiResquester.getOccurrences("Morus Bassanus",1), false);
		if(ApiResquester.readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/1?scientificname=Morus%20Bassanus").equals(ApiResquester.getOccurrences("Morus Bassanus",1))) {
			System.out.println("oui");
		}else{
			System.out.println("non");
			fail("");
		};
		
		/*ObjectMapper mapper = new ObjectMapper();

		JsonNode tree1 = mapper.readTree(jsonInput1);
		JsonNode tree2 = mapper.readTree(jsonInput2);

		boolean areTheyEqual = tree1.equals(tree2);
		*/
	}
	
	/*
	@Test
	@DisplayName("Test getOccurrences per region for a species without name passed in parameter")
	void testOccurencesOfSpeciesInRegionWithoutName() throws Exception {
		
		assertEquals(ApiResquester.readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/1?scientificname="), ApiResquester.getOccurrences("",1));
	}
	
	@Test
	@DisplayName("Test getOccurrences per region for a species with 0 precision passed in parameter")
	void testOccurencesOfSpeciesInRegionWithoutPrecision() throws Exception {
		
		assertEquals(ApiResquester.readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/0?scientificname=Morus%20Bassanus"), ApiResquester.getOccurrences("Morus Bassanus",0));
	}
	
	@Test
	@DisplayName("Test getOccurrences per region for a species name and between two dates passed in parameter")
	void testOccurencesOfSpeciesInRegionWithInterval() throws Exception {
		
		assertEquals(ApiResquester.readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/2?scientificname=Morus%20bassanus&startdate=2015-04-13&enddate=2018-01-23"), ApiResquester.getOccurrences("Morus bassanus",2, LocalDateTime.of(2015, 04, 13,0,0), Period.of(3, 01, 3),2));
	}
	
	@Test
	@DisplayName("Test getOccurrences for a species name passed in parameter and a GeoHash")
	void testOccurencesOfSpeciesWithSameGeoHash() throws Exception {
		
		assertEquals(ApiResquester.readJsonFromUrl("https://api.obis.org/v3/occurrence?scientificname=Morus%20bassanus&geometry=spd"), ApiResquester.getOccurrences("Morus Bassanus","spd"));
	}
	
	@Test
	@DisplayName("Test getOccurrences with a GeoHash")
	void testOccurencesWithSameGeoHash() throws Exception {
		
		assertEquals(ApiResquester.readJsonFromUrl("https://api.obis.org/v3/occurrence?geometry=spd"), ApiResquester.getOccurrences("","spd"));
	}
	*/
	
	/*
	@Test
	@DisplayName("Test getOccurrences of the first 20 names")
	void testOccurrencesOf20firstNames() {
		JSONObject jsonOccurence = ApiResquester.getOccurrences("ma");;
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
	*/

	@Test
	@DisplayName("Fail Test")
	void failTest() {
		if(true) {
			fail("Description of custom failure");
		}
	}

}
