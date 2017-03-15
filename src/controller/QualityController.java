package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class QualityController {

    private BufferedReader br;
    
	private ControllerManager manager;

	public QualityController(ControllerManager manager) {
		this.manager = manager;
		br = new BufferedReader(new InputStreamReader(System.in));
	}
	
    public void getSession() throws IOException {
        System.out.println("Do you want \n"
        		+ "1. heaviest lift or \n"
        		+ "2. longest run?");
        String inout = null;
        while(inout == null ){
            String input = br.readLine();
            
            if (input.equals("1")){
            	String query = "SELECT sessionStartDateAndTime, exerciseName, MAX(weight) AS 'weight' " + 
        				"FROM Results " + 
        				"GROUP BY exerciseName;";
            	manager.getDatabaseController().showTable(query, "inResults");
            	inout = input;
            }
            else if(input.equals("2")){
                String query = "SELECT sessionStartDateAndTime, exerciseName, MAX(distance) AS 'distance' " + 
        				"FROM Results " + 
        				"GROUP BY exerciseName;";
                manager.getDatabaseController().showTable(query, "outResults");
                inout = input;
            }
            else{
                System.out.println("Det er ikke en godkjent treningstype (in/out)");
            }
        }
    }
}
