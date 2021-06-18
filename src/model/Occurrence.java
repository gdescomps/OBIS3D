package model;

public class Occurrence {

	private String order="unknown";
	private String superClass="unknown";
	private String recordedBy="unknown";
	private Species species;

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
	 * @return recordedBy 
	 */
	public String getRecordedBy() {
		return recordedBy;
	}

	/**
	 * @param recordedBy
	 */
	public void setRecordedBy(String recordedBy) {
		this.recordedBy = recordedBy;
	}
	
	@Override
	public String toString() {
		return "\nOrder = " + getOrder() +"\nSuper class = " + getSuperclass() + "\nRecorded by = " +getRecordedBy() + getSpecies() +"\n";
	}
}
