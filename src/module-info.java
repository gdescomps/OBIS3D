module OBIS3D {
	requires javafx.controls;
	requires javafx.fxml;
	requires org.junit.jupiter.api;
	requires org.json;
	requires java.net.http;
	
	opens application to javafx.graphics, javafx.fxml;
}
