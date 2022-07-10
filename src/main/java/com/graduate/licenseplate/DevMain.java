package com.graduate.licenseplate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Env;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/dev")
@Controller
public class DevMain {
	@GetMapping("/main")
	public String devIndex() {
		return "devIndex";
	}
}
