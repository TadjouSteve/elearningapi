/**
 * 
 */
package com.streenge.service.venteService;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.streenge.model.LigneDocument;
import com.streenge.model.admin.Entreprise;
import com.streenge.model.admin.Taxe;
import com.streenge.model.admin.Utilisateur;
import com.streenge.model.produit.Pack;
import com.streenge.model.produit.ProduitStockage;
import com.streenge.model.vente.BonCommande;
import com.streenge.model.vente.BonLivraison;
import com.streenge.model.vente.facture.Facture;
import com.streenge.model.vente.facture.LigneFacture;
import com.streenge.repository.LigneDocumentRepository;
import com.streenge.repository.adminRepository.EntrepriseRepository;
import com.streenge.repository.adminRepository.StockageRepository;
import com.streenge.repository.adminRepository.TaxeRepository;
import com.streenge.repository.adminRepository.UtilisateurRepository;
import com.streenge.repository.contactRepo.ClientRepository;
import com.streenge.repository.produitRepo.PackRepository;
import com.streenge.repository.produitRepo.ProduitStockageRepository;
import com.streenge.repository.venteRepo.BonCommandeRepository;
import com.streenge.repository.venteRepo.DevisRepository;
import com.streenge.service.contactService.ClientService;
import com.streenge.service.produitService.ProduitService;
import com.streenge.service.stockService.RegistreStockService;
import com.streenge.service.utils.erroclass.ErrorAPI;
import com.streenge.service.utils.erroclass.StreengeException;
import com.streenge.service.utils.formInt.FormPanier;
import com.streenge.service.utils.formInt.LinePanier;
import com.streenge.service.utils.formOut.FormBonCommande;
import com.streenge.service.utils.formOut.FormPack;
import com.streenge.service.utils.formOut.FormTable;
import com.streenge.service.utils.formOut.RowTable;
import com.streenge.service.utils.streengeData.ColorStatut;
import com.streenge.service.utils.streengeData.Data;
import com.streenge.service.utils.streengeData.Etat;
import com.streenge.service.utils.streengeData.Profil;
import com.streenge.service.utils.streengeData.Statut;
import com.streenge.service.utils.streengeFunction.Methode;

/**
 * @author STEVE
 *
 */
@Service
public class BonCommandeService {

	@Autowired
	private BonCommandeRepository bonCommandeRepository;

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
	private DevisRepository devisRepository;

	@Autowired
	private ProduitService produitService;

	@Autowired
	private LigneDocumentRepository ligneDocumentRepository;

	@Autowired
	private ClientService clientService;

	@Autowired
	private FactureService factureService;

	@Autowired
	private BonLivraisonService bonLivraisonService;

	@Autowired
	private RegistreStockService registreStockService;

	@Autowired
	private ProduitStockageRepository produitStockageRepository;

	@Autowired
	private UtilisateurRepository utilisateurRepository;

	public FormTable generateListBonCommande(String filter) {
		List<BonCommande> listBonCommandes = new ArrayList<BonCommande>();
		List<RowTable> rows = new ArrayList<RowTable>();
		SimpleDateFormat formater = new SimpleDateFormat("dd MMMM yyyy");
		DecimalFormat df = new DecimalFormat("###,###,###,###.##");

		if (filter != null) {
			listBonCommandes = bonCommandeRepository.findByClientNomStartingWithOrderByIdBonCommandeDesc(filter);
			if (listBonCommandes.size() < 1) {
				listBonCommandes = bonCommandeRepository
						.findByIdBonCommandeStartingWithOrUtilisateurNomStartingWithOrStatutStartingWithOrderByIdBonCommandeDesc(
								filter, filter, filter);
			}

			if (listBonCommandes.size() < 1) {
				listBonCommandes = bonCommandeRepository.findByStockageNomStartingWithOrderByIdBonCommandeDesc(filter);
			}
		} else {
			listBonCommandes = bonCommandeRepository.findAllByOrderByIdBonCommandeDesc();
		}

		for (BonCommande bonCommande : listBonCommandes) {
			if (bonCommande.getEtat().equals(Etat.getActif())) {

				RowTable row = new RowTable();
				List<String> dataList = new ArrayList<String>();
				dataList.add(bonCommande.getIdBonCommande());
				dataList.add((bonCommande.getClient() != null) ? bonCommande.getClient().getNom() : null);
				dataList.add(bonCommande.getStockage().getNom());
				dataList.add(formater.format(bonCommande.getDateCreation()));
				dataList.add(df.format(Methode.getQuantiteTotalDocument(bonCommande.getLigneDocuments())));
				dataList.add(df.format(Methode.getMomtantTolalDocument(bonCommande.getLigneDocuments(),
						bonCommande.getCoutLivraison())) + " " + Data.getDevise());
				row.setData(dataList);
				row.setProgressList(getHashMapProgressBonCommande(bonCommande));
				row.setStatut(bonCommande.getStatut());
				row.setColorStatut(ColorStatut.getColor(bonCommande.getStatut()));
				row.setId(bonCommande.getIdBonCommande());
				row.setIdStockage(bonCommande.getStockage().getIdStockage());
				row.setIdUtilisateur((bonCommande.getUtilisateur() != null) ? bonCommande.getUtilisateur().getId() : 0);
				rows.add(row);

			}
		}

		List<String> colunm = new ArrayList<String>();
		colunm.add("BON COMMANDE #");
		colunm.add("CLIENT");
		colunm.add("LIEU DE STOCKAGE");
		colunm.add("DATE DE CRÉATION");
		colunm.add("QUANTITÉS");
		colunm.add("TOTAL");

		FormTable formTable = new FormTable();
		// formTable.setNumber(listBonCommandes.size());
		formTable.setColunm(colunm);
		formTable.setRows(rows);

		return formTable;
	}

