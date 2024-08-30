package iri.elearningapi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import iri.elearningapi.model.courModel.Chapitre;
import iri.elearningapi.model.courModel.EtudiantChapitre;
import iri.elearningapi.model.courModel.Module;
import iri.elearningapi.model.userModel.Admin;
import iri.elearningapi.model.userModel.Etudiant;
import iri.elearningapi.model.userModel.GammeEtudiant;
import iri.elearningapi.repository.courRepository.ChapitreRepository;
import iri.elearningapi.repository.courRepository.EtudiantChapitreRepository;
import iri.elearningapi.repository.courRepository.ModuleRepository;
import iri.elearningapi.repository.courRepository.QuestionCourRepository;
import iri.elearningapi.repository.userRepository.AdminRepository;
import iri.elearningapi.repository.userRepository.EtudiantRepository;
import iri.elearningapi.repository.userRepository.GammeEtudiantRepository;
import iri.elearningapi.repository.userRepository.ProfesseurRepository;
import iri.elearningapi.utils.elearningData.Profil;
import iri.elearningapi.utils.elearningFunction.Cryptage;
import iri.elearningapi.utils.elearningFunction.SenderMail;
import iri.elearningapi.utils.errorClass.ElearningException;
import iri.elearningapi.utils.errorClass.ErrorAPI;
import iri.elearningapi.utils.form.formInt.FormMail;
import iri.elearningapi.utils.form.formOut.UserAdminDashboard;
import iri.elearningapi.utils.form.formOut.UserElearning;

@Service
public class AdminService {
	@Autowired
	private EtudiantRepository etudiantRepository;

	@Autowired
	private ProfesseurRepository professeurRepository;

	@Autowired
	private ModuleRepository moduleRepository;

	@Autowired
	private QuestionCourRepository questionCourRepository;
	
	@Autowired
	private GammeEtudiantRepository gammeEtudiantRepository;
	
	//@Autowired
	//private EtudiantModuleRepository etudiantModuleRepository;

	@Autowired
	private EtudiantChapitreRepository etudiantChapitreRepository;

	@Autowired
	private ChapitreRepository chapitreRepository;

	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private SenderMail senderMail;

	public UserElearning getLoginAdmin(UserElearning userLogin) {
		Admin admin = new Admin();
		//Admin admin2 = new Admin();

		admin = adminRepository.findByEmailOrTelephone(userLogin.getLogin(), userLogin.getLogin());
		if (admin != null && admin.getPassword() != null
				&& admin.getPassword().equals(Cryptage.getMd5(userLogin.getPassword()))) {
			UserElearning user = new UserElearning();
			user.setEmail(admin.getEmail());
			user.setId(admin.getId());
			// user.setMatricule(etudiant.getMatricule());
			user.setNom(admin.getNom());
			user.setPrenom(admin.getPrenom());
			user.setProfil(Profil.ADMIN_USER);
			user.setPassword(admin.getPassword());
			user.setLevel(admin.getLevel());
			user.setConfirmation(1);
			user.setOpenDashboard(true);
			return user;
		}

		return null;
	}

	public UserAdminDashboard getAdminDashboard() {
		UserAdminDashboard userAdminDashboard = new UserAdminDashboard();

		userAdminDashboard.setEtudiantInscrit((int) etudiantRepository.count());
		userAdminDashboard.setProfTotal((int) professeurRepository.count());
		userAdminDashboard.setChapitreTotal((int) chapitreRepository.count());
		userAdminDashboard.setCourLu((int) etudiantChapitreRepository.count());
		userAdminDashboard.setModuleTotal((int) moduleRepository.count());
		userAdminDashboard.setModuleActif((int) ((List<Module>) moduleRepository.findAll()).stream()
				.filter(module -> (module.getDateDeblocage() != null && module.getDateDeblocage().before(new Date())))
				.count());
		userAdminDashboard.setQcmValide(
				etudiantChapitreRepository.findAll().stream().mapToInt(item -> item.getQcmValide()).sum());

		userAdminDashboard
				.setTauxReuissite((userAdminDashboard.getQcmValide()*100) / (((List<Chapitre>) chapitreRepository.findAll())
						.stream().mapToInt(item -> item.getQcms().size()).sum()));
		
		System.out.println("Total QCM == "+(((List<Chapitre>) chapitreRepository.findAll())
				.stream().mapToInt(item -> item.getQcms().size()).sum()));
		
		userAdminDashboard.setQuestionPose((int) questionCourRepository.count());

		return userAdminDashboard;
	}
	
	public void envoiMail(FormMail formMail) {
		if (formMail==null) {
			throw new ElearningException(new ErrorAPI("le mail ne peut etre null"));
		}
		
		if (formMail.getObjet()==null || formMail.getObjet().length()<5) {
			throw new ElearningException(new ErrorAPI("L'objet du mail ne peut contenir moins de 5 carractere"));
		}
		
		if (formMail.getBodyHtml()==null || formMail.getBodyHtml().length()<10) {
			throw new ElearningException(new ErrorAPI("Le corps du mail ne peut contenir moins de 10 carractere"));
		}
		List<Etudiant> etudiants=new ArrayList<Etudiant>();
		
		List<GammeEtudiant> gammeEtudiants=gammeEtudiantRepository.findAll();
		
		for (GammeEtudiant gammeEtudiant : gammeEtudiants) {
			String nombre  = formMail.getProfil();
			int chiffre = gammeEtudiant.getId();
	        char chiffreChar = (char) (chiffre + '0');
	        
	        if (nombre.indexOf(chiffreChar) != -1) {
	        	etudiants.addAll(gammeEtudiant.getEtudiants());
	        }
		}
		
		senderMail.sendFromFormMailToStutend(etudiants, formMail);
	}

}
