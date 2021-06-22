package test;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;

//<<<<<<< HEAD
import java.io.IOException;
//import java.time.LocalDateTime;
//import java.time.Period;
//import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import model.ApiResquester;
import model.GlobalReport;

import org.json.JSONArray;
//import org.json.JSONObject;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
import org.json.JSONObject;

import model.Model;
import model.Occurrence;
import model.Species;
import model.ZoneReport;
//import model.ApiResquester;
//import model.GlobalReport;


class ModelTests {

	
//	@Test
//	@DisplayName("Fail Test")
//	void failTest() {
//		if(true) {
//			fail("Description of custom failure");
//		}
//	}
	
	
	/**
	 * Test equality of Global Reports gotten from Local and from the API
	 * @throws Exception
	 */
	@Test
	@DisplayName("Test equality of Global Reports gotten from Local and from the API")
	void compareLocalAndApiGlobalReport() throws Exception {
		Model model = new Model();

		GlobalReport globalReportLocal = new GlobalReport();
		GlobalReport globalReportAPI = new GlobalReport();

		
		globalReportAPI= model.getExhaustiveReport("Delphinidae");
//		System.out.println(globalReportAPI.getMaxOccurences());
//		System.out.println(globalReportAPI.getMinOccurences());
//		System.out.println(globalReportAPI.getZoneReports().get(0).getZone().get(0));
//		System.out.println(globalReportAPI.getZoneReports().get(0).getZone().get(1));
//		System.out.println(globalReportAPI.getZoneReports().get(0).getZone().get(2));

		globalReportLocal = model.getExhaustiveReportFromLocal("Delphinidae", "src/delphinidaeOccurence.json");
//		System.out.println(globalReportLocal.getMaxOccurences());
//		System.out.println(globalReportLocal.getMinOccurences());
//		System.out.println(globalReportLocal.getZoneReports().get(0).getZone().get(0));
//		System.out.println(globalReportLocal.getZoneReports().get(0).getZone().get(1));
//		System.out.println(globalReportLocal.getZoneReports().get(0).getZone().get(2));
//		
		if(!(globalReportAPI.equals(globalReportLocal))) {
			fail("Different Global Reports");
		}
	}
	

	@Test
	@DisplayName("Test getOccurrences per region for a species name passed in parameter")
	void testOccurencesOfSpeciesInRegion() throws Exception {
		//test of https://api.obis.org/v3/occurrence/grid/1?scientificname=Morus%20Bassanus
		JSONObject jsonOccurrence = ApiResquester.getOccurrences("Morus bassanus",1); //test name with space	
		JSONArray result = jsonOccurrence.getJSONArray("features");
		JSONObject firstElement = result.getJSONObject(0); //the first element
		 assertEquals(firstElement.getString("type"),"Feature");
	}  //valid� 1
	
	
	@Test
	@DisplayName("Test of getZoneReportsbyTimePeriods")
	void testgetZoneReportsbyTimePeriods() throws IOException {
		Model model = new Model();
		
		ArrayList<GlobalReport> zoneReportsbyTimePeriods = new ArrayList<GlobalReport>();
		zoneReportsbyTimePeriods = model.getZoneReportsbyTimePeriods("Morus bassanus", 2,LocalDateTime.of(2015, 04, 13,0,0), Period.of(3, 01, 3),2);

		// test equality between Global reports from  zoneReportsbyTimePeriods and Global reports from local
		
		GlobalReport globalReportLocal = new GlobalReport();
		globalReportLocal = model.getExhaustiveReportFromLocal("Morus bassanus", "src/beginDate=2015-04-13&endDate=2018-05-16.json");
		if(!(globalReportLocal.equals(zoneReportsbyTimePeriods.get(0)))) {
			fail("Different Global Reports");
		}
		
		
		GlobalReport globalReportLocal2 = new GlobalReport();
		globalReportLocal2 = model.getExhaustiveReportFromLocal("Morus bassanus", "src/beginDate=2018-05-16&endDate=2021-06-19.json");
		if(!(globalReportLocal2.equals(zoneReportsbyTimePeriods.get(1)))) {
			fail("Different Global Reports");
		}
	}
	
