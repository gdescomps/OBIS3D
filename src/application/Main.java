package application;
	
import controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Model;
import view.MainWindowController;
import view.View;



public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		
		System.out.println("Starting...");
		
		Controller controller = new Controller();
		
		Model model = new Model(controller);
		View view = new MainWindowController(controller, primaryStage);
		
		
		
		
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
		
}

