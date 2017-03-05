package controller;

import model.Exercise;
import model.Session;

public class ControllerManager {

	private DatabaseController databaseController;
	private SessionController sessionController;
	private InputController inputController;
	
	public ControllerManager() {
		createControllers();
	}
	
	private void createControllers() {
		this.databaseController = new DatabaseController();
    	this.sessionController = new SessionController(databaseController);
    	this.inputController = new InputController(sessionController, databaseController);
    	
    	inputController.getAction();
    }
	
}
