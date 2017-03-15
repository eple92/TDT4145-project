package controller;

import model.Results;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ResultsController {

    private DatabaseController dbController;
    private BufferedReader br;
    SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

    public ResultsController(){
        this.br = new BufferedReader(new InputStreamReader(System.in));
        this.dbController = dbController;
    }

    boolean isValidExersice (String exersice){ // noe usikker på om denne virker
        if (exersice.matches("knebøy, benkpress, markløft")){
            return true;
        } else {
            return false;
        }
    }

    boolean isExersiceTime (String sessionStartDateAndTime){ // denne må også skjekkes, vet ikke hvordan man kan få til dette
        if(sessionStartDateAndTime.matches("godkjente trningstider fra databasen")){
            return true;
        } else {
            return false;
        }
    }

    public void addResults() throws IOException {
        System.out.println("here you can add results from your exersice. \n which exersice did you perform?");
        String exersiceName = null;
        while (exersiceName == null){
            String input = br.readLine();
            if (isValidExersice(input)){
                exersiceName = input;
            } else {
                System.out.println("Thats not a recognized exersice, please try again");
            }
        }

        System.out.println("when did your session start?");
        String sessionStartDateAndTime = null;
        while(sessionStartDateAndTime == null){
            String input = br.readLine();
            if(isExersiceTime(input)){
                sessionStartDateAndTime = input;
            } else {
                System.out.println("There are no exersice sessions at that time, please insert a valid exersice time");
            }
        }

        System.out.println("Did you perform this exersice outside, or inside?");
        String inOrOut = null;
        String weight = null;
        String rep = null;
        String exersiceSet = null;
        String distance = null;
        String duration = null;
        Boolean correct = false;
        while (inOrOut==null){
            String input = br.readLine();
            if(input.equals("in")){

                System.out.println("What weight did you use?");
                while(weight == null){
                    String inputWeight = br.readLine();
                    if(inputWeight.matches("[0-9]{1,2}") & Integer.parseInt(input) > 0 & Integer.parseInt(input) < 500 ){
                        weight = inputWeight;
                    } else {
                        System.out.println("That's not a valid weight, please insert a valid one.");
                    }
                }

                System.out.println("How many repetitions did you manage?");
                while(rep == null){
                    String inputRep = br.readLine();
                    if(inputRep.matches("[0-9]{1,2}") & Integer.parseInt(input) > 0 & Integer.parseInt(input) < 250 ){
                        rep = inputRep;
                    } else {
                        System.out.println("That's not a valid amount of repetitions, please insert a valid one.");
                    }
                }

                System.out.println("How many sets did you manage?");
                while(exersiceSet == null){
                    String inputSets = br.readLine();
                    if(inputSets.matches("[0-9]{1,2}") & Integer.parseInt(input) > 0 & Integer.parseInt(input) < 500 ){
                        exersiceSet = inputSets;
                    } else {
                        System.out.println("That's not amount of sets, please insert a valid one");
                    }
                }


                System.out.println("You exercised " + exersiceName +
                        " in your session at " + sessionStartDateAndTime + ", you lifted " + weight +
                        " kg, " + rep + " repetitions for" + exersiceSet + " sets. Is that correct? (y/n)");

                String answer = br.readLine();
                if(answer.equals("y")){
                    correct = true;
                } else if (answer.equals("n")){
                    addResults();
                } else {
                    System.out.println("not a valid input, (y/n)");
                }

            } else if (input.equals("out")){

                System.out.println("What distance did you cover in km?");
                while(distance == null){
                    String inputDistance = br.readLine();
                    if(inputDistance.matches("[0-9]{1,2}") & Integer.parseInt(input) > 0 & Integer.parseInt(input) < 500 ){
                        weight = inputDistance;
                    } else {
                        System.out.println("That's not a valid length, please insert a valid one.");
                    }
                }

                System.out.println("how long did you run in minutes?");
                while(duration == null){
                    String inputDuration = br.readLine();
                    if(inputDuration.matches("[0-9]{1,2}") & Integer.parseInt(input) > 0 & Integer.parseInt(input) < 500 ){
                        weight = inputDuration;
                    } else {
                        System.out.println("That's not a valid duration, please insert a valid one.");
                    }
                }


                System.out.println("You exercised " + exersiceName +
                        " in your session at " + sessionStartDateAndTime + ", you ran " + distance +
                        " km, for " + duration + " min. Is that correct? (y/n)");

                String answer = br.readLine();
                if(answer.equals("y")){
                    correct = true;
                } else if (answer.equals("n")){
                    addResults();
                } else {
                    System.out.println("not a valid input, (y/n)");
                }

            }else{
                System.out.println("Please choose a correct term");
            }

            while (correct==true){

                Date start = null;
                try {
                    start = formatter.parse(sessionStartDateAndTime);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Integer w = Integer.parseInt(weight);
                Integer r = Integer.parseInt(rep);
                Integer s = Integer.parseInt(exersiceSet);
                Integer di = Integer.parseInt(distance);
                Integer du = Integer.parseInt(duration);

                Results results = new Results(exersiceName, start, w, r, s, di, du);
                String resultsInsertQuery = results.getInsertQuery();
                dbController.insertAction(resultsInsertQuery);

            }

        }
    }
}
