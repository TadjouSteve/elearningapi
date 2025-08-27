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
import com.streenge.model.admin.Utilisateur;
import com.streenge.model.produit.Pack;
import com.streenge.model.produit.ProduitStockage;
import com.streenge.model.stock.MouvementStock;
import com.streenge.repository.LigneDocumentRepository;
import com.streenge.repository.adminRepository.StockageRepository;
import com.streenge.repository.produitRepo.PackRepository;
import com.streenge.repository.produitRepo.ProduitStockageRepository;
import com.streenge.repository.stockRepo.MouvementStockRepository;
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
public class MouvementStockService {

	@Autowired
	private StockageRepository stockageRepository;

	@Autowired
	private MouvementStockRepository mouvementStockRepository;

	@Autowired
	private LigneDocumentRepository ligneDocumentRepository;

	@Autowired
	private ProduitService produitService;

	@Autowired
	private PackRepository packRepository;

	@Autowired
	private RegistreStockService registreStockService;

	@Autowired
	private ProduitStockageRepository produitStockageRepository;

	public MouvementStock createOrUpdateEntreOrSortieStock(FormPanier formPanier, boolean isNewEntreOrSortieStock,
			boolean isEntreStock) {
		Methode.controlFormPanier(formPanier);
		List<ProduitStockage> produitStockagesBefore = Methode
				.getProduitStockagesEager((List<ProduitStockage>) produitStockageRepository.findAll());

		Utilisateur utilisateur = formPanier.getUtilisateur();
		MouvementStock entreOrSortieStock;
		if (isNewEntreOrSortieStock) {
			String prefix = isEntreStock ? "ES" : "SS";
			entreOrSortieStock = new MouvementStock();
			entreOrSortieStock.setIdMouvementStock(Methode.createID(mouvementStockRepository, prefix, 0));
			entreOrSortieStock.setDateCreation(new Date());
			entreOrSortieStock.setUtilisateur(formPanier.getUtilisateur());
		} else {

			entreOrSortieStock = mouvementStockRepository
					.findById(isEntreStock ? formPanier.getIdEntreStock() : formPanier.getIdSortieStock()).get();

			for (LigneDocument ligne : entreOrSortieStock.getLigneDocuments()) {
				produitService.updateProduitQuantite(ligne.getPack(), (ligne.getQuantite() * (isEntreStock ? -1 : 1)), // (isEntreStock?-1:1)
						entreOrSortieStock.getStockage(), true, true);
			}
			if (isEntreStock) {
				produitService.updateCoutUMP(mouvementStockRepository
						.findById(isEntreStock ? formPanier.getIdEntreStock() : formPanier.getIdSortieStock()).get()
						.getLigneDocuments(), false);
			}
			
			for (LigneDocument ligne : entreOrSortieStock.getLigneDocuments()) {
				ligneDocumentRepository.deleteLigneDocument(ligne.getId());
			}

			entreOrSortieStock.setDateModification(new Date());
		}

		entreOrSortieStock.setDescription(formPanier.getDescription());
		entreOrSortieStock.setNote(formPanier.getNote());

		if (isNewEntreOrSortieStock) {
			entreOrSortieStock.setStatut(Statut.getTerminer());
			entreOrSortieStock.setEtat(Etat.getActif());
		}

		if (stockageRepository.findById(formPanier.getIdStockage()).isPresent()) {
			entreOrSortieStock.setStockage(stockageRepository.findById(formPanier.getIdStockage()).get());
		} else {
			ErrorAPI errorAPI = new ErrorAPI();
			errorAPI.setMessage(
					"L'enregistrement de cette operation n'est pas possible, Le stockage choisi N'existe pas...! Changer de lieu de stockage et cree une nouvelle facture..! ");
			throw new StreengeException(errorAPI);
		}

		List<LigneDocument> ligneDocuments = new ArrayList<LigneDocument>();
		List<LinePanier> lines = formPanier.getLines();
		for (LinePanier line : lines) {

			LigneDocument ligneBonCommane = new LigneDocument();
			ligneBonCommane.setMouvementStock(entreOrSortieStock);
			ligneBonCommane.setDesignation(line.getDesignation());
			ligneBonCommane.setPrix(line.getPrix());
			ligneBonCommane.setQuantite(line.getQuantite());

			if (packRepository.findById(line.getIdPack()).isPresent()) {
				ligneBonCommane.setPack(packRepository.findById(line.getIdPack()).get());
			} else {
				ErrorAPI errorAPI = new ErrorAPI();
				errorAPI.setMessage("L'enregistrement de cette operation n'est pas possible,Le produit: "
						+ line.getDesignation().toUpperCase()
						+ " N'existe pas où alors ce produit à été récemment supprimer");
				throw new StreengeException(errorAPI);
			}
			ligneDocuments.add(ligneBonCommane);
		}
		entreOrSortieStock.setLigneDocuments(ligneDocuments);

		for (LigneDocument ligne : entreOrSortieStock.getLigneDocuments()) {
			produitService.updateProduitQuantite(ligne.getPack(), (ligne.getQuantite() * (isEntreStock ? 1 : -1)),
					entreOrSortieStock.getStockage(), true, true);
		}

		try {
			entreOrSortieStock = mouvementStockRepository.save(entreOrSortieStock);
		} catch (DataIntegrityViolationException e) {
			System.out.println("Entre Or sortie stock already exist");
		}

		if (isEntreStock) {
			registreStockService.updateRegistreStock(produitStockagesBefore,
					isNewEntreOrSortieStock ? "Création de l'Entrée de stock" : "Modification de l'Entrée de stock",
					utilisateur, entreOrSortieStock.getIdMouvementStock());
			produitService.updateCoutUMP(entreOrSortieStock.getLigneDocuments(), true);
		} else {
			registreStockService.updateRegistreStock(produitStockagesBefore,
					isNewEntreOrSortieStock ? "Création de la Sortie de stock" : "Modification de la Sortie de stock",
					utilisateur, entreOrSortieStock.getIdMouvementStock());
		}

		return entreOrSortieStock;
	}

