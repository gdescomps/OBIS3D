package model;

import java.awt.geom.Point2D;
import java.util.ArrayList;



public class ZoneReport {
	private ArrayList<Point2D> zone = new ArrayList<Point2D>(5);
	private int occurrenceCount;
	private ArrayList<Occurrence> occurrences = new ArrayList<Occurrence>();

	
/// Constructors :
	
	/**
	 * @param zone
	 * @param occurrenceCount
	 */
	public ZoneReport(ArrayList<Point2D> zone, int occurrenceCount) {
		this.zone = zone;
		this.occurrenceCount = occurrenceCount;
	}

	/**
	 * @param zone
	 * @param occurrenceCount
	 * @param occurrences
	 */
	public ZoneReport(ArrayList<Point2D> zone, int occurrenceCount, ArrayList<Occurrence> occurrences) {
		this.zone = zone;
		this.occurrenceCount = occurrenceCount;
		this.occurrences = occurrences;
	}

	
	
/// Setters :

	/**
	 * @param zone the zone to set
	 */
	public void setZone(ArrayList<Point2D> zone) {
		this.zone = zone;
	}
	
	/**
	 * @param occurrenceCount the occurrenceCount to set
	 */
	public void setOccurrenceCount(int occurrenceCount) {
		this.occurrenceCount = occurrenceCount;
	}

	/**
	 * @param occurrences the occurrences to set
	 */
	public void setOccurrences(ArrayList<Occurrence> occurrences) {
		this.occurrences = occurrences;
	}
	
	
	
/// Getters :
	
	/**
	 * @return the zone
	 */
	public ArrayList<Point2D> getZone() {
		return zone;
	}


	/**
	 * @return the occurrenceCount
	 */
	public int getOccurrenceCount() {
		return occurrenceCount;
	}
	

	/**
	 * @return the occurrences
	 */
	public ArrayList<Occurrence> getOccurrences() {
		return occurrences;
	}

	
	
	/// Overrides :
	
	@Override
    public boolean equals (Object zoneReport) {
		if (this.getOccurrenceCount() != ((ZoneReport) zoneReport).getOccurrenceCount()) {
			System.out.println("Different OccurrenceCount");
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
	
	@Override
	public String toString() {
		return "Zone = "+zone+ " \nOccurrence count = "+occurrenceCount+ "\noccurrences = "+occurrences;
	}

	

}
