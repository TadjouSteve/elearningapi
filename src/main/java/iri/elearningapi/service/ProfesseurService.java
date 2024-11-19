package iri.elearningapi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import iri.elearningapi.model.courModel.Chapitre;
import iri.elearningapi.model.courModel.EtudiantChapitre;
import iri.elearningapi.model.courModel.Module;
import iri.elearningapi.model.courModel.ProfesseurModule;
import iri.elearningapi.model.courModel.QroEtudiant;
import iri.elearningapi.model.userModel.Etudiant;
import iri.elearningapi.model.userModel.Professeur;
import iri.elearningapi.repository.courRepository.ChapitreRepository;
import iri.elearningapi.repository.courRepository.EtudiantChapitreRepository;
import iri.elearningapi.repository.courRepository.ModuleRepository;
import iri.elearningapi.repository.courRepository.ProfesseurModuleRepository;
import iri.elearningapi.repository.courRepository.QroEtudiantRepository;
import iri.elearningapi.repository.userRepository.EtudiantRepository;
import iri.elearningapi.repository.userRepository.ProfesseurRepository;
import iri.elearningapi.utils.elearningData.Profil;
import iri.elearningapi.utils.elearningData.Statut;
import iri.elearningapi.utils.elearningFunction.Cryptage;
import iri.elearningapi.utils.elearningFunction.Methode;
import iri.elearningapi.utils.elearningFunction.PasswordGenerator;
import iri.elearningapi.utils.elearningFunction.SenderMail;
import iri.elearningapi.utils.errorClass.ElearningException;
import iri.elearningapi.utils.errorClass.ErrorAPI;
import iri.elearningapi.utils.form.formInt.FormLink;
import iri.elearningapi.utils.form.formOut.FormChapitre;
import iri.elearningapi.utils.form.formOut.FormCorrectionQro;
import iri.elearningapi.utils.form.formOut.FormModule;
import iri.elearningapi.utils.form.formOut.FormViewProfesseur;
import iri.elearningapi.utils.form.formOut.UserElearning;

@Service
public class ProfesseurService {
	@Autowired
	private ProfesseurRepository professeurRepository;

	@Autowired
	private EtudiantRepository etudiantRepository;

	@Autowired
	private QroEtudiantRepository qroEtudiantRepository;

	@Autowired
	private ModuleRepository moduleRepository;

	@Autowired
	private EtudiantChapitreRepository etudiantChapitreRepository;

	@Autowired
	private ProfesseurModuleRepository professeurModuleRepository;

	@Autowired
	private ChapitreRepository chapitreRepository;

	@Autowired
	private SenderMail senderMail;

	private static final Random RANDOM = new Random();

	public UserElearning getLoginProf(UserElearning userLogin) {
		Professeur professeur = new Professeur();

		professeur = professeurRepository.findByTelephone(userLogin.getLogin());
		if (professeur != null && professeurRepository.existsById(professeur.getId())) {
			UserElearning user = new UserElearning();
			user.setEmail(professeur.getEmail());
			user.setId(professeur.getId());
			// user.setMatricule(etudiant.getMatricule());
			user.setNom(professeur.getNom());
			user.setPrenom(professeur.getPrenom());
			user.setProfil(Profil.PROFESSEUR_USER);
			user.setPassword(professeur.getPassword());
			user.setLevel(3);
			user.setConfirmation(1);
			user.setOpenDashboard(true);
			return user;
		}

		return null;
	}

	public Page<Professeur> getListProfesseurs(String filter, int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, 50);

		Page<Professeur> pageProfesseurs;

