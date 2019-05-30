package com.dd.dto;

public class ActivityDto implements IDto {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String event;
	private TimeDto time;
	private String range;
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public TimeDto getTime() {
		return time;
	}
	public void setTime(TimeDto time) {
		this.time = time;
	}
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
}
