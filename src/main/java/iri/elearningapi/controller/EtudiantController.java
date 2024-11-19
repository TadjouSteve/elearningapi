package iri.elearningapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import iri.elearningapi.model.courModel.Chapitre;
import iri.elearningapi.model.courModel.QroEtudiant;
import iri.elearningapi.model.userModel.Candidature;
import iri.elearningapi.model.userModel.Etudiant;
import iri.elearningapi.model.userModel.Information;
import iri.elearningapi.service.ContenuFormationService;
import iri.elearningapi.service.etudiant.EtudiantService;
import iri.elearningapi.utils.elearningFunction.Methode;
import iri.elearningapi.utils.form.formInt.FormInscriptionEtudiant;
import iri.elearningapi.utils.form.formInt.FormLink;
import iri.elearningapi.utils.form.formInt.FormQcmForValidation;
import iri.elearningapi.utils.form.formInt.FormResetPassword;
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
		//Methode.printOut("etape 0");
		contenuFormationService.saveReponseToQRO(formLinks, matricule);
		return true;
	}
	
	
	@PostMapping("/etudiant/resetpassword/stepone")
	public boolean resetPasswordStepOne(@RequestBody FormResetPassword formResetPassword) {
		Methode.printOut("etape one - 1");
		etudiantService.generetedCodeForResetPassword(formResetPassword);
		return true;
	}
	
	@PostMapping("/etudiant/resetpassword/steptwo")
	public boolean resetPasswordSteptwo(@RequestBody FormResetPassword formResetPassword) {
		Methode.printOut("etape two - 1");
		etudiantService.resetPasswordWithCode(formResetPassword);
		return true;
	}
	
	@PostMapping("/etudiant/candidatsalonentrepreneur/stepone")
	public Candidature setCandidatureSalon(@RequestBody Candidature formResetPassword) {
		Methode.printOut("etape one candidature - 1");
		return etudiantService.registrationSalonEntrepreneur(formResetPassword);
	
	}
	
	@PostMapping("/etudiant/upload/videosalonentrepreneur/{email}")
	public UserElearning setvideoSalon(@PathVariable("email") String email,@RequestParam("image") MultipartFile file) {
		Methode.printOut("etape one candidature - 2 upload video");
		return etudiantService.uploadVideoCandidatureSalon(file, email);
		
	}
	
	@PostMapping("/etudiant/upload/videosalonentrepreneurtest/{email}")
	public UserElearning uploadFilesWithDifferentNames(@PathVariable("email") String email,
            @RequestParam("video") MultipartFile video,
            @RequestParam("businessPlan") MultipartFile businessPlan,
            @RequestParam(value = "facture", required = false) MultipartFile facture,
            @RequestParam(value = "conformite", required = false) MultipartFile conformite) {
	    

		Methode.printOut("etape one candidature plusieur fichier - 22222 upload video");
		return etudiantService.uploadDocumentsCandidature(video, businessPlan, facture, conformite, email);
	}
	
	
}
