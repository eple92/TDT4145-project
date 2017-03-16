package model;

import java.util.Date;

public class Indoor extends Session {
	
	private String aircondition;
	private int viewers;
	
	public Indoor(Date startDate, Date endDate, Integer personalShape, Integer prestation, String note, String aircondition, int viewers) {
		super(startDate, endDate, "in", personalShape, prestation, note);
		this.aircondition = aircondition;
		this.viewers = viewers;
	}

	public String getAircondition() {
		return aircondition;
	}

	public int getViewers() {
		return viewers;
	}
}
