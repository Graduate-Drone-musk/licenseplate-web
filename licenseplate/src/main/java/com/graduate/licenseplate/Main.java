package com.graduate.licenseplate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Env;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Main implements ErrorController{
	@Value("${spring.datasource.username}")
	String name;
	
	@Value("${spring.datasource.url}")
	String url;
	
	@GetMapping("/error")
	public String index() {
		return "../static/index.html";
	}
}
