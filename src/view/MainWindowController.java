package view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.stage.Stage;

public class MainWindowController  extends View implements Initializable{
	
	public MainWindowController(Stage primaryStage) {
		super(primaryStage);
	}

	@FXML
	private Pane viewPane;
	
	@FXML
	private AnchorPane window;
	
	@SuppressWarnings("rawtypes")
	@FXML
	private TableView table;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
//		table = new TableView();
		

        
        startReport();
        
		
		//Create a Pane et graph scene root for the 3D content
        Group root3D = new Group();

        //Create cube shape
        Box cube = new Box(1, 1, 1);

        //Create Material
        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.BLUE);
        blueMaterial.setSpecularColor(Color.BLUE);
        //Set it to the cube
        cube.setMaterial(blueMaterial);

        //Add the cube to this node
        root3D.getChildren().add(cube);

        //Add a camera group
        PerspectiveCamera camera = new PerspectiveCamera(true);
        
        
        new CameraManager(camera, viewPane, root3D);
        
		
		SubScene subscene = new SubScene(root3D,0,0,true,SceneAntialiasing.BALANCED);
		
		System.out.println(viewPane.getWidth()+" "+viewPane.getHeight());
		subscene.setCamera(camera);
		subscene.setFill(Color.GREY);
		
		viewPane.getChildren().addAll(subscene);
		
		// Resize the subscene according to viewPane size
		subscene.heightProperty().bind(viewPane.heightProperty());
		subscene.widthProperty().bind(viewPane.widthProperty());
		subscene.setManaged(false);
	
	}
	
	public void startReport() {
		table.setEditable(true);
		 
        TableColumn<ZoneEntry, String> occurenceCountCol = new TableColumn<ZoneEntry, String>("Occurences count");
        TableColumn<ZoneEntry, String> zonePointsCol = new TableColumn<ZoneEntry, String>("Zone points");
        
        final ObservableList<ZoneEntry> data = FXCollections.observableArrayList(
    	    new ZoneEntry("24", "1,2,3,4")
    	);
        
//        this.getController().getExhaustiveReport();
        
        
        table.setItems(data);
        
        occurenceCountCol.setCellValueFactory(new PropertyValueFactory<>("occurenceCount"));
        zonePointsCol.setCellValueFactory(new PropertyValueFactory<>("zonePoints"));
        
        table.getColumns().addAll(occurenceCountCol, zonePointsCol);
	}
	
}
