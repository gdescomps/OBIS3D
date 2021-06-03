package controller;

import model.Model;
import view.View;

public class Controller {
	
	Model model;
	View view;
	public Controller(Model model, View view) {
		this.model = model;
		this.model.setController(this);
		
		this.view = view;
		this.view.setController(this);
	}
	
	
	
	
}
