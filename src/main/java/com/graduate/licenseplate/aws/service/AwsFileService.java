package com.graduate.licenseplate.aws.service;

import java.util.List;

import com.graduate.licenseplate.aws.model.IllegalResultVO;

public interface AwsFileService {
	IllegalResultVO getUrlWithCondition(String startTime, String endTime);
	String updateTestDB(String today);
	String uploadImgfile();
	String updateDB(String today, List<String> fileData);
}
