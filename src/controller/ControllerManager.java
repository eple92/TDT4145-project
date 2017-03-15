package controller;

public class ControllerManager {

	private DatabaseController databaseController;
	private SessionController sessionController;
    private ResultsController resultsController;
	private QualityController qualityController;
    private InputController inputController;
	
	public ControllerManager() {
		createControllers();
	}
	
	private void createControllers() {
		this.databaseController = new DatabaseController(this);
		this.resultsController = new ResultsController(this);
    	this.sessionController = new SessionController(this);
    	this.qualityController = new QualityController(this);
    	this.inputController = new InputController(this);
    }

    public DatabaseController getDatabaseController() {
		return databaseController;
	}

	public SessionController getSessionController() {
		return sessionController;
	}

	public ResultsController getResultsController() {
	    return resultsController;
    }

    public QualityController getQualityController(){
	    return qualityController;
    }

    public InputController getInputController(){
        return inputController;
    }
}
