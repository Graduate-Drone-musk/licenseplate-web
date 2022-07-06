package com.graduate.licenseplate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;



@Configuration
public class AwsConfig {
	@Value("${cloud.aws.credentials.access-key}")
	private String accessKey;
	
	@Value("${cloud.aws.credentials.secret-key}")
	private String accessSecret;
	@Value("${cloud.aws.region.static}")
	private String region;
	
	@Bean
	public AmazonS3 s3Client() {
		System.out.println("[ACCESSS KEY] : "+accessKey);
		System.out.println("[accessSecret] : "+accessSecret);
		System.out.println("[region] : "+region);
		BasicAWSCredentials credentialse = new BasicAWSCredentials(accessKey, accessSecret);
		return AmazonS3ClientBuilder.standard()
		        .withRegion(Regions.AP_NORTHEAST_2)
		        .withCredentials(new AWSStaticCredentialsProvider(credentialse))
		        .build();
	}
}
