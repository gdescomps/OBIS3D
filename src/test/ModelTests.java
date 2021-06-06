package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
	@DisplayName("Fail Test")
	void failTest() {
		if(true) {
			fail("Description of custom failure");
		}
	}

}

