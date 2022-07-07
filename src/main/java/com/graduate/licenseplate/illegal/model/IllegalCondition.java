package com.graduate.licenseplate.illegal.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class IllegalCondition {
	private String startTime;
	private String endTime;
	private String showCount;
	private String page;
	private String allPageCount;
	
	private String name;
	private String registNum;
	
	@Override
	public String toString() {
		return "startTime : " + this.startTime
				+"\nendTime : " + this.endTime
				+"\nshowCount :" + this.showCount
				+"\npage : " +this.page
				+"\nname : " + this.name
				+"\nregist: " + this.registNum; 
	}
}
