package com.dd.dto;

public class TimeDto implements IDto{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int hour;
	private int minute;
	private String period;
	
	public TimeDto() {
		
	}
	
	public TimeDto(int hour, int minute, String period) {
		this.hour = hour;
		this.minute = minute;		
		this.period = (period==null)? "AM" : period;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}
	
	public String toString() {
		return this.hour + ":" + this.minute + " "+ this.period + " ";		
	}
	
	public void addMinutes(int minute) {
		this.minute = this.minute + minute;		
		this.updateMinute();		
		this.updateHour();
	}
	
	private void updateMinute() {
		if(this.minute >= 60) {
			this.minute = this.minute%60;
			this.hour = this.hour + 1;
			updateMinute();
		}
	}
	
	private void updateHour() {
		if(this.hour == 12)
			this.period = (this.period == "AM")? "PM" : "AM";
		if(this.hour > 12) {
			this.hour = this.hour %12;			
			updateHour();
		}
	}
	
	public TimeDto clone() {
		TimeDto clone = new TimeDto();
		clone.setHour(this.getHour());
		clone.setMinute(this.getMinute());
		clone.setPeriod(this.getPeriod());
		return clone;
	}
}
