package com.graduate.licenseplate.aws.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.graduate.licenseplate.aws.model.IllegalImage;
import com.graduate.licenseplate.aws.model.IllegalResultVO;
import com.graduate.licenseplate.aws.model.IllegalReturn;
import com.graduate.licenseplate.illegal.entity.IllegalLicense;
import com.graduate.licenseplate.illegal.entity.OriginLicense;
import com.graduate.licenseplate.illegal.model.IllegalLicenseVO;
import com.graduate.licenseplate.illegal.repository.IllegalLicenseRepository;
import com.graduate.licenseplate.illegal.repository.OriginLicenseRepository;
import com.graduate.licenseplate.member.entity.IllegalMember;
import com.graduate.licenseplate.member.entity.OriginMember;
import com.graduate.licenseplate.member.repository.IllegalMemberRepository;
import com.graduate.licenseplate.member.repository.OriginMemberRepository;

@Service
public class AwsFileServiceImpl implements AwsFileService{

	@Value("${cloud.application.bucket.name}")
	private String bucketName;
	
	private final AmazonS3 s3;
	private final IllegalLicenseRepository illegalLicenseRepository;
	private final IllegalMemberRepository illegalMemberRepository;
	
	private final OriginLicenseRepository originLicenseRepository;
	private final OriginMemberRepository originMemberRepository;
	
	
	public AwsFileServiceImpl(AmazonS3 s3, 
			IllegalLicenseRepository illegalLicenseRepository, IllegalMemberRepository illegalMemberRepository,
			 OriginLicenseRepository originLicenseRepository, OriginMemberRepository originMemberRepository) {
		this.s3 = s3;
		this.illegalLicenseRepository = illegalLicenseRepository;
		this.illegalMemberRepository = illegalMemberRepository;
		this.originLicenseRepository = originLicenseRepository;
		this.originMemberRepository = originMemberRepository;
	}
	
	// 현재 날짜
	@Override
	public String getToday() {
		LocalDate now = LocalDate.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
		return now.format(format);
	}

	public IllegalReturn addOriginData(IllegalLicenseVO licenseVo) {
		OriginMember member = OriginMember.builder()
				.registrationNumber("1234560123456")
				.userName("정민규")
				.phone("01022348115")
				.totalPenalty(0)
				.build();
		OriginLicense license = OriginLicense.builder()
				.licenseplate(licenseVo.getLicenseplate())
				.accidentCount(0)
				.member(member)
				.build();
		
		return IllegalReturn.builder()
				.originLicense(license)
				.originMember(member)
				.build();
		
	}

	
	public IllegalMember getOrigin2Illegalmember(OriginMember member) {
		return IllegalMember.builder()
		.registrationNumber(member.getRegistrationNumber())
		.userName(member.getUserName())
		.phone(member.getPhone())
		.build();
	}

	public IllegalReturn addIllegalLicense(IllegalLicenseVO licenseVo) {
		Optional<OriginLicense> optionalLicense = originLicenseRepository.findById(licenseVo.getLicenseplate());
		
		if(!optionalLicense.isPresent()) {
			// 유저 정보 X, 번호판 X
			System.out.println("번호판이 아닙니다.");
			return null;
		}
		
		OriginLicense license = optionalLicense.get();
		OriginMember member = license.getMember();
			
		IllegalMember illegalMember = getOrigin2Illegalmember(member);
		IllegalLicense illegalLicense = IllegalLicense.builder()
				.licenseplate(license.getLicenseplate())
				.date(Integer.valueOf(licenseVo.getDate()))
				.time(Integer.valueOf(licenseVo.getTime()))
				.lpUrl(licenseVo.getLpUrl())
				.originUrl(licenseVo.getOriginUrl())
				.illegalMember(illegalMember)
				.build();
		
		return IllegalReturn.builder()
				.illegalLicense(illegalLicense)
				.illegalMember(illegalMember)
				.build();
	}

