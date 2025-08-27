package iri.elearningapi.utils.elearningFunction;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import iri.elearningapi.model.courModel.Chapitre;
import iri.elearningapi.model.courModel.Module;
import iri.elearningapi.model.userModel.Etudiant;
import iri.elearningapi.model.userModel.Professeur;
import iri.elearningapi.utils.form.formInt.FormMail;
import iri.elearningapi.utils.form.formInt.FormQcmForValidation;

@Service
public class SenderMail {
	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private SendGridMail sendGridMail;

	@Async
	public void sendMailWithLinkForConfirmation(Etudiant etudiant) {
		String masculin = "Masculin";
		String tritreSex = "";
		tritreSex = (etudiant.getInformations() != null && !etudiant.getInformations().isEmpty())
				? masculin.equals(etudiant.getInformations().get(0).getSexe()) ? " Monsieur " : " Madame "
				: "Mr//Mme";

		String headerHTML = ""
				+ "<!DOCTYPE html PUBLIC “-//W3C//DTD XHTML 1.0 Transitional//EN” “https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd”>\r\n"
				+ "<html xmlns=“https://www.w3.org/1999/xhtml”>\r\n" + "<head>\r\n"
				+ "<title>confirmer votre addresse mail</title>\r\n"
				+ "<meta http–equiv=“Content-Type” content=“text/html; charset=UTF-8” />\r\n"
				+ "<meta http–equiv=“X-UA-Compatible” content=“IE=edge” />\r\n"
				+ "<meta name=“viewport” content=“width=device-width, initial-scale=1.0 “ />" + "<style>" + ""
				+ "td{text-align:right;margin-right:25px;}" + "tr{min-height:35px;border-bottom: 5px solid red;}"
				+ "</style>" + "</head>";

		String bodyHTML = ("" + "<body style='min-width:100%;'>" + "<div>"
				+ "<h2 style='color:blue'>Validation de votre enregistrement au programme de formation des 1000 jeunes entrepreneurs...!</h2>"
				+ "<h3>  " + tritreSex + " " + etudiant.getNom() + " " + etudiant.getPrenom() + "</h3><br\"><br\">"

				+ "<div style='font-size:19px'><span style='font-size:20px' >Pour valider votre inscription, cliquez sur ce lien: <span style='color:blue;font-weight:600;font-size:22px'><a href='https://programmeleadership.org/validationcompte/"
				+ etudiant.getLienConfirmation() + "' target='_blank'>" + "validez votre compte"
				+ "</a></span></span></div>"

				+ "<h5>Bienvenue au programme de formation organisé par le Groupe IRI (Institut de Recherche en Intelligences) en partenariat avec le MINJEC.<h5>"
				+ "<div>Vous êtes inscrit au programme avec l’e-mail suivant: <span style='color:blue;font-weight:600;font-size:15px'>"
				+ etudiant.getEmail() + "</span></div><br>" + getFooterMail() + "</body></html>");

		sendEmail(etudiant.getEmail(), (headerHTML + bodyHTML), "Demande de validation de  votre inscripton");
	}

