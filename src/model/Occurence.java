package model;

import tools.Date;

public class Occurence {
	
	public static int count = 0;
	private int id;
	private Date date;
	
	public Occurence(Date date) {
		this.setId() ;
		this.setDate(date);
		
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId() {
		this.id = count;
		count += 1;
	}
 
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	

}
