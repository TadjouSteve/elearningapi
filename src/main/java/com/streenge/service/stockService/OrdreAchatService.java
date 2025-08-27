package com.streenge.service.stockService;

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
import com.streenge.model.admin.Taxe;
import com.streenge.model.admin.Utilisateur;
import com.streenge.model.produit.Pack;
import com.streenge.model.produit.ProduitStockage;
import com.streenge.model.stock.Livraison;
import com.streenge.model.stock.OrdreAchat;
import com.streenge.repository.LigneDocumentRepository;
import com.streenge.repository.adminRepository.EntrepriseRepository;
import com.streenge.repository.adminRepository.StockageRepository;
import com.streenge.repository.adminRepository.TaxeRepository;
import com.streenge.repository.contactRepo.FournisseurRepository;
import com.streenge.repository.produitRepo.PackRepository;
import com.streenge.repository.produitRepo.ProduitStockageRepository;
import com.streenge.repository.stockRepo.OrdreAchatRepository;
import com.streenge.service.contactService.FournisseurService;
import com.streenge.service.produitService.ProduitService;
import com.streenge.service.utils.erroclass.ErrorAPI;
import com.streenge.service.utils.erroclass.StreengeException;
import com.streenge.service.utils.formInt.FormPanier;
import com.streenge.service.utils.formInt.LinePanier;
import com.streenge.service.utils.formOut.FormOrdreAchat;
import com.streenge.service.utils.formOut.FormPack;
import com.streenge.service.utils.formOut.FormTable;
import com.streenge.service.utils.formOut.RowTable;
import com.streenge.service.utils.streengeData.ColorStatut;
import com.streenge.service.utils.streengeData.Data;
import com.streenge.service.utils.streengeData.Etat;
import com.streenge.service.utils.streengeData.Statut;
import com.streenge.service.utils.streengeFunction.Methode;

@Service
public class OrdreAchatService {

	@Autowired
	private OrdreAchatRepository ordreAchatRepository;

	@Autowired
	private StockageRepository stockageRepository;

	@Autowired
	private PackRepository packRepository;

	@Autowired
	private TaxeRepository taxeRepository;

	@Autowired
	private ProduitService produitService;

	@Autowired
	private LigneDocumentRepository ligneDocumentRepository;

	@Autowired
	private FournisseurRepository fournisseurRepository;

	@Autowired
	private FournisseurService fournisseurService;

	@Autowired
	private EntrepriseRepository entrepriseRepository;

	@Autowired
	private LivraisonService livraisonService;

	@Autowired
	private RegistreStockService registreStockService;

	@Autowired
	private ProduitStockageRepository produitStockageRepository;

	public FormTable generateListOrdreAchat(String filter) {
		List<OrdreAchat> listOrdreAchats = new ArrayList<OrdreAchat>();
		List<RowTable> rows = new ArrayList<RowTable>();
		SimpleDateFormat formater = new SimpleDateFormat("dd MMMM yyyy");
		DecimalFormat df = new DecimalFormat("###,###,###,###.##");

		if (filter != null) {
			listOrdreAchats = ordreAchatRepository.findByFournisseurNomStartingWithOrderByIdOrdreAchatDesc(filter);
		} else {
			listOrdreAchats = ordreAchatRepository.findAllByOrderByIdOrdreAchatDesc();
		}

		for (OrdreAchat ordreAchat : listOrdreAchats) {
			if (ordreAchat.getEtat().equals(Etat.getActif())) {
				RowTable row = new RowTable();
				List<String> dataList = new ArrayList<String>();
				dataList.add(ordreAchat.getIdOrdreAchat());
				dataList.add((ordreAchat.getFournisseur() != null) ? ordreAchat.getFournisseur().getNom() : null);
				dataList.add(ordreAchat.getStockage().getNom());
				dataList.add(formater.format(ordreAchat.getDateCreation()));
				dataList.add(df.format(Methode.getQuantiteTotalDocument(ordreAchat.getLigneDocuments())));
				dataList.add(df.format(Methode.getMomtantTolalDocument(ordreAchat.getLigneDocuments(),
						ordreAchat.getCoutLivraison())) + " " + Data.getDevise());
				row.setData(dataList);
				row.setStatut(ordreAchat.getStatut());
				row.setColorStatut(ColorStatut.getColor(ordreAchat.getStatut()));
				row.setId(ordreAchat.getIdOrdreAchat());
				row.setIdUtilisateur((ordreAchat.getUtilisateur()!=null)?ordreAchat.getUtilisateur().getId():0);
				row.setIdStockage(ordreAchat.getStockage().getIdStockage());
				rows.add(row);
			}
		}

		List<String> colunm = new ArrayList<String>();
		colunm.add("ORDRE ACHAT #");
		colunm.add("FOURNISSEUR");
		colunm.add("LIEU DE STOCKAGE");
		colunm.add("DATE DE CRÉATION");
		colunm.add("QUANTITÉS");
		colunm.add("TOTAL");

		FormTable formTable = new FormTable();
		formTable.setColunm(colunm);
		formTable.setRows(rows);

		return formTable;
	}

