package com.streenge.service.venteService;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.streenge.model.LigneDocument;
import com.streenge.model.admin.Entreprise;
import com.streenge.model.admin.Utilisateur;
import com.streenge.model.produit.Pack;
import com.streenge.model.produit.ProduitStockage;
import com.streenge.model.vente.BonCommande;
import com.streenge.model.vente.BonLivraison;
import com.streenge.repository.LigneDocumentRepository;
import com.streenge.repository.adminRepository.EntrepriseRepository;
import com.streenge.repository.produitRepo.PackRepository;
import com.streenge.repository.produitRepo.ProduitStockageRepository;
import com.streenge.repository.venteRepo.BonCommandeRepository;
import com.streenge.repository.venteRepo.BonLivraisonRepository;
import com.streenge.service.contactService.ClientService;
import com.streenge.service.produitService.ProduitService;
import com.streenge.service.stockService.RegistreStockService;
import com.streenge.service.utils.erroclass.ErrorAPI;
import com.streenge.service.utils.erroclass.StreengeException;
import com.streenge.service.utils.formInt.FormPanier;
import com.streenge.service.utils.formInt.LinePanier;
import com.streenge.service.utils.formOut.FormPack;
import com.streenge.service.utils.formOut.FormTable;
import com.streenge.service.utils.formOut.RowTable;
import com.streenge.service.utils.streengeData.ColorStatut;
import com.streenge.service.utils.streengeData.Etat;
import com.streenge.service.utils.streengeData.Statut;
import com.streenge.service.utils.streengeFunction.Methode;

@Service
public class BonLivraisonService {
	@Autowired
	private BonLivraisonRepository bonLivraisonRepository;

	@Autowired
	private LigneDocumentRepository ligneDocumentRepository;

	@Autowired
	private BonCommandeRepository bonCommandeRepository;

	@Autowired
	private PackRepository packRepository;

	@Autowired
	private ClientService clientService;

	@Autowired
	private BonCommandeService bonCommandeService;

	@Autowired
	private ProduitService produitService;

	@Autowired
	private EntrepriseRepository entrepriseRepository;

	@Autowired
	private RegistreStockService registreStockService;

	@Autowired
	private ProduitStockageRepository produitStockageRepository;

	public BonLivraison createOrUpdateBonLivraison(FormPanier formPanier, boolean isNewbonLivraison) {
		Methode.controlFormPanier(formPanier);
		List<ProduitStockage> produitStockagesBefore = Methode
				.getProduitStockagesEager((List<ProduitStockage>) produitStockageRepository.findAll());

		Utilisateur utilisateur = formPanier.getUtilisateur();
		BonLivraison bonLivraison;
		if (isNewbonLivraison) {
			bonLivraison = new BonLivraison();
			bonLivraison.setIdBonLivraison(Methode.createID(bonLivraisonRepository, "BL"));
			bonLivraison.setBonCommande(bonCommandeRepository.findById(formPanier.getIdBonCommande()).get());
			bonLivraison.setEtat(Etat.getActif());
			bonLivraison.setStatut(Statut.getEnattente());
			formPanier.setDateCreation(new Date());
			bonLivraison.setUtilisateur(formPanier.getUtilisateur());
		} else {
			bonLivraison = bonLivraisonRepository.findById(formPanier.getIdBonLivraison()).get();
			this.controlUpdateBonLivraison(bonLivraison);
			for (LigneDocument ligne : bonLivraison.getLigneDocuments()) {
				ligneDocumentRepository.deleteLigneDocument(ligne.getId());
				bonLivraison.setDateModification(new Date());
			}
		}

		bonLivraison.setNote(formPanier.getNote());
		bonLivraison.setDateCreation(formPanier.getDateCreation());
		bonLivraison.setDateLivraison(formPanier.getDateLivraison());
		bonLivraison.setDescription(formPanier.getDescription());

		// bonLivraison.setSoumettreA(formPanier.getSoumettreA());

		List<LigneDocument> ligneBonLivraisons = new ArrayList<LigneDocument>();
		LigneDocument ligneBonLivraison = null;
		// List<LinePanier> lines = formPanier.getLines();
		for (LinePanier line : formPanier.getLines()) {
			ligneBonLivraison = new LigneDocument();
			ligneBonLivraison.setBonLivraison(bonLivraison);
			ligneBonLivraison.setDesignation(line.getDesignation());
			ligneBonLivraison.setQuantite(line.getQuantite());
			if (packRepository.findById(line.getIdPack()).isPresent()) {
				ligneBonLivraison.setPack(packRepository.findById(line.getIdPack()).get());
			} else {
				ErrorAPI errorAPI = new ErrorAPI();
				errorAPI.setMessage("L'enregistrement du bon de livraison n'est pas possible,Le produit: "
						+ line.getDesignation().toUpperCase()
						+ " N'existe pas où alors ce produit à été récemment supprimer");
				throw new StreengeException(errorAPI);
			}
			ligneBonLivraisons.add(ligneBonLivraison);
		}

		bonLivraison.setLigneDocuments(ligneBonLivraisons);

		try {
			bonLivraison = bonLivraisonRepository.save(bonLivraison);
		} catch (DataIntegrityViolationException e) {
			System.out.println("bonLivraison already exist");
		}

		registreStockService.updateRegistreStock(produitStockagesBefore,
				isNewbonLivraison ? "Création du bon de livraison" : "Modification du bon Livraison", utilisateur,
				bonLivraison.getIdBonLivraison());
		return bonLivraison;
	}

