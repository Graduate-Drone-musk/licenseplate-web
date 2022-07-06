package com.graduate.licenseplate.illegal.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.graduate.licenseplate.member.entity.IllegalMember;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class IllegalLicense {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long illegalId;

	private String licenseplate;
	
	private String date;
	private String lpUrl;
	private String originUrl;
	
	@ManyToOne
	IllegalMember illegalMember;
}
