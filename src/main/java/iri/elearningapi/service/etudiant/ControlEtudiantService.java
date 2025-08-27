package iri.elearningapi.service.etudiant;

import org.springframework.stereotype.Service;

import iri.elearningapi.model.userModel.GammeEtudiant;
import iri.elearningapi.model.userModel.Information;
import iri.elearningapi.repository.userRepository.EtudiantRepository;
import iri.elearningapi.utils.elearningData.Code;
import iri.elearningapi.utils.elearningFunction.Methode;
import iri.elearningapi.utils.errorClass.ElearningException;
import iri.elearningapi.utils.errorClass.ErrorAPI;

@Service
public class ControlEtudiantService {
	private EtudiantRepository etudiantRepository;

	public void controlInformationEtudiant(Information information, GammeEtudiant gammeEtudiant) {
		if (information != null && gammeEtudiant != null) {
			if (Code.C001.name().equals(gammeEtudiant.getCode())) {
				controlEntrepreneur(information);
			} else if (Code.C002.name().equals(gammeEtudiant.getCode())) {
				// controlEtudiantEtPorteurProjet(information);
				controlSexe(information.getSexe(), 0);
			} else if (Code.C003.name().equals(gammeEtudiant.getCode())) {
				controlFamille(information);
			} else if (Code.C004.name().equals(gammeEtudiant.getCode())) {
				controlProfesseur(information);
			} else {
				throw new ElearningException(new ErrorAPI(
						"Le profile de fromation n'as pas correctement ete defini, recommencer le processus d'enregistrement..!",
						-1));
			}
		}
	}

	private void controlEntrepreneur(Information information) {
		controlSexe(information.getSexe(), 0);
		controlNomEntreprise(information.getNomEntreprise(), 1);
		controlProfession(information.getProfession(), 1);
		controlDomaineActivite(information.getDomaineActivite(), 1);
		controlAnneeExperience(information.getAnneeExperience(), 1);
		controlSecteurActivite(information.getSecteurActivite(), 1);
		controlTailleEntreprise(information.getTailleEntreprise(), 1);
		controlMarcheCible(information.getMarcheCible(), 1);
		controlAnneeEntreprise(information.getAnneeEntreprise(), 1);
		controlChiffreAffaire(information.getChiffreAffaire(), 1);

		// niveau 02
		/*
		 * controlMotivation(information.getMotivation(),
		 * "Veuillez sélectionner vos motivations", 2);
		 * controlSouhaitAcquisitionDurant(information.getSouhaitAcquisitionDurant(),
		 * "Veuillez sélectionner ce que vous espérez améliorer dans votre entreprise durant la formation"
		 * , 2); controlNiveauConnaissance(information.getNiveauConnaissance(), 2);
		 * 
		 * controlFormatFormation(information.getFormatFormation(), 2);
		 * 
		 * controlSouhaitAcquisitionFin(information.getSouhaitAcquisitionFin(),
		 * "Veuillez remplir le champ sur ce que vous espérez acquérir à la fin de cette formation "
		 * , 2);
		 * controlConnaissanceMarcheLocal(information.getConnaissanceMarcheLocal(), 2);
		 * controlPreferenceHoraire(information.getPreferenceHoraire(), 2);
		 */
	}

	private void controlEtudiantEtPorteurProjet(Information information) {
		controlSexe(information.getSexe(), 0);
		controlUniversite(information.getUniversite(),
				"Le nom de votre Université/Etablissement de doit pas etre vide ou contenir moins de 3 caractères, si vous n'etes pas etudiant écrivez \"Sans objet\" dans le champ) ",
				1);
		controlFiliere(information.getFiliere(), 1);
		controlNiveauAcademique(information.getNiveauAcademique(), 1);
		controlIdeeProjet(information.getIdeeProjet(), 1);
		controlSecteurActivite(information.getSecteurActivite(), 1);
		controlMarcheCible(information.getMarcheCible(), 1);

		// niveau 02

		controlMotivation(information.getMotivation(), "Veuillez sélectionner vos motivations", 2);
		controlSouhaitAcquisitionDurant(information.getSouhaitAcquisitionDurant(),
				"Veuillez sélectionner ce que vous espérez pouvoir obtenir comme compétences durant la formation", 2);
		controlNiveauConnaissance(information.getNiveauConnaissance(), 2);

		controlFormatFormation(information.getFormatFormation(), 2);

		controlSouhaitAcquisitionFin(information.getSouhaitAcquisitionFin(),
				"Veuillez remplir le champ sur ce que vous espérez acquérir à la fin de cette formation ", 2);
		controlsouhaitFormationn(information.getSouhaitFormation(),
				"Veuillez remplir le champ sur: Quelles compétences ou sujets spécifiques aimeriez-vous particulièrement aborder au cours de cette formation? (si vous n'avais de besoin specicifique écrivez \"Sans objet\" dans le champ)",
				2);

	}

