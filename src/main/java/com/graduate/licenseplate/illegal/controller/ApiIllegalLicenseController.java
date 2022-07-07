package com.graduate.licenseplate.illegal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.graduate.licenseplate.aws.model.IllegalResultVO;
import com.graduate.licenseplate.illegal.model.IllegalCondition;
import com.graduate.licenseplate.illegal.service.IllegalLicenseService;

@RestController
public class ApiIllegalLicenseController {
	private final IllegalLicenseService illegalLicenseService;
	
	public ApiIllegalLicenseController (IllegalLicenseService illegalLicenseService) {
		this.illegalLicenseService = illegalLicenseService;
	}
	
	@ResponseBody
	@PostMapping("/api/search/url")
	public ResponseEntity<?> apiImgUrl(@RequestBody IllegalCondition illegalCondition) {
		
		System.out.println(illegalCondition.toString());
		IllegalResultVO illegalResultVO = illegalLicenseService.getUrlWithCondition(illegalCondition);
		
		
		return ResponseEntity.ok().body(illegalResultVO);
	}
	
	@ResponseBody
	@PostMapping("/api/search/human")
	public ResponseEntity<?> apiSearchHuman(@RequestBody IllegalCondition illegalCondition) {
		
		System.out.println(illegalCondition.toString());
		IllegalResultVO illegalResultVO = illegalLicenseService.getUrlWithHuman(illegalCondition);
		
		
		return ResponseEntity.ok().body(illegalResultVO);
	}
}
