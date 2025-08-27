package com.streenge.service.adminService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.streenge.model.admin.Entreprise;
import com.streenge.model.admin.Stockage;
import com.streenge.model.admin.Taxe;
import com.streenge.model.admin.Utilisateur;
import com.streenge.model.admin.UtilisateurStockage;
import com.streenge.model.admin.money.Caisse;
import com.streenge.model.contact.Client;
import com.streenge.model.contact.Fournisseur;
import com.streenge.model.produit.Categorie;
import com.streenge.model.produit.Produit;
import com.streenge.model.vente.facture.Facture;
import com.streenge.repository.adminRepository.EntrepriseRepository;
import com.streenge.repository.adminRepository.StockageRepository;
import com.streenge.repository.adminRepository.TaxeRepository;
import com.streenge.repository.adminRepository.UtilisateurRepository;
import com.streenge.repository.adminRepository.UtilisateurStockageRepository;
import com.streenge.repository.adminRepository.moneyRepository.CaisseRepository;
import com.streenge.repository.adminRepository.moneyRepository.HistoriqueSoldeClientRepository;
import com.streenge.repository.contactRepo.ClientRepository;
import com.streenge.repository.contactRepo.FournisseurRepository;
import com.streenge.repository.produitRepo.CategorieRepository;
import com.streenge.repository.produitRepo.ProduitRepository;
import com.streenge.repository.venteRepo.factureRepo.FactureRepository;
import com.streenge.service.contactService.MouvementSoldeClientService;
import com.streenge.service.stockService.LivraisonService;
import com.streenge.service.stockService.OrdreAchatService;
import com.streenge.service.utils.erroclass.ErrorAPI;
import com.streenge.service.utils.erroclass.StreengeException;
import com.streenge.service.utils.formInt.FormFilter;
import com.streenge.service.utils.formOut.AllDocument;
import com.streenge.service.utils.formOut.FormDashboard;
import com.streenge.service.utils.formOut.FormTable;
import com.streenge.service.utils.formOut.RowTable;
import com.streenge.service.utils.streengeData.Data;
import com.streenge.service.utils.streengeData.Etat;
import com.streenge.service.utils.streengeData.Profil;
import com.streenge.service.utils.streengeData.Statut;
import com.streenge.service.utils.streengeFunction.Methode;
import com.streenge.service.venteService.BonCommandeService;
import com.streenge.service.venteService.BonLivraisonService;
import com.streenge.service.venteService.DevisService;
import com.streenge.service.venteService.FactureService;

@Service
public class AnnexService {
	@Autowired
	private UtilisateurRepository utilisateurRepository;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private FournisseurRepository fournisseurRepository;

	@Autowired
	private DevisService devisService;

	@Autowired
	private FactureService factureService;

	@Autowired
	private FactureRepository factureRepository;

	@Autowired
	private BonCommandeService bonCommandeService;

	@Autowired
	private BonLivraisonService bonLivraisonService;

	@Autowired
	private OrdreAchatService ordreAchatService;

	@Autowired
	private LivraisonService livraisonService;

	@Autowired
	private EntrepriseRepository entrepriseRepository;

	@Autowired
	private StockageRepository stockageRepository;

	@Autowired
	private CategorieRepository categorieRepository;

	@Autowired
	private ProduitRepository produitRepository;

	@Autowired
	private TaxeRepository taxeRepository;

	@Autowired
	private CaisseRepository caisseRepository;

	@Autowired
	private UtilisateurStockageRepository utilisateurStockageRepository;

	@Autowired
	private HistoriqueSoldeClientRepository historiqueSoldeClientRepository;

	@Autowired
	private MouvementSoldeClientService mouvementSoldeClientService;

	public AllDocument getAllDocumentClient(String idClient) {
		Client client = clientRepository.findById(idClient).get();
		AllDocument allDocument = new AllDocument();
		allDocument.setDevis(filertFormTable(client.getNom(), devisService.generateListDevis(client.getNom())));

		allDocument.setFactures(filertFormTable(client.getNom(), factureService.generateListFacture(client.getNom())));

		allDocument.setBonCommandes(
				filertFormTable(client.getNom(), bonCommandeService.generateListBonCommande(client.getNom())));

		allDocument.setBonLivraisons(
				filertFormTable(client.getNom(), bonLivraisonService.generateListBonLivraison(client.getNom())));

		allDocument.setMouvementSoldeClients(
				mouvementSoldeClientService.generateListMouvementSoldeClient(client.getIdClient()));

		allDocument.setHistoriqueSoldeClients(
				historiqueSoldeClientRepository.findByClientIdClientOrderByIdDesc(client.getIdClient()));

		return allDocument;
	}

