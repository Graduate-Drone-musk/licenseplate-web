package com.graduate.licenseplate.illegal.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class IllegalLicenseVO {
	private String licenseplate;
	private String date;
	private String lpUrl;
	private String originUrl;
	private String time;
}
