package com.dd.dto;

public class TypeDto implements IDto{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String type;
	private int typeMinutes;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getTypeMinutes() {
		return typeMinutes;
	}
	public void setTypeMinutes(int typeMinutes) {
		this.typeMinutes = typeMinutes;
	}
}
