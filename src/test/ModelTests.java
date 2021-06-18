package test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.Period;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
//import org.skyscreamer.jsonassert.JSONAssert;

import model.GlobalReport;
import model.Model;
import model.ApiResquester;


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
			fail("On s'est fait bais�");
		}
	}
	
	@Test
	@DisplayName("Test READ FILE")
	void testReadFile() throws Exception {
		Model model = new Model();
		GlobalReport globalReportLocal = new GlobalReport();
		GlobalReport globalReportAPI = new GlobalReport();

		
		globalReportAPI= model.getExhaustiveReport("Delphinidae");
		System.out.println(globalReportAPI.getMaxOccurences());
		System.out.println(globalReportAPI.getMinOccurences());
		System.out.println(globalReportAPI.getZoneReports().get(0).getZone().get(0));
		System.out.println(globalReportAPI.getZoneReports().get(0).getZone().get(1));
		System.out.println(globalReportAPI.getZoneReports().get(0).getZone().get(2));
		
		globalReportLocal = model.getExhaustiveReportFromLocal("Delphinidae", "src/delphinidaeOccurence.json");
		System.out.println(globalReportLocal.getMaxOccurences());
		System.out.println(globalReportLocal.getMinOccurences());
		System.out.println(globalReportLocal.getZoneReports().get(0).getZone().get(0));
		System.out.println(globalReportLocal.getZoneReports().get(0).getZone().get(1));
		System.out.println(globalReportLocal.getZoneReports().get(0).getZone().get(2));
		
		if(!(globalReportAPI.equals(globalReportLocal))) {
			fail("Global Report diff");
		}
	}
	
	

}
