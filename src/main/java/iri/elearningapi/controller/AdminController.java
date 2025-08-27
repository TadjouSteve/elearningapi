package iri.elearningapi.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import iri.elearningapi.model.courModel.Chapitre;
import iri.elearningapi.model.courModel.Module;
import iri.elearningapi.model.courModel.Proposition;
import iri.elearningapi.model.courModel.Qcm;
import iri.elearningapi.model.courModel.Qro;
import iri.elearningapi.model.mediaModel.Article;
import iri.elearningapi.model.mediaModel.Rubrique;
import iri.elearningapi.model.userModel.Candidature;
import iri.elearningapi.model.userModel.Etudiant;
import iri.elearningapi.model.userModel.Professeur;
import iri.elearningapi.service.AdminService;
import iri.elearningapi.service.ContenuFormationService;
import iri.elearningapi.service.ImageService;
import iri.elearningapi.service.MediaService;
import iri.elearningapi.service.ProfesseurService;
import iri.elearningapi.service.etudiant.EtudiantService;
import iri.elearningapi.utils.URLElearning;
import iri.elearningapi.utils.form.formInt.FormFilter;
import iri.elearningapi.utils.form.formInt.FormLink;
import iri.elearningapi.utils.form.formInt.FormMail;
import iri.elearningapi.utils.form.formInt.FormQCM;
import iri.elearningapi.utils.form.formOut.FormChapitre;
import iri.elearningapi.utils.form.formOut.FormViewChapitre;
import iri.elearningapi.utils.form.formOut.FormViewEtudiant;
import iri.elearningapi.utils.form.formOut.FormViewModule;
import iri.elearningapi.utils.form.formOut.FormViewProfesseur;
import iri.elearningapi.utils.form.formOut.UserAdminDashboard;


@RestController
@CrossOrigin
public class AdminController {

	@Autowired
	private AdminService adminService;

	@Autowired
	private ProfesseurService professeurService;

	@Autowired
	private EtudiantService etudiantService;

	@Autowired
	private MediaService mediaService;

	@Autowired
	private ImageService imageService;
	
	@Autowired
	private ContenuFormationService contenuFormationService; 

	@GetMapping("/admin/dashboard")
	public UserAdminDashboard getAdminDashboard() {
		return adminService.getAdminDashboard();
	}

	@GetMapping("/admin/etudiants/{pageNumber}")
	public Page<Etudiant> getAllEtudiant(@PathVariable("pageNumber") int pageNumber) {
		return etudiantService.getListEtudiants(null, pageNumber);
	}
	
	@GetMapping("/admin/candidature/{idCandidature}")
	public Candidature getCandidature(@PathVariable("idCandidature") int idCandidature) {
		return etudiantService.getCandidature(idCandidature);
	}
	
	@GetMapping("/admin/etudiant/{matricule}")
	public FormViewEtudiant getFormViewEtudiantByAdmin(@PathVariable("matricule") String matriculeEtudiant) {
		return etudiantService.getFormViewEtudiantForAdmin(matriculeEtudiant, null);
	}
	
	@GetMapping("/admin/candidatures/{pageNumber}")
	public Page<Candidature> getAllCandidature(@PathVariable("pageNumber") int pageNumber) {
		return etudiantService.getListCandidaturePage(null, pageNumber);
	}

	

	@GetMapping("/admin/professeurs/{pageNumber}")
	public Page<Professeur> getAllProfesseur(@PathVariable("pageNumber") int pageNumber) {
		return professeurService.getListProfesseurs(null, pageNumber);
	}

	@GetMapping("/admin/professeur/{matricule}")
	public FormViewProfesseur getFormProfesseur(@PathVariable("matricule") String matriculeProf) {
		return professeurService.getFormViewProfesseur(matriculeProf);
	}

	@PostMapping("/admin/professeur")
	public Professeur addProfesseur(@RequestBody Professeur professeur) {
		return professeurService.createProfesseur(professeur);
	}

	@PostMapping("/admin/linkprofesseurtomodule/{matricule}")
	public boolean linkProfesseurToMatricule(@PathVariable("matricule") String matriculeProf,
			@RequestBody List<FormLink> formLinks) {
		professeurService.linkProfesseurToModule(matriculeProf, formLinks);
		System.out.println("linked module to prof is Ok");
		return true;
	}

