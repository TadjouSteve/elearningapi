package iri.elearningapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import iri.elearningapi.service.AdminService;
import iri.elearningapi.service.EtudiantService;
import iri.elearningapi.service.JWTService;
import iri.elearningapi.utils.errorClass.ElearningException;
import iri.elearningapi.utils.errorClass.ErrorAPI;
import iri.elearningapi.utils.form.formOut.UserElearning;

@RestController
@CrossOrigin
public class LoginController {
	@Autowired
	private EtudiantService etudiantService;
	
	@Autowired
	private  AdminService adminService;

	private JWTService jwtService;
	
	public LoginController(JWTService jwtService) {
		this.jwtService = jwtService;
	}
	
	@PostMapping("/login01")
	public String getToken(Authentication authentication) {
        		String token = jwtService.generateToken(authentication);
        		return token;
	}
	
	@PostMapping("/login/")
	public UserElearning getConnexion(@RequestBody UserElearning userElearning) {
		
		if (etudiantService.getLogin(userElearning)!=null) {
			return etudiantService.getLogin(userElearning);
		}else if(adminService.getLoginAdmin(userElearning)!=null) {
			return adminService.getLoginAdmin(userElearning);
		}else {
			throw new ElearningException(new ErrorAPI("Information de connexion incorrect...!",0));
		}
	}
	
}