package com.streenge.service.venteService;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.streenge.model.admin.Entreprise;
import com.streenge.model.admin.Stockage;
import com.streenge.model.admin.Taxe;
import com.streenge.model.admin.Utilisateur;
import com.streenge.model.admin.money.Caisse;
import com.streenge.model.admin.money.HistoriqueCaisse;
import com.streenge.model.admin.money.HistoriqueSoldeClient;
import com.streenge.model.contact.Client;
import com.streenge.model.produit.Pack;
import com.streenge.model.produit.ProduitStockage;
import com.streenge.model.vente.facture.Facture;
import com.streenge.model.vente.facture.LigneFacture;
import com.streenge.model.vente.facture.MethodePaiement;
import com.streenge.model.vente.facture.Paiement;
import com.streenge.repository.adminRepository.EntrepriseRepository;
import com.streenge.repository.adminRepository.StockageRepository;
import com.streenge.repository.adminRepository.TaxeRepository;
import com.streenge.repository.adminRepository.UtilisateurRepository;
import com.streenge.repository.adminRepository.moneyRepository.CaisseRepository;
import com.streenge.repository.adminRepository.moneyRepository.HistoriqueCaisseRepository;
import com.streenge.repository.adminRepository.moneyRepository.HistoriqueSoldeClientRepository;
import com.streenge.repository.contactRepo.ClientRepository;
import com.streenge.repository.produitRepo.PackRepository;
import com.streenge.repository.produitRepo.ProduitStockageRepository;
import com.streenge.repository.venteRepo.BonCommandeRepository;
import com.streenge.repository.venteRepo.DevisRepository;
import com.streenge.repository.venteRepo.factureRepo.FactureRepository;
import com.streenge.repository.venteRepo.factureRepo.LigneFactureRepository;
import com.streenge.repository.venteRepo.factureRepo.MethodePaiementRepository;
import com.streenge.repository.venteRepo.factureRepo.PaiementRepository;
import com.streenge.service.contactService.ClientService;
import com.streenge.service.produitService.ProduitService;
import com.streenge.service.stockService.RegistreStockService;
import com.streenge.service.utils.erroclass.ErrorAPI;
import com.streenge.service.utils.erroclass.StreengeException;
import com.streenge.service.utils.formInt.FormPanier;
import com.streenge.service.utils.formInt.LinePanier;
import com.streenge.service.utils.formOut.FormPack;
import com.streenge.service.utils.formOut.FormPaiement;
import com.streenge.service.utils.formOut.FormTable;
import com.streenge.service.utils.formOut.RowTable;
import com.streenge.service.utils.streengeData.ColorStatut;
import com.streenge.service.utils.streengeData.Data;
import com.streenge.service.utils.streengeData.Etat;
import com.streenge.service.utils.streengeData.Profil;
import com.streenge.service.utils.streengeData.Statut;
import com.streenge.service.utils.streengeFunction.Methode;

@Service
public class FactureService {

	@Autowired
	private FactureRepository factureRepository;

	@Autowired
	private EntrepriseRepository entrepriseRepository;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private StockageRepository stockageRepository;

	@Autowired
	private PackRepository packRepository;

	@Autowired
	private TaxeRepository taxeRepository;

	@Autowired
	private MethodePaiementRepository methodePaiementRepository;

	@Autowired
	private PaiementRepository paiementRepository;

	@Autowired
	private LigneFactureRepository ligneFactureRepository;

	@Autowired
	private ClientService clientService;

	@Autowired
	private ProduitService produitService;

	@Autowired
	private DevisRepository devisRepository;

	@Autowired
	private BonCommandeRepository bonCommandeRepository;

	@Autowired
	private BonCommandeService bonCommandeService;

	@Autowired
	private RegistreStockService registreStockService;

	@Autowired
	private ProduitStockageRepository produitStockageRepository;

	@Autowired
	private HistoriqueSoldeClientRepository historiqueSoldeClientRepository;

	@Autowired
	private HistoriqueCaisseRepository historiqueCaisseRepository;

	@Autowired
	private CaisseRepository		caisseRepository;
	@Autowired
	private UtilisateurRepository	utilisateurRepository;

