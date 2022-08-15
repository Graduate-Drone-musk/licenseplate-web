package com.graduate.licenseplate.component.message;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graduate.licenseplate.component.message.model.NaverFileDto;
import com.graduate.licenseplate.component.message.model.NaverMessageDto;
import com.graduate.licenseplate.component.message.model.NaverMessageRequest;
import com.graduate.licenseplate.component.message.model.NaverMessageResponse;
import com.graduate.licenseplate.illegal.entity.IllegalLicense;
import com.graduate.licenseplate.member.entity.IllegalMember;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

@Configuration
public class MessageService {

	private MessageProperties messageProperties;
	
	public MessageService(MessageProperties messageProperties) {
		this.messageProperties = messageProperties;
	}
	
	
	public void sendOneMessage(IllegalLicense user, String url) throws CoolsmsException {
		Message msg = new Message(messageProperties.getCoolAccessKey(), messageProperties.getCoolSecretkKey());
		msg.send(setParam(user, url));
		System.out.println("성공");
	}
	
	private HashMap<String, String> setParam(IllegalLicense user, String url){
		IllegalMember userInfo =  user.getIllegalMember();
		
		HashMap<String, String> context = new HashMap<String, String>();
		context.put("to", userInfo.getPhone());
		context.put("from", messageProperties.getNumber());
		context.put("type", "MMS");
		context.put("text", getSendText(user, url));
		context.put("app_version", "LicensePlate 1..0.0"); // application name and version
		return context;
	}
	
	private String getSendText(IllegalLicense user, String url) {
		String date = user.getDate().toString();
		String time = user.getTime().toString();
		return "안녕하세요 Drone Musk입니다.\n"
				+ date.substring(0,4)+"년 " + date.substring(4,6)+"월 "+date.substring(6)+"일 "
				+ time.substring(0,2)+"시 "+time.substring(2,4)+"분경에 불법주차가 감지되어 벌금이 부과됩니다.\n"
				+ "자세한 사항은 \""+url+"\"에서 확인해주시기 바랍니다.";
	}
	
	
	public NaverMessageResponse sendNaverMessage(List<IllegalLicense> userList, String url) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, RestClientException, URISyntaxException, JsonProcessingException {
		Long time = System.currentTimeMillis();
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", messageProperties.getNaverAccessKey());
        headers.set("x-ncp-apigw-signature-v2", makeSignature(time));
        
        List<NaverMessageDto> messageDto = new ArrayList<>();
        for(IllegalLicense user: userList){
        	messageDto.add(NaverMessageDto.builder()
        			.to(user.getIllegalMember().getPhone())
        			.subject("[Drone Musk] 불법 주차 단속 안내입니다.")
        			.content(getSendText(user, url))
        			.build());
        }
        
        
        NaverMessageRequest request = NaverMessageRequest.builder()
        		.type("LMS")
        		.contextType("COMM")
        		.countryCode("82")
        		.from(messageProperties.getNumber())
//        		.subject()
        		.content("내용")
        		.messages(messageDto)
//        		.files()
        		.build();
        
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(request);
        HttpEntity<String> body = new HttpEntity<>(jsonBody,headers);
        
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        
        NaverMessageResponse response = restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/"+messageProperties.getServiceId()+"/messages"), body, NaverMessageResponse.class);
        return response;
	
	}
	
	public String makeSignature(Long time) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
		String space = " ";					// one space
		String newLine = "\n";					// new line
		String method = "POST";					// method
		String url = "/sms/v2/services/"+messageProperties.getServiceId()+"/messages";	// url (include query string)
		String timestamp = time.toString();			// current timestamp (epoch)
		String accessKey = messageProperties.getNaverAccessKey();			// access key id (from portal or Sub Account)
		String secretKey = messageProperties.getNaverSecretKey();

		String message = new StringBuilder()
			.append(method)
			.append(space)
			.append(url)
			.append(newLine)
			.append(timestamp)
			.append(newLine)
			.append(accessKey)
			.toString();

		SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(signingKey);

		byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
		String encodeBase64String = Base64.encodeBase64String(rawHmac);

	  return encodeBase64String;
	}
	
}
