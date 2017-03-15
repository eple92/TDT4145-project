package controller;

import model.Exercise;
import model.Session;

public class ControllerManager {

	private DatabaseController databaseController;
	private SessionController sessionController;
	private InputController inputController;
	public QualityController qualityController;
	private ResultsController resultsController;
	
	public ControllerManager() {
		createControllers();
	}
	
	private void createControllers() {
		this.databaseController = new DatabaseController();
		this.resultsController = new ResultsController(databaseController);
    	this.sessionController = new SessionController(databaseController, resultsController);
    	this.inputController = new InputController(sessionController, databaseController, resultsController);
    	this.qualityController = new QualityController();//må linkes ordentlig, vet ikke hvordan dette gjøres

    	
    	inputController.getAction();
    }
	
}
