package iri.elearningapi.utils.elearningFunction;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import iri.elearningapi.model.courModel.Chapitre;
import iri.elearningapi.model.userModel.Etudiant;
import iri.elearningapi.utils.form.formInt.FormMail;

@Service
public class AttestationGenerator {
	
	@Value("${document.upload.certificats.dir}")
	private  String certificatsDir;
	
	
	private SendGridMail sendGridMail = new SendGridMail();
	
	public   void sendAttestationToStudentByChapitre(Etudiant etudiant,Chapitre chapitre) {
		String fillePath=generateAndSaveAttestation(etudiant.getNom(), etudiant.getMatricule(),chapitre.getTitre(),"Famille d'entrepreneur");
		
		//String bodyHTML="<div>Voici votre certification</div>";
		FormMail formMail=new FormMail();
		formMail.setObjet("Test envoi de certifaction via piece jointe");
		formMail.setBodyHtml("voici votre certification pour le chapitre: "+chapitre.getTitre());
		try {
			sendGridMail.sendMailBySendGrid(etudiant.getEmail(), formMail.getBodyHtml(), formMail.getObjet(), true,new String[] {fillePath} );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    public  String generateAndSaveAttestation(String studentName,String matricule, String courseTitle,String profilApprenant) {
        PdfReader pdfReader;
        System.out.println("_01");
        System.out.println("Debut du processus de creation de l'attestation de: "+studentName);
        
        Date toDay = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);
        String formattedDate = formatter.format(toDay);

        try {
            // Charger le modèle PDF
            pdfReader = new PdfReader(certificatsDir+"/model_attestation.pdf");
            // Définir le nom du fichier de sortie avec le nom de l'étudiant
            String outputFilePath = certificatsDir+"/" + (studentName.replaceAll(" ", "_")+"_"+courseTitle.replace(" ","_")).replaceAll("[:\\\\/*?|<>]", "-") + "_attestation.pdf";
            PdfWriter pdfWriter = new PdfWriter(outputFilePath);
            PdfDocument pdfDoc = new PdfDocument(pdfReader, pdfWriter);

            // Charger le formulaire et remplir les champs
            PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
            Map<String, PdfFormField> fields = form.getAllFormFields();
            
         // Afficher les noms des champs
           /* System.out.println("Liste des champs de formulaire dans le PDF :");
            for (String fieldName : fields.keySet()) {
                System.out.println("Nom du champ : " + fieldName);
            }*/

            // Remplacer les champs par les valeurs dynamiques
            if (fields.containsKey("studentName")) {
                fields.get("studentName").setValue(studentName.toUpperCase());
               // PdfTextFormField  studentNameField = (PdfTextFormField) fields.get("studentName");
      
            } else {
                System.out.println("Le champ 'Text1' est introuvable dans le PDF.");
            }

            if (fields.containsKey("courseTitle")) {
                fields.get("courseTitle").setValue(Methode.capitalizeFirstLetter(courseTitle));
            } else {
                System.out.println("Le champ 'courseTitle Text1' est introuvable dans le PDF.");
            }
            
            if (fields.containsKey("matricule")) {
                fields.get("matricule").setValue("ID°:"+matricule).setFontSize(10);
            } else {
                System.out.println("Le champ 'matricule' est introuvable dans le PDF.");
            }
            
            if (fields.containsKey("texte01")) {
                fields.get("texte01").setValue("pour sa brillante capacitation en");
            } else {
                System.out.println("Le champ 'matricule' est introuvable dans le PDF.");
            }

            if (fields.containsKey("date")) {
                fields.get("date").setValue(formattedDate).setFontSize(10);
            } else {
                System.out.println("Le champ 'date' est introuvable dans le PDF.");
            }

            form.flattenFields(); // Optionnel: rendre le PDF non modifiable après génération
            pdfDoc.close();

            System.out.println("Attestation générée et sauvegardée avec succès à : " + outputFilePath);
            return outputFilePath  ;

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur: Le chemin du fichier d'attestation est introuvable ou invalide.");
        }
		return null;
    }
}