	private void controlFamille(Information information) {
		controlSexe(information.getSexe(), 0);
	}

	private void controlProfesseur(Information information) {
		controlSexe(information.getSexe(), 0);
		controlUniversite(information.getUniversite(), null, 1);
		controlFaculte(information.getFaculte(),
				"Le nom de votre faculté ne doit pas être vide ou contenir moins de 5 caractères", 1);

		// controlDepartement(information.getDepartement(), 1);
		controlFonction(information.getFonction(), 1);
		controlGrade(information.getGrade(), 1);

		// niveau 2
		/*
		 * controlMotivation(information.getMotivation(), null, 2);
		 * controlSouhaitAcquisitionFin(information.getSouhaitAcquisitionFin(),
		 * "Veuillez remplir le champ sur: Qu’espérez-vous accomplir à la fin de cette formation ?"
		 * , 2); controlBienFondeUniversitaire(information.getBienFondeUniversitaire(),
		 * 2); controlDisposerAccompagnement(information.getDisposerAccompagnement(),
		 * 2);
		 */
	}

	private void controlAmeliorationDurant(String string, String message, int etape) {
		if (!Methode.isCorrectString(string, 10, 0))
			throw new ElearningException(new ErrorAPI(
					message != null ? message
							: "Remplissez ce champ : Quels sont vos domaines à améliorer durant cette formation ?",
					etape));
	}

	private void controlAnneeEntreprise(int value, int etape) {
		if (value < 0 || value > 100)
			throw new ElearningException(new ErrorAPI(
					"Le nombre d'annee d'existance de votre entreprise doit etre compris entre 0 et 100", etape));
	}

	private void controlAnneeExperience(int value, int etape) {
		if (value < 0 || value > 100)
			throw new ElearningException(
					new ErrorAPI("Remplir le champ annee d'experience ( valeur valide entre 0 et 100 )", etape));
	}

	private void controlBienFondeUniversitaire(String string, int etape) {
		if (!Methode.isCorrectString(string))
			throw new ElearningException(new ErrorAPI(
					"Remplir le champ sur le bien-fondé d’une formation en entreprenariat en milieu universitaire",
					etape));
	}

	private void controlChiffreAffaire(String string, int etape) {
		if (!Methode.isCorrectString(string))
			throw new ElearningException(
					new ErrorAPI("Selectionner un interval de votre chiffre d'affaire annuel", etape));
	}

	private void controlConnaissanceMarcheLocal(String string, int etape) {
		if (!Methode.isCorrectString(string))
			throw new ElearningException(new ErrorAPI("Remplir le champ sur votre connaissnce du marche local", etape));
	}

	private void controlDefinitionEntrepreneuriat(String string, int etape) {
		if (!Methode.isCorrectString(string))
			throw new ElearningException(
					new ErrorAPI("Veuillez donner votre définition de ce qu’est l’entrepreneuriat.", etape));
	}

	private void controlDepartement(String string, int etape) {
		if (!Methode.isCorrectString(string))
			throw new ElearningException(new ErrorAPI("Veuillez remplir le champ sur votre departement.", etape));
	}

	private void controlDisposerAccompagnement(String string, int etape) {
		if (!Methode.isCorrectString(string))
			throw new ElearningException(new ErrorAPI(
					"Veuillez remplir le champ sur votre disposition à accompagner les étudiants.", etape));
	}

	private void controlDomaineActivite(String string, int etape) {
		if (!Methode.isCorrectString(string))
			throw new ElearningException(new ErrorAPI("Veuillez remplir le champ sur votre domaine d’activité", etape));
	}

