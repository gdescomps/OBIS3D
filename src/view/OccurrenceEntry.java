package view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableColumn;


public class OccurrenceEntry {
	private StringProperty order = new SimpleStringProperty();
	public void setOrder(String value) { orderProperty().set(value); }
    public String getOrder() { return orderProperty().get(); }
    public StringProperty orderProperty() {
        if (order == null) order = new SimpleStringProperty(this, "order");
        return order;
    }
    
    private StringProperty superclass = new SimpleStringProperty();
	public void setSuperclass(String value) { superclassProperty().set(value); }
    public String getSuperclass() { return superclassProperty().get(); }
    public StringProperty superclassProperty() {
        if (superclass == null) superclass = new SimpleStringProperty(this, "superclass");
        return superclass;
    }
    
    private StringProperty bathymetry = new SimpleStringProperty();
	public void setBathymetry(String value) { bathymetryProperty().set(value); }
    public String getBathymetry() { return bathymetryProperty().get(); }
    public StringProperty bathymetryProperty() {
        if (bathymetry == null) bathymetry = new SimpleStringProperty(this, "bathymetry");
        return bathymetry;
    }
    
    private StringProperty shoreDistance = new SimpleStringProperty();
	public void setShoreDistance(String value) { shoreDistanceProperty().set(value); }
    public String getShoreDistance() { return shoreDistanceProperty().get(); }
    public StringProperty shoreDistanceProperty() {
        if (shoreDistance == null) shoreDistance = new SimpleStringProperty(this, "shoreDistance");
        return shoreDistance;
    }
    
    private StringProperty recordedBy = new SimpleStringProperty();
	public void setRecordedBy(String value) { recordedByProperty().set(value); }
    public String getRecordedBy() { return recordedByProperty().get(); }
    public StringProperty recordedByProperty() {
        if (recordedBy == null) recordedBy = new SimpleStringProperty(this, "recordedBy");
        return recordedBy;
    }
    
    
    
    private StringProperty date = new SimpleStringProperty();
	public void setDate(String value) { dateProperty().set(value); }
    public String getDate() { return dateProperty().get(); }
    public StringProperty dateProperty() {
        if (date == null) date = new SimpleStringProperty(this, "date");
        return date;
    }
	
	
	public OccurrenceEntry(String order, String superclass, String bathymetry, String shoreDistance, String recordedBy, String date) {
		this.order.set(order);
		this.bathymetry.set(bathymetry);
		this.superclass.set(superclass);
		this.shoreDistance.set(shoreDistance);
		this.recordedBy.set(recordedBy);
		this.date.set(date);
	}
}
