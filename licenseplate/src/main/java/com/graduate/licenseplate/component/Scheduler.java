package com.graduate.licenseplate.component;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.graduate.licenseplate.aws.service.AwsFileService;

@Component
public class Scheduler {
	private AwsFileService awsFileService;
	
	public Scheduler(AwsFileService awsFileService) {
		this.awsFileService = awsFileService;
	}
	
	// 30분마다
	@Scheduled(cron = "0 0/30 9-18 * *")
	public void updateLicenseDB() {
		awsFileService.uploadImgfile();
	}
}
