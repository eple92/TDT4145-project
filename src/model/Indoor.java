package model;

import java.util.Date;

public class Indoor extends Session {
	
	private String aircondition;
	private int viewers;
	
	public static final String createTableQuery = "CREATE TABLE IF NOT EXISTS Indoor (" +
			"indoortartDateAndTime DATETIME NOT NULL PRIMARY KEY, " + 
			"aircondition VARCHAR(20), " +
			"viewers INT(3);";
	
	public static final String selectAllQuery = "SELECT * FROM Indoor;";
	
	public Indoor(Date startDate, Date endDate, Integer personalShape, Integer prestation, String note, String aircondition, int viewers) {
		super(startDate, endDate, personalShape, prestation, note);
		this.aircondition = aircondition;
		this.viewers = viewers;
	}

	public String getIndoorInserQuery() {
		String q = "INSERT INTO Indoor VALUES ('" + 
				super.getStartDateString() + "', '" + aircondition + "', " + viewers + ");";
		
		return q;
	}
}