	@Async
	public void sendMailWithLinkToConnectAfterValidationInscription(Etudiant etudiant) {
		Hibernate.initialize(etudiant.getInformations());
		String masculin = "Masculin";
		String tritreSex = "";
		tritreSex = (etudiant.getInformations() != null && !etudiant.getInformations().isEmpty())
				? masculin.equals(etudiant.getInformations().get(0).getSexe()) ? " Monsieur " : " Madame "
				: "Mr//Mme";
		String nomGammeEtudiant = (etudiant.getGammeEtudiant() != null) ? etudiant.getGammeEtudiant().getNom()
				: "entrepreneur--";
		/// String nomGammeEtudiant = "rien du tout";

		String headerHTML = ""
				+ "<!DOCTYPE html PUBLIC “-//W3C//DTD XHTML 1.0 Transitional//EN” “https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd”>\r\n"
				+ "<html xmlns=“https://www.w3.org/1999/xhtml”>\r\n" + "<head>\r\n"
				+ "<title>inscription reissi</title>\r\n"
				+ "<meta http–equiv=“Content-Type” content=“text/html; charset=UTF-8” />\r\n"
				+ "<meta http–equiv=“X-UA-Compatible” content=“IE=edge” />\r\n"
				+ "<meta name=“viewport” content=“width=device-width, initial-scale=1.0 “ />" + "<style>" + ""
				+ "td{text-align:right;margin-right:25px;}" + "tr{min-height:35px;border-bottom: 5px solid red;}"
				+ "</style>" + "</head>";

		String bodyHTML = ("" + "<body style='min-width:100%;'>" + "<div>"

				+ "<h3>La formation a commencé le 02 septembre 2024.<h5><br>"

				+ "<div>" + "<p style='font-size:19px;'>" + "Félicitations " + tritreSex
				+ " <span style='font-size:21px;font-weight:800;'>" + etudiant.getNom() + " " + etudiant.getPrenom()
				+ "</span>.<br><br>"
				+ "			Votre inscription sur la plateforme de capacitation entrepreneuriale <span style='color:green'> en tant que "
				+ nomGammeEtudiant + "</span> a été validée avec succès.<br><br>"
				+ "			C’est un plaisir pour nous de vous compter parmi les ambitieux bénéficiaires de cette opportunité de formation qui aura pour but ultime de parfaire votre Capacitation entrepreneuriale.<br><br>"

				+ "<div>Concerver vos informtion de connexion:<br><span style='font-size:18px'>Login: "
				+ etudiant.getEmail() + "</span><br><span style='font-size:18px'>Mot de passe: "
				+ etudiant.getPasswordClear() + "</span></div><br><br>"

				+ "			<p style='font-size:19px;'>Pendant un (01) mois du 02 septembre au 02 octobre 2024, vous bénéficierez des secrets du Leadership et de  l’entrepreneuriat local qui vous permettront assurément de bâtir des entreprises fortes, rentables, compétitives pour relever les défis du temps.<br><br>"

				+ "			Vous pourrez accéder à ladite formation en cliquant sur le lien suivant : <a href=\"https://programmeleadership.org\" target=\"_blank\">WWW.PROGRAMMELEADERSHIP.ORG</a> (les modules seront activés uniquement à la date de lancement le 02 septembre 2024. )<br><br>"

				+ "			Nous vous invitons à intégrer le groupe WhatsApp pour plus d’information sur le programme à l’adresse suivant :<a href=\"https://chat.whatsapp.com/FPdFxuA22W14Bub9iAJOrM\" target=\"-blank\">Lien du groupe whatsapp</a>"

				+ "			<br><br>NB : les meilleurs projets à l’issue du programme de Formation bénéficieront d’un accompagnement spécial de l’ensemble des partenaires du programme. Les accompagnements sont de l’ordre de :</p>"
				+ "			<ul style='font-size:20px;'>"
				+ "				<li>Une cagnotte de 50 Millions de FCFA mobilisée  par le Programme LEADERSHIP </li>"
				+ "				<li>Des investissements en fond de roulement et capitaux apportés par des potentiels actionnaires locaux</li>"
				+ "				<li>Des marchés et opportunités de prestation</li>"
				+ "				<li>Un  Accompagnement technique  courant dans les fonctions de l’entreprise par des experts</li>"
				+ "			</ul><br>\n" + "			Ensemble, bâtissons un Cameroun fort et prospère!"
				+ "		</p>"

				+ "<div><span style='font-size:20px'>Suivez l’actualité économique et entrepreneuriale sur notre plateforme <a href='https://programmeleadership.org/medias' target='_blank'> media et actualité</a></span></div><br>"
				+ "<div style='font-size:19px'>Vous etes inscrit au programme avec l'email suivant: <span style='color:blue;font-weight:600;font-size:12px'>"
				+ etudiant.getEmail() + "</span></div><br/>"

				+ "<div><span style='font-size:19px'>Vous pourrez vous connecter à la plateforme avec ce lien:<a href='https://programmeleadership.org' target='_blank'>Programme Leadership</a> </span></div><br>"

				+ getFooterMail() + "</body></html>");

		sendEmail(etudiant.getEmail(), (headerHTML + bodyHTML), "Confirmation d'inscription (IRI)");
	}

	@Async
	public void sendMailAfterRegistration(Etudiant etudiant) {
		String headerHTML = ""
				+ "<!DOCTYPE html PUBLIC “-//W3C//DTD XHTML 1.0 Transitional//EN” “https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd”>\r\n"
				+ "<html xmlns=“https://www.w3.org/1999/xhtml”>\r\n" + "<head>\r\n"
				+ "<title>inscription valide</title>\r\n"
				+ "<meta http–equiv=“Content-Type” content=“text/html; charset=UTF-8” />\r\n"
				+ "<meta http–equiv=“X-UA-Compatible” content=“IE=edge” />\r\n"
				+ "<meta name=“viewport” content=“width=device-width, initial-scale=1.0 “ />" + "<style>" + ""
				+ "td{text-align:right;margin-right:25px;}" + "tr{min-height:35px;border-bottom: 5px solid red;}"
				+ "</style>" + "</head>";
		String bodyHTML = ("" + "<body style='min-width:100%;'>" + "<div>"
				+ "<h2 style='color:blue'>Validation de votre Enregistement au programme de formation de 1000 entrepreuneur jeune...!</h2>"
				+ "<h3> Mr/Mme " + etudiant.getNom() + " " + etudiant.getPrenom() + "</h3><br\"><br\">"
				+ "<h5>Bienvenu au programme de formation organiser par le Groupe IRI (Institut de Recherche en Intelligence) en partenariat avec le MINJEC<h5>"
				+ "<div>Vous etes inscrit au programme avec l'email suivant: <span style='color:blue;font-weight:600;font-size:12px'>"
				+ etudiant.getEmail() + "</span></div>" + "</body></html>");

		sendEmail(etudiant.getEmail(), (headerHTML + bodyHTML), "Confirmation d'inscription (IRI)");
	}

