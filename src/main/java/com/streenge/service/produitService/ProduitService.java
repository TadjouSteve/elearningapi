package com.streenge.service.produitService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streenge.model.LigneDocument;
import com.streenge.model.admin.Stockage;
import com.streenge.model.admin.Utilisateur;
import com.streenge.model.produit.Categorie;
import com.streenge.model.produit.GroupeProduit;
import com.streenge.model.produit.Pack;
import com.streenge.model.produit.Produit;
import com.streenge.model.produit.ProduitStockage;
import com.streenge.repository.adminRepository.StockageRepository;
import com.streenge.repository.produitRepo.CategorieRepository;
import com.streenge.repository.produitRepo.GroupeProduitRepository;
import com.streenge.repository.produitRepo.GroupeRepository;
import com.streenge.repository.produitRepo.PackRepository;
import com.streenge.repository.produitRepo.ProduitRepository;
import com.streenge.repository.produitRepo.ProduitStockageRepository;
import com.streenge.repository.produitRepo.medicamentRepo.ComposantRepository;
import com.streenge.service.adminService.StockageService;
import com.streenge.service.produitService.medicament.MedicamentService;
import com.streenge.service.stockService.RegistreStockService;
import com.streenge.service.utils.erroclass.ErrorAPI;
import com.streenge.service.utils.erroclass.StreengeException;
import com.streenge.service.utils.formInt.FormCreateProduit;
import com.streenge.service.utils.formInt.FormFilter;
import com.streenge.service.utils.formOut.FormListProduits;
import com.streenge.service.utils.formOut.FormPack;
import com.streenge.service.utils.formOut.FormProduit;
import com.streenge.service.utils.formOut.FormStock;
import com.streenge.service.utils.formOut.FormUpdatePrice;
import com.streenge.service.utils.formOut.FormViewProduit;
import com.streenge.service.utils.formOut.RowTable;
import com.streenge.service.utils.formOut.StockagesAndCategoriesList;
import com.streenge.service.utils.streengeData.Data;
import com.streenge.service.utils.streengeData.Etat;
import com.streenge.service.utils.streengeData.Statut;
import com.streenge.service.utils.streengeFunction.Methode;

@Service
public class ProduitService {

	@Autowired
	private ProduitRepository produitRepository;

	@Autowired
	private CategorieServise categorieServise;

	@Autowired
	private StockageService stockageService;

	@Autowired
	private CategorieRepository categorieRepository;

	@Autowired
	private StockageRepository stockageRepository;

	@Autowired
	private PackRepository packRepository;

	@Autowired
	private ProduitStockageRepository produitStockageRepository;//

	@Autowired
	private RegistreStockService registreStockService;
	
	@Autowired
	private GroupeRepository groupeRepository;
	
	@Autowired
	private ComposantRepository composantRepository;
	
	@Autowired
	private MedicamentService medicamentService;
	@Autowired
	private GroupeService groupeService;
	@Autowired
	private GroupeProduitRepository groupeProduitRepository;

	@Transactional
	public FormListProduits getAllProduit(String filter) {
		return generateListProduits(filter);
	}

	@Transactional
	public FormViewProduit getProduit(String id) {
		Produit produit = produitRepository.findById(id).isPresent() ? produitRepository.findById(id).get()
				: new Produit();
		FormViewProduit formViewProduit = generateFormProduit(produit);

		return formViewProduit;
	}

