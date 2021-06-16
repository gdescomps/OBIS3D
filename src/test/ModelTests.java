package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import model.GlobalReport;
import model.Model;

class ModelTests {

	@Test
	@DisplayName("Test Hello World")
	void testHelloWorld() {
		Model model = new Model();
		
		assertEquals("Hello World", model.helloWorld());
	}
	
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
	
	@Test
	@DisplayName("Test READ FILE")
	void testReadFile() throws Exception {
		Model model = new Model();
		GlobalReport globalReportLocal = new GlobalReport();
		GlobalReport globalReportAPI = new GlobalReport();
		
		globalReportAPI= model.getExhaustiveReport("Delphinidae");
		globalReportLocal = model.getExhaustiveReportFromLocal("Delphinidae", "src/delphinidaeOccurence.json");
		
		assertSame(globalReportAPI,globalReportLocal,"Identiques");
	}
	
	

}