	public OrdreAchat createOrUpdateOdreAchat(FormPanier formPanier, boolean isNewOdreAchat) {
		Methode.controlFormPanier(formPanier);
		List<ProduitStockage> produitStockagesBefore = Methode
				.getProduitStockagesEager((List<ProduitStockage>) produitStockageRepository.findAll());

		Utilisateur utilisateur = formPanier.getUtilisateur();
		OrdreAchat ordreAchat;
		if (isNewOdreAchat) {
			ordreAchat = new OrdreAchat();
			ordreAchat.setIdOrdreAchat(Methode.createID(ordreAchatRepository, "OA"));
			ordreAchat.setDateCreation(new Date());
			ordreAchat.setUtilisateur(utilisateur);
		} else {
			ordreAchat = ordreAchatRepository.findById(formPanier.getIdOrdreAchat()).get();
			controlUpdateOrdreAchat(formPanier, ordreAchat);
			for (LigneDocument ligne : ordreAchat.getLigneDocuments()) {
				float restantToRecieve = this.getMaxQuantitePackOrdreAchatOnLivraison(ligne.getPack(), ordreAchat);
				produitService.updateProduitQuantiteAvenir(ligne.getPack(), (restantToRecieve * -1),
						ordreAchat.getStockage());
				ligneDocumentRepository.deleteLigneDocument(ligne.getId());
			}
			ordreAchat.setDateModification(new Date());
		}

		if (!fournisseurRepository.findByNom(formPanier.getNomFournisseur()).isEmpty()) {
			ordreAchat.setFournisseur(fournisseurRepository.findByNom(formPanier.getNomFournisseur()).get(0));
		}

		ordreAchat.setNote(formPanier.getNote());
		ordreAchat.setCoutLivraison(formPanier.getCoutLivraison());
		// ordreAchat.setDateCreation(formPanier.getDateCreation());
		// devi.setDateExpiration(formPanier.getDateExpiration());
		ordreAchat.setDateLivraison(formPanier.getDateLivraison());
		ordreAchat.setDescription(formPanier.getDescription());
		if (isNewOdreAchat) {
			ordreAchat.setEtat(Etat.getActif());
			ordreAchat.setStatut(Statut.getNonlivre());
		}

		if (stockageRepository.findById(formPanier.getIdStockage()).isPresent()) {
			ordreAchat.setStockage(stockageRepository.findById(formPanier.getIdStockage()).get());
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
			ligneBonCommane.setOrdreAchat(ordreAchat);
			ligneBonCommane.setDesignation(line.getDesignation());
			ligneBonCommane.setPrix(line.getPrix());
			ligneBonCommane.setQuantite(line.getQuantite());
			ligneBonCommane.setRemise(line.getRemise());

			if (line.getTaxe() != null)
				ligneBonCommane.setTaxe(taxeRepository.findById(line.getTaxe().getId()).get());

			if (packRepository.findById(line.getIdPack()).isPresent()) {
				ligneBonCommane.setPack(packRepository.findById(line.getIdPack()).get());
			} else {
				ErrorAPI errorAPI = new ErrorAPI();
				errorAPI.setMessage("L'enregistrement de l'ordre d'achat n'est pas possible,Le produit: "
						+ line.getDesignation().toUpperCase()
						+ " N'existe pas où alors ce produit à été récemment supprimer");
				throw new StreengeException(errorAPI);
			}
			ligneDocuments.add(ligneBonCommane);// lo
		}

		ordreAchat.setLigneDocuments(ligneDocuments);

		for (LigneDocument ligne : ligneDocuments) {

			if (isNewOdreAchat) {
				produitService.updateProduitQuantiteAvenir(ligne.getPack(), (ligne.getQuantite()),
						ordreAchat.getStockage());
			} else {
				float restantToRecieve = this.getMaxQuantitePackOrdreAchatOnLivraison(ligne.getPack(), ordreAchat);
				produitService.updateProduitQuantiteAvenir(ligne.getPack(), (restantToRecieve),
						ordreAchat.getStockage());
			}

		}

		try {
			ordreAchat = ordreAchatRepository.save(ordreAchat);
		} catch (DataIntegrityViolationException e) {
			System.out.println("Ordre Achat already exist");
		}

		registreStockService.updateRegistreStock(produitStockagesBefore,
				isNewOdreAchat ? "Création de l'ordre d'achat" : "Modification de l'ordre d'achat", utilisateur,
				ordreAchat.getIdOrdreAchat());

		return ordreAchat;
	}

