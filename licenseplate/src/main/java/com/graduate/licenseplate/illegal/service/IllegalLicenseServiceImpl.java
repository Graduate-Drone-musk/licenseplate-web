package com.graduate.licenseplate.illegal.service;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.graduate.licenseplate.aws.model.IllegalImage;
import com.graduate.licenseplate.aws.model.IllegalResultVO;
import com.graduate.licenseplate.aws.service.AwsFileService;
import com.graduate.licenseplate.component.message.MessageService;
import com.graduate.licenseplate.component.message.model.NaverMessageResponse;
import com.graduate.licenseplate.illegal.entity.IllegalLicense;
import com.graduate.licenseplate.illegal.model.IllegalCondition;
import com.graduate.licenseplate.illegal.model.MessageRequest;
import com.graduate.licenseplate.illegal.model.MessageResponse;
import com.graduate.licenseplate.illegal.repository.IllegalLicenseRepository;
import com.graduate.licenseplate.member.entity.IllegalMember;

import net.nurigo.java_sdk.exceptions.CoolsmsException;

@Service
public class IllegalLicenseServiceImpl implements IllegalLicenseService{
	@Value("${cloud.application.bucket.name}")
	private String bucketName;
	
	private final AmazonS3 s3;

	private final IllegalLicenseRepository illegalLicenseRepository;
	
	private final MessageService messageService;
	
	public IllegalLicenseServiceImpl(
			AmazonS3 s3,
			AwsFileService awsFileService,
			IllegalLicenseRepository illegalLicenseRepository,
			MessageService messageService) {
		this.s3 = s3;
		this.illegalLicenseRepository = illegalLicenseRepository;
		this.messageService = messageService;
	}
	
	// 유효시간 검사
	public Date getExpiration() {
		Date expiration = new Date();
		long expTimeMills = Instant.now().toEpochMilli();
		expTimeMills += 1000*60*60*24; 
		expiration.setTime(expTimeMills);
		return expiration;
	}
	
	public GeneratePresignedUrlRequest getPresignedUrl(String url, Date expiration) {
		return new GeneratePresignedUrlRequest(bucketName, url)
				.withMethod(HttpMethod.GET)
				.withExpiration(expiration);
	}
	
	public List<IllegalImage> setIllegalImage(List<IllegalLicense> licenseList, Date expiration) {
		List<IllegalImage> setList = new ArrayList<>();
		for(IllegalLicense license : licenseList) {
			IllegalMember member = license.getIllegalMember();
			
			GeneratePresignedUrlRequest presignedOriginUrl = getPresignedUrl(license.getOriginUrl(), expiration);
			GeneratePresignedUrlRequest presignedLpUrl = getPresignedUrl(license.getLpUrl(), expiration);

			URL originUrl = s3.generatePresignedUrl(presignedOriginUrl);
			URL lpUrl = s3.generatePresignedUrl(presignedLpUrl);
			
			setList.add(IllegalImage.builder()
					.id(license.getIllegalId().toString())
					.phone(member.getPhone())
					.userName(member.getUserName())
					.date(license.getDate().toString()+"_"+license.getTime().toString())
					.licenseplate(license.getLicenseplate())
					.lpUrl(lpUrl.toString())
					.originUrl(originUrl.toString())
					.build());
		}
		
		return setList;
	}
	
	public Integer getMaxPage(Integer maxPageCount, Integer showCount) {
		Integer maxPage = 0;
		if(maxPageCount % showCount != 0) {
			maxPage = 1;
		}
		return maxPage + maxPageCount / showCount;
	}
	
