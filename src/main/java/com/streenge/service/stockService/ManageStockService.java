package com.streenge.service.stockService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.streenge.model.produit.ProduitStockage;
import com.streenge.repository.produitRepo.ProduitStockageRepository;
import com.streenge.service.utils.formOut.FormStock;
import com.streenge.service.utils.formOut.FormViewProduit;
import com.streenge.service.utils.streengeFunction.Methode;

@Service
public class ManageStockService {

	@Autowired
	private ProduitStockageRepository produitStockageRepository;
	@Autowired
	private RegistreStockService registreStockService;
	
	
	public void updateStock(List<FormViewProduit> formProduits,int idUser) {
	
		List<ProduitStockage> produitStockagesBefore = Methode
				.getProduitStockagesEager((List<ProduitStockage>) produitStockageRepository.findAll());
		for (FormViewProduit formProduit : formProduits) {
			for (int i = 0; i < formProduit.getStocks().size(); i++) {
				FormStock formStock=formProduit.getStocks().get(i);
				if (produitStockageRepository.findById(formStock.getIdProduitStockage()).isPresent()) {
					ProduitStockage produitStockage=produitStockageRepository.findById(formStock.getIdProduitStockage()).get();
					float ajustement=formStock.getAjustement();
					produitStockage.setStockDisponible(produitStockage.getStockDisponible()+ajustement);
					produitStockage.setStockReel(produitStockage.getStockReel()+ajustement);
					produitStockage.setPointCommande(formStock.getPointCommande());
					produitStockage.setEmplacement(formStock.getEmplacement());
					
					produitStockageRepository.save(produitStockage);
				}
			}
		}
		
		registreStockService.updateRegistreStock(produitStockagesBefore, "Mise à jour Manuel du niveau de stock", idUser,null);
	}
	
}