	@Async
	public void sendMailAfterRegistration(Professeur professeur) {
		String headerHTML = ""
				+ "<!DOCTYPE html PUBLIC “-//W3C//DTD XHTML 1.0 Transitional//EN” “https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd”>\r\n"
				+ "<html xmlns=“https://www.w3.org/1999/xhtml”>\r\n" + "<head>\r\n"
				+ "<title>Enrolement professeur programme leadership</title>\r\n"
				+ "<meta http–equiv=“Content-Type” content=“text/html; charset=UTF-8” />\r\n"
				+ "<meta http–equiv=“X-UA-Compatible” content=“IE=edge” />\r\n"
				+ "<meta name=“viewport” content=“width=device-width, initial-scale=1.0 “ />" + "<style>" + ""
				+ "td{text-align:right;margin-right:25px;}" + "tr{min-height:35px;border-bottom: 5px solid red;}"
				+ "</style>" + "</head>";
		String bodyHTML = ("" + "<body style='min-width:100%;'>" + "<div>"
				+ "<h2 style='color:blue'>Vous avez été enregistré sur la plateforme en ligne du programme Leadership en tant que professeur.</h2>"
				+ "<h3> Mr/Mme " + professeur.getNom() + " " + professeur.getPrenom() + "</h3><br\"><br\">"
				+ "<h4>Information de connexion</h4>"
				+ "<span style='font-weight:600'>Email :<span style='font-weight:700;font-size:12px'>"
				+ professeur.getEmail() + "</span></span><br\">"
				+ "<span style='font-weight:600'>Téléphone :<span style='font-weight:700;font-size:12px'>"
				+ professeur.getTelephone() + "</span></span><br\">"
				+ "<span style='font-weight:600'>Mot de passe :<span style='font-weight:700;font-size:12px'>"
				+ professeur.getPassword() + "</span></span><br\">"
				+ "<h5>Bienvenu au programme de formation organiser par le Groupe IRI (Institut de Recherche en Intelligence) en partenariat avec le MINJEC<h5>"
				+ "<div>Vous etes inscrit au programme en tant que professeur avec l'email suivant: <span style='color:blue;font-weight:600;font-size:12px'>"
				+ professeur.getEmail() + "</span></div>" + "</body></html>");

		sendEmail(professeur.getEmail(), (headerHTML + bodyHTML), "Inscription au programme leadership)");
	}

	@Async
	public void sendMailFirstAccesChapitre(Etudiant etudiant, Chapitre chapitre) {
		String headerHTML = ""
				+ "<!DOCTYPE html PUBLIC “-//W3C//DTD XHTML 1.0 Transitional//EN” “https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd”>\r\n"
				+ "<html xmlns=“https://www.w3.org/1999/xhtml”>\r\n" + "<head>\r\n"
				+ "<title>Rapport d'activite</title>\r\n"
				+ "<meta http–equiv=“Content-Type” content=“text/html; charset=UTF-8” />\r\n"
				+ "<meta http–equiv=“X-UA-Compatible” content=“IE=edge” />\r\n"
				+ "<meta name=“viewport” content=“width=device-width, initial-scale=1.0 “ />" + "<style>" + ""
				+ "td{text-align:right;margin-right:25px;}" + "tr{min-height:35px;border-bottom: 5px solid red;}"
				+ "</style>" + "</head>";
		String bodyHTML = ("" + "<body style='min-width:100%;'>" + "<div>"
				+ "<h3 style='color:green'>Vous avait debuter avec le chapitre sur " + chapitre.getTitre() + "</h3>"
				+ "<h3> Ce chapitre vous permettra de .... </h3><br\"><br\">"
				+ "<h5>Continuer dans cette lancer pour terminer le programme et optenir votre attestation<h5>"
				+ "<div>Vous etes inscrit au programme avec l'email suivant: <span style='color:blue;font-weight:600;font-size:12px'>"
				+ etudiant.getEmail() + "</span></div><br\"><br\"><br\">" + getFooterMail() + "</body></html>");

		sendEmail(etudiant.getEmail(), (headerHTML + bodyHTML), "Nouveau chapitre debute.");
	}

