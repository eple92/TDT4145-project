package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import model.Indoor;
import model.Outdoor;
import model.Session;

public class SessionController {
	
	SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
	SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yy");
	SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
	private BufferedReader br;

	private DatabaseController dbController;
	
	public SessionController(DatabaseController dbController) {
		this.br = new BufferedReader(new InputStreamReader(System.in));
		this.dbController = dbController;
	}

	boolean isValidDate(String date) {
		if (date.matches("\\d{2}.\\d{2}.\\d{2}")) {
			String[] parts = date.split("\\.");
			int day = Integer.parseInt(parts[0]);
			int month = Integer.parseInt(parts[1]);
			int year = Integer.parseInt(parts[2]);
			if (day > 31 || day < 1 || month > 12 || month < 1 || year >17 || year < 12){
				return false;
			}
		}
			if (date.matches("\\d{2}.\\d{2}.\\d{2}")) {
				try {
					dateFormatter.parse(date);
					return true;
				} catch (ParseException e) {
					return false;
				}
			} else {
				return false;
			}
		}

	
	boolean isValidTime(String time) {
		if (time.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
			return true;
		} else {
			return false;
		}
	}
	
	
	public void addSession() throws IOException {
		
    	System.out.println("When was the session (dd.mm.yy)?");
    	String date = null;
    	while(date == null) {
	    	String input = br.readLine();
	    	
	    	if (isValidDate(input)) {
	    		date = input;
	    	} else {
	    		System.out.println("The date does not match the format dd.mm.yy. Try again.");
	    	}
    	}
    	
    	System.out.println("When did the session start and end (hh:mm-hh:mm)?");
    	String duration = null;
    	while (duration == null) {
    		String input = br.readLine();
    		String from = input.substring(0, 5);
    		String to = input.substring(6, 11);
    		System.out.println(isValidTime(from));
    		System.out.println(isValidTime(to));
        	if (isValidTime(from) & isValidTime(to)) {
        		duration = input;
        	} else {
        		System.out.println("The time to and/or from does not match the format hh:mm-hh:mm or is not valid. Try again.");
        	}
    	}
    	
    	System.out.println("Was it in or out?");
    	String inOrOut = null;
    	String airCond = "";
    	String viewers = "";
    	String temp = "";
    	String weather = "";
    	while (inOrOut == null) {
    		String input = br.readLine();
    		if (input.equals("in")) {
    			System.out.println("How was the airconditioning?");
    			airCond = br.readLine();
        		System.out.println("How many viewers?");
        		viewers= br.readLine();
				inOrOut = "in";
    		} else if (input.equals("out")) {
    			System.out.println("How was the temperature?");
    			temp = br.readLine();
        		System.out.println("How was the weather?");
        		weather= br.readLine();
				inOrOut = "out";
    		} else {
    			System.out.println("That is not a valid option. In or out?");
    		}
    	}
    	
    	System.out.println("What was your personal shape (1-10)?");
    	String personalShape = null;
    	while (personalShape == null) {
    		String input = br.readLine();
    		if (input.matches("[0-9]{1,2}") & Integer.parseInt(input) >= 1 & Integer.parseInt(input) <= 10) {
    			personalShape = input;
    		} else {
    			System.out.println("The number is not a number or not between 1 and 10. Try again.");	
    		}
    	}
    	
       	System.out.println("What was your preformance (1-10)?");
       	String prestation = null;
       	while (prestation == null) {
    		String input = br.readLine();
    		if (input.matches("[0-9]{1,2}") & Integer.parseInt(input) >= 1 & Integer.parseInt(input) <= 10) {
    			prestation = input;
    		} else {
    			System.out.println("The number is not a number or not between 1 and 10. Try again.");	
    		}
    	}
       	
    	System.out.println("Any notes to the session?");
    	String note = br.readLine();

    	System.out.println("You are saying that you exercised the " + date + 
    			" at " + duration + ", and you personal shape was " + personalShape +
    			" and your prestation " + prestation + ". Notes: " + note + ". Is that correct? (y/n)");
    	String correct = null; 
    
    	while (correct == null) {
    		String input = br.readLine();
	    	if (input.equals("y")) {
	    		String startHour = duration.substring(0, 4);
	    		String endHour = duration.substring(6, 10);
	    		
	    		Date start = null;
				try {
					start = formatter.parse(date + " " + startHour + ":00");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		Date end = null;
				try {
					end = formatter.parse(date + " " + startHour + ":00");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
	    		Integer ps = Integer.parseInt(personalShape);
	    		Integer prest = Integer.parseInt(prestation);
	    		
	    		Integer v = null;
                Integer t = null;
                if (inOrOut == "in"){
                    v = Integer.parseInt(viewers);
                }
				else if (inOrOut == "out"){
					t = Integer.parseInt(temp);
				}

	    		if ((start != null) & (end != null)) {
	    			// If in
	    			
	    			if (inOrOut.equals("in")) {
	    				Indoor indoor = new Indoor(start, end, ps, prest, note, airCond, v);
	    				String sessionInsertQuery = indoor.getInsertQuery();
	    				String indoorInsertQuery = indoor.getIndoorInserQuery();
						System.out.println(indoor.getStartDate());
	    				dbController.insertAction(sessionInsertQuery);
	    				dbController.insertAction(indoorInsertQuery);
	    				
	    			} else if (inOrOut.equals("out")) {
	    				Outdoor outdoor = new Outdoor(start, end, ps, prest, note, t, weather);
	    				String sessionInsertQuery = outdoor.getInsertQuery();
	    				String outdoorInsertQuery = outdoor.getOutdoorInserQuery();
	    				dbController.insertAction(sessionInsertQuery);
	    				dbController.insertAction(outdoorInsertQuery);
	    			}
	    				        		
	    		} else {
	    			System.out.println("Something is wrong. Start and end couldn't be parsed");
	    		}
	    		
	    	} else if (input.equals("n")) {
	    		System.out.println("Sorry, my bad. Let's start over.");
	    		addSession();
	    	} else {
	    		System.out.println("Sorry, that is not a valid answer. Try again.");
	    	}
		}
    }
}