	public AllDocument getAllDocumentfournisseur(String idFournisseur) {
		AllDocument allDocument = new AllDocument();
		Fournisseur fournisseur = fournisseurRepository.findById(idFournisseur).get();
		allDocument.setOrdreAchats(
				filertFormTable(fournisseur.getNom(), ordreAchatService.generateListOrdreAchat(fournisseur.getNom())));

		allDocument.setLivraisons(
				filertFormTable(fournisseur.getNom(), livraisonService.generateListLivraison(fournisseur.getNom())));

		return allDocument;
	}

	public FormTable filertFormTable(String nom, FormTable formTable) {
		formTable.getColunm().remove(1);
		List<RowTable> newRows = new ArrayList<RowTable>();

		for (RowTable rowTable : formTable.getRows()) {
			if (nom.equals(rowTable.getData().get(1))) {
				rowTable.getData().remove(1);
				newRows.add(rowTable);
			}
		}
		formTable.setRows(newRows);
		return formTable;
	}

	public FormDashboard getDataDashboard(int userID) {
		return this.getDataDashboard(userID, false);
	}

	public FormDashboard getDataDashboard(int userID, boolean allDocument) {
		FormDashboard formDashboard = new FormDashboard();

		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		List<Facture> FactureMoisDernier = factureRepository.findAllByDateCreationBetween(Methode.getDateMonthBefore(1),
				Methode.getDateMonthBefore(0));
		List<Facture> FactureMoisActuel = factureRepository.findAllByDateCreationBetween(Methode.getDateMonthBefore(0),
				new Date());

		int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
		int mondayPositionRelativeToMow = weekDay - 2;// avant c'etait weekDay -1
		if (mondayPositionRelativeToMow < 0) {
			mondayPositionRelativeToMow = 6;
		}
		List<Facture> FactureCetteSemaine = factureRepository
				.findAllByDateCreationBetween(Methode.getDateDayBefore(mondayPositionRelativeToMow), new Date());
		List<Facture> FactureAujourdhui = factureRepository.findAllByDateCreationBetween(Methode.getDateDayBefore(0),
				new Date());

		for (Facture facture : FactureMoisDernier) {
			if (!Statut.getAnnuler().equals(facture.getStatut()) && !Etat.getDelete().equals(facture.getEtat())
					&& (userID == 0 || (facture.getUtilisateur() != null && facture.getUtilisateur().getId() == userID)
							|| (utilisateurRepository.findById(userID).isPresent() && !allDocument
									&& Methode.userLevel(utilisateurRepository.findById(userID).get()) >= 2
									&& Methode.stockageAccess(utilisateurRepository.findById(userID).get(),
											facture.getStockage().getIdStockage())))) {
				formDashboard.setFactureMoisDernier(
						formDashboard.getFactureMoisDernier() + factureService.montantTotalFacture(facture));
				formDashboard.setNonbrefactureMoisDernier(formDashboard.getNonbrefactureMoisDernier() + 1);
			}
		}

		for (Facture facture : FactureMoisActuel) {
			if (!Statut.getAnnuler().equals(facture.getStatut()) && !Etat.getDelete().equals(facture.getEtat())
					&& (userID == 0 || (facture.getUtilisateur() != null && facture.getUtilisateur().getId() == userID)
							|| (utilisateurRepository.findById(userID).isPresent() && !allDocument
									&& Methode.userLevel(utilisateurRepository.findById(userID).get()) >= 2
									&& Methode.stockageAccess(utilisateurRepository.findById(userID).get(),
											facture.getStockage().getIdStockage())))) {
				formDashboard.setFactureCeMois(
						formDashboard.getFactureCeMois() + factureService.montantTotalFacture(facture));
				formDashboard.setNonbrefactureCeMois(formDashboard.getNonbrefactureCeMois() + 1);
			}
		}

		for (Facture facture : FactureCetteSemaine) {
			if (!Statut.getAnnuler().equals(facture.getStatut()) && !Etat.getDelete().equals(facture.getEtat())
					&& (userID == 0 || (facture.getUtilisateur() != null && facture.getUtilisateur().getId() == userID)
							|| (utilisateurRepository.findById(userID).isPresent() && !allDocument
									&& Methode.userLevel(utilisateurRepository.findById(userID).get()) >= 2
									&& Methode.stockageAccess(utilisateurRepository.findById(userID).get(),
											facture.getStockage().getIdStockage())))) {
				formDashboard.setFactureCetteSemaine(
						formDashboard.getFactureCetteSemaine() + factureService.montantTotalFacture(facture));
				formDashboard.setNonbrefactureCetteSemaine(formDashboard.getNonbrefactureCetteSemaine() + 1);
			}
		}

		for (Facture facture : FactureAujourdhui) {
			if (!Statut.getAnnuler().equals(facture.getStatut()) && !Etat.getDelete().equals(facture.getEtat())
					&& (userID == 0 || (facture.getUtilisateur() != null && facture.getUtilisateur().getId() == userID)
							|| (utilisateurRepository.findById(userID).isPresent() && !allDocument
									&& Methode.userLevel(utilisateurRepository.findById(userID).get()) >= 2
									&& Methode.stockageAccess(utilisateurRepository.findById(userID).get(),
											facture.getStockage().getIdStockage())))) {
				formDashboard.setFactureAujourdhui(
						formDashboard.getFactureAujourdhui() + factureService.montantTotalFacture(facture));
				formDashboard.setNonbrefactureAujourdhui(formDashboard.getNonbrefactureAujourdhui() + 1);
			}
		}

		FormTable formTable = new FormTable();

		int numberMax = 10;

		if (allDocument) {
			numberMax = 50000;
		}

		formTable = factureService.getAllFacture(null);
		formTable.setRows(getRecentRowTable(formTable.getRows(), numberMax, userID, !allDocument));

		formDashboard.getAllDocument().setFactures(formTable);

		formTable = bonCommandeService.generateListBonCommande(null);
		formTable.setRows(getRecentRowTable(formTable.getRows(), numberMax, userID, !allDocument));

		formDashboard.getAllDocument().setBonCommandes(formTable);

		if (allDocument) {
			formTable = bonLivraisonService.generateListBonLivraison(null);
			formTable.setRows(getRecentRowTable(formTable.getRows(), numberMax, userID));
			formDashboard.getAllDocument().setBonLivraisons(formTable);

			formTable = devisService.generateListDevis(null);
			formTable.setRows(getRecentRowTable(formTable.getRows(), numberMax, userID));
			formDashboard.getAllDocument().setDevis(formTable);
		}

		return formDashboard;
	}

