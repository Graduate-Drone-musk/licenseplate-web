package com.graduate.licenseplate.aws.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.graduate.licenseplate.aws.service.AwsFileService;

@Controller
public class AwsShowIllegalController {

	private AwsFileService awsFileService;
	
	public AwsShowIllegalController(AwsFileService awsFileService) {
		this.awsFileService = awsFileService;
	}
	
	@GetMapping("/show")
	public String showImg() {
		System.out.println("??");
		String startTime = "20220626";
		String endTime = "20220626";
//		awsFileService.getUrlWithCondition(startTime, endTime);
//		awsFileService.readTxtFile();
		awsFileService.uploadImgfile();
		return "show/illegal";
	}
}
