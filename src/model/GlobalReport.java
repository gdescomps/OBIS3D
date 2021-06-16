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
	 * @param zoneReports the zoneReports to set
	 */
	public void addZoneReport(ZoneReport zoneReport) {
		this.zoneReports.add(zoneReport);
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
	
	
	
	

}