	private HashMap<String, Float> getHashMapProgressBonCommande(BonCommande bonCommande) {
		FormBonCommande formBonCommande = getPanierBonCommande(bonCommande.getIdBonCommande());

		float quantiteLivre = 0;
		float quantiteTotal = Methode.getQuantiteTotalDocument(bonCommande.getLigneDocuments());

		HashMap<String, Float> progressList = new HashMap<>();

		for (BonLivraison bonLivraison : bonCommande.getBonLivraisons()) {
			if (!Etat.getDelete().equals(bonLivraison.getEtat())
					&& Statut.getLivre().equals(bonLivraison.getStatut())) {
				quantiteLivre += Methode.getQuantiteTotalDocument(bonLivraison.getLigneDocuments());
			}
		}
		progressList.put("Livré", ((quantiteLivre / quantiteTotal) * 100));

		float totalFacture = 0;
		float totalPaye = 0;

		for (Facture facture : bonCommande.getFactures()) {
			if (!Etat.getDelete().equals(facture.getEtat()) && !Statut.getAnnuler().equals(facture.getStatut())) {
				totalFacture += factureService.montantTotalFacture(facture);
				totalPaye += factureService.getPaiementTotalFacture(facture);
			}
		}

		progressList.put("Facturé", ((totalFacture / (totalFacture + formBonCommande.getRestantToFacture())) * 100));
		progressList.put("Payé", ((totalPaye / (totalFacture + formBonCommande.getRestantToFacture())) * 100));

		return progressList;
	}

