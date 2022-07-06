package com.graduate.licenseplate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/dev")
@Controller
public class Main {
	
	@GetMapping("/main")
	public String index() {
		return "index";
	}
}
