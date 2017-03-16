package model;


import javafx.beans.property.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Goals {

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private StringProperty exersiceName;
    private ObjectProperty<Date> sessionStartDateAndTime;
    private SimpleIntegerProperty weight;
    private SimpleIntegerProperty rep;
    private SimpleIntegerProperty exersiceSet;
    private SimpleIntegerProperty distance;
    private SimpleIntegerProperty duration;

    public Goals (String exerciseName, Date sessionStartDateAndTime, int weight, int rep, int exersiceSet, int distance, int duration){
        this.exersiceName = new SimpleStringProperty(exerciseName);
        this.sessionStartDateAndTime = new SimpleObjectProperty<>(sessionStartDateAndTime);
        this.weight = new SimpleIntegerProperty(weight);
        this.rep = new SimpleIntegerProperty(rep);
        this.exersiceSet = new SimpleIntegerProperty(exersiceSet);
        this.distance = new SimpleIntegerProperty(distance);
        this.duration = new SimpleIntegerProperty(duration);
    }

    public String getInsertQuery() {
        String q = "Insert into Goals (exersiceName, sessionStartDateAndTIme, weight, rep, exersiceSet, distance, duration) " +
                "Values("+ getExerciseNameString()+ "', '" + getSessionStartDateAndTime() + "', " + getWeight() + ", " +
                getRep() + ", " + getExerciseSet() + ", " + getDistance() + ", " + getDuration() + ");";
        return q;

    }

    public String getExerciseNameString(){return exersiceName.get();}

    public String getSessionStartDateAndTime(){
        if(sessionStartDateAndTime == null) {
            return null;
        }else{
            return formatter.format(sessionStartDateAndTime.get());
        }
    }

    public int getWeight(){return weight.get();}

    public int getRep(){return rep.get();}

    public int getExerciseSet(){return exersiceSet.get();}

    public int getDistance(){return distance.get();}

    public int getDuration(){return duration.get();}
}