	public BonCommande createOrUpdateBonCommande(FormPanier formPanier, boolean isNewBonCommande) {
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
		if (utilisateur!=null && utilisateur.getPassword().isBlank()) {
			utilisateur.setPassword(".");
		}
		if (utilisateur != null && utilisateur.getProfil() != Profil.getRoot()
				&& utilisateurRepository.findById(utilisateur.getId()).isPresent() && utilisateur.getPassword()
						.equals(utilisateurRepository.findById(utilisateur.getId()).get().getPassword())) {
			utilisateur = utilisateurRepository.findById(utilisateur.getId()).get();
			formPanier.setUtilisateur(utilisateur);
		} else {
			throw new StreengeException(
					new ErrorAPI("Impossible de creer/modifier ce bon de commande...! Utilisateur Non defini..!"));
		}
		BonCommande bonCommande;
		if (isNewBonCommande) {
			bonCommande = new BonCommande();
			bonCommande.setIdBonCommande(Methode.createID(bonCommandeRepository, "BC"));
			formPanier.setDateCreation(new Date());
			bonCommande.setUtilisateur(formPanier.getUtilisateur());
			if (!formPanier.getIdDevis().isBlank()) {
				// if (devisRepository.findById(formPanier.getIdDevis()).isPresent())
				bonCommande.setDevi(devisRepository.findById(formPanier.getIdDevis()).get());

				// bonCommandeRepository.findById(formPanier.getIdDevis()).get());
			}
		} else {
			bonCommande = bonCommandeRepository.findById(formPanier.getIdBonCommande().toUpperCase()).get();
			controlUpdateBonCommande(formPanier, bonCommande);
			for (LigneDocument ligne : bonCommande.getLigneDocuments()) {
				produitService.updateProduitQuantite(ligne.getPack(), ligne.getQuantite(), bonCommande.getStockage(),
						true, false);
				ligneDocumentRepository.deleteLigneDocument(ligne.getId());
				bonCommande.setDateModification(new Date());
			}
		}
		if (!clientRepository.findByNom(formPanier.getNomClient()).isEmpty()) {
			bonCommande.setClient(clientRepository.findByNom(formPanier.getNomClient()).get(0));
		}
		// set
		// facture.setConditionVente(formPanier.getNote());
		bonCommande.setNote(formPanier.getNote());
		bonCommande.setCoutLivraison(formPanier.getCoutLivraison());
		bonCommande.setDateCreation(formPanier.getDateCreation());
		// devi.setDateExpiration(formPanier.getDateExpiration());
		bonCommande.setDateLivraison(formPanier.getDateLivraison());
		bonCommande.setDescription(formPanier.getDescription());
		bonCommande.setSoumettreA(formPanier.getSoumettreA());
		bonCommande.setPourcentageRemise(formPanier.getPourcentageRemise());

		if (isNewBonCommande) {
			bonCommande.setEtat(Etat.getActif());
			bonCommande.setStatut(Statut.getActif());
		}
		if (stockageRepository.findById(formPanier.getIdStockage()).isPresent()) {
			bonCommande.setStockage(stockageRepository.findById(formPanier.getIdStockage()).get());
		} else {
			ErrorAPI errorAPI = new ErrorAPI();
			errorAPI.setMessage(
					"L'enregistrement du bon de commande n'est pas possible, Le stockage choisi N'existe pas...! Changer de lieu de stockage et cree une nouvelle facture..! ");
			throw new StreengeException(errorAPI);
		}

		List<LigneDocument> ligneDocuments = new ArrayList<LigneDocument>();
		List<LinePanier> lines = formPanier.getLines();
		for (LinePanier line : lines) {

			LigneDocument ligneBonCommane = new LigneDocument();
			ligneBonCommane.setBonCommande(bonCommande);
			ligneBonCommane.setDesignation(line.getDesignation());
			ligneBonCommane.setPrix(line.getPrix());
			ligneBonCommane.setQuantite(line.getQuantite());
			ligneBonCommane.setRemise(line.getRemise());

			if (line.getTaxe() != null)
				ligneBonCommane.setTaxe(taxeRepository.findById(line.getTaxe().getId()).get());

			if (packRepository.findById(line.getIdPack()).isPresent()) {
				ligneBonCommane.setPack(packRepository.findById(line.getIdPack()).get());
				ligneBonCommane.setCout_UMP(ligneBonCommane.getPack().getProduit().getCout_UMP()
						* ligneBonCommane.getPack().getNombreUnite());
			} else {
				ErrorAPI errorAPI = new ErrorAPI();
				errorAPI.setMessage("L'enregistrement du bon de commande n'est pas possible,Le produit: "
						+ line.getDesignation().toUpperCase()
						+ " N'existe pas où alors ce produit à été récemment supprimer");
				throw new StreengeException(errorAPI);
			}
			ligneDocuments.add(ligneBonCommane);
		}

		bonCommande.setLigneDocuments(ligneDocuments);

		for (LigneDocument ligne : ligneDocuments) {
			produitService.updateProduitQuantite(ligne.getPack(), (ligne.getQuantite() * -1), bonCommande.getStockage(),
					true, false);
		}

		// ici on met ajour le client sur les facture au cas lors de lamodification de
		// du bon de commande l'utilsateur decide de changer le client ou le stockage
		if (!isNewBonCommande) {
			for (int i = 0; i < bonCommande.getFactures().size(); i++) {
				Facture facture = bonCommande.getFactures().get(i);
				FormPanier formPanierFacture = factureService.getPanierFacture(facture.getIdFacture());
				formPanierFacture.setNomClient(formPanier.getNomClient());
				formPanierFacture.setIdStockage(formPanier.getIdStockage());
				factureService.createOrUpdateFacture(formPanierFacture, false);
				/*
				 * facture.setClient(bonCommande.getClient());
				 * facture.setStockage(bonCommande.getStockage());
				 * factureRepository.save(facture);
				 */
			}
		}

		try {
			bonCommande = bonCommandeRepository.save(bonCommande);
		} catch (DataIntegrityViolationException e) {
			System.out.println("BonComande already exist");
		}

		registreStockService.updateRegistreStock(produitStockagesBefore,
				isNewBonCommande ? "Création du bon de commande" : "Modification du bon de commande", utilisateur,
				bonCommande.getIdBonCommande());

		return bonCommande;
	}

