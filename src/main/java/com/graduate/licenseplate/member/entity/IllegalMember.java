package com.graduate.licenseplate.member.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.graduate.licenseplate.illegal.entity.IllegalLicense;
import com.graduate.licenseplate.illegal.entity.OriginLicense;

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
public class IllegalMember {
	@Id
	@Column(nullable = false)
	private String registrationNumber;
	
	private String userName;
	private String phone;
	
	@OneToMany(mappedBy = "illegalMember")
	private List<IllegalLicense> illegalLicenseList = new ArrayList<>();
	
}