	@Async
	public void sendMailFinishAllQroChapitre(Etudiant etudiant, Chapitre chapitre) {

		String masculin = "Masculin";
		String tritreSex = "";
		tritreSex = (etudiant.getInformations() != null && !etudiant.getInformations().isEmpty())
				? masculin.equals(etudiant.getInformations().get(0).getSexe()) ? " Monsieur " : " Madame "
				: "Mr//Mme";

		String headerHTML = ""
				+ "<!DOCTYPE html PUBLIC “-//W3C//DTD XHTML 1.0 Transitional//EN” “https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd”>\r\n"
				+ "<html xmlns=“https://www.w3.org/1999/xhtml”>\r\n" + "<head>\r\n"
				+ "<title>Rapport d'activite</title>\r\n"
				+ "<meta http–equiv=“Content-Type” content=“text/html; charset=UTF-8” />\r\n"
				+ "<meta http–equiv=“X-UA-Compatible” content=“IE=edge” />\r\n"
				+ "<meta name=“viewport” content=“width=device-width, initial-scale=1.0 “ />" + "<style>" + ""
				+ "td{text-align:right;margin-right:25px;}" + "tr{min-height:35px;border-bottom: 5px solid red;}"
				+ "</style>" + "</head>";
		String bodyHTML = ("" + "<body style='min-width:100%;'>" + "<div>" + "<h2>" + tritreSex + " "
				+ etudiant.getNom() + " " + etudiant.getPrenom() + " ,</h2>"
				+ "<p>Félicitations pour avoir terminé le cours !</p>"
				+ "<p>Vous avez complété avec succès toutes les questions du cours <strong>" + chapitre.getTitre()
				+ "</strong>.</p>" + "<br><br>"
				+ "<p>Votre engagement et votre travail acharné sont remarquables. Continuez ainsi !</p>"
				+ "<p>Nous vous invitons à explorer nos autres cours pour approfondir vos compétences.</p>"
				+ getFooterMail() + "</body></html>");

		sendEmail(etudiant.getEmail(), (headerHTML + bodyHTML),
				"Félicitations pour avoir répondu à toutes les questions du cours " + chapitre.getTitre());
	}

	@Async
	public void sendMailValidationQCM(Etudiant etudiant, Chapitre chapitre, FormQcmForValidation formQcmForValidation) {
		String headerHTML = ""
				+ "<!DOCTYPE html PUBLIC “-//W3C//DTD XHTML 1.0 Transitional//EN” “https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd”>\r\n"
				+ "<html xmlns=“https://www.w3.org/1999/xhtml”>\r\n" + "<head>\r\n"
				+ "<title>Rapport d'activite</title>\r\n"
				+ "<meta http–equiv=“Content-Type” content=“text/html; charset=UTF-8” />\r\n"
				+ "<meta http–equiv=“X-UA-Compatible” content=“IE=edge” />\r\n"
				+ "<meta name=“viewport” content=“width=device-width, initial-scale=1.0 “ />" + "<style>" + ""
				+ "td{text-align:right;margin-right:25px;}" + "tr{min-height:35px;border-bottom: 5px solid red;}"
				+ "</style>" + "</head>";
		String bodyHTML = ("" + "<body style='min-width:100%;'>" + "<div>"
				+ "<h2 style='color:orange'> Notes de QCM du  cour  sur " + chapitre.getTitre() + "</h2>"
				+ "<h3> </h3><br\"><br\">" + "<div><span>Note:</span><span style='font-size:18px;color:"
				+ (((formQcmForValidation.getTotalQcm() / 2) >= formQcmForValidation.getQcmValide()) ? "red" : "green")
				+ "'>" + formQcmForValidation.getQcmValide() + " / " + formQcmForValidation.getTotalQcm()
				+ "</span></div>"
				+ (((formQcmForValidation.getTotalQcm() / 2) >= formQcmForValidation.getQcmValide())
						? "<div><span style='color:red;font-size:15px'>Vous avez n'avez pas valider ce chapitre...! Vous pourez de nouveau faire ce QCM dans 24h...!</span></div><br\"><br\">"
						: "<div><span style='color:green;font-size:15px'>Felicitation...! Vous avez valider  cet exercice avec succes, passer au chapitre suivant...</span></div><br\"><br\">")
				+ "<div>Vous etes inscrit au programme avec l'email suivant: <span style='color:blue;font-weight:600;font-size:12px'>"
				+ etudiant.getEmail() + "</span></div><br\"><br>" + getFooterMail() + "</body></html>");

		sendEmail(etudiant.getEmail(), (headerHTML + bodyHTML), "Resultat QCM");
	}

