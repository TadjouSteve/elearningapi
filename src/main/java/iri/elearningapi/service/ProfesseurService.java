package iri.elearningapi.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import iri.elearningapi.model.courModel.Module;
import iri.elearningapi.model.courModel.ProfesseurModule;
import iri.elearningapi.model.userModel.Professeur;
import iri.elearningapi.repository.courRepository.ModuleRepository;
import iri.elearningapi.repository.courRepository.ProfesseurModuleRepository;
import iri.elearningapi.repository.userRepository.EtudiantRepository;
import iri.elearningapi.repository.userRepository.ProfesseurRepository;
import iri.elearningapi.utils.elearningFunction.Cryptage;
import iri.elearningapi.utils.elearningFunction.Methode;
import iri.elearningapi.utils.elearningFunction.PasswordGenerator;
import iri.elearningapi.utils.elearningFunction.SenderMail;
import iri.elearningapi.utils.errorClass.ElearningException;
import iri.elearningapi.utils.errorClass.ErrorAPI;
import iri.elearningapi.utils.form.formInt.FormLink;
import iri.elearningapi.utils.form.formOut.FormViewProfesseur;

@Service
public class ProfesseurService {
	@Autowired
	private ProfesseurRepository professeurRepository;

	@Autowired
	private EtudiantRepository etudiantRepository;

	@Autowired
	private ModuleRepository moduleRepository;

	@Autowired
	private ProfesseurModuleRepository professeurModuleRepository;

	@Autowired
	private SenderMail senderMail;

	private static final Random RANDOM = new Random();

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
		professeur.setPassword("");
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
			System.out.println("les lien idmodule: "+formLink.getIdModule()+" || islinked= "+formLink.isLinked());
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
							//System.out.println("enregistrement du lien");
						}

					} else {
						if (isAlReadyLink) {
							Optional<ProfesseurModule> professeurModuleOptional = module.getProfesseurModules().stream()
									.filter(item -> (item.getProfesseur() != null
											&& item.getProfesseur().getMatricule() == professeur.getMatricule()))
									.findAny();
							if (professeurModuleOptional.isPresent()) {
								professeurModuleRepository.deleteEasy(professeurModuleOptional.get().getId());
								//System.out.println("supression du lien");
							}
						}
					}
					//
				}
			}
		}
	}
}
