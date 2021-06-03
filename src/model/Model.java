package model;

import controller.Controller;

public class Model {
	
	Controller controller;

	/**
	 * @return the controller
	 */
	public Controller getController() {
		return controller;
	}

	/**
	 * @param controller the controller to set
	 */
	public void setController(Controller controller) {
		this.controller = controller;
	}
	
	public String helloWorld() {
		return "Hello World";
	}
	

}