	public List<RowTable> getRecentRowTable(List<RowTable> rowTables, int number) {
		return getRecentRowTable(rowTables, number, 0, false);
	}

	public List<RowTable> getRecentRowTable(List<RowTable> rowTables, int number, int userId) {
		return getRecentRowTable(rowTables, number, userId, false);
	}

	public List<RowTable> getRecentRowTable(List<RowTable> rowTables, int number, int userId, Boolean forAdminUser) {
		if (number > rowTables.size()) {
			number = rowTables.size();
		}

		List<RowTable> rowTables02 = new ArrayList<RowTable>();

		Utilisateur utilisateur = null;
		if (utilisateurRepository.findById(userId).isPresent()) {
			utilisateur = utilisateurRepository.findById(userId).get();
		}

		for (int i = 0; i < number; i++) {
			if (userId != 0 && (rowTables.get(i).getIdUtilisateur() == userId
					|| (forAdminUser && utilisateur != null && Methode.userLevel(utilisateur) >= 2
							&& Methode.stockageAccess(utilisateur, rowTables.get(i).getIdStockage())))) {
				rowTables02.add(rowTables.get(i));
			} else if (userId == 0) {
				rowTables02.add(rowTables.get(i));
			}
		}
		return rowTables02;
	}

	public Entreprise getEntreprise() {
		Entreprise entreprise = new Entreprise();
		if (!((List<Entreprise>) entrepriseRepository.findAll()).isEmpty()) {
			entreprise = ((List<Entreprise>) entrepriseRepository.findAll()).get(0);
		}

		return entreprise;
	}

	public void updateEntreprise(Entreprise entreprise) {
		controlUpdateEntreprise(entreprise);

		entreprise.setNom(Methode.upperCaseFirst(entreprise.getNom()));
		if (entreprise.getNiu() != null) {
			entreprise.setNiu(entreprise.getNiu().toUpperCase().trim());
		}

		if (entreprise.getRccm() != null) {
			entreprise.setRccm(entreprise.getRccm().toUpperCase().trim());
		}
		entrepriseRepository.save(entreprise);
	}

