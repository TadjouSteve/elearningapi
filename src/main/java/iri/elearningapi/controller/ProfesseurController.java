package iri.elearningapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import iri.elearningapi.service.ProfesseurService;
import iri.elearningapi.utils.URLElearning;
import iri.elearningapi.utils.form.formInt.FormLink;
import iri.elearningapi.utils.form.formOut.FormCorrectionQro;
import iri.elearningapi.utils.form.formOut.FormModule;





@RestController
@CrossOrigin
@RequestMapping("/professeur")
public class ProfesseurController {
	
	@Autowired
	private ProfesseurService professeurService;
	
	@GetMapping("/list/modules")
	public List<FormModule> getListModules() {
		return professeurService.getListModules();
	}
	
	@GetMapping("/next/etudiantchapitre/{idChapitre}")
	public FormCorrectionQro getMethodName(@PathVariable("idChapitre") int idChapitre) {
		return professeurService.getFormCorrectionQro(idChapitre);
	}
	
	@PostMapping("/savescrore/qro/")
	public URLElearning postScroreQcm(@RequestBody List<FormLink> formLinks) {
		//TODO: process POST request
		professeurService.setScoreQRO(formLinks);
		return new URLElearning();
	}
	
	
	

}