	public FormOrdreAchat getPanierOrdreAchat(String idOrdreAchat) {
		FormOrdreAchat formOrdreAchat = new FormOrdreAchat();
		List<LinePanier> linePaniers = new ArrayList<LinePanier>();
		List<FormPack> listFormPacks = new ArrayList<FormPack>();

		updateStatutOrdreAchat(idOrdreAchat);
		OrdreAchat ordreAchat = ordreAchatRepository.findById(idOrdreAchat).get();

		LinePanier linePanier = null;
		for (LigneDocument ligneOrdreAchat : ordreAchat.getLigneDocuments()) {
			linePanier = new LinePanier();

			linePanier.setDesignation(ligneOrdreAchat.getDesignation());
			linePanier.setPrix(ligneOrdreAchat.getPrix());
			linePanier.setQuantite(ligneOrdreAchat.getQuantite());
			linePanier.setRemise(ligneOrdreAchat.getRemise());
			linePanier.setRestant(getMaxQuantitePackOrdreAchatOnLivraison(ligneOrdreAchat.getPack(), ordreAchat));
			linePanier.setQuantiteMin(getMinQuantitePackOrdreAchat(ligneOrdreAchat.getPack(), ordreAchat));

			if (ligneOrdreAchat.getTaxe() != null) {
				Taxe taxe = ligneOrdreAchat.getTaxe();
				Taxe taxe1 = new Taxe();
				taxe1.setId(taxe.getId());
				taxe1.setAbreviation(taxe.getAbreviation());
				taxe1.setTaux(taxe.getTaux());
				taxe1.setNom(taxe.getNom());
				linePanier.setTaxe(taxe1);
			}

			linePanier.setIdPack(ligneOrdreAchat.getPack().getId());
			System.out.println("==Designation==" + linePanier.getDesignation());
			int i = 0;
			for (FormPack pack : listFormPacks) {
				if (pack.getIdPack() == linePanier.getIdPack()) {
					linePanier.setIndexPack(i);
				}
				i++;
			}

			if (linePanier.getIndexPack() == -1) {
				Pack pack = ligneOrdreAchat.getPack();

				FormPack formPack = new FormPack();
				formPack.setIdPack(pack.getId());
				formPack.setSku(pack.getProduit().getSku());
				formPack.setNombreUnite(pack.getNombreUnite());
				formPack.setIdProduit(pack.getProduit().getIdProduit());
				formPack.setDesignation(ligneOrdreAchat.getDesignation());
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
					if (produitStock.getStockage().getIdStockage() == ordreAchat.getStockage().getIdStockage()) {
						formPack.setDisponible(produitStock.getStockDisponible() / pack.getNombreUnite());
						formPack.setReel(produitStock.getStockReel() / pack.getNombreUnite());
						formPack.setAvenir(produitStock.getStockAvenir() / pack.getNombreUnite());
					}
				}

				listFormPacks.add(formPack);
				linePanier.setIndexPack(listFormPacks.size() - 1);
			}
			
			if (Etat.getDelete().equals(ligneOrdreAchat.getPack().getProduit().getEtat())) {
				linePanier.setIsDelete(true);
			}
			
			linePaniers.add(linePanier);
		}