	public FormBonCommande getPanierBonCommande(String idBonCommande) {
		// updateStatutFacture(idFacture);
		FormBonCommande formBonCommande = new FormBonCommande();
		List<LinePanier> linePaniers = new ArrayList<LinePanier>();
		List<FormPack> listFormPacks = new ArrayList<FormPack>();
		// List<FormProduit> listFormProduits = new ArrayList<FormProduit>();

		LinePanier linePanier = null;
		BonCommande bonCommande = bonCommandeRepository.findById(idBonCommande.toUpperCase()).get();

		// List<LigneDocument> ligneBonCommandes = bonCommande.getLigneDocuments();
		for (LigneDocument ligneBonCommande : bonCommande.getLigneDocuments()) {
			linePanier = new LinePanier();

			linePanier.setDesignation(ligneBonCommande.getDesignation());
			linePanier.setPrix(ligneBonCommande.getPrix());
			linePanier.setPrixMin(ligneBonCommande.getPack().getPrixVenteMin());
			linePanier.setQuantite(ligneBonCommande.getQuantite());
			linePanier.setRemise(ligneBonCommande.getRemise());
			linePanier.setRestant(getMaxQuantitePackBonCommandeOnBonLivraison(ligneBonCommande.getPack(), bonCommande));
			// on utilise une fois cette boucle pour terminer le nombre d'article qui n'as
			// pas ete facture et prix total de ces dernier, DE CEETE FACON ON EST PAS
			// INFLUANCER PR LE FAIT QUE L'UTILISATEUR PUISSE CHANGER LE PRIX DES ARTICLE
			// LORS DES FACTURATIONS
			formBonCommande.setRestantToFacture(formBonCommande.getRestantToFacture()
					+ (getMaxQuantitePackBonCommandeOnFacture(ligneBonCommande.getPack(), bonCommande)
							* linePanier.getPrix()));
			linePanier.setQuantiteMin(getMinQuantitePackBonCommande(ligneBonCommande.getPack(), bonCommande));
			if (ligneBonCommande.getTaxe() != null) {
				Taxe taxe = ligneBonCommande.getTaxe();
				Taxe taxe1 = new Taxe();
				taxe1.setId(taxe.getId());
				taxe1.setAbreviation(taxe.getAbreviation());
				taxe1.setTaux(taxe.getTaux());
				taxe1.setNom(taxe.getNom());
				linePanier.setTaxe(taxe1);
			}
			linePanier.setIdPack(ligneBonCommande.getPack().getId());
			/// System.out.println("==Designation==" + linePanier.getDesignation());
			int i = 0;
			for (FormPack pack : listFormPacks) {
				if (pack.getIdPack() == linePanier.getIdPack()) {
					linePanier.setIndexPack(i);
				}
				i++;
			}

			if (linePanier.getIndexPack() == -1) {
				Pack pack = ligneBonCommande.getPack();

				FormPack formPack = new FormPack();
				formPack.setIdPack(pack.getId());
				formPack.setSku(pack.getProduit().getSku());
				formPack.setNombreUnite(pack.getNombreUnite());
				formPack.setIdProduit(pack.getProduit().getIdProduit());
				formPack.setDesignation(ligneBonCommande.getDesignation());
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

				formPack.setPrixVenteMin(ligneBonCommande.getPack().getPrixVenteMin());
				formPack.setCoutUMP(pack.getProduit().getCout_UMP() * pack.getNombreUnite());

				for (ProduitStockage produitStock : pack.getProduit().getProduitStockages()) {
					if (produitStock.getStockage().getIdStockage() == bonCommande.getStockage().getIdStockage()) {
						formPack.setDisponible(produitStock.getStockDisponible() / pack.getNombreUnite());
						formPack.setReel(produitStock.getStockReel() / pack.getNombreUnite());
						formPack.setAvenir(produitStock.getStockAvenir() / pack.getNombreUnite());
					}
				}

				listFormPacks.add(formPack);
				linePanier.setIndexPack(listFormPacks.size() - 1);
			}
			
			if (Etat.getDelete().equals(ligneBonCommande.getPack().getProduit().getEtat())) {
				linePanier.setIsDelete(true);
			}

			linePaniers.add(linePanier);
		}

		formBonCommande.setIdBonCommande(bonCommande.getIdBonCommande());
		formBonCommande.setNomClient((bonCommande.getClient() != null) ? bonCommande.getClient().getNom() : "");
		formBonCommande.setContact(
				(bonCommande.getClient() != null) ? clientService.getClient(bonCommande.getClient().getIdClient())
						: null);
		formBonCommande.setCoutLivraison(bonCommande.getCoutLivraison());
		formBonCommande.setDateCreation(bonCommande.getDateCreation());
		formBonCommande.setDateModification(bonCommande.getDateModification());
		formBonCommande.setDateLivraison(bonCommande.getDateLivraison());
		// formPanier.setDateExpiration(devi.getDateExpiration());
		formBonCommande.setDescription(bonCommande.getDescription());
		formBonCommande.setIdStockage(bonCommande.getStockage().getIdStockage());
		formBonCommande.setNomStockage(bonCommande.getStockage().getNom());
		formBonCommande.setStockage(bonCommande.getStockage());
		formBonCommande.setNote(bonCommande.getNote());
		formBonCommande.setSoumettreA(bonCommande.getSoumettreA());
		formBonCommande.setTaxe(linePaniers.get(0).getTaxe());
		formBonCommande.setUtilisateur(bonCommande.getUtilisateur());
		formBonCommande.setPourcentageRemise(bonCommande.getPourcentageRemise());
		// Choix de l'entreprise a afficher sur les documents
		if (!((List<Entreprise>) entrepriseRepository.findAll()).isEmpty()) {
			formBonCommande.setEntreprise(((List<Entreprise>) entrepriseRepository.findAll()).get(0));
		}

		if (bonCommande.getDevi() != null) {
			formBonCommande.setIdDevis(bonCommande.getDevi().getIdDevis());
		}

		// generation des données du tableau pour l'affichage des factures et
		// bon_livraison d'un bon_commande
		formBonCommande.setFactures(this.generetedListFacture(bonCommande.getFactures()));
		formBonCommande.setBonLivraisons(this.generetedListBonLivraison(bonCommande.getBonLivraisons()));

		// attribut propre a un bon de commande info sur les bon de livraison
		formBonCommande.setQuantiteTotal(Methode.getQuantiteTotalDocument(bonCommande.getLigneDocuments()));
		float quantiteDejaLivre = 0;
		formBonCommande.setStatutLivraison(Statut.getNonlivre());
		Boolean haveDelivered = false;
		Boolean isAllDelivered = true;
		for (BonLivraison bonLivraison : bonCommande.getBonLivraisons()) {
			if (bonLivraison.getEtat().equals(Etat.getActif())
					&& !bonLivraison.getStatut().equals(Statut.getAnnuler())) {
				quantiteDejaLivre += Methode.getQuantiteTotalDocument(bonLivraison.getLigneDocuments());
				if (bonLivraison.getStatut().equals(Statut.getLivre()))
					haveDelivered = true;
				else
					isAllDelivered = false;
			}
		}
		formBonCommande.setQuantiteLivre(quantiteDejaLivre);
		if (haveDelivered && isAllDelivered && (quantiteDejaLivre == formBonCommande.getQuantiteTotal())) {
			formBonCommande.setStatutLivraison(Statut.getLivre());
		} else if (haveDelivered) {
			formBonCommande.setStatutLivraison(Statut.getPartiellementlivre());
		}

		// attribut propre a un bon de commande info sur la facturarion
		formBonCommande.setSommeTotal(
				Methode.getMomtantTolalDocument(bonCommande.getLigneDocuments(), bonCommande.getCoutLivraison()));
		float montantDejaFacure = 0;
		float paiementTotal = 0;

		formBonCommande.setStatutPaiement(Statut.getNonpaye());
		for (Facture facture : bonCommande.getFactures()) {
			if (facture.getEtat().equals(Etat.getActif()) && !facture.getStatut().equals(Statut.getAnnuler())) {
				montantDejaFacure += factureService.montantTotalFacture(facture);
				paiementTotal += factureService.getPaiementTotalFacture(facture);
			}
		}
		formBonCommande.setSommeFacture(montantDejaFacure);

		if (paiementTotal >= montantDejaFacure && formBonCommande.getRestantToFacture() <= 0) {
			formBonCommande.setStatutPaiement(Statut.getPaye());
		} else if (paiementTotal < montantDejaFacure && paiementTotal > 0) {
			formBonCommande.setStatutPaiement(Statut.getPartiellementpaye());
		}

		/*
		 * if(Statut.getPaye().equals(formBonCommande.getStatutPaiement()) &&
		 * formBonCommande.getRestantToFacture()>0) {
		 * formBonCommande.setStatutPaiement(Statut.getPartiellementpaye()); }
		 */

		// mise ajour du statut du bon de commande
		if (!Statut.getAnnuler().equals(bonCommande.getStatut())
				&& Statut.getLivre().equals(formBonCommande.getStatutLivraison())
				&& Statut.getPaye().equals(formBonCommande.getStatutPaiement())) {
			bonCommande.setStatut(Statut.getTerminer());
			bonCommandeRepository.save(bonCommande);

		} else if (!Statut.getAnnuler().equals(bonCommande.getStatut())) {
			bonCommande.setStatut(Statut.getActif());
			bonCommandeRepository.save(bonCommande);
		}

		formBonCommande.setStatut(bonCommande.getStatut());
		formBonCommande.setColorStatut(ColorStatut.getColor(bonCommande.getStatut()));

		// formPanier.setPaiements(formPaiements);
		formBonCommande.setLines(linePaniers);
		formBonCommande.setPacksPresent(listFormPacks);

		return formBonCommande;
	}