	public Facture createOrUpdateFacture(FormPanier formPanier, boolean isNewFacture) {
		Methode.controlFormPanier(formPanier, true);
		if (formPanier.getNote()!=null) {
			formPanier.getNote().trim();
		}
		
		if (formPanier.getDescription()!=null) {
			formPanier.getDescription().trim();
		}

		List<ProduitStockage> produitStockagesBefore = Methode
				.getProduitStockagesEager((List<ProduitStockage>) produitStockageRepository.findAll());

		Utilisateur utilisateur = formPanier.getUtilisateur();
		Facture facture;
		Client previousClient = null;
		float previousRestantPaye = 0;
		Stockage previousStockage = null;

		if (utilisateur != null && utilisateur.getPassword().isBlank()) {
			utilisateur.setPassword(".");
		}
		if (utilisateur != null && utilisateur.getProfil() != Profil.getRoot()
				&& utilisateurRepository.findById(utilisateur.getId()).isPresent() && utilisateur.getPassword()
						.equals(utilisateurRepository.findById(utilisateur.getId()).get().getPassword())) {
			utilisateur = utilisateurRepository.findById(utilisateur.getId()).get();
			formPanier.setUtilisateur(utilisateur);
		} else {
			throw new StreengeException(
					new ErrorAPI("Impossible de creer/modifier cette facture...! Utilisateur Non defini..!"));
		}

		if (isNewFacture) {
			facture = new Facture();
			facture.setIdFacture(Methode.createID(factureRepository, "FA"));
			formPanier.setDateCreation(new Date());
			facture.setUtilisateur(utilisateur);

			if (!formPanier.getIdDevis().isBlank()) {
				// if (devisRepository.findById(formPanier.getIdDevis()).isPresent())
				facture.setDevi(devisRepository.findById(formPanier.getIdDevis()).get());
			}

			if (!formPanier.getIdBonCommande().isBlank()) {
				// if (devisRepository.findById(formPanier.getIdDevis()).isPresent())
				facture.setBonCommande(bonCommandeRepository.findById(formPanier.getIdBonCommande()).get());
			}

		} else {
			facture = factureRepository.findById(formPanier.getIdFacture().toUpperCase()).get();
			if (!Methode.stockageAccess(utilisateur, facture.getStockage().getIdStockage())
					|| Methode.userLevel(utilisateur) < 2) {
				throw new StreengeException(new ErrorAPI(
						"Vous n'êtes pas habilité à faire des modifications sur cette facture...! Seul un administrateur ayant acces a ce stockage peut modifier ce document."));
			}
			previousRestantPaye = getPaiementRestant(facture);
			previousClient = facture.getClient();
			previousStockage = facture.getStockage();
			for (LigneFacture ligne : facture.getLigneFactures()) {
				if (facture.getBonCommande() == null) {
					produitService.updateProduitQuantite(ligne.getPack(), ligne.getQuantite(), facture.getStockage(),
							true, true);
				}
				ligneFactureRepository.deleteLigneFacture(ligne.getId());
				facture.setDateModification(new Date());
			}
		}
		if (!clientRepository.findByNom(formPanier.getNomClient()).isEmpty()) {
			facture.setClient(clientRepository.findByNom(formPanier.getNomClient()).get(0));
		} else {
			facture.setClient(null);
		}
		// set
		// facture.setConditionVente(formPanier.getNote());
		facture.setNote(formPanier.getNote());
		facture.setCoutLivraison(formPanier.getCoutLivraison());
		facture.setDateCreation(formPanier.getDateCreation());
		facture.setDateEcheance(formPanier.getEcheance());
		facture.setDateLivraison(formPanier.getDateLivraison());
		facture.setPourcentageRemise(formPanier.getPourcentageRemise());
		facture.setDescription(formPanier.getDescription());
		facture.setSoumettreA(formPanier.getSoumettreA());
		if (isNewFacture) {
			facture.setEtat(Etat.getActif());
			facture.setStatut(Statut.getNonpaye());
		}
		if (stockageRepository.findById(formPanier.getIdStockage()).isPresent()) {
			facture.setStockage(stockageRepository.findById(formPanier.getIdStockage()).get());
		} else {
			ErrorAPI errorAPI = new ErrorAPI();
			errorAPI.setMessage(
					"L'enregistrement de la facture n'est pas possible, Le stockage choisi N'existe pas...! Changer de lieu de stockage et cree une nouvelle facture..! ");
			throw new StreengeException(errorAPI);
		}

		List<LigneFacture> ligneFactures = new ArrayList<LigneFacture>();
		List<LinePanier> lines = formPanier.getLines();
		for (LinePanier line : lines) {

			LigneFacture ligneFacture = new LigneFacture();
			ligneFacture.setFacture(facture);
			ligneFacture.setDesignation(line.getDesignation());
			ligneFacture.setPrix(line.getPrix());
			ligneFacture.setQuantite(line.getQuantite());
			ligneFacture.setRemise(line.getRemise());

			// ligneFacture.setCout_UMP(line.get);

			if (line.getTaxe() != null)
				ligneFacture.setTaxe(taxeRepository.findById(line.getTaxe().getId()).get());

			if (packRepository.findById(line.getIdPack()).isPresent()) {
				ligneFacture.setPack(packRepository.findById(line.getIdPack()).get());
				ligneFacture.setCout_UMP(
						ligneFacture.getPack().getProduit().getCout_UMP() * ligneFacture.getPack().getNombreUnite());
			} else {
				ErrorAPI errorAPI = new ErrorAPI();
				errorAPI.setMessage("L'enregistrement de la facture n'est pas possible,Le produit: "
						+ line.getDesignation().toUpperCase()
						+ " N'existe pas où alors ce produit à été récemment supprimer");
				throw new StreengeException(errorAPI);
			}
			ligneFactures.add(ligneFacture);
		}

		facture.setLigneFactures(ligneFactures);

		if (facture.getBonCommande() == null) {
			for (LigneFacture ligne : ligneFactures) {
				produitService.updateProduitQuantite(ligne.getPack(), (ligne.getQuantite() * -1), facture.getStockage(),
						true, true);
			}
		}

		// try {
		facture = factureRepository.save(facture);
		manageFacturePaiementOnCreationAndUpdate(isNewFacture, utilisateur, facture, previousRestantPaye,
				previousStockage, previousClient);
		// } catch (DataIntegrityViolationException e) {
		// System.out.println("Facture already exist");
		// }
		registreStockService.updateRegistreStock(produitStockagesBefore,
				isNewFacture ? "Creation d'une facture." : "Modification d'une facture", utilisateur,
				facture.getIdFacture());
		return facture;
	}

	public FormTable getAllFacture(String filter) {
		return this.generateListFacture(filter);
	}

	public FormPanier getPanierFacture(String idFacture) {
		updateStatutFacture(idFacture);
		FormPanier formPanier = new FormPanier();
		List<LinePanier> linePaniers = new ArrayList<LinePanier>();
		List<FormPack> listFormPacks = new ArrayList<FormPack>();
		// List<FormProduit> listFormProduits = new ArrayList<FormProduit>();

		LinePanier linePanier = null;
		Facture facture = factureRepository.findById(idFacture.toUpperCase()).get();

		List<LigneFacture> ligneFactures = facture.getLigneFactures();
		for (LigneFacture ligneFacture : ligneFactures) {
			linePanier = new LinePanier();

			linePanier.setDesignation(ligneFacture.getDesignation());
			linePanier.setPrix(ligneFacture.getPrix());
			linePanier.setQuantite(ligneFacture.getQuantite());
			linePanier.setRemise(ligneFacture.getRemise());
			linePanier.setPrixMin(ligneFacture.getPack().getPrixVenteMin());
			if (ligneFacture.getTaxe() != null) {
				Taxe taxe = ligneFacture.getTaxe();
				Taxe taxe1 = new Taxe();
				taxe1.setId(taxe.getId());
				taxe1.setAbreviation(taxe.getAbreviation());
				taxe1.setTaux(taxe.getTaux());
				taxe1.setNom(taxe.getNom());
				linePanier.setTaxe(taxe1);
			}
			linePanier.setIdPack(ligneFacture.getPack().getId());
			// System.out.println("==Designation==" + linePanier.getDesignation());
			int i = 0;
			for (FormPack pack : listFormPacks) {
				if (pack.getIdPack() == linePanier.getIdPack()) {
					linePanier.setIndexPack(i);
				}
				i++;
			}

			if (linePanier.getIndexPack() == -1) {
				Pack pack = ligneFacture.getPack();
				FormPack formPack = new FormPack();
				formPack.setIdPack(pack.getId());
				formPack.setSku(pack.getProduit().getSku());
				formPack.setNombreUnite(pack.getNombreUnite());
				formPack.setIdProduit(pack.getProduit().getIdProduit());
				formPack.setDesignation(ligneFacture.getDesignation());
				formPack.setType(pack.getProduit().getType());
				if (pack.getPrixVente() > 0) {
					formPack.setPrixVente(pack.getPrixVente());
				} else {
					formPack.setPrixVente(pack.getProduit().getPrixVente() * pack.getNombreUnite());
				}
				if (pack.getPrixAchat() > 0) {
					formPack.setPrixAchat(pack.getPrixAchat());
				} else {
					formPack.setPrixAchat(pack.getProduit().getPrixAchat() * pack.getNombreUnite());
				}

				formPack.setPrixVenteMin(ligneFacture.getPack().getPrixVenteMin());
				formPack.setCoutUMP(pack.getProduit().getCout_UMP() * pack.getNombreUnite());

				for (ProduitStockage produitStock : pack.getProduit().getProduitStockages()) {
					if (produitStock.getStockage().getIdStockage() == facture.getStockage().getIdStockage()) {
						formPack.setDisponible(produitStock.getStockDisponible() / pack.getNombreUnite());
						formPack.setReel(produitStock.getStockReel() / pack.getNombreUnite());
						formPack.setAvenir(produitStock.getStockAvenir() / pack.getNombreUnite());
					}
				}

				listFormPacks.add(formPack);
				linePanier.setIndexPack(listFormPacks.size() - 1);
			}

			if (Etat.getDelete().equals(ligneFacture.getPack().getProduit().getEtat())) {
				linePanier.setIsDelete(true);
			}

			linePaniers.add(linePanier);
		}

		formPanier.setIdFacture(facture.getIdFacture());
		formPanier.setNomClient((facture.getClient() != null) ? facture.getClient().getNom() : "");
		formPanier.setContact(
				(facture.getClient() != null) ? clientService.getClient(facture.getClient().getIdClient()) : null);
		formPanier.setCoutLivraison(facture.getCoutLivraison());
		formPanier.setDateCreation(facture.getDateCreation());
		formPanier.setDateModification(facture.getDateModification());
		formPanier.setDateLivraison(facture.getDateLivraison());
		formPanier.setEcheance(facture.getDateEcheance());
		formPanier.setDescription(facture.getDescription());
		formPanier.setIdStockage(facture.getStockage().getIdStockage());
		formPanier.setNomStockage(facture.getStockage().getNom());
		formPanier.setStockage(facture.getStockage());
		formPanier.setNote(facture.getNote());
		formPanier.setTaxe(linePaniers.get(0).getTaxe());
		formPanier.setSoumettreA(facture.getSoumettreA());
		formPanier.setUtilisateur(facture.getUtilisateur());
		formPanier.setPourcentageRemise(facture.getPourcentageRemise());

		formPanier.setStatut(facture.getStatut());
		formPanier.setColorStatut(ColorStatut.getColor(facture.getStatut()));

		if (facture.getDevi() != null) {
			formPanier.setIdDevis(facture.getDevi().getIdDevis());
		}
		if (facture.getBonCommande() != null) {
			formPanier.setIdBonCommande(facture.getBonCommande().getIdBonCommande());
			// bonCommandeService
			List<LinePanier> lines = new ArrayList<LinePanier>();
			for (LinePanier linePanier1 : linePaniers) {
				linePanier1.setQuantiteMax(
						linePanier1.getQuantite() + bonCommandeService.getMaxQuantitePackBonCommandeOnFacture(
								packRepository.findById(linePanier1.getIdPack()).get(), facture.getBonCommande()));
				// linePanier1.setDesignation("lover");
				lines.add(linePanier1);
			}

			linePaniers = lines;
			formPanier.setAddLine(false);
		}
		// formPanier.setIdDevis(devis.getIdDevis());

		// Choix de l'entreprise a afficher sur les documents
		if (!((List<Entreprise>) entrepriseRepository.findAll()).isEmpty()) {
			formPanier.setEntreprise(((List<Entreprise>) entrepriseRepository.findAll()).get(0));
		}

		List<FormPaiement> formPaiements = new ArrayList<FormPaiement>();
		for (Paiement paiement : facture.getPaiements()) {
			FormPaiement formPaiement = new FormPaiement();
			formPaiement.setIdPaiement(paiement.getId());
			formPaiement.setIdFacture(idFacture);
			formPaiement.setDatePaiement(paiement.getDatePaiement());
			formPaiement.setDescription(paiement.getDescription());
			formPaiement.setMontant(paiement.getMontant());
			formPaiement.setNomMethodePaiement(
					(paiement.getMethodePaiement() != null) ? paiement.getMethodePaiement().getNom() : null);

			formPaiements.add(formPaiement);
			formPaiement
					.setNomUtilisateur((paiement.getUtilisateur() != null) ? paiement.getUtilisateur().getNom() : null);
		}

		formPanier.setPaiements(formPaiements);
		formPanier.setLines(linePaniers);
		formPanier.setPacksPresent(listFormPacks);

		return formPanier;
	}

