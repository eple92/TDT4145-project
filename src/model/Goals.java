package model;


import javafx.beans.property.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Goals {

    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    private StringProperty exerciseName;
    private ObjectProperty<Date> goalStartDate;
    private ObjectProperty<Date> goalEndDate;
    private SimpleIntegerProperty weight;
    private SimpleIntegerProperty rep;
    private SimpleIntegerProperty exerciseSet;
    private SimpleIntegerProperty distance;
    private SimpleIntegerProperty duration;

    public Goals (String exerciseName, Date goalStartDate, int weight, int rep, int exerciseSet, int distance, int duration){
        this.exerciseName = new SimpleStringProperty(exerciseName);
        this.goalStartDate = new SimpleObjectProperty<>(goalStartDate);
        this.weight = new SimpleIntegerProperty(weight);
        this.rep = new SimpleIntegerProperty(rep);
        this.exerciseSet = new SimpleIntegerProperty(exerciseSet);
        this.distance = new SimpleIntegerProperty(distance);
        this.duration = new SimpleIntegerProperty(duration);
    }

    public String getInsertQuery() {
        String q = "Insert into Goals (startDate, endDate, exerciseName, weight, rep, exerciseSet, distance, duration) " +
                "Values("+ getGoalStartDate() + "', '" + getGoalEndDate() + "', '" + getExerciseNameString()+ "', " + getWeight() + ", " +
                getRep() + ", " + getExerciseSet() + ", " + getDistance() + ", " + getDuration() + ");";
        return q;

    }

    public String getExerciseNameString(){return exerciseName.get();}

    public String getGoalStartDate(){
        if(goalStartDate == null) {
            return null;
        }else{
            return dateFormatter.format(goalStartDate.get());
        }
    }

    public String getGoalEndDate(){
        if(goalEndDate == null) {
            return null;
        }else{
            return dateFormatter.format(goalStartDate.get());
        }
    }

    public int getWeight(){return weight.get();}

    public int getRep(){return rep.get();}

    public int getExerciseSet(){return exerciseSet.get();}

    public int getDistance(){return distance.get();}

    public int getDuration(){return duration.get();}
}
