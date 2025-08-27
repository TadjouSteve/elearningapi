package iri.elearningapi.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class TestController {
	
	@GetMapping("/toto")
	public String test() {
		return "Test Okeee..!202";
	}
}
