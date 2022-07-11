package com.graduate.licenseplate.illegal.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.graduate.licenseplate.member.entity.OriginMember;

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
public class OriginLicense {
	@Id
	private String licenseplate;
	private Integer accidentCount;
	
	@ManyToOne
	OriginMember member;
}
