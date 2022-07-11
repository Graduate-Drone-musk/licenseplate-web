package com.graduate.licenseplate.aws.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class IllegalImage {
	private String phone;
	private String userName;
	private String date;
	private String licenseplate;
	private String lpUrl;
	private String originUrl;
}