	// @Transactional
	public Produit CreateProduit(FormCreateProduit formProduit) {
		ControlFromProduit(formProduit);
		List<ProduitStockage> produitStockagesBefore = (List<ProduitStockage>) produitStockageRepository.findAll();
		Produit produit = new Produit();
		if (formProduit.getNom().trim().length() < 3) {
			throw new StreengeException(
					new ErrorAPI("Le Non du produit ne doit pas êre vide, ou contenir moins de trois(03) Caractères"));
		}

		// produit=produitRepository.save(produit);
		produit.setProduitStockages(produit.getProduitStockages());
		Categorie categorie = categorieServise.getCategorie(formProduit.getCategorieID());
		System.out.println("INFO categorie NOM= " + categorie.getNom() + " || id= " + categorie.getId());
		Stockage stockage = stockageService.getStockage(formProduit.getStockageID());

		if (Data.typeService.equals(formProduit.getType())) {
			formProduit.setStockInitial(20000000);
		}

		ProduitStockage produitStockage = new ProduitStockage();
		produitStockage.setStockage(stockage);
		produitStockage.setEmplacement(formProduit.getEmplacement());
		produitStockage.setPointCommande(formProduit.getPointCommande());
		produitStockage.setStockAvenir(0);
		produitStockage.setStockDisponible(formProduit.getStockInitial());
		produitStockage.setStockReel(formProduit.getStockInitial());

		produit.addProduitStockage(produitStockage);
		produit.setCategorie(categorie);

		produit.setIdProduit(createID((int) produitRepository.count()));
		produit.setNom(Methode.upperCaseFirst(formProduit.getNom().trim()));
		produit.setCodeBarre(
				(formProduit.getCodeBarre().trim().length() > 5 ? formProduit.getCodeBarre().trim() : null));
		produit.setCout_UMP(formProduit.getCoutInitial());
		produit.setDateAjout(formProduit.getDateAjout());
		produit.setDescription(formProduit.getDescription().trim());
		produit.setEtat(Etat.getActif());
		produit.setType(formProduit.getType());
		produit.setNomUnite(formProduit.getNomUnite().trim());
		produit.setCoutInitial(formProduit.getCoutInitial());
		produit.setStockInitial(formProduit.getStockInitial());

		Pack pack = new Pack();
		pack.setEtat(Etat.getActif());
		pack.setType(produit.getType());
		pack.setNom(formProduit.getNomUnite().trim());
		pack.setNombreUnite(1);
		pack.setPrixAchat(formProduit.getPrixAchat());
		pack.setPrixVente(formProduit.getPrixVente());
		pack.setPrixVenteMin(formProduit.getPrixVenteMin());
		pack.setStatut(Statut.getPackunitaire());

		produit.setPrixVente(formProduit.getPrixVente());
		produit.setPrixVenteMin(formProduit.getPrixVenteMin());
		produit.setPrixAchat(formProduit.getPrixAchat());
		produit.setSuivreStock((byte) (formProduit.isSuivreStock() ? 1 : 0));
		produit.setSku((formProduit.getSku().trim().length() > 2) ? formProduit.getSku().trim().toUpperCase() : null);
		produit.addPack(pack);

		/*
		 * ErrorAPI errorAPI = new ErrorAPI(); errorAPI.setMessage("Sa fonctione");
		 * throw new StreengeException(errorAPI);
		 */

		try {
			produit = produitRepository.save(produit);
			medicamentService.createMedicament(formProduit.getFormMedicament(), produit);
			groupeService.AddProduitToGroupe(formProduit.getFormGroupes(), produit);
		} catch (DataIntegrityViolationException e) {
			System.out.println("The product already exist");
		}

		Utilisateur utilisateur = formProduit.getUtilisateur();
		registreStockService.updateRegistreStock(produitStockagesBefore, "Initialisation du stock.",
				(utilisateur != null) ? utilisateur.getId() : 0, null);
		return produit;

	}

	@Transactional
	public StockagesAndCategoriesList getListCategieAndStockage() {
		StockagesAndCategoriesList stockAndAndCat = new StockagesAndCategoriesList();

		stockAndAndCat.setCategories((List<Categorie>) (categorieRepository.findAllByOrderByNomAsc()));
		stockAndAndCat.setStockages((List<Stockage>) stockageRepository.findAll());
		stockAndAndCat.setComposants(composantRepository.findAllByOrderByNomAsc());
		stockAndAndCat.setGroupes(groupeRepository.findAllByOrderByNomAsc());

		return stockAndAndCat;
	}

	private String createID(int number) {
		String id = "PR";
		if (number < 9) {
			id += "000" + (number + 1);
		} else if (number < 99) {
			id += "00" + (number + 1);
		} else if (number < 999) {
			id += "0" + (number + 1);
		} else if (number < 9999) {
			id += "" + (number + 1);
		} else if (number < 99999) {
			id += "" + (number + 1);
		}

		Optional<Produit> produitTest = this.produitRepository.findById(id);

		if (produitTest.isPresent()) {
			int nbr = number + 1;
			String id2 = "";
			do {
				nbr++;
				if (nbr < 10) {
					id2 = "PR000" + nbr;
				} else if (nbr < 100) {
					id2 = "PR00" + nbr;
				} else if (nbr < 1000) {
					id2 = "PR0" + nbr;
				} else if (nbr < 200000) {
					id2 = "PR" + nbr;
				} else {
					ErrorAPI errorAPI = new ErrorAPI();
					errorAPI.setMessage(
							"Problème Majeur...! l'application est Saturer, vous avez atteint la limite Autorisée...! Contactez L'informaticien...!");
					throw new StreengeException(errorAPI);
				}

				id = id2;
				produitTest = this.produitRepository.findById(id);
			} while (produitTest.isPresent());
		}

		return id;
	}