	public FormPaiement addPaiement(FormPaiement formPaiement) {
		Utilisateur utilisateur = utilisateurRepository.findById(formPaiement.getUtilisateur().getId()).get();
		HistoriqueCaisse historiqueCaisse = new HistoriqueCaisse();

		HistoriqueSoldeClient historiqueSoldeClient = new HistoriqueSoldeClient();

		Paiement paiement = new Paiement();
		paiement.setDatePaiement(formPaiement.getDatePaiement());
		paiement.setDescription(formPaiement.getDescription());
		paiement.setMontant(formPaiement.getMontant());
		paiement.setFacture(factureRepository.findById(formPaiement.getIdFacture()).get());
		if (!methodePaiementRepository.findByNom(formPaiement.getNomMethodePaiement()).isEmpty()) {
			paiement.setMethodePaiement(
					methodePaiementRepository.findByNom(formPaiement.getNomMethodePaiement()).get(0));
		}
		paiement.setUtilisateur(formPaiement.getUtilisateur());

		if (paiement.getMontant() > getPaiementRestant(paiement.getFacture())) {
			throw new StreengeException(new ErrorAPI("Le nouveau paeiment ne doit pas être superieur à : "
					+ getPaiementRestant(paiement.getFacture()) + " " + Data.devise));
		} else if (formPaiement.getMontant() <= 0) {
			throw new StreengeException(new ErrorAPI("Un paiement de ne peut pas être negatif ou egale a zero"));
		} else if (!Methode.stockageAccess(utilisateur, paiement.getFacture().getStockage().getIdStockage())) {
			throw new StreengeException(
					new ErrorAPI("Vous n'êtes pas habilité à ajouter un paiement sur cette facture...!"));
		}

		paiement = paiementRepository.save(paiement);
		formPaiement.setIdPaiement(paiement.getId());

		Caisse caisse = paiement.getFacture().getStockage().getCaisses().get(0);
		historiqueCaisse.setDate(new Date());
		historiqueCaisse.setIdDocument(paiement.getFacture().getIdFacture());
		historiqueCaisse.setCaisse(caisse);
		historiqueCaisse.setUtilisateur(paiement.getUtilisateur());
		historiqueCaisse.setContext("Ajout d'un paiement à la facute " + paiement.getFacture().getIdFacture());
		historiqueCaisse.setAjustement(paiement.getMontant());
		historiqueCaisse.setSolde(caisse.getSolde() + historiqueCaisse.getAjustement());
		caisse.setSolde(historiqueCaisse.getSolde());
		caisseRepository.save(caisse);
		historiqueCaisseRepository.save(historiqueCaisse);

		if (paiement.getFacture().getClient() != null) {
			Client client = paiement.getFacture().getClient();
			historiqueSoldeClient.setUtilisateur(paiement.getUtilisateur());
			historiqueSoldeClient.setDate(new Date());
			historiqueSoldeClient.setIdDocument(paiement.getFacture().getIdFacture());
			historiqueSoldeClient.setClient(client);
			historiqueSoldeClient
					.setContext("Ajout d'un paiement à la facture " + paiement.getFacture().getIdFacture());

			historiqueSoldeClient.setAjustement(paiement.getMontant());
			historiqueSoldeClient.setSolde(client.getSolde() + historiqueSoldeClient.getAjustement());
			client.setSolde(historiqueSoldeClient.getSolde());

			historiqueSoldeClientRepository.save(historiqueSoldeClient);
			clientRepository.save(client);
		}

		return formPaiement;
	}

