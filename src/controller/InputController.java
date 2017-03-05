package controller;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

import model.Session;

public class InputController {

	private SessionController sessionController;
	private BufferedReader br;
	private DatabaseController dbController;
	
	public InputController(SessionController sessionController, DatabaseController dbController) {
		this.br = new BufferedReader(new InputStreamReader(System.in));
		this.sessionController = sessionController;
		this.dbController = dbController;
	}
	
	public void getAction(){
    	System.out.println("What is your action? \n" +
    		"1. New session \n" + 
    		"2. Get best session \n" +
    		"3. See all sessions \n" + 
    		"4. Quit"
    	);
    	
    	String input;
		try {
			input = br.readLine();
			System.out.println("Input was: " + input);
			if (input.equals("1")) {
				sessionController.addSession();
	    	} else if (input.equals("2")) {
	    		// TODO: mangler
	    		System.out.println("Nothing here yet");
	    	} else if (input.equals("3")) {
	    		dbController.showTable(Session.selectAllQuery, "session");
	    	}
			
		} catch (IOException e) {
			System.out.println("Error with reading line");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	getAction();

	}
	
	
}