	private void ControlFromProduit(FormCreateProduit form) {
		if (form.getNom().trim().length() < 3)
			throw new StreengeException(new ErrorAPI(
					"Le Non du produit/service ne doit pas êre vide, ou contenir moins de trois(03) Caractères"));
		if (form.getNom().trim().length() > 50)
			throw new StreengeException(
					new ErrorAPI("Le Non du produit/service est trop long...! il ne doit pas depasser 50 craractères"));

		if (form.getSku() != null && form.getSku().trim().length() > 50)
			throw new StreengeException(new ErrorAPI(
					"Le SKU (reference) du produit est trop long...! Le SKU ne doit pas depasser 50 craractères"));

		/*
		 * if (!produitRepository.findByNom(form.getNom().trim()).isEmpty()) throw new
		 * StreengeException(new
		 * ErrorAPI("Vous Avez deja enregistrer un produit avec ce Nom"));
		 */
		if (!produitRepository.findBySku(form.getSku().trim()).isEmpty())
			throw new StreengeException(new ErrorAPI("Le SKU entrez est déjà associer au produit: "
					+ produitRepository.findBySku(form.getSku().trim()).get(0).getNom()));
		if (!produitRepository.findByCodeBarre(form.getCodeBarre().trim()).isEmpty())
			throw new StreengeException(new ErrorAPI("Le Code-Barre entrez est déjà associer au produit: "
					+ produitRepository.findByCodeBarre(form.getCodeBarre().trim()).get(0).getNom()));

		if (form.getCoutInitial() < 0)
			throw new StreengeException(new ErrorAPI("Le coût initial ne peut pas être NEGATIF...!"));
		if (form.getPointCommande() < 0)
			throw new StreengeException(new ErrorAPI("Le poin de commande ne peut pas être NEGATIF...!"));
		if (form.getPrixVente() < 0)
			throw new StreengeException(new ErrorAPI("Le prix d'achat ne peut pas être NEGATIF...!"));
		if (form.getPrixVente() < 0)
			throw new StreengeException(new ErrorAPI("Le prix de vente ne peut pas être NEGATIF...!"));
		if (form.getPrixVenteMin() < 0)
			throw new StreengeException(new ErrorAPI("Le prix de vente minimun ne peut pas être NEGATIF...!"));

		if (form.getPrixVente() < form.getPrixVenteMin())
			throw new StreengeException(
					new ErrorAPI("Le prix de vente doit etre superieur au prix de vente minimum..!"));

	}

	public FormViewProduit generateFormProduit(Produit produit) {
		FormViewProduit form = new FormViewProduit();
		List<FormStock> formStocks = new ArrayList<FormStock>();
		List<FormPack> formPacks = new ArrayList<FormPack>();
		List<Pack> packs;

		form.setId(produit.getIdProduit());
		form.setNom(produit.getNom());
		form.setSku(produit.getSku());
		form.setType(produit.getType());
		form.setCodeBarre(produit.getCodeBarre());
		form.setDescription(produit.getDescription());
		form.setCategorie((produit.getCategorie() != null) ? produit.getCategorie().getNom() : null);
		form.setCoutUMP(produit.getCout_UMP());
		form.setCoutInitial(produit.getCoutInitial());
		form.setStockInitial(produit.getStockInitial());
		form.setPointCommande(produit.getPointCommande());
		form.setNomUnite(produit.getNomUnite());
		form.setImgages(produit.getImages());
		packs = produit.getPacks();
		form.setPrixAchat(packs.get(0).getPrixAchat());
		form.setPrixVente(packs.get(0).getPrixVente());
		form.setPrixVenteMin(packs.get(0).getPrixVenteMin());
		List<String> listCategories = new ArrayList<>();
		for (Categorie categorie : categorieRepository.findAllByOrderByNomAsc()) {
			listCategories.add(categorie.getNom());
		}
		form.setListCategories(listCategories);
		packs.remove(0);

		// On retire le pack unitaitre et on envoi les autre pack, S'il n'ya pas d'autre
		// pack on revoie NULL (on ne renvoi rien)
		if (!packs.isEmpty()) {
			for (Pack pack : packs) {
				if (!pack.getEtat().equals(Etat.getDelete())) {
					FormPack formPack = new FormPack();
					formPack.setIdPack(pack.getId());
					formPack.setIdProduit(pack.getProduit().getIdProduit());
					formPack.setStatut(pack.getStatut());
					formPack.setPrincipal(
							(pack.getStatut() != null) ? pack.getStatut().equals(Statut.getPackprincipal()) : false);
					formPack.setPrixAchat(pack.getPrixAchat());
					formPack.setPrixVente(pack.getPrixVente());
					formPack.setPrixVenteMin(pack.getPrixVenteMin());
					formPack.setNombreUnite(pack.getNombreUnite());
					formPack.setNom(pack.getNom());
					formPacks.add(formPack);
					if (Statut.getPackprincipal().equals(pack.getStatut())) {
						form.setNomPackPrincipale(pack.getNom());
						form.setNombreUnitePackPrincipale(pack.getNombreUnite());
					}
				}
			}
			form.setPacks(formPacks);
		}

		List<ProduitStockage> produitStockages = produit.getProduitStockages();
		for (ProduitStockage produitStockage : produitStockages) {
			FormStock formStock = new FormStock();
			formStock.setIdProduitStockage(produitStockage.getId());
			formStock.setNom(produitStockage.getStockage().getNom());
			formStock.setEmplacement(produitStockage.getEmplacement());
			formStock.setPointCommande(produitStockage.getPointCommande());
			formStock.setAvenir(produitStockage.getStockAvenir());
			formStock.setDisponible(produitStockage.getStockDisponible());
			formStock.setReel(produitStockage.getStockReel());
			formStock.setReserve(produitStockage.getStockReel() - produitStockage.getStockDisponible());

			formStocks.add(formStock);
			formStock.setIdStockage(produitStockage.getStockage().getIdStockage());

			form.setReel(form.getReel() + produitStockage.getStockReel());
			form.setDisponible(form.getDisponible() + produitStockage.getStockDisponible());
			form.setAvenir(form.getAvenir() + produitStockage.getStockAvenir());
		}

		form.setStocks(formStocks);
		form.setFormMedicament(medicamentService.getFormMedicament((produit.getMedicament()!=null)?produit.getMedicament().getId():0));
		form.setFormGroupes(groupeService.getFormGroupesForProduit(produit.getIdProduit()));

		return form;
	}
	
