package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.text.SimpleDateFormat;
import java.util.Date;

// Unused class. Related functionality not yet implemented
public class PulsAndGPS {

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private SimpleObjectProperty dateAndTime;
    private SimpleIntegerProperty puls;
    private SimpleIntegerProperty longitude;
    private SimpleIntegerProperty latitude;
    private SimpleIntegerProperty metersAboveOcean;
    private SimpleIntegerProperty results_fk;

    public PulsAndGPS(Date dateAndTime, int puls, int longitude, int latitude, int metersAboveOcean, int results_fk){
        this.dateAndTime = new SimpleObjectProperty<Date>(dateAndTime);
        this.puls = new SimpleIntegerProperty(puls);
        this.longitude = new SimpleIntegerProperty(longitude);
        this.latitude = new SimpleIntegerProperty(latitude);
        this.metersAboveOcean = new SimpleIntegerProperty(metersAboveOcean);
        this.results_fk = new SimpleIntegerProperty(results_fk);
    }
    public String getDateAndTime(){
        if(dateAndTime == null) {
            return null;
        }else{
            return formatter.format(dateAndTime.get());
        }
    }

    public int getPuls(){
    	return puls.get();
    }

    public int getLongitude(){
    	return longitude.get();
    }

    public int getLatitude(){
    	return latitude.get();
    }

    public int getMetersAboveOcean(){
    	return metersAboveOcean.get();
    }

    public int getResults_fk(){
    	return  results_fk.get();
    }


}
