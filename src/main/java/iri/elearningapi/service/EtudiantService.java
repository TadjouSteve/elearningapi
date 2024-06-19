package iri.elearningapi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
//import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import iri.elearningapi.model.courModel.Chapitre;
import iri.elearningapi.model.courModel.EtudiantChapitre;
import iri.elearningapi.model.courModel.EtudiantModule;
import iri.elearningapi.model.courModel.Module;
import iri.elearningapi.model.courModel.Proposition;
import iri.elearningapi.model.courModel.Qcm;
import iri.elearningapi.model.userModel.Etudiant;
import iri.elearningapi.repository.courRepository.ChapitreRepository;
import iri.elearningapi.repository.courRepository.EtudiantChapitreRepository;
import iri.elearningapi.repository.courRepository.EtudiantModuleRepository;
import iri.elearningapi.repository.courRepository.ModuleRepository;
import iri.elearningapi.repository.userRepository.EtudiantRepository;
import iri.elearningapi.repository.userRepository.ProfesseurRepository;
import iri.elearningapi.utils.elearningData.Profil;
import iri.elearningapi.utils.elearningFunction.Cryptage;
import iri.elearningapi.utils.elearningFunction.Methode;
import iri.elearningapi.utils.elearningFunction.SenderMail;
import iri.elearningapi.utils.errorClass.ElearningException;
import iri.elearningapi.utils.errorClass.ErrorAPI;
import iri.elearningapi.utils.form.formInt.FormQcmForValidation;
import iri.elearningapi.utils.form.formOut.FormChapitre;
import iri.elearningapi.utils.form.formOut.FormViewEtudiant;
import iri.elearningapi.utils.form.formOut.UserDashboard;
import iri.elearningapi.utils.form.formOut.UserElearning;

@Service
public class EtudiantService {
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
	private SenderMail senderMail;

	private static final Random RANDOM = new Random();

	public UserElearning enregistrementEtudiant(Etudiant etudiant) {
		controlEtudiant(etudiant, true);

		etudiant.setMatricule(generateUniqueMatricule());

		etudiant.setNom(Methode.upperCaseFirst(etudiant.getNom()));
		etudiant.setPrenom(Methode.upperCaseFirst(etudiant.getPrenom()));
		etudiant.setLieuNaissance(Methode.upperCaseFirst(etudiant.getLieuNaissance()));
		etudiant.setRegion(Methode.upperCaseFirst(etudiant.getRegion()));
		etudiant.setProfession(Methode.upperCaseFirst(etudiant.getProfession()));
		etudiant.setNomEntreprise(Methode.upperCaseFirst(etudiant.getNomEntreprise()));

		etudiant.setPassword(Cryptage.getMd5(etudiant.getPassword()));

		etudiant.setLastConnexion(new Date());
		etudiant.setDateInscription(new Date());
		// etudiant.setPassword(passwordEncoder.(etudiant.getPassword()));
		etudiant = etudiantRepository.save(etudiant);
		senderMail.sendMailAfterRegistration(etudiant);
		UserElearning user = new UserElearning();
		user.setEmail(etudiant.getEmail());
		user.setId(etudiant.getId());
		user.setMatricule(etudiant.getMatricule());
		user.setNom(etudiant.getNom());
		user.setPrenom(etudiant.getPrenom());
		user.setProfil(Profil.ETUDIANT_USER);
		user.setProfession(etudiant.getProfession());
		user.setNomEntreprise(etudiant.getNomEntreprise());

		return user;
	}

	public UserElearning getLogin(UserElearning userLogin) {
		Etudiant etudiant = null;
		Etudiant etudiant2 = null;
		etudiant = etudiantRepository.findByEmailOrTelephone(userLogin.getLogin(), userLogin.getLogin());
		etudiant2 = etudiantRepository.findByPassword(Cryptage.getMd5(userLogin.getPassword()));
		if (etudiant != null && etudiant2 != null && etudiant.getId() == etudiant2.getId()) {
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
			//System.out.println("== go N2 ==");
			return user;
		}
		return null;
	}

	@Async
	public void updateLastConnexion(int idEtudiant) {
		if (etudiantRepository.existsById(idEtudiant)) {
			Etudiant etudiant = etudiantRepository.findById(idEtudiant).get();
			etudiant.setLastConnexion(new Date());
			etudiantRepository.save(etudiant);
			//System.out.println("== go N111 ==");
		}
	}

