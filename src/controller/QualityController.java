package controller;

import model.Results;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class QualityController {

    private BufferedReader br;
    
	private ControllerManager manager;

	public QualityController(ControllerManager manager) {
		this.manager = manager;
		br = new BufferedReader(new InputStreamReader(System.in));
	}
	
    public void getSession() throws IOException {
        System.out.println("Choose an option: \n" +
        		"1. Show heaviest lift\n" +
                "2. Show highest number of repetitions\n" +
                "3. Show highest number of sets\n" +
        		"4. Show longest run in km\n" +
                "5. Show longest run in minutes\n" +
                "6. Go back to main menu");
        boolean isValidOption = false;
        while(!isValidOption){
            String input = br.readLine();

            switch (input) {
                case "1": {
                    String query = "SELECT *, MAX(weight) AS 'weight' " +
                            "FROM Results " +
                            "GROUP BY exerciseName;";
                    List<Results> results = manager.getDatabaseController().selectResultsByQuery(query);
                    for (Results r : results) {
                        if (r.getWeight() > 0) {
                            System.out.println(r.getSessionStartDateAndTimeString() + ": " + r.getExerciseNameString() + " - " + r.getWeight() + "kg");
                        }
                    }
                    isValidOption = true;
                    break;
                }
                case "2": {
                    String query = "SELECT *, MAX(rep) AS 'rep' " +
                            "FROM Results " +
                            "GROUP BY exerciseName;";
                    List<Results> results = manager.getDatabaseController().selectResultsByQuery(query);
                    for (Results r : results) {
                        if (r.getRep() > 0) {
                            System.out.println(r.getSessionStartDateAndTimeString() + ": " + r.getExerciseNameString() + " - " + r.getRep() + " repetitions");
                        }
                    }
                    isValidOption = true;
                    break;
                }
                case "3": {
                    String query = "SELECT *, MAX(exerciseSet) AS 'exerciseSet' " +
                            "FROM Results " +
                            "GROUP BY exerciseName;";
                    List<Results> results = manager.getDatabaseController().selectResultsByQuery(query);
                    for (Results r : results) {
                        if (r.getExerciseSet() > 0) {
                            System.out.println(r.getSessionStartDateAndTimeString() + ": " + r.getExerciseNameString() + " - " + r.getExerciseSet() + " sets");
                        }
                    }
                    isValidOption = true;
                    break;
                }
                case "4": {
                    String query = "SELECT *, MAX(distance) AS 'distance' " +
                            "FROM Results " +
                            "GROUP BY exerciseName;";
                    List<Results> results = manager.getDatabaseController().selectResultsByQuery(query);
                    for (Results r : results) {
                        if (r.getDistance() > 0) {
                            System.out.println(r.getSessionStartDateAndTimeString() + ": " + r.getExerciseNameString() + " - " + r.getDistance() + "km");
                        }
                    }
                    isValidOption = true;
                    break;
                }
                case "5": {
                    String query = "SELECT *, MAX(duration) AS 'duration' " +
                            "FROM Results " +
                            "GROUP BY exerciseName;";
                    List<Results> results = manager.getDatabaseController().selectResultsByQuery(query);
                    for (Results r : results) {
                        if (r.getDistance() > 0) {
                            System.out.println(r.getSessionStartDateAndTimeString() + ": " + r.getExerciseNameString() + " - " + r.getDuration() + " min");
                        }
                    }
                    isValidOption = true;
                    break;
                }
                case "6": {
                    return;
                }
                default:
                    System.out.println("This is not a valid option. Please choose a number from the list.");
                    break;
            }
        }
    }
}
