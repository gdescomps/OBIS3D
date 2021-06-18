package model;

import java.util.ArrayList;

public class GlobalReport {
	private Species species;

	private int minOccurrences;
	private int maxOccurrences;

	ArrayList<ZoneReport> zoneReports = new ArrayList<ZoneReport>();
	
	/// Construtors :
	
	/**
	 * @constructor GlobalReport
	 */
	public GlobalReport(Species species) {
		this.species = species;
	}
	
	/**
	 */
	public Species getSpecies() {
		return this.species;
	}
	
	/**
	 * @constructor GlobalReport
	 */
	public GlobalReport() {
	}


	/**
	 * @param zoneReports the zoneReports to set
	 */
	public void addZoneReport(ZoneReport zoneReport) {
		this.zoneReports.add(zoneReport);
	}
	
	/**
	 * @return zoneReports 
	 */
	public ArrayList<ZoneReport> getZoneReport() {
		return this.zoneReports;
	}


	
	
	/// Setters :

	/**
	 * @param minOccurrences the minOccurrences to set
	 */
	public void setMinOccurrences(int minOccurrences) {
		this.minOccurrences = minOccurrences;
	}


	/**
	 * @param maxOccurrences the maxOccurrences to set
	 */
	public void setMaxOccurrences(int maxOccurrences) {
		this.maxOccurrences = maxOccurrences;
	}


	
	/// Getters :
	
	/**
	 * @return the maxOccurences
	 */
	public int getMaxOccurences() {
		return maxOccurrences;
	}


	/**
	 * @return the zoneReports
	 */
	public ArrayList<ZoneReport> getZoneReports() {
		return zoneReports;
	}
	
	/**
	 * @return the minOccurences
	 */
	public int getMinOccurences() {
		return minOccurrences;
	}
	
	
	
	/// Overrides :
	
	@Override
    public boolean equals(Object globalReport) {
		if (this.getMaxOccurences() != ((GlobalReport) globalReport).getMaxOccurences()) {
			System.out.println("Different MaxOccurences");
			return false;
		}
		if (this.getMinOccurences() != ((GlobalReport) globalReport).getMinOccurences()) {
			System.out.println("Different MinOccurences");
			return false;
		}
		for (int i = 0; i<this.zoneReports.size(); i++) {
			if(!(this.zoneReports.get(i).equals(((GlobalReport) globalReport).getZoneReports().get(i)))) {
				System.out.println("Different zones");
				 return false;
			}
		}
		return true;
	}



	
	
	/**
	 * @return minOccurrences the minOccurrences to get
	 */
	public int getMinOccurrences() {
		return this.minOccurrences;
	}


	/**
	 * @return maxOccurrences the maxOccurrences to get
	 */
	public int getMaxOccurrences() {
		return this.maxOccurrences;
	}
	
	@Override
	public String toString() {
		String result;
		result = species + " \nmin occurrence = " + minOccurrences + " \nmax occurrence = " + maxOccurrences ;
		result += "\nzone report =" + zoneReports;
		return result;
	}
	

}
