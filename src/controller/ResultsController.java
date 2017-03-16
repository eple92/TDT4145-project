package controller;

import model.Exercise;
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
    private SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");

    public ResultsController(ControllerManager manager){
        this.br = new BufferedReader(new InputStreamReader(System.in));
        this.manager = manager;
    }

    private boolean isValidExercise(String exercise){
        ArrayList<Exercise> getExercise = manager.getDatabaseController().selectExercise(exercise);
        if (getExercise == null || getExercise.isEmpty()) {
            return false;
        }
        return true;
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

    private boolean isExerciseTime(Date sessionStartDateAndTime){
        ArrayList<Session> sessions = manager.getDatabaseController().selectSession(sessionStartDateAndTime);
        if (sessions == null || sessions.isEmpty()) {
            return false;
        }
        return true;
        /*if (manager.getDatabaseController().selectAction(new Session(sessionStartDateAndTime, sessionStartDateAndTime, "in", 0, 0,"").getSelectQuery(), "session").isEmpty()){
            return false;
        } else {
            return true;
        }*/
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

    public void addResults() throws IOException {
        System.out.println("here you can add results from your exercise.");

        boolean notValidSessionTime = true;
        String sessionStartDateAndTime = null;
        Date start = null;

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
            if (!isExerciseTime(start)){
                start = null;
            }
        }

        System.out.println("which exercise did you perform?");
        String exerciseName = null;
        while (exerciseName == null){
            String input = br.readLine();
            if (exercisePerformedInSession(input, start)){
                exerciseName = input;
            } else {
                System.out.println("That's not a recognized exercise, or was not performed during that session");
            }
        }

        System.out.println("Did you perform this exercise outside, or inside?");
        String inOrOut = null;
        String weight = null;
        String rep = null;
        String exerciseSet = null;
        String distance = null;
        String duration = null;
        while (inOrOut==null){
            String input = br.readLine();
            if(input.equals("in")){
                inOrOut = "in";
            } else if (input.equals("out")) {
                inOrOut = "out";
            } else {
                System.out.println("Please choose in or out:");
            }
        }
        if(inOrOut.equals("in")){

            System.out.println("What weight did you use?");
            while(weight == null){
                String inputWeight = br.readLine();
                if(inputWeight.matches("[0-9]{1,2}") & Integer.parseInt(inputWeight) > 0 & Integer.parseInt(inputWeight) < 500 ){
                    weight = inputWeight;
                } else {
                    System.out.println("That's not a valid weight, please insert a valid one.");
                }
            }

            System.out.println("How many repetitions did you manage?");
            while(rep == null){
                String inputRep = br.readLine();
                if(inputRep.matches("[0-9]{1,2}") & Integer.parseInt(inputRep) > 0 & Integer.parseInt(inputRep) < 250 ){
                    rep = inputRep;
                } else {
                    System.out.println("That's not a valid amount of repetitions, please insert a valid one.");
                }
            }

            System.out.println("How many sets did you manage?");
            while(exerciseSet == null){
                String inputSets = br.readLine();
                if(inputSets.matches("[0-9]{1,2}") & Integer.parseInt(inputSets) > 0 & Integer.parseInt(inputSets) < 500 ){
                    exerciseSet = inputSets;
                } else {
                    System.out.println("That's not amount of sets, please insert a valid one");
                }
            }


            System.out.println("You exercised " + exerciseName +
                    " in your session at " + sessionStartDateAndTime + ", you lifted " + weight +
                    " kg, " + rep + " repetitions for" + exerciseSet + " sets. Is that correct? (y/n)");

        } else {

            System.out.println("What distance did you cover in km?");
            while(distance == null){
                String inputDistance = br.readLine();
                if(inputDistance.matches("[0-9]{1,2}") & Integer.parseInt(inputDistance) > 0 & Integer.parseInt(inputDistance) < 500 ){
                    distance = inputDistance;
                } else {
                    System.out.println("That's not a valid length, please insert a valid one.");
                }
            }

            System.out.println("how long did you run in minutes?");
            while(duration == null){
                String inputDuration = br.readLine();
                if(inputDuration.matches("[0-9]{1,2}") & Integer.parseInt(inputDuration) > 0 & Integer.parseInt(inputDuration) < 500 ){
                    duration = inputDuration;
                } else {
                    System.out.println("That's not a valid duration, please insert a valid one.");
                }
            }


            System.out.println("You exercised " + exerciseName +
                    " in your session at " + sessionStartDateAndTime + ", you ran " + distance +
                    " km, for " + duration + " min. Is that correct? (y/n)");
        }

        String answer = br.readLine();
        if(answer.equals("y")){
            Integer w = 0;
            Integer r = 0;
            Integer s = 0;
            Integer di = 0;
            Integer du = 0;

            if (inOrOut.equals("in")) {
                w = Integer.parseInt(weight);
                r = Integer.parseInt(rep);
                s = Integer.parseInt(exerciseSet);
            } else {
                di = Integer.parseInt(distance);
                du = Integer.parseInt(duration);
            }

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