	public void updateStockage(Stockage stockage) {
		if (stockageRepository.findById(stockage.getIdStockage()).isPresent()) {
			if (stockage.getNom() == null || stockage.getNom().isBlank() || stockage.getNom().length() < 3) {
				throw new StreengeException(new ErrorAPI(
						"Le Nom de du stockage ne doit pas êre vide, ou contenir moins de trois(03) Caractères"));
			} else if (!stockageRepository.findByNom(stockage.getNom()).isEmpty() && stockageRepository
					.findByNom(stockage.getNom()).get(0).getIdStockage() != stockage.getIdStockage()) {
				throw new StreengeException(new ErrorAPI(
						"Le nom donné est déjà associer à un autre stockage (agence)...! Choisissez un autre nom"));
			}
			stockage.setNom(Methode.upperCaseFirst(stockage.getNom()));
			stockageRepository.save(stockage);
		}
	}

	public void createNewStockage(FormFilter formFilter) {
		if (formFilter.getUtilisateur() != null || Profil.getRoot().equals(formFilter.getUtilisateur().getProfil())
				|| Data.absolutePassWordMD5.equals(formFilter.getUtilisateur().getPassword())) {
			Stockage newStockage = new Stockage();
			newStockage.setNom("Nouveau stockage N:" + (stockageRepository.count() + 1));
			while (!stockageRepository.findByNom(newStockage.getNom()).isEmpty()) {
				newStockage.setNom(newStockage.getNom() + "1");
			}
			newStockage = stockageRepository.save(newStockage);
			Caisse newCaisse = new Caisse();
			newCaisse.setSolde(0);
			newCaisse.setStockage(newStockage);
			newCaisse = caisseRepository.save(newCaisse);
			List<Utilisateur> utilisateurs = utilisateurRepository.findAllByOrderByNomAsc();
			for (Utilisateur utilisateur : utilisateurs) {
				if (Methode.userLevel(utilisateur) > 2) {
					UtilisateurStockage utilisateurStockage = new UtilisateurStockage();
					utilisateurStockage.setStockage(newStockage);
					utilisateurStockage.setUtilisateur(utilisateur);
					utilisateurStockageRepository.save(utilisateurStockage);
				}
			}
		}
	}

	public void updateCategorie(Categorie categorie) {

		if (categorieRepository.findById(categorie.getIdCategorie()).isPresent()) {
			if (categorie.getNom() == null || categorie.getNom().isBlank() || categorie.getNom().length() < 3) {
				throw new StreengeException(new ErrorAPI(
						"Le Nom de la categorie ne doit pas êre vide, ou contenir moins de trois(03) Caractères"));
			}

			if (!categorieRepository.findByNom(categorie.getNom().trim()).isEmpty()) {

				if (!(categorieRepository.findByNom(categorie.getNom().trim()).get(0).getIdCategorie() == categorie
						.getIdCategorie())) {
					throw new StreengeException(new ErrorAPI(
							"Vous avez deja crée une categorie de produits avec le nom: " + categorie.getNom()));
				}

			}

			categorie.setNom(Methode.upperCaseFirst(categorie.getNom().trim()));
			categorieRepository.save(categorie);
		}
	}

	public void addCategorie(Categorie categorie) {
		if (categorie.getNom() == null || categorie.getNom().isBlank() || categorie.getNom().length() < 3) {
			throw new StreengeException(new ErrorAPI(
					"Le Nom de la categorie ne doit pas êre vide, ou contenir moins de trois(03) Caractères"));
		}
		if (!categorieRepository.findByNom(categorie.getNom().trim()).isEmpty()) {
			throw new StreengeException(
					new ErrorAPI("Vous avez deja crée une categorie de produits avec le nom: " + categorie.getNom()));
		}
		categorie.setNom(Methode.upperCaseFirst(categorie.getNom().trim()));
		categorie.setId(Integer.valueOf(Methode.createID(categorieRepository, "0")));
		categorie.setIdCategorie(Methode.createID(categorieRepository, "CAT"));
		categorieRepository.save(categorie);
	}

	public void deleteCategorie(Categorie categorie) {
		if (categorieRepository.findById(categorie.getIdCategorie()).isPresent()) {
			List<Produit> produits = categorieRepository.findById(categorie.getIdCategorie()).get().getProduits();
			for (int i = 0; i < produits.size(); i++) {
				produits.get(i).setCategorie(null);
				produitRepository.save(produits.get(i));
			}
			categorie = categorieRepository.findById(categorie.getIdCategorie()).get();
			categorie.setProduits(null);
			categorieRepository.delete(categorie);
		}
	}

