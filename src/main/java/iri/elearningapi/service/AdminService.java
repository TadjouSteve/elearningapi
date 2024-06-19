package iri.elearningapi.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import iri.elearningapi.model.userModel.Admin;
import iri.elearningapi.model.courModel.Chapitre;
import iri.elearningapi.model.courModel.Module;
import iri.elearningapi.repository.courRepository.ChapitreRepository;
import iri.elearningapi.repository.courRepository.EtudiantChapitreRepository;
import iri.elearningapi.repository.courRepository.EtudiantModuleRepository;
import iri.elearningapi.repository.courRepository.ModuleRepository;
import iri.elearningapi.repository.courRepository.QuestionCourRepository;
import iri.elearningapi.repository.userRepository.AdminRepository;
import iri.elearningapi.repository.userRepository.EtudiantRepository;
import iri.elearningapi.repository.userRepository.ProfesseurRepository;
import iri.elearningapi.utils.elearningData.Profil;
import iri.elearningapi.utils.elearningFunction.Cryptage;
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
	private EtudiantModuleRepository etudiantModuleRepository;

	@Autowired
	private EtudiantChapitreRepository etudiantChapitreRepository;

	@Autowired
	private ChapitreRepository chapitreRepository;

	@Autowired
	private AdminRepository adminRepository;

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

}
