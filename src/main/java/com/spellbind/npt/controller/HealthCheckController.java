package com.spellbind.npt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

	@GetMapping("/healthCheck")
	public String getDepartments() {
		return "Application is working";
	}

}
