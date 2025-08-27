package com.streenge.service.utils.streengeFunction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.streenge.model.LigneDocument;
import com.streenge.model.admin.Utilisateur;
import com.streenge.model.admin.UtilisateurStockage;
import com.streenge.model.produit.ProduitStockage;
import com.streenge.model.vente.facture.LigneFacture;
import com.streenge.service.utils.erroclass.ErrorAPI;
import com.streenge.service.utils.erroclass.StreengeException;
import com.streenge.service.utils.formInt.FormPanier;
import com.streenge.service.utils.formInt.LinePanier;
import com.streenge.service.utils.streengeData.Data;
import com.streenge.service.utils.streengeData.Periode;
import com.streenge.service.utils.streengeData.Profil;

@Service
public class Methode {

	public static void controlFormPanier(FormPanier formPanier) {
		controlFormPanier(formPanier, false);
	}

	public static void controlFormPanier(FormPanier formPanier, boolean controlPrixInferieur) {
		if (formPanier.getLines().isEmpty()) {
			throw new StreengeException(
					new ErrorAPI("Le document ne peut pas être vide, il doit contenir au moins un article"));
		}

		// ici on verifie que que l'utilisateur a bien le droit de vendre a un prix
		// minimu
		if (formPanier.getUtilisateur() != null && controlPrixInferieur) {
			for (LinePanier line : formPanier.getLines()) {
				if ((formPanier.getUtilisateur().getPrixInferieurMin()==null || formPanier.getUtilisateur().getPrixInferieurMin().isBlank()) && line.getPrix() < line.getPrixMin()) {
					throw new StreengeException(new ErrorAPI("Vous n'avez pas le droit de vendre "
							+ line.getDesignation() + " a moins de " + line.getPrixMin() + " " + Data.getDevise()));
				}
			}
		}
	}

	public static String createID(CrudRepository<?, String> repository, String prefix) {
		int number = (int) repository.count();
		String id = prefix;
		if (number < 9) {
			id += "00000" + (number + 1);
		} else if (number < 99) {
			id += "0000" + (number + 1);
		} else if (number < 999) {
			id += "000" + (number + 1);
		} else if (number < 9999) {
			id += "00" + (number + 1);
		} else if (number < 99999) {
			id += "0" + (number + 1);
		} else if (number < 999999) {
			id += "" + (number + 1);
		} else {
			ErrorAPI errorAPI = new ErrorAPI();
			errorAPI.setMessage(
					"Problème Majeur...! l'application est Saturer, vous avez atteint la limite Autorisée...! Contactez L'informaticien...!");
			throw new StreengeException(errorAPI);
		}

		// Optional<Object> devisTest = repository.findById(id);

		if (repository.findById(id).isPresent()) { 
			int nbr = number + 1;
			String id2 = "";
			do {
				nbr++;
				if (nbr < 10) {
					id2 = prefix + "00000" + nbr;
				} else if (nbr < 100) {
					id2 = prefix + "0000" + nbr;
				} else if (nbr < 1000) {
					id2 = prefix + "000" + nbr;
				} else if (nbr < 10000) {
					id2 = prefix + "00" + nbr;
				} else if (nbr < 100000) {
					id2 = prefix + "0" + nbr;
				} else if (nbr < 1000000) {
					id2 = prefix + "0" + nbr;
				}

				id = id2;

			} while (repository.findById(id).isPresent());
		}

		return id;
	}

	public static float getMomtantTolalDocument(List<LigneDocument> ligneDocuments, float coutLivraison) {

		float montantTotal = coutLivraison;
		for (LigneDocument ligneDocument : ligneDocuments) {
			float totalHT = (ligneDocument.getPrix() * ligneDocument.getQuantite() - ligneDocument.getRemise());
			float totalTTC = totalHT;

			if (ligneDocument.getTaxe() != null) {
				totalTTC = totalHT + (totalHT * (ligneDocument.getTaxe().getTaux() / 100));
			}
			montantTotal += totalTTC;
		}
		return montantTotal;
	}

	public static float getQuantiteTotalDocument(List<LigneDocument> ligneDocuments) {
		float quantite = 0;
		for (LigneDocument ligneDocument : ligneDocuments) {
			quantite += ligneDocument.getQuantite();
		}
		return quantite;
	}

	public static String upperCaseFirst(String val) {
		char[] arr = val.toCharArray();
		arr[0] = Character.toUpperCase(arr[0]);
		return new String(arr);
	}

	// Speciallement cree pour genere les indentifiant des movement de stock vue que
	// j'ai decide t'utiliser une seul table pour tou les different ty
	public static String createID(CrudRepository<?, String> repository, String prefix, int number) {
		// int number = (int) repository.count();
		int nbr = number;
		String id = "";
		do {
			nbr++;
			if (nbr < 10) {
				id = prefix + "00000" + nbr;
			} else if (nbr < 100) {
				id = prefix + "0000" + nbr;
			} else if (nbr < 1000) {
				id = prefix + "000" + nbr;
			} else if (nbr < 10000) {
				id = prefix + "00" + nbr;
			} else if (nbr < 100000) {
				id = prefix + "0" + nbr;
			} else if (nbr < 1000000) {
				id = prefix + "0" + nbr;
			} else {
				ErrorAPI errorAPI = new ErrorAPI();
				errorAPI.setMessage(
						"Problème Majeur...! l'application est Saturer, vous avez atteint la limite Autorisée...! Contactez L'informaticien...!");
				throw new StreengeException(errorAPI);
			}

		} while (repository.findById(id).isPresent());

		return id;
	}

