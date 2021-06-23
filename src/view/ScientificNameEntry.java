package view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ScientificNameEntry {
	private StringProperty scientificName = new SimpleStringProperty();
	public void setScientificName(String value) { scientificNameProperty().set(value); }
    public String getScientificName() { return scientificNameProperty().get(); }
    public StringProperty scientificNameProperty() {
        if (scientificName == null) scientificName = new SimpleStringProperty(this, "scientificName");
        return scientificName;
    }
	
	
	public ScientificNameEntry(String scientificName) {
		this.scientificName.set(scientificName);
	}
}