	@DisplayName("Test getOccurrences per region for a species name and between two dates passed in parameter")
	void testOccurencesOfSpeciesInRegionWithInterval() throws Exception {
		//Test of https://api.obis.org/v3/occurrence/grid/2?scientificname=Morus%20bassanus&startdate=2015-04-13&enddate=2018-01-23")
		JSONObject jsonOccurrence = ApiResquester.getOccurrences("Morus bassanus",2, LocalDateTime.of(2015, 04, 13,0,0), Period.of(3, 01, 3)); //test interval
		JSONArray result = jsonOccurrence.getJSONArray("features");
		JSONObject firstElement = result.getJSONObject(0); //the first element
		assertEquals(firstElement.getString("type"),"Feature");		
	} // valid� 2
	
	
	@Test
	@DisplayName("Test getOccurrences for a species name passed in parameter and a GeoHash")
	void testOccurencesOfSpeciesWithSameGeoHash() throws Exception {
		//test of https://api.obis.org/v3/occurrence?scientificname=Morus%20bassanus&geometry=spd"
		JSONObject jsonOccurrence = ApiResquester.getOccurrences("Morus bassanus","spd"); 
		JSONArray result = jsonOccurrence.getJSONArray("results");
		JSONObject secondElement = result.getJSONObject(1); 
		//Test some value of the second element
		assertEquals(secondElement.getString("country"),"FR");
		assertEquals(secondElement.getInt("date_year"),2011);
		assertEquals(secondElement.getString("scientificNameID"),"urn:lsid:marinespecies.org:taxname:148776");
		assertEquals(secondElement.getInt("year"),2011);
		assertEquals(secondElement.getString("scientificName"),"Morus bassanus");
	} // valid� 3
	
	
	@Test
	@DisplayName("Test getOccurrences with a GeoHash")
	void testOccurencesWithSameGeoHash() throws Exception {
		//test of https://api.obis.org/v3/occurrence?geometry=spd
		JSONObject jsonOccurrence = ApiResquester.getOccurrences("","spd"); 
		JSONArray result = jsonOccurrence.getJSONArray("results");
		JSONObject firstElement = result.getJSONObject(0); 
		assertEquals(firstElement.getString("infraphylum"),"Dinoflagellata");
	} // valid� 4
	
	
	@Test
	@DisplayName("Test getOccurrences of the first 20 names")
	void testOccurrencesOf20firstNames() {
		JSONObject jsonOccurrence = ApiResquester.getSpeciesNames("ma");;
		JSONArray result = jsonOccurrence.getJSONArray("search");
		if(result.length()<=20) { 
			int i=0;
			while(i<result.length()) { //we loop on the json objects to see if all the scientific names are correct
				if(!result.getJSONObject(i).getString("scientificName").substring(0, 2).equals("Ma")) { //they all need to have a "Ma" at the beginning
					fail("the name is incorrect");
				}
				i++;
			}
		}else { //if the json has a size greater than 20 there is a problem
			fail("the array list has a size greater than 20");
		}
	} // valid� 5
	
	@Test
	@DisplayName("Test get 20 first names")
	void testGet20firstNames() {
		Model model = new Model();
		ArrayList<String> listOfNames = model.get20firstNames("a");
		if(listOfNames.size()<=20) { 
			int i=0;
			while(i<listOfNames.size()) { //we loop on the array list if all the scientific names are correct
				if(!listOfNames.get(0).substring(0, 1).equals("A")) { //they all need to have a "A" at the beginning
					fail("the name is incorrect");
				}
				i++;
			}
		}else { //if the array list has a size greater than 20 there is a problem
			fail("the array list has a size greater than 20");
		}
	}//valid� 6
	