	public FormViewProduit generateSimpleFormViewProduitForMobile(String idProduit) {
		FormViewProduit formViewProduit = new FormViewProduit();
		if (produitRepository.existsById(idProduit)) {
			Produit produit= produitRepository.findById(idProduit).get();
			
			formViewProduit.setId(produit.getIdProduit());
			formViewProduit.setPrixVente(produit.getPrixVente());
			formViewProduit.setNom(produit.getNom());
			formViewProduit.setNomUnite(produit.getNomUnite());
			formViewProduit.setImgages(produit.getImages());
			formViewProduit.setFormMedicament(medicamentService.getFormMedicament((produit.getMedicament()!=null)?produit.getMedicament().getId():0));
			formViewProduit.setFormGroupes(groupeService.getFormGroupesForProduit(produit.getIdProduit()));
		}
		
		return  formViewProduit;
	}

	public FormListProduits generateListProduits(String filter) {
		return generateListProduits(filter, true);
	}

	public FormListProduits generateListProduits(String filter, boolean isProduit) {
		List<RowTable> rows = new ArrayList<RowTable>();
		List<Produit> listProduits;

		if (filter != null) {
			filter = filter.trim();
			listProduits = produitRepository.findByNomStartingWithOrSkuStartingWithOrderByNomAsc(filter, filter);
			if (listProduits.size() < 1) {
				listProduits = produitRepository.findByCategorieNomStartingWithOrderByNomAsc(filter);
			}

			if (listProduits.size() < 1 && filter.length() >= 3) {
				listProduits = produitRepository.findByNomContainingOrSkuContainingOrCodeBarreContaining(filter, filter,
						filter);
			}

		} else {
			listProduits = produitRepository.findAllByOrderByNomAsc();
		}

		for (Produit produit : listProduits) {
			if (!produit.getEtat().equals(Etat.getDelete())
					&& Data.typeService.equals(produit.getType()) != isProduit) {

				List<String> dataList = new ArrayList<String>();
				String nomUnite = produit.getNomUnite();
				float nombreProduit = (float) 0;
				float prix = produit.getPrixVente();
				RowTable row = new RowTable();
				dataList.add(produit.getNom());
				dataList.add(produit.getSku());
				List<ProduitStockage> produitStockages = produit.getProduitStockages();
				for (ProduitStockage produitStock : produitStockages) {
					nombreProduit += produitStock.getStockReel();
				}
				List<Pack> packs = produit.getPacks();
				for (Pack pack : packs) {
					if ((Statut.getPackprincipal().equals(pack.getStatut())) && (pack.getNombreUnite() != 0)) {
						nombreProduit = (nombreProduit / pack.getNombreUnite());
						nomUnite = pack.getNom();

						if (pack.getPrixVente() == 0) {
							prix = produit.getPrixVente() * pack.getNombreUnite();
						} else {
							prix = pack.getPrixVente();
						}

						break;
					}
				}
				DecimalFormat df = new DecimalFormat("###,###,###,###.##");
				if (isProduit) {
					dataList.add(df.format(nombreProduit) + " " + nomUnite
							+ ((nombreProduit > 1 && !nomUnite.equals("")) ? "s" : ""));

					dataList.add(df.format(prix) + " " + Data.devise);
					dataList.add((produit.getCategorie() != null) ? produit.getCategorie().getNom() : null);
				} else {
					dataList.add(df.format(prix) + " " + Data.devise);
					dataList.add(nomUnite);
				}

				row.setData(dataList);
				row.setId(produit.getIdProduit());
				rows.add(row);
			}
		}

		List<String> colunm = new ArrayList<String>();
		colunm.add(isProduit ? "NOM DU PRODUIT" : "NOM DU SERVICE");
		colunm.add("SKU");
		if (isProduit) {
			colunm.add("GESTIION DES STOCK");
			colunm.add("PRIX ven.");
			colunm.add("CATEGORIE");
		} else {
			colunm.add("PRIX");
			colunm.add("UNITÉ");
		}

		FormListProduits formListProduits = new FormListProduits();

		formListProduits.setColunm(colunm);
		formListProduits.setRows(rows);

		return formListProduits;
	}

