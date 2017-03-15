package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import model.Session;

public class InputController {

	private SessionController sessionController;
	private BufferedReader br;
	private DatabaseController dbController;
	private QualityController qualityController;
	private ResultsController resultsController;
	
	public InputController(DatabaseController dbController, SessionController sessionController, QualityController qualityController, ResultsController resultsController) {
		this.br = new BufferedReader(new InputStreamReader(System.in));
		this.sessionController = sessionController;
		this.dbController = dbController;
		this.qualityController = qualityController;
		this.resultsController = resultsController;
	}
	
	public void getAction(){
    	System.out.println("What is your action? \n" +
    		"1. New session \n" + 
    		"2. Get best result \n" +
    		"3. See all sessions \n" +
			"4. Add exercises to a session \n" +
			"5. Add Results \n" +
    		"6. Quit"
    	);
    	
    	String input;
		try {
			input = br.readLine();
			System.out.println("Input was: " + input);
			if (input.equals("1")) {
				sessionController.addSession();
	    	} else if (input.equals("2")) {
	    		System.out.println("QualityController: " + qualityController);
	    		qualityController.getSession();
	    	} else if (input.equals("3")) {
	    		dbController.showTable(Session.selectAllQuery, "session");
	    	} else if (input.equals("4")) {
				System.out.println("add exersices here");
				// TODO
			} else if (input.equals("5")) {
				System.out.println("add results here");
				resultsController.addResults();
			} else if (input.equals("6")) {
				System.out.println("thanks for using workoutdiary");
				System.exit(0);
			}
			
		} catch (IOException e) {
			System.out.println("Error with reading line");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	getAction();

	}	
	
}
