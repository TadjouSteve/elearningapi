package iri.elearningapi .utils.elearningFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import iri.elearningapi.model.courModel.Chapitre;
import iri.elearningapi.model.userModel.Etudiant;
import iri.elearningapi.model.userModel.Professeur;
import iri.elearningapi.utils.form.formInt.FormQcmForValidation;
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class SenderMail {
	@Autowired
	private  JavaMailSender mailSender;
	
	@Async
	public  void sendMailAfterRegistration(Etudiant etudiant) {
		String headerHTML = ""
				+ "<!DOCTYPE html PUBLIC “-//W3C//DTD XHTML 1.0 Transitional//EN” “https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd”>\r\n"
				+ "<html xmlns=“https://www.w3.org/1999/xhtml”>\r\n" + "<head>\r\n"
				+ "<title>inscription valide</title>\r\n"
				+ "<meta http–equiv=“Content-Type” content=“text/html; charset=UTF-8” />\r\n"
				+ "<meta http–equiv=“X-UA-Compatible” content=“IE=edge” />\r\n"
				+ "<meta name=“viewport” content=“width=device-width, initial-scale=1.0 “ />" + "<style>" + ""
				+ "td{text-align:right;margin-right:25px;}" + "tr{min-height:35px;border-bottom: 5px solid red;}"
				+ "</style>" + "</head>";
		String bodyHTML = ("" + "<body style='min-width:100%;'>" + "<div>" + "<h2 style='color:blue'>Validation de votre Enregistement au programme de formation de 1000 entrepreuneur jeune...!</h2>"
					+"<h3> Mr/Mme "+etudiant.getNom()+" "+etudiant.getPrenom()+"</h3><br\"><br\">"
					+"<h5>Bienvenu au programme de formation organiser par le Groupe IRI (Institut de Recherche en Intelligence) en partenariat avec le MINJEC<h5>"
					+"<div>Vous etes inscrit au programme avec l'email suivant: <span style='color:blue;font-weight:600;font-size:12px'>"+etudiant.getEmail()+"</span></div>"
					+"</body></html>"
				);
		
		sendEmail(etudiant.getEmail() , (headerHTML+bodyHTML), "Confirmation d'inscription (IRI)");
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
		String bodyHTML = ("" + "<body style='min-width:100%;'>" + "<div>" + "<h2 style='color:blue'>Vous avez été enregistré sur la plateforme en ligne du programme Leadership en tant que professeur.</h2>"
					+"<h3> Mr/Mme "+professeur.getNom()+" "+professeur.getPrenom()+"</h3><br\"><br\">"
					+"<h4>Information de connexion</h4>"
					+"<span style='font-weight:600'>Email :<span style='font-weight:700;font-size:12px'>"+professeur.getEmail()+"</span></span><br\">"
					+"<span style='font-weight:600'>Téléphone :<span style='font-weight:700;font-size:12px'>"+professeur.getTelephone()+"</span></span><br\">"
					+"<span style='font-weight:600'>Mot de passe :<span style='font-weight:700;font-size:12px'>"+professeur.getPassword()+"</span></span><br\">"
					+"<h5>Bienvenu au programme de formation organiser par le Groupe IRI (Institut de Recherche en Intelligence) en partenariat avec le MINJEC<h5>"
					+"<div>Vous etes inscrit au programme en tant que professeur avec l'email suivant: <span style='color:blue;font-weight:600;font-size:12px'>"+professeur.getEmail()+"</span></div>"
					+"</body></html>"
				);
		
		sendEmail(professeur.getEmail() , (headerHTML+bodyHTML), "Inscription au programme leadership)");
	}
	
	@Async
	public void sendMailFirstAccesChapitre(Etudiant etudiant,Chapitre chapitre) {
		String headerHTML = ""
				+ "<!DOCTYPE html PUBLIC “-//W3C//DTD XHTML 1.0 Transitional//EN” “https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd”>\r\n"
				+ "<html xmlns=“https://www.w3.org/1999/xhtml”>\r\n" + "<head>\r\n"
				+ "<title>Rapport d'activite</title>\r\n"
				+ "<meta http–equiv=“Content-Type” content=“text/html; charset=UTF-8” />\r\n"
				+ "<meta http–equiv=“X-UA-Compatible” content=“IE=edge” />\r\n"
				+ "<meta name=“viewport” content=“width=device-width, initial-scale=1.0 “ />" + "<style>" + ""
				+ "td{text-align:right;margin-right:25px;}" + "tr{min-height:35px;border-bottom: 5px solid red;}"
				+ "</style>" + "</head>";
		String bodyHTML = ("" + "<body style='min-width:100%;'>" + "<div>" + "<h3 style='color:green'>Vous avait debuter avec le chapitre sur "+chapitre.getTitre()+"</h3>"
					+"<h3> Ce chapitre vous permettra de .... </h3><br\"><br\">"
					+"<h5>Continuer dans cette lancer pour terminer le programme et optenir votre attestation<h5>"
					+"<div>Vous etes inscrit au programme avec l'email suivant: <span style='color:blue;font-weight:600;font-size:12px'>"+etudiant.getEmail()+"</span></div><br\"><br\"><br\">"
					+"<div><span style='color:blue;font-weight:600;font-size:12px'> powered by IRI (Institut de Recherche en Intelligence)<span></div>"
					+"</body></html>"
				);
		
		sendEmail(etudiant.getEmail() , (headerHTML+bodyHTML), "Nouveau chapitre debute.");
	}
	
	@Async
	public void sendMailValidationQCM(Etudiant etudiant,Chapitre chapitre,FormQcmForValidation formQcmForValidation) {
		String headerHTML = ""
				+ "<!DOCTYPE html PUBLIC “-//W3C//DTD XHTML 1.0 Transitional//EN” “https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd”>\r\n"
				+ "<html xmlns=“https://www.w3.org/1999/xhtml”>\r\n" + "<head>\r\n"
				+ "<title>Rapport d'activite</title>\r\n"
				+ "<meta http–equiv=“Content-Type” content=“text/html; charset=UTF-8” />\r\n"
				+ "<meta http–equiv=“X-UA-Compatible” content=“IE=edge” />\r\n"
				+ "<meta name=“viewport” content=“width=device-width, initial-scale=1.0 “ />" + "<style>" + ""
				+ "td{text-align:right;margin-right:25px;}" + "tr{min-height:35px;border-bottom: 5px solid red;}"
				+ "</style>" + "</head>";
		String bodyHTML = ("" + "<body style='min-width:100%;'>" + "<div>" + "<h2 style='color:orange'> Notes de QCM du  cour  sur "+chapitre.getTitre()+"</h2>"
					+"<h3> </h3><br\"><br\">"
					+"<div><span>Note:</span><span style='font-size:18px;color:"+(((formQcmForValidation.getTotalQcm()/2)>=formQcmForValidation.getQcmValide())?"red":"green")+"'>"+formQcmForValidation.getQcmValide()+" / " +formQcmForValidation.getTotalQcm()+"</span></div>"
					+(((formQcmForValidation.getTotalQcm()/2)>=formQcmForValidation.getQcmValide())?
							"<div><span style='color:red;font-size:15px'>Vous avez echouer...! Vous pourez de nouveau faire ce QCM dans 24h...!</span></div><br\"><br\">"
							:
							"<div><span style='color:green;font-size:15px'>Felicitation...! Vous avez valider  cet exercice avec succes, passer au chapitre suivant...</span></div><br\"><br\">")
					+"<div>Vous etes inscrit au programme avec l'email suivant: <span style='color:blue;font-weight:600;font-size:12px'>"+etudiant.getEmail()+"</span></div><br\"><br\"><br\">"
					+"<div><span style='color:blue;font-weight:600;font-size:12px'> powered by IRI (Institut de Recherche en Intelligence)<span></div>"
					+"</body></html>"
					);
		
		sendEmail(etudiant.getEmail() , (headerHTML+bodyHTML), "Resultat QCM");
	}
	
	
	
	
	private  void sendEmail(String email, String bodyMessage, String subject) {
		// use mailSender here...
		// String bodyMessage=getEmailBody();
		String from = "streengeapp@gmail.com";
		String to = email.trim();

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(bodyMessage, true);
			mailSender.send(message);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
