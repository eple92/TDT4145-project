package model;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import main.MainApp;

public class Session {
	
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private ObjectProperty<Date> startDate;
	private ObjectProperty<Date> endDate;
	private String inOrOut = "in";
	protected SimpleIntegerProperty personalShape;
	protected SimpleIntegerProperty prestation;
	protected StringProperty note;

	public static final String selectAllQuery = "SELECT * FROM ExerciseSession;";

	public Session(Date startDate, Date endDate, Integer personalShape, Integer prestation, String note) {
		this.startDate = new SimpleObjectProperty<Date>(startDate);
		this.endDate = new SimpleObjectProperty<Date>(endDate);
		this.personalShape = new SimpleIntegerProperty(personalShape);
		this.prestation = new SimpleIntegerProperty(prestation);
		this.note = new SimpleStringProperty(note); 
	}
	
	public String getInsertQuery() {
		String q = "INSERT INTO ExerciseSession ('" + 
				getStartDateString() + "', '" + getEndDateString()+ "', '" + this.inOrOut + "' " + getPersonalShape() + ", " +
				getPrestation() + ", '" + getNote() + "');";	
		return q;
	}
	
	public Date getStartDate() {
		if (startDate == null) {
			return null;
		} else {
			return startDate.get();
		}
	}
	
	public Date getEndDate() {
		if (endDate == null) {
			return null;
		} else {
			return endDate.get();
		}
	}
	
	public String getStartDateString() {
		if (startDate == null) {
			return null;
		} else {
			return formatter.format(startDate.get());
		}
	}
	
	public String getEndDateString() {
		if (endDate == null) {
			return null;
		} else {
			return formatter.format(endDate.get());
		}
	}
	
	public int getPersonalShape() {
		return personalShape.get();
	}
	
	public int getPrestation() {
		return prestation.get();
	}
	
	public String getNote() {
		return note.get();
	}

}