	public FormPaiement alterPaiement(FormPaiement formPaiement) {
		Paiement paiement = paiementRepository.findById(formPaiement.getIdPaiement()).get();
		HistoriqueCaisse historiqueCaisse = new HistoriqueCaisse();
		HistoriqueSoldeClient historiqueSoldeClient = new HistoriqueSoldeClient();

		if ((formPaiement.getMontant() - paiement.getMontant()) > getPaiementRestant(paiement.getFacture())) {
			throw new StreengeException(new ErrorAPI("Le nouveau paeiment ne doit pas être superieur à : "
					+ (paiement.getMontant() + getPaiementRestant(paiement.getFacture())) + " " + Data.devise));
		} else if (formPaiement.getMontant() <= 0) {
			throw new StreengeException(new ErrorAPI("Un paiement de ne peut pas être negatif ou egale a zero"));
		} else if (!Methode.stockageAccess(formPaiement.getUtilisateur(),
				paiement.getFacture().getStockage().getIdStockage())
				|| Methode.userLevel(formPaiement.getUtilisateur()) < 2) {
			throw new StreengeException(
					new ErrorAPI("Vous n'êtes pas habilité à faire des modifications sur ce paiement...!"));
		}

		if (paiement.getMethodePaiement() != null
				&& Data.COMPTE_CLIENT.equals(paiement.getMethodePaiement().getNom())) {
			throw new StreengeException(new ErrorAPI(
					"Vous ne pouvez pas modifier ou supprimer un paiement qui a été fait de facon automatique, en débitant directement le compte du client...!"));
		}

		Caisse caisse = paiement.getFacture().getStockage().getCaisses().get(0);
		historiqueCaisse.setDate(new Date());
		historiqueCaisse.setIdDocument(paiement.getFacture().getIdFacture());
		historiqueCaisse.setCaisse(caisse);
		historiqueCaisse.setUtilisateur(formPaiement.getUtilisateur());
		historiqueCaisse.setContext("Modification d'un paiement de la facute " + paiement.getFacture().getIdFacture());
		historiqueCaisse.setAjustement(formPaiement.getMontant() - paiement.getMontant());
		historiqueCaisse.setSolde(caisse.getSolde() + historiqueCaisse.getAjustement());
		caisse.setSolde(historiqueCaisse.getSolde());
		caisseRepository.save(caisse);
		historiqueCaisseRepository.save(historiqueCaisse);

		Paiement newPaiement = new Paiement();
		newPaiement.setDatePaiement(new Date());
		newPaiement.setMethodePaiement(methodePaiementRepository.findByNom(Data.COMPTE_CLIENT).get(0));
		Facture facture = paiement.getFacture();
		if (paiement.getFacture().getClient() != null) {
			float soldeInitial = paiement.getFacture().getClient().getSolde();
			if (soldeInitial > 0 && (formPaiement.getMontant() - paiement.getMontant()) < 0) {
				newPaiement.setDatePaiement(new Date());
				newPaiement.setMethodePaiement(methodePaiementRepository.findByNom(Data.COMPTE_CLIENT).get(0));
				newPaiement.setCaisse(null);
				newPaiement.setFacture(facture);
				newPaiement.setDescription(
						"Paiement enregistrer automatiquement depuis le solde du client, suite a une reduction d'un paiement standard");
				if (soldeInitial >= (paiement.getMontant() - formPaiement.getMontant())) {
					newPaiement.setMontant((paiement.getMontant() - formPaiement.getMontant()));
					facture.setStatut(Statut.getPaye());
				} else {
					newPaiement.setMontant(soldeInitial);
					facture.setStatut(Statut.getPartiellementpaye());
				}
				paiementRepository.save(newPaiement);
			}
			Client client = paiement.getFacture().getClient();
			historiqueSoldeClient.setUtilisateur(formPaiement.getUtilisateur());
			historiqueSoldeClient.setDate(new Date());
			historiqueSoldeClient.setIdDocument(paiement.getFacture().getIdFacture());
			historiqueSoldeClient.setClient(client);
			historiqueSoldeClient
					.setContext("Modification d'un paiement de la facture " + paiement.getFacture().getIdFacture());

			historiqueSoldeClient.setAjustement(formPaiement.getMontant() - paiement.getMontant());
			historiqueSoldeClient.setSolde(client.getSolde() + historiqueSoldeClient.getAjustement());
			client.setSolde(historiqueSoldeClient.getSolde());

			historiqueSoldeClientRepository.save(historiqueSoldeClient);
			clientRepository.save(client);
		}

		paiement.setUtilisateur(formPaiement.getUtilisateur());
		paiement.setDatePaiement(formPaiement.getDatePaiement());
		paiement.setDescription(formPaiement.getDescription());
		paiement.setMontant(formPaiement.getMontant());
		if (!methodePaiementRepository.findByNom(formPaiement.getNomMethodePaiement()).isEmpty()) {
			paiement.setMethodePaiement(
					methodePaiementRepository.findByNom(formPaiement.getNomMethodePaiement()).get(0));
		}

		paiement = paiementRepository.save(paiement);
		return formPaiement;
	}

	public boolean deletePaiement(FormPaiement formPaiement) {
		HistoriqueCaisse historiqueCaisse = new HistoriqueCaisse();
		HistoriqueSoldeClient historiqueSoldeClient = new HistoriqueSoldeClient();
		Paiement paiement = paiementRepository.findById(formPaiement.getIdPaiement()).get();
		if (!Methode.stockageAccess(formPaiement.getUtilisateur(), paiement.getFacture().getStockage().getIdStockage())
				|| Methode.userLevel(formPaiement.getUtilisateur()) < 2) {
			throw new StreengeException(new ErrorAPI("Vous n'êtes pas habilité à supprimer sur ce paiement...!"));
		}

		if (paiement.getMethodePaiement() != null
				&& Data.COMPTE_CLIENT.equals(paiement.getMethodePaiement().getNom())) {
			throw new StreengeException(new ErrorAPI(
					"Vous ne pouvez pas modifier ou supprimer un paiement qui a été fait de facon automatique, en débitant directement le compte du client...!"));
		}
		Paiement newPaiement = new Paiement();
		newPaiement.setDatePaiement(new Date());
		newPaiement.setMethodePaiement(methodePaiementRepository.findByNom(Data.COMPTE_CLIENT).get(0));
		Facture facture = paiement.getFacture();
		if (paiement.getFacture().getClient() != null) {
			Client client = paiement.getFacture().getClient();
			float soldeInitial = client.getSolde();
			if (soldeInitial > 0) {
				newPaiement.setDatePaiement(new Date());
				newPaiement.setMethodePaiement(methodePaiementRepository.findByNom(Data.COMPTE_CLIENT).get(0));
				newPaiement.setCaisse(null);
				newPaiement.setFacture(facture);
				newPaiement.setDescription(
						"Paiement enregistrer automatiquement depuis le solde du client, suite a une suppression d'un paiement standard");
				if (soldeInitial >= paiement.getMontant()) {
					newPaiement.setMontant(paiement.getMontant());
					facture.setStatut(Statut.getPaye());
				} else {
					newPaiement.setMontant(soldeInitial);
					facture.setStatut(Statut.getPartiellementpaye());
				}
				paiementRepository.save(newPaiement);
			}

			historiqueSoldeClient.setUtilisateur(formPaiement.getUtilisateur());
			historiqueSoldeClient.setDate(new Date());

			historiqueSoldeClient.setIdDocument(paiement.getFacture().getIdFacture());
			historiqueSoldeClient.setClient(client);
			historiqueSoldeClient.setContext(
					"Suppression d'un paiement standard de la facture " + paiement.getFacture().getIdFacture());

			historiqueSoldeClient.setAjustement(-1 * paiement.getMontant());
			historiqueSoldeClient.setSolde(client.getSolde() + historiqueSoldeClient.getAjustement());
			client.setSolde(historiqueSoldeClient.getSolde());

			historiqueSoldeClientRepository.save(historiqueSoldeClient);
			clientRepository.save(client);
		}

		Caisse caisse = paiement.getFacture().getStockage().getCaisses().get(0);
		historiqueCaisse.setDate(new Date());
		historiqueCaisse.setIdDocument(paiement.getFacture().getIdFacture());
		historiqueCaisse.setCaisse(caisse);
		historiqueCaisse.setUtilisateur(formPaiement.getUtilisateur());
		historiqueCaisse.setContext("suppression d'un paiement de la facute " + paiement.getFacture().getIdFacture());
		historiqueCaisse.setAjustement(-1 * paiement.getMontant());
		historiqueCaisse.setSolde(caisse.getSolde() + historiqueCaisse.getAjustement());
		caisse.setSolde(historiqueCaisse.getSolde());
		caisseRepository.save(caisse);
		historiqueCaisseRepository.save(historiqueCaisse);

		paiementRepository.deletePaiement(formPaiement.getIdPaiement());
		return paiementRepository.findById(formPaiement.getIdPaiement()).isPresent();
	}