	public List<FormUpdatePrice> getListPrixProduit(String filter) {

		return getListPrixProduit(filter, null);
	}

	public List<FormUpdatePrice> getListPrixProduit(String filter, FormFilter formFilter) {

		if (formFilter == null) {
			formFilter = new FormFilter();
			formFilter.setIdCategorie(null);
		}

		List<FormUpdatePrice> listUpdatePrice = new ArrayList<>();
		List<Produit> listProduits;
		if (filter != null) {
			filter = filter.trim();
			listProduits = produitRepository.findByNomStartingWithOrSkuStartingWithOrderByNomAsc(filter, filter);
			if (listProduits.size() < 1) {
				listProduits = produitRepository.findByCategorieNomStartingWithOrderByNomAsc(filter);
			}

			if (listProduits.size() < 1 && filter.length() >= 3) {
				listProduits = produitRepository.findByNomContainingOrSkuContainingOrCodeBarreContaining(filter, filter,
						filter);
			}
		} else {
			listProduits = produitRepository.findAllByOrderByNomAsc();
		}
		for (Produit produit : listProduits) {
			if (!produit.getEtat().equals(Etat.getDelete()) && produit.getType() == null) {
				if (formFilter.getIdCategorie() == null
						|| (produit.getCategorie() != null
								&& formFilter.getIdCategorie().equals(produit.getCategorie().getIdCategorie()))
						|| (produit.getCategorie() == null && formFilter.getIdCategorie().equals("---"))) {
					FormUpdatePrice form = new FormUpdatePrice();
					form.setId(produit.getIdProduit());
					form.setNom(produit.getNom());
					form.setSku(produit.getSku());
					form.setPrixAchat(produit.getPrixAchat());
					form.setPrixVente(produit.getPrixVente());
					form.setUnite(produit.getNomUnite());
					listUpdatePrice.add(form);
				}
			}
		}
		return listUpdatePrice;
	}

	public void setNewPrice(List<FormUpdatePrice> list) {
		for (FormUpdatePrice form : list) {
			Optional<Produit> optional = produitRepository.findById(form.getId());
			if (optional.isPresent()) {
				optional.get().setPrixAchat(form.getPrixAchat());
				optional.get().getPacks().get(0).setPrixAchat(form.getPrixAchat());
				optional.get().setPrixVente(form.getPrixVente());
				optional.get().getPacks().get(0).setPrixVente(form.getPrixVente());
				produitRepository.save(optional.get());
			}
		}
	}

	public List<FormPack> getFormPacks(String filter, int idStock) {
		List<FormPack> listFormPacks = new ArrayList<FormPack>();
		// List<FormProduit> listFormProduits = new ArrayList<FormProduit>();
		List<Produit> listProduits;
		if (filter != null) {
			filter = filter.trim();
			listProduits = produitRepository
					.findByNomStartingWithOrSkuStartingWithOrCodeBarreStartingWithOrderByNomAsc(filter, filter, filter);
			if (listProduits.size() < 1 && filter.length() >= 2) {
				listProduits = produitRepository.findByNomContainingOrSkuContainingOrCodeBarreContaining(filter, filter,
						filter);
			}

			if (listProduits.size() < 1 && filter.length() >= 2) {
				listProduits = produitRepository.findByCategorieNomStartingWithOrderByNomAsc(filter);
			}
		} else {
			listProduits = produitRepository.findAllByOrderByNomAsc();
		}

		for (Produit produit : listProduits) {
			if (!produit.getEtat().equals(Etat.getDelete())) {
				List<Pack> packs = produit.getPacks();
				for (Pack pack : packs) {
					if (!pack.getEtat().equals(Etat.getDelete()) && pack.getNombreUnite() > 0) {
						FormPack formPack = new FormPack();
						formPack.setIdPack(pack.getId());
						formPack.setNombreUnite(pack.getNombreUnite());
						formPack.setSku(produit.getSku());
						formPack.setIdProduit(produit.getIdProduit());
						formPack.setType(produit.getType());
						formPack.setDesignation(" " + Methode.upperCaseFirst(produit.getNom().trim())
								+ (pack.getNom().equals("") ? "" : " - " + pack.getNom().toLowerCase()));

						if (pack.getPrixVente() > 0) {
							formPack.setPrixVente(pack.getPrixVente());
						} else {
							formPack.setPrixVente(produit.getPrixVente() * pack.getNombreUnite());
						}

						formPack.setPrixVenteMin(pack.getPrixVenteMin());
						formPack.setCoutUMP(produit.getCout_UMP() * pack.getNombreUnite());

						if (pack.getPrixAchat() > 0) {
							formPack.setPrixAchat(pack.getPrixAchat());
						} else {
							formPack.setPrixAchat(produit.getPrixAchat() * pack.getNombreUnite());
						}

						for (ProduitStockage produitStock : produit.getProduitStockages()) {
							if (produitStock.getStockage().getIdStockage() == idStock) {
								formPack.setDisponible(produitStock.getStockDisponible() / pack.getNombreUnite());
								formPack.setReel(produitStock.getStockReel() / pack.getNombreUnite());
								formPack.setAvenir(produitStock.getStockAvenir() / pack.getNombreUnite());
							}
						}

						listFormPacks.add(formPack);
					}
				}
			}
		}

		return listFormPacks;
	}

