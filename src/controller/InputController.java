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
    	System.out.println("What do you want to do? \n" +
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
            switch (input) {
                case "1":
                    manager.getSessionController().addSession();
                    break;
                case "2":
                    System.out.println("QualityController: " + manager.getQualityController());
                    manager.getQualityController().getSession();
                    break;
                case "3":
                    ArrayList<Session> allSessions = manager.getDatabaseController().selectAllSessions();
                    for (Session s : allSessions) {
                        System.out.print(s.getStartDateString() + " to " + s.getEndDateString() +
                                ". Personal shape: " + s.getPersonalShape() + ". Performance: " + s.getPrestation() +
                                ". Note: " + s.getNote());
                        if (s.getInOrOut().equals("out")) {
                            System.out.print(" Location: Outdoor. Temperature: " + ((Outdoor) s).getTemperature() + ". Weather: " + ((Outdoor) s).getWeather());
                        } else {
                            System.out.print(" Location: Indoor. Aircondition: " + ((Indoor) s).getAircondition() + ". Viewers: " + ((Indoor) s).getViewers());
                        }
                        System.out.println();
                    }
                    break;
                case "4":
                    manager.getResultsController().addResults();
                    break;
                case "5":
                    manager.getSetupController().setUp();
                    break;
                case "6":
                    System.out.println("thanks for using workoutdiary");
                    System.exit(0);
                default:
                    System.out.println("Not a valid option. Please choose a number from the list.");
                    break;
            }
			
		} catch (IOException e) {
			System.out.println("Error with reading line");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	getAction();

	}	
	
}
