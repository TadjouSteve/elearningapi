package iri.elearningapi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import iri.elearningapi.model.courModel.Chapitre;
import iri.elearningapi.model.courModel.QroEtudiant;
import iri.elearningapi.model.userModel.Etudiant;
import iri.elearningapi.model.userModel.Information;
import iri.elearningapi.service.ContenuFormationService;
import iri.elearningapi.service.etudiant.EtudiantService;
import iri.elearningapi.utils.elearningFunction.Methode;
import iri.elearningapi.utils.form.formInt.FormInscriptionEtudiant;
import iri.elearningapi.utils.form.formInt.FormLink;
import iri.elearningapi.utils.form.formInt.FormQcmForValidation;
import iri.elearningapi.utils.form.formOut.FormChapitre;
import iri.elearningapi.utils.form.formOut.UserDashboard;
import iri.elearningapi.utils.form.formOut.UserElearning;


@RestController
@CrossOrigin
public class EtudiantController {
	
	@Autowired
	private EtudiantService etudiantService;
	
	@Autowired
	private ContenuFormationService contenuFormationService;
	
	@GetMapping("/teste")
	public String test1000() {
		System.out.println("on est arrive ici");
		return "Test Okeeee...!";
	}
	
	@PostMapping("/etudiant/enregistrement/")
	public UserElearning saveEtudiant(@RequestBody Etudiant etudiant ) {
		return etudiantService.enregistrementEtudiant(etudiant);
	}
	
	
	@PostMapping("/etudiant/enregistrement002/")
	public UserElearning saveEtudiant002(@RequestBody FormInscriptionEtudiant formInscriptionEtudiant ) {
		Etudiant etudiant=formInscriptionEtudiant.getEtudiant();
		List<Information> informations=new ArrayList<Information>();
		informations.add(formInscriptionEtudiant.getInformation());
		etudiant.setInformations(informations);
		return etudiantService.enregistrementEtudiant(etudiant);
	}
	
	
	@GetMapping("/etudiant/dashboard/{idEtudiant}")
	public UserDashboard getEtudiantDashboard(@PathVariable("idEtudiant") int idEtudiant) {
		return etudiantService.getEtudiantDashoard(idEtudiant);
	}
	
	@GetMapping("/etudiant/module/{idEtudiant}/{idModule}")
	public List<FormChapitre> getEtudiantModule(@PathVariable("idEtudiant") int idEtudiant,@PathVariable("idModule") int idModule) {
		return etudiantService.getAllChapitreModuleEtudiant(idEtudiant, idModule);
	}
	
	
	@GetMapping("/etudiant/chapitre/{idEtudiant}/{idChapitre}")
	public Chapitre getEtudiantChapitre(@PathVariable("idEtudiant") int idEtudiant,@PathVariable("idChapitre") int idChapitre) {
		return etudiantService.getChapitreByEtudiant(idEtudiant,idChapitre);
	}
	
	@PostMapping("/etudiant/validerqcm/{idEtudiant}/{idChapitre}")
	public FormQcmForValidation validationQCM(@RequestBody List<FormQcmForValidation> formQcmForValidations, @PathVariable("idEtudiant") int idEtudiant,@PathVariable("idChapitre") int idChapitre) {
		return etudiantService.getValidationQcmEtudiant(formQcmForValidations, idEtudiant, idChapitre);
	}
	
	@GetMapping("/etudiant/reponseqro/{matricule}/{idQro}")
	public QroEtudiant getMethodName(@PathVariable("matricule") String matricule,@PathVariable("idQro") int idQro) {
		return contenuFormationService.getReponseQROByEtudiant(idQro, matricule);
	}
	
	@PostMapping("/etudiant/reponseqro/{matricule}")
	public boolean saveReponseEtudiantToQro(@RequestBody List<FormLink> formLinks,@PathVariable("matricule") String matricule) {
		Methode.printOut("etape 0");
		contenuFormationService.saveReponseToQRO(formLinks, matricule);
		return true;
	}
	
	
	
	
}
