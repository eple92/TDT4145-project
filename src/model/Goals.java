package model;


import javafx.beans.property.*;

import java.text.SimpleDateFormat;
import java.util.Date;

// Unused class. Related functionality not yet implemented. Only referenced by unused method
public class Goals {

    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    private ObjectProperty<Date> goalStartDate;
    private ObjectProperty<Date> goalEndDate;
    private StringProperty exerciseName;
    private SimpleIntegerProperty weight;
    private SimpleIntegerProperty rep;
    private SimpleIntegerProperty exerciseSet;
    private SimpleIntegerProperty distance;
    private SimpleIntegerProperty duration;

    public Goals (Date goalStartDate, Date goalEndDate, String exerciseName, int weight, int rep, int exerciseSet, int distance, int duration){
        this.goalStartDate = new SimpleObjectProperty<>(goalStartDate);
        this.goalEndDate = new SimpleObjectProperty<>(goalEndDate);
        this.exerciseName = new SimpleStringProperty(exerciseName);
        this.weight = new SimpleIntegerProperty(weight);
        this.rep = new SimpleIntegerProperty(rep);
        this.exerciseSet = new SimpleIntegerProperty(exerciseSet);
        this.distance = new SimpleIntegerProperty(distance);
        this.duration = new SimpleIntegerProperty(duration);
    }

    public String getExerciseNameString(){return exerciseName.get();}

    public String getGoalStartDateString(){
        if(goalStartDate == null) {
            return null;
        }else{
            return dateFormatter.format(goalStartDate.get());
        }
    }

    public String getGoalEndDateString(){
        if(goalEndDate == null) {
            return null;
        }else{
            return dateFormatter.format(goalStartDate.get());
        }
    }

    public Date getGoalStartDate() {
        if (goalStartDate == null) {
            return null;
        } else {
            return goalStartDate.get();
        }
    }

    public Date getGoalEndDate() {
        if (goalEndDate == null) {
            return null;
        } else {
            return goalEndDate.get();
        }
    }

    public int getWeight(){return weight.get();}

    public int getRep(){return rep.get();}

    public int getExerciseSet(){return exerciseSet.get();}

    public int getDistance(){return distance.get();}

    public int getDuration(){return duration.get();}
}
