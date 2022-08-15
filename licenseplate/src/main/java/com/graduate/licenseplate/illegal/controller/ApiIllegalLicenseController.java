package com.graduate.licenseplate.illegal.controller;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.graduate.licenseplate.aws.model.IllegalResultVO;
import com.graduate.licenseplate.component.message.model.NaverMessageResponse;
import com.graduate.licenseplate.illegal.model.IllegalCondition;
import com.graduate.licenseplate.illegal.model.MessageRequest;
import com.graduate.licenseplate.illegal.service.IllegalLicenseService;

import net.nurigo.java_sdk.exceptions.CoolsmsException;

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
	
	@ResponseBody
	@PostMapping("/api/send/message")
	public ResponseEntity<?> sendMessage(@RequestBody MessageRequest messageRequest) throws CoolsmsException {
		
		illegalLicenseService.sendOneMessage(messageRequest);

		return ResponseEntity.ok().body(null);
	}
	
	@ResponseBody
	@PostMapping("/api/send/naverMessage")
	public ResponseEntity<?> sendNaverMessage(@RequestBody MessageRequest messageRequest) throws CoolsmsException, InvalidKeyException, RestClientException, JsonProcessingException, UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException {
		
		NaverMessageResponse response = illegalLicenseService.sendMessage(messageRequest);

		return ResponseEntity.ok().body(response);
	}
}
