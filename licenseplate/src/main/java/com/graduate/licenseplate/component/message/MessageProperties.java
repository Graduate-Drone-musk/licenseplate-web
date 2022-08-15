package com.graduate.licenseplate.component.message;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Configuration;


@ConfigurationProperties("message")
@Configuration
public class MessageProperties {
	private String coolAccessKey;
	private String coolSecretkKey;
	private String number;
	
	private String serviceId;
	private String naverSecretKey;
	private String naverAccessKey;
	
	public String getCoolAccessKey() {
		return this.coolAccessKey;
	}
	public String getCoolSecretkKey() {
		return this.coolSecretkKey;
	}
	public String getNumber() {
		return this.number;
	}
	
	public void setCoolAccessKey(String coolAccessKey) {
		this.coolAccessKey = coolAccessKey;
	}
	public void setCoolSecretKey(String coolSecretkKey) {
		this.coolSecretkKey = coolSecretkKey;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getNaverSecretKey() {
		return naverSecretKey;
	}
	public void setNaverSecretKey(String naverSecretKey) {
		this.naverSecretKey = naverSecretKey;
	}
	public String getNaverAccessKey() {
		return naverAccessKey;
	}
	public void setNaverAccessKey(String naverAccessKey) {
		this.naverAccessKey = naverAccessKey;
	}
	
	
}
