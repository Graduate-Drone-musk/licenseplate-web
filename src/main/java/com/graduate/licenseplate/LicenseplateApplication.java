package com.graduate.licenseplate;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class LicenseplateApplication {
		
	public static final String APP_LOCATIONS = "spring.config.location="
			+"classpath:/application.yaml,"
			+"classpath:/application-secret.yaml";
	
	public static void main(String[] args) {
		new SpringApplicationBuilder(LicenseplateApplication.class)
				.properties(APP_LOCATIONS)
				.run(args);
	}

}
