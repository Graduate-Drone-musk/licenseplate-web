package com.graduate.licenseplate.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.graduate.licenseplate.member.entity.IllegalMember;
import com.graduate.licenseplate.member.model.PlateInput;
import com.graduate.licenseplate.member.repository.IllegalMemberRepository;

@Controller
public class IllegalController {
	
	private IllegalMemberRepository illegalMember;
	
	public IllegalController (IllegalMemberRepository illegalMemberRepository) {
		this.illegalMember = illegalMemberRepository;
	}
	
	@GetMapping("/find/my-lp")
	public String findIlliger() {
		return "find/my-lp";
	}
	
	@PostMapping("/find/my-lp")
	public String findIlligerSubmit(PlateInput plateInput) {
		System.out.println("[POST] find/my-ip, Input : " + plateInput.getPlateNum());
		
		return "find/my-lp";
	}
}
