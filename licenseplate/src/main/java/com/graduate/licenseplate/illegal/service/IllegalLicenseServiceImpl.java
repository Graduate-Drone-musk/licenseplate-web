package com.graduate.licenseplate.illegal.service;

import java.net.URISyntaxException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.graduate.licenseplate.aws.model.IllegalImage;
import com.graduate.licenseplate.aws.model.IllegalResultVO;
import com.graduate.licenseplate.aws.service.AwsFileService;
import com.graduate.licenseplate.illegal.entity.IllegalLicense;
import com.graduate.licenseplate.illegal.model.IllegalCondition;
import com.graduate.licenseplate.illegal.repository.IllegalLicenseRepository;
import com.graduate.licenseplate.member.entity.IllegalMember;

@Service
public class IllegalLicenseServiceImpl implements IllegalLicenseService{
	@Value("${cloud.application.bucket.name}")
	private String bucketName;
	
	private final AmazonS3 s3;

	private final IllegalLicenseRepository illegalLicenseRepository;
	
	public IllegalLicenseServiceImpl(
			AmazonS3 s3,
			AwsFileService awsFileService,
			IllegalLicenseRepository illegalLicenseRepository) {
		this.s3 = s3;
		this.illegalLicenseRepository = illegalLicenseRepository;
	}
	
	// 유효시간 검사
	public Date getExpiration() {
		Date expiration = new Date();
		long expTimeMills = Instant.now().toEpochMilli();
		expTimeMills += 1000*60*60*24; 
		expiration.setTime(expTimeMills);
		return expiration;
	}
	
	public List<IllegalImage> setIllegalImage(List<IllegalLicense> licenseList, Date expiration) {
		List<IllegalImage> setList = new ArrayList<>();
		for(IllegalLicense license : licenseList) {
			IllegalMember member = license.getIllegalMember();
			
			GeneratePresignedUrlRequest presignedOriginUrl = new GeneratePresignedUrlRequest(bucketName, license.getOriginUrl())
					.withMethod(HttpMethod.GET)
					.withExpiration(expiration);
			GeneratePresignedUrlRequest presignedLpUrl = new GeneratePresignedUrlRequest(bucketName, license.getLpUrl())
					.withMethod(HttpMethod.GET)
					.withExpiration(expiration);
			URL originUrl = s3.generatePresignedUrl(presignedOriginUrl);
			URL lpUrl = s3.generatePresignedUrl(presignedLpUrl);
			
			setList.add(IllegalImage.builder()
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
}
