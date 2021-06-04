package application;
	
import controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Model;
import view.View;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		
		Model model = new Model();
		View view = new View();
		
		Controller controller = new Controller(model, view);
		
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("Sample.fxml"));
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("hello world");
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