	@Test
	@DisplayName("Test getSpecies")
	void testGetSpecies() {
		JSONObject jsonOccurrence = ApiResquester.getSpecies("Morus"); //https://api.obis.org/v3/taxon/morus 
		JSONArray result = jsonOccurrence.getJSONArray("results");
		JSONObject firstElement = result.getJSONObject(0); 
		//Test some value of the first element
		assertEquals(firstElement.getString("taxonRank"), "Genus");
		assertEquals(firstElement.getString("scientificNameAuthorship"), "Vieillot, 1816");
		assertEquals(firstElement.getString("scientificName"), "Morus");
		assertEquals(firstElement.getInt("genusid"), 148775);
		assertEquals(firstElement.getInt("ncbi_id"), 3497);
	} // valid� 7
	
	@Test
	@DisplayName("Test global report")
	void testGlobalReport() throws Exception {
		Model model = new Model();
		GlobalReport globalReport = model.getExhaustiveReport("Delphinidae");
		ZoneReport zoneReport = globalReport.getZoneReport().get(1);
		//Test of the values of the global report
		System.out.println(globalReport.getMaxOccurrences());
		System.out.println(globalReport);
		if(globalReport.getMaxOccurrences()!=95609) { 
			fail("maxOccurence is wrong");			
		}else if(globalReport.getMinOccurrences()!=1) {
			fail("minOccurence is wrong");			
		}
		else if(globalReport.getSpecies().getScientificName()!="Delphinidae") { //if the name is wrong
			fail("the name is wrong");			
		}
		else if(globalReport.getZoneReport().size()<=0) { //if there is no zone
			fail("There is no zone");
		}else if(zoneReport.getOccurrenceCount()!=41021) {
			//Test of the second value 
			fail("First occurrence count is wrong");
		}else if(zoneReport.getZone().get(0).getX()!=-45.0 || zoneReport.getZone().get(0).getY()!=0.0) {
			fail("First zone coordinate is wrong");
		}
	} // valid� 8
	

	@Test
	@DisplayName("Test getOccurrencesDetails")
	void testGetOccurrencesDetails() {
		Model model = new Model();
		//Test of a request with name and geohash passed in parameter
		ArrayList<Occurrence> occurrences = model.getOccurrencesDetails("Manta birostris", "spd");
		//Test of the first element
		System.out.println("bath = " + occurrences.get(0).getBathymetry());
		if(!occurrences.get(0).getOrder().equals("Myliobatiformes")) {
			fail("the order is wrong");
		};
		if(!occurrences.get(0).getSuperclass().equals("Pisces")) {
			fail("the superclass is wrong");
		};
		if(!occurrences.get(0).getRecordedBy().equals("morgados")) {
			fail("the recordedby is wrong");
		};
		if(!occurrences.get(0).getSpecies().getScientificName().equals("Mobula birostris")) {
			fail("the name is wrong");
		};
		
		//if(occurrences.get(0).getBathymetry()!=37.8) {
		if(Float.compare(occurrences.get(0).getBathymetry(),(float) 37.8)==0) {
			System.out.println("bath = " + occurrences.get(0).getBathymetry());
			fail("the bathymetry is wrong");
		};
//		if(!occurrences.get(0).getEventDate("")) {
//			
//		}
		if(occurrences.get(0).getShoredistance()!=1321) {
			fail("the shore distance is wrong");
		}
		//Test of a request without name passed in parameter
		ArrayList<String> otherOccurrences = model.getScientificNamesByGeoHash("spp");
		//Test values of the third element
		if(!otherOccurrences.get(0).equals("Globigerina bulloides")){
			fail("the first name is wrong");
		}
		if(!otherOccurrences.get(1).equals("Agrenocythere pliocenica")){
			fail("the second name is wrong");
		}
		if(!otherOccurrences.get(2).equals("Mobula mobular")) {
			fail("the third name is wrong");
		};
	} // valid� 9
	
}
