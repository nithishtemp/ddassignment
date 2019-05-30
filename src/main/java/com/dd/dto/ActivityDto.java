package com.dd.dto;

public class ActivityDto implements IDto {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String event;
	private Long activityId;
	private TimeDto time;
	private String range;
	private int minute;
	private long teamId;
	private String timeString;
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
	public Long getActivityId() {
		return activityId;
	}
	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
	public long getTeamId() {
		return teamId;
	}
	public void setTeamId(long teamId) {
		this.teamId = teamId;
	}
	public String getTimeString() {
		return timeString;
	}
	public void setTimeString(String timeString) {
		this.timeString = timeString;
	}
}