		if (filter != null) {
			pageProfesseurs = professeurRepository
					.findByNomStartingWithOrPrenomStartingWithOrProfessionStartingWithOrEmailStartingWithOrderByNomAsc(
							filter, filter, filter, filter, pageable);

			if (pageProfesseurs.isEmpty()) {
				pageProfesseurs = professeurRepository
						.findByNomContainingOrPrenomContainingOrProfessionContainingOrEmailContainingOrderByNomAsc(
								filter, filter, filter, filter, pageable);
			}

		} else {
			pageProfesseurs = professeurRepository.findAllByOrderByNomAsc(pageable);
		}
		return pageProfesseurs;
	}

	public Professeur createProfesseur(Professeur professeur) {
		controlFormProfesseur(professeur, true);
		String passworrd = PasswordGenerator.generatePassword(8);
		professeur.setPassword(Cryptage.getMd5(passworrd));
		professeur.setDateInscription(new Date());
		professeur.setMatricule(generateMatriculeForProfesseur());
		professeur = professeurRepository.save(professeur);
		professeur.setPassword(passworrd);
		// System.out.println("Pass Word: "+professeur.getPassword());
		senderMail.sendMailAfterRegistration(professeur);
		// professeur.setPassword("");
		return professeur;
	}

	private String generateMatriculeForProfesseur() {
		String matricule;
		do {
			int prefix = RANDOM.nextInt(90) + 10; // Génère un nombre aléatoire à deux chiffres
			int suffix = RANDOM.nextInt(9000) + 1000; // Génère un nombre aléatoire à quatre chiffres
			matricule = prefix + "PROF" + suffix;
		} while (professeurRepository.findByMatricule(matricule) != null);
		return matricule;
	}

	public Professeur getProfesseur(String matricule) {
		Professeur professeur = new Professeur();

		if (professeurRepository.existsByMatricule(matricule)) {
			professeur = professeurRepository.findByMatricule(matricule);
		}

		return professeur;
	}

	public FormViewProfesseur getFormViewProfesseur(String matricule) {
		Professeur professeur = getProfesseur(matricule);
		FormViewProfesseur formViewProfesseur = new FormViewProfesseur(professeur.getId(), professeurRepository,
				moduleRepository);

		return formViewProfesseur;
	}

	private void controlFormProfesseur(Professeur professeur, Boolean isNew) {
		if (professeur.getNom() == null || professeur.getNom().isBlank() || professeur.getNom().length() < 2) {
			throw new ElearningException(new ErrorAPI(
					"Le nom du professeur ne doit pas être vide, ou contenir moins de trois(03) caractères", 0));
		}

		if (professeur.getEmail() == null || !Methode.isValidEmail(professeur.getEmail())) {
			throw new ElearningException(new ErrorAPI("L'adresse mail du professeur est invalide", 0));
		}

		if (professeur.getEmail() != null && professeurRepository.existsByEmail(professeur.getEmail())) {
			if (professeurRepository.findByEmail(professeur.getEmail()).getId() != professeur.getId()) {
				throw new ElearningException(
						new ErrorAPI("L'adresse mail entrée est deja attribuer a un autre professeur", 0));
			}
		}

		if (professeur.getTelephone() != null && professeurRepository.existsByTelephone(professeur.getTelephone())) {
			if (professeurRepository.findByTelephone(professeur.getTelephone()).getId() != professeur.getId()) {
				throw new ElearningException(
						new ErrorAPI("Le numero de téléphone entré est deja attribuer a un autre professeur", 0));
			}
		}

		if (professeur.getTelephone() != null && etudiantRepository.existsByTelephone(professeur.getTelephone())) {
			throw new ElearningException(
					new ErrorAPI("Le numero de téléphone entré est deja utiliser par un compte Etudiant", 0));
		}

		if (professeur.getEmail() != null && etudiantRepository.existsByEmail(professeur.getEmail())) {
			throw new ElearningException(
					new ErrorAPI("L'adresse mail entrée est deja utiliser par un compte Etudiant", 0));
		}
	}

	public void linkProfesseurToModule(String matriculeProfesseur, List<FormLink> formLinks) {
		for (FormLink formLink : formLinks) {
			System.out.println("les lien idmodule: " + formLink.getIdModule() + " || islinked= " + formLink.isLinked());
		}
		if (professeurRepository.existsByMatricule(matriculeProfesseur)) {
			Professeur professeur = professeurRepository.findByMatricule(matriculeProfesseur);
			for (FormLink formLink : formLinks) {
				if (moduleRepository.existsById(formLink.getIdModule())) {
					Module module = moduleRepository.findById(formLink.getIdModule()).get();
					boolean isAlReadyLink = module.getProfesseurModules().stream()
							.anyMatch(item -> (item.getProfesseur() != null
									&& item.getProfesseur().getMatricule() == professeur.getMatricule()));

					//
					if (formLink.isLinked()) {
						if (!isAlReadyLink) {
							ProfesseurModule professeurModule = new ProfesseurModule();
							professeurModule.setModule(module);
							professeurModule.setProfesseur(professeur);
							professeurModuleRepository.save(professeurModule);
							// System.out.println("enregistrement du lien");
						}

					} else {
						if (isAlReadyLink) {
							Optional<ProfesseurModule> professeurModuleOptional = module.getProfesseurModules().stream()
									.filter(item -> (item.getProfesseur() != null
											&& item.getProfesseur().getMatricule() == professeur.getMatricule()))
									.findAny();
							if (professeurModuleOptional.isPresent()) {
								professeurModuleRepository.deleteEasy(professeurModuleOptional.get().getId());
								// System.out.println("supression du lien");
							}
						}
					}
					//
				}
			}
		}
	}

	public List<FormModule> getListModules() {
		List<FormModule> formModules = new ArrayList<FormModule>();

		for (Module module : moduleRepository.findAllByOrderByTitreAsc()) {
			if (!module.getGammeEtudiantModules().isEmpty()) {
				FormModule formModule = new FormModule();
				formModule.setIdModule(module.getIdModule());
				formModule.setTitre(module.getTitre());
				formModule.setTitreEn(module.getTitreEn());

				List<FormChapitre> formChapitres = new ArrayList<FormChapitre>();

				for (Chapitre chapitre : module.getChapitres()) {
					FormChapitre formChapitre = new FormChapitre();
					formChapitre.setIdChapitre(chapitre.getIdChapitre());
					formChapitre.setIdModule(module.getIdModule());
					formChapitre.setTitre(chapitre.getTitre());
					formChapitre.setTotalQRO(chapitre.getQros().size());
					formChapitre.setTitreModule(module.getTitre());

					formChapitres.add(formChapitre);
				}

				formModule.setChapitres(formChapitres);

				formModules.add(formModule);
			}

		}
		return formModules;
	}

	public FormCorrectionQro getFormCorrectionQro(int idChapitre) {
		if (chapitreRepository.existsById(idChapitre)) {
			Chapitre chapitre = chapitreRepository.findById(idChapitre).get();
			FormCorrectionQro formCorrectionQro = new FormCorrectionQro();
			Boolean haveQRO = false;
			System.out.println("Etape :01");
			do {
				System.out.println("Etape :02");
				EtudiantChapitre etudiantChapitre;
				List<EtudiantChapitre> listEtudiantChapitres = etudiantChapitreRepository
						.findByChapitreAndStatut(chapitre, Statut.NOT_CORRECTED.name());

				if (!listEtudiantChapitres.isEmpty()) {
					etudiantChapitre = listEtudiantChapitres.get(0);
					Etudiant etudiant = etudiantChapitre.getEtudiant();
					
					List<QroEtudiant> qroEtudiants = new ArrayList<QroEtudiant>();
					for (QroEtudiant qroEtudiant : etudiant.getQroEtudiants()) {
						if (qroEtudiant.getQro().getChapitre().getIdChapitre() == chapitre.getIdChapitre()) {
							qroEtudiants.add(qroEtudiant);
						}
					}
					haveQRO = !qroEtudiants.isEmpty();

					System.out.println("value HaveQRO 01 :" + haveQRO);
					System.out.println("value HaveQRO 01-01 :" + etudiant.getQroEtudiants().isEmpty());
					if (!haveQRO) {
						etudiantChapitre.setStatut(Statut.CORRECTED.name());
						etudiantChapitreRepository.save(etudiantChapitre);
						System.out.println("Etape :02 mise a jour du statut");
					}
					formCorrectionQro.setEtudiant(etudiant);
					formCorrectionQro.setQroEtudiants(qroEtudiants);
				} else {
					haveQRO = true;
				}
				System.out.println("value HaveQRO 02 :" + haveQRO);
			} while (!haveQRO);
			System.out.println("Etape :03");
			return formCorrectionQro;
		} else {
			throw new ElearningException(new ErrorAPI("le chapitre utilisaer pour recuperer les qro n'existe pas"));
		}
	}

	public void setScoreQRO(List<FormLink> formScoreQros) {
		if (!formScoreQros.isEmpty()) {
			for (FormLink formScoreQro : formScoreQros) {
				if (qroEtudiantRepository.existsById(formScoreQro.getIdElement())) {
					QroEtudiant qroEtudiant = qroEtudiantRepository.findById(formScoreQro.getIdElement()).get();
					qroEtudiant.setNote(formScoreQro.getNote());
					qroEtudiant=qroEtudiantRepository.save(qroEtudiant);
				}
			}

			FormLink formScoreQro01 = formScoreQros.get(0);
			if (qroEtudiantRepository.existsById(formScoreQro01.getIdElement())) {
				QroEtudiant qroEtudiant = qroEtudiantRepository.findById(formScoreQro01.getIdElement()).get();
				Chapitre chapitre = qroEtudiant.getQro().getChapitre();
				Etudiant etudiant = qroEtudiant.getEtudiant();

				EtudiantChapitre etudiantChapitre = etudiantChapitreRepository.findByEtudiantAndChapitre(etudiant,
						chapitre);
				etudiantChapitre.setStatut(Statut.CORRECTED.name());
				etudiantChapitre.setDateCorrection(new Date());
				etudiantChapitre = etudiantChapitreRepository.save(etudiantChapitre);
			}
		}else {
			//throw new ElearningException(new ErrorAPI("La liste des reponses envoyer est vide"));
		}
	}

}