	public static Date getDateYearBefore(int ecartYear) {

		String dateFormString;

		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		// int day=calendar.get(Calendar.DAY_OF_MONTH);
		// int month = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);

		year = year - ecartYear;

		dateFormString = "" + year + "-01-01 00:00";

		try {
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateFormString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	public static Date getDateMonthBefore(int ecartMonth) {

		String dateFormString;

		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		// int day=calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);

		month = month - ecartMonth;

		dateFormString = "" + year + "-" + (month) + "-01 00:00";

		try {
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateFormString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	public static Date getDateDayBefore(int ecartDay) {

		String dateFormString;

		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);

		day = day - ecartDay;

		dateFormString = "" + year + "-" + (month) + "-" + (day) + " 00:00";

		try {
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateFormString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	public static List<ProduitStockage> getProduitStockagesEager(List<ProduitStockage> produitStockages) {
		List<ProduitStockage> produitStockages2 = new ArrayList<>();

		for (ProduitStockage produitStockage : produitStockages) {
			ProduitStockage produitStockage1 = new ProduitStockage();

			produitStockage1.setId(produitStockage.getId());
			produitStockage1.setStockage(produitStockage.getStockage());
			produitStockage1.setProduit(produitStockage.getProduit());

			produitStockage1.setStockReel(produitStockage.getStockReel() + 0);
			produitStockage1.setStockDisponible(produitStockage.getStockDisponible() + 0);
			produitStockage1.setStockAvenir(produitStockage.getStockAvenir() + 0);

			produitStockages2.add(produitStockage1);
		}

		return produitStockages2;
	}

	public static float getTotalLigneDocument(LigneDocument ligne) {
		float totalTTC = 0;

		float totalHT = 0;

		totalHT = ligne.getPrix() * ligne.getQuantite() - ligne.getRemise();

		totalTTC = totalHT + ((ligne.getTaxe() != null) ? ((totalHT * ligne.getTaxe().getTaux()) / 100) : 0);

		return totalTTC;
	}

	public static float getTotalLigneDocument(LigneFacture ligne) {
		float totalTTC = 0;

		float totalHT = 0;

		totalHT = ligne.getPrix() * ligne.getQuantite() - ligne.getRemise();

		totalTTC = totalHT + ((ligne.getTaxe() != null) ? ((totalHT * ligne.getTaxe().getTaux()) / 100) : 0);

		return totalTTC;
	}

	public static List<Date> getIntevalDateForPeriode(int periode) {
		List<Date> dates = new ArrayList<Date>();
		Date toDay = new Date();
		Date dateDebut = new Date();
		Date dateFin = new Date();
		if (periode == Periode.aujourdhui) {
			dateDebut = Methode.getDateDayBefore(0);
		} else if (periode == Periode.cetteSemaine) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(toDay);
			int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
			int mondayPositionRelativeToMow = weekDay - 1;
			if (mondayPositionRelativeToMow < 0) {
				mondayPositionRelativeToMow = 6;
			}
			dateDebut = Methode.getDateDayBefore(mondayPositionRelativeToMow);
		} else if (periode == Periode.ceMois) {
			dateDebut = Methode.getDateMonthBefore(0);
		} else if (periode == Periode.moisPasse) {
			dateDebut = Methode.getDateMonthBefore(1);
			dateFin = Methode.getDateMonthBefore(0);
		} else if (periode == Periode.cetteAnnee) {
			dateDebut = Methode.getDateYearBefore(0);
		} else if (periode == Periode.anneePasse) {
			dateDebut = Methode.getDateYearBefore(1);
			dateFin = Methode.getDateYearBefore(0);
		} else {
			dateDebut = Methode.getDateMonthBefore(0);
		}

		dates.add(dateDebut);
		dates.add(dateFin);

		return dates;
	}

	public static int userLevel(Utilisateur user) {
		int level = 1;

		if (Profil.getRoot().equals(user.getProfil())) {
			level = 5;
		} else if (Profil.getDirecteur().equals(user.getProfil())) {
			level = 4;
		} else if (Profil.getAdminprincipal().equals(user.getProfil())) {
			level = 3;
		} else if (Profil.getAdminsecondaire().equals(user.getProfil())) {
			level = 2;
		}

		return level;
	}
	
	public static String getProfil(int userLevel) {
		String profil=Profil.vendeur;
		if (userLevel==2) {
			profil=Profil.adminSecondaire;
		}else if (userLevel==3) {
			profil=Profil.adminPrincipal;
		}else if (userLevel==4) {
			profil=Profil.directeur;
		}else if (userLevel>=5) {
			profil=Profil.root;
		} 
		return profil;
	}

	public static Boolean stockageAccess(Utilisateur user, int idStockage) {
		Boolean haveAccess = false;
		if (userLevel(user) >= 3) {
			haveAccess = true;
		} else if (user.getUtilisateurStockages()!=null) {
			for (UtilisateurStockage utilisateurStockage : user.getUtilisateurStockages()) {
				if (utilisateurStockage.getStockage().getIdStockage() == idStockage) {
					haveAccess = true;
					break;
				}
			}
		}
		return haveAccess;
	}

}
