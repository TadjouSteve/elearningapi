package iri.elearningapi.utils.elearningFunction;

import java.io.IOException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

@Service
public class SendGridMail {

	public String sendMailBySendGrid(String email, String bodyMessage, String subject, Boolean printStackTraceIferror)
			throws IOException {
		return sendMailBySendGrid(email, bodyMessage, subject, printStackTraceIferror, null);
	}

	public String sendMailBySendGrid(String email, String bodyMessage, String subject, Boolean printStackTraceIferror,
			String[] filePaths) throws IOException {
		String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
		Pattern pattern = Pattern.compile(emailRegex);
		Matcher matcher = pattern.matcher(email);
		boolean isValideEmail = matcher.matches();
		Email from = new Email("NePasRepondre_noReply@programmeleadership.net", "Programme Leadership");
		// String subject = "Invitation à la Conférence de Presse";
		Email to = new Email(email);
		Content content = new Content("text/html", bodyMessage);
		Mail mail = new Mail(from, subject, to, content);
		// Ajout des pièces jointes si fournies
		if (filePaths != null) {
			for (String filePath : filePaths) {
				if (filePath != null && !filePath.isEmpty()) {
					Attachments attachment = new Attachments();
					// Convertir le fichier en base64
					byte[] fileData = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(filePath));
					String encodedFile = Base64.getEncoder().encodeToString(fileData);
					attachment.setContent(encodedFile);
					attachment.setType("application/octet-stream"); // Vous pouvez définir le type MIME ici
					attachment.setFilename(java.nio.file.Paths.get(filePath).getFileName().toString());
					attachment.setDisposition("attachment");
					mail.addAttachments(attachment);
				}
			}
		}
		SendGrid sg = new SendGrid("API_KEY_twilioAPI");// il faut prendre un abonnement sur twiloSendrid et obtenir une cle d'api
												// pour utiliser leur service, cella permet d'envoyer de faonc sure
												// plusieurs centainne de mail par jour, ce qui est presque impossible
												// en utilisant simplement le VPS comme serveur de messagerie
		Request request = new Request();
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sg.api(request);
			// System.out.println("Email: "+email);
			// System.out.println(response.getStatusCode());
			// .out.println(response.getBody());
			// System.out.println(response.getHeaders());
			return "is ok.";
		} catch (IOException ex) {
			throw ex;
		}
	}
}
