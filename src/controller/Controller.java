package controller;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.GlobalReport;
import model.Model;
import model.Species;
import model.ZoneReport;
import view.MainWindowController;
import view.View;
import view.ZoneEntry;

public class Controller {
	
	Model model;
	View view;
	
	private Species selectedSpecies;
	
	public Controller() {
		this.setSelectedSpecies(null);
	}
	
	
	/**
	 * @return the selectedSpecies
	 */
	public Species getSelectedSpecies() {
		return selectedSpecies;
	}


	/**
	 * @param selectedSpecies the selectedSpecies to set
	 */
	public void setSelectedSpecies(Species selectedSpecies) {
		this.selectedSpecies = selectedSpecies;
	}


	/**
	 * @return the model
	 */
	public Model getModel() {
		return model;
	}


	/**
	 * @param model the model to set
	 */
	public void setModel(Model model) {
		this.model = model;
	}


	/**
	 * @return the view
	 */
	public View getView() {
		return view;
	}
	
	/**
	 * @return the view with a cast to MainWindowController
	 */
	public MainWindowController getMainView() {
		return (MainWindowController) view;
	}
	

	/**
	 * @param view the view to set
	 */
	public void setView(View view) {
		this.view = view;
	}


	public GlobalReport getExhaustiveReport() {
		
		GlobalReport globalReport = null;
		
		try {
			globalReport = this.getModel().getExhaustiveReportFromLocal("Delphinidae", "src/delphinidaeOccurence.json");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return globalReport;
	
	}


	public void selectSpecies(String speciesName) {
		GlobalReport globalReport = null;
		
		try {
			globalReport = this.getModel().getExhaustiveReport(speciesName);
			this.setSelectedSpecies(globalReport.getSpecies());
			this.getMainView().displayGlobalReport(globalReport);
			this.getMainView().setAnimationControlsState(false);
		} catch (Exception e) {
			this.getMainView().wrongSpeciesName();
		}
				
	}


	public void parameterizedReport(int geohashPrecision, LocalDate beginDate, LocalDate endDate) {
		
		if(beginDate == null)
			beginDate=LocalDate.EPOCH;
		
		if(endDate==null)
			endDate=LocalDate.now();
		
//		System.out.println(geohashPrecision+" "+beginDate+" "+endDate);
		
		Period period = Period.between(beginDate, endDate);
		
		ArrayList<GlobalReport> globalReportList = this.getModel().getZoneReportsbyTimePeriods(
				this.getSelectedSpecies().getScientificName(), 
				geohashPrecision, 
				beginDate.atStartOfDay(), 
				period, 
				1
				);
		
		this.getMainView().displayGlobalReport(globalReportList.get(0));
		this.getMainView().setPlayable();
	}


	public void getReportsForAnimation(int geohashPrecision, LocalDate beginDate, LocalDate endDate) {
		if(beginDate == null)
			beginDate=LocalDate.EPOCH;
		
		if(endDate==null)
			endDate=LocalDate.now();
		
		Period period = Period.ofYears(5);
		
		int periodCount = ( endDate.getYear() - beginDate.getYear() ) / 5;
		
		ArrayList<GlobalReport> globalReportList = this.getModel().getZoneReportsbyTimePeriods(
				this.getSelectedSpecies().getScientificName(), 
				geohashPrecision, 
				beginDate.atStartOfDay(), 
				period, 
				periodCount
				);
		this.getMainView().animate(globalReportList, beginDate, periodCount);
	}


	public void getSpeciesInGeohash(String geohash) {
		ArrayList<String> speciesNames = this.getModel().getScientificNamesByGeoHash(geohash);
		this.getMainView().displaySpeciesNames(speciesNames);
	}
	
	
	
	
	
}