	public void controlEtudiant(Etudiant etudiant, boolean isNew) {
		if (etudiant.getNom() == null || etudiant.getNom().trim().length() < 3)
			throw new ElearningException(new ErrorAPI(
					"Le nom du canditat ne doit pas être vide, ou contenir moins de trois(03) caractères", 0));

		if (etudiant.getNom().trim().length() > 50)
			throw new ElearningException(
					new ErrorAPI("Le nom du canditat est trop long...! Il ne doit pas dépasser 50 caractères", 0));

		if (etudiant.getPrenom() == null && etudiant.getPrenom().trim().length() > 50)
			throw new ElearningException(
					new ErrorAPI("Le prenom du canditat est trop long...! Il ne doit pas dépasser 50 caractères", 0));

		if (etudiant.getDateNaissance() == null)
			throw new ElearningException(new ErrorAPI("La date de naissance du canditat est obligatoire", 0));

		if (etudiant.getEmail() == null || etudiant.getEmail().trim().isEmpty())
			throw new ElearningException(new ErrorAPI("L'email du canditat est obligatoire", 0));

		if (!isValidEmail(etudiant.getEmail()))
			throw new ElearningException(new ErrorAPI("L'email entré est invalide", 0));

		if (etudiant.getProfession() == null || etudiant.getProfession().trim().isEmpty())
			throw new ElearningException(new ErrorAPI("La profession du canditat est obligatoire", 1));

		if (etudiant.getRegion() == null || etudiant.getRegion().trim().isEmpty())
			throw new ElearningException(new ErrorAPI("La région du canditat est obligatoire", 1));

		if (etudiant.getLieuNaissance() == null || etudiant.getLieuNaissance().trim().isEmpty())
			throw new ElearningException(new ErrorAPI("Le lieu de naissance du canditat est obligatoire", 0));

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
		
		if (etudiant.getTelephone()!=null && professeurRepository.existsByTelephone(etudiant.getTelephone())) {
			throw new ElearningException(new ErrorAPI("Le numero de téléphone entré est deja utiliser par un compte Professeur",0));
		}
		
		
		if (etudiant.getEmail()!=null && professeurRepository.existsByEmail(etudiant.getEmail())) {
			throw new ElearningException(new ErrorAPI("L'adresse mail entrée est deja utiliser par un compte Professeur",0));
		}

	}

	public boolean isValidEmail(String email) {
		String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
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
			}

			for (Module module01 : moduleRepository.findAll()) {
				if (module01.getDateDeblocage() == null || module01.getDateDeblocage().before(new Date())) {
					userDashboard.setModuleAccessible(userDashboard.getModuleAccessible() + 1);
					module01.setIsAccessible(true);
				}

				module01.setFistTime(false);
				for (EtudiantModule etudiantModule : etudiant.getEtudiantModules()) {
					if (etudiantModule.getModule().getIdModule() == module01.getIdModule()) {
						module01.setFistTime(true);
					}
				}
				userDashboard.addModule(module01);
			}
			
			
			userDashboard.setModuleTotal((int) moduleRepository.count());
			userDashboard.setChapitreTotal((int)chapitreRepository.count());
			userDashboard.setQcmTotal((int) chapitreRepository.findAllByOrderByTitreAsc().stream().mapToInt(item->item.getQcms().size()).sum());

		}     

		return userDashboard;
	}

	public List<FormChapitre> getAllChapitreModuleEtudiant(int idEtudiant, int idModule) {
		List<FormChapitre> formChapitres = new ArrayList<>();

		if (etudiantRepository.existsById(idEtudiant) && moduleRepository.existsById(idModule)) {

			Module module = moduleRepository.findById(idModule).get();

			Boolean isFirstOpen = true;

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
			}

			for (Chapitre chapitre : moduleRepository.findById(idModule).get().getChapitres()) {
				FormChapitre formChapitre = new FormChapitre();

				formChapitre.setIdChapitre(chapitre.getIdChapitre());
				formChapitre.setIdModule(idModule);
				formChapitre.setTitre(chapitre.getTitre());
				formChapitre.setImage(chapitre.getImage());

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
				etudiantChapitre = etudiantChapitreRepository.save(etudiantChapitre);
				senderMail.sendMailFirstAccesChapitre(etudiantChapitre.getEtudiant(), chapitre);
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
}
