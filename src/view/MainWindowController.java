package view;

import java.awt.geom.Point2D;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.Timer;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import controller.Controller;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
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
import model.Occurrence;
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
    private Group zonesDisplay;
    private Group root3D;
    private Group legend;
    
    private int animationState;
    
    private ArrayList<GlobalReport> animationReportList;
    
    private int animationReportIndex;
    
    private Timer animationTimer = new Timer(1000,  (e) -> {
		this.displayGlobalReport(getAnimationReportList().get(animationReportIndex));
		animationReportIndex=(animationReportIndex >= getAnimationReportList().size() ? 0 : animationReportIndex+1);
	});
    
    private int geohashPrecision;
	
	
	/** Create a MainWindowController instance
	 * @param controller
	 * @param primaryStage
	 */
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
	private Label geohashPrecisionLabel;
	
	@FXML
	private Slider precisionSlider;
	
	@FXML
	private DatePicker beginDatePicker;
	
	@FXML
	private DatePicker endDatePicker;
		
	@FXML
	private Button viewButton;
	
	/**
	 * Initialize the interface
	 */
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
		subscene.setFill(Color.web("#3b3b3b"));
		
		viewPane.getChildren().addAll(subscene);
		
		legend = new Group();
		viewPane.getChildren().add(legend);
		
		zonesDisplay = new Group();
		root3D.getChildren().add(zonesDisplay);
		
		// Resize the subscene according to viewPane size
		subscene.heightProperty().bind(viewPane.heightProperty());
		subscene.widthProperty().bind(viewPane.widthProperty());
		subscene.setManaged(false);
		
		subscene.addEventHandler(MouseEvent.ANY, event -> {
			if(event.getEventType() == MouseEvent.MOUSE_PRESSED && event.isAltDown()) {
				PickResult pickResult = event.getPickResult();
				Point3D spaceCoord = pickResult.getIntersectedPoint();
				globeClicked(spaceCoord, true, false);
			}
			else if(event.getEventType() == MouseEvent.MOUSE_PRESSED && event.isShiftDown()) {
				PickResult pickResult = event.getPickResult();
				Point3D spaceCoord = pickResult.getIntersectedPoint();
				globeClicked(spaceCoord, false, true);
			}
		});
		
		startReport();
		
