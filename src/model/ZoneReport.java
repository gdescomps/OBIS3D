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

	/**
	 * @param zone the zone to set
	 */
	public void setZone(ArrayList<Point2D> zone) {
		this.zone = zone;
	}
	
	/**
	 * @return the zone
	 */
	public ArrayList<Point2D> getZone() {
		return this.zone;
	}
	
	
	/**
	 * @param occurenceCount the occurenceCount to set
	 */
	public void setOccurenceCount(int occurenceCount) {
		this.occurenceCount = occurenceCount;
	}
	
	/**
	 * @return occurenceCount
	 */
	public int getOccurenceCount() {
		return this.occurenceCount;
	}
	
	/**
	 * @param occurences the occurences to set
	 */
	public void setOccurences(ArrayList<Occurence> occurences) {
		this.occurences = occurences;
	}
	
	/**
	 * @return the occurences
	 */
	public ArrayList<Occurence> getOccurences() {
		return this.occurences;
	}
	
	@Override
	public String toString() {
		return "Zone = "+zone+ " \nOccurence count = "+occurenceCount+ "\noccurences = "+occurences;
	}
	

}
