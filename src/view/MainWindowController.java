package view;

import java.net.URL;
import java.util.ResourceBundle;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import controller.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.AmbientLight;
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
import javafx.scene.shape.MeshView;
import javafx.stage.Stage;

public class MainWindowController  extends View implements Initializable{
	
	private static final float TEXTURE_LAT_OFFSET = -0.2f;
    private static final float TEXTURE_LON_OFFSET = 2.8f;
    
    private Group earth;
	
	public MainWindowController(Controller controller, Stage primaryStage) {
		super(controller, primaryStage);
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
		
        
        startReport();
        
		
		//Create a Pane et graph scene root for the 3D content
        Group root3D = new Group();

        
        // Load geometry
        ObjModelImporter objImporter = new ObjModelImporter();
        try {
            URL modelUrl = this.getClass().getResource("Earth/earth.obj");
            objImporter.read(modelUrl);
        } catch (ImportException e) {
            // handle exception
            System.out.println(e.getMessage());
        }
        MeshView[] meshViews = objImporter.getImport();
        earth = new Group(meshViews);
        
        root3D.getChildren().add(earth);
        root3D.setFocusTraversable(true);
        

        //Add a camera group
        PerspectiveCamera camera = new PerspectiveCamera(true);
        new CameraManager(camera, viewPane, root3D);
        
        // Add point light
        PointLight light = new PointLight(Color.WHITE);
        light.setTranslateX(-180);
        light.setTranslateY(-90);
        light.setTranslateZ(-120);
        light.getScope().addAll(root3D);
        root3D.getChildren().add(light);

        // Add ambient light
        AmbientLight ambientLight = new AmbientLight(Color.WHITE);
        ambientLight.getScope().addAll(root3D);
        root3D.getChildren().add(ambientLight);
		
		
        SubScene subscene = new SubScene(root3D,0,0,true,SceneAntialiasing.BALANCED);
		
		subscene.setCamera(camera);
		subscene.setFill(Color.GREY);
		
		viewPane.getChildren().addAll(subscene);
		
		
		
		// Resize the subscene according to viewPane size
		subscene.heightProperty().bind(viewPane.heightProperty());
		subscene.widthProperty().bind(viewPane.widthProperty());
		subscene.setManaged(false);
	
	}
	
	public void startReport() {
		table.setEditable(false);
		 
        TableColumn<ZoneEntry, String> occurenceCountCol = new TableColumn<ZoneEntry, String>("Count");
        TableColumn<ZoneEntry, String> zonePointsCol = new TableColumn<ZoneEntry, String>("Zone");
        
        
        ObservableList<ZoneEntry> data = this.getController().getExhaustiveReport();
        
        
        table.setItems(data);
        
        occurenceCountCol.setCellValueFactory(new PropertyValueFactory<>("occurenceCount"));
        zonePointsCol.setCellValueFactory(new PropertyValueFactory<>("zonePoints"));
        
        table.getColumns().addAll(occurenceCountCol, zonePointsCol);
	}
	
}
