package com.graduate.licenseplate.drone.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.graduate.licenseplate.Secret;

@Controller
public class MapController {

	@GetMapping("/drone/map")
	public String drone(Model model) {
		
		model.addAttribute("clientId", Secret.CLIENT_ID);
		model.addAttribute("clientSecret", Secret.CLIENT_SECRET);
		return "redirect:drone/map";
	}
}
