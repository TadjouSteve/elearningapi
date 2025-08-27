package com.streenge.service.scheduledService;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.streenge.model.admin.Entreprise;
import com.streenge.model.admin.Utilisateur;
import com.streenge.model.depense.FicheDepense;
import com.streenge.model.depense.LigneDepense;
import com.streenge.model.depense.PointDepense;
import com.streenge.model.produit.Pack;
import com.streenge.model.produit.Produit;
import com.streenge.model.vente.facture.Facture;
import com.streenge.model.vente.facture.LigneFacture;
import com.streenge.repository.adminRepository.EntrepriseRepository;
import com.streenge.repository.adminRepository.UtilisateurRepository;
import com.streenge.repository.depense.FicheDepenseRepository;
import com.streenge.repository.venteRepo.factureRepo.FactureRepository;
import com.streenge.repository.venteRepo.factureRepo.LigneFactureRepository;
import com.streenge.service.utils.formInt.LinePanier;
import com.streenge.service.utils.streengeData.Etat;
import com.streenge.service.utils.streengeData.Statut;
import com.streenge.service.utils.streengeFunction.Methode;

@Service
public class TacheAutomatiser {
	@Autowired
	private FactureRepository factureRepository;

	@Autowired
	private EntrepriseRepository entrepriseRepository;

	@Autowired
	private LigneFactureRepository ligneFactureRepository;

	@Autowired
	private UtilisateurRepository utilisateurRepository;

	@Autowired
	private FicheDepenseRepository ficheDepenseRepository;

	@Autowired
	private JavaMailSender mailSender;
	// private static final Logger log =
	// LoggerFactory.getLogger(TacheAutomatiser.class);

	private static final SimpleDateFormat	dateFormat				= new SimpleDateFormat("HH:mm:ss");
	SimpleDateFormat						formater				= new SimpleDateFormat("dd MMMM yyyy");
	SimpleDateFormat						formaterMonthAndYear	= new SimpleDateFormat("MMMM yyyy");

	// @SuppressWarnings({ "deprecation" })
	@Scheduled(fixedRate = (1000 * 60 * 60))
	public void reportCurrentTime() {
		Entreprise entreprise = new Entreprise();
		if (!((List<Entreprise>) entrepriseRepository.findAll()).isEmpty()) {
			entreprise = (((List<Entreprise>) entrepriseRepository.findAll()).get(0));
		}
		// log.info("The time is now {}", dateFormat.format(new Date()));
		Date date = new Date();
		// Calendar calendar = Calendar.getInstance();
		// calendar.setTime(date);
		System.out.println("The time is now {}" + dateFormat.format(new Date()) + "-- this day: "
				+ formater.format(new Date()) + "--- day before : " + formater.format(Methode.getDateDayBefore(1))
				+ "--|| heure:" + date.getHours());

		if (date.getHours() == 6) {
			String bodyMessage = getEmailBody(false);
			String bodyMessageMonth = null;

			if (date.getDate() == 1) {
				bodyMessageMonth = getEmailBody(true);
			}

			for (Utilisateur utilisateur : utilisateurRepository.findAll()) {
				if (Methode.userLevel(utilisateur) >= 3 && utilisateur.getEmail() != null
						&& !Statut.getBloquer().equals(utilisateur.getStatut())
						&& !((entreprise.getDateLimite() != null && entreprise.getDateLimite().before(new Date())))
						&& !Statut.getBloquer().equals(entreprise.getEtat())) {
					sendEmail(utilisateur.getEmail(), bodyMessage, false);
					if (bodyMessageMonth != null) {
						sendEmail(utilisateur.getEmail(), bodyMessageMonth, true);
					}
				}
			}
		}

	}

