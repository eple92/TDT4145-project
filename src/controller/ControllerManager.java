package controller;

public class ControllerManager {

	private DatabaseController databaseController;
	private SessionController sessionController;
    private ResultsController resultsController;
	private QualityController qualityController;
    private InputController inputController;
    private SetupController setupController;
	
	public ControllerManager() {
		createControllers();
	}
	
	private void createControllers() {
		this.databaseController = new DatabaseController(this);
		this.resultsController = new ResultsController(this);
    	this.sessionController = new SessionController(this);
    	this.qualityController = new QualityController(this);
    	this.inputController = new InputController(this);
    	this.setupController = new SetupController(this);
    }

    DatabaseController getDatabaseController() {return databaseController;}

	SessionController getSessionController() {return sessionController;}

	ResultsController getResultsController() {return resultsController;}

    QualityController getQualityController(){return qualityController;}

	SetupController getSetupController() {return setupController;}

    public InputController getInputController(){
        return inputController;
    }


}
