package view;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class ZoneEntry {
	private StringProperty occurenceCount = new SimpleStringProperty();
	public void setOccurenceCount(String value) { occurenceCountProperty().set(value); }
    public String getOccurenceCount() { return occurenceCountProperty().get(); }
    public StringProperty occurenceCountProperty() {
        if (occurenceCount == null) occurenceCount = new SimpleStringProperty(this, "occurenceCount");
        return occurenceCount;
    }
	
	private StringProperty zonePoints = new SimpleStringProperty();
	public void setZonePoints(String value) { zonePointsProperty().set(value); }
    public String getZonePoints() { return zonePointsProperty().get(); }
    public StringProperty zonePointsProperty() {
        if (zonePoints == null) zonePoints = new SimpleStringProperty(this, "zonePoints");
        return zonePoints;
    }
	
	public ZoneEntry(String occurenceCount, String zonePoints) {
		this.occurenceCount.set(occurenceCount);
		this.zonePoints.set(zonePoints);
	}
	
    
	
	
}
