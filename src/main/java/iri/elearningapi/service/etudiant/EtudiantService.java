package iri.elearningapi.service.etudiant;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
//import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import iri.elearningapi.model.courModel.Chapitre;
import iri.elearningapi.model.courModel.EtudiantChapitre;
import iri.elearningapi.model.courModel.EtudiantModule;
import iri.elearningapi.model.courModel.Module;
import iri.elearningapi.model.courModel.Proposition;
import iri.elearningapi.model.courModel.Qcm;
import iri.elearningapi.model.userModel.Candidature;
import iri.elearningapi.model.userModel.Etudiant;
import iri.elearningapi.model.userModel.Information;
import iri.elearningapi.repository.RegionRepository;
import iri.elearningapi.repository.courRepository.ChapitreRepository;
import iri.elearningapi.repository.courRepository.EtudiantChapitreRepository;
import iri.elearningapi.repository.courRepository.EtudiantModuleRepository;
import iri.elearningapi.repository.courRepository.ModuleRepository;
import iri.elearningapi.repository.userRepository.CandidatureRepository;
import iri.elearningapi.repository.userRepository.EtudiantRepository;
import iri.elearningapi.repository.userRepository.GammeEtudiantRepository;
import iri.elearningapi.repository.userRepository.InformationRepository;
import iri.elearningapi.repository.userRepository.ProfesseurRepository;
import iri.elearningapi.utils.elearningData.Code;
import iri.elearningapi.utils.elearningData.Profil;
import iri.elearningapi.utils.elearningData.Statut;
import iri.elearningapi.utils.elearningFunction.Cryptage;
import iri.elearningapi.utils.elearningFunction.Methode;
import iri.elearningapi.utils.elearningFunction.SenderMail;
import iri.elearningapi.utils.errorClass.ElearningException;
import iri.elearningapi.utils.errorClass.ErrorAPI;
import iri.elearningapi.utils.form.formInt.FormMail;
import iri.elearningapi.utils.form.formInt.FormQcmForValidation;
import iri.elearningapi.utils.form.formInt.FormResetPassword;
import iri.elearningapi.utils.form.formOut.FormChapitre;
import iri.elearningapi.utils.form.formOut.FormViewEtudiant;
import iri.elearningapi.utils.form.formOut.UserDashboard;
import iri.elearningapi.utils.form.formOut.UserElearning;

@Service
public class EtudiantService {

	private final static LocalDate DATE_DEBUT_FORMATION = LocalDate.of(2024, 9, 1);

	@Autowired
	private EtudiantRepository etudiantRepository;

	@Autowired
	private ModuleRepository moduleRepository;

	@Autowired
	private EtudiantModuleRepository etudiantModuleRepository;

	@Autowired
	private EtudiantChapitreRepository etudiantChapitreRepository;

	@Autowired
	private ChapitreRepository chapitreRepository;

	@Autowired
	private ProfesseurRepository professeurRepository;

	@Autowired
	private GammeEtudiantRepository gammeEtudiantRepository;

	@Autowired
	private RegionRepository regionRepository;

	@Autowired
	private InformationRepository informationRepository;

	@Autowired
	private CandidatureRepository candidatureRepository;

	@Autowired
	private ControlEtudiantService controlEtudiantService;

	@Value("${video.upload.salon.dir}")
	private String uploadVideoDir;

	@Value("${document.upload.salon.dir}")
	private String uploadDocumentDir;

	@Autowired
	private SenderMail senderMail;

	private static final Random RANDOM = new Random();

	public UserElearning enregistrementEtudiant(Etudiant etudiant) {

		if (etudiant.getGammeEtudiant() != null) {
			System.out.println("Gamme Etudiant Present == " + etudiant.getGammeEtudiant().getNom());
		} else {
			System.out.println("La gamme de l'etudiant n'est pas defini");
		}

		controlEtudiant(etudiant, true);

		Information information = etudiant.getInformations().get(0);

		etudiant.setMatricule(generateUniqueMatricule());

		etudiant.setNom(Methode.upperCaseFirst(etudiant.getNom()));
		etudiant.setPrenom(Methode.upperCaseFirst(etudiant.getPrenom()));
		etudiant.setLieuNaissance(Methode.upperCaseFirst(etudiant.getLieuNaissance()));
		etudiant.setProfession(Methode.upperCaseFirst(etudiant.getProfession()));
		etudiant.setNomEntreprise(Methode.upperCaseFirst(etudiant.getNomEntreprise()));

		// etudiant.setPasswordClear(etudiant.getPassword());
		etudiant.setConfirmation(-1);
		String lienConfirm = "";
		do {
			lienConfirm = Methode.generateRandomString();
		} while (etudiantRepository.existsByLienConfirmation(lienConfirm));
		etudiant.setLienConfirmation(lienConfirm);
		etudiant.setPassword(Cryptage.getMd5(etudiant.getPassword()));

		etudiant.setLastConnexion(new Date());
		etudiant.setDateInscription(new Date());

		// etudiant.setPassword(passwordEncoder.(etudiant.getPassword()));
		etudiant = etudiantRepository.save(etudiant);
		information.setEtudiant(etudiant);
		information = informationRepository.save(information);
		senderMail.sendMailWithLinkForConfirmation(etudiant);
		UserElearning user = new UserElearning();
		user.setEmail(etudiant.getEmail());
		user.setId(etudiant.getId());
		user.setMatricule(etudiant.getMatricule());
		user.setNom(etudiant.getNom());
		user.setPrenom(etudiant.getPrenom());
		user.setProfil(Profil.ETUDIANT_USER);
		user.setProfession(etudiant.getProfession());
		user.setNomEntreprise(etudiant.getNomEntreprise());
		user.setConfirmation(etudiant.getConfirmation());
		user.setOpenDashboard(true);

		return user;
	}

