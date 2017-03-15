package controller;

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
    	this.sessionController = new SessionController(databaseController);
    	this.qualityController = new QualityController(databaseController);
    	this.inputController = new InputController(databaseController, sessionController, qualityController, resultsController);
    	
    	
    	inputController.getAction();
    }
	
}