	@Async
	public void sendMailOvertureModule(Etudiant etudiant, Module module) {
		String masculin = "Masculin";
		String tritreSex = "";
		tritreSex = (etudiant.getInformations() != null && !etudiant.getInformations().isEmpty())
				? masculin.equals(etudiant.getInformations().get(0).getSexe()) ? " Monsieur " : " Madame "
				: "Mr//Mme";

		String headerHTML = ""
				+ "<!DOCTYPE html PUBLIC “-//W3C//DTD XHTML 1.0 Transitional//EN” “https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd”>\r\n"
				+ "<html xmlns=“https://www.w3.org/1999/xhtml”>\r\n" + "<head>\r\n"
				+ "<title>Rapport d'activite</title>\r\n"
				+ "<meta http–equiv=“Content-Type” content=“text/html; charset=UTF-8” />\r\n"
				+ "<meta http–equiv=“X-UA-Compatible” content=“IE=edge” />\r\n"
				+ "<meta name=“viewport” content=“width=device-width, initial-scale=1.0 “ />" + "<style>" + ""
				+ "td{text-align:right;margin-right:25px;}" + "tr{min-height:35px;border-bottom: 5px solid red;}"
				+ "</style>" + "</head>";
		String bodyHTML = ("" + "<body style='min-width:100%;'>" + "<div>" + "<h3>" + tritreSex + " "
				+ etudiant.getNom() + " " + etudiant.getPrenom() + "</h3>"
				+ "Bienvenue à la formation en ligne sur la capacitation entrepreneuriale. Vous êtes prêt à parcourir le premier module de cette formation."
				+ "Important : lisez attentivement les contenus, puis répondez à toutes les questions pour accéder au module suivant. "
				+ "NB :<br>" + "<ol>"
				+ "<li>Les  modules sont déverrouillés  chaque semaine selon leur ordre de passage et de mise en ligne</li> "
				+ "<li>Un cours débuté à 3 jours pour aller jusqu’à la réponse aux questions,sinon il reviendra à la note de Zéro au delà de ce délai</li> "
				+ "<li>La condition de notation : moyenne supérieure ou égale à 70 %. Vous avez la possibilité d’améliorer votre moyenne par un autre essai</li> "
				+ "</ol>" + "<br>" + "<span>Cordialement,</span>" + getFooterMail() + "</div></body></html>");

		sendEmail(etudiant.getEmail(), (headerHTML + bodyHTML), "Premier module de formation commencés");
	}

	@Async
	public void sendMailStartChapitre(Etudiant etudiant, Chapitre chapitre) {
		String masculin = "Masculin";
		String tritreSex = "";
		tritreSex = (etudiant.getInformations() != null && !etudiant.getInformations().isEmpty())
				? masculin.equals(etudiant.getInformations().get(0).getSexe()) ? " Monsieur " : " Madame "
				: "Mr//Mme";

		String headerHTML = ""
				+ "<!DOCTYPE html PUBLIC “-//W3C//DTD XHTML 1.0 Transitional//EN” “https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd”>\r\n"
				+ "<html xmlns=“https://www.w3.org/1999/xhtml”>\r\n" + "<head>\r\n"
				+ "<title>Rapport d'activite</title>\r\n"
				+ "<meta http–equiv=“Content-Type” content=“text/html; charset=UTF-8” />\r\n"
				+ "<meta http–equiv=“X-UA-Compatible” content=“IE=edge” />\r\n"
				+ "<meta name=“viewport” content=“width=device-width, initial-scale=1.0 “ />" + "<style>" + ""
				+ "td{text-align:right;margin-right:25px;}" + "tr{min-height:35px;border-bottom: 5px solid red;}"
				+ "</style>" + "</head>";
		String bodyHTML = ("" + "<body style='min-width:100%;'>" + "<div>" + "<h3>" + tritreSex + " "
				+ etudiant.getNom() + " " + etudiant.getPrenom() + "</h3>"
				+ "<span style='font-size:15px'>Félicitations, vous venez de commencer votre nouveau cours :" + "<b>"
				+ chapitre.getTitre() + "</b>" + "</span><br>"
				+ "<span style='font-size:15px'>Prêt à devenir un(e) Leader et Dirigeant(e) de grande Entreprise? </span>"
				+ "<br>" + "<span>Cordialement,</span>" + getFooterMail() + "</div></body></html>");

		sendEmail(etudiant.getEmail(), (headerHTML + bodyHTML), "Nouveau cours debuté #" + chapitre.getIdChapitre());
	}

