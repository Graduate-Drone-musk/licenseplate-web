package com.graduate.licenseplate.aws.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.graduate.licenseplate.aws.model.InputToday;
import com.graduate.licenseplate.aws.service.AwsFileService;

@RequestMapping("/dev")
@Controller
public class AwsShowIllegalController {

	private AwsFileService awsFileService;
	
	public AwsShowIllegalController(AwsFileService awsFileService) {
		this.awsFileService = awsFileService;
	}
	
	@GetMapping("/update/illegal")
	public String updateOrigin() {
		awsFileService.uploadImgfile();
		return "update/illegal";
	}
	
	@GetMapping("/update/origin")
	public String showImg(InputToday inputToday) {
		return "update/origin";
	}
	
	@PostMapping("/update/origin")
	public String getToday(InputToday inputToday) {
		awsFileService.updateTestDB(inputToday.getToday());
		return "update/origin";
	}
	

}
