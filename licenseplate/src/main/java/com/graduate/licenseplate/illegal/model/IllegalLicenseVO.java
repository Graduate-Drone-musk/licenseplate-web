package com.graduate.licenseplate.illegal.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.graduate.licenseplate.member.entity.IllegalMember;

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