	@Async
	public void sendMailWithLinkCodeToChangePassword(Etudiant etudiant) {

		String masculin = "Masculin";
		String tritreSex = "";
		tritreSex = (etudiant.getInformations() != null && !etudiant.getInformations().isEmpty())
				? masculin.equals(etudiant.getInformations().get(0).getSexe()) ? " Monsieur " : " Madame "
				: "Mr//Mme";

		String headerHTML = ""
				+ "<!DOCTYPE html PUBLIC “-//W3C//DTD XHTML 1.0 Transitional//EN” “https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd”>\r\n"
				+ "<html xmlns=“https://www.w3.org/1999/xhtml”>\r\n" + "<head>\r\n"
				+ "<title>Rapport d'activite</title>\r\n"
				+ "<meta http–equiv=“Content-Type” content=“text/html; charset=UTF-8” />\r\n"
				+ "<meta http–equiv=“X-UA-Compatible” content=“IE=edge” />\r\n"
				+ "<meta name=“viewport” content=“width=device-width, initial-scale=1.0 “ />" + "<style>" + ""
				+ "td{text-align:right;margin-right:25px;}" + "tr{min-height:35px;border-bottom: 5px solid red;}"
				+ "</style>" + "</head>";
		String bodyHTML = ("" + "<body style='min-width:100%;'>" + "<div>" + "<h2>" + tritreSex + " "
				+ etudiant.getNom() + " " + etudiant.getPrenom() + " ,</h2>"
				+ "<span style='font-size:19px'><b>Pour réinitialiser votre mot de passe,"
				+ "<a style='font-size:22px;color:green' " + "href='https://programmeleadership.org/resetpassword/"
				+ etudiant.getCodeChangePassword() + "'" + "target='_blank' rel='noreferrer'" + ">" + "cliquez ici"
				+ "</a>  </b></span><br><br>"
				+ "<b><span style='font-size:16px'><span style='font-size:17px;color:red'>NB: </span>Si vous n’êtes pas à l’origine de cette demande de réinitialisation de mot de passe, <span style='font-size:17px;color:red'>Ignorez ce mail.</span></span></b>"
				+ "<br><br>" + getFooterMail() + "</div></body></html>");

		sendEmail(etudiant.getEmail(), (headerHTML + bodyHTML), "RÉINITIALISATION DU MOT DE PASSE");

	}

	@Async
	public void sendMailAfterResetPasswordEtudiant(Etudiant etudiant) {

		String masculin = "Masculin";
		String tritreSex = "";
		tritreSex = (etudiant.getInformations() != null && !etudiant.getInformations().isEmpty())
				? masculin.equals(etudiant.getInformations().get(0).getSexe()) ? " Monsieur " : " Madame "
				: "Mr//Mme";

		String headerHTML = ""
				+ "<!DOCTYPE html PUBLIC “-//W3C//DTD XHTML 1.0 Transitional//EN” “https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd”>\r\n"
				+ "<html xmlns=“https://www.w3.org/1999/xhtml”>\r\n" + "<head>\r\n"
				+ "<title>Rapport d'activite</title>\r\n"
				+ "<meta http–equiv=“Content-Type” content=“text/html; charset=UTF-8” />\r\n"
				+ "<meta http–equiv=“X-UA-Compatible” content=“IE=edge” />\r\n"
				+ "<meta name=“viewport” content=“width=device-width, initial-scale=1.0 “ />" + "<style>" + ""
				+ "td{text-align:right;margin-right:25px;}" + "tr{min-height:35px;border-bottom: 5px solid red;}"
				+ "</style>" + "</head>";
		String bodyHTML = ("" + "<body style='min-width:100%;'>" + "<div>" + "<h2>" + tritreSex + " "
				+ etudiant.getNom() + " " + etudiant.getPrenom() + " ,</h2>"
				+ "<span style='font-size:17px'><b>Votre mot de passe a été réinitialisé avec succès. Veuillez le conserver en lieu sûr."
				+ "</b></span><br><br>" + "<br><br>" + getFooterMail() + "</div></body></html>");

		sendEmail(etudiant.getEmail(), (headerHTML + bodyHTML), "RÉUSSITE DE LA RÉINITIALISATION DU MOT DE PASSE”");
	}

	@Async
	public void sendFromFormMailToStutend(List<Etudiant> etudiants, FormMail formMail) {
		for (Etudiant etudiant : etudiants) {
			String masculin = "Masculin";
			String tritreSex = "";
			tritreSex = (etudiant.getInformations() != null && !etudiant.getInformations().isEmpty())
					? masculin.equals(etudiant.getInformations().get(0).getSexe()) ? " Monsieur " : " Madame "
					: "Mr//Mme";

			String headerHTML = ""
					+ "<!DOCTYPE html PUBLIC “-//W3C//DTD XHTML 1.0 Transitional//EN” “https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd”>\r\n"
					+ "<html xmlns=“https://www.w3.org/1999/xhtml”>\r\n" + "<head>\r\n"
					+ "<title>Rapport d'activite</title>\r\n"
					+ "<meta http–equiv=“Content-Type” content=“text/html; charset=UTF-8” />\r\n"
					+ "<meta http–equiv=“X-UA-Compatible” content=“IE=edge” />\r\n"
					+ "<meta name=“viewport” content=“width=device-width, initial-scale=1.0 “ />" + "<style>" + ""
					+ "td{text-align:right;margin-right:25px;}" + "tr{min-height:35px;border-bottom: 5px solid red;}"
					+ "</style>" + "</head>";
			String bodyHTML = ("" + "<body style='min-width:100%;'>" + "<div>" + "<h2>" + tritreSex + " "
					+ etudiant.getNom() + " " + etudiant.getPrenom() + " ,</h2>" + formMail.getBodyHtml() + "<br><br>"
					+ getFooterMail() + "</div></body></html>");

			sendEmail(etudiant.getEmail(), (headerHTML + bodyHTML), formMail.getObjet());

		}
	};