	public FormTable generetedListFacture(List<Facture> factures) {
		List<RowTable> rows = new ArrayList<RowTable>();
		SimpleDateFormat formater = new SimpleDateFormat("dd MMMM yyyy");
		DecimalFormat df = new DecimalFormat("###,###,###,###.##");
		for (Facture facture : factures) {
			if (facture.getEtat().equals(Etat.getActif())) {
				RowTable row = new RowTable();
				List<String> dataList = new ArrayList<String>();
				dataList.add(facture.getIdFacture());
				// dataList.add((facture.getClient() != null) ? facture.getClient().getNom() :
				// null);
				dataList.add(facture.getStockage().getNom());
				dataList.add(formater.format(facture.getDateCreation()));
				dataList.add((facture.getDateEcheance() != null) ? formater.format(facture.getDateEcheance()) : "");
				dataList.add(df.format(factureService.montantTotalFacture(facture)) + "  " + Data.getDevise());
				// DecimalFormat.getCurrencyInstance().format( montantTotalFacture(facture))
				row.setData(dataList);
				row.setStatut(facture.getStatut());
				row.setColorStatut(ColorStatut.getColor(facture.getStatut()));
				row.setId(facture.getIdFacture());
				rows.add(row);
			}
		}

		List<String> colunm = new ArrayList<String>();
		colunm.add("FACTURE #");
		// colunm.add("CLIENT");
		colunm.add("LIEU DE STOCKAGE");
		colunm.add("DATE DE CRÉATION");
		colunm.add("ÉCHÉANCE");
		colunm.add("TOTAL");

		FormTable formTable = new FormTable();
		formTable.setColunm(colunm);
		formTable.setRows(rows);

		return formTable;
	}

