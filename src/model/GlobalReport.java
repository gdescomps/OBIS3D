package model;

import java.util.ArrayList;

public class GlobalReport {
	private Species species;
	private int minOccurences;
	private int maxOccurences;
	ArrayList<ZoneReport> zoneReports = new ArrayList<ZoneReport>();
	
	
	
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



	/**
	 * @param minOccurences the minOccurences to set
	 */
	public void setMinOccurences(int minOccurences) {
		this.minOccurences = minOccurences;
	}


	/**
	 * @param maxOccurences the maxOccurences to set
	 */
	public void setMaxOccurences(int maxOccurences) {
		this.maxOccurences = maxOccurences;
	}
	
	/**
	 * @return minOccurences the minOccurences to get
	 */
	public int getMinOccurences() {
		return this.minOccurences;
	}


	/**
	 * @return maxOccurences the maxOccurences to get
	 */
	public int getMaxOccurences() {
		return this.maxOccurences;
	}
	
	@Override
	public String toString() {
		String result;
		result = species + " \nmin occurence = " + minOccurences + " \nmax occurence = " + maxOccurences ;
		result += "\nzone report =" + zoneReports;
		return result;
	}
	

}
