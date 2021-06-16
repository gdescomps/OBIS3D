package application;
	
import controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Model;
import view.View;



public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		
		System.out.println("Starting...");
		
		Model model = new Model();
		View view = new View(primaryStage);
		
		Controller controller = new Controller(model, view);
		
		
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
		
}