	public FormTable generetedListBonLivraison(List<BonLivraison> bonLivraisons) {
		List<RowTable> rows = new ArrayList<RowTable>();
		SimpleDateFormat formater = new SimpleDateFormat("dd MMMM yyyy");
		DecimalFormat df = new DecimalFormat("###,###,###,###.##");
		for (BonLivraison bonLivraison : bonLivraisons) {
			if (bonLivraison.getEtat().equals(Etat.getActif())) {
				RowTable row = new RowTable();
				List<String> dataList = new ArrayList<String>();
				dataList.add(bonLivraison.getIdBonLivraison());
				// dataList.add((facture.getClient() != null) ? facture.getClient().getNom() :
				// null);
				dataList.add(bonLivraison.getBonCommande().getStockage().getNom());
				dataList.add(formater.format(bonLivraison.getDateCreation()));
				dataList.add(
						(bonLivraison.getDateLivraison() != null) ? formater.format(bonLivraison.getDateLivraison())
								: "");
				dataList.add(df.format(Methode.getQuantiteTotalDocument(bonLivraison.getLigneDocuments())));
				// DecimalFormat.getCurrencyInstance().format( montantTotalFacture(facture))
				row.setData(dataList);
				row.setStatut(bonLivraison.getStatut());
				row.setColorStatut(ColorStatut.getColor(bonLivraison.getStatut()));
				row.setId(bonLivraison.getIdBonLivraison());
				rows.add(row);
			}
		}

		List<String> colunm = new ArrayList<String>();
		colunm.add("BON DE LIVRAISON #");
		// colunm.add("CLIENT");
		colunm.add("LIEU DE STOCKAGE");
		colunm.add("DATE DE CRÉATION");
		colunm.add("DATE DE lIVRAISON");
		colunm.add("QUATITÉS");

		FormTable formTable = new FormTable();
		formTable.setColunm(colunm);
		formTable.setRows(rows);

		return formTable;
	}

	public float getMinQuantitePackBonCommande(Pack pack, BonCommande bonCommande) {
		// ici on va chercher a avoir le min le plus grand entre celui des quantite lie
		// au facture et celui des quantite lie au bon de livraison

		float minFacture = 0;
		float minLivraison = 0;
		for (BonLivraison bonLivraison : bonCommande.getBonLivraisons()) {
			if (!Statut.getAnnuler().equals(bonLivraison.getStatut())) {
				for (LigneDocument ligneDocument : bonLivraison.getLigneDocuments()) {
					if (ligneDocument.getPack().getId() == pack.getId()) {
						minLivraison += ligneDocument.getQuantite();
					}
				}
			}
		}

		for (Facture facture : bonCommande.getFactures()) {
			if (!Statut.getAnnuler().equals(facture.getStatut())) {
				for (LigneFacture ligneFacture : facture.getLigneFactures()) {
					if (ligneFacture.getPack().getId() == pack.getId()) {
						minFacture += ligneFacture.getQuantite();
					}
				}
			}
		}

		if (minFacture >= minLivraison)
			return minFacture;
		else
			return minLivraison;
	}

	// quantite de produits(packProduit) Maximun qu'on peut avoir dans une facture
	// lie a un bon de commande
	public float getMaxQuantitePackBonCommandeOnFacture(Pack pack, BonCommande bonCommande) {
		LigneDocument ligneDocument = new LigneDocument();
		for (LigneDocument ligneDocument2 : bonCommande.getLigneDocuments()) {
			if (ligneDocument2.getPack().getId() == pack.getId()) {
				ligneDocument = ligneDocument2;
			}
		}

		float quatiteMax = ligneDocument.getQuantite();
		List<Facture> factures = bonCommande.getFactures();

		for (Facture facture : factures) {
			if (!facture.getStatut().equals(Statut.getAnnuler()) && facture.getEtat().equals(Etat.getActif())) {
				List<LigneFacture> ligneFactures = facture.getLigneFactures();
				for (LigneFacture ligneFacture : ligneFactures) {
					if (pack.getId() == ligneFacture.getPack().getId()) {
						quatiteMax -= ligneFacture.getQuantite();
					}
				}
			}
		}

		return quatiteMax;
	}

