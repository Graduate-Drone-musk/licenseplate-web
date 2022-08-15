package com.graduate.licenseplate.component.message.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class NaverMessageRequest {
	private String type;
	private String contextType;
	private String countryCode;
	private String from;
	private String subject;
	private String content;
	private List<NaverMessageDto> messages;
	private List<NaverFileDto> files;
	private String scheduleCode;
	
}