	@Async
	public void sendMailToStutend(Etudiant etudiant, FormMail formMail) {

		String masculin = "Masculin";
		/*
		 * String tritreSex = ""; tritreSex = (etudiant.getInformations() != null &&
		 * !etudiant.getInformations().isEmpty()) ?
		 * masculin.equals(etudiant.getInformations().get(0).getSexe()) ? " Monsieur " :
		 * " Madame " : "Mr//Mme";
		 */

		String headerHTML = ""
				+ "<!DOCTYPE html PUBLIC “-//W3C//DTD XHTML 1.0 Transitional//EN” “https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd”>\r\n"
				+ "<html xmlns=“https://www.w3.org/1999/xhtml”>\r\n" + "<head>\r\n"
				+ "<title>Rapport d'activite</title>\r\n"
				+ "<meta http–equiv=“Content-Type” content=“text/html; charset=UTF-8” />\r\n"
				+ "<meta http–equiv=“X-UA-Compatible” content=“IE=edge” />\r\n"
				+ "<meta name=“viewport” content=“width=device-width, initial-scale=1.0 “ />" + "<style>" + ""
				+ "td{text-align:right;margin-right:25px;}" + "tr{min-height:35px;border-bottom: 5px solid red;}"
				+ "</style>" + "</head>";
		String bodyHTML = ("" + "<body style='min-width:100%;'>" + "<div>" + formMail.getBodyHtml() + "<br><br>"
				+ getFooterMail() + "</div></body></html>");

		sendEmail(etudiant.getEmail(), (headerHTML + bodyHTML), formMail.getObjet());
	};

	@Async
	public void sendMailToStutend(Etudiant etudiant, FormMail formMail, String filePath) {

		// String masculin = "Masculin";
		/*
		 * String tritreSex = ""; tritreSex = (etudiant.getInformations() != null &&
		 * !etudiant.getInformations().isEmpty()) ?
		 * masculin.equals(etudiant.getInformations().get(0).getSexe()) ? " Monsieur " :
		 * " Madame " : "Mr//Mme";
		 */

		String headerHTML = ""
				+ "<!DOCTYPE html PUBLIC “-//W3C//DTD XHTML 1.0 Transitional//EN” “https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd”>\r\n"
				+ "<html xmlns=“https://www.w3.org/1999/xhtml”>\r\n" + "<head>\r\n"
				+ "<title>Rapport d'activite</title>\r\n"
				+ "<meta http–equiv=“Content-Type” content=“text/html; charset=UTF-8” />\r\n"
				+ "<meta http–equiv=“X-UA-Compatible” content=“IE=edge” />\r\n"
				+ "<meta name=“viewport” content=“width=device-width, initial-scale=1.0 “ />" + "<style>" + ""
				+ "td{text-align:right;margin-right:25px;}" + "tr{min-height:35px;border-bottom: 5px solid red;}"
				+ "</style>" + "</head>";
		String bodyHTML = ("" + "<body style='min-width:100%;'>" + "<div>" + formMail.getBodyHtml() + "<br><br>"
				+ getFooterMail() + "</div></body></html>");

		sendEmail(etudiant.getEmail(), (headerHTML + bodyHTML), formMail.getObjet(), true, new String[] { filePath });
	};

