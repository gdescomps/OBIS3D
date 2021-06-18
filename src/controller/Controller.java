package controller;

import java.awt.geom.Point2D;
import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.GlobalReport;
import model.Model;
import model.ZoneReport;
import view.View;
import view.ZoneEntry;

public class Controller {
	
	Model model;
	View view;
	public Controller() {

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
	 * @param view the view to set
	 */
	public void setView(View view) {
		this.view = view;
	}


	public ObservableList<ZoneEntry> getExhaustiveReport() {
		
		
		final ObservableList<ZoneEntry> data = FXCollections.observableArrayList();
		
		GlobalReport globalReport = null;
		
		try {
			globalReport = this.getModel().getExhaustiveReportFromLocal("Delphinidae", "src/delphinidaeOccurence.json");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (ZoneReport zoneReport : globalReport.getZoneReports()) {
			
			String zoneString = "";
			
			for (Point2D point : zoneReport.getZone()) {
				zoneString+="["+point.getX()+", "+point.getY()+"]"; 
			}
			
			data.add(new ZoneEntry(""+zoneReport.getOccurenceCount(), zoneString));
			
			System.out.println(""+zoneReport.getOccurenceCount()+" "+zoneString);
		}
		
		
		return data;
	}
	
	
	
	
}
