package model;


import javafx.beans.property.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Goals {

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private StringProperty exersiceName;
    private ObjectProperty<Date> sessionStartDateAndTime;
    protected SimpleIntegerProperty weight;
    protected SimpleIntegerProperty rep;
    protected SimpleIntegerProperty exersiceSet;
    protected SimpleIntegerProperty distance;
    protected SimpleIntegerProperty duration;

    public Goals (String exersiceName, Date sessionStartDateAndTime, int weight, int rep, int exersiceSet, int distance, int duration){
        this.exersiceName = new SimpleStringProperty(exersiceName);
        this.sessionStartDateAndTime = new SimpleObjectProperty<Date>(sessionStartDateAndTime);
        this.weight = new SimpleIntegerProperty(weight);
        this.rep = new SimpleIntegerProperty(rep);
        this.exersiceSet = new SimpleIntegerProperty(exersiceSet);
        this.distance = new SimpleIntegerProperty(distance);
        this.duration = new SimpleIntegerProperty(duration);
    }

    public String getInsertQuery() {
        String q = "Insert into Goals (exersiceName, sessionStartDateAndTIme, weight, rep, exersiceSet, distance, duration) " +
                "values("+getExersiceNameString()+ "', '" + getSessionStartDateAndTime() + "', " + getWeight() + ", " +
                getRep() + ", " + getExersiceSet() + ", " + getDistance() + ", " + getDuration() + ");";
        return q;

    }

    public String getExersiceNameString(){return exersiceName.get();}

    public String getSessionStartDateAndTime(){
        if(sessionStartDateAndTime == null) {
            return null;
        }else{
            return formatter.format(sessionStartDateAndTime.get());
        }
    }

    public int getWeight(){return weight.get();}

    public int getRep(){return rep.get();}

    public int getExersiceSet(){return exersiceSet.get();}

    public int getDistance(){return distance.get();}

    public int getDuration(){return duration.get();}
}
