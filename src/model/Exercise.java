package model;

import main.MainApp;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Exercise {
	
	private StringProperty name;
	private StringProperty description;
			
	public static final String selectAllQuery = "SELECT * FROM Exercise;";

	public Exercise(String name, String description) {
		this.name = new SimpleStringProperty(name);
		this.description = new SimpleStringProperty(description);
	}
	
	public String getName() {
		return name.get();
	}
	
	public String getDescription() {
		return description.get();
	}

	public String getInstertQuery() {
		return "INSERT INTO Exercise(exerciseName, description) VALUES('" + getName() + "', '" + getDescription() + "');";
	}

}