	public void sendEmail(String email, String bodyMessage, boolean isMonth) {
		// use mailSender here...
		// String bodyMessage=getEmailBody();
		String from = "streengeapp@gmail.com";
		String to = email.trim();

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject("Streenge: Rapport " + (isMonth ? "mensuel" : "jounalier:"));
			helper.setText(bodyMessage, true);
			mailSender.send(message);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getEmailBody(boolean isMonth) {
		DecimalFormat df = new DecimalFormat("###,###,###,###.##");
		// SimpleDateFormat formater = new SimpleDateFormat("dd MMMM yyyy");
		// Date toDay = new Date();
		int NombreDeFacture = 0;
		int factureAnnule = 0;
		int factureNonPaye = 0;
		int faturePartiellementPAyer = 0;
		int facturePaye = 0;

		List<LinePanier> lines = new ArrayList<>();

		List<Facture> factures = factureRepository.findAllByDateCreationBetween(
				isMonth ? Methode.getDateMonthBefore(1) : Methode.getDateDayBefore(1), Methode.getDateDayBefore(0));

		for (Facture facture : factures) {
			if (facture.getEtat().equals(Etat.getActif())) {
				NombreDeFacture++;
				if (Statut.getPaye().equals(facture.getStatut())) {
					facturePaye++;
				} else if (Statut.getNonpaye().equals(facture.getStatut())) {
					factureNonPaye++;
				} else if (Statut.getPartiellementpaye().equals(facture.getStatut())) {
					faturePartiellementPAyer++;
				} else if (Statut.getAnnuler().equals(facture.getStatut())) {
					factureAnnule++;
				}

				if (!Statut.getAnnuler().equals(facture.getStatut())) {
					for (LigneFacture ligneFacture : ligneFactureRepository
							.findByFactureIdFactureOrderById(facture.getIdFacture())) {
						boolean isAlreadyAdd = false;
						float total = Methode.getTotalLigneDocument(ligneFacture);
						float quantite = ligneFacture.getQuantite() * ligneFacture.getPack().getNombreUnite();
						Produit produit = ligneFacture.getPack().getProduit();

						for (int i = 0; i < lines.size(); i++) {
							if (lines.get(i).getIdProduit().equals(produit.getIdProduit())) {
								isAlreadyAdd = true;
								lines.get(i).setPrix(lines.get(i).getPrix() + total);
								lines.get(i).setQuantite(lines.get(i).getQuantite() + quantite);
							}
						}
						if (!isAlreadyAdd) {
							LinePanier newLine = new LinePanier();
							newLine.setIdProduit(produit.getIdProduit());
							newLine.setPrix(total);
							newLine.setQuantite(quantite);
							newLine.setSku(produit.getSku());
							newLine.setDiviseur(1);
							newLine.setDesignation(produit.getNom()
									+ ((produit.getNomUnite() != null && !produit.getNomUnite().isBlank())
											? (" - " + produit.getNomUnite())
											: ""));

							for (Pack pack : produit.getPacks()) {
								if ((Statut.getPackprincipal().equals(pack.getStatut()))
										&& (pack.getNombreUnite() != 0)) {
									newLine.setDiviseur(pack.getNombreUnite());
									newLine.setDesignation(produit.getNom() + " - " + pack.getNom());
								}
							}
							lines.add(newLine);
						}
					}
				}
			}
		}

		String ligneTableau = "";
		float totalVendu = 0;
		for (int i = 0; i < lines.size(); i++) {
			lines.get(i).setQuantite(lines.get(i).getQuantite() / lines.get(i).getDiviseur());
			totalVendu += lines.get(i).getPrix();
			String bgColor = (((i % 2) == 0) ? "#41e1e5" : "#e1f3f4");

			ligneTableau = ligneTableau + "" + "<tr style='font-size:12px;background-color:" + bgColor + "'>"
					+ "<td  style='text-align:left' border='0'>" + lines.get(i).getDesignation() + "</td>"
					+ "<td style='min-height:45px;text-align:right;' border='0'>"
					+ df.format(lines.get(i).getQuantite()) + "</td>" + "<td border='0' style='text-align:right'>"
					+ df.format(lines.get(i).getPrix()) + "</td>" + "</tr>";
		}
		// FFA500
		String endLinge = "" + "<tr style='font-size:12px;background-color:#FFA500'>"
				+ "<td  style='text-align:left' border='0'>TOTAL </td>" + "<td style='' border='0'></td>"
				+ "<td border='0' style='text-align:right'>" + df.format(totalVendu) + "</td>" + "</tr>";

		ligneTableau = ligneTableau + endLinge;

		Entreprise entreprise = new Entreprise();
		if (!((List<Entreprise>) entrepriseRepository.findAll()).isEmpty()) {
			entreprise = (((List<Entreprise>) entrepriseRepository.findAll()).get(0));
		}

		int NombreFicheDepense = 0;
		List<LinePanier> linesDepenses = new ArrayList<>();
		List<FicheDepense> ficheDepenses = ficheDepenseRepository.findAllByDateBetween(
				isMonth ? Methode.getDateMonthBefore(1) : Methode.getDateDayBefore(1), Methode.getDateDayBefore(0));

		for (FicheDepense ficheDepense : ficheDepenses) {
			if (!Statut.getAnnuler().equals(ficheDepense.getStatut())) {
				NombreFicheDepense++;
				for (LigneDepense ligneDepense : ficheDepense.getLigneDepenses()) {
					boolean isAlreadyAdd = false;
					float total = ligneDepense.getMontant();
					PointDepense pointDepense = ligneDepense.getPointDepense();

					for (int i = 0; i < linesDepenses.size(); i++) {
						if (linesDepenses.get(i).getIdPointDepense() == pointDepense.getId()) {
							isAlreadyAdd = true;
							linesDepenses.get(i).setPrix(linesDepenses.get(i).getPrix() + total);
							linesDepenses.get(i).setQuantite(linesDepenses.get(i).getQuantite() + 1);
						}
					}

					if (!isAlreadyAdd) {
						LinePanier newLine = new LinePanier();
						newLine.setIdPointDepense(pointDepense.getId());
						newLine.setPrix(total);
						newLine.setQuantite(1);
						newLine.setSku(pointDepense.getReference());
						newLine.setDiviseur(1);
						newLine.setDesignation(pointDepense.getNom());
						linesDepenses.add(newLine);
					}
				}
			}
		}

		String ligneTableauDepense = "";
		float totalDepense = 0;
		for (int i = 0; i < linesDepenses.size(); i++) {
			linesDepenses.get(i).setQuantite(linesDepenses.get(i).getQuantite() / linesDepenses.get(i).getDiviseur());
			totalDepense += linesDepenses.get(i).getPrix();
			String bgColor = (((i % 2) == 0) ? "#fca2f4" : "#ffe8fd");

			ligneTableauDepense = ligneTableauDepense + "" + "<tr style='font-size:12px;background-color:" + bgColor
					+ "'>" + "<td  style='text-align:left' border='0'>" + linesDepenses.get(i).getDesignation()
					+ "</td>" + "<td style='min-height:45px;text-align:right;' border='0'>"
					+ df.format(linesDepenses.get(i).getQuantite()) + "</td>"
					+ "<td border='0' style='text-align:right'>" + df.format(linesDepenses.get(i).getPrix()) + "</td>"
					+ "</tr>";
		}

		String enLinge2 = "" + "<tr style='font-size:12px;background-color:#FFA500'>"
				+ "<td  style='text-align:left' border='0'>TOTAL </td>" + "<td style='' border='0'></td>"
				+ "<td border='0'>" + df.format(totalDepense) + "</td>" + "</tr>";

		ligneTableauDepense = ligneTableauDepense + enLinge2;

		String headerHTML = ""
				+ "<!DOCTYPE html PUBLIC “-//W3C//DTD XHTML 1.0 Transitional//EN” “https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd”>\r\n"
				+ "<html xmlns=“https://www.w3.org/1999/xhtml”>\r\n" + "<head>\r\n"
				+ "<title>Rapport d'activite</title>\r\n"
				+ "<meta http–equiv=“Content-Type” content=“text/html; charset=UTF-8” />\r\n"
				+ "<meta http–equiv=“X-UA-Compatible” content=“IE=edge” />\r\n"
				+ "<meta name=“viewport” content=“width=device-width, initial-scale=1.0 “ />" + "<style>" + ""
				+ "td{text-align:right;margin-right:25px;}" + "tr{min-height:35px;border-bottom: 5px solid red;}"
				+ "</style>" + "</head>";

		String bodyHTML = ("" + "<body style='min-width:100%;'>" + "<div>" + "<h2>" + entreprise.getNom() + "</h2>"
				+ "<h3>Rapport d'activite " + (isMonth ? "de: " : "du: ")
				+ (isMonth ? formaterMonthAndYear.format(Methode.getDateMonthBefore(1))
						: formater.format(Methode.getDateDayBefore(1)))
				+ "</h3>" + "<br\"><br\">" + "<div>"
				+ "<span style='margin-top:5px;font-weight:600;font-size:12px;color:blue'>Nombre de facture: "
				+ NombreDeFacture + "</span><br/>"
				+ "<span style='margin-top:10px;font-weight:600;font-size:12px;color:green'>Facture payé: "
				+ facturePaye + "</span><br/>"
				+ "<span style='margin-top:10px;font-weight:600;font-size:12px;color:orange'>Facture partiellement payé: "
				+ faturePartiellementPAyer + "</span><br/>"
				+ "<span style='margin-top:10px;font-weight:600;font-size:12px;color:orange'>Facture non payé: "
				+ factureNonPaye + "</span><br/>"
				+ "<span style='margin-top:10px;font-weight:600;font-size:12px;color:red'>Facture Annulée: "
				+ factureAnnule + "</span>" + "</div>" + "<br\"><br\">" + "<div style='min-width:100%;'>"
				+ "<h4>Produits vendus<h4>"
				+ ((lines.size() > 0)
						? ("<table style='min-width:50%;'>" + "<thead>"
								+ "<tr style='font-size:15px;background-color:#FFA500'>"
								+ "<th style='text-align:left' >Nom du produit/service</th>" + "<th >Qté</th>"
								+ "<th >Total(XAF)</th>" + "</tr>" + "</thead>" + "<tbody>" + ligneTableau + "</tbody>"
								+ "</table>")
						: ("<h6>Aucune produits vendu durant " + (isMonth ? "ce mois" : "cette journée") + "...!</h6>"))
				+ "<h4 style='margin-top:40px'>Dépenses realisées<h4>"
				+ "<span style='margin-top:5px;margin-bottom:5px;font-weight:600;font-size:12px;color:blue'>Nombre de Fiche de dépense: "
				+ NombreFicheDepense + "</span><br/>"
				+ ((linesDepenses.size() > 0)
						? ("<table style='min-width:50%;'>" + "<thead>"
								+ "<tr style='font-size:15px;background-color:#FFA500'>"
								+ "<th style='text-align:left' >Point de dépense</th>" + "<th >Nbr</th>"
								+ "<th >Total(XAF)</th>" + "</tr>" + "</thead>" + "<tbody>" + ligneTableauDepense
								+ "</tbody>" + "</table>")
						: ("<h6>Aucune dépense realisée durant " + (isMonth ? "ce mois" : "cette journée")
								+ "...!</h6>"))
				+ "</div>" + "<br/>" + "<div>Streenge App</div>" + "" + "</body>" + "</html>" + "");

		return (headerHTML + bodyHTML);
	}

}
