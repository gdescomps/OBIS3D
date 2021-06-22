package view;

import java.awt.geom.Point2D;
import java.awt.image.ColorConvertOp;
import java.net.URL;
import java.util.ResourceBundle;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import controller.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
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
import javafx.scene.shape.CullFace;
import javafx.scene.shape.MeshView;
import javafx.stage.Stage;
import model.GlobalReport;
import model.ZoneReport;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class MainWindowController  extends View implements Initializable{
	
	private static final float TEXTURE_LAT_OFFSET = -0.2f;
    private static final float TEXTURE_LON_OFFSET = 2.8f;
    
    private Group earth;
    private Group root3D;
	
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
		
        
        
        
		
		//Create a Pane et graph scene root for the 3D content
        root3D = new Group();

        
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
		
		
		startReport();
	}
	
	public void startReport() {
		table.setEditable(false);
		 
        TableColumn<ZoneEntry, String> occurenceCountCol = new TableColumn<ZoneEntry, String>("Count");
        TableColumn<ZoneEntry, String> zonePointsCol = new TableColumn<ZoneEntry, String>("Zone");
        
        GlobalReport globalReport = this.getController().getExhaustiveReport();
        
        ObservableList<ZoneEntry> data = FXCollections.observableArrayList();
        
        for (ZoneReport zoneReport : globalReport.getZoneReports()) {
			
			String zoneString = "";
			
			for (Point2D point : zoneReport.getZone()) {
				zoneString+="["+point.getX()+", "+point.getY()+"]"; 
			}
			
			data.add(new ZoneEntry(""+zoneReport.getOccurrenceCount(), zoneString));
			
//			System.out.println(""+zoneReport.getOccurrenceCount()+" "+zoneString);
		}
        
        
        table.setItems(data);
        
        occurenceCountCol.setCellValueFactory(new PropertyValueFactory<>("occurenceCount"));
        zonePointsCol.setCellValueFactory(new PropertyValueFactory<>("zonePoints"));
        
        table.getColumns().addAll(occurenceCountCol, zonePointsCol);
        
        
//        System.out.println(globalReport.getMinOccurrences()+" "+globalReport.getMaxOccurrences());
        
        for (ZoneReport zoneReport : globalReport.getZoneReports()) {
			PhongMaterial material = new PhongMaterial();

			float colorCoeff = zoneReport.getOccurrenceCount()/globalReport.getMaxOccurences();
			material.setDiffuseColor(Color.color(colorCoeff, 0, 1-colorCoeff, 0.2));
			
			addQuadrilateral(root3D, 
					geoCoordTo3dCoord(zoneReport.getZone().get(0)), 
					geoCoordTo3dCoord(zoneReport.getZone().get(1)), 
					geoCoordTo3dCoord(zoneReport.getZone().get(2)), 
					geoCoordTo3dCoord(zoneReport.getZone().get(3)), 
					material);
		}
        
	}
	
	
	// From Rahel Lüthy : https://netzwerg.ch/blog/2015/03/22/javafx-3d-line/
    public Cylinder createLine(Point3D origin, Point3D target) {
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = target.subtract(origin);
        double height = diff.magnitude();

        Point3D mid = target.midpoint(origin);
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

        Cylinder line = new Cylinder(0.01f, height);

        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);

        return line;
    }

    public static Point3D geoCoordTo3dCoord(double lat, double lon) {
        double lat_cor = lat + TEXTURE_LAT_OFFSET;
        double lon_cor = lon + TEXTURE_LON_OFFSET;
        return new Point3D(
                -java.lang.Math.sin(java.lang.Math.toRadians(lon_cor))
                        * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)),
                -java.lang.Math.sin(java.lang.Math.toRadians(lat_cor)),
                java.lang.Math.cos(java.lang.Math.toRadians(lon_cor))
                        * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)));
    }
    
    public static Point3D geoCoordTo3dCoord(Point2D point) {
        return geoCoordTo3dCoord(point.getX(), point.getY());
    }
    
    private void addQuadrilateral(Group parent, Point3D bottomLeft, Point3D topLeft, Point3D topRight, Point3D bottomRight, PhongMaterial material) {
    	
    	float offset = 1.18f;
    	
    	final TriangleMesh triangleMesh = new TriangleMesh();
    	
    	
    	final float[] points = {
    			(float)topRight.getX()*offset, (float)topRight.getY()*offset, (float)topRight.getZ()*offset,
    			(float)topLeft.getX()*offset, (float)topLeft.getY()*offset, (float)topLeft.getZ()*offset,
    			(float)bottomLeft.getX()*offset, (float)bottomLeft.getY()*offset, (float)bottomLeft.getZ()*offset,
    			(float)bottomRight.getX()*offset, (float)bottomRight.getY()*offset, (float)bottomRight.getZ()*offset
    	};
    	
    	final float[] texCoords = {
    			1,1,
    			1,0,
    			0,1,
    			0,0
    	};
    	
    	final int[] faces = {
    			0,1,1,0,2,2,
    			0,1,2,2,3,3
    	};
    	
    	triangleMesh.getPoints().setAll(points);
    	triangleMesh.getTexCoords().setAll(texCoords);
    	triangleMesh.getFaces().setAll(faces);
    	
    	final MeshView meshView = new MeshView(triangleMesh);
    	meshView.setMaterial(material);
    	meshView.setCullFace(CullFace.NONE);
    	parent.getChildren().addAll(meshView);
    }
	
    
    private void addPoint(Group parent, Point3D position, PhongMaterial material) {
    	Sphere citySphere = new Sphere(0.01);
        
        //Set it to the cube
        citySphere.setMaterial(material);
        
        Group city = new Group(citySphere);
        
        city.setTranslateX(position.getX());
        city.setTranslateY(position.getY());
        city.setTranslateZ(position.getZ());
        
        parent.getChildren().add(city);
    }
}
