package model;

import main.MainApp;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Exercise {
	
	private StringProperty name;
	private StringProperty description;
	
	MainApp mainApp;
	public static final String createTableQuery = "CREATE TABLE IF NOT EXISTS Exercise ("+ 
			"exerciseName VARCHAR(20) NOT NULL PRIMARY KEY, " +
			"description TEXT, " +
			"type VARCHAR(4));";
			
	public static final String selectAllQuery = "SELECT * FROM Exercise;";

	public Exercise(MainApp mainApp, String name, String description) {
		this.mainApp = mainApp;
		this.name = new SimpleStringProperty(name);
		this.description = new SimpleStringProperty(description);
	}
	
	public String getName() {
		return name.get();
	}
	
	public String getDescription() {
		return description.get();
	}


}