	public FormPack craeteOrUpdatePack(FormPack formPack, Boolean isNew) {
		// packRepository
		controlPack(formPack);
		Pack pack = null;
		if (isNew) {
			pack = new Pack();
			pack.setProduit(produitRepository.findById(formPack.getIdProduit()).get());
			pack.setType(pack.getProduit().getType());
		} else {
			pack = packRepository.findById(formPack.getIdPack()).get();
		}

		pack.setEtat(Etat.getActif());
		pack.setNom(formPack.getNom());
		pack.setNombreUnite(formPack.getNombreUnite());
		pack.setPrixAchat(formPack.getPrixAchat());
		pack.setPrixVente(formPack.getPrixVente());
		pack.setPrixVenteMin(formPack.getPrixVenteMin());
		if (formPack.isPrincipal()) {
			List<Pack> packs = produitRepository.findById(formPack.getIdProduit()).get().getPacks();
			for (int i = 0; i < packs.size(); i++) {
				Pack pack2 = packs.get(i);
				if (Statut.getPackprincipal().equals(pack2.getStatut())) {
					pack2.setStatut(null);
					packRepository.save(pack2);
				}
			}
			pack.setStatut(Statut.getPackprincipal());
		} else {
			pack.setStatut(null);
		}
		pack = packRepository.save(pack);
		formPack.setIdPack(pack.getId());

		return formPack;
	}

	public void deletePack(FormPack formPack) {
		Pack pack = packRepository.findById(formPack.getIdPack()).get();
		pack.setEtat(Etat.getDelete());
		pack = packRepository.save(pack);
	}

	public void controlPack(FormPack form) {
		if (form.getNom().trim().length() < 3)
			throw new StreengeException(
					new ErrorAPI("Le Nom du Pack ne doit pas êre vide, ou contenir moins de trois(03) Caractères"));
		if (form.getNombreUnite() == 0)
			throw new StreengeException(new ErrorAPI("Le Nombre d'unité ne peut pas etre egale a zero"));

		if (form.getPrixVente() < form.getPrixVenteMin())
			throw new StreengeException(
					new ErrorAPI("Le prix de vente doit etre superieur au prix de vente minimum..!"));

	}

	public List<FormProduit> getSimplifiedFormProduits(int idStock) {
		List<FormProduit> formProduits = new ArrayList<FormProduit>();
		List<Produit> listProduits = produitRepository.findAllByOrderByNomAsc();
		for (Produit produit : listProduits) {
			FormProduit formProduit = new FormProduit();
			formProduit.setIdProduit(produit.getIdProduit());
			formProduit.setNom(produit.getNom());
			formProduit.setIsDelete(produit.getEtat().equals(Etat.getDelete()));
			formProduit.setSku(produit.getSku());
			formProduit.setType(produit.getType());
			for (ProduitStockage produitStock : produit.getProduitStockages()) {
				if (produitStock.getStockage().getIdStockage() == idStock) {
					formProduit.setDisponible(produitStock.getStockDisponible());
					formProduit.setReel(produitStock.getStockReel());
					formProduit.setAvenir(produitStock.getStockAvenir());
					formProduit.setPointCommande(produitStock.getPointCommande());
				}
			}
			formProduits.add(formProduit);
		}
		return formProduits;
	}