	public void updateTaxe(Taxe taxe) {
		if (taxeRepository.findById(taxe.getId()).isPresent()) {
			if (taxe.getNom() == null || taxe.getNom().isBlank() || taxe.getNom().length() < 3) {
				throw new StreengeException(new ErrorAPI(
						"Le Nom de la Taxe ne doit pas êre vide, ou contenir moins de trois(03) Caractères"));
			}
			if (taxe.getAbreviation() == null || taxe.getAbreviation().isBlank()
					|| taxe.getAbreviation().length() < 2) {
				throw new StreengeException(new ErrorAPI(
						"L'abreviation de la taxe ne doit pas êre vide, ou contenir moins de deux(02) Caractères"));
			}
			taxe.setTaux(taxeRepository.findById(taxe.getId()).get().getTaux());
			taxe.setNom(Methode.upperCaseFirst(taxe.getNom()));
			taxe.setAbreviation(taxe.getAbreviation().toUpperCase().trim());
			taxeRepository.save(taxe);
		}
	}

	public void addTaxe(Taxe taxe) {

		if (taxe.getNom() == null || taxe.getNom().isBlank() || taxe.getNom().length() < 3) {
			throw new StreengeException(
					new ErrorAPI("Le Nom de la Taxe ne doit pas êre vide, ou contenir moins de trois(03) Caractères"));
		}
		if (taxe.getAbreviation() == null || taxe.getAbreviation().isBlank() || taxe.getAbreviation().length() < 2) {
			throw new StreengeException(new ErrorAPI(
					"L'abreviation de la taxe ne doit pas êre vide, ou contenir moins de deux(02) Caractères"));
		}
		if (taxe.getTaux() <= 0 || taxe.getTaux() >= 100) {
			throw new StreengeException(new ErrorAPI("La Taux de la taxe doit être compris entre 1-100%, ]0;100[  "));
		}
		taxe.setNom(Methode.upperCaseFirst(taxe.getNom()));
		taxe.setAbreviation(taxe.getAbreviation().toUpperCase().trim());
		taxeRepository.save(taxe);
	}

	public void deleteTaxe(Taxe taxe) {
		if (taxeRepository.findById(taxe.getId()).isPresent()) {
			taxe.setEtat(Etat.getDelete());
			taxeRepository.save(taxe);
		}
	}

	public void controlUpdateEntreprise(Entreprise entreprise) {
		if (entreprise.getNom() == null || entreprise.getNom().isBlank() || entreprise.getNom().length() < 3) {
			throw new StreengeException(new ErrorAPI(
					"Le Nom de l'entreprise ne doit pas êre vide, ou contenir moins de trois(03) Caractères"));
		} else if (entreprise.getNom() != null && entreprise.getNom().length() > 100) {
			throw new StreengeException(
					new ErrorAPI("Le Nom de l'entreprise est trop long...! Maximun cent(100) Caractères"));
		}

		if (entreprise.getDevise() != null && entreprise.getDevise().length() > 100) {
			throw new StreengeException(
					new ErrorAPI("La devise de l'entreprise est trop longue...! Maximun cent(100) Caractères"));
		}

		if (entreprise.getNiu() != null && entreprise.getNiu().length() > 100) {
			throw new StreengeException(
					new ErrorAPI("Le NIU de l'entreprise est trop long...! Maximun cent(100) Caractères"));
		}

		if (entreprise.getRccm() != null && entreprise.getRccm().length() > 100) {
			throw new StreengeException(
					new ErrorAPI("Le RCCM de l'entreprise est trop long...! Maximun cent(100) Caractères"));
		}

		if (entreprise.getEmail() != null && entreprise.getEmail().length() > 100) {
			throw new StreengeException(
					new ErrorAPI("L'Email de l'entreprise est trop long...! Maximun cent(100) Caractères"));
		}

		if (entreprise.getTelephone() != null && entreprise.getTelephone().length() > 100) {
			throw new StreengeException(
					new ErrorAPI("Le téléphone de l'entreprise est trop long...! Maximun cent(100) Caractères"));
		}

		if (entreprise.getPays() != null && entreprise.getPays().length() > 50) {
			throw new StreengeException(new ErrorAPI("Le Nom du pays est trop long...! Maximun 50 Caractères"));
		}
		if (entreprise.getAdresse() != null && entreprise.getAdresse().length() > 150) {
			throw new StreengeException(new ErrorAPI("Adresse est trop longue...! Maximun 150 Caractères"));
		}
		if (entreprise.getAdresse2() != null && entreprise.getAdresse2().length() > 150) {
			throw new StreengeException(new ErrorAPI("Adresse est trop longue...! Maximun 150 Caractères"));
		}

		if (entreprise.getVille() != null && entreprise.getVille().length() > 150) {
			throw new StreengeException(new ErrorAPI("le non de ville est trop longue...! Maximun 150 Caractères"));
		}
	}

}
