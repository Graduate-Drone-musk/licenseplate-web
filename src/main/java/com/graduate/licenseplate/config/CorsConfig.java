package com.graduate.licenseplate.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer{
	@Value("${spring.datasource.username}")
	String name;
	
	@Value("${spring.datasource.url}")
	String url;
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		System.out.println(name);
		registry.addMapping("/api/**")
				.allowedOrigins("http://localhost:3000", "http://54.180.70.202:8080", "*")
				.allowedMethods("GET", "POST");
	}
} 