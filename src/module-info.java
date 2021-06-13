module OBIS3D {
	requires javafx.controls;
	requires javafx.fxml;
	requires org.junit.jupiter.api;
	requires org.json;
	requires java.net.http;
	requires java.desktop;
	
	opens application to javafx.graphics, javafx.fxml;
}