	private void controlDomaineFamille(String string, int etape) {
		if (!Methode.isCorrectString(string))
			throw new ElearningException(
					new ErrorAPI("Veuillez remplir le champ sur  domaine d’activité de votre famille", etape));
	}

	private void controlEntrepriseCreer(String string, int etape) {
		if (!Methode.isCorrectString(string))
			throw new ElearningException(new ErrorAPI(
					"Veuillez remplir le champ concernant la question de savoir si vous avez déjà créé une entreprise familiale ou non",
					etape));
	}

	private void controlFaculte(String string, String message, int etape) {
		if (!Methode.isCorrectString(string, 5, 0))
			throw new ElearningException(new ErrorAPI(message != null ? message
					: "Le nom de votre faculté ne doit pas être vide ou contenir moins de 5 caractères…!(Si vous n’êtes pas étudiant, écrivez \"Sans objet\" dans le champ)",
					etape));
	}

	private void controlFiliere(String string, int etape) {
		if (!Methode.isCorrectString(string, 5, 0))
			throw new ElearningException(new ErrorAPI(
					"Le nom de votre filière ne doit pas être vide ou contenir moins de 5 caractères…! (Si vous n’êtes pas étudiant, écrivez \"Sans objet\" dans le champ)",
					etape));
	}

	private void controlFonction(String string, int etape) {
		if (!Methode.isCorrectString(string, 5, 0))
			throw new ElearningException(new ErrorAPI(
					"Le titre de votre function ne doit pas être vide ou contenir moins de 5 caractères… !", etape));
	}

	private void controlFormatFormation(String string, int etape) {
		if (!Methode.isCorrectString(string))
			throw new ElearningException(new ErrorAPI(
					"Veuillez sélectionner les préférences d’apprentissage qui vous conviennent le plus.", etape));
	}

	private void controlGrade(String string, int etape) {
		if (!Methode.isCorrectString(string))
			throw new ElearningException(new ErrorAPI("Veuillez sélectionner votre grade", etape));
	}

	private void controlIdeeProjet(String string, int etape) {
		if (!Methode.isCorrectString(string))
			throw new ElearningException(new ErrorAPI(
					"Veuillez remplir le champ concernant une idee de projet que vous avez. (Si vous n’avez pas d'idee de projet, écrivez \"Sans objet\" dans le champ)",
					etape));
	}

	private void controlMarcheCible(String string, int etape) {
		if (!Methode.isCorrectString(string))
			throw new ElearningException(new ErrorAPI(
					"Veuillez remplir le champ concernant votre marché cible. (Si vous n’avez pas de marché cible, écrivez \"Sans objet\" dans le champ)",
					etape));
	}

	private void controlMenbreFamille(int value, int etape) {
		if (value < 1 || value > 100)
			throw new ElearningException(new ErrorAPI(
					"Remplir le champ sur le nombre de persone que compte votre famille...! (de 1 a 100)", etape));
	}

	private void controlMotivation(String string, String message, int etape) {
		if (!Methode.isCorrectString(string))
			throw new ElearningException(new ErrorAPI(message != null ? message
					: "Veuillez remplir le champ sur vos motivations. (Si vous n’avez pas de motivation, écrivez \"Sans objet\" dans le champ)",
					etape));
	}

	private void controlNiveauAcademique(String string, int etape) {
		if (!Methode.isCorrectString(string))
			throw new ElearningException(new ErrorAPI(
					"Veuillez remplir le champ sur votre Niveau academique. (Si vous n’avez pas fait d'etude, écrivez \"Sans objet\" dans le champ)",
					etape));
	}

	private void controlNiveauConnaissance(String string, int etape) {
		if (!Methode.isCorrectString(string))
			throw new ElearningException(new ErrorAPI("Veuillez sélectionner votre niveau de connaissance", etape));
	}

	private void controlNiveauEtudeFamille(String string, int etape) {
		if (!Methode.isCorrectString(string))
			throw new ElearningException(new ErrorAPI(
					"Veuillez sélectionner le ou les differents niveau d'etude des mendres de votre famille", etape));
	}

