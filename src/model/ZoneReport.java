package model;

import java.awt.geom.Point2D;
import java.util.ArrayList;



public class ZoneReport {
	private ArrayList<Point2D> zone = new ArrayList<Point2D>(5);
	private int occurenceCount;
	private ArrayList<Occurence> occurences = new ArrayList<Occurence>();
	

	
	
	/**
	 * @param zone
	 * @param occurenceCount
	 */
	public ZoneReport(ArrayList<Point2D> zone, int occurenceCount) {
		super();
		this.zone = zone;
		this.occurenceCount = occurenceCount;
	}



	/**
	 * @param zone
	 * @param occurenceCount
	 * @param occurences
	 */
	public ZoneReport(ArrayList<Point2D> zone, int occurenceCount, ArrayList<Occurence> occurences) {
		this.zone = zone;
		this.occurenceCount = occurenceCount;
		this.occurences = occurences;
	}

/// Setters :

	/**
	 * @param zone the zone to set
	 */
	public void setZone(ArrayList<Point2D> zone) {
		this.zone = zone;
	}
	/**
	 * @param occurenceCount the occurenceCount to set
	 */
	public void setOccurenceCount(int occurenceCount) {
		this.occurenceCount = occurenceCount;
	}
	/**
	 * @param occurences the occurences to set
	 */
	public void setOccurences(ArrayList<Occurence> occurences) {
		this.occurences = occurences;
	}
	
	
	
/// Getters :
	
	/**
	 * @return the zone
	 */
	public ArrayList<Point2D> getZone() {
		return zone;
	}


	/**
	 * @return the occurenceCount
	 */
	public int getOccurenceCount() {
		return occurenceCount;
	}
	

	/**
	 * @return the occurences
	 */
	public ArrayList<Occurence> getOccurences() {
		return occurences;
	}

	
	
	/// Overrides :
	
	@Override
    public boolean equals (Object zoneReport) {
		if (this.getOccurenceCount() != ((ZoneReport) zoneReport).getOccurenceCount()) {
			System.out.println("Different OccurenceCount");
			return false;
		} 
		
		for (int i = 0; i<this.zone.size(); i++) {
			if (!(this.zone.get(i).equals(((ZoneReport) zoneReport).getZone().get(i)))) {
				System.out.println("Different Points2D included in Zone");
				return  false;
			} 
		}
		return true;
	}

	

}
