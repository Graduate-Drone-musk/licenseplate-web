package com.graduate.licenseplate.illegal.service;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.graduate.licenseplate.aws.model.IllegalResultVO;
import com.graduate.licenseplate.component.message.model.NaverMessageResponse;
import com.graduate.licenseplate.illegal.model.IllegalCondition;
import com.graduate.licenseplate.illegal.model.MessageRequest;
import com.graduate.licenseplate.illegal.model.MessageResponse;

import net.nurigo.java_sdk.exceptions.CoolsmsException;

public interface IllegalLicenseService {

	IllegalResultVO getUrlWithCondition(IllegalCondition illegalCondition);

	IllegalResultVO getUrlWithHuman(IllegalCondition illegalCondition);

	MessageResponse sendOneMessage(MessageRequest messageRequest)throws CoolsmsException;

	NaverMessageResponse sendMessage(MessageRequest messageRequest) throws InvalidKeyException, RestClientException, JsonProcessingException, UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException; 

}
