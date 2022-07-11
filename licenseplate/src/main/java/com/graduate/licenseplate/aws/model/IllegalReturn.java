package com.graduate.licenseplate.aws.model;

import com.graduate.licenseplate.illegal.entity.IllegalLicense;
import com.graduate.licenseplate.illegal.entity.OriginLicense;
import com.graduate.licenseplate.member.entity.IllegalMember;
import com.graduate.licenseplate.member.entity.OriginMember;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class IllegalReturn {
	private IllegalMember illegalMember;
	private IllegalLicense illegalLicense;
	private OriginMember originMember;
	private OriginLicense originLicense;
}