	public void controlFormPanier(FormPanier formPanier) {
		if (formPanier.getLines().isEmpty()) {
			throw new StreengeException(
					new ErrorAPI("La Facture ne peut pas être vide, elle doit contenir au moins un article"));
		}
	}

	public void updateStatutFacture(String idFacture) {
		Facture facture = new Facture();
		if (factureRepository.findById(idFacture).isPresent()) {
			facture = factureRepository.findById(idFacture).get();
			if (!Statut.getAnnuler().equals(facture.getStatut())) {
				float montantTotal = facture.getCoutLivraison();
				float montantPaye = 0;

				for (LigneFacture ligneFacture : facture.getLigneFactures()) {
					float totalHT = (ligneFacture.getPrix() * ligneFacture.getQuantite() - ligneFacture.getRemise());
					float totalTTC = totalHT;

					if (ligneFacture.getTaxe() != null) {
						totalTTC = totalHT + (totalHT * (ligneFacture.getTaxe().getTaux() / 100));
					}
					montantTotal += totalTTC;
				}

				for (Paiement paiement : facture.getPaiements()) {
					montantPaye += paiement.getMontant();
				}

				if (montantPaye == 0) {
					facture.setStatut(Statut.getNonpaye());
				} else if (montantPaye < montantTotal) {
					facture.setStatut(Statut.getPartiellementpaye());
				} else {
					facture.setStatut(Statut.getPaye());
				}

				if (getPaiementRestant(facture) < 0.1) {
					facture.setStatut(Statut.getPaye());
				}
			} ///
			facture = factureRepository.save(facture);

			if (facture.getBonCommande() != null) {
				bonCommandeService.getPanierBonCommande(facture.getBonCommande().getIdBonCommande());
			}
		}
	}

	public FormTable generateListFacture(String filter) {
		List<Facture> factures = new ArrayList<Facture>();
		List<RowTable> rows = new ArrayList<RowTable>();
		SimpleDateFormat formater = new SimpleDateFormat("dd MMMM yyyy");
		DecimalFormat df = new DecimalFormat("###,###,###,###.##");

		if (filter != null) {

			factures = factureRepository.findByClientNomStartingWithOrderByIdFactureDesc(filter);
			if (factures.size() < 1) {
				factures = factureRepository
						.findByIdFactureStartingWithOrBonCommandeIdBonCommandeStartingWithOrUtilisateurNomStartingWithOrStatutStartingWithOrderByIdFactureDesc(
								filter, filter, filter, filter);
			}

			if (factures.size() < 1) {
				factures = factureRepository.findByStockageNomStartingWithOrderByIdFactureDesc(filter);
			}
		} else {
			factures = factureRepository.findAllByOrderByIdFactureDesc();
		}

		for (Facture facture : factures) {
			if (facture.getEtat().equals(Etat.getActif())) {
				RowTable row = new RowTable();
				List<String> dataList = new ArrayList<String>();
				dataList.add(facture.getIdFacture());
				dataList.add((facture.getClient() != null) ? facture.getClient().getNom() : null);
				dataList.add(facture.getStockage().getNom());
				dataList.add(formater.format(facture.getDateCreation()));
				dataList.add((facture.getDateEcheance() != null) ? formater.format(facture.getDateEcheance()) : "");
				dataList.add(df.format(montantTotalFacture(facture)) + "  " + Data.getDevise());
				// DecimalFormat.getCurrencyInstance().format( montantTotalFacture(facture))
				row.setData(dataList);
				row.setStatut(facture.getStatut());
				row.setColorStatut(ColorStatut.getColor(facture.getStatut()));
				row.setId(facture.getIdFacture());
				row.setIdStockage(facture.getStockage().getIdStockage());
				row.setIdUtilisateur((facture.getUtilisateur() != null) ? facture.getUtilisateur().getId() : 0);
				rows.add(row);
			}
		}

		List<String> colunm = new ArrayList<String>();
		colunm.add("FACTURE #");
		colunm.add("CLIENT");
		colunm.add("LIEU DE STOCKAGE");
		colunm.add("DATE DE CRÉATION");
		colunm.add("ÉCHÉANCE");
		colunm.add("TOTAL");

		FormTable formTable = new FormTable();
		formTable.setColunm(colunm);
		formTable.setRows(rows);

		return formTable;
	}

	public float montantTotalFacture(Facture facture) {
		float montantTotal = facture.getCoutLivraison();
		for (LigneFacture ligneFacture : facture.getLigneFactures()) {
			float totalHT = (ligneFacture.getPrix() * ligneFacture.getQuantite() - ligneFacture.getRemise());
			float totalTTC = totalHT;

			if (ligneFacture.getTaxe() != null) {
				totalTTC = totalHT + (totalHT * (ligneFacture.getTaxe().getTaux() / 100));
			}
			montantTotal += totalTTC;
		}
		return montantTotal;
	}

	public float getPaiementTotalFacture(Facture facture) {
		float paiementTatal = 0;
		List<Paiement> paiements = facture.getPaiements();
		for (Paiement paiement : paiements) {
			paiementTatal += paiement.getMontant();
		}
		return paiementTatal;
	}

	public float getPaiementRestant(Facture facture) {
		return (montantTotalFacture(facture) - getPaiementTotalFacture(facture));
	}

