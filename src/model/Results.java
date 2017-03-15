package model;

import javafx.beans.property.*;

import java.util.Date;
import java.text.SimpleDateFormat;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Results {

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private StringProperty exerciseName;
    private ObjectProperty<Date> sessionStartDateAndTime;
    private SimpleIntegerProperty weight;
    private SimpleIntegerProperty rep;
    private SimpleIntegerProperty exerciseSet;
    private SimpleIntegerProperty distance;
    private SimpleIntegerProperty duration;

    public Results (String exersiceName, Date sessionStartDateAndTime, int weight, int rep, int exerciseSet, int distance, int duration){
        this.exerciseName = new SimpleStringProperty(exersiceName);
        this.sessionStartDateAndTime = new SimpleObjectProperty<>(sessionStartDateAndTime);
        this.weight = new SimpleIntegerProperty(weight);
        this.rep = new SimpleIntegerProperty(rep);
        this.exerciseSet = new SimpleIntegerProperty(exerciseSet);
        this.distance = new SimpleIntegerProperty(distance);
        this.duration = new SimpleIntegerProperty(duration);
    }

    public String getInsertQuery() {
        String q = "INSERT INTO Results (exerciseName, sessionStartDateAndTIme, weight, rep, exerciseSet, distance, duration) " +
                "VALUES('"+ getExerciseNameString()+ "', '" + getSessionStartDateAndTime() + "', '" + getWeight() + "', '" +
                getRep() + "', '" + getExerciseSet() + "', '" + getDistance() + "', '" + getDuration() + "');";
        return q;

    }

    public String getExerciseNameString(){return exerciseName.get();}

    public String getSessionStartDateAndTime(){
        if(sessionStartDateAndTime == null) {
            return null;
        }else{
            return formatter.format(sessionStartDateAndTime.get());
        }
    }

    public int getWeight(){return weight.get();}

    public int getRep(){return rep.get();}

    public int getExerciseSet(){return exerciseSet.get();}

    public int getDistance(){return distance.get();}

    public int getDuration(){return duration.get();}
}