	@Override
	public IllegalResultVO getUrlWithCondition(IllegalCondition illegalCondition) {
		if (illegalCondition.getStartTime().isEmpty() && !illegalCondition.getEndTime().isEmpty()){
			return IllegalResultVO.builder()
					.status("startTimeError")
					.message("시작 조건이 없습니다.")
					.illegalList(null)
					.allPageCount("-1")
					.build();
		}
		if (!illegalCondition.getStartTime().isEmpty() && illegalCondition.getEndTime().isEmpty()){
			return IllegalResultVO.builder()
					.status("endTimeError")
					.message("끝 조건이 없습니다.")
					.illegalList(null)
					.allPageCount("-1")
					.build();
		}
		
		// 만료 시간
		Date expiration = getExpiration();
		
		Integer showCount = Integer.valueOf(illegalCondition.getShowCount());
		Integer page = Integer.valueOf(illegalCondition.getPage()); 
		Integer startIndex = (page-1) * (showCount);
		
		Integer maxPageCount = Integer.valueOf(illegalCondition.getAllPageCount());
		
		
		if(illegalCondition.getStartTime().isEmpty() && illegalCondition.getEndTime().isEmpty()) {
			// Get All process to page and showCount
			
			if(maxPageCount == -1) {
				maxPageCount = illegalLicenseRepository.findCountById();
			}
			
			
			List<IllegalLicense> licenseList = illegalLicenseRepository.findLimitShowCountByDate(
					startIndex,
					showCount);

			return IllegalResultVO.builder()
					.status("Good")
					.message("전체 검색")
					.allPageCount(getMaxPage(maxPageCount, showCount).toString())
					.illegalList(setIllegalImage(licenseList, expiration))
					.build();
		}
		
		if(maxPageCount == -1) {
			maxPageCount = illegalLicenseRepository.findCountByCondition(
					Integer.valueOf(illegalCondition.getStartTime()),
					Integer.valueOf(illegalCondition.getEndTime()));
		}
		
		List<IllegalLicense> licenseList = illegalLicenseRepository.findIllegalByStartAndEnd(
				Integer.valueOf(illegalCondition.getStartTime()),
				Integer.valueOf(illegalCondition.getEndTime()),
				startIndex,
				showCount);
		
		return IllegalResultVO.builder()
				.status("Good")
				.message("부분 검색.")
				.allPageCount(getMaxPage(maxPageCount, showCount).toString())
				.illegalList(setIllegalImage(licenseList, expiration))
				.build();
	}

	@Override
	public IllegalResultVO getUrlWithHuman(IllegalCondition illegalCondition) {
		// search human
		List<IllegalLicense> licenseList = illegalLicenseRepository.findByIllegalMember_RegistrationNumber(illegalCondition.getRegistNum());
		
		Integer maxPageCount = Integer.valueOf(illegalCondition.getAllPageCount());
		
		Date expiration = getExpiration();
		
		return IllegalResultVO.builder()
				.status("Good")
				.message("부분 검색.")
				.allPageCount(Integer.toString(licenseList.size()))
				.illegalList(setIllegalImage(licenseList, expiration))
				.build();
	}
	
	@Override
	public MessageResponse sendOneMessage(MessageRequest messageRequest) throws CoolsmsException {		
		Optional optionalLicense = illegalLicenseRepository.findById(Long.parseLong(messageRequest.getId()));
		if(optionalLicense.isEmpty()) {
			return MessageResponse.builder()
					.status("Bad")
					.message("messageRequest를 받지 못했습니다.")
					.build();
		}
		
		IllegalLicense license = (IllegalLicense)optionalLicense.get();
		URL originUrl = s3.generatePresignedUrl(getPresignedUrl(license.getOriginUrl(), getExpiration()));
		System.out.println(originUrl.toString());
		messageService.sendOneMessage(license, originUrl.toString());
		return null;
	}
	
	@Override
	public NaverMessageResponse sendMessage(MessageRequest messageRequest) throws InvalidKeyException, RestClientException, JsonProcessingException, UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException {
		Optional optionalLicense = illegalLicenseRepository.findById(Long.parseLong(messageRequest.getId()));
		if(optionalLicense.isEmpty()) {
			return NaverMessageResponse.builder()
					.statusCode("500")
					.build();
		}
		List<IllegalLicense> licenseList = new ArrayList<>();
		IllegalLicense license = (IllegalLicense)optionalLicense.get();
		URL originUrl = s3.generatePresignedUrl(getPresignedUrl(license.getOriginUrl(), getExpiration()));
		System.out.println(originUrl.toString());
		licenseList.add(license);
		
		return messageService.sendNaverMessage(licenseList, originUrl.toString());
	}
}