	public Produit updateProduit(FormViewProduit form) {
		ControlUpdateProduit(form);
		Produit produit = produitRepository.findById(form.getId()).get();
		produit.setNom(form.getNom().trim());
		produit.setPrixVente(form.getPrixVente());
		produit.setPrixAchat(form.getPrixAchat());
		produit.setNomUnite(form.getNomUnite());
		produit.setPrixVenteMin(form.getPrixVenteMin());

		if (produit.getCoutInitial() != form.getCoutInitial()) {
			produit.setCoutInitial(form.getCoutInitial());
			produit.setCout_UMP(form.getCoutInitial());
			// updateCoutUMP();
		}

		Pack pack = produit.getPacks().get(0);
		pack.setNom(produit.getNomUnite());
		pack.setPrixVente(produit.getPrixVente());
		pack.setPrixVenteMin(produit.getPrixVenteMin());
		pack.setPrixAchat(produit.getPrixAchat());
		pack = packRepository.save(pack);

		produit.setSku((form.getSku() != null && form.getSku().trim().length() > 2) ? form.getSku().trim().toUpperCase()
				: null);
		produit.setCodeBarre(
				(form.getCodeBarre() != null && form.getCodeBarre().trim().length() > 5) ? form.getCodeBarre().trim()
						: null);
		produit.setDescription(form.getDescription());

		if (form.getCategorie() != null) {
			for (Categorie categorie : categorieRepository.findAll()) {
				if (categorie.getNom().trim().equals(form.getCategorie().trim())) {
					produit.setCategorie(categorie);
				}
			}
		}

		List<FormStock> formStocks = form.getStocks();
		for (FormStock formStock : formStocks) {
			ProduitStockage produitStockage = produitStockageRepository.findById(formStock.getIdProduitStockage())
					.get();
			produitStockage.setEmplacement(formStock.getEmplacement());
			produitStockage.setPointCommande(formStock.getPointCommande());
			produitStockage = produitStockageRepository.save(produitStockage);
		}

		try {
			produit = produitRepository.save(produit);
			medicamentService.createMedicament(form.getFormMedicament(), produit);
			groupeService.AddProduitToGroupe(form.getFormGroupes(), produit);
		} catch (DataIntegrityViolationException e) {
			System.out.println("impossble de mettre a jour le produit modifier");
		}
		
		return produit;
	}

	private void ControlUpdateProduit(FormViewProduit form) {
		if (form.getNom().trim().length() < 3)
			throw new StreengeException(
					new ErrorAPI("Le Nom du produit ne doit pas êre vide, ou contenir moins de trois(03) Caractères"));
		if (form.getNom().trim().length() > 50)
			throw new StreengeException(
					new ErrorAPI("Le Non du produit est trop long...! il ne doit pas depasser 50 craractères"));

		if (form.getSku() != null && form.getSku().trim().length() > 50)
			throw new StreengeException(new ErrorAPI(
					"Le SKU (reference) du produit est trop long...! Le SKU ne doit pas depasser 50 craractères"));
		/*
		 * if (!produitRepository.findByNom(form.getNom().trim()).isEmpty()) { if
		 * (!produitRepository.findByNom(form.getNom().trim()).get(0).getIdProduit().
		 * toLowerCase().trim() .equals(form.getId().toLowerCase().trim())) throw new
		 * StreengeException(new
		 * ErrorAPI("Vous Avez deja enregistrer un produit avec ce Nom")); }
		 */
		if (form.getSku() != null && !produitRepository.findBySku(form.getSku().trim()).isEmpty()) {
			if (!produitRepository.findBySku(form.getSku().trim()).get(0).getIdProduit().equals(form.getId()))
				throw new StreengeException(new ErrorAPI("Le SKU entrez est déjà associer au produit: "
						+ produitRepository.findBySku(form.getSku().trim()).get(0).getNom()));
		}
		if (form.getCodeBarre() != null && !produitRepository.findByCodeBarre(form.getCodeBarre().trim()).isEmpty()) {
			if (!produitRepository.findByCodeBarre(form.getCodeBarre().trim()).get(0).getIdProduit()
					.equals(form.getId()))
				throw new StreengeException(new ErrorAPI("Le Code-Barre entrez est déjà associer au produit: "
						+ produitRepository.findByCodeBarre(form.getCodeBarre().trim()).get(0).getNom()));
		}

		if (form.getPrixVente() < form.getPrixVenteMin())
			throw new StreengeException(
					new ErrorAPI("Le prix de vente doit etre superieur au prix de vente minimum..!"));
	}

	public void updateProduitQuantite(Pack pack, float difference, Stockage stockage, Boolean inpactDisponible,
			Boolean inpactReel) {
		this.controlIfExistProduitStockegeAndCreateIt(pack.getProduit(), stockage);
		Produit produit = pack.getProduit();
		float totalDif = difference * pack.getNombreUnite();
		for (int i = 0; i < produit.getProduitStockages().size(); i++) {
			ProduitStockage produitStockage = produit.getProduitStockages().get(i);
			if (produitStockage.getStockage().getIdStockage() == stockage.getIdStockage()) {
				if (inpactDisponible) {
					produitStockage.setStockDisponible(totalDif + produitStockage.getStockDisponible());
				}
				if (inpactReel) {
					produitStockage.setStockReel(totalDif + produitStockage.getStockReel());
				}
				produitStockage = produitStockageRepository.save(produitStockage);
			}
		}
	}

