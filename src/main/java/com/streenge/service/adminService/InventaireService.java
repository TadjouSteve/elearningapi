package com.streenge.service.adminService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.streenge.model.admin.Entreprise;
import com.streenge.model.admin.Stockage;
import com.streenge.model.produit.Pack;
import com.streenge.model.produit.Produit;
import com.streenge.model.produit.ProduitStockage;
import com.streenge.repository.adminRepository.EntrepriseRepository;
import com.streenge.repository.adminRepository.StockageRepository;
import com.streenge.repository.produitRepo.CategorieRepository;
import com.streenge.repository.produitRepo.ProduitRepository;
import com.streenge.service.utils.formInt.FormFilter;
import com.streenge.service.utils.formInt.FormPanier;
import com.streenge.service.utils.formInt.LinePanier;
import com.streenge.service.utils.streengeData.Data;
import com.streenge.service.utils.streengeData.Etat;
import com.streenge.service.utils.streengeData.Statut;

@Service
public class InventaireService {
	@Autowired
	private ProduitRepository produitRepository;

	@Autowired
	private StockageRepository stockageRepository;

	@Autowired
	private EntrepriseRepository entrepriseRepository;
	
	@Autowired
	private CategorieRepository categorieRepository;

	public FormPanier getInventaire(FormFilter formFilter) {
		FormPanier formPanier = new FormPanier();
		List<LinePanier> linePaniers = new ArrayList<>();
		if (formFilter == null) {
			formFilter = new FormFilter();
		}

		Stockage stockage = null;

		if (stockageRepository.findById(formFilter.getIdStockage()).isPresent()) {
			stockage = stockageRepository.findById(formFilter.getIdStockage()).get();
			formPanier.setStockage(stockage);
		}

		formPanier.setDateCreation(new Date());
		formPanier.setNomStockage((stockage != null) ? stockage.getNom() : "Tous les dêpots");
		if (!((List<Entreprise>) entrepriseRepository.findAll()).isEmpty()) {
			formPanier.setEntreprise(((List<Entreprise>) entrepriseRepository.findAll()).get(0));
		}
		for (Produit produit : produitRepository.findAllByOrderByNomAsc()) {
			if (!produit.getEtat().equals(Etat.getDelete()) && !Data.typeService.equals(produit.getType())) {
				LinePanier linePanier = new LinePanier();
				linePanier.setCoutUMP(produit.getCout_UMP());
				linePanier.setPrix(produit.getPrixVente());
				linePanier.setPrixMin(produit.getPrixVenteMin());
				linePanier.setPrixAchat(produit.getPrixAchat());
				linePanier.setQuantite(0);
				linePanier.setSku(produit.getSku());
				linePanier.setNomCategorie((produit.getCategorie()!=null)?produit.getCategorie().getNom():null);
				String nomUnite = produit.getNomUnite();

				for (ProduitStockage produitStockage : produit.getProduitStockages()) {
					if (stockage == null) {
						linePanier.setQuantite(linePanier.getQuantite() + produitStockage.getStockReel());
					} else if (stockage.getIdStockage() == produitStockage.getStockage().getIdStockage()) {
						linePanier.setQuantite(linePanier.getQuantite() + produitStockage.getStockReel());
						linePanier.setPointCommande(produitStockage.getPointCommande());
						linePanier.setEmplacement(produitStockage.getEmplacement());
						break;
					}
				}

				List<Pack> packs = produit.getPacks();
				for (Pack pack : packs) {
					if ((Statut.getPackprincipal().equals(pack.getStatut())) && (pack.getNombreUnite() != 0)) {
						// nombreProduit = (nombreProduit / pack.getNombreUnite());
						linePanier.setCoutUMP(linePanier.getCoutUMP() * pack.getNombreUnite());
						linePanier.setPrix((pack.getPrixVente() == 0) ? (linePanier.getPrix() * pack.getNombreUnite())
								: pack.getPrixVente());
						linePanier.setPointCommande(linePanier.getPointCommande()/pack.getNombreUnite());
						linePanier.setPrixMin(pack.getPrixVenteMin());
						linePanier.setQuantite(linePanier.getQuantite() / pack.getNombreUnite());
						nomUnite = pack.getNom();
						break;
					}
				}

				linePanier.setDesignation(produit.getNom() + ((nomUnite != null && !nomUnite.isBlank()) ? (" - " + nomUnite) : ""));
				linePaniers.add(linePanier);
			}
		}

		formPanier.setCategories(categorieRepository.findAllByOrderByNomAsc());
		/*for (int i = 0; i < 11; i++) {
			linePaniers.addAll(linePaniers);
		}*/
		//System.out.println("Total produit: "+linePaniers.size());
		formPanier.setLines(linePaniers);
		

		return formPanier;
	}
}