	@Override
	public String updateTestDB(String today) {
		ListObjectsV2Request prefix = new ListObjectsV2Request().withBucketName(bucketName)
				.withPrefix("illegal_file/"+today);
		ListObjectsV2Result result = s3.listObjectsV2(prefix);
		List<S3ObjectSummary> objects = result.getObjectSummaries();
		
		List<IllegalImage> illegalList = new ArrayList<IllegalImage>();
		
		StringBuilder origin = new StringBuilder();
		List<OriginMember> memberList = new ArrayList<>();
		List<OriginLicense> licenseList = new ArrayList<>();
		
		for (S3ObjectSummary ob: objects) {	
			String path = ob.getKey();
			String[] pathList = path.split("/");
			String[] fileName = pathList[3].split("__");
			
			if (fileName.length == 2) {
				
				String[] lastName = fileName[0].split("_");
				origin.append(pathList[0]+"/");
				origin.append(pathList[1]+"/");
				origin.append(pathList[2]+"/");
				origin.append(lastName[0]+"_");
				origin.append(lastName[1]+".jpg");
				
				IllegalLicenseVO licenseVo = IllegalLicenseVO.builder()
						.licenseplate(lastName[2])
						.date(today)
						.lpUrl(path)
						.originUrl(origin.toString())
						.build();
				
				
				IllegalReturn illegalReturn = addOriginData(licenseVo);
				memberList.add(illegalReturn.getOriginMember());
				licenseList.add(illegalReturn.getOriginLicense());
				
				origin.delete(0, origin.length());
			}	
		}
		originMemberRepository.saveAll(memberList);
		originLicenseRepository.saveAll(licenseList);
		
		return null;
	}
	
	@Override
	public String updateDB(String today, List<String> fileData) {
		// TODO Auto-generated method stub
		System.out.println(today);
		
		ListObjectsV2Request prefix = new ListObjectsV2Request().withBucketName(bucketName)
				.withPrefix("illegal_file/"+today);
		ListObjectsV2Result result = s3.listObjectsV2(prefix);
		List<S3ObjectSummary> objects = result.getObjectSummaries();
				
		
		StringBuilder origin = new StringBuilder();
		
		Set<String> setPath = new HashSet<String>();
		List<IllegalMember> memberList = new ArrayList<>();
		List<IllegalLicense> licenseList = new ArrayList<>();
		HashMap<String, Integer> detectCount = new HashMap<>();
		
		
		for (S3ObjectSummary ob: objects) {	
			String path = ob.getKey();
			String[] pathList = path.split("/");

			// 이미 올린 데이터 & check.txt 할 필요 X
			if("check.txt".equals(pathList[2])
					|| fileData.contains(pathList[2])) {
				continue;
			}
			
			String[] fileName = pathList[3].split("__");	
			
			if (fileName.length == 2) {
				setPath.add(pathList[2]);
				
				String[] lastName = fileName[0].split("_");
				origin.append(pathList[0]+"/");
				origin.append(pathList[1]+"/");
				origin.append(pathList[2]+"/");
				origin.append(lastName[0]+"_");
				origin.append(lastName[1]+".jpg");
				
				IllegalLicenseVO licenseVo = IllegalLicenseVO.builder()
						.licenseplate(lastName[2])
						.date(today)
						.time(lastName[0])
						.lpUrl(path)
						.originUrl(origin.toString())
						.build();	
				
				IllegalReturn illegalReturn = addIllegalLicense(licenseVo);
				
				memberList.add(illegalReturn.getIllegalMember());
				licenseList.add(illegalReturn.getIllegalLicense());
				
				origin.delete(0, origin.length());
			}	
		}
		uploadCheckText(setPath, prefix.getPrefix(), "");
		
		illegalMemberRepository.saveAll(memberList);
		illegalLicenseRepository.saveAll(licenseList);

		return null;
	}
	
	public boolean uploadCheckText(Set<String> setPath, String prefix, String existData) {
		StringBuffer uploadData = new StringBuffer();
		uploadData.append(existData);
		
		// 데이터 넣음
		try {
			Iterator<String> it = setPath.iterator();
			while(it.hasNext()) {
				uploadData.append(it.next().toString()+"\n");
			}
			
			// 새 텍스트 파일 생성
			s3.putObject(bucketName, prefix+"/check.txt", uploadData.toString());
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	@Override
	public String uploadImgfile() {
		String today = getToday();
//		
		List<String> fileData = new ArrayList<String>();
		System.out.println(fileData);
		try {
			S3Object object = s3.getObject(new GetObjectRequest(bucketName, "illegal_file/"+today+"/check2.txt"));
			System.out.println("[Text File Exist]");
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(object.getObjectContent()));
			
			String temp=null;
			while((temp = reader.readLine()) != null) {
				fileData.add(temp);
			}
			
			System.out.println(String.join("\n", fileData));
			updateDB(today, fileData);
			
		} catch (AmazonS3Exception e) {
			// txt 파일 없을 때
			if ("NoSuchKey".equals(e.getErrorCode())) {
				System.out.println("NO file");
				updateDB(today, fileData);
			}
			else {
				System.out.println("[Error] : "+e.getErrorMessage());
			}
		}
		catch (Exception e) {
			// File read Error
			System.out.println(e);
		}
		
		return null;
	}
}