	public void updateProduitQuantiteAvenir(Pack pack, float difference, Stockage stockage) {
		this.controlIfExistProduitStockegeAndCreateIt(pack.getProduit(), stockage);
		Produit produit = pack.getProduit();
		float totalDif = difference * pack.getNombreUnite();

		for (int i = 0; i < produit.getProduitStockages().size(); i++) {
			ProduitStockage produitStockage = produit.getProduitStockages().get(i);
			if (produitStockage.getStockage().getIdStockage() == stockage.getIdStockage()) {
				produitStockage.setStockAvenir(totalDif + produitStockage.getStockAvenir());
				produitStockage = produitStockageRepository.save(produitStockage);
			}
		}
	}

	private void controlIfExistProduitStockegeAndCreateIt(Produit produit, Stockage stockage) {
		boolean isExist = false;
		for (ProduitStockage produitStockage : produit.getProduitStockages()) {
			if (produitStockage.getStockage().getIdStockage() == stockage.getIdStockage()) {
				isExist = true;
			}
		}

		if (!isExist) {
			ProduitStockage produitStockage = new ProduitStockage();
			produitStockage.setStockage(stockage);
			if (Data.typeService.equals(produit.getType())) {
				produitStockage.setStockReel(20000000);
				produitStockage.setStockDisponible(20000000);
			}
			produit.addProduitStockage(produitStockage);
			produitRepository.save(produit);
		}

	}

	public void deleteProduit(String idProduit) {
		if (produitRepository.findById(idProduit).isPresent()) {
			Produit produit = produitRepository.findById(idProduit).get();
			produit.setEtat(Etat.getDelete());
			produit.setCodeBarre((produit.getCodeBarre() == null) ? null
					: produit.getCodeBarre() + " XXX-XX" + produit.getIdProduit());
			produit.setSku((produit.getSku() == null) ? null : produit.getSku() + " XXX-XX-" + produit.getIdProduit());

			produit=produitRepository.save(produit);
			if (produit.getGroupeProduits()!=null) {
				List<GroupeProduit> groupeProduits=produit.getGroupeProduits();
				for (GroupeProduit groupeProduit : groupeProduits) {
					groupeProduitRepository.deleteGroupeProduit(groupeProduit.getId());
				}
			}
			
			
		}
	}

	

	public void updateCoutUMP(List<LigneDocument> ligneDocuments, Boolean isAdding) {

		List<Produit> produits = (List<Produit>) produitRepository.findAll();

		for (Produit produit : produits) {
			float qtActuel = 0;
			if (!Etat.getDelete().equals(produit.getEtat()) && produit.getType() == null) {

				for (ProduitStockage produitStockage : produit.getProduitStockages()) {
					qtActuel += produitStockage.getStockReel();
				}

				for (LigneDocument ligne : ligneDocuments) {
					if (ligne.getPack().getProduit().getIdProduit().equals(produit.getIdProduit())) {
						if (isAdding) {
							qtActuel = qtActuel - (ligne.getQuantite() * ligne.getPack().getNombreUnite());
						} else {
							qtActuel = qtActuel + (ligne.getQuantite() * ligne.getPack().getNombreUnite());
						}

					}
				}

				// calcul du cout CUMP
				for (LigneDocument ligne : ligneDocuments) {
					if (ligne.getPack().getProduit().getIdProduit().equals(produit.getIdProduit())) {
						float montantTotalLigne = Methode.getTotalLigneDocument(ligne);

						if (isAdding) {
							if (qtActuel <= 0) {
								qtActuel += (ligne.getQuantite() * ligne.getPack().getNombreUnite());
								float cump = montantTotalLigne
										/ (ligne.getQuantite() * ligne.getPack().getNombreUnite());
								produit.setCout_UMP(cump);
							} else if (ligne.getQuantite() > 0) {
								float cump = (qtActuel * produit.getCout_UMP() + montantTotalLigne)
										/ (qtActuel + (ligne.getQuantite() * ligne.getPack().getNombreUnite()));
								produit.setCout_UMP(cump);
							}
							qtActuel = qtActuel + (ligne.getQuantite() * ligne.getPack().getNombreUnite());

						} else {
							float qtPrimaire = qtActuel - (ligne.getQuantite() * ligne.getPack().getNombreUnite());
							if (qtPrimaire <= 0) {

							} else if (ligne.getQuantite() > 0) {
								float cumpInit = (produit.getCout_UMP() * qtActuel - montantTotalLigne) / (qtPrimaire);
								produit.setCout_UMP(cumpInit);
							}
							qtActuel = qtActuel - (ligne.getQuantite() * ligne.getPack().getNombreUnite());
						}
					}

					produitRepository.save(produit);
				}
			}

		}
	}
}
