package iri.elearningapi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
import iri.elearningapi.repository.courRepository.QroEtudiantRepository;
import iri.elearningapi.repository.courRepository.QuestionCourRepository;
import iri.elearningapi.repository.userRepository.AdminRepository;
import iri.elearningapi.repository.userRepository.EtudiantRepository;
import iri.elearningapi.repository.userRepository.GammeEtudiantRepository;
import iri.elearningapi.repository.userRepository.ProfesseurRepository;
import iri.elearningapi.utils.elearningData.Profil;
import iri.elearningapi.utils.elearningFunction.AttestationGenerator;
import iri.elearningapi.utils.elearningFunction.Methode;
import iri.elearningapi.utils.elearningFunction.SenderMail;
import iri.elearningapi.utils.errorClass.ElearningException;
import iri.elearningapi.utils.errorClass.ErrorAPI;
import iri.elearningapi.utils.form.formInt.FormLink;
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

	@Autowired
	private AttestationGenerator attestationGenerator;

	@Autowired
	private EtudiantChapitreRepository etudiantChapitreRepository;

	@Autowired
	private QroEtudiantRepository qroEtudiantRepository;

	@Autowired
	private ChapitreRepository chapitreRepository;

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private SenderMail senderMail;

	public UserElearning getLoginAdmin(UserElearning userLogin) {
		Admin admin = new Admin();
		// Admin admin2 = new Admin();

		admin = adminRepository.findByEmailOrTelephone(userLogin.getLogin(), userLogin.getLogin());
		if (admin != null && adminRepository.existsById(admin.getId())) {
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
		userAdminDashboard.setQroRepondu((int) qroEtudiantRepository.count());
		/*
		 * userAdminDashboard.setQcmValide(
		 * etudiantChapitreRepository.findAll().stream().mapToInt(item ->
		 * item.getQcmValide()).sum());
		 * 
		 * userAdminDashboard .setTauxReuissite((userAdminDashboard.getQcmValide()*100)
		 * / (((List<Chapitre>) chapitreRepository.findAll()) .stream().mapToInt(item ->
		 * item.getQcms().size()).sum()));
		 * 
		 * System.out.println("Total QCM == "+(((List<Chapitre>)
		 * chapitreRepository.findAll()) .stream().mapToInt(item ->
		 * item.getQcms().size()).sum()));
		 * 
		 * userAdminDashboard.setQuestionPose((int) questionCourRepository.count());
		 */

		return userAdminDashboard;
	}

	public void envoiMail(FormMail formMail) {
		if (formMail == null) {
			throw new ElearningException(new ErrorAPI("le mail ne peut etre null"));
		}

		if (formMail.getObjet() == null || formMail.getObjet().length() < 5) {
			throw new ElearningException(new ErrorAPI("L'objet du mail ne peut contenir moins de 5 carractere"));
		}

		if (formMail.getBodyHtml() == null || formMail.getBodyHtml().length() < 10) {
			throw new ElearningException(new ErrorAPI("Le corps du mail ne peut contenir moins de 10 carractere"));
		}
		List<Etudiant> etudiants = new ArrayList<Etudiant>();

		List<GammeEtudiant> gammeEtudiants = gammeEtudiantRepository.findAll();

		for (GammeEtudiant gammeEtudiant : gammeEtudiants) {
			String nombre = formMail.getProfil();
			int chiffre = gammeEtudiant.getId();
			char chiffreChar = (char) (chiffre + '0');

			if (nombre.indexOf(chiffreChar) != -1) {
				etudiants.addAll(gammeEtudiant.getEtudiants());
			}
		}

		if (formMail.getIdRegion() > 0) {
			int regionId = formMail.getIdRegion();
			etudiants = etudiants.stream().filter(etudiant -> etudiant.getRegion().getId() == regionId)
					.collect(Collectors.toList());
		}

		senderMail.sendFromFormMailToStutend(etudiants, formMail);
	}

	public List<Etudiant> sendAttestationByEmailToStudentByChapitre(FormLink formLink) {
		if (formLink != null) {
			if (chapitreRepository.existsById(formLink.getIdElement())) {
				Chapitre chapitre = chapitreRepository.findById(formLink.getIdElement()).get();
				List<Etudiant> etudiants = new ArrayList<Etudiant>();

				if (formLink.getMatricule() != null && etudiantRepository.existsByMatricule(formLink.getMatricule())) {
					Etudiant etudiant = etudiantRepository.findByMatricule(formLink.getMatricule());
					etudiants.add(etudiant);
				}

				if (formLink.isSendToAll()) {

					for (EtudiantChapitre etudiantChapitre : chapitre.getEtudiantChapitres()) {
						etudiants.add(etudiantChapitre.getEtudiant());
					}
				}

				for (Etudiant etudiant : etudiants) {
					String masculin = "Masculin";
					String titreSex = "";
					titreSex = (etudiant.getInformations() != null && !etudiant.getInformations().isEmpty())
							? masculin.equals(etudiant.getInformations().get(0).getSexe()) ? "Cher Monsieur " : "Chère Madame "
							: "Mr//Mme";
					
					String filePath = attestationGenerator.generateAndSaveAttestation((etudiant.getNom()+" "+etudiant.getPrenom()),
							etudiant.getMatricule(), chapitre.getTitre(), etudiant.getGammeEtudiant().getNom());
					FormMail formMail = new FormMail();
					formMail.setObjet("Votre attestation pour le cours: " + chapitre.getTitre().toUpperCase());
					formMail.setBodyHtml("" + "<div><b>"
							+ titreSex +""+(etudiant.getNom()+" "+etudiant.getPrenom())
							+ ",</b><br><br>" + "Nous espérons que vous avez trouvé le cours   <b>"
							+ chapitre.getTitre().toUpperCase() + "</b> du module <b>" + chapitre.getModule().getTitre()
							+ "</b> enrichissant et utile pour votre carrière Professionnelle et votre Perspective Entrepreneuriale. <br><br>"
							+ "Veuillez trouver ci-joint votre attestation<b>"
							+ "</b>. Cette certification témoigne de votre engagement à développer des capacités essentielles pour une contribution positive à l'essor économique et compétitif local et International.<br><br>"
							+ "Nous vous encourageons à poursuivre votre capacitation avec enthousiasme et détermination sur <a href='https://programmeleadership.net'>www.programmeleadership.org</a> . Si vous avez des questions ou des commentaires, n'hésitez pas à nous contacter.<br><br>"
							+ "Avec nos sincères félicitations,<br><b>L'équipe du Programme LEADERSHIP</b>" + "<div>"
							+ ""
							+ "<br><br>"
							+ "<div style='width:100%;height:5px;border:2px solid black'></div>"
							+ "<br>"
							+ ""
							+ "We hope that you have found the   <b>"
							+ chapitre.getTitre().toUpperCase()+"</b>course of the <b>"+ chapitre.getModule().getTitre()+"</b>module interesting and useful for your Professional career and your Entrepreneurial Perspectives."
							+ "<br><br>"
							+ ""
							+ "Please find attached with your certificate. This certification attest your commitment to develop essential abilities for your contribution on local and international economic scale and competitive development. We encourage you to continue your empowerment with enthusiasm and determination on <a href='https://programmeleadership.net'>www.programmeleadership.org</a>."
							+ "<br><br>"
							+ "If you have any questions or comments, do not hesitate to contact us.<br><br> With our sincere congratulations, <br><b>The LEADERSHIP Program team</b>"
							+ "");
					senderMail.sendMailToStutend(etudiant, formMail, filePath);
				}

				return etudiants;

			} else {
				throw new ElearningException(new ErrorAPI("Le chapitre selectioner est introuvable...!"));
			}
		} else {
			throw new ElearningException(new ErrorAPI("Le formulaire de selection est vide"));
		}
	}

}