	// quantite de produits(packProduit) Maximun qu'on peut avoir dans un bon de
	// livraison cree a partir d'un Bon decommande
	public float getMaxQuantitePackBonCommandeOnBonLivraison(Pack pack, BonCommande bonCommande) {
		LigneDocument ligneDocument = new LigneDocument();
		for (LigneDocument ligneDocument2 : bonCommande.getLigneDocuments()) {
			if (ligneDocument2.getPack().getId() == pack.getId()) {
				ligneDocument = ligneDocument2;
			}
		}

		float quatiteMax = ligneDocument.getQuantite();
		List<BonLivraison> bonLivraisons = bonCommande.getBonLivraisons();

		for (BonLivraison bonLivraison : bonLivraisons) {
			if (!bonLivraison.getStatut().equals(Statut.getAnnuler())
					&& bonLivraison.getEtat().equals(Etat.getActif())) {
				List<LigneDocument> ligneBonLivraisons = bonLivraison.getLigneDocuments();
				for (LigneDocument ligneBonLivraison : ligneBonLivraisons) {
					if (pack.getId() == ligneBonLivraison.getPack().getId()) {
						quatiteMax -= ligneBonLivraison.getQuantite();
					}
				}
			}
		}

		return quatiteMax;
	}

	public void controlUpdateBonCommande(FormPanier formPanier, BonCommande bonCommande) {
		List<LinePanier> lines = formPanier.getLines();

		for (LinePanier linePanier : lines) {
			if (getMinQuantitePackBonCommande(packRepository.findById(linePanier.getIdPack()).get(),
					bonCommande) > linePanier.getQuantite()) {
				throw new StreengeException(new ErrorAPI("Vous avez deja livre ou Facture "
						+ getMinQuantitePackBonCommande(packRepository.findById(linePanier.getIdPack()).get(),
								bonCommande)
						+ " " + linePanier.getDesignation()
						+ " ,vous ne pouvais pas en mettre moins pour modification"));
			}
		}
	}

	public FormPanier getpanierToCreateFacture(String idBonCommande) {
		FormPanier formPanier = this.getPanierBonCommande(idBonCommande);
		List<LinePanier> lines = new ArrayList<LinePanier>();
		BonCommande bonCommande = bonCommandeRepository.findById(idBonCommande).get();

		for (LinePanier linePanier : formPanier.getLines()) {
			linePanier.setQuantite(this.getMaxQuantitePackBonCommandeOnFacture(
					packRepository.findById(linePanier.getIdPack()).get(), bonCommande));
			linePanier.setQuantiteMax(linePanier.getQuantite());
			if (linePanier.getQuantiteMax() > 0) {
				linePanier.setQuantiteMin(-1);
				lines.add(linePanier);
			}
		}

		// List<Facture> factures = bonCommande.getFactures();
		for (Facture facture : bonCommande.getFactures()) {
			formPanier.setCoutLivraison(formPanier.getCoutLivraison() - facture.getCoutLivraison());
		}

		formPanier.setIdDevis("");
		formPanier.setLines(lines);
		formPanier.setAddLine(false);
		return formPanier;
	}

	public Facture createFactureFromBonCommande(FormPanier formPanier) {
		if (bonCommandeRepository.findById(formPanier.getIdBonCommande()).isPresent()) {

			Facture facture = factureService.createOrUpdateFacture(formPanier, true);
			BonCommande bonCommande = bonCommandeRepository.findById(formPanier.getIdBonCommande()).get();
			// devis.setStatut(Statut.getAcceptertransformer());
			List<Facture> factures = new ArrayList<Facture>();
			factures.add(facture);
			bonCommande.setFactures(factures);
			bonCommandeRepository.save(bonCommande);
			return facture;
		} else {
			throw new StreengeException(new ErrorAPI("Impossible de créer une facture á partir de ce Bon de commande"));
		}
	}

	public FormPanier getpanierToCreateBonLivraison(String idBonCommande) {
		FormPanier formPanier = this.getPanierBonCommande(idBonCommande);
		// System.out.println("Id bon de commande N1: "+formPanier.getIdBonCommande());
		List<LinePanier> lines = new ArrayList<LinePanier>();
		BonCommande bonCommande = bonCommandeRepository.findById(idBonCommande).get();

		for (LinePanier linePanier : formPanier.getLines()) {
			linePanier.setQuantite(this.getMaxQuantitePackBonCommandeOnBonLivraison(
					packRepository.findById(linePanier.getIdPack()).get(), bonCommande));
			linePanier.setQuantiteMax(linePanier.getQuantite());
			if (linePanier.getQuantiteMax() > 0) {
				linePanier.setQuantiteMin(-1);
				lines.add(linePanier);
			}
		}

		// List<Facture> factures = bonCommande.getFactures();

		formPanier.setIdDevis("");
		formPanier.setLines(lines);
		formPanier.setAddLine(false);
		return formPanier;
	}

