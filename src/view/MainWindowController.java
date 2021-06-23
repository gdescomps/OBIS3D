package view;

import java.awt.geom.Point2D;
import java.awt.image.ColorConvertOp;
import java.net.URL;
import java.text.DecimalFormat;
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
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.GlobalReport;
import model.ZoneReport;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class MainWindowController  extends View implements Initializable{
	
	private static final float TEXTURE_LAT_OFFSET = -0.2f;
    private static final float TEXTURE_LON_OFFSET = 2.8f;
    
    private Group earth;
    private Group root3D;
    private Group legend;
	
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
	
	// Animation controls
	
	@FXML
	private Slider animationDateSlider;
	
	@FXML
	private Label animationBeginDateLabel;
	
	@FXML
	private Label animationEndDateLabel;
	
	@FXML
	private Button playButton;
	
	@FXML
	private Button pauseButton;
	
	@FXML
	private Button stopButton;
	
	
	// View Properties
	
	@FXML
	private TextField speciesNameField;
	
	@FXML
	private Button searchButton;
	
	@FXML
	private Button selectButton;
	
	@FXML
	private Label speciesStatus;
	
	@FXML
	private TextField latitudeField;
	
	@FXML
	private TextField longitudeField;
	
	@FXML
	private Slider precisionSlider;
	
	@FXML
	private DatePicker beginDatePicker;
	
	@FXML
	private DatePicker intervalPicker;
	
	@FXML
	private TextField intervalCountField;
	
	@FXML
	private Button viewButton;
	
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
		
		legend = new Group();
		viewPane.getChildren().add(legend);
		
		
		// Resize the subscene according to viewPane size
		subscene.heightProperty().bind(viewPane.heightProperty());
		subscene.widthProperty().bind(viewPane.widthProperty());
		subscene.setManaged(false);
		
		
		startReport();
		
//		addBar(root3D, 45,45);
		
		setViewPropertiesState(false);
		setAnimationControlsState(false);
	}
	
	private void setAnimationControlsState(boolean enabled) {
		boolean disable = !enabled;
		
		animationDateSlider.setDisable(disable);
		
		animationBeginDateLabel.setText("");
		animationBeginDateLabel.setDisable(disable);
		
		animationEndDateLabel.setText("");
		animationEndDateLabel.setDisable(disable);
		
		playButton.setDisable(disable);
		
		pauseButton.setDisable(disable);
		
		stopButton.setDisable(disable);
	}
	
	private void setViewPropertiesState(boolean enabled) {
		boolean disable = !enabled;
		
		latitudeField.setDisable(disable);
		
		longitudeField.setDisable(disable);
		
		precisionSlider.setDisable(disable);
		
		beginDatePicker.setDisable(disable);
		
		intervalPicker.setDisable(disable);
		
		intervalCountField.setDisable(disable);
		
		viewButton.setDisable(disable);
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
//        System.out.println(globalReport.getZoneReports().size());
        
        displayReportOnGlobe(globalReport);
        
	}
	
	
	private void displayReportOnGlobe(GlobalReport globalReport) {
		float max = globalReport.getMaxOccurences();
        
		
        for (ZoneReport zoneReport : globalReport.getZoneReports()) {
			PhongMaterial material = new PhongMaterial();
			
			float colorCoeff = zoneReport.getOccurrenceCount()/globalReport.getMaxOccurences();
			float occurenceCount = zoneReport.getOccurrenceCount();
			
//			Logarithmic color scale : 
//			occurenceCount 	-> colorCoeff
//
//			max     		-> 1
//			max/2   		-> 0.8
//			max/2^2 		-> 0.6
//			max/2^3 		-> 0.4
//			max/2^4 		-> 0.2
			for (int n = 4; n >= 0; n--) {
				if(occurenceCount < max / Math.pow(2,n)) {

					float a = (float) (0.2f/ (max/Math.pow(2,n)));
					float x = (float) (occurenceCount - (max/Math.pow(2,n)));
					float b = 1f-(0.2f*n);
					
					colorCoeff = a*x+b;
					
					break;
				}
			}

			
//			System.out.println(occurenceCount+" "+colorCoeff);
			
			
			material.setDiffuseColor(getColor(colorCoeff).deriveColor(1, 1, 1, 0.2));
			
			addQuadrilateral(root3D, 
					geoCoordTo3dCoord(zoneReport.getZone().get(0)), 
					geoCoordTo3dCoord(zoneReport.getZone().get(1)), 
					geoCoordTo3dCoord(zoneReport.getZone().get(2)), 
					geoCoordTo3dCoord(zoneReport.getZone().get(3)), 
					material);
		}
        
        updateLegend(max);

	}
	
	private void updateLegend(float max) {
		
		legend.getChildren().clear();
        
	//      Rectangle background = new Rectangle(100,100,Color.color(1,1,1,0.2));
	      
	      Text t = new Text(10, 15, "Legend");
	      t.setFont(new Font(13));
	      
	      legend.getChildren().addAll(t);
	      
	//		max     -> 1
	//		max/2   -> 0.8
	//		max/2^2 -> 0.6
	//		max/2^3 -> 0.4
	//		max/2^4 -> 0.2
	      for (int n = 0; n <= 4; n++) {
	      	Rectangle colorBox = new Rectangle(35,12, getColor(1f-(0.2f*n)));
	      	colorBox.relocate(10,12*n+20);
	      	
	      	Text elementText = new Text(11, 15, new DecimalFormat("#").format(max/Math.pow(2,n)));
	          elementText.setFont(new Font(12));
	          elementText.relocate(50,12*n+20);
	      	
	          legend.getChildren().addAll(colorBox, elementText);
	      }
	      int i=0;
	      for(float x=0.01f; x>0.001; x=x-0.002f) {
	      	
	      
		        float a = (float) (0.2f/ (max/Math.pow(2,4)));
				float c = (float) (max/Math.pow(2,4));
				float b = 1f-(0.2f*4);
				float occurenceCount = (x - b) / a + c;
				
				Rectangle colorBox = new Rectangle(35,12, getColor(x));
		    	colorBox.relocate(10,12*i+12*5+20);
		    	
		    	Text elementText = new Text(11, 15, new DecimalFormat("#").format(occurenceCount));
		        elementText.setFont(new Font(12));
		        elementText.relocate(50,12*i+12*5+20);
		    	
		        legend.getChildren().addAll(colorBox, elementText);
		    	
		    	i++;
	      }
	      
	      Rectangle colorBox = new Rectangle(35,12, getColor(0));
	  	colorBox.relocate(10,12*i+12*5+20);
	  	
	  	Text elementText = new Text(11, 15, "~0");
	      elementText.setFont(new Font(12));
	      elementText.relocate(50,12*i+12*5+20);
	  	
	      legend.getChildren().addAll(colorBox, elementText);
	}
	
	private void addBar(Group root3d2, double latitude, double longitude) {
				
		Point3D from = geoCoordTo3dCoord(latitude, longitude);
		Box box = new Box(0.01f,0.01f,1f);

		Point3D to = Point3D.ZERO;
		Point3D yDir = new Point3D(0, 1, 0);

		Group group = new Group();
		Affine affine = new Affine();
		affine.append( lookAt(from,to,yDir));
		group.getTransforms().setAll(affine); 
		group.getChildren().addAll(box);

		root3d2.getChildren().addAll(group);
	}
	
	
	private Color getColor(float colorCoeff) {
		float redCoeff = colorCoeff > 0.01 ? colorCoeff : 0;
		float greenCoeff = colorCoeff <= 0.01 ? 1-colorCoeff*100 : 0;
		float blueCoeff = colorCoeff > 0.01 ? 1-colorCoeff : 0.2f; 
		
		return Color.color(redCoeff, greenCoeff, blueCoeff);
	}
	
	private static Affine lookAt(Point3D from, Point3D to, Point3D ydir) {
	    Point3D zVec = to.subtract(from).normalize();
	    Point3D xVec = ydir.normalize().crossProduct(zVec).normalize();
	    Point3D yVec = zVec.crossProduct(xVec).normalize();
	    return new Affine(xVec.getX(), yVec.getX(), zVec.getX(), from.getX(),
	                      xVec.getY(), yVec.getY(), zVec.getY(), from.getY(),
	                      xVec.getZ(), yVec.getZ(), zVec.getZ(), from.getZ());
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
