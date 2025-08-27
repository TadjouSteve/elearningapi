package com.streenge.controller.produitController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.streenge.model.produit.Produit;
import com.streenge.service.produitService.ProduitService;
import com.streenge.service.utils.URLStreenge;
import com.streenge.service.utils.formInt.FormCreateProduit;
import com.streenge.service.utils.formInt.FormFilter;
import com.streenge.service.utils.formOut.FormListProduits;
import com.streenge.service.utils.formOut.FormPack;
import com.streenge.service.utils.formOut.FormProduit;
import com.streenge.service.utils.formOut.FormUpdatePrice;
import com.streenge.service.utils.formOut.FormViewProduit;
import com.streenge.service.utils.formOut.StockagesAndCategoriesList;

@RestController
@CrossOrigin
public class ProduitController {
	@Autowired
	private ProduitService produitService;

	@GetMapping("/produits")
	public FormListProduits getAllProduit() {
		return produitService.getAllProduit(null);
	}

	@GetMapping("/produits/{filter}")
	public FormListProduits getProduitFilter(@PathVariable("filter") String filter) {
		return produitService.getAllProduit(filter);
	}

	@GetMapping("/services")
	public FormListProduits getAllService() {
		return produitService.generateListProduits(null, false);
	}

	@GetMapping("/services/{filter}")
	public FormListProduits getServiceFilter(@PathVariable("filter") String filter) {
		return produitService.generateListProduits(filter, false);
	}

	@GetMapping("/produit/{id}")
	public FormViewProduit getProduit(@PathVariable("id") String id) {
		return produitService.getProduit(id);
	}
	

	@GetMapping("/produit/liststockageandcategorie")
	public StockagesAndCategoriesList getStockages() {
		return produitService.getListCategieAndStockage();
	}

	@PostMapping("/produit")
	public URLStreenge createProduit(@RequestBody FormCreateProduit formCreateProduit) {
		Produit produit = produitService.CreateProduit(formCreateProduit);
		URLStreenge urlStreenge = new URLStreenge();
		urlStreenge.setUrl("/produit/" + produit.getIdProduit());
		urlStreenge.setData(produit);
		return urlStreenge;
	}

	@PutMapping("/produit")
	public URLStreenge alterProduit(@RequestBody FormViewProduit formViewProduit) {
		Produit produit = produitService.updateProduit(formViewProduit);
		URLStreenge urlStreenge = new URLStreenge();
		urlStreenge.setUrl("/produit/" + produit.getIdProduit());
		urlStreenge.setData(produit);
		return urlStreenge;
	}

	@PostMapping("/service")
	public URLStreenge createService(@RequestBody FormCreateProduit formCreateProduit) {
		Produit service = produitService.CreateProduit(formCreateProduit);
		URLStreenge urlStreenge = new URLStreenge();
		urlStreenge.setUrl("/service/" + service.getIdProduit());
		urlStreenge.setData(service);
		return urlStreenge;
	}

	@PutMapping("/service")
	public URLStreenge alterService(@RequestBody FormViewProduit formViewProduit) {
		Produit service = produitService.updateProduit(formViewProduit);
		URLStreenge urlStreenge = new URLStreenge();
		urlStreenge.setUrl("/service/" + service.getIdProduit());
		urlStreenge.setData(service);
		return urlStreenge;
	}

	//delete
	@GetMapping("/produits/listupdateprice")
	public List<FormUpdatePrice> getListPrixProduit() {
		return produitService.getListPrixProduit(null);
	}

	//delete
	@GetMapping("/produits/listupdateprice/{filter}")
	public List<FormUpdatePrice> getListPrixProduit(@PathVariable("filter") String filter) {
		return produitService.getListPrixProduit(filter);
	}
	

	@PostMapping("/produits/listupdateprice")
	public List<FormUpdatePrice> getListPrixProduit(@RequestBody FormFilter formFilter) {
		return produitService.getListPrixProduit(null,formFilter);
	}
	
	@PostMapping("/produits/listupdateprice/{filter}")
	public List<FormUpdatePrice> getListPrixProduit(@PathVariable("filter") String filter,@RequestBody FormFilter formFilter) {
		return produitService.getListPrixProduit(filter,formFilter);
	}

	@PostMapping("/produits/listupdateprice/update")
	public URLStreenge setNewPrice(@RequestBody List<FormUpdatePrice> form) {
		produitService.setNewPrice(form);
		URLStreenge urlStreenge = new URLStreenge();
		urlStreenge.setUrl("/produits/");
		return urlStreenge;
	}

	@GetMapping("/produits/listpacks/{idStockage}/{filter}")
	public List<FormPack> getPacksFilter(@PathVariable("idStockage") int idStockage,
			@PathVariable("filter") String filter) {

		return produitService.getFormPacks(filter, idStockage);
	}
	
	@PostMapping("/produits/listpacks/{idStockage}")
	public List<FormPack> getPacksFilter(@PathVariable("idStockage") int idStockage, @RequestBody FormFilter formFilter) {
		return produitService.getFormPacks(formFilter.getFilter(), idStockage);
	}

	@GetMapping("/produits/listpacks/{idStockage}/")
	public List<FormPack> getPacksFilter(@PathVariable("idStockage") int idStockage) {
		return produitService.getFormPacks(null, idStockage);
	}

	@GetMapping("/formproduit/{idStockage}")
	public List<FormProduit> getSimpleform(@PathVariable("idStockage") int idStock) {
		return produitService.getSimplifiedFormProduits(idStock);
	}

	@CrossOrigin
	@PostMapping("/produit/pack")
	public URLStreenge createPack(@RequestBody FormPack formPack) {
		System.out.println("==Debut ajout du pack==");
		FormPack formPack2 = produitService.craeteOrUpdatePack(formPack, true);
		URLStreenge urlStreenge = new URLStreenge();
		urlStreenge.setUrl("/produits/");
		urlStreenge.setData(formPack2);
		return urlStreenge;
	}

	@PutMapping("/produit/pack")
	public URLStreenge AlterPack(@RequestBody FormPack formPack) {
		System.out.println("==Debut de modification du pack==");
		FormPack formPack2 = produitService.craeteOrUpdatePack(formPack, false);
		URLStreenge urlStreenge = new URLStreenge();
		urlStreenge.setUrl("/produits/");
		urlStreenge.setData(formPack2);
		return urlStreenge;
	}

	@DeleteMapping("/produit/pack")
	public URLStreenge deletePack(@RequestBody FormPack formPack) {
		System.out.println("==Debut supression du pack==");
		produitService.deletePack(formPack);
		URLStreenge urlStreenge = new URLStreenge();
		urlStreenge.setUrl("/produits/");
		urlStreenge.setData(true);
		return urlStreenge;
	}

	@DeleteMapping("/produit/delete/{idProduit}")
	public URLStreenge deleteProdui(@PathVariable("idProduit") String idProduit) {
		produitService.deleteProduit(idProduit);
		URLStreenge urlStreenge = new URLStreenge();
		urlStreenge.setUrl("/produits/");
		urlStreenge.setData(true);
		return urlStreenge;
	}
}
