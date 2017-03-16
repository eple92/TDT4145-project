package model;

import java.util.Date;

public class Outdoor extends Session {
	
	private int temperature;
	private String weather;
	
	public Outdoor(Date startDate, Date endDate, Integer personalShape, Integer prestation, String note, int temperature, String weather) {
		super(startDate, endDate, "out", personalShape, prestation, note);
		this.temperature = temperature;
		this.weather = weather;
	}

	public int getTemperature() {
		return temperature;
	}

	public String getWeather() {
		return weather;
	}
}
