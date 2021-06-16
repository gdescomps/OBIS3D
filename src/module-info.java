module OBIS3D {
	requires javafx.controls;
	requires javafx.fxml;
//	requires org.junit.jupiter.api;
	requires java.net.http;
	//requires jsonassert;
   	requires java.desktop;
	requires org.json;
	requires org.junit.jupiter.api;
	
	opens application to javafx.graphics;
	opens view to javafx.fxml;
}

