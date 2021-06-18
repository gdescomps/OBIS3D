package model;

import java.util.ArrayList;

public class GlobalReport {
	private Species species;
	private int minOccurrences;
	private int maxOccurrences;
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
