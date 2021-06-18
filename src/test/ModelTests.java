package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;

import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import model.ApiResquester;
import model.GlobalReport;
import model.Model;

class ModelTests {
	
//	@Test
//	@DisplayName("Fail Test")
//	void failTest() {
//		if(true) {
//			fail("Description of custom failure");
//		}
//	}
	
	@Test
	@DisplayName("test Global Report")
	void testGlobalReport() throws Exception {
		Model model = new Model();
		GlobalReport globalReport;
		globalReport=model.getExhaustiveReport("Delphinidae");
		if(globalReport.getMaxOccurences() == -1 ) {
			fail("On s'est fait baisé");
		}
	}
	
	/**
	 * Test if the globalReport of a species gotten from
	 * @throws Exception
	 */
	@Test
	@DisplayName("Test READ FILE")
	void testReadFile() throws Exception {
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
	
	

}

