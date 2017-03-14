package controller;

import model.Exercise;
import model.Session;

public class ControllerManager {

	private DatabaseController databaseController;
	private SessionController sessionController;
	private InputController inputController;
	public QualityController qualityController;
	
	public ControllerManager() {
		createControllers();
	}
	
	private void createControllers() {
		this.databaseController = new DatabaseController();
    	this.sessionController = new SessionController(databaseController);
    	this.inputController = new InputController(sessionController, databaseController);
    	this.qualityController = new QualityController();//må linkes ordentlig, vet ikke hvordan dette gjøres
    	
    	inputController.getAction();
    }
	
}
