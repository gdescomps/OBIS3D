package model;

public class Occurrence {

	private String order="unknown";
	private String superClass="unknown";
	private String recordedBy="unknown";
	private Species species;
	private float bathymetry=-1;
	private int shoredistance=-1;
	private String eventDate="unknown";
	
	/**
	 * @return the species the species to get
	 */
	public Species getSpecies() {
		return species;
	}

	/**
	 * @param species the species to set
	 */
	public void setSpecies(Species species) {
		this.species = species;
	}

	/**
	 * @return the order the order to get
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(String order) {
		this.order = order;
	}

	
	/**
	 * @return superClass the superClass to get
	 */
	public String getSuperclass() {
		return superClass;
	}

	/**
	 * 
	 * @param superClass the superClass to get
	 */
	public void setSuperclass(String superclass) {
		this.superClass = superclass;
	}

	/**
	 * @return recordedBy  the recordedBy to get
	 */
	public String getRecordedBy() {
		return recordedBy;
	}

	/**
	 * @param recordedBy the recordedBy to set
	 */
	public void setRecordedBy(String recordedBy) {
		this.recordedBy = recordedBy;
	}
	
	@Override
	public String toString() {
		return "\nOrder = " + getOrder() +"\nSuper class = " + getSuperclass() + "\nRecorded by = " +getRecordedBy() + getSpecies() +"\n";
	}

	/**
	 * @return bathymetry the bathymetry to get
	 */
	public float getBathymetry() {
		return this.bathymetry;
	}

	/**
	 * @param bathymetry the bathymetry to set
	 */
	public void setBathymetry(float bathymetry) {
		this.bathymetry = bathymetry;
	}

	/**
	 * @return the shoredistance to get
	 */
	public int getShoredistance() {
		return shoredistance;
	}

	/**
	 * @param shoredistance the shoredistance to set
	 */
	public void setShoredistance(int shoredistance) {
		this.shoredistance = shoredistance;
	}

	/**
	 * @return eventDate the eventDate to get
	 */
	public String getEventDate() {
		return eventDate;
	}

	/**
	 * 
	 * @param eventDate the eventDate to set
	 */
	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}
}
