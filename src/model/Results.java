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

    private StringProperty exersiceName;
    private ObjectProperty<Date> sessionStartDateAndTime;
    protected SimpleIntegerProperty weight;
    protected SimpleIntegerProperty rep;
    protected SimpleIntegerProperty exersiceSet;
    protected SimpleIntegerProperty distance;
    protected SimpleIntegerProperty duration;

    public static final String createTableQuery = "CREATE TABLE IF NOT EXISTS Results ( " +
            "resultID INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
            "exerciseName VARCHAR(20) NOT NULL, " +
    "sessionStartDateAndTime DATETIME NOT NULL, " +
    "weight INT(3), " +
    "rep INT(2), " +
    "exerciseSet INT(2), " +
    "distance INT(3), " +
    "duration INT(3), " +
    "FOREIGN KEY (exerciseName) " +
        "REFERENCES Exercise (exerciseName) " +
        "ON DELETE NO ACTION " +
        "ON UPDATE CASCADE, " +
    "FOREIGN KEY (sessionStartDateAndTime) " +
        "REFERENCES Session (startDateAndTime) " +
        "ON DELETE RESTRICT " +
        "ON UPDATE CASCADE " +
    ");";

    public Results (String exersiceName, Date sessionStartDateAndTime, int weight, int rep, int exersiceSet, int distance, int duration){
        this.exersiceName = new SimpleStringProperty(exersiceName);
        this.sessionStartDateAndTime = new SimpleObjectProperty<Date>(sessionStartDateAndTime);
        this.weight = new SimpleIntegerProperty(weight);
        this.rep = new SimpleIntegerProperty(rep);
        this.exersiceSet = new SimpleIntegerProperty(exersiceSet);
        this.distance = new SimpleIntegerProperty(distance);
        this.duration = new SimpleIntegerProperty(duration);
    }

    public String getInsertQuery() {
        String q = "Insert into Results (exersiceName, sessionStartDateAndTIme, weight, rep, exersiceSet, distance, duration) " +
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