	public UserElearning getLogin(UserElearning userLogin) {
		Etudiant etudiant = null;
		Etudiant etudiant2 = null;
		etudiant = etudiantRepository.findByEmailOrTelephone(userLogin.getLogin(), userLogin.getLogin());
		// ancien code utiliser pour verifier que le login et le mot de passe etait
		// correct derenavent on cotrole juste si le login est correct,
		// etudiant2 =
		// etudiantRepository.findByPassword(Cryptage.getMd5(userLogin.getPassword()));
		// if (etudiant != null && etudiant2 != null && etudiant.getId() ==
		// etudiant2.getId()) {
		if (etudiant != null && etudiantRepository.existsByMatricule(etudiant.getMatricule())) {
			UserElearning user = new UserElearning();
			user.setEmail(etudiant.getEmail());
			user.setId(etudiant.getId());
			user.setMatricule(etudiant.getMatricule());
			user.setNom(etudiant.getNom());
			user.setPrenom(etudiant.getPrenom());
			user.setProfil(Profil.ETUDIANT_USER);
			user.setProfession(etudiant.getProfession());
			user.setNomEntreprise(etudiant.getNomEntreprise());
			user.setPassword(etudiant.getPassword());
			updateLastConnexion(etudiant.getId());
			user.setOpenDashboard(true);

			/*
			 * if (etudiant.getStatut() >= 2 ||
			 * !LocalDate.now().isBefore(DATE_DEBUT_FORMATION)) {
			 * user.setOpenDashboard(true); }
			 */
			// System.out.println("== go N2 ==");
			return user;
		}
		return null;
	}

	public UserElearning validationInscriptionCompte(String lienConfirmation) {
		if (etudiantRepository.existsByLienConfirmation(lienConfirmation)) {
			Etudiant etudiant = etudiantRepository.findByLienConfirmation(lienConfirmation);
			if (etudiant.getConfirmation() == -1) {
				etudiant.setConfirmation(1);
				etudiant.setDateConfiramtion(new Date());
				etudiant = etudiantRepository.save(etudiant);
				senderMail.sendMailWithLinkToConnectAfterValidationInscription(etudiant);
			}

			UserElearning user = new UserElearning();
			user.setEmail(etudiant.getEmail());
			user.setId(etudiant.getId());
			user.setMatricule(etudiant.getMatricule());
			user.setNom(etudiant.getNom());
			user.setPrenom(etudiant.getPrenom());
			user.setConfirmation(etudiant.getConfirmation());
			user.setProfil(Profil.ETUDIANT_USER);
			if (etudiant.getStatut() >= 2) {
				user.setOpenDashboard(true);
			} else {
				user.setOpenDashboard(false);
			}

			return user;
		} else {
			throw new ElearningException(
					new ErrorAPI("Impossible de valider votre inscription, lien de validation introuvable...", 0));
		}
	}

	public UserElearning controleValidationCompte(String matricule) {
		if (etudiantRepository.existsByMatricule(matricule)) {
			Etudiant etudiant = etudiantRepository.findByMatricule(matricule);
			UserElearning user = new UserElearning();
			user.setEmail(etudiant.getEmail());
			user.setId(etudiant.getId());
			user.setMatricule(etudiant.getMatricule());
			user.setNom(etudiant.getNom());
			user.setPrenom(etudiant.getPrenom());
			user.setConfirmation(etudiant.getConfirmation());
			user.setProfil(Profil.ETUDIANT_USER);
			if (etudiant.getStatut() >= 2) {
				user.setOpenDashboard(true);
			} else {
				user.setOpenDashboard(false);
			}
			return user;
		} else {
			throw new ElearningException(
					new ErrorAPI("Impossible de verifier la validite du compte, matricule introuvable...", 0));
		}
	}

	@Async
	public void updateLastConnexion(int idEtudiant) {
		if (etudiantRepository.existsById(idEtudiant)) {
			Etudiant etudiant = etudiantRepository.findById(idEtudiant).get();
			etudiant.setLastConnexion(new Date());
			etudiantRepository.save(etudiant);
			// System.out.println("== go N111 ==");
		}
	}

