package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import model.Indoor;
import model.Outdoor;
import model.Session;

public class InputController {

	private ControllerManager manager;
	private BufferedReader br;
	
	public InputController(ControllerManager manager) {
		this.manager = manager;
		this.br = new BufferedReader(new InputStreamReader(System.in));
	}

	private boolean needsSetUp() {
	    File propsFile = new File("resources/config.properties");
	    if (propsFile.exists()){
	        return false;
        }
	    return true;
    }
	
	public void getAction(){
	    if (needsSetUp()) {
	        manager.getSetupController().firstTimeSetUp();
        } else {
	        manager.getDatabaseController().makeNewConnection();
        }
    	System.out.println("What is your action? \n" +
                "1. New session \n" +
                "2. Get best result \n" +
                "3. See all sessions \n" +
                "4. Add Results \n" +
                "5. Setup \n" +
                "6. Quit"
        );
    	
    	String input;
		try {
			input = br.readLine();
			System.out.println("Input was: " + input);
			if (input.equals("1")) {
				manager.getSessionController().addSession();
	    	} else if (input.equals("2")) {
	    		System.out.println("QualityController: " + manager.getQualityController());
	    		manager.getQualityController().getSession();
	    	} else if (input.equals("3")) {
                ArrayList<Session> allSessions = manager.getDatabaseController().selectAllSessions();
                for (Session s : allSessions) {
                    System.out.print(s.getStartDateString() + " to " + s.getEndDateString() +
                            ". Personal shape: " + s.getPersonalShape() + ". Performance: " + s.getPrestation() +
                            ". Note: " + s.getNote());
                    if (s.getInOrOut().equals("out")){
                        System.out.print(" Location: Outdoor. Temperature: " + ((Outdoor) s).getTemperature() + ". Weather: " + ((Outdoor) s).getWeather());
                    } else {
                        System.out.print(" Location: Indoor. Aircondition: " + ((Indoor) s).getAircondition() + ". Viewers: " + ((Indoor) s).getViewers());
                    }
                    System.out.println();
                }
			} else if (input.equals("4")) {
				manager.getResultsController().addResults();
			} else if (input.equals("5")) {
			    manager.getSetupController().setUp();
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