	/*
	 * admin controler for article and media action
	 * 
	 * 
	 */
	@PostMapping("/admin/media/rubrique")
	public URLElearning createRubrique(@RequestBody Rubrique rubrique) {
		Rubrique rubrique2 = mediaService.createOrUpdateRubrique(rubrique, true);
		return new URLElearning("/rubrique/" + rubrique2.getId());
	}

	@PutMapping("/admin/media/rubrique")
	public URLElearning updateRubrique(@RequestBody Rubrique rubrique) {
		Rubrique rubrique2 = mediaService.createOrUpdateRubrique(rubrique, false);
		return new URLElearning("/rubrique/" + rubrique2.getId());
	}

	@PostMapping("/admin/media/article/{idRubrique}")
	public URLElearning createArticleByRubrique(@PathVariable("idRubrique") int idRubrique,
			@RequestBody Article article) {
		Article article2 = mediaService.createOrUpdateArticle(article, idRubrique, true);
		return new URLElearning("/article/" + article2.getId());
	}

	@PutMapping("/admin/media/article/{idRubrique}")
	public URLElearning UpdateArticleByRubrique(@PathVariable("idRubrique") int idRubrique,
			@RequestBody Article article) {
		Article article2 = mediaService.createOrUpdateArticle(article, idRubrique, false);
		return new URLElearning("/article/" + article2.getId());
	}

	@GetMapping("/admin/media/changestatutarticle/{idArticle}")
	public Boolean getMethodName(@PathVariable("idArticle") int idArticle) {
		System.out.println(" change statut actif");
		return mediaService.changeStatutArticle(idArticle);
	}

	@PostMapping("/admin/imagearticle/{idArticle}")
	public URLElearning uploadImageArticle(@PathVariable("idArticle") int idArticle,
			@RequestParam("image") MultipartFile file) throws IOException {
		imageService.uploadImageArticle(file, idArticle);
		return new URLElearning("/articles", true);
	}

	@DeleteMapping("/admin/imagearticle/{idImage}")
	public URLElearning deleteImageArticle(@PathVariable("idImage") int idImage) {
		imageService.deleteImageArticle(idImage);
		System.out.println("Supprimmer image:" + idImage);
		return new URLElearning();
	}

	@PostMapping("/admin/imagearticle/addtitre/{idImage}")
	public URLElearning addTitreToImageArticle(@PathVariable("idImage") int idImage, @RequestBody String titre) {
		imageService.ajoutTitreImageArticle(idImage, titre);
		return new URLElearning();
	}
	
	/*
	 * admin controler for Contenu formation
	 * 
	 */

	@GetMapping("/admin/modules")
	public List<Module> getAllModule() {
		return contenuFormationService.getListModuule(null);
	}
	
	@GetMapping("/admin/module/{idModule}")
	public FormViewModule getModuleById(@PathVariable("idModule") int idModule) {
		return contenuFormationService.getFormViewModule(idModule);
	}
	
	@PostMapping("/admin/module")
	public URLElearning createModule(@RequestBody Module module) {
		//TODO: process POST request
		Module module2=contenuFormationService.createOrUpdateModule(module, true);
		URLElearning urlElearning=new URLElearning("/module/"+module2.getIdModule());
		return urlElearning;
	}
	
	@PutMapping("/admin/module")
	public URLElearning updateModule(@RequestBody Module module) {
		//TODO: process POST request
		Module module2=contenuFormationService.createOrUpdateModule(module, false);
		URLElearning urlElearning=new URLElearning("/module/"+module2.getIdModule());
		return urlElearning;
	}
	
	@PostMapping("/admin/linkgammmetudianttomodule/{idModule}")
	public boolean linkProfesseurToMatricule(@PathVariable("idModule") int idModule,
			@RequestBody List<FormLink> formLinks) {
		contenuFormationService.linkGammeEtudiantToModule(idModule, formLinks);
		//System.out.println("linked gammeetudiant to module is Ok");
		return true;
	}
	
	
	@GetMapping("/admin/cours")
	public List<FormChapitre> getMethodName() {
		return contenuFormationService.getListChapitre(null);
	}
	
	@GetMapping("/admin/cour/{idChapitre}")
	public FormViewChapitre getFormViewChapitre(@PathVariable("idChapitre") int idChapitre) {
		return contenuFormationService.getFormViewChapitre(idChapitre);
	}
	
