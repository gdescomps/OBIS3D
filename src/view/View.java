package view;

import controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.GlobalReport;

public class View {
	
	protected Controller controller;

	public View(Controller controller, Stage primaryStage) {
		
		this.setController(controller);
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
			  
			  //On définit la vue comme contrôleur de ce fichier
			  loader.setController(this);
			  
			  //On charge le fichier FXML, il appellera la méthode *initialize()* de la vue
			  Parent root = loader.load();
			  
			Scene scene = new Scene(root,800,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}

	/**
	 * @return the controller
	 */
	public Controller getController() {
		return controller;
	}

	/**
	 * @param controller the controller to set
	 */
	public void setController(Controller controller) {
		this.controller = controller;
		this.controller.setView(this);
	}


}
