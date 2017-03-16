package controller;

import model.Results;
import model.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResultsController {

    private ControllerManager manager;
    private BufferedReader br;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yy");

    public ResultsController(ControllerManager manager){
        this.br = new BufferedReader(new InputStreamReader(System.in));
        this.manager = manager;
    }

    private boolean exercisePerformedInSession(String exercise, Date startDateAndTime) {
        System.out.println(exercise);
        List<String> exercisesPerformed = manager.getDatabaseController().selectAllExerciseNamesFromSession(startDateAndTime);
        for (String s:exercisesPerformed) {
            if(s.equalsIgnoreCase(exercise)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSessionTime(Date sessionStartDateAndTime){
        ArrayList<Session> sessions = manager.getDatabaseController().selectSession(sessionStartDateAndTime);
        if (sessions == null || sessions.isEmpty()) {
            return false;
        }
        return true;
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

    private String addResultIfApplicable(String qIsApplicable, String qResult, String check){
        String result = null;
        System.out.println(qIsApplicable);
        try {
            boolean validYN = false;
            while (!validYN) {
                String input = br.readLine();
                if (input.equals("y")) {
                    while (result == null){
                        System.out.println(qResult);
                        input = br.readLine();
                        if (input.matches(check)) {
                            result = input;
                        } else {
                            System.out.println("Not valid input. Try again.");
                        }
                    }
                    validYN = true;
                } else if (input.equals("n")){
                    result = "0";
                    validYN = true;
                } else {
                    System.out.println("That was not a valid answer. Please enter y or n");
                }
            }

        } catch (IOException e){
            System.out.println("Sorry, couldn't read line.");
        }
        return result;
    }

    public void addResults() throws IOException {
        System.out.println("here you can add results from your exercise.");

        String sessionStartDateAndTime = null;
        Date start = null;
        String exerciseName = null;

        while(start == null) {
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

            System.out.println("When did the session start (hh:mm)?");
            String time = null;
            while (time == null) {
                String input = br.readLine();
                if(input.matches("([01]?[0-9]|2[0-3]):([0-5][0-9])")){
                    time = input;
                } else {
                    System.out.println("The time does not match the format hh:mm or is not valid. Try again.");
            }
            }
            sessionStartDateAndTime = date + " " + time + ":00";
            start = null;
            try {
                start = formatter.parse(sessionStartDateAndTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (!isSessionTime(start)){
                start = null;
            }
        }

        System.out.println("which exercise did you perform?");
        while (exerciseName == null){
            String input = br.readLine();
            if (exercisePerformedInSession(input, start)){
                exerciseName = input;
            } else {
                System.out.println("That's not a recognized exercise, or was not performed during that session");
            }
        }

        String weight = addResultIfApplicable("Is weight relevant to your result? (y/n)", "What weight did you use?", "[0-9]{1,2}");
        String rep = addResultIfApplicable("Are repetitions relevant to your result? (y/n)", "How many repetitions did you manage?", "[0-9]{1,2}");
        String exerciseSet = addResultIfApplicable("Are sets relevant to your result? (y/n)", "How many sets did you manage?", "[0-9]{1,2}");
        String distance = addResultIfApplicable("Is distance relevant to your result? (y/n)", "What distance did you cover in km?", "[0-9]{1,2}");
        String duration = addResultIfApplicable("Is time relevant to your result? (y/n)", "How long did you run in minutes?", "[0-9]{1,4}");

        System.out.println("You exercised " + exerciseName +
                " in your session at " + sessionStartDateAndTime + ", you lifted " + weight +
                        " kg, performed " + rep + " repetitions for" + exerciseSet + " sets and ran " + distance +
                " km, for " + duration + " min. Is that correct? (y/n)");
        String answer = br.readLine();
        if(answer.equals("y")){
            Integer w = Integer.parseInt(weight);
            Integer r = Integer.parseInt(rep);
            Integer s = Integer.parseInt(exerciseSet);
            Integer di = Integer.parseInt(distance);
            Integer du = Integer.parseInt(duration);

            Results results = new Results(exerciseName, start, w, r, s, di, du);
            /*String resultsInsertQuery = results.getInsertQuery();
            manager.getDatabaseController().insertAction(resultsInsertQuery);*/
            manager.getDatabaseController().insertResults(results);
        } else if (answer.equals("n")){
            addResults();
        } else {
            System.out.println("not a valid input, (y/n)");
        }
    }
}
