package com.graduate.licenseplate.component.message.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class NaverMessageResponse {
	private String requestId;
	private String requestTime;
	private String statusCode;
	private String statusName;
	
	public NaverMessageResponse() {}
	public NaverMessageResponse(String reqId, String reqTime, String code, String name) {
		this.requestId = reqId;
		this.requestTime = reqTime;
		this.statusCode = code;
		this.statusName = name;
	}
}