		formOrdreAchat.setIdOrdreAchat(ordreAchat.getIdOrdreAchat());
		formOrdreAchat
				.setNomFournisseur((ordreAchat.getFournisseur() != null) ? ordreAchat.getFournisseur().getNom() : "");
		formOrdreAchat.setContact((ordreAchat.getFournisseur() != null)
				? fournisseurService.getFournissuer(ordreAchat.getFournisseur().getIdFournisseur())
				: null);
		formOrdreAchat.setCoutLivraison(ordreAchat.getCoutLivraison());
		formOrdreAchat.setDateCreation(ordreAchat.getDateCreation());
		formOrdreAchat.setDateLivraison(ordreAchat.getDateLivraison());
		formOrdreAchat.setDescription(ordreAchat.getDescription());
		formOrdreAchat.setIdStockage(ordreAchat.getStockage().getIdStockage());
		formOrdreAchat.setNomStockage(ordreAchat.getStockage().getNom());
		formOrdreAchat.setNote(ordreAchat.getNote());
		formOrdreAchat.setUtilisateur(ordreAchat.getUtilisateur());
		formOrdreAchat.setTaxe(linePaniers.get(0).getTaxe());
		formOrdreAchat.setStockage(ordreAchat.getStockage());

		formOrdreAchat.setStatut(ordreAchat.getStatut());
		formOrdreAchat.setColorStatut(ColorStatut.getColor(ordreAchat.getStatut()));

		// Choix de l'entreprise a afficher sur les documents
		if (!((List<Entreprise>) entrepriseRepository.findAll()).isEmpty()) {
			formOrdreAchat.setEntreprise(((List<Entreprise>) entrepriseRepository.findAll()).get(0));
		}

		formOrdreAchat.setLivraisons(generetedListLivraison(ordreAchat.getLivraisons()));

		float quantiteTotal = Methode.getQuantiteTotalDocument(ordreAchat.getLigneDocuments());
		float quantiteRestant = quantiteTotal;
		for (Livraison livraison : ordreAchat.getLivraisons()) {
			if (!Statut.getAnnuler().equals(livraison.getStatut())) {
				quantiteRestant -= Methode.getQuantiteTotalDocument(livraison.getLigneDocuments());
			}
		}

		formOrdreAchat.setQuantiteRestant(quantiteRestant);
		formOrdreAchat.setQuantiteTotal(quantiteTotal);
		if (quantiteRestant == 0) {
			formOrdreAchat.setStatutLivraison(Statut.getLivre());
		} else if (quantiteRestant >= quantiteTotal) {
			formOrdreAchat.setStatutLivraison(Statut.getNonlivre());
		} else {
			formOrdreAchat.setStatutLivraison(Statut.getPartiellementlivre());
		}

		formOrdreAchat.setLines(linePaniers);
		formOrdreAchat.setPacksPresent(listFormPacks);