	public FormPanier getPanierManipulationStock(String idMouvementStock, Boolean isEntreStock,
			Boolean isMouvementEntreStock) {
		FormPanier formPanier = new FormPanier();
		List<LinePanier> linePaniers = new ArrayList<LinePanier>();
		List<FormPack> listFormPacks = new ArrayList<FormPack>();

		MouvementStock mouvementStock = mouvementStockRepository.findById(idMouvementStock).get();

		LinePanier linePanier = null;
		List<LigneDocument> ligneOrdreAchats = mouvementStock.getLigneDocuments();
		for (LigneDocument ligneOrdreAchat : ligneOrdreAchats) {
			linePanier = new LinePanier();

			linePanier.setDesignation(ligneOrdreAchat.getDesignation());
			linePanier.setPrix(ligneOrdreAchat.getPrix());
			linePanier.setQuantite(ligneOrdreAchat.getQuantite());

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
					if (produitStock.getStockage().getIdStockage() == mouvementStock.getStockage().getIdStockage()) {
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

		formPanier.setLines(linePaniers);
		formPanier.setPacksPresent(listFormPacks);

		if (isEntreStock) {
			formPanier.setIdEntreStock(mouvementStock.getIdMouvementStock());
		} else if (isMouvementEntreStock) {
			formPanier.setIdMouvementStock(idMouvementStock);
			formPanier.setNomStockageArriver(mouvementStock.getStockageArrive().getNom());
			formPanier.setIdStockageArriver(mouvementStock.getStockageArrive().getIdStockage());
		} else {
			formPanier.setIdSortieStock(mouvementStock.getIdMouvementStock());
		}

		formPanier.setDateCreation(mouvementStock.getDateCreation());
		formPanier.setDescription(mouvementStock.getDescription());
		formPanier.setIdStockage(mouvementStock.getStockage().getIdStockage());
		formPanier.setNomStockage(mouvementStock.getStockage().getNom());
		formPanier.setNote(mouvementStock.getNote());
		formPanier.setUtilisateur(mouvementStock.getUtilisateur());

		formPanier.setStatut(mouvementStock.getStatut());
		formPanier.setColorStatut(ColorStatut.getColor(mouvementStock.getStatut()));

		return formPanier;
	}

	public FormTable generateListEntreOrSortieStock(String filter, boolean isEntreStock) {
		String prefix = isEntreStock ? "ES" : "SS";
		List<RowTable> rows = new ArrayList<RowTable>();
		SimpleDateFormat formater = new SimpleDateFormat("dd MMMM yyyy");
		DecimalFormat df = new DecimalFormat("###,###,###,###.##");
		List<MouvementStock> entreOrSortieStocks = mouvementStockRepository
				.findAllByIdMouvementStockStartingWithOrderByIdMouvementStockDesc(prefix);

		for (MouvementStock mouvement : entreOrSortieStocks) {
			if (!Etat.getDelete().equals(mouvement.getEtat())) {
				RowTable row = new RowTable();
				List<String> dataList = new ArrayList<String>();
				dataList.add(mouvement.getIdMouvementStock());
				dataList.add(mouvement.getStockage().getNom());
				dataList.add(formater.format(mouvement.getDateCreation()));
				dataList.add(df.format(Methode.getQuantiteTotalDocument(mouvement.getLigneDocuments())));

				row.setData(dataList);
				row.setStatut(mouvement.getStatut());
				row.setColorStatut(ColorStatut.getColor(mouvement.getStatut()));
				row.setId(mouvement.getIdMouvementStock());
				row.setIdUtilisateur((mouvement.getUtilisateur()!=null)?mouvement.getUtilisateur().getId():0);
				row.setIdStockage(mouvement.getStockage().getIdStockage());
				rows.add(row);
			}

		}
		List<String> colunm = new ArrayList<String>();
		colunm.add((isEntreStock ? "ENTRÉE DE STOCK #" : "SORTIE DE STOCK"));// (isEntreStock?"ENTRÉE DE STOCK
																				// #":"SORTIE DE STOCK")
		colunm.add("LIEU DE STOCKAGE");
		colunm.add("DATE DE CRÉATION");
		colunm.add("QUANTITÉS");

		FormTable formTable = new FormTable();
		formTable.setColunm(colunm);
		formTable.setRows(rows);

		return formTable;
	}

	public void cancelEntreOrSortieStock(String idEntreOrSortieStock, Boolean isEntreStock, int idUtilisateur) {
		if (mouvementStockRepository.findById(idEntreOrSortieStock).isPresent()) {
			List<ProduitStockage> produitStockagesBefore = Methode
					.getProduitStockagesEager((List<ProduitStockage>) produitStockageRepository.findAll());

			MouvementStock entreOrSortieStock = mouvementStockRepository.findById(idEntreOrSortieStock).get();
			if (!Statut.getAnnuler().equals(entreOrSortieStock.getStatut())) {
				for (LigneDocument ligne : entreOrSortieStock.getLigneDocuments()) {
					produitService.updateProduitQuantite(ligne.getPack(),
							(ligne.getQuantite() * (isEntreStock ? -1 : 1)), // (isEntreStock?-1:1)
							entreOrSortieStock.getStockage(), true, true);
				}
				entreOrSortieStock.setStatut(Statut.getAnnuler());
				mouvementStockRepository.save(entreOrSortieStock);

				registreStockService.updateRegistreStock(produitStockagesBefore,
						isEntreStock ? "Annulation de l'Entrée de stock" : "Annulation de la Sortie de stock",
						idUtilisateur, entreOrSortieStock.getIdMouvementStock());

				if (isEntreStock) {
					produitService.updateCoutUMP(entreOrSortieStock.getLigneDocuments(), false);
				}

			}
		}
	}

	public void reactiveEntreOrSortieStock(String idEntreOrSortieStock, Boolean isEntreStock, int idUtilisateur) {
		if (mouvementStockRepository.findById(idEntreOrSortieStock).isPresent()) {
			MouvementStock entreOrSortieStock = mouvementStockRepository.findById(idEntreOrSortieStock).get();

			List<ProduitStockage> produitStockagesBefore = Methode
					.getProduitStockagesEager((List<ProduitStockage>) produitStockageRepository.findAll());
			if (Statut.getAnnuler().equals(entreOrSortieStock.getStatut())
					&& !Etat.getDelete().equals(entreOrSortieStock.getEtat())) {
				for (LigneDocument ligne : entreOrSortieStock.getLigneDocuments()) {
					produitService.updateProduitQuantite(ligne.getPack(),
							(ligne.getQuantite() * (isEntreStock ? 1 : -1)), // (isEntreStock?-1:1)
							entreOrSortieStock.getStockage(), true, true);
				}
				entreOrSortieStock.setStatut(Statut.getTerminer());
				mouvementStockRepository.save(entreOrSortieStock);

				registreStockService.updateRegistreStock(produitStockagesBefore,
						isEntreStock ? "Reactivation de l'Entrée de stock" : "Reactivation de la Sortie de stock",
						idUtilisateur, entreOrSortieStock.getIdMouvementStock());
				if (isEntreStock) {
					produitService.updateCoutUMP(entreOrSortieStock.getLigneDocuments(), true);
				}
			}
		}
	}

	public MouvementStock createOrUpdateMouvementEntreStock(FormPanier formPanier, boolean isNewMouvementStock) {
		Methode.controlFormPanier(formPanier);
		List<ProduitStockage> produitStockagesBefore = Methode
				.getProduitStockagesEager((List<ProduitStockage>) produitStockageRepository.findAll());

		Utilisateur utilisateur = formPanier.getUtilisateur();
		if (formPanier.getIdStockageArriver() == 0) {
			throw new StreengeException(new ErrorAPI("Vous devez choisir un stockage d'arriver...!"));
		}

		if (formPanier.getIdStockageArriver() == formPanier.getIdStockage()) {
			throw new StreengeException(
					new ErrorAPI("Le Stockage de depart dois être different du stockage d'arriver"));
		}

		MouvementStock mouvementStock;
		if (isNewMouvementStock) {
			String prefix = "MS";
			mouvementStock = new MouvementStock();
			mouvementStock.setIdMouvementStock(Methode.createID(mouvementStockRepository, prefix, 0));
			mouvementStock.setDateCreation(new Date());
			mouvementStock.setUtilisateur(formPanier.getUtilisateur());
		} else {

			mouvementStock = mouvementStockRepository.findById(formPanier.getIdMouvementStock()).get();
			for (LigneDocument ligne : mouvementStock.getLigneDocuments()) {

				produitService.updateProduitQuantite(ligne.getPack(), (ligne.getQuantite() * (1)),
						mouvementStock.getStockage(), true, true); // on recharge lestock de depart;
				produitService.updateProduitQuantite(ligne.getPack(), (ligne.getQuantite() * (-1)),
						mouvementStock.getStockageArrive(), true, true); // on retire les produit du stock d'arriver;
				ligneDocumentRepository.deleteLigneDocument(ligne.getId());
			}
			mouvementStock.setDateModification(new Date());
		}

		mouvementStock.setDescription(formPanier.getDescription());
		mouvementStock.setNote(formPanier.getNote());

		if (isNewMouvementStock) {
			mouvementStock.setStatut(Statut.getTerminer());
			mouvementStock.setEtat(Etat.getActif());
		}

		if (stockageRepository.findById(formPanier.getIdStockage()).isPresent()) {
			mouvementStock.setStockage(stockageRepository.findById(formPanier.getIdStockage()).get());
		} else {
			ErrorAPI errorAPI = new ErrorAPI();
			errorAPI.setMessage(
					"L'enregistrement de cette operation n'est pas possible, Le stockage choisi N'existe pas...! Changer de lieu de stockage et cree un nouveau document..! ");
			throw new StreengeException(errorAPI);
		}

		if (stockageRepository.findById(formPanier.getIdStockageArriver()).isPresent()) {
			mouvementStock.setStockageArrive(stockageRepository.findById(formPanier.getIdStockageArriver()).get());
		} else {
			ErrorAPI errorAPI = new ErrorAPI();
			errorAPI.setMessage(
					"L'enregistrement de cette operation n'est pas possible, Le stockage d'arriver choisi N'existe pas...! Changer stockage d'arriver et cree un nouveau document..! ");
			throw new StreengeException(errorAPI);
		}

		List<LigneDocument> ligneDocuments = new ArrayList<LigneDocument>();
		List<LinePanier> lines = formPanier.getLines();
		for (LinePanier line : lines) {

			LigneDocument ligneBonCommane = new LigneDocument();
			ligneBonCommane.setMouvementStock(mouvementStock);
			ligneBonCommane.setDesignation(line.getDesignation());
			ligneBonCommane.setPrix(line.getPrix());
			ligneBonCommane.setQuantite(line.getQuantite());

			if (packRepository.findById(line.getIdPack()).isPresent()) {
				ligneBonCommane.setPack(packRepository.findById(line.getIdPack()).get());
			} else {
				ErrorAPI errorAPI = new ErrorAPI();
				errorAPI.setMessage("L'enregistrement de l'entre de stock n'est pas possible,Le produit: "
						+ line.getDesignation().toUpperCase()
						+ " N'existe pas où alors ce produit à été récemment supprimer");
				throw new StreengeException(errorAPI);
			}
			ligneDocuments.add(ligneBonCommane);
		}
		mouvementStock.setLigneDocuments(ligneDocuments);

		for (LigneDocument ligne : mouvementStock.getLigneDocuments()) {
			produitService.updateProduitQuantite(ligne.getPack(), (ligne.getQuantite() * (-1)),
					mouvementStock.getStockage(), true, true);// on retire du stock de depart,
			produitService.updateProduitQuantite(ligne.getPack(), (ligne.getQuantite() * (1)),
					mouvementStock.getStockageArrive(), true, true);// on charge le stock de d'arriver,
		}

		try {
			mouvementStock = mouvementStockRepository.save(mouvementStock);
		} catch (DataIntegrityViolationException e) {
			System.out.println("Entre Or sortie stock already exist");
		}

		registreStockService.updateRegistreStock(produitStockagesBefore,
				isNewMouvementStock ? "Création du mouvement de stock" : "Modification du mouvement de stock",
				utilisateur, mouvementStock.getIdMouvementStock());

		return mouvementStock;
	}

	public FormTable generateListMouvementStock(String filter) {
		String prefix = "MS";
		List<RowTable> rows = new ArrayList<RowTable>();
		SimpleDateFormat formater = new SimpleDateFormat("dd MMMM yyyy");
		DecimalFormat df = new DecimalFormat("###,###,###,###.##");
		List<MouvementStock> entreOrSortieStocks = mouvementStockRepository
				.findAllByIdMouvementStockStartingWithOrderByIdMouvementStockDesc(prefix);

		for (MouvementStock mouvement : entreOrSortieStocks) {
			if (!Etat.getDelete().equals(mouvement.getEtat())) {
				RowTable row = new RowTable();
				List<String> dataList = new ArrayList<String>();
				dataList.add(mouvement.getIdMouvementStock());
				dataList.add(mouvement.getStockage().getNom());
				dataList.add(mouvement.getStockageArrive().getNom());
				dataList.add(formater.format(mouvement.getDateCreation()));
				dataList.add(df.format(Methode.getQuantiteTotalDocument(mouvement.getLigneDocuments())));

				row.setData(dataList);
				row.setStatut(mouvement.getStatut());
				row.setColorStatut(ColorStatut.getColor(mouvement.getStatut()));
				row.setId(mouvement.getIdMouvementStock());
				rows.add(row);
			}

		}
		List<String> colunm = new ArrayList<String>();
		colunm.add(("MOUVEMENT DE STOCK #"));// (isEntreStock?"ENTRÉE DE STOCK #":"SORTIE DE STOCK")
		colunm.add("DÉPART");
		colunm.add("ARRIVÉE");
		colunm.add("DATE DE CRÉATION");
		colunm.add("QUANTITÉS");

		FormTable formTable = new FormTable();
		formTable.setColunm(colunm);
		formTable.setRows(rows);

		return formTable;
	}

	public void cancelMouvementEntreStock(String idMouvementStock, int idUtilisateur) {
		if (mouvementStockRepository.findById(idMouvementStock).isPresent()) {
			MouvementStock mouvementStock = mouvementStockRepository.findById(idMouvementStock).get();

			List<ProduitStockage> produitStockagesBefore = Methode
					.getProduitStockagesEager((List<ProduitStockage>) produitStockageRepository.findAll());
			if (!Statut.getAnnuler().equals(mouvementStock.getStatut())) {
				for (LigneDocument ligne : mouvementStock.getLigneDocuments()) {
					produitService.updateProduitQuantite(ligne.getPack(), (ligne.getQuantite() * (1)),
							mouvementStock.getStockage(), true, true);// on recharge le stock de depart,
					produitService.updateProduitQuantite(ligne.getPack(), (ligne.getQuantite() * (-1)),
							mouvementStock.getStockageArrive(), true, true);// on retire les produit du stock d'arriver;
				}
				mouvementStock.setStatut(Statut.getAnnuler());
				mouvementStockRepository.save(mouvementStock);

				registreStockService.updateRegistreStock(produitStockagesBefore, "Annulation du mouvement de stock",
						idUtilisateur, idMouvementStock);
			}
		}
	}

	public void reactiveMouvementEntreStock(String idMouvementStock, int idUtilisateur) {
		if (mouvementStockRepository.findById(idMouvementStock).isPresent()) {
			MouvementStock mouvementStock = mouvementStockRepository.findById(idMouvementStock).get();

			List<ProduitStockage> produitStockagesBefore = Methode
					.getProduitStockagesEager((List<ProduitStockage>) produitStockageRepository.findAll());
			if (Statut.getAnnuler().equals(mouvementStock.getStatut())
					&& !Etat.getDelete().equals(mouvementStock.getEtat())) {
				for (LigneDocument ligne : mouvementStock.getLigneDocuments()) {
					produitService.updateProduitQuantite(ligne.getPack(), (ligne.getQuantite() * (-1)),
							mouvementStock.getStockage(), true, true);
					produitService.updateProduitQuantite(ligne.getPack(), (ligne.getQuantite() * (1)),
							mouvementStock.getStockageArrive(), true, true);
				}
				mouvementStock.setStatut(Statut.getTerminer());
				mouvementStockRepository.save(mouvementStock);

				registreStockService.updateRegistreStock(produitStockagesBefore, "Réactivation du mouvement de stock",
						idUtilisateur, idMouvementStock);
			}
		}
	}

}
