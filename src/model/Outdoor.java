package model;

import java.util.Date;

public class Outdoor extends Session {
	
	private int temperature;
	private String weather;
	
	
	public static final String createTableQuery = "CREATE TABLE IF NOT EXISTS Outdoor (" +
			"outdoorstartDateAndTime DATETIME NOT NULL PRIMARY KEY, " + 
			"weather VARCHAR(20), " +
			"temperature INT(2);";
	
	public static final String selectAllQuery = "SELECT * FROM Outdoor;";
	
	public Outdoor(Date startDate, Date endDate, Integer personalShape, Integer prestation, String note, int temperature, String weather) {
		super(startDate, endDate, personalShape, prestation, note);
		this.temperature = temperature;
		this.weather = weather;
	}
	
	public String getOutdoorInserQuery() {
		String q = "INSERT INTO Outdoor VALUES ('" + 
				super.getStartDateString() + "', " + temperature + ", '" + weather+ "');";
		
		return q;
	}
}