		return formOrdreAchat;
	}

	private void updateStatutOrdreAchat(String idOrdreAchat) {
		OrdreAchat ordreAchat = ordreAchatRepository.findById(idOrdreAchat).get();
		if (!Statut.getAnnuler().equals(ordreAchat.getStatut())) {
			float quantiteTotal = Methode.getQuantiteTotalDocument(ordreAchat.getLigneDocuments());
			float quantiteRestant = quantiteTotal;
			for (Livraison livraison : ordreAchat.getLivraisons()) {
				if (!Statut.getAnnuler().equals(livraison.getStatut())) {
					quantiteRestant -= Methode.getQuantiteTotalDocument(livraison.getLigneDocuments());
				}
			}
			if (quantiteRestant == 0) {
				ordreAchat.setStatut(Statut.getLivre());
			} else if (quantiteRestant >= quantiteTotal) {
				ordreAchat.setStatut(Statut.getNonlivre());
			} else {
				ordreAchat.setStatut(Statut.getPartiellementlivre());
			}
			ordreAchatRepository.save(ordreAchat);
		}

	}

	public FormTable generetedListLivraison(List<Livraison> livraisons) {
		List<RowTable> rows = new ArrayList<RowTable>();
		SimpleDateFormat formater = new SimpleDateFormat("dd MMMM yyyy");
		DecimalFormat df = new DecimalFormat("###,###,###,###.##");
		for (Livraison livraison : livraisons) {
			if (livraison.getEtat().equals(Etat.getActif())) {
				RowTable row = new RowTable();
				List<String> dataList = new ArrayList<String>();
				dataList.add(livraison.getIdLivraison());
				// dataList.add((facture.getClient() != null) ? facture.getClient().getNom() :
				// null);
				dataList.add(livraison.getOrdreAchat().getStockage().getNom());
				dataList.add(formater.format(livraison.getDateCreation()));
				dataList.add(
						(livraison.getDateLivraison() != null) ? formater.format(livraison.getDateLivraison()) : "");
				dataList.add(df.format(Methode.getQuantiteTotalDocument(livraison.getLigneDocuments())));
				// DecimalFormat.getCurrencyInstance().format( montantTotalFacture(facture))
				row.setData(dataList);
				row.setStatut(livraison.getStatut());
				row.setColorStatut(ColorStatut.getColor(livraison.getStatut()));
				row.setId(livraison.getIdLivraison());
				rows.add(row);
			}
		}

		List<String> colunm = new ArrayList<String>();
		colunm.add("LIVRAISON #");
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

	// quantite de produits(packProduit) Maximun qu'on peut avoir dans une
	// livraison cree a partir d'un ordeAchat
	public float getMaxQuantitePackOrdreAchatOnLivraison(Pack pack, OrdreAchat ordreAchat) {
		LigneDocument ligneDocument = new LigneDocument();
		for (LigneDocument ligneDocument2 : ordreAchat.getLigneDocuments()) {
			if (ligneDocument2.getPack().getId() == pack.getId()) {
				ligneDocument = ligneDocument2;
			}
		}

		float quatiteMax = ligneDocument.getQuantite();
		List<Livraison> livraisons = ordreAchat.getLivraisons();

		for (Livraison livraison : livraisons) {
			if (!livraison.getStatut().equals(Statut.getAnnuler()) && !livraison.getEtat().equals(Etat.getDelete())) {
				List<LigneDocument> ligneBonLivraisons = livraison.getLigneDocuments();
				for (LigneDocument ligneBonLivraison : ligneBonLivraisons) {
					if (pack.getId() == ligneBonLivraison.getPack().getId()) {
						quatiteMax -= ligneBonLivraison.getQuantite();
					}
				}
			}
		}

		return quatiteMax;
	}

	public float getMinQuantitePackOrdreAchat(Pack pack, OrdreAchat ordreAchat) {
		float minLivraison = 0;
		for (Livraison livraison : ordreAchat.getLivraisons()) {
			if (!livraison.getStatut().equals(Statut.getAnnuler()) && !livraison.getEtat().equals(Etat.getDelete())) {
				for (LigneDocument ligneDocument : livraison.getLigneDocuments()) {
					if (ligneDocument.getPack().getId() == pack.getId()) {
						minLivraison += ligneDocument.getQuantite();
					}
				}
			}
		}

		return minLivraison;
	}

	public void controlUpdateOrdreAchat(FormPanier formPanier, OrdreAchat ordreAchat) {
		List<LinePanier> lines = formPanier.getLines();

		for (LinePanier linePanier : lines) {
			if (getMinQuantitePackOrdreAchat(packRepository.findById(linePanier.getIdPack()).get(),
					ordreAchat) > linePanier.getQuantite()) {
				throw new StreengeException(new ErrorAPI("Vous avez deja receptioner "
						+ getMinQuantitePackOrdreAchat(packRepository.findById(linePanier.getIdPack()).get(),
								ordreAchat)
						+ " " + linePanier.getDesignation()
						+ " ,vous ne pouvais pas en mettre moins pour modification"));
			}
		}
	}

	public FormPanier getpanierToCreateLivraison(String idOrdreAchat) {
		FormPanier formPanier = this.getPanierOrdreAchat(idOrdreAchat);
		List<LinePanier> lines = new ArrayList<LinePanier>();
		OrdreAchat bonCommande = ordreAchatRepository.findById(idOrdreAchat).get();

		for (LinePanier linePanier : formPanier.getLines()) {
			linePanier.setQuantite(this.getMaxQuantitePackOrdreAchatOnLivraison(
					packRepository.findById(linePanier.getIdPack()).get(), bonCommande));
			linePanier.setQuantiteMax(linePanier.getQuantite());
			if (linePanier.getQuantiteMax() > 0) {
				linePanier.setQuantiteMin(-1);
				lines.add(linePanier);
			}
		}

		for (Livraison livraison : bonCommande.getLivraisons()) {
			formPanier.setCoutLivraison(formPanier.getCoutLivraison() - livraison.getCoutLivraison());
		}

		formPanier.setIdDevis("");
		formPanier.setLines(lines);
		formPanier.setAddLine(false);
		return formPanier;
	}

	public Livraison createLivraisonFromOrdreAchat(FormPanier formPanier) {
		if (ordreAchatRepository.findById(formPanier.getIdOrdreAchat()).isPresent()) {

			Livraison livraison = livraisonService.createOrUpdateLivraison(formPanier, true);
			OrdreAchat ordreAchat = ordreAchatRepository.findById(formPanier.getIdOrdreAchat()).get();
			// devis.setStatut(Statut.getAcceptertransformer());
			List<Livraison> livraisons = new ArrayList<Livraison>();
			livraisons.add(livraison);
			ordreAchat.setLivraisons(livraisons);
			ordreAchatRepository.save(ordreAchat);
			return livraison;
		} else {
			throw new StreengeException(
					new ErrorAPI("Impossible de recevoir les produits á partir de cette ordre achat"));
		}
	}

	public void cancelOrdreAchat(String idOrdreAchat,int idUtilisateur) {
		if (ordreAchatRepository.findById(idOrdreAchat).isPresent()) {
			OrdreAchat ordreAchat = ordreAchatRepository.findById(idOrdreAchat).get();
			List<ProduitStockage> produitStockagesBefore = Methode
					.getProduitStockagesEager((List<ProduitStockage>) produitStockageRepository.findAll());

			
			if (!Statut.getAnnuler().equals(ordreAchat.getStatut())) {
				List<Livraison> livraisons = ordreAchat.getLivraisons();

				for (int i = 0; i < livraisons.size(); i++) {
					livraisonService.cancelLivraison(livraisons.get(i).getIdLivraison(),idUtilisateur);
				}

				for (LigneDocument ligne : ordreAchat.getLigneDocuments()) {
					produitService.updateProduitQuantiteAvenir(ligne.getPack(), (ligne.getQuantite() * -1),
							ordreAchat.getStockage());
				}
				ordreAchat.setStatut(Statut.getAnnuler());
				ordreAchatRepository.save(ordreAchat);
				
				registreStockService.updateRegistreStock(produitStockagesBefore, "Annulation de l'ordre d'achat.",
						idUtilisateur, idOrdreAchat);
			}
		}
	}

	public void reactiveOrdreAchat(String idOrdreAchat,int idUtilisateur) {
		if (ordreAchatRepository.findById(idOrdreAchat).isPresent()) {
			OrdreAchat ordreAchat = ordreAchatRepository.findById(idOrdreAchat).get();
			List<ProduitStockage> produitStockagesBefore = Methode
					.getProduitStockagesEager((List<ProduitStockage>) produitStockageRepository.findAll());
			if (Statut.getAnnuler().equals(ordreAchat.getStatut()) && !Etat.getDelete().equals(ordreAchat.getEtat())) {
				for (LigneDocument ligne : ordreAchat.getLigneDocuments()) {
					float restantToRecieve = this.getMaxQuantitePackOrdreAchatOnLivraison(ligne.getPack(), ordreAchat);
					produitService.updateProduitQuantiteAvenir(ligne.getPack(), (restantToRecieve),
							ordreAchat.getStockage());
				}
				ordreAchat.setStatut(Statut.getNonlivre());
				ordreAchatRepository.save(ordreAchat);
				
				registreStockService.updateRegistreStock(produitStockagesBefore, "Réactivation de l'ordre d'achat.",
						idUtilisateur, idOrdreAchat);
			}
		}
	}

	public void deleteOrdreAchat(String idOrdreAchat,int idUtilisateur) {
		if (ordreAchatRepository.findById(idOrdreAchat).isPresent()) {
			List<ProduitStockage> produitStockagesBefore = Methode
					.getProduitStockagesEager((List<ProduitStockage>) produitStockageRepository.findAll());
			OrdreAchat ordreAchat = ordreAchatRepository.findById(idOrdreAchat).get();
			cancelOrdreAchat(idOrdreAchat,idUtilisateur);
			List<Livraison> livraisons = ordreAchat.getLivraisons();
			for (int i = 0; i < livraisons.size(); i++) {
				livraisonService.deleteLivraison(livraisons.get(i).getIdLivraison(),idUtilisateur);
			}
			ordreAchat.setEtat(Etat.getDelete());
			ordreAchatRepository.save(ordreAchat);
			registreStockService.updateRegistreStock(produitStockagesBefore, "Supression de l'ordre d'achat.",
					idUtilisateur, idOrdreAchat);
		}
	}

}
