package model;

import java.util.ArrayList;

public class GlobalReport {
	private Species species;
	private int minOccurences ;
	private int maxOccurences ;
	ArrayList<ZoneReport> zoneReports = new ArrayList<ZoneReport>();
	
	/// Construtors :
	
	/**
	 * @constructor GlobalReport
	 */
	public GlobalReport(Species species) {
		this.species = species;
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

	
	
	/// Setters :

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


	
	/// Getters :
	
	/**
	 * @return the maxOccurences
	 */
	public int getMaxOccurences() {
		return maxOccurences;
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
		return minOccurences;
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



	
	
	
	
	

}