	@Async
	public void sendMailConfirmCanditatureSalon(Etudiant etudiant) {

		String texte = "Nous vous remercions d'avoir <b>soumis votre candidature pour présenter votre projet/entreprise au Salon de l'Entrepreneuriat.</b>"
				+ "<br><br>"
				+ "Nous avons bien reçu l'ensemble des informations que vous avez fournies, y compris la <b>vidéo de présentation de votre prototype ou de votre entreprise</b>. Soyez assuré(e) que toutes vos données ont été correctement enregistrées et seront examinées avec attention."
				+ "<br><br>"
				+ "<b>Notre équipe prendra contact avec vous dans les prochains jours pour vous informer de la suite du processus de sélection.</b> Si votre candidature est retenue, nous vous fournirons tous les détails nécessaires à votre participation."
				+ "<br><br>"
				+ "Nous vous remercions pour l'intérêt que vous portez à cet événement et nous vous souhaitons bonne chance dans la poursuite de votre projet entrepreneurial."
				+ "<br><br>" + "<b>Cordialement,<br>" + "L'équipe du programme Leadership</b>" + "<br><br>"
				+ "<div style=\"width:100%;height:5px;border:2px solid black\"></div>" + "<br>"
				+ "Thank you for <b>submitting your application to present your project/company at the Entrepreneurship Fair.</b>\n"
				+ "<br><br>"
				+ "We have received all the information you provided, including the <b>video presentation of your prototype or company</b>. Rest assured that all your data has been correctly recorded and will be carefully examined."
				+ "<br><br>"
				+ "<b>Our team will contact you in the coming days to inform you of the rest of the selection process.</b> If your application is successful, we will provide you with all the necessary details for your participation."
				+ "<br><br>"
				+ "We thank you for your interest in this event and we wish you good luck in pursuing your entrepreneurial project."
				+ "<br><br>" + "<b>Sincerely,<br>" + "The Leadership Program Team<b>";

		String masculin = "Masculin";
		String tritreSex = "";
		tritreSex = (etudiant.getInformations() != null && !etudiant.getInformations().isEmpty())
				? masculin.equals(etudiant.getInformations().get(0).getSexe()) ? " Monsieur " : " Madame "
				: "Mr//Mme";

		String headerHTML = ""
				+ "<!DOCTYPE html PUBLIC “-//W3C//DTD XHTML 1.0 Transitional//EN” “https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd”>\r\n"
				+ "<html xmlns=“https://www.w3.org/1999/xhtml”>\r\n" + "<head>\r\n"
				+ "<title>Rapport d'activite</title>\r\n"
				+ "<meta http–equiv=“Content-Type” content=“text/html; charset=UTF-8” />\r\n"
				+ "<meta http–equiv=“X-UA-Compatible” content=“IE=edge” />\r\n"
				+ "<meta name=“viewport” content=“width=device-width, initial-scale=1.0 “ />" + "<style>" + ""
				+ "td{text-align:right;margin-right:25px;}" + "tr{min-height:35px;border-bottom: 5px solid red;}"
				+ "</style>" + "</head>";
		String bodyHTML = ("" + "<body style='min-width:100%;'>" + "<div>" + "<h2>" + tritreSex + " "
				+ etudiant.getNom() + " " + etudiant.getPrenom() + " ,</h2>" + texte + "<br><br>" + getFooterMail()
				+ "</div></body></html>");

		sendEmail(etudiant.getEmail(), (headerHTML + bodyHTML),
				"Confirmation de votre candidature au Salon de l'Entrepreneuriat");

	};

	public void name() {

	}

	private String getFooterMail() {
		return "<br><br>" + "<span style='font-size:19px'><b>Le Programme LEADERSHIP </b></span><br>"
				+ "<span style='font-size:19px'><b>Institut de Recherche en Intelligences  </b></span><br>"
				+ "<span style='font-size:17px'><b> Tel: +237 695 83 58 77 / +237 672 64 33 53  </b></span><br>"
				+ "<span style='font-size:17px'><b>Pour nous ecrire sur whatsApp "
				+ "<a style='font-size:19px;color:green' " + "href='https://chat.whatsapp.com/HZvUzVRNNHFFZNyden9NEi'"
				+ "target='_blank' rel='noreferrer'" + ">" + "cliquez ici" + "</a>  </b></span><br><br>"
				+ "<div style='width:100%;text-align: center;'>"
				+ "<img style='max-width:100%;height: auto;' src='https://programmeleadership.org/images/logos02.png' alt='Logo'>"
				+ "<div>";
	}

	private void sendEmail(String email, String bodyMessage, String subject) {
		sendEmail(email, bodyMessage, subject, true, null);
	}

	@Async
	private void sendEmail(String email, String bodyMessage, String subject, Boolean printStackTraceIferror,
			String[] filePaths) {
		// use mailSender here...
		// String bodyMessage=getEmailBody();
		// String from = "streengeapp@gmail.com";
		String from = "Programme Leadership";
		String to = email.trim();

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
		// String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
		Pattern pattern = Pattern.compile(emailRegex);
		Matcher matcher = pattern.matcher(email);
		boolean isValideEmail = matcher.matches();

		try {
			// helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(bodyMessage, true);
			message.setFrom("Programme Leadership <NePasRepondre_noReply@programmeleadership.net>");

			// helper.setCc("archive@programmeleadership.net");
			// helper.setText(bodyMessage);

			if (filePaths != null) {
				for (String filePath : filePaths) {
					File file = new File(filePath);
					if (file.exists()) {
						helper.addAttachment(file.getName(), file);
					} else {
						System.err.println("File not found: " + filePath);
					}
				}
			}
			if (isValideEmail) {
				mailSender.send(message);
				sendGridMail.sendMailBySendGrid(email, bodyMessage, subject,
				printStackTraceIferror,filePaths); // a utiliser pour lorsque l'on souhaite
				// dedier l'envoi des mail a un service externe comme sendGrid, pour cella il
				// faut un prendre un abonement sur a sendGrid, l'ancien abonnement est terminer
				// depuis, ceci permet d'envoyer plus de 1000 mails de facon simultaner
			}
			// System.out.println("Le mail a ete envoyer");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// if (printStackTraceIferror) {
			e.printStackTrace();
			// }
			// System.out.println(" ERREUR ERREUR ERREUR echec de l'envoi du mail...!");
		}
	}
}