//		addBar(root3D, 45,45);
		
		precisionSlider.setSnapToTicks(true);
		
		setViewPropertiesState(false);
		setAnimationControlsState(false);
		
		setupEvents();
		
		setAnimationState(0);
		
	}
	
	/** A click occured on the globe
	 * @param spaceCoord
	 * @param alt was pressed
	 * @param shift was pressed
	 */
	private void globeClicked(Point3D spaceCoord, boolean alt, boolean shift) {
		javafx.geometry.Point2D coord = spaceCoordToGeoCoord(spaceCoord);
		Location location = new Location("", coord.getX(), coord.getY());
		System.out.println(GeoHashHelper.getGeohash(location, getGeohashPrecision()));
		if(alt)
			this.getController().getSpeciesInGeohash(GeoHashHelper.getGeohash(location, getGeohashPrecision()));
		else if(shift)
			this.getController().getOccurrencesDetails(GeoHashHelper.getGeohash(location, getGeohashPrecision()));
	}
	


	/**
	 * Setup actions to do on events
	 */
	private void setupEvents() {
		
		selectButton.setOnAction(event ->  
    	{
    		System.out.println("Select species "+speciesNameField.getText());
    		this.getController().selectSpecies(speciesNameField.getText());
        });
		
		viewButton.setOnAction(event ->  
    	{
    		System.out.println("View "+speciesNameField.getText());
    		this.getController().parameterizedReport(geohashPrecision, beginDatePicker.getValue(), endDatePicker.getValue());
        });
		
		playButton.setOnAction(event ->  
    	{
    		System.out.println("Play");
    		
    		if(getAnimationState()==0) {
    			setAnimationState(1);
    			this.getController().getReportsForAnimation(geohashPrecision, beginDatePicker.getValue(), endDatePicker.getValue());
    		}
    		
        });
		
		precisionSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                    setGeohashPrecision(new_val.intValue());
            }
        });
		
		
		
	}
	
	/**
	 * @return the animationState
	 */
	public int getAnimationState() {
		return animationState;
	}

	/**
	 * @param animationState the animationState to set
	 */
	public void setAnimationState(int animationState) {
		this.animationState = animationState;
	}

	/**
	 * @return the animationReportList
	 */
	public ArrayList<GlobalReport> getAnimationReportList() {
		return animationReportList;
	}

	/**
	 * @param animationReportList the animationReportList to set
	 */
	public void setAnimationReportList(ArrayList<GlobalReport> animationReportList) {
		this.animationReportList = animationReportList;
	}

	/**
	 * @return the geohashPrecision
	 */
	public int getGeohashPrecision() {
		return geohashPrecision;
	}

	/**
	 * @param geohashPrecision the geohashPrecision to set
	 */
	public void setGeohashPrecision(int geohashPrecision) {
		this.geohashPrecision = geohashPrecision;
		geohashPrecisionLabel.setText("Geohash Precision : " + geohashPrecision);
	}
	
	/** Set the enable state of animation controls
	 * @param enabled
	 */
	public void setAnimationControlsState(boolean enabled) {
		boolean disable = !enabled;
		
		animationBeginDateLabel.setText("");
		animationBeginDateLabel.setDisable(disable);
		
		animationEndDateLabel.setText("");
		animationEndDateLabel.setDisable(disable);
		
		playButton.setDisable(disable);
		
		pauseButton.setDisable(disable);
		
		stopButton.setDisable(disable);
	}
	
	public void setPlayable() {
		playButton.setDisable(false);
	}
	
	/** Change the enable state of View Properties
	 * @param enabled
	 */
	private void setViewPropertiesState(boolean enabled) {
		boolean disable = !enabled;
		
		precisionSlider.setDisable(disable);
		
		beginDatePicker.setDisable(disable);
		
		endDatePicker.setDisable(disable);
		
		viewButton.setDisable(disable);
	}
	
	/**
	 * Initialize the interface with local report
	 */
	public void startReport() {
		
		GlobalReport globalReport = this.getController().getExhaustiveReport();
              
        displayGlobalReport(globalReport);
        
        speciesStatus.setText("Delphinidae (local file)");
		setViewPropertiesState(false);
        
		setGeohashPrecision(1);
	}
	
	/** Update table with report zone informations
	 * @param globalReport
	 */
	private void updateTableWithReport(GlobalReport globalReport) {
		table.getColumns().clear();
		
		table.setEditable(false);
		 
        TableColumn<ZoneEntry, String> occurenceCountCol = new TableColumn<ZoneEntry, String>("Count");
        TableColumn<ZoneEntry, String> zonePointsCol = new TableColumn<ZoneEntry, String>("Zone");
        
        
        
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
	}
	
	/** Display the report on the interface
	 * @param globalReport
	 */
	public void displayGlobalReport(GlobalReport globalReport) {
		displayReportOnGlobe(globalReport);
		updateTableWithReport(globalReport);
		speciesStatus.setText(globalReport.getSpecies().getScientificName());
		setViewPropertiesState(true);
	}
	
	/** Update the informations on the globe according to the report
	 * @param globalReport
	 */
	private void displayReportOnGlobe(GlobalReport globalReport) {
		zonesDisplay.getChildren().clear();
		
		float max = globalReport.getMaxOccurences();
        
		float zoneNumber = globalReport.getZoneReports().size();
		float zoneCount = globalReport.getZoneReports().size();
		
        for (ZoneReport zoneReport : globalReport.getZoneReports()) {
			PhongMaterial material = new PhongMaterial();
			
			float colorCoeff = zoneNumber/zoneCount;
			zoneNumber--;
			
			float occurenceCount = zoneReport.getOccurrenceCount();
						
//			System.out.println(occurenceCount+" "+colorCoeff+" "+zoneNumber+" "+zoneCount);
			
			
			material.setDiffuseColor(getColor(colorCoeff).deriveColor(0, 1, 1, 0.2));
			
			float offset=1.18f;
			switch (this.getGeohashPrecision()) {
			case 1:
				offset=1.18f;
				break;
			case 2:
				offset=1.05f;
				break;
			case 3:
				offset=1.02f;
				break;
			case 4:
				offset=1.01f;
				break;

			default:
				break;
			}
			
			addQuadrilateral(zonesDisplay, 
					geoCoordTo3dCoord(zoneReport.getZone().get(0)), 
					geoCoordTo3dCoord(zoneReport.getZone().get(1)), 
					geoCoordTo3dCoord(zoneReport.getZone().get(2)), 
					geoCoordTo3dCoord(zoneReport.getZone().get(3)), 
					material,
					offset
					);
		}
        
        int[] legendText = new int[10];
        
        for (int i = 0; i < 10; i++) {
        	int zoneI = (int) (i/10f*zoneCount);
        	
			legendText[i]=globalReport.getZoneReports().get(zoneI).getOccurrenceCount();
//			System.out.println(zoneI+" "+legendText[i]);
		}
        
        updateLegend(max, legendText);

	}
	
	/** Update the legend
	 * @param max
	 * @param legendText
	 */
	private void updateLegend(float max, int[] legendText) {
		
		int colorBoxHeight = 20;
		
		legend.getChildren().clear();
	      
		Text t = new Text(10, 15, "Legend");
		t.setFont(new Font(13));
		  
		legend.getChildren().addAll(t);
	      
		// Add colors
		for(int color = 9; color>=0; color--) {
			Rectangle colorBox = new Rectangle(25,colorBoxHeight, getColor(color*0.1f));
	      	colorBox.relocate(10,colorBoxHeight*(10-color)+10);
	      	legend.getChildren().add(colorBox);
		}
		
		// Add numbers
		for (int i = 0; i < 10; i++) {
			Text elementText = new Text(11, 15, legendText[i]+"");
			elementText.setFont(new Font(12));
			elementText.relocate(37,colorBoxHeight*i+25);
	      	
	        legend.getChildren().add(elementText);
		}
		
		Text elementText = new Text(11, 15, "0");
		elementText.setFont(new Font(12));
		elementText.relocate(37,colorBoxHeight*10+25);
      	
        legend.getChildren().add(elementText);
	
	}
	
	
	/**
	 * @param parent
	 * @param latitude
	 * @param longitude
	 */
	private void addBar(Group parent, double latitude, double longitude) {
				
		Point3D from = geoCoordTo3dCoord(latitude, longitude);
		Box box = new Box(0.01f,0.01f,1f);

		Point3D to = Point3D.ZERO;
		Point3D yDir = new Point3D(0, 1, 0);

		Group group = new Group();
		Affine affine = new Affine();
		affine.append( lookAt(from,to,yDir));
		group.getTransforms().setAll(affine); 
		group.getChildren().addAll(box);

		parent.getChildren().addAll(group);
	}
	
	
	/** Get the color corresponding to the color coefficient
	 * @param colorCoeff
	 * @return
	 */
	private Color getColor(float colorCoeff) {
		
		Color color = null;
		
		if(colorCoeff>=0.9f)
			color = Color.web("#ff0000");
		else if(colorCoeff>=0.8f)
			color = Color.web("#e86700");
		else if(colorCoeff>=0.7f)
			color = Color.web("#d88500");
		else if(colorCoeff>=0.6f)
			color = Color.web("#c39f00");
		else if(colorCoeff>=0.5f)
			color = Color.web("#adb41c");
		else if(colorCoeff>=0.4f)
			color = Color.web("#94c748");
		else if(colorCoeff>=0.3f)
			color = Color.web("#79d873");
		else if(colorCoeff>=0.2f)
			color = Color.web("#58e69f");
		else if(colorCoeff>=0.1f)
			color = Color.web("#30f3ca");
		else
			color = Color.web("#00fff9");
		
		return color.deriveColor(0, 1, 0.8, 1);
		
	}
	
	/**
	 * @param from
	 * @param to
	 * @param ydir
	 * @return
	 */
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

    /** Get 3D coordinates from geo coordinates
     * @param lat
     * @param lon
     * @return
     */
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
    
    /** Get 3D coordinates from geo coordinates
     * @param point
     * @return
     */
    public static Point3D geoCoordTo3dCoord(Point2D point) {
        return geoCoordTo3dCoord(point.getX(), point.getY());
    }
    
    /** Add a quadrilateral to parent according to provided properties
     * @param parent
     * @param bottomLeft
     * @param topLeft
     * @param topRight
     * @param bottomRight
     * @param material
     * @param offset distance from the globe
     */
    private void addQuadrilateral(Group parent, Point3D bottomLeft, Point3D topLeft, Point3D topRight, Point3D bottomRight, PhongMaterial material, float offset) {
    	
    	
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
	
    
    /** Add a point at provided position
     * @param parent
     * @param position
     * @param material
     */
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

	/**
	 * Notify user a wrong name has been submitted
	 */
	public void wrongSpeciesName() {
		speciesStatus.setText("Scientific name not found.");
		setViewPropertiesState(false);
	}

	public void animate(ArrayList<GlobalReport> globalReportList, LocalDate beginDate, int periodCount) {
		setAnimationReportList(globalReportList);
//		this.animationBeginDateLabel.setText(beginDate+"");
//		this.animationEndDateLabel.setText(beginDate.plusYears(periodCount*5)+"");
		animationPlay();

	}

	private void animationPlay() {
		
		this.setAnimationState(2);
		
		animationTimer.start();

	}
	
	
	/** Get geo coordinates from 3D coordinates
	 * @param p
	 * @return
	 */
	public static javafx.geometry.Point2D spaceCoordToGeoCoord(Point3D p) {    
		  double lat = Math.PI/2.0 - Math.acos(-p.getY());
		  double lon = -Math.atan2(p.getX(), p.getZ());
	  return new javafx.geometry.Point2D(Math.toDegrees(lat) - TEXTURE_LAT_OFFSET, Math.toDegrees(lon) - TEXTURE_LON_OFFSET);
	}

	/** Display provided speciesNames inside the table
	 * @param speciesNames
	 */
	public void displaySpeciesNames(ArrayList<String> speciesNames) {
		table.getColumns().clear();
	
		table.setEditable(false);
		 
        TableColumn<ScientificNameEntry, String> scientificNamesCol = new TableColumn<ScientificNameEntry, String>("Scientific Name");
              
        
        ObservableList<ScientificNameEntry> data = FXCollections.observableArrayList();
        
        for (String speciesName : speciesNames) {
			
			
			data.add(new ScientificNameEntry(speciesName));
			
//			System.out.println(""+zoneReport.getOccurrenceCount()+" "+zoneString);
		}
        
        
        table.setItems(data);
        
        scientificNamesCol.setCellValueFactory(new PropertyValueFactory<>("scientificName"));
        
        table.getColumns().add(scientificNamesCol);
		table.setOnMouseClicked(event ->  
    	{
    		System.out.println( ((ScientificNameEntry)table.getSelectionModel().getSelectedItem()).getScientificName());
    		speciesNameField.setText(((ScientificNameEntry)table.getSelectionModel().getSelectedItem()).getScientificName());
    		
    	});
	}

	/** Display provided occurences inside the table
	 * @param occurrences
	 */
	public void displayOccurrences(ArrayList<Occurrence> occurrences) {
		
		table.getColumns().clear();
		
		table.setEditable(false);
		 
        TableColumn<OccurrenceEntry, String> orderCol = new TableColumn<OccurrenceEntry, String>("Order");
        TableColumn<OccurrenceEntry, String> superclassCol = new TableColumn<OccurrenceEntry, String>("Superclass");
        TableColumn<OccurrenceEntry, String> bathymetryCol = new TableColumn<OccurrenceEntry, String>("Bathymetry");
        TableColumn<OccurrenceEntry, String> shoreDistanceCol = new TableColumn<OccurrenceEntry, String>("Shore distance");
        TableColumn<OccurrenceEntry, String> recordedByCol = new TableColumn<OccurrenceEntry, String>("Recorded by");
        TableColumn<OccurrenceEntry, String> dateCol = new TableColumn<OccurrenceEntry, String>("Date");
        
        ObservableList<OccurrenceEntry> data = FXCollections.observableArrayList();
        
        for (Occurrence occurrence : occurrences) {
						
			data.add(new OccurrenceEntry(occurrence.getOrder(), occurrence.getSuperclass(), occurrence.getBathymetry()+"", occurrence.getShoredistance()+"", occurrence.getRecordedBy(), occurrence.getEventDate()));
			
		}
        
        
        table.setItems(data);
        
        orderCol.setCellValueFactory(new PropertyValueFactory<>("order"));
        superclassCol.setCellValueFactory(new PropertyValueFactory<>("superclass"));
        bathymetryCol.setCellValueFactory(new PropertyValueFactory<>("bathymetry"));
        shoreDistanceCol.setCellValueFactory(new PropertyValueFactory<>("shoreDistance"));
        recordedByCol.setCellValueFactory(new PropertyValueFactory<>("recordedBy"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        
        table.getColumns().addAll(orderCol,superclassCol, bathymetryCol, shoreDistanceCol, recordedByCol, dateCol);
		
		
	}

}