	public void cancelFacture(String IdFacture, int idUtilisateur) {
		if (factureRepository.findById(IdFacture).isPresent()) {
			List<ProduitStockage> produitStockagesBefore = Methode
					.getProduitStockagesEager((List<ProduitStockage>) produitStockageRepository.findAll());
			Facture facture = factureRepository.findById(IdFacture).get();
			if (!facture.getStatut().equals(Statut.getAnnuler())) {

				if (facture.getBonCommande() == null) {
					for (LigneFacture ligne : facture.getLigneFactures()) {
						produitService.updateProduitQuantite(ligne.getPack(), ligne.getQuantite(),
								facture.getStockage(), true, true);
					}
				}

				float sommPaiementCompte = 0;
				float sommePaiementStandard = 0;
				List<Paiement> paiements = facture.getPaiements();
				for (Paiement paiement : paiements) {
					if (paiement.getMethodePaiement() != null
							&& Data.COMPTE_CLIENT.equals(paiement.getMethodePaiement().getNom())) {
						sommPaiementCompte += paiement.getMontant();
					} else {
						sommePaiementStandard += paiement.getMontant();
					}
					// paiement.setFacture(null);
					// paiement=paiementRepository.save(paiement);
					paiementRepository.deletePaiement(paiement.getId());
				}

				if (facture.getClient() != null) {
					HistoriqueSoldeClient historiqueSoldeClient = new HistoriqueSoldeClient();
					historiqueSoldeClient.setUtilisateur((utilisateurRepository.findById(idUtilisateur).isPresent())
							? utilisateurRepository.findById(idUtilisateur).get()
							: null);
					historiqueSoldeClient.setDate(new Date());
					historiqueSoldeClient.setIdDocument(facture.getIdFacture());

					Client client = facture.getClient();
					historiqueSoldeClient.setClient(client);
					historiqueSoldeClient.setContext("Annulation de la facture" + facture.getIdFacture());

					float paiementRestant = getPaiementRestant(facture);

					historiqueSoldeClient.setAjustement(paiementRestant + sommPaiementCompte);
					historiqueSoldeClient.setSolde(client.getSolde() + historiqueSoldeClient.getAjustement());
					client.setSolde(historiqueSoldeClient.getSolde());

					historiqueSoldeClientRepository.save(historiqueSoldeClient);
					clientRepository.save(client);

				}

				if (sommePaiementStandard > 0) {
					Caisse caisse = facture.getStockage().getCaisses().get(0);
					HistoriqueCaisse historiqueCaisse = new HistoriqueCaisse();
					historiqueCaisse.setDate(new Date());
					historiqueCaisse.setIdDocument(facture.getIdFacture());
					historiqueCaisse.setCaisse(caisse);
					historiqueCaisse.setUtilisateur((utilisateurRepository.findById(idUtilisateur).isPresent())
							? utilisateurRepository.findById(idUtilisateur).get()
							: null);
					historiqueCaisse.setContext("Annulation de la facture " + facture.getIdFacture());
					historiqueCaisse.setAjustement(-1 * sommePaiementStandard);
					historiqueCaisse.setSolde(caisse.getSolde() + historiqueCaisse.getAjustement());
					caisse.setSolde(historiqueCaisse.getSolde());
					caisseRepository.save(caisse);
					historiqueCaisseRepository.save(historiqueCaisse);
				}

				facture.setStatut(Statut.getAnnuler());
				factureRepository.save(facture);
				if (sommPaiementCompte > 0 && facture.getClient() != null) {
					clientService.balancementCompteClient(facture.getClient(), sommPaiementCompte,
							"Reafectation des paiements de type compte client suite à l'annulation d'une facture qui contenait des paiement de type compte client");
				}
				registreStockService.updateRegistreStock(produitStockagesBefore, "Annulation d'une facture",
						idUtilisateur, IdFacture);
			}
		}
	}

	public void reactiveFacture(String idFacture, int idUtilisateur) {
		if (factureRepository.findById(idFacture).isPresent()) {
			List<ProduitStockage> produitStockagesBefore = Methode
					.getProduitStockagesEager((List<ProduitStockage>) produitStockageRepository.findAll());
			Facture facture = factureRepository.findById(idFacture).get();
			if (facture.getBonCommande() == null && facture.getStatut().equals(Statut.getAnnuler())) {
				for (LigneFacture ligne : facture.getLigneFactures()) {
					produitService.updateProduitQuantite(ligne.getPack(), (ligne.getQuantite() * -1),
							facture.getStockage(), true, true);
				}
			} else if (facture.getBonCommande() != null && facture.getStatut().equals(Statut.getAnnuler())) {
				if (facture.getBonCommande().getStatut().equals(Statut.getAnnuler())) {
					throw new StreengeException(new ErrorAPI(
							"Il est impossible de Reactiver cette facture car commande mère a été annuler, reactiver d'abord la commnade mère...!"));
				}
				for (LigneFacture ligneFacture : facture.getLigneFactures()) {
					if (ligneFacture.getQuantite() > bonCommandeService
							.getMaxQuantitePackBonCommandeOnFacture(ligneFacture.getPack(), facture.getBonCommande())) {
						throw new StreengeException(new ErrorAPI(
								"Il est impossible de Reactiver cette facture car certains produit on deja été totalement facturer dans d'autre facture de la commande mère"));
					}
				}
			}
			facture.setStatut(Statut.getNonpaye());
			factureRepository.save(facture);
			registreStockService.updateRegistreStock(produitStockagesBefore, "Reactivation d'une facture",
					idUtilisateur, idFacture);

			if (facture.getClient() != null && getPaiementRestant(facture) > 0) {
				HistoriqueSoldeClient historiqueSoldeClient = new HistoriqueSoldeClient();
				historiqueSoldeClient.setUtilisateur((utilisateurRepository.findById(idUtilisateur).isPresent())
						? utilisateurRepository.findById(idUtilisateur).get()
						: null);
				historiqueSoldeClient.setDate(new Date());
				historiqueSoldeClient.setIdDocument(facture.getIdFacture());

				Client client = facture.getClient();
				historiqueSoldeClient.setClient(client);
				historiqueSoldeClient.setContext("Réactivation de la facture " + facture.getIdFacture());
				if (client.getSolde() > 0) {
					Paiement paiement = new Paiement();
					paiement.setDatePaiement(new Date());
					paiement.setMethodePaiement(methodePaiementRepository.findByNom(Data.COMPTE_CLIENT).get(0));
					paiement.setCaisse(null);
					paiement.setFacture(facture);
					paiement.setDescription(
							"Paiement enregistrer automatiquement depuis le solde du client suite à la reactivation de cette facture...!");
					if (client.getSolde() >= getPaiementRestant(facture)) {
						paiement.setMontant(getPaiementRestant(facture));
						historiqueSoldeClient
								.setContext("Réactivation et Paiement Total de la facture " + facture.getIdFacture());
						facture.setStatut(Statut.getPaye());
					} else {
						paiement.setMontant(client.getSolde());
						historiqueSoldeClient.setContext(
								"Réactivation et Paiement partielle de la facture " + facture.getIdFacture());
						facture.setStatut(Statut.getPartiellementpaye());
					}
					paiementRepository.save(paiement);
					factureRepository.save(facture);
				}
				historiqueSoldeClient.setAjustement(-1 * getPaiementRestant(facture));
				historiqueSoldeClient.setSolde(client.getSolde() + historiqueSoldeClient.getAjustement());
				client.setSolde(historiqueSoldeClient.getSolde());

				historiqueSoldeClientRepository.save(historiqueSoldeClient);
				clientRepository.save(client);
			}

		}
	}

	public void deleteFacture(String idFacture, int idUtilisateur) {
		if (factureRepository.findById(idFacture).isPresent()) {
			Facture facture = factureRepository.findById(idFacture).get();
			if (!facture.getEtat().equals(Etat.getDelete())) {
				this.cancelFacture(idFacture, idUtilisateur);
				facture.setBonCommande(null);
				facture.setDevi(null);
				facture.setEtat(Etat.getDelete());
				factureRepository.save(facture);
			}
		}
	}

