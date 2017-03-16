package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import model.Exercise;
import model.Indoor;
import model.Outdoor;
import model.Session;

public class SessionController {

	
	SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
	SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yy");
	SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
	private BufferedReader br;

	private ControllerManager manager;
	
	public SessionController(ControllerManager manager) {
		this.br = new BufferedReader(new InputStreamReader(System.in));
		this.manager = manager;
	}

	private boolean isValidDate(String date) {
		if (date.matches("\\d{2}.\\d{2}.\\d{2}")) {
			try {
				dateFormatter.setLenient(false);
				dateFormatter.parse(date);
				return true;
			} catch (ParseException e) {
				return false;
			}
		} else {
			return false;
		}
	}

    private boolean isValidExercise(String exercise){
        ArrayList<Exercise> getExercise = manager.getDatabaseController().selectExercise(exercise);
        if (getExercise == null || getExercise.isEmpty()) {
            return false;
        }
        return true;
    }

    private boolean isSessionTime(Date sessionStartDateAndTime){
        ArrayList<Session> sessions = manager.getDatabaseController().selectSession(sessionStartDateAndTime);
        if (sessions == null || sessions.isEmpty()) {
            return false;
        }
        return true;
    }
	
	
	public void addSession() throws IOException {
        Date start = null;
        Date end = null;
        String date = null;
        String duration = null;

        boolean preexistingSession = true;
        while (preexistingSession) {
            System.out.println("When was the session (dd.mm.yy)?");
            date = null;
            while(date == null) {
                String input = br.readLine();

                if (isValidDate(input)) {
                    date = input;
                } else {
                    System.out.println("The date does not match the format dd.mm.yy. Try again.");
                }
            }

            System.out.println("When did the session start and end (hh:mm-hh:mm)?");
            String from = null;
            String to = null;
            while (duration == null) {
                String input = br.readLine();
                if(input.matches("([01]?[0-9]|2[0-3]):([0-5][0-9])-([01]?[0-9]|2[0-3]):([0-5][0-9])")){
                    from = input.substring(0, 5);
                    to = input.substring(6, 11);
                    boolean valid = false;
                    try {
                        valid = timeFormatter.parse(from).before(timeFormatter.parse(to));
                    } catch (ParseException e) {
                    }

                    if (valid) {
                        duration = input;
                    } else {
                        System.out.println("The time to and/or from does not match the format hh:mm-hh:mm or is not valid. Try again.");
                    }
                }
                else {
                    System.out.println("The time to and/or from does not match the format hh:mm-hh:mm or is not valid. Try again.");
                }
            }
            try {
                start = formatter.parse(date + " " + from + ":00");
                end = formatter.parse(date + " " + to + ":00");
            } catch (ParseException e){
                e.printStackTrace();
            }
            if (isSessionTime(start)){
                System.out.println("There is already a session registered on this time. Try again.");
            } else {
                preexistingSession = false;
            }
        }



        List<String> exercises = null;
        boolean validExerciseInput = false;
        while (!validExerciseInput) {
            System.out.println("Which exercises did you perform? (exercise,exercise,exercise,...)");
            String input = br.readLine();
            exercises = Arrays.asList(input.split(","));
            validExerciseInput = true;
            for (String s: exercises) {
                if(!isValidExercise(s)){
                    System.out.println(s + " is not a valid exercise.");
                    validExerciseInput = false;
                }
            }
        }
    	
    	System.out.println("Was it in or out?");
    	String inOrOut = null;
    	String airCond = "";
		Integer viewers = null;
    	String temp = null;
    	String weather = "";
    	while (inOrOut == null) {
    		String input = br.readLine();
    		if (input.equals("in") || input.equals("IN")) {
				inOrOut = "in";
    		} else if (input.equals("out") || input.equals("OUT")) {
				inOrOut = "out";
    		} else {
    			System.out.println("That is not a valid option. In or out?");
    		}
    	}
    	if(inOrOut.equals("in")){
			System.out.println("How was the airconditioning?");
			airCond = br.readLine();
			System.out.println("How many viewers?");
			while (viewers == null) {
				String input = br.readLine();
				if (input.matches("\\d{1,3}")) {
				    viewers = Integer.parseInt(input);
				} else {
					System.out.println("This is not a valid number. Try Again.");
				}
			}
		} else {
			System.out.println("How was the temperature?");
			while (temp == null){
			    String input = br.readLine();
			    if (input.matches("^-?\\d{1,2}")) {
			        temp = input;
                } else {
                    System.out.println("This is not a valid temperature. Try Again.");
                }
            }
			System.out.println("How was the weather?");
			weather= br.readLine();
		}
    	
    	System.out.println("What was your personal shape (1-10)?");
    	String personalShape = null;
    	while (personalShape == null) {
    		String input = br.readLine();
    		if (input.matches("\\d{1,2}") && Integer.parseInt(input) >= 1 && Integer.parseInt(input) <= 10) {
    			personalShape = input;
    		} else {
    			System.out.println("The number is not a number or not between 1 and 10. Try again.");	
    		}
    	}
    	
       	System.out.println("What was your performance (1-10)?");
       	String prestation = null;
       	while (prestation == null) {
    		String input = br.readLine();
    		if (input.matches("\\d{1,2}") && Integer.parseInt(input) >= 1 && Integer.parseInt(input) <= 10) {
    			prestation = input;
    		} else {
    			System.out.println("The number is not a number or not between 1 and 10. Try again.");	
    		}
    	}
       	
    	System.out.println("Any notes to the session?");
    	String note = br.readLine();

    	System.out.println("You are saying that you exercised the " + date + 
    			" at " + duration + ", and you personal shape was " + personalShape +
    			" and your performance " + prestation + ". Notes: " + note + ". Is that correct? (y/n)");
    	boolean notValidAnswer = true;
    
    	while (notValidAnswer) {
    		String input = br.readLine();
	    	if (input.equals("y")) {
					
	    		Integer ps = Integer.parseInt(personalShape);
	    		Integer prest = Integer.parseInt(prestation);
	    		
	    		Integer v = null;
                Integer t = null;
                /*if (inOrOut == "in"){
                    v = Integer.parseInt(viewers);
                }*/
				if (inOrOut == "out"){
					t = Integer.parseInt(temp);
				}

	    		if ((start != null) & (end != null)) {
	    			// If in
	    			
	    			if (inOrOut.equals("in")) {
	    				Indoor indoor = new Indoor(start, end, ps, prest, note, airCond, viewers);
	    				manager.getDatabaseController().insertIndoorSession(indoor, exercises);
	    				/*String sessionInsertQuery = indoor.getInsertQuery();
	    				String indoorInsertQuery = indoor.getIndoorInserQuery();
						System.out.println(indoor.getStartDate());
	    				manager.getDatabaseController().insertAction(sessionInsertQuery);
	    				manager.getDatabaseController().insertAction(indoorInsertQuery);*/
	    				
	    			} else if (inOrOut.equals("out")) {
	    				Outdoor outdoor = new Outdoor(start, end, ps, prest, note, t, weather);
	    				manager.getDatabaseController().insertOutdoorSession(outdoor, exercises);
	    				/*String sessionInsertQuery = outdoor.getInsertQuery();
	    				String outdoorInsertQuery = outdoor.getOutdoorInserQuery();
	    				manager.getDatabaseController().insertAction(sessionInsertQuery);
	    				manager.getDatabaseController().insertAction(outdoorInsertQuery);*/
	    			}
	    				        		
	    		} else {
	    			System.out.println("Something is wrong. Start and end couldn't be parsed");
	    		}
	    		notValidAnswer = false;
	    	} else if (input.equals("n")) {
	    		System.out.println("Sorry, my bad. Let's start over.");
	    		notValidAnswer = false;
	    		addSession();
	    	} else {
	    		System.out.println("Sorry, that is not a valid answer. Try again.");
	    	}
		}
    }
}
