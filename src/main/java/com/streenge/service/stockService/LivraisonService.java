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
import com.streenge.repository.adminRepository.TaxeRepository;
import com.streenge.repository.produitRepo.PackRepository;
import com.streenge.repository.produitRepo.ProduitStockageRepository;
import com.streenge.repository.stockRepo.LivraisonRepository;
import com.streenge.repository.stockRepo.OrdreAchatRepository;
import com.streenge.service.contactService.FournisseurService;
import com.streenge.service.produitService.ProduitService;
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
public class LivraisonService {
	@Autowired
	private LivraisonRepository livraisonRepository;

	@Autowired
	private OrdreAchatRepository ordreAchatRepository;

	@Autowired
	private LigneDocumentRepository ligneDocumentRepository;

	@Autowired
	private TaxeRepository taxeRepository;

	@Autowired
	private ProduitService produitService;

	@Autowired
	private PackRepository packRepository;

	@Autowired
	private EntrepriseRepository entrepriseRepository;

	@Autowired
	private OrdreAchatService ordreAchatService;

	@Autowired
	private FournisseurService fournisseurService;
	
	@Autowired
	private RegistreStockService registreStockService;

	@Autowired
	private ProduitStockageRepository produitStockageRepository;

	public Livraison createOrUpdateLivraison(FormPanier formPanier, boolean isNewlivraison) {
		Methode.controlFormPanier(formPanier);
		List<ProduitStockage> produitStockagesBefore = Methode
				.getProduitStockagesEager((List<ProduitStockage>) produitStockageRepository.findAll());

		Utilisateur utilisateur = formPanier.getUtilisateur();
		Livraison livraison;
		if (isNewlivraison) {
			livraison = new Livraison();
			livraison.setIdLivraison(Methode.createID(livraisonRepository, "LV"));

			livraison.setOrdreAchat(ordreAchatRepository.findById(formPanier.getIdOrdreAchat()).get());
			livraison.setEtat(Etat.getActif());
			livraison.setStatut(Statut.getTerminer());
			livraison.setUtilisateur(utilisateur);
			formPanier.setDateCreation(new Date());
		} else {
			livraison = livraisonRepository.findById(formPanier.getIdLivraison()).get();
			this.controlUpdateLivraison(livraison);
			for (LigneDocument ligne : livraison.getLigneDocuments()) {
				produitService.updateProduitQuantiteAvenir(ligne.getPack(), ligne.getQuantite(),
						livraison.getOrdreAchat().getStockage());
				produitService.updateProduitQuantite(ligne.getPack(), (ligne.getQuantite() * -1),
						livraison.getOrdreAchat().getStockage(), true, true);
			}
			
			if (!isNewlivraison) {
				produitService.updateCoutUMP(livraisonRepository.findById(formPanier.getIdLivraison()).get().getLigneDocuments(),false);
			}
			
			for (LigneDocument ligne : livraison.getLigneDocuments()) {
				ligneDocumentRepository.deleteLigneDocument(ligne.getId());
			}
			
			livraison.setDateModification(new Date());
		}

		livraison.setNote(formPanier.getNote());
		livraison.setDateCreation(formPanier.getDateCreation());
		livraison.setDateLivraison(formPanier.getDateLivraison());
		livraison.setDescription(formPanier.getDescription());
		// bonLivraison.setSoumettreA(formPanier.getSoumettreA());

		List<LigneDocument> ligneLivraisons = new ArrayList<LigneDocument>();
		// LigneDocument ligneLivraison = null;
		// List<LinePanier> lines = formPanier.getLines();
		for (LinePanier line : formPanier.getLines()) {
			LigneDocument ligneLivraison = new LigneDocument();
			ligneLivraison.setLivraison(livraison);
			ligneLivraison.setDesignation(line.getDesignation());
			ligneLivraison.setPrix(line.getPrix());
			ligneLivraison.setQuantite(line.getQuantite());
			ligneLivraison.setRemise(line.getRemise());

			System.out.println(
					"==Reception produit==" + ligneLivraison.getDesignation() + "||" + ligneLivraison.getQuantite());
			if (line.getTaxe() != null)
				ligneLivraison.setTaxe(taxeRepository.findById(line.getTaxe().getId()).get());

			if (packRepository.findById(line.getIdPack()).isPresent()) {
				ligneLivraison.setPack(packRepository.findById(line.getIdPack()).get());
			} else {
				ErrorAPI errorAPI = new ErrorAPI();
				errorAPI.setMessage("L'enregistrement du bon de livraison n'est pas possible,Le produit: "
						+ line.getDesignation().toUpperCase()
						+ " N'existe pas où alors ce produit à été récemment supprimer");
				throw new StreengeException(errorAPI);
			}
			ligneLivraisons.add(ligneLivraison);
		}

		livraison.setLigneDocuments(ligneLivraisons);

		for (LigneDocument ligne : ligneLivraisons) {
			produitService.updateProduitQuantiteAvenir(ligne.getPack(), (ligne.getQuantite() * -1),
					livraison.getOrdreAchat().getStockage());
			produitService.updateProduitQuantite(ligne.getPack(), ligne.getQuantite(),
					livraison.getOrdreAchat().getStockage(), true, true);
		}
		

		try {
			livraison = livraisonRepository.save(livraison);
		} catch (DataIntegrityViolationException e) {
			System.out.println("bonLivraison already exist");
		}

		/*for (LigneDocument ligne : livraison.getLigneDocuments()) {
			System.out.println("==Reception produit 102==" + ligne.getDesignation() + "||" + ligne.getQuantite());
		}*/

		registreStockService.updateRegistreStock(produitStockagesBefore,
				isNewlivraison ? "Réception des produits" : "Modification réception des produits", utilisateur,
				livraison.getIdLivraison());
		
		produitService.updateCoutUMP(livraison.getLigneDocuments(),true);
		
		ordreAchatService.getPanierOrdreAchat(livraison.getOrdreAchat().getIdOrdreAchat());
		
		return livraison;
	}