	public BonLivraison createBonLivraisonFromBonCommande(FormPanier formPanier) {
		// System.out.println("Id bon de commande: "+formPanier.getIdBonCommande());;
		if (bonCommandeRepository.findById(formPanier.getIdBonCommande()).isPresent()) {

			BonLivraison bonLivraison = bonLivraisonService.createOrUpdateBonLivraison(formPanier, true);
			BonCommande bonCommande = bonCommandeRepository.findById(formPanier.getIdBonCommande()).get();
			// devis.setStatut(Statut.getAcceptertransformer());
			List<BonLivraison> bonLivraisons = new ArrayList<BonLivraison>();
			bonLivraisons.add(bonLivraison);
			bonCommande.setBonLivraisons(bonLivraisons);
			bonCommandeRepository.save(bonCommande);
			return bonLivraison;
		} else {
			throw new StreengeException(
					new ErrorAPI("Impossible de créer un bon livraisonn á partir de ce Bon de commande"));
		}
	}

	public void cancelBonCommande(String idBonCommande, int idUtilisateur) {
		if (bonCommandeRepository.findById(idBonCommande).isPresent()) {
			List<ProduitStockage> produitStockagesBefore = Methode
					.getProduitStockagesEager((List<ProduitStockage>) produitStockageRepository.findAll());
			BonCommande bonCommande = bonCommandeRepository.findById(idBonCommande).get();
			if (!bonCommande.getStatut().equals(Statut.getAnnuler())
					&& !bonCommande.getEtat().equals(Etat.getDelete())) {

				List<Facture> factures = bonCommande.getFactures();
				// Facture facture:bonCommande.getFactures()
				for (int i = 0; i < factures.size(); i++) {
					System.out.println("annule facture");
					factureService.cancelFacture(factures.get(i).getIdFacture(), idUtilisateur);
				}

				List<BonLivraison> bonLivraisons = bonCommande.getBonLivraisons();
				for (int i = 0; i < bonLivraisons.size(); i++) {
					System.out.println("annule bon livraison");
					bonLivraisonService.cancelBonLivraison(bonLivraisons.get(i).getIdBonLivraison(), idUtilisateur);
				}
				for (LigneDocument ligneDocument : bonCommande.getLigneDocuments()) {
					produitService.updateProduitQuantite(ligneDocument.getPack(), (ligneDocument.getQuantite()),
							bonCommande.getStockage(), true, false);
				}
				bonCommande.setStatut(Statut.getAnnuler());
				bonCommandeRepository.save(bonCommande);
			}
			registreStockService.updateRegistreStock(produitStockagesBefore, "Annulation du bon de commannde",
					idUtilisateur, idBonCommande);
		}
	}

	public void reactiveBonCommande(String idBonCommande, int idUtilisateur) {
		if (bonCommandeRepository.findById(idBonCommande).isPresent()) {
			List<ProduitStockage> produitStockagesBefore = Methode
					.getProduitStockagesEager((List<ProduitStockage>) produitStockageRepository.findAll());
			BonCommande bonCommande = bonCommandeRepository.findById(idBonCommande).get();
			if (bonCommande.getStatut().equals(Statut.getAnnuler())
					&& !bonCommande.getEtat().equals(Etat.getDelete())) {
				for (LigneDocument ligneDocument : bonCommande.getLigneDocuments()) {
					produitService.updateProduitQuantite(ligneDocument.getPack(), (ligneDocument.getQuantite() * -1),
							bonCommande.getStockage(), true, false);
				}
				bonCommande.setStatut(Statut.getActif());
				bonCommandeRepository.save(bonCommande);
			}
			registreStockService.updateRegistreStock(produitStockagesBefore, "Reactivation du bon de commannde",
					idUtilisateur, idBonCommande);
		}
	}

	public void deleteBonCommande(String idBonCommande, int idUtilisateur) {
		if (bonCommandeRepository.findById(idBonCommande).isPresent()) {
			List<ProduitStockage> produitStockagesBefore = Methode
					.getProduitStockagesEager((List<ProduitStockage>) produitStockageRepository.findAll());
			BonCommande bonCommande = bonCommandeRepository.findById(idBonCommande).get();
			if (!bonCommande.getEtat().equals(Etat.getDelete())) {
				for (Facture facture : bonCommande.getFactures()) {
					factureService.deleteFacture(facture.getIdFacture(), idUtilisateur);
				}
				for (BonLivraison bonLivraison : bonCommande.getBonLivraisons()) {
					bonLivraisonService.deleteBonLivraison(bonLivraison.getIdBonLivraison(), idUtilisateur);
				}
				if (!bonCommande.getStatut().equals(Statut.getAnnuler())) {
					for (LigneDocument ligneDocument : bonCommande.getLigneDocuments()) {
						produitService.updateProduitQuantite(ligneDocument.getPack(), (ligneDocument.getQuantite()),
								bonCommande.getStockage(), true, false);
					}
				}
				bonCommande.setStatut(Statut.getAnnuler());
				bonCommande.setEtat(Etat.getDelete());
				bonCommandeRepository.save(bonCommande);
			}
			registreStockService.updateRegistreStock(produitStockagesBefore, "Suppression du bon de commannde",
					idUtilisateur, idBonCommande);
		}
	}
}