	public void controlEtudiant(Etudiant etudiant, boolean isNew) {

		if (etudiant.getGammeEtudiant() == null
				|| !gammeEtudiantRepository.existsById(etudiant.getGammeEtudiant().getId())) {
			throw new ElearningException(new ErrorAPI("Vous n'avez pas defini votre profil de formation", -1));
		}

		if (etudiant.getNom() == null || etudiant.getNom().trim().length() < 3)
			throw new ElearningException(new ErrorAPI(
					"Le nom du canditat ne doit pas être vide, ou contenir moins de trois(03) caractères", 0));

		if (etudiant.getNom().trim().length() > 50)
			throw new ElearningException(
					new ErrorAPI("Le nom du canditat est trop long...! Il ne doit pas dépasser 50 caractères", 0));

		if (etudiant.getPrenom() != null && etudiant.getPrenom().trim().length() > 50)
			throw new ElearningException(
					new ErrorAPI("Le prenom du canditat est trop long...! Il ne doit pas dépasser 50 caractères", 0));

		if (!Code.C004.name().equals(etudiant.getGammeEtudiant().getCode())
				&& !Code.C003.name().equals(etudiant.getGammeEtudiant().getCode())) {
			System.out.println("voici le code gamme de l'entrepreueneur == " + etudiant.getGammeEtudiant().getCode());
			if (etudiant.getDateNaissance() == null)
				throw new ElearningException(new ErrorAPI("La date de naissance du canditat est obligatoire", 0));

			if (etudiant.getLieuNaissance() == null || etudiant.getLieuNaissance().trim().isEmpty())
				throw new ElearningException(new ErrorAPI("Le lieu de naissance du canditat est obligatoire", 0));
		}

		if (etudiant.getEmail() == null || etudiant.getEmail().trim().isEmpty())
			throw new ElearningException(new ErrorAPI("L'email du canditat est obligatoire", 0));

		if (!isValidEmail(etudiant.getEmail()))
			throw new ElearningException(new ErrorAPI("L'email entré est invalide", 0));

		if (etudiant.getRegion() == null || !regionRepository.existsById(etudiant.getRegion().getId())) {
			throw new ElearningException(new ErrorAPI("vous devez definir votre region de residence", 0));
		}

		if (etudiant.getTelephone() == null || etudiant.getTelephone().trim().length() < 8)
			throw new ElearningException(new ErrorAPI(
					"Le numero de telephone du canditat ne doit pas être vide, ou contenir moins de trois(08) caractères",
					0));

		if (etudiant.getInformations() != null && !etudiant.getInformations().isEmpty()) {
			controlEtudiantService.controlInformationEtudiant(etudiant.getInformations().get(0),
					etudiant.getGammeEtudiant());
			System.out.println("Test Information passer avec success:" + etudiant.getInformations().get(0).getSexe());
		} else {
			throw new ElearningException(
					new ErrorAPI("Les informations du formulaires ne sont pas correctement envoye", 0));
		}

		if (isNew) {
			// Si c'est un nouvel étudiant, vérifiez si l'email ou le numéro de téléphone
			// existe déjà
			if (etudiantRepository.findByEmail(etudiant.getEmail()) != null) {
				throw new ElearningException(new ErrorAPI("Un canditat avec cet email existe déjà", 0));
			}
			if (etudiantRepository.findByTelephone(etudiant.getTelephone()) != null) {
				throw new ElearningException(new ErrorAPI("Un canditat avec ce numéro de téléphone existe déjà", 0));
			}

			if (etudiant.getPassword() == null || etudiant.getPassword().trim().isEmpty()) {
				throw new ElearningException(new ErrorAPI("Le mot de passe est obligatoire", 2));
			}
			if (etudiant.getPassword().length() < 8) {
				throw new ElearningException(new ErrorAPI("Le mot de passe doit contenir au moins 8 caractères", 2));
			}
			if (!etudiant.getPassword().equals(etudiant.getConfirmPassword())) {
				throw new ElearningException(
						new ErrorAPI("Le mot de passe et la confirmation du mot de passe ne correspondent pas", 2));
			}

		} else {
			// Si c'est une modification, vérifiez si l'email ou le numéro de téléphone
			// existe déjà pour un autre étudiant
			Etudiant existingEtudiantWithEmail = etudiantRepository.findByEmail(etudiant.getEmail());
			if (existingEtudiantWithEmail != null && !(existingEtudiantWithEmail.getId() == (etudiant.getId()))) {
				throw new ElearningException(new ErrorAPI("Un autre canditat avec cet email existe déjà", 0));
			}
			Etudiant existingEtudiantWithTelephone = etudiantRepository.findByTelephone(etudiant.getTelephone());
			if (existingEtudiantWithTelephone != null
					&& !(existingEtudiantWithTelephone.getId() == (etudiant.getId()))) {
				throw new ElearningException(
						new ErrorAPI("Un autre canditat avec ce numéro de téléphone existe déjà", 0));
			}
		}

		if (etudiant.getTelephone() != null && professeurRepository.existsByTelephone(etudiant.getTelephone())) {
			throw new ElearningException(
					new ErrorAPI("Le numero de téléphone entré est deja utiliser par un compte de supervision ", 0));
		}

		if (etudiant.getEmail() != null && professeurRepository.existsByEmail(etudiant.getEmail())) {
			throw new ElearningException(
					new ErrorAPI("L'adresse mail entrée est deja utiliser par un  compte de supervision", 0));
		}

	}

