package model;

public class Species {
	private String scientificName;
	

	public Species(String scientificName) {
		super();
		this.scientificName = scientificName;
	}

	/**
	 * @return the scientificName
	 */
	public String getScientificName() {
		return scientificName;
	}

	/**
	 * @param scientificName the scientificName to set
	 */
	public void setScientificName(String scientificName) {
		this.scientificName = scientificName;
	}
	
	@Override
	public String toString() {
		return "Species name = " + scientificName;
	}

}