	private void controlNomEntreprise(String string, int etape) {
		if (!Methode.isCorrectString(string, 5, 0))
			throw new ElearningException(new ErrorAPI(
					"Le nom de l’entreprise ne doit pas être vide ou contenir moins de 5 caractères. Si vous n’avez pas encore d’entreprise, modifiez votre profil de formation et inscrivez-vous en tant qu’étudiant ou porteur de projet.",
					etape));
	}

	private void controlNombreEntrepreneurFamille(String string, int etape) {
		if (!Methode.isCorrectString(string))
			throw new ElearningException(new ErrorAPI(
					"Veuillez remplir le champ sur Combien d’entrepreneur compte votre famille ? Et dans quels domaines exercent –t-ils ?",
					etape));
	}

	private void controlObstacleFamille(String string, int etape) {
		if (!Methode.isCorrectString(string))
			throw new ElearningException(new ErrorAPI(
					"Veuillez remplir le champ sur les obstacles ou difficultés auxquels peuvent faire face une famille d’entrepreneur ?",
					etape));
	}

	private void controlPreferenceHoraire(String string, int etape) {
		if (!Methode.isCorrectString(string))
			throw new ElearningException(new ErrorAPI(
					"Veuillez remplir le champ sur vos preferences horaires concernant la formation ?", etape));
	}

	private void controlProfession(String string, int etape) {
		if (!Methode.isCorrectString(string, 5, 0))
			throw new ElearningException(new ErrorAPI(
					"Le titre de votre profession ne doit pas être vide ou contenir moins de 5 caractères…!(Si vous n’avez pas  de profession, écrivez \"Sans objet\" dans le champ)",
					etape));
	}

	private void controlSecteurActivite(String string, int etape) {
		if (!Methode.isCorrectString(string, 5, 0))
			throw new ElearningException(new ErrorAPI(
					"Votre secteur d’activité ne doit pas être vide ou contenir moins de 5 caractères…!(Si vous n’avez pas  de secteur d’activité bien defini, écrivez \"Sans objet\" dans le champ)",
					etape));
	}

	private void controlSexe(String string, int etape) {
		if (!Methode.isCorrectString(string))
			throw new ElearningException(new ErrorAPI("Veuillez sélectionner votre genre  (sexe)", etape));
	}

	private void controlSouhaitAcquisitionDurant(String string, String message, int etape) {
		if (!Methode.isCorrectString(string))
			throw new ElearningException(new ErrorAPI(message != null ? message
					: "Veuillez remplir le champ sur: Qu’espérez-vous pouvoir améliorer dans votre entreprise durant la formation ?",
					etape));
	}

	private void controlSouhaitAcquisitionFin(String string, String message, int etape) {
		if (!Methode.isCorrectString(string))
			throw new ElearningException(new ErrorAPI(
					message != null ? message
							: "Veuillez remplir le champ sur: Que souhaitez-vous acquérir a la fin cette formation ? ?",
					etape));
	}

	private void controlsouhaitFormationn(String string, String message, int etape) {
		if (!Methode.isCorrectString(string))
			throw new ElearningException(new ErrorAPI(message != null ? message
					: "Veuillez remplir le champ sur: Quelles compétences ou sujets spécifiques aimeriez-vous particulièrement aborder au cours de cette formation? (si vous n'avais de besoin specicifique écrivez \"Sans objet\" dans le champ)",
					etape));
	}

	private void controlTailleEntreprise(String string, int etape) {
		if (!Methode.isCorrectString(string))
			throw new ElearningException(new ErrorAPI("Veuillez sélectionner la taille de votre entreprise."
					+ "Si vous n’avez pas encore d’entreprise, modifiez votre profil de formation et inscrivez-vous en tant qu’étudiant ou porteur de projet.",
					etape));
	}

	private void controltrancheAge(String string, int etape) {
		if (!Methode.isCorrectString(string))
			throw new ElearningException(new ErrorAPI(
					"Veuillez remplir le champ sur: Quelle est la tranche d’âge des membres de votre famille qui envisagent suivre cette formation ?",
					etape));
	}

	private void controlUniversite(String string, String message, int etape) {
		if (!Methode.isCorrectString(string, 3, 0))
			throw new ElearningException(new ErrorAPI(message != null ? message
					: "Le nom de votre université/établissement ne doit pas être vide ou contenir moins de 3 caractères",
					etape));
	}

}