	public void controlUpdateLivraison(Livraison livraison) {
		if (livraison.getStatut().equals(Statut.getAnnuler())) {
			throw new StreengeException(new ErrorAPI(
					"Vous ne pouvais pas Modifier un  document Annuler,pour pouvoir le modifier, il faut d'abord le reactiver...!"));
		}
	}

	public FormPanier getPanierLivraison(String idLivraison) {
		FormPanier formPanier = new FormPanier();
		List<LinePanier> linePaniers = new ArrayList<LinePanier>();
		List<FormPack> listFormPacks = new ArrayList<FormPack>();

		Livraison livraison = livraisonRepository.findById(idLivraison).get();

		OrdreAchat ordreAchat = livraison.getOrdreAchat();

		formPanier.setIdLivraison(idLivraison);
		formPanier.setIdOrdreAchat(ordreAchat.getIdOrdreAchat());
		formPanier.setAddLine(false);

		LinePanier linePanier = null;
		List<LigneDocument> ligneLivraisons = livraison.getLigneDocuments();
		for (LigneDocument ligneLivraison : ligneLivraisons) {
			linePanier = new LinePanier();
			linePanier.setPrix(ligneLivraison.getPrix());
			linePanier.setDesignation(ligneLivraison.getDesignation());
			linePanier.setQuantite(ligneLivraison.getQuantite());
			linePanier.setIdPack(ligneLivraison.getPack().getId());

			linePanier.setQuantiteMax(
					linePanier.getQuantite() + ordreAchatService.getMaxQuantitePackOrdreAchatOnLivraison(
							packRepository.findById(linePanier.getIdPack()).get(), ordreAchat));

			if (ligneLivraison.getTaxe() != null) {
				Taxe taxe = ligneLivraison.getTaxe();
				Taxe taxe1 = new Taxe();
				taxe1.setId(taxe.getId());
				taxe1.setAbreviation(taxe.getAbreviation());
				taxe1.setTaux(taxe.getTaux());
				taxe1.setNom(taxe.getNom());
				linePanier.setTaxe(taxe1);
			}
			System.out.println("==Designation==" + linePanier.getDesignation());
			int i = 0;
			for (FormPack pack : listFormPacks) {
				if (pack.getIdPack() == linePanier.getIdPack()) {
					linePanier.setIndexPack(i);
				}
				i++;
			}

			if (linePanier.getIndexPack() == -1) {
				Pack pack = ligneLivraison.getPack();
				FormPack formPack = new FormPack();
				formPack.setIdPack(pack.getId());
				formPack.setSku(pack.getProduit().getSku());
				formPack.setNombreUnite(pack.getNombreUnite());
				formPack.setIdProduit(pack.getProduit().getIdProduit());
				formPack.setDesignation(ligneLivraison.getDesignation());
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
			
			if (Etat.getDelete().equals(ligneLivraison.getPack().getProduit().getEtat())) {
				linePanier.setIsDelete(true);
			}
			linePaniers.add(linePanier);
		}

		if (!((List<Entreprise>) entrepriseRepository.findAll()).isEmpty()) {
			formPanier.setEntreprise(((List<Entreprise>) entrepriseRepository.findAll()).get(0));
		}

		formPanier.setNomFournisseur((ordreAchat.getFournisseur() != null) ? ordreAchat.getFournisseur().getNom() : "");
		formPanier.setContact((ordreAchat.getFournisseur() != null)
				? fournisseurService.getFournissuer(ordreAchat.getFournisseur().getIdFournisseur())
				: null);
		formPanier.setDateCreation(livraison.getDateCreation());
		formPanier.setDateModification(livraison.getDateModification());
		formPanier.setDateLivraison(livraison.getDateLivraison());
		formPanier.setDescription(livraison.getDescription());
		formPanier.setIdStockage(ordreAchat.getStockage().getIdStockage());
		formPanier.setNomStockage(ordreAchat.getStockage().getNom());
		formPanier.setNote(livraison.getNote());
		formPanier.setTaxe(linePaniers.get(0).getTaxe());
		formPanier.setUtilisateur(livraison.getUtilisateur());
		formPanier.setStockage(ordreAchat.getStockage());

		formPanier.setStatut(livraison.getStatut());
		formPanier.setColorStatut(ColorStatut.getColor(livraison.getStatut()));

		formPanier.setLines(linePaniers);
		formPanier.setPacksPresent(listFormPacks);

		return formPanier;
	}

	public FormTable generateListLivraison(String filter) {
		List<Livraison> livraisons = new ArrayList<Livraison>();
		List<RowTable> rows = new ArrayList<RowTable>();
		SimpleDateFormat formater = new SimpleDateFormat("dd MMMM yyyy");
		DecimalFormat df = new DecimalFormat("###,###,###,###.##");
		
		if (filter!=null) {
			livraisons = livraisonRepository.findByOrdreAchatFournisseurNomStartingWithOrderByIdLivraisonDesc(filter);
		}else {
			livraisons = livraisonRepository.findAllByOrderByIdLivraisonDesc();
		}
		
		for (Livraison livraison : livraisons) {
			if (livraison.getStockage()==null) {
				livraison.setStockage(livraison.getOrdreAchat().getStockage());
				livraisonRepository.save(livraison);
			}
			if (livraison.getEtat().equals(Etat.getActif())) {
				RowTable row = new RowTable();
				List<String> dataList = new ArrayList<String>();
				dataList.add(livraison.getIdLivraison());
				dataList.add((livraison.getOrdreAchat().getFournisseur() != null)
						? livraison.getOrdreAchat().getFournisseur().getNom()
						: null);
				dataList.add(livraison.getOrdreAchat().getStockage().getNom());
				dataList.add(formater.format(livraison.getDateCreation()));
				dataList.add(
						(livraison.getDateLivraison() != null) ? formater.format(livraison.getDateLivraison()) : null);
				dataList.add(df.format(Methode.getQuantiteTotalDocument(livraison.getLigneDocuments())));

				row.setData(dataList);
				row.setStatut(livraison.getStatut());
				row.setColorStatut(ColorStatut.getColor(livraison.getStatut()));
				row.setId(livraison.getIdLivraison());
				row.setIdUtilisateur((livraison.getUtilisateur()!=null)?livraison.getUtilisateur().getId():0);
				row.setIdStockage(((livraison.getOrdreAchat()!=null)?livraison.getOrdreAchat().getStockage().getIdStockage():0));
				rows.add(row);
			}
		}

		List<String> colunm = new ArrayList<String>();
		colunm.add("LIVRAISON #");
		colunm.add("FOURNISSEUR");
		colunm.add("LIEU DE STOCKAGE");
		colunm.add("DATE DE CRÉATION");
		colunm.add("DATE DE LIVRAISON");
		colunm.add("QUANTITÉS");

		FormTable formTable = new FormTable();
		formTable.setColunm(colunm);
		formTable.setRows(rows);

		return formTable;
	}

	public void cancelLivraison(String idLivraison,int idUtilisateur) {
		if (livraisonRepository.findById(idLivraison).isPresent()) {
			Livraison livraison = livraisonRepository.findById(idLivraison).get();
			List<ProduitStockage> produitStockagesBefore = Methode
					.getProduitStockagesEager((List<ProduitStockage>) produitStockageRepository.findAll());
			if (!Statut.getAnnuler().equals(livraison.getStatut())) {
				for (LigneDocument ligne : livraison.getLigneDocuments()) {
					produitService.updateProduitQuantiteAvenir(ligne.getPack(), ligne.getQuantite(),
							livraison.getOrdreAchat().getStockage());
					produitService.updateProduitQuantite(ligne.getPack(), (ligne.getQuantite() * -1),
							livraison.getOrdreAchat().getStockage(), true, true);
				}
				livraison.setStatut(Statut.getAnnuler());
				// livraison.setDateModification(new Date());
				livraisonRepository.save(livraison);
				
				registreStockService.updateRegistreStock(produitStockagesBefore, "Anullation de la réception de produits",
						idUtilisateur, idLivraison);
				produitService.updateCoutUMP(livraison.getLigneDocuments(),false);
			}
		}
	}

	public void reactiveLivraison(String idLivraison,int idUtilisateur) {
		if (livraisonRepository.findById(idLivraison).isPresent()) {
			Livraison livraison = livraisonRepository.findById(idLivraison).get();
			OrdreAchat ordreAchat = livraison.getOrdreAchat();
			List<ProduitStockage> produitStockagesBefore = Methode
					.getProduitStockagesEager((List<ProduitStockage>) produitStockageRepository.findAll());
			if (Statut.getAnnuler().equals(ordreAchat.getStatut())) {
				throw new StreengeException(
						new ErrorAPI("Il est impossible de Reactiver cette Livraison car son Ordre Acahat  ( "
								+ ordreAchat.getIdOrdreAchat()
								+ " ) a été annuler, Reactiver d'abord son Ordre d'achat"));
			}

			if (livraison.getStatut().equals(Statut.getAnnuler()) && !Etat.getDelete().equals(livraison.getEtat())) {

				List<LigneDocument> ligneDocuments = livraison.getLigneDocuments();
				for (LigneDocument ligneDocument : ligneDocuments) {
					if (ligneDocument.getQuantite() > ordreAchatService
							.getMaxQuantitePackOrdreAchatOnLivraison(ligneDocument.getPack(), ordreAchat)) {
						throw new StreengeException(new ErrorAPI(
								"Il est impossible de Reactiver cette Livraison car certains produit on déjâ été totalement livre (receptioner) dans d'autre Livraison du même ordre d'achtat ( "
										+ livraison.getIdLivraison() + " )"));
					}
				}
				for (LigneDocument ligne : livraison.getLigneDocuments()) {
					produitService.updateProduitQuantiteAvenir(ligne.getPack(), (ligne.getQuantite() * -1),
							livraison.getOrdreAchat().getStockage());
					produitService.updateProduitQuantite(ligne.getPack(), (ligne.getQuantite()),
							livraison.getOrdreAchat().getStockage(), true, true);
				}
				livraison.setStatut(Statut.getTerminer());
				livraisonRepository.save(livraison);
				
				registreStockService.updateRegistreStock(produitStockagesBefore, "Reactivation de la réception de produits",
						idUtilisateur, idLivraison);
				
				produitService.updateCoutUMP(livraison.getLigneDocuments(),true);
			}
		}
	}

	public void deleteLivraison(String idLivraison,int idUtilisateur) {
		if (livraisonRepository.findById(idLivraison).isPresent()) {
			cancelLivraison(idLivraison,idUtilisateur);
			Livraison livraison = livraisonRepository.findById(idLivraison).get();
			livraison.setEtat(Etat.getDelete());
		}
	}

}