	public boolean isValidEmail(String email) {
		String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
		// String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
		Pattern pattern = Pattern.compile(emailRegex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public String generateUniqueMatricule() {
		String matricule;
		do {
			int prefix = RANDOM.nextInt(90) + 10; // Génère un nombre aléatoire à deux chiffres
			int suffix = RANDOM.nextInt(9000) + 1000; // Génère un nombre aléatoire à quatre chiffres
			matricule = prefix + "IRI" + suffix;
		} while (etudiantRepository.findByMatricule(matricule) != null);
		return matricule;
	}

	public UserDashboard getEtudiantDashoard(int idEtudiant) {
		UserDashboard userDashboard = new UserDashboard();

		if (etudiantRepository.existsById(idEtudiant)) {
			Etudiant etudiant = etudiantRepository.findById(idEtudiant).get();
			userDashboard.setCourLu(etudiant.getEtudiantChapitres().size());
			for (EtudiantChapitre etudiantChapitre : etudiant.getEtudiantChapitres()) {
				userDashboard.setQCMValide(userDashboard.getQCMValide() + etudiantChapitre.getQcmValide());
				userDashboard
						.setQroRepondu(userDashboard.getQroRepondu() + etudiantChapitre.getChapitre().getQros().size());
			}

			for (Module module01 : moduleRepository.findAll()) {
				if (etudiant.getGammeEtudiant() != null && etudiant.getGammeEtudiant().getGammeEtudiantModules()
						.stream().anyMatch(e -> (e.getModule() != null && e.getModule().equals(module01)))) {
					if (module01.getDateDeblocage() == null || module01.getDateDeblocage().before(new Date())) {
						userDashboard.setModuleAccessible(userDashboard.getModuleAccessible() + 1);
						module01.setIsAccessible(true);
					}

					module01.setFistTime(true);
					module01.setTotalChapitre(module01.getChapitres().size());
					for (EtudiantModule etudiantModule : etudiant.getEtudiantModules()) {
						if (etudiantModule.getModule().getIdModule() == module01.getIdModule()) {
							module01.setFistTime(false);
							for (EtudiantChapitre etudiantChapitre : etudiantModule.getEtudiant()
									.getEtudiantChapitres()) {
								if (etudiantChapitre.getChapitre().getModule().getIdModule() == module01
										.getIdModule()) {
									module01.setChapitreLu(module01.getChapitreLu() + 1);
								}
							}
						}
					}
					userDashboard.addModule(module01);
					userDashboard.setQroRepondu(etudiant.getQroEtudiants().size());
					if (module01.getChapitres() != null) {
						userDashboard
								.setChapitreTotal(userDashboard.getChapitreTotal() + module01.getChapitres().size());
						userDashboard.setQcmTotal(userDashboard.getQcmTotal()
								+ module01.getChapitres().stream().mapToInt(e -> e.getQcms().size()).sum());

						userDashboard.setQroTotal(userDashboard.getQroTotal()
								+ module01.getChapitres().stream().mapToInt(e -> e.getQros().size()).sum());
					}
				}
			}

			userDashboard.setModuleTotal(userDashboard.getModules() != null ? userDashboard.getModules().size() : 0);

			// userDashboard.setModuleTotal(userDashboard.getModules() != null ?
			// userDashboard.getModules().size() : 0);
			// userDashboard.setChapitreTotal((int) chapitreRepository.count());
			/*
			 * userDashboard.setQcmTotal((int)
			 * chapitreRepository.findAllByOrderByTitreAsc().stream() .mapToInt(item ->
			 * item.getQcms().size()).sum());
			 */
		}

		return userDashboard;
	}

	public List<FormChapitre> getAllChapitreModuleEtudiant(int idEtudiant, int idModule) {
		List<FormChapitre> formChapitres = new ArrayList<>();

		if (etudiantRepository.existsById(idEtudiant) && moduleRepository.existsById(idModule)) {

			Module module = moduleRepository.findById(idModule).get();
			Etudiant etudiant = etudiantRepository.findById(idEtudiant).get();

			if (module.getGammeEtudiantModules() == null || !module.getGammeEtudiantModules().stream().anyMatch(
					e -> (e.getGammeEtudiant() != null && e.getGammeEtudiant().equals(etudiant.getGammeEtudiant())))) {
				System.out.println("Acces au cours du module refuser");
				throw new ElearningException(new ErrorAPI("Vous n'avaez pas acces a ce module"));
			}

			Boolean isFirstOpen = true;

			if (etudiant.getEtudiantModules() == null || etudiant.getEtudiantModules().isEmpty()) {

				senderMail.sendMailOvertureModule(etudiant, module);
				System.out.println("mail d'ouverture module envoyer");
			}

			for (EtudiantModule etudiantModule : module.getEtudiantModules()) {
				if (etudiantModule.getEtudiant() != null && etudiantModule.getEtudiant().getId() == idEtudiant) {
					isFirstOpen = false;
				}
			}

			if (isFirstOpen) {
				EtudiantModule etudiantModule = new EtudiantModule();
				etudiantModule.setModule(module);
				etudiantModule.setEtudiant(etudiantRepository.findById(idEtudiant).get());
				etudiantModule.setDateDebut(new Date());
				etudiantModuleRepository.save(etudiantModule);
				// senderMail.sendMailOvertureModule(etudiant,module);
				System.out.println("is first open module");
			}

			for (Chapitre chapitre : moduleRepository.findById(idModule).get().getChapitres()) {
				FormChapitre formChapitre = new FormChapitre();

				formChapitre.setIdChapitre(chapitre.getIdChapitre());
				formChapitre.setIdModule(idModule);
				formChapitre.setTitre(chapitre.getTitre());
				formChapitre.setImageURL(chapitre.getImage());
				formChapitre.setEtat(chapitre.getEtat());
				formChapitre.setTitreModule(module.getTitre());
				formChapitre.setOrdre(chapitre.getOrdre());

				if (chapitre.getChapitreEns() != null && !chapitre.getChapitreEns().isEmpty()) {
					formChapitre.setTitreEn(chapitre.getChapitreEns().get(0).getTitre());
				} else {
					formChapitre.setTitreEn(chapitre.getTitre());
				}

				formChapitre.setTotalQcm(chapitre.getQcms().size());

				for (EtudiantChapitre etudiantChapitre : chapitre.getEtudiantChapitres()) {
					if (etudiantChapitre.getEtudiant().getId() == idEtudiant) {
						formChapitre.setFirstTime(false);
						formChapitre.setTotalQcmValide(etudiantChapitre.getQcmValide());
					}
				}

				formChapitres.add(formChapitre);
			}
		}

		return formChapitres;
	}

	public Chapitre getChapitreByEtudiant(int idEtudiant, int idChapitre) {
		Chapitre chapitre = new Chapitre();
		if (etudiantRepository.existsById(idEtudiant) && chapitreRepository.existsById(idChapitre)) {
			chapitre = chapitreRepository.findById(idChapitre).get();

			Boolean isFirstOpen = true;

			for (EtudiantChapitre etudiantChapitre : chapitre.getEtudiantChapitres()) {
				if (etudiantChapitre.getEtudiant() != null && etudiantChapitre.getEtudiant().getId() == idEtudiant) {
					isFirstOpen = false;
				}
			}

			if (isFirstOpen) {
				EtudiantChapitre etudiantChapitre = new EtudiantChapitre();
				etudiantChapitre.setChapitre(chapitre);
				etudiantChapitre.setEtudiant(etudiantRepository.findById(idEtudiant).get());
				etudiantChapitre.setDateDebut(new Date());
				etudiantChapitre.setStatut(Statut.NOT_CORRECTED.name());
				etudiantChapitre = etudiantChapitreRepository.save(etudiantChapitre);
				// senderMail.sendMailStartChapitre(etudiantChapitre.getEtudiant(), chapitre);
			}
		} else {
			throw new ElearningException(new ErrorAPI(
					"Impossible de retrouver l'etudiant où le cour conserner, controlez les identifiants entrés"));
		}

		return chapitre;
	}

	public FormQcmForValidation getValidationQcmEtudiant(List<FormQcmForValidation> formQcmForValidations,
			int idEtudiant, int idChapitre) {
		FormQcmForValidation reponse = new FormQcmForValidation();
		Chapitre chapitre = new Chapitre();

		if (etudiantRepository.existsById(idEtudiant) && chapitreRepository.existsById(idChapitre)
				&& formQcmForValidations != null) {
			Etudiant etudiant = etudiantRepository.findById(idEtudiant).get();
			chapitre = chapitreRepository.findById(idChapitre).get();
			if (chapitre.getQcms() != null) {
				reponse.setTotalQcm(chapitre.getQcms().size());
				for (Qcm qcm : chapitre.getQcms()) {
					Boolean isCorrect = true;
					FormQcmForValidation qcmEtudiant = findFormQcmForValidation(formQcmForValidations, qcm.getId());
					if (qcmEtudiant != null && qcmEtudiant.getPropositions() != null && qcm.getPropositions() != null) {

						for (Proposition proposition : qcm.getPropositions()) {
							// System.out.println("poroposition: "+proposition.getValeur()+" ||
							// id:"+proposition.getId()+" || Etat: " +proposition.getEtat()+" ||
							// isPresent:"+qcmEtudiant.getPropositions().contains(proposition.getId()));
							if ((proposition.getEtat() > 0
									&& !qcmEtudiant.getPropositions().contains(proposition.getId()))
									|| (proposition.getEtat() <= 0
											&& qcmEtudiant.getPropositions().contains(proposition.getId()))) {
								isCorrect = false;
							}
						}

						for (int idqcm01 : qcmEtudiant.getPropositions()) {
							if (qcm.getPropositions().stream()
									.noneMatch(proposition -> proposition.getId() == idqcm01)) {
								System.out.println("Le piege est tomber");
								isCorrect = false;
							}
						}
						if (isCorrect && qcm.getPropositions() != null) {
							reponse.setQcmValide(reponse.getQcmValide() + 1);
						}
					}

				}

				if (etudiantChapitreRepository.findByEtudiantAndChapitre(etudiant, chapitre) != null) {
					// System.out.print("TOUT CE PASSE BIEN MR STEVE");
					EtudiantChapitre etudiantChapitre = etudiantChapitreRepository.findByEtudiantAndChapitre(etudiant,
							chapitre);
					etudiantChapitre.setQcmValide(reponse.getQcmValide());
					etudiantChapitre.setDateValidationQcm(new Date());
					etudiantChapitreRepository.save(etudiantChapitre);
					senderMail.sendMailValidationQCM(etudiant, chapitre, reponse);
				}

			}
		}
		return reponse;
	}

	private FormQcmForValidation findFormQcmForValidation(List<FormQcmForValidation> formQcmForValidations, int idQcm) {

		for (FormQcmForValidation formQcmForValidation : formQcmForValidations) {
			if (formQcmForValidation.getId() == idQcm) {
				return formQcmForValidation;
			}
		}
		return null;
	}

	public Page<Etudiant> getListEtudiants(String filter, int pageNumber) {

		Pageable pageable = PageRequest.of(pageNumber, 50);

		Page<Etudiant> pageEtudiants;

		if (filter != null) {
			pageEtudiants = etudiantRepository
					.findByNomStartingWithOrPrenomStartingWithOrRegionStartingWithOrEmailStartingWithOrNomEntrepriseStartingWithOrderByNomAsc(
							filter, filter, filter, filter, filter, pageable);
			if (pageEtudiants.isEmpty()) {
				pageEtudiants = etudiantRepository
						.findByNomContainingOrPrenomContainingOrRegionContainingOrEmailContainingOrNomEntrepriseContainingOrderByNomAsc(
								filter, filter, filter, filter, filter, pageable);
			}

		} else {
			pageEtudiants = etudiantRepository.findAllByOrderByNomAsc(pageable);
		}

		return pageEtudiants;
	}

	public FormViewEtudiant getFormViewEtudiantForAdmin(String matriculeEtudiant, UserElearning user) {
		Etudiant etudiant = new Etudiant();
		FormViewEtudiant formViewEtudiant = new FormViewEtudiant();
		if (etudiantRepository.existsByMatricule(matriculeEtudiant)) {
			etudiant = etudiantRepository.findByMatricule(matriculeEtudiant);
			formViewEtudiant = new FormViewEtudiant(etudiant.getId(), etudiantRepository, chapitreRepository,
					moduleRepository);

		}
		return formViewEtudiant;
	}

	public void generetedCodeForResetPassword(FormResetPassword formResetPassword) {
		// Methode.printOut("etape one - 2");
		if (formResetPassword.getEmailOrPhone() == null || formResetPassword.getEmailOrPhone().isBlank()) {
			throw new ElearningException(
					new ErrorAPI("Vous devez entrer soit votre adresse e-mail, soit votre numéro de téléphone"));
		}

		if (!etudiantRepository.existsByEmail(formResetPassword.getEmailOrPhone())
				&& !etudiantRepository.existsByTelephone(formResetPassword.getEmailOrPhone())) {
			throw new ElearningException(new ErrorAPI("Adresse e-mail ou numéro de téléphone incorrect."));
		}

		Etudiant etudiant = new Etudiant();
		if (etudiantRepository.existsByEmail(formResetPassword.getEmailOrPhone())) {
			etudiant = etudiantRepository.findByEmail(formResetPassword.getEmailOrPhone());
		} else {
			etudiant = etudiantRepository.findByTelephone(formResetPassword.getEmailOrPhone());
		}

		String code = "";
		do {
			code = Methode.generateRandomString().toLowerCase();
		} while (etudiantRepository.existsByCodeChangePassword(code));

		etudiant.setCodeChangePassword(code);
		etudiant = etudiantRepository.save(etudiant);
		// Methode.printOut("etape one - 3");
		senderMail.sendMailWithLinkCodeToChangePassword(etudiant);

	}

	public UserElearning resetPasswordWithCode(FormResetPassword formResetPassword) {
		// Methode.printOut("etape tow - 2");
		if (formResetPassword.getPassword() == null || formResetPassword.getPassword().trim().isEmpty()) {
			throw new ElearningException(new ErrorAPI("Le nouveau mot de passe est obligatoire"));
		}
		if (formResetPassword.getPassword().length() < 8) {
			throw new ElearningException(new ErrorAPI("Le nouveau mot de passe doit contenir au moins 8 caractères"));
		}
		if (!formResetPassword.getPassword().equals(formResetPassword.getConfirmPassword())) {
			throw new ElearningException(new ErrorAPI(
					"Le nouveau mot de passe et la confirmation du nouveau mot de passe ne correspondent pas"));
		}

		if (!etudiantRepository.existsByCodeChangePassword(formResetPassword.getCodeChangePassword())) {
			throw new ElearningException(new ErrorAPI("Le lien de réinitialisation n'est pas valide...!"));
		} else {
			Etudiant etudiant = etudiantRepository.findByCodeChangePassword(formResetPassword.getCodeChangePassword());
			etudiant.setCodeChangePassword(null);
			etudiant.setPassword(Cryptage.getMd5(formResetPassword.getPassword()));
			etudiant.setPasswordClear(null);
			etudiant.setLastConnexion(new Date());
			etudiant = etudiantRepository.save(etudiant);

			// Methode.printOut("etape two - 3");
			senderMail.sendMailAfterResetPasswordEtudiant(etudiant);

			UserElearning user = new UserElearning();
			user.setEmail(etudiant.getEmail());
			user.setId(etudiant.getId());
			user.setMatricule(etudiant.getMatricule());
			user.setNom(etudiant.getNom());
			user.setPrenom(etudiant.getPrenom());
			user.setProfil(Profil.ETUDIANT_USER);
			user.setProfession(etudiant.getProfession());
			user.setNomEntreprise(etudiant.getNomEntreprise());
			user.setPassword(etudiant.getPassword());
			updateLastConnexion(etudiant.getId());
			user.setOpenDashboard(false);

			if (etudiant.getStatut() >= 2 || !LocalDate.now().isBefore(DATE_DEBUT_FORMATION)) {
				user.setOpenDashboard(true);
			}
			// System.out.println("== go N2 ==");
			return user;
		}

	}

	public void setRapport() {

	}

	public Candidature registrationSalonEntrepreneur(Candidature candidature) {
		if (candidature != null) {
			if (etudiantRepository.existsByEmail(candidature.getEmail())
					|| etudiantRepository.existsByTelephone(candidature.getEmail())) {
				Etudiant etudiant = etudiantRepository.findByEmailOrTelephone(candidature.getEmail(),
						candidature.getEmail());
				if (!etudiant.getCandidatures().isEmpty()) {
					// candidature.setId(etudiant.getCandidatures().get(0).getId());
					candidatureRepository.delete(etudiant.getCandidatures().get(0));
				}
				candidature.setEtudiant(etudiant);
				candidature.setEmail(etudiant.getEmail());
				candidature = candidatureRepository.save(candidature);
				candidature.setMatricule(etudiant.getMatricule());
				return candidature;
			} else {
				throw new ElearningException(new ErrorAPI(
						"L'adresse email ou le numéro de téléphone saisi ne correspond à aucun candidat. Si vous êtes nouveau sur la plateforme, veuillez d'abord créer un compte."));
			}
		} else {
			throw new ElearningException(new ErrorAPI("le formulaire de candidature est vide...!"));
		}
	}

	public UserElearning uploadVideoCandidatureSalon(MultipartFile file, String email) {
		if (file.isEmpty()) {
			throw new ElearningException(new ErrorAPI("Le fichier est vide...!"));
		}

		// Vérification du type de fichier vidéo
		String contentType = file.getContentType();
		if (!contentType.startsWith("video/")) {
			throw new ElearningException(new ErrorAPI("Le fichier n'est pas une vidéo."));
		}

		Etudiant etudiant=new Etudiant();
		Candidature candidature;
		if (etudiantRepository.existsByEmail(email)) {
			etudiant = etudiantRepository.findByEmail(email);
			candidature=etudiant.getCandidatures().get(0);
		} else {
			throw new ElearningException(new ErrorAPI(
					"L'adresse email ou le numéro de téléphone saisi ne correspond à aucun candidat. Si vous êtes nouveau sur la plateforme, veuillez d'abord créer un compte."));
		}

		try {
			// Créer un nom de fichier unique
            //String uniqueFileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

			String uniqueFileName = etudiant.getMatricule() + "-" + file.getOriginalFilename();
			candidature.setLienVideo(uniqueFileName);
			Path filePath = Paths.get(uploadVideoDir + File.separator + uniqueFileName);
			
			// Vérifier si le répertoire existe, sinon le créer
	        Path directoryPath = Paths.get(uploadVideoDir);
	        if (!Files.exists(directoryPath)) {
	            Files.createDirectories(directoryPath);
	        }

			// Sauvegarder le fichier dans le répertoire spécifié 
			Files.copy(file.getInputStream(), filePath,StandardCopyOption.REPLACE_EXISTING);
			candidature=candidatureRepository.save(candidature);
			senderMail.sendMailConfirmCanditatureSalon(etudiant);
			UserElearning user = new UserElearning();
			user.setNom((etudiant.getNom()+" "+etudiant.getPrenom()));
			user.setEmail(etudiant.getEmail());
			user.setTelephone(etudiant.getTelephone());
			user.setMatricule(etudiant.getMatricule());
			user.setLogin(filePath.toString());
			return user;
		} catch (IOException e) {
			e.printStackTrace();
			throw new ElearningException(new ErrorAPI("Erreur lors du téléchargement du fichier."));
		}
	}
	
	
	private String saveFile(Path userDirectory, MultipartFile file, String fileNamePrefix,Etudiant etudiant){
       String nomFichier=(etudiant.getMatricule()+"_"+fileNamePrefix + "_" + file.getOriginalFilename()).replace(" ", "_");
       //String nomFichier=(etudiant.getMatricule()+"_"+fileNamePrefix);
       Path destinationFile = userDirectory.resolve(
                Paths.get(nomFichier))
                .normalize().toAbsolutePath();
        try {
			Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
			 // Définir les permissions du fichier à "rw-r--r--" (644)
			 if (FileSystems.getDefault().supportedFileAttributeViews().contains("posix")) {
		            // Only attempt to set permissions if POSIX is supported
		        	Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rw-r--r--");
			        Files.setPosixFilePermissions(destinationFile, perms);
		        }
			return nomFichier;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ElearningException(new ErrorAPI("Erreur lors du téléchargement du fichier ( "+fileNamePrefix+" )"));
		}
    }
	
	public UserElearning uploadDocumentsCandidature(MultipartFile fileVideo, MultipartFile fileBusinessPlan,
            MultipartFile fileFacture, MultipartFile fileConformite, String email) {
		Path rootLocation = Paths.get(uploadDocumentDir);
		Etudiant etudiant=new Etudiant();
		Candidature candidature;
		if (etudiantRepository.existsByEmail(email)) {
			etudiant = etudiantRepository.findByEmail(email);
			candidature=etudiant.getCandidatures().get(0);
		} else {
			throw new ElearningException(new ErrorAPI(
					"L'adresse email ou le numéro de téléphone saisi ne correspond à aucun candidat. Si vous êtes nouveau sur la plateforme, veuillez d'abord créer un compte."));
		}
		String nomDossierPrive=(etudiant.getMatricule()+" "+etudiant.getNom()+" "+etudiant.getPrenom()).replace(" ", "_");
        // Créer le répertoire de l'utilisateur s'il n'existe pas
        Path userDirectory = rootLocation.resolve(nomDossierPrive);
        if (!Files.exists(userDirectory)) {
            try {
				Files.createDirectories(userDirectory);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new ElearningException(new ErrorAPI("Erreur lors du téléchargement du fichier. #710"));
			}
        }

        // Enregistrement de la vidéo
        if (fileVideo != null && !fileVideo.isEmpty()) {
        	// Récupérer le type MIME
            String contentType = fileVideo.getContentType();
        	// Vérifier si le type MIME correspond à une vidéo
            if (contentType != null && contentType.startsWith("video/")) {
                candidature.setLienVideo(saveFile(userDirectory, fileVideo, "video", etudiant));
            } else {
                throw new ElearningException(new ErrorAPI("Le fichier de votre projet n'est pas une vidéo valide."));
            }
            
        }else {
        	throw new ElearningException(new ErrorAPI("Impossible de sauvegarder cette Video"));
		}

        // Enregistrement du business plan
        if (fileBusinessPlan != null && !fileBusinessPlan.isEmpty()) {
        	candidature.setBusinessPlan(saveFile(userDirectory, fileBusinessPlan, "businessPlan",etudiant));
        }else {
        	throw new ElearningException(new ErrorAPI("Impossible de sauvegarder le business plan"));
		}

        // Enregistrement de la facture (optionnel)
        if (fileFacture != null && !fileFacture.isEmpty()) {
        	candidature.setFacture(saveFile(userDirectory, fileFacture, "facture",etudiant));
        }

        // Enregistrement de la conformité (optionnel)
        if (fileConformite != null && !fileConformite.isEmpty()) {
        	candidature.setConformite(saveFile(userDirectory, fileConformite, "attestation_conformite",etudiant));
        }
        
        candidature=candidatureRepository.save(candidature);
        senderMail.sendMailConfirmCanditatureSalon(etudiant);
        UserElearning user = new UserElearning();
		user.setNom((etudiant.getNom()+" "+etudiant.getPrenom()));
		user.setEmail(etudiant.getEmail());
		user.setTelephone(etudiant.getTelephone());
		user.setMatricule(etudiant.getMatricule());
		//user.setLogin(filePath.toString());
		return user;
    }
	
	public Page<Candidature> getListCandidaturePage(String filter, int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, 1000);

		Page<Candidature> pageCandidatures;
		
		pageCandidatures=candidatureRepository.findAll(pageable);
		
		return pageCandidatures; 

	}
	
	public Candidature getCandidature( int idCandidature) {
		Candidature candidature= new Candidature();
		if (candidatureRepository.existsById(idCandidature)) {
			candidature= candidatureRepository.findById(idCandidature).get();
		}else {
			return null;
		}
		return candidature;
	}
	
	
	public void sendMailToStudent(FormMail formMail,int idEtudiant) {
		
		if (formMail.getObjet()==null || formMail.getObjet().isBlank()) {
			Methode.returnErrorAPI("L'objet du mail ne doit pas etre vide");
		}
		
		if (formMail.getBodyHtml()==null || formMail.getBodyHtml().isBlank()) {
			Methode.returnErrorAPI("Le corps du mail ne doit pas etre vide");
		}
		
		if (etudiantRepository.existsById(idEtudiant)) {	
			Etudiant etudiant=etudiantRepository.findById(idEtudiant).get();
			senderMail.sendMailToStutend(etudiant, formMail);
		}else {
			Methode.returnErrorAPI("Cette etudiant n'est retroouber dans la base de donnee");
		}
	}

}