	public void manageFacturePaiementOnCreationAndUpdate(Boolean isNewFacture, Utilisateur utilisateur, Facture facture,
			float previousRestantPaye, Stockage previousStockage, Client previousClient) {
		if (methodePaiementRepository.findByNom(Data.COMPTE_CLIENT).isEmpty()) {
			MethodePaiement methodePaiement02 = new MethodePaiement();
			methodePaiement02.setNom(Data.COMPTE_CLIENT);
			methodePaiementRepository.save(methodePaiement02);
		}

		// historiqueCaisse.setIdDocument(facture.getIdFacture());
		HistoriqueSoldeClient historiqueSoldeClient = new HistoriqueSoldeClient();
		historiqueSoldeClient.setUtilisateur(utilisateur);
		historiqueSoldeClient.setDate(new Date());
		historiqueSoldeClient.setIdDocument(facture.getIdFacture());

		// celui est utiliser pour le previaows client , j'ai rencontrer un proleme
		// lorsque on modifie une facture d'un client existant pour un nouveau client,
		// le solde du precedent client est bien modified mais aucune ligne n'est
		// ajouter a son histoique
		HistoriqueSoldeClient historiqueSoldeClient02 = new HistoriqueSoldeClient();
		historiqueSoldeClient02.setUtilisateur(utilisateur);
		historiqueSoldeClient02.setDate(new Date());
		historiqueSoldeClient02.setIdDocument(facture.getIdFacture());

		if (isNewFacture) {
			if (facture.getClient() != null) {
				Client client = facture.getClient();
				historiqueSoldeClient.setClient(client);
				historiqueSoldeClient.setContext("Create de la facture " + facture.getIdFacture());
				if (client.getSolde() > 0) {
					Paiement paiement = new Paiement();
					paiement.setDatePaiement(new Date());
					paiement.setMethodePaiement(methodePaiementRepository.findByNom(Data.COMPTE_CLIENT).get(0));
					paiement.setCaisse(null);
					paiement.setFacture(facture);
					paiement.setDescription("Paiement enregistrer automatiquement depuis le solde du client");
					if (client.getSolde() >= montantTotalFacture(facture)) {
						paiement.setMontant(montantTotalFacture(facture));
						historiqueSoldeClient
								.setContext("Creation et Paiement Total de la facture " + facture.getIdFacture());
						facture.setStatut(Statut.getPaye());
					} else {
						paiement.setMontant(client.getSolde());
						historiqueSoldeClient
								.setContext("Creation et Paiement partielle de la facture " + facture.getIdFacture());
						facture.setStatut(Statut.getPartiellementpaye());
					}
					paiementRepository.save(paiement);
					factureRepository.save(facture);
				}
				historiqueSoldeClient.setAjustement(-1 * montantTotalFacture(facture));
				historiqueSoldeClient.setSolde(client.getSolde() + historiqueSoldeClient.getAjustement());
				client.setSolde(historiqueSoldeClient.getSolde());

				historiqueSoldeClientRepository.save(historiqueSoldeClient);
				clientRepository.save(client);
			}
		} else {
			Boolean isDifferentClient = false;

			String idPreviousClient = (previousClient != null) ? previousClient.getIdClient() : "idisnullopqs1l5";
			String idClient = (facture.getClient() != null) ? facture.getClient().getIdClient() : "idisnullopqs1l5";

			isDifferentClient = !idClient.equals(idPreviousClient);

			// ici on control si c'est le meme client, si oui, alors on verifie si la
			// modification de la facture impacte le cout total,
			// si l'impact de la modification a augmente ou diminue le coût total de la
			// facture;
			// si le cout augmenter; on controle le solde du s'il est positf, on essaie donc
			// d'ajouter un nouveau paiemente de type "Compte client" avec l'objet type
			// si le cout le coût dimimue alors on regarde parmie le paiement, s'il y'a des
			// paiement de type(methode) "Compte client" alors, on essai de reduire ces
			// paiement en remetant l'excedant dans le comte du client
			if (!isDifferentClient && previousClient != null) {
				float paiementRestant = getPaiementRestant(facture);
				// float totalDejePaye = getPaiementTotalFacture(facture);

				if (paiementRestant >= 0) {
					Client client = facture.getClient();
					historiqueSoldeClient.setClient(client);
					historiqueSoldeClient.setContext("Ajout d'un paiement a la facture " + facture.getIdFacture()
							+ " suite à ca modification qui a augmenté sa valeur");
					if (client.getSolde() > 0) {
						Paiement paiement = new Paiement();
						paiement.setDatePaiement(new Date());
						paiement.setMethodePaiement(methodePaiementRepository.findByNom(Data.COMPTE_CLIENT).get(0));
						paiement.setCaisse(null);
						paiement.setFacture(facture);
						paiement.setDescription(
								"Paiement enregistrer automatiquement depuis le solde du client apres la modification de la facture");
						if (client.getSolde() >= getPaiementRestant(facture)) {
							paiement.setMontant(getPaiementRestant(facture));
							historiqueSoldeClient.setContext("Paiement Total de la facture " + facture.getIdFacture()
									+ " suite à sa modification");
							facture.setStatut(Statut.getPaye());
						} else {
							paiement.setMontant(client.getSolde());
							historiqueSoldeClient.setContext("Paiement partielle de la facture "
									+ facture.getIdFacture() + "suite à sa modification");
							facture.setStatut(Statut.getPartiellementpaye());
						}
						paiementRepository.save(paiement);
						factureRepository.save(facture);
					}
					historiqueSoldeClient.setAjustement(-1 * getPaiementRestant(facture) + previousRestantPaye);
					// ici, on ajoute le previosRestantPayer, pour compence avec l'ajustement
					// initial qui a été effectuer lors de la creation de la facture ou lors de la
					// derniere modification(modification des payement ou meme de la factures)
					historiqueSoldeClient.setSolde(client.getSolde() + historiqueSoldeClient.getAjustement());
					client.setSolde(historiqueSoldeClient.getSolde());

					historiqueSoldeClientRepository.save(historiqueSoldeClient);
					clientRepository.save(client);
				} else {
					Client client = facture.getClient();
					historiqueSoldeClient.setClient(client);
					historiqueSoldeClient.setContext("reajustement paiements de la facture " + facture.getIdFacture()
							+ " suite à une modification qui a réduit son montant");

					float sommeRestore = -1 * paiementRestant;
					float sommPaiementCompte = 0;
					List<Paiement> paiements02 = facture.getPaiements();
					for (Paiement paiement : paiements02) {
						if (paiement.getMethodePaiement() != null
								&& Data.COMPTE_CLIENT.equals(paiement.getMethodePaiement().getNom())) {
							sommPaiementCompte += paiement.getMontant();
							paiementRepository.deletePaiement(paiement.getId());
						}
					}
					if (sommPaiementCompte > sommeRestore) {
						Paiement paiement = new Paiement();
						paiement.setDatePaiement(new Date());
						paiement.setMethodePaiement(methodePaiementRepository.findByNom(Data.COMPTE_CLIENT).get(0));
						paiement.setCaisse(null);
						paiement.setFacture(facture);
						paiement.setDescription(
								"Paiement enregistrer suite à une modification de la facture qui a reduit sa valeur alors que celle ci disposer deja de paiement realiser depuis le compte client");
						paiement.setMontant(sommPaiementCompte - sommeRestore);
						historiqueSoldeClient.setAjustement(1 * sommeRestore + previousRestantPaye);
						historiqueSoldeClient.setSolde(client.getSolde() + historiqueSoldeClient.getAjustement());
						client.setSolde(historiqueSoldeClient.getSolde());

						paiementRepository.save(paiement);
						historiqueSoldeClientRepository.save(historiqueSoldeClient);
						clientRepository.save(client);
						clientService.balancementCompteClient(client, (sommeRestore),
								"Reafectation des paiements de type compte client suite a une modification d'une facture qui a reduit son montant");
					} else if (sommPaiementCompte > 0) {
						historiqueSoldeClient.setAjustement(1 * sommPaiementCompte + previousRestantPaye);
						historiqueSoldeClient.setSolde(client.getSolde() + historiqueSoldeClient.getAjustement());
						client.setSolde(historiqueSoldeClient.getSolde());

						historiqueSoldeClientRepository.save(historiqueSoldeClient);
						clientRepository.save(client);

						clientService.balancementCompteClient(client, sommPaiementCompte,
								"Reafectation des paiements de type compte client suite a une modification d'une facture qui a reduit son montant");
					}
				}

			} else if (isDifferentClient && previousClient != null) {
				Client client = previousClient;
				historiqueSoldeClient02.setClient(client);
				historiqueSoldeClient02.setContext("reajustement du solde client  suite à la reactribution de la facture "
						+ facture.getIdFacture() + " à un autre client");

				float sommPaiementCompte = 0;
				List<Paiement> paiements01 = facture.getPaiements();
				for (Paiement paiement : paiements01) {
					if (paiement.getMethodePaiement() != null
							&& Data.COMPTE_CLIENT.equals(paiement.getMethodePaiement().getNom())) {
						sommPaiementCompte += paiement.getMontant();
						paiementRepository.deletePaiement(paiement.getId());
					}
				}
 
				historiqueSoldeClient02.setAjustement(1 * sommPaiementCompte + previousRestantPaye);
				historiqueSoldeClient02.setSolde(client.getSolde() + historiqueSoldeClient02.getAjustement());
				client.setSolde(historiqueSoldeClient02.getSolde());

				historiqueSoldeClientRepository.save(historiqueSoldeClient02);
				clientRepository.save(client);

				clientService.balancementCompteClient(client, sommPaiementCompte,
						"Reafectation des paiements de type compte client suite a une reatribution d'une facture à un autre client...!");
			}

			if ((previousClient == null || isDifferentClient) && facture.getClient() != null) {
				Client client = facture.getClient();
				historiqueSoldeClient.setContext("Reactribution de la facture " + facture.getIdFacture());
				historiqueSoldeClient.setClient(client);
				if (client.getSolde() > 0) {
					Paiement paiement = new Paiement();
					paiement.setDatePaiement(new Date());
					paiement.setMethodePaiement(methodePaiementRepository.findByNom(Data.COMPTE_CLIENT).get(0));
					paiement.setCaisse(null);
					paiement.setFacture(facture);
					paiement.setDescription(
							"Paiement enregistrer automatiquement depuis le solde du client apres reactribution de la facture");
					if (client.getSolde() >= getPaiementRestant(facture)) {
						paiement.setMontant(getPaiementRestant(facture));
						historiqueSoldeClient
								.setContext("Reactribution et Paiement Total de la facture " + facture.getIdFacture());
						facture.setStatut(Statut.getPaye());
					} else {
						paiement.setMontant(client.getSolde());
						historiqueSoldeClient.setContext(
								"Reactribution et Paiement partielle de la facture " + facture.getIdFacture());
						facture.setStatut(Statut.getPartiellementpaye());
					}
					paiementRepository.save(paiement);
					factureRepository.save(facture);
				}
				historiqueSoldeClient.setAjustement(-1 * getPaiementRestant(facture));
				historiqueSoldeClient.setSolde(client.getSolde() + historiqueSoldeClient.getAjustement());
				client.setSolde(historiqueSoldeClient.getSolde());

				historiqueSoldeClientRepository.save(historiqueSoldeClient);
				clientRepository.save(client);
			}
		}

		if (facture.getPaiements() != null && !facture.getPaiements().isEmpty()) {
			List<Paiement> paiements = facture.getPaiements();
			for (Paiement paiement : paiements) {
				if (paiement.getMontant() == 0 || paiement.getMontant() < 0.01) {
					paiementRepository.deletePaiement(paiement.getId());
				}
			}
		}

		if (!isNewFacture && previousStockage != null
				&& facture.getStockage().getIdStockage() != previousStockage.getIdStockage()) {

			float sommePaiementStandard = 0;
			for (Paiement paiement : facture.getPaiements()) {
				if (!(paiement.getMethodePaiement() != null
						&& Data.COMPTE_CLIENT.equals(paiement.getMethodePaiement().getNom()))) {
					sommePaiementStandard += paiement.getMontant();
				}
			}

			Caisse newCaisse = facture.getStockage().getCaisses().get(0);
			Caisse previowsCaisse = previousStockage.getCaisses().get(0);

			HistoriqueCaisse historiqueNewCaisse = new HistoriqueCaisse();
			historiqueNewCaisse.setDate(new Date());
			historiqueNewCaisse.setIdDocument(facture.getIdFacture());
			historiqueNewCaisse.setCaisse(newCaisse);
			historiqueNewCaisse.setUtilisateur(utilisateur);
			historiqueNewCaisse.setContext("Changement d'agence (stockage) de la facture " + facture.getIdFacture());
			historiqueNewCaisse.setAjustement(1 * sommePaiementStandard);
			historiqueNewCaisse.setSolde(newCaisse.getSolde() + historiqueNewCaisse.getAjustement());
			newCaisse.setSolde(historiqueNewCaisse.getSolde());

			// ici on cree une fois l'historique de caisse pour le pour l'aciene caisse ou
			// les paiement standart avait éte enegistrer, pour eviter les potentiel
			// probleme de cle

			caisseRepository.save(newCaisse);
			// historiqueCaisseRepository.save(historiqueNewCaisse);

			HistoriqueCaisse historiquePreviowCaisse = new HistoriqueCaisse();
			historiquePreviowCaisse.setDate(new Date());
			historiquePreviowCaisse.setIdDocument(facture.getIdFacture());
			historiquePreviowCaisse.setCaisse(previowsCaisse);
			historiquePreviowCaisse.setUtilisateur(utilisateur);
			historiquePreviowCaisse
					.setContext("Changement d'agence (stockage) de la facture " + facture.getIdFacture());
			historiquePreviowCaisse.setAjustement(-1 * sommePaiementStandard);
			historiquePreviowCaisse.setSolde(previowsCaisse.getSolde() + historiquePreviowCaisse.getAjustement());
			previowsCaisse.setSolde(historiquePreviowCaisse.getSolde());

			caisseRepository.save(previowsCaisse);

			List<HistoriqueCaisse> listhHistoriqueCaisses = new ArrayList<>();
			listhHistoriqueCaisses.add(historiqueNewCaisse);
			listhHistoriqueCaisses.add(historiquePreviowCaisse);
			historiqueCaisseRepository.saveAll(listhHistoriqueCaisses);
		}

	}
}