	public FormPanier getPanierBonLivraison(String idBonLivraison) {
		FormPanier formPanier = new FormPanier();
		List<LinePanier> linePaniers = new ArrayList<LinePanier>();
		List<FormPack> listFormPacks = new ArrayList<FormPack>();

		BonLivraison bonLivraison = bonLivraisonRepository.findById(idBonLivraison).get();

		BonCommande bonCommande = bonLivraison.getBonCommande();

		formPanier.setIdBonLivraison(idBonLivraison);
		formPanier.setIdBonCommande(bonCommande.getIdBonCommande());
		formPanier.setAddLine(false);

		LinePanier linePanier = null;
		List<LigneDocument> ligneBonLivraisons = bonLivraison.getLigneDocuments();
		for (LigneDocument ligneBonLivraison : ligneBonLivraisons) {
			linePanier = new LinePanier();

			linePanier.setDesignation(ligneBonLivraison.getDesignation());
			linePanier.setQuantite(ligneBonLivraison.getQuantite());
			linePanier.setIdPack(ligneBonLivraison.getPack().getId());

			linePanier.setQuantiteMax(
					linePanier.getQuantite() + bonCommandeService.getMaxQuantitePackBonCommandeOnBonLivraison(
							packRepository.findById(linePanier.getIdPack()).get(), bonCommande));

			//System.out.println("==Designation==" + linePanier.getDesignation());
			int i = 0;
			for (FormPack pack : listFormPacks) {
				if (pack.getIdPack() == linePanier.getIdPack()) {
					linePanier.setIndexPack(i);
				}
				i++;
			}

			if (linePanier.getIndexPack() == -1) {
				Pack pack = ligneBonLivraison.getPack();
				FormPack formPack = new FormPack();
				formPack.setIdPack(pack.getId());
				formPack.setSku(pack.getProduit().getSku());
				formPack.setNombreUnite(pack.getNombreUnite());
				formPack.setIdProduit(pack.getProduit().getIdProduit());
				formPack.setDesignation(ligneBonLivraison.getDesignation());
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
			
			if (Etat.getDelete().equals(ligneBonLivraison.getPack().getProduit().getEtat())) {
				linePanier.setIsDelete(true);
			}
			
			linePaniers.add(linePanier);
		}

		if (!((List<Entreprise>) entrepriseRepository.findAll()).isEmpty()) {
			formPanier.setEntreprise(((List<Entreprise>) entrepriseRepository.findAll()).get(0));
		}

		formPanier.setNomClient((bonCommande.getClient() != null) ? bonCommande.getClient().getNom() : "");
		formPanier.setContact(
				(bonCommande.getClient() != null) ? clientService.getClient(bonCommande.getClient().getIdClient())
						: null);
		formPanier.setDateCreation(bonLivraison.getDateCreation());
		formPanier.setDateModification(bonLivraison.getDateModification());
		formPanier.setDateLivraison(bonLivraison.getDateLivraison());
		formPanier.setDescription(bonLivraison.getDescription());
		formPanier.setIdStockage(bonCommande.getStockage().getIdStockage());
		formPanier.setNomStockage(bonCommande.getStockage().getNom());
		formPanier.setStockage(bonCommande.getStockage());
		formPanier.setUtilisateur(bonCommande.getUtilisateur());
		formPanier.setNote(bonLivraison.getNote());
		formPanier.setUtilisateur(bonLivraison.getUtilisateur());

		formPanier.setStatut(bonLivraison.getStatut());
		formPanier.setColorStatut(ColorStatut.getColor(bonLivraison.getStatut()));

		formPanier.setLines(linePaniers);
		formPanier.setPacksPresent(listFormPacks);

		return formPanier;
	}

	public FormTable generateListBonLivraison(String filter) {
		List<BonLivraison> BonLivraisons = new ArrayList<BonLivraison>();
		List<RowTable> rows = new ArrayList<RowTable>();
		SimpleDateFormat formater = new SimpleDateFormat("dd MMMM yyyy");
		DecimalFormat df = new DecimalFormat("###,###,###,###.##");
		if (filter != null) {
			BonLivraisons = bonLivraisonRepository
					.findByBonCommandeClientNomStartingWithOrderByIdBonLivraisonDesc(filter);

			if (BonLivraisons.size() < 1) {
				BonLivraisons = bonLivraisonRepository
						.findByIdBonLivraisonStartingWithOrUtilisateurNomStartingWithOrBonCommandeIdBonCommandeStartingWithOrStatutStartingWithOrderByIdBonLivraisonDesc(
								filter, filter, filter, filter);
			}

			if (BonLivraisons.size() < 1) {
				BonLivraisons = bonLivraisonRepository
						.findByBonCommandeStockageNomStartingWithOrderByIdBonLivraisonDesc(filter);
			}
		} else {
			BonLivraisons = bonLivraisonRepository.findAllByOrderByIdBonLivraisonDesc();
		}

		for (BonLivraison bonLivraison : BonLivraisons) {
			if (bonLivraison.getStockage()==null) {
				bonLivraison.setStockage(bonLivraison.getBonCommande().getStockage());
				bonLivraisonRepository.save(bonLivraison);
			}
			if (bonLivraison.getEtat().equals(Etat.getActif())) {
				RowTable row = new RowTable();
				List<String> dataList = new ArrayList<String>();
				dataList.add(bonLivraison.getIdBonLivraison());
				dataList.add((bonLivraison.getBonCommande().getClient() != null)
						? bonLivraison.getBonCommande().getClient().getNom()
						: null);
				dataList.add(bonLivraison.getBonCommande().getStockage().getNom());
				dataList.add(formater.format(bonLivraison.getDateCreation()));
				dataList.add(
						(bonLivraison.getDateLivraison() != null) ? formater.format(bonLivraison.getDateLivraison())
								: null);
				dataList.add(df.format(Methode.getQuantiteTotalDocument(bonLivraison.getLigneDocuments())));

				row.setData(dataList);
				row.setStatut(bonLivraison.getStatut());
				row.setColorStatut(ColorStatut.getColor(bonLivraison.getStatut()));
				row.setId(bonLivraison.getIdBonLivraison());
				row.setIdUtilisateur(
						(bonLivraison.getUtilisateur() != null) ? bonLivraison.getUtilisateur().getId() : 0);
				row.setIdStockage(((bonLivraison.getBonCommande()!=null)?bonLivraison.getBonCommande().getStockage().getIdStockage():0));
				rows.add(row);
			}
		}

		List<String> colunm = new ArrayList<String>();
		colunm.add("BON LIVRAISON #");
		colunm.add("CLIENT");
		colunm.add("LIEU DE STOCKAGE");
		colunm.add("DATE DE CRÉATION");
		colunm.add("DATE DE LIVRAISON");
		colunm.add("QUANTITÉS");

		FormTable formTable = new FormTable();
		formTable.setColunm(colunm);
		formTable.setRows(rows);

		return formTable;
	}

	public void setStatutBonLivraison(String iBonLivraison, String statut, int idUtilisateur) {
		BonLivraison bonLivraison = bonLivraisonRepository.findById(iBonLivraison).get();
		BonCommande bonCommande = bonLivraison.getBonCommande();

		List<ProduitStockage> produitStockagesBefore = Methode
				.getProduitStockagesEager((List<ProduitStockage>) produitStockageRepository.findAll());
		if (!statut.equals(bonLivraison.getStatut()) && !bonLivraison.getStatut().equals(Statut.getAnnuler())) {
			// dans le cas ou on vient tout juste de livre alors on diminue la quantite réel
			// de produit
			if (statut.equals(Statut.getLivre())) {

				for (LigneDocument ligneDocument : bonLivraison.getLigneDocuments()) {
					produitService.updateProduitQuantite(ligneDocument.getPack(), (ligneDocument.getQuantite() * -1),
							bonCommande.getStockage(), false, true);
				}
			} else if (bonLivraison.getStatut().equals(Statut.getLivre())) {
				// dans la cas ou le bon de livraison est marquer comme livre mais l'utilisateur
				// decide de changer son statut alors on augmente le stock reel du produit pour
				// revenir au point de depart
				for (LigneDocument ligneDocument : bonLivraison.getLigneDocuments()) {
					produitService.updateProduitQuantite(ligneDocument.getPack(), (ligneDocument.getQuantite()),
							bonCommande.getStockage(), false, true);
				}
			}
			bonLivraison.setStatut(statut);
		}
		bonLivraisonRepository.save(bonLivraison);
		registreStockService.updateRegistreStock(produitStockagesBefore, "Changement du statut du bon de livraison.",
				idUtilisateur, iBonLivraison);
		if (bonLivraison.getBonCommande()!=null) {
			bonCommandeService.getPanierBonCommande(bonLivraison.getBonCommande().getIdBonCommande());
		}
	}

	public void cancelBonLivraison(String idBonLivraison, int idUtilisateur) {

		BonLivraison bonLivraison = bonLivraisonRepository.findById(idBonLivraison).get();
		BonCommande bonCommande = bonLivraison.getBonCommande();
		if (!bonLivraison.getStatut().equals(Statut.getAnnuler())) {
			List<ProduitStockage> produitStockagesBefore = Methode
					.getProduitStockagesEager((List<ProduitStockage>) produitStockageRepository.findAll());
			if (bonLivraison.getStatut().equals(Statut.getLivre())) {
				// dans la cas ou le bon de livraison est marquer comme livre mais l'utilisateur
				// decide de changer son statut alors on augmente le stock reel du produit pour
				// revenir au point de depart
				for (LigneDocument ligneDocument : bonLivraison.getLigneDocuments()) {
					produitService.updateProduitQuantite(ligneDocument.getPack(), (ligneDocument.getQuantite()),
							bonCommande.getStockage(), false, true);
				}
			}
			bonLivraison.setStatut(Statut.getAnnuler());
			bonLivraisonRepository.save(bonLivraison);

			registreStockService.updateRegistreStock(produitStockagesBefore, "Annulation du bon de livraison",
					idUtilisateur, idBonLivraison);
			
			//pour mettre a jour le statut du bon de commande
			if (bonLivraison.getBonCommande()!=null) {
				bonCommandeService.getPanierBonCommande(bonLivraison.getBonCommande().getIdBonCommande());
			}
		}

	}

	public void reactiveBonLivraison(String idBonLivraison) {
		BonLivraison bonLivraison = bonLivraisonRepository.findById(idBonLivraison).get();
		BonCommande bonCommande = bonLivraison.getBonCommande();
		if (bonCommande.getStatut().equals(Statut.getAnnuler())) {
			throw new StreengeException(
					new ErrorAPI("Il est impossible de Reactiver ce bon de Livraison car ca commande mère ( "
							+ bonCommande.getIdBonCommande() + " ) a été annuler, Reactiver d'abord la commande mère"));
		}
		if (bonLivraison.getStatut().equals(Statut.getAnnuler())) {

			List<LigneDocument> ligneDocuments = bonLivraison.getLigneDocuments();
			for (LigneDocument ligneDocument : ligneDocuments) {
				if (ligneDocument.getQuantite() > bonCommandeService
						.getMaxQuantitePackBonCommandeOnBonLivraison(ligneDocument.getPack(), bonCommande)) {
					throw new StreengeException(new ErrorAPI(
							"Il est impossible de Reactiver ce bon de Livraison car certains produit on déjâ été totalement livre dans d'autre bon de Livraison de la commande mère ( "
									+ bonCommande.getIdBonCommande() + " )"));
				}
			}
			bonLivraison.setStatut(Statut.getEnattente());
			bonLivraisonRepository.save(bonLivraison);
			if (bonLivraison.getBonCommande()!=null) {
				bonCommandeService.getPanierBonCommande(bonLivraison.getBonCommande().getIdBonCommande());
			}
		}
	}

	public void deleteBonLivraison(String idBonLivraison, int idUtilisateur) {
		if (bonLivraisonRepository.findById(idBonLivraison).isPresent()) {
			BonLivraison bonLivraison = bonLivraisonRepository.findById(idBonLivraison).get();
			if (!bonLivraison.getEtat().equals(Etat.getDelete())) {
				cancelBonLivraison(idBonLivraison, idUtilisateur);
				bonLivraison.setEtat(Etat.getDelete());
				bonLivraison = bonLivraisonRepository.save(bonLivraison);
			}
		}
	}

	public void controlUpdateBonLivraison(BonLivraison bonLivraison) {

		if (bonLivraison.getStatut().equals(Statut.getAnnuler())) {
			throw new StreengeException(new ErrorAPI(
					"Vous ne pouvais pas Modifier  Document Annuler, pour pouvoir le modifier, il faut d'abord le reactiver...!"));
		} else if (bonLivraison.getStatut().equals(Statut.getLivre())) {
			throw new StreengeException(new ErrorAPI(
					"Vous ne pouvais pas modifier un bon de livraison qui est Deja Livré, pour modifier ce bon de livraison, changé son statut...!"));
		}
	}

}
