package com.graduate.licenseplate.component.message.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class NaverMessageDto {
	private String to;
	private String subject;
	private String content;
}
