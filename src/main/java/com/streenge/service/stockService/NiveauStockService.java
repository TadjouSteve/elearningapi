package com.streenge.service.stockService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.streenge.model.LigneDocument;
import com.streenge.model.produit.Produit;
import com.streenge.model.vente.BonCommande;
import com.streenge.model.vente.BonLivraison;
import com.streenge.repository.produitRepo.ProduitRepository;
import com.streenge.repository.venteRepo.BonCommandeRepository;
import com.streenge.service.produitService.ProduitService;
import com.streenge.service.utils.formInt.FormFilter;
import com.streenge.service.utils.formOut.FormViewProduit;
import com.streenge.service.utils.streengeData.Etat;
import com.streenge.service.utils.streengeData.Statut;

@Service
public class NiveauStockService {

	@Autowired
	private ProduitRepository produitRepository;

	@Autowired
	private BonCommandeRepository bonCommandeRepository;

	// @Autowired
	// private ProduitStockageRepository produitStockageRepository;

	@Autowired
	private ProduitService produitService;

	public List<FormViewProduit> getAllFormProduit(String filter) {
		return getAllFormProduit(filter, null);
	}

	public List<FormViewProduit> getAllFormProduit(String filter, FormFilter formFilter) {

		if (formFilter == null) {
			formFilter = new FormFilter();
			formFilter.setIdCategorie(null);
		}

		List<FormViewProduit> formViewProduits = new ArrayList<FormViewProduit>();
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
			if (!Etat.getDelete().equals(produit.getEtat()) && produit.getType() == null) {
				if (formFilter.getIdCategorie() == null
						|| (produit.getCategorie() != null
								&& formFilter.getIdCategorie().equals(produit.getCategorie().getIdCategorie()))
						|| (produit.getCategorie() == null && formFilter.getIdCategorie().equals("---"))) {
					FormViewProduit formViewProduit = new FormViewProduit();
					formViewProduit = produitService.getProduit(produit.getIdProduit());
					if (formViewProduit.getDisponible() != formViewProduit.getReel()) {
						formViewProduit.setListBonCommande(getListBonCommande(produit.getIdProduit()));
					}
					formViewProduits.add(formViewProduit);
				}
			}
		}
		return formViewProduits;
	}

	public HashMap<String, Float> getListBonCommande(String idProduit) {
		HashMap<String, Float> listBonCommande = new HashMap<>();
		List<BonCommande> bonCommandes = bonCommandeRepository.findAllByOrderByIdBonCommandeDesc();

		for (BonCommande bonCommande : bonCommandes) {
			if (!Statut.getAnnuler().equals(bonCommande.getStatut())
					&& !Etat.getDelete().equals(bonCommande.getEtat())) {
				float quantite = 0;
				for (LigneDocument ligneDocument : bonCommande.getLigneDocuments()) {
					if (ligneDocument.getPack().getProduit().getIdProduit().equals(idProduit)) {
						quantite += (ligneDocument.getQuantite() * ligneDocument.getPack().getNombreUnite());
					}
				}

				if (quantite > 0) {
					for (BonLivraison bonLivraison : bonCommande.getBonLivraisons()) {
						if (Statut.getLivre().equals(bonLivraison.getStatut())
								&& !Etat.getDelete().equals(bonLivraison.getEtat())) {
							for (LigneDocument ligneDocument03 : bonLivraison.getLigneDocuments()) {
								if (ligneDocument03.getPack().getProduit().getIdProduit().equals(idProduit)) {
									quantite -= (ligneDocument03.getQuantite()
											* ligneDocument03.getPack().getNombreUnite());
								}
							}
						}
					}
				}

				if (quantite > 0) {
					listBonCommande.put(bonCommande.getIdBonCommande(), quantite);
				}

			}
		}

		return listBonCommande;
	}

}
