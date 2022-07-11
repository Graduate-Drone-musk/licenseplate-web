package com.graduate.licenseplate.aws.service;

import java.util.List;

import com.graduate.licenseplate.aws.model.IllegalResultVO;

public interface AwsFileService {
	String updateTestDB(String today);
	String uploadImgfile();
	String updateDB(String today, List<String> fileData);
	String getToday();
}
