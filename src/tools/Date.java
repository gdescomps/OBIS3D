package tools;


import java.time.LocalDate;
import java.time.ZoneId;

public class Date implements Comparable<Date> {
	
	private int jour;
	private int mois;
	private int annee;
	
	
	public Date(int annee, int mois, int jour) {
		this.jour = jour;
		this.mois = mois;
		this.annee = annee;

	}
	
	
	/**
	 * Met la date d'aujourd'hui :)
	 */
	public Date() {
		ZoneId zonedId = ZoneId.of( "America/Montreal" );
		LocalDate today = LocalDate.now( zonedId );
		this.setJour(today.getDayOfMonth());
		this.setMois(today.getMonth().getValue());
		this.setAnnee(today.getYear());
	}
	
	
	@Override
	public int compareTo(Date date) {
		if (this.annee > date.annee) {return 1;}
		else if(this.annee < date.annee) {return -1;}
		else {
			
			if (this.mois > date.mois) {return 1;}
			else if(this.mois < date.mois) {return -1;}
			else {
				
				if(this.jour > date.jour) {return 1;}
				else if(this.jour < date.jour) {return -1;}
				else return 0;
			}
		}
	}

	/**
	 * Retourne une chaine de caractères correspondant à la date
	 */
	@Override
	public String toString() {
		return jour + "/" + mois + "/" + annee;
	}
	
	/** Obtenir le/la jour
	 * @return le/la jour
	 */
	public int getJour() {
		return jour;
	}


	/** D�finition de jour
	 * @param jour le/la jour � d�finir
	 */
	public void setJour(int jour) {
		this.jour = jour;
	}


	/** Obtenir le/la mois
	 * @return le/la mois
	 */
	public int getMois() {
		return mois;
	}


	/** D�finition de mois
	 * @param mois le/la mois � d�finir
	 */
	public void setMois(int mois) {
		this.mois = mois;
	}


	/** Obtenir le/la annee
	 * @return le/la annee
	 */
	public int getAnnee() {
		return annee;
	}


	/** D�finition de annee
	 * @param annee le/la annee � d�finir
	 */
	public void setAnnee(int annee) {
		this.annee = annee;
	}
	

}

