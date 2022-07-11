package com.graduate.licenseplate.illegal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.graduate.licenseplate.illegal.model.IllegalCondition;
import com.graduate.licenseplate.illegal.service.IllegalLicenseService;

@Controller
public class IllegalLicenseController {
	private final IllegalLicenseService illegalLicenseService;
	
	public IllegalLicenseController(
			IllegalLicenseService illegalLicenseService) {
		this.illegalLicenseService = illegalLicenseService;
	}
	
	@GetMapping("/test/geturl")
	public String imgUrl(IllegalCondition apiIllegalCondition) {
		String startTime = null;
		String endTime = null;
		String showCount = "10";
		String page = "1";
		
		IllegalCondition illegalCondition = IllegalCondition.builder()
				.startTime(startTime)
				.endTime(endTime)
				.showCount(endTime)
				.page(endTime)
				.build();
		
		System.out.println(apiIllegalCondition);
//		illegalLicenseService.getUrlWithCondition(illegalCondition);
		
		return "find/my-lp";
	}
}