	@PostMapping("/admin/chapitre")
	public URLElearning createChapitre(@RequestBody Chapitre chapitre) {
		//TODO: process POST request
		Chapitre chapitre2=contenuFormationService.createOrUpdateChapitre(chapitre, true);
		URLElearning urlElearning=new URLElearning("/cour/"+chapitre2.getIdChapitre());
		return urlElearning;
	}
	
	@PutMapping("/admin/chapitre")
	public URLElearning updateChapitre(@RequestBody Chapitre chapitre) {
		//TODO: process PUT request
		Chapitre chapitre2=contenuFormationService.createOrUpdateChapitre(chapitre, false);
		URLElearning urlElearning=new URLElearning("/cour/"+chapitre2.getIdChapitre());
		return urlElearning;
	}
	
	@PostMapping("/admin/qro/{idChapitre}")
	public URLElearning createQRO(@RequestBody Qro qro, @PathVariable("idChapitre") int IdChapitre) {
		//TODO: process POST request
		contenuFormationService.createOrUpdateQRO(qro, IdChapitre);
		URLElearning urlElearning=new URLElearning("/cour/"+IdChapitre);
		return urlElearning;
	}
	
	@GetMapping("/admin/qcm/{idQcm}")
	public Qcm getQcm(@PathVariable("idQcm") int idQcm) {
		return contenuFormationService.getQCM(idQcm);
	}
	
	@PostMapping("/admin/qcm/")
	public URLElearning createQCM(@RequestBody FormQCM froFormQCM) {
		//TODO: process POST request
		Qcm qcm=contenuFormationService.createQCM(froFormQCM);
		URLElearning urlElearning=new URLElearning("/qcm/"+qcm.getChapitre().getIdChapitre()+"/"+qcm.getId());
		return urlElearning;
	}
	
	@PutMapping("/admin/qcm")
	public URLElearning updateQCM(@RequestBody Qcm qcm) {
		//TODO: process POST request
		Qcm qcm02=contenuFormationService.updateQcm(qcm);
		URLElearning urlElearning=new URLElearning("/qcm/"+qcm02.getChapitre().getIdChapitre()+"/"+qcm02.getId());
		return urlElearning;
	}
	
	@PostMapping("/admin/qcmproposition/{idQCM}")
	public URLElearning createOrUpdatePropositionQCM(@PathVariable("idQCM") int idQCM,@RequestBody Proposition proposition) {
		contenuFormationService.CreateOrUpdatePropositionQCM(idQCM, proposition);
		URLElearning urlElearning=new URLElearning("/cours");
		return urlElearning;
	}
	
	@DeleteMapping("/admin/qcmproposition/{idProposition}")
	public URLElearning deletepropositionQCM(@PathVariable("idProposition")int idProposition) {
		contenuFormationService.deletePropositionQcm(idProposition);
		URLElearning urlElearning=new URLElearning("/cours");
		return urlElearning;
	}
	
	/*
	 * admin controler for MAIL SENDING
	 * 
	 * 
	 */
	@PostMapping("/admin/sendmail/")
	public URLElearning sendMailFromFornmail(@RequestBody FormMail formMail) {
		adminService.envoiMail(formMail);
		URLElearning urlElearning=new URLElearning("/mail/success");
		return urlElearning;
	}
	
	@PostMapping("/admin/sendmail/{idEtudiant}")
	public URLElearning sendMailToEtudiant(@PathVariable("idEtudiant")int idEtudiant,@RequestBody FormMail formMail) {
		etudiantService.sendMailToStudent(formMail,idEtudiant);
		URLElearning urlElearning=new URLElearning("/mail/success");
		return urlElearning;
	}
	
	/*
	 * envoi video pour un chapitre
	 * 
	 * 
	 */
	@PostMapping("/admin/uploadvidoechapitre/{idChapitre}")
	public URLElearning sendMailFromFornmail(@PathVariable("idChapitre") int idChapitre,@RequestParam("image") MultipartFile file) {
		contenuFormationService.setVideoChapitre(idChapitre, file);
		URLElearning urlElearning=new URLElearning();
		return urlElearning;
	}
	
	/*
	 * envoi des certification pour chaque cour
	 * 
	 */
	
	@PostMapping("/admin/generete/attestation/")
	public List<Etudiant> generetedAttestationByChapitreAndSendByMail(@RequestBody FormLink formLink) {
		return adminService.sendAttestationByEmailToStudentByChapitre(formLink);
	}
}
