package com.streenge.controller.adminControler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.streenge.model.admin.Entreprise;
import com.streenge.model.admin.Stockage;
import com.streenge.model.admin.Taxe;
import com.streenge.model.produit.Categorie;
import com.streenge.service.adminService.AnnexService;
import com.streenge.service.utils.URLStreenge;
import com.streenge.service.utils.formInt.FormFilter;
import com.streenge.service.utils.formOut.AllDocument;
import com.streenge.service.utils.formOut.FormDashboard;

@RestController
@CrossOrigin
public class AnnexController {
	@Autowired
	private AnnexService serviceAnex;
	
	@GetMapping("/annex/document/client/{idClient}")
	public AllDocument getAllDocumentClient(@PathVariable("idClient") String idClient) {
		return serviceAnex.getAllDocumentClient(idClient);
	}
	
	@GetMapping("/annex/document/fournisseur/{idFournisseur}")
	public AllDocument getAllDocumentFournisseur(@PathVariable("idFournisseur") String idFournisseur) {
		return serviceAnex.getAllDocumentfournisseur(idFournisseur);
	}
	
	@GetMapping("/annex/document/utilisateur/{idUser}")
	public FormDashboard getAllDocumentUtilisateur(@PathVariable("idUser") int idUser) {
		return serviceAnex.getDataDashboard(idUser,true);
	}
	
	@GetMapping("/dashboard/streenge")
	public FormDashboard getDataDashboard() {
		return serviceAnex.getDataDashboard(0);
	}
	
	@GetMapping("/dashboard/streenge/{idUser}")
	public FormDashboard getDataDashboard(@PathVariable("idUser") int idUser) {
		return serviceAnex.getDataDashboard(idUser);
	}
	
	
	@GetMapping("/entreprise")
	public Entreprise getEntreprise() {
		return serviceAnex.getEntreprise();
	}
	
	@PutMapping("/entreprise") 
	public URLStreenge updateEntrepise(@RequestBody Entreprise entreprise) {
		serviceAnex.updateEntreprise(entreprise);
		URLStreenge urlStreenge=new URLStreenge();
		urlStreenge.setUrl("/");
		urlStreenge.setData(true);
		return urlStreenge;
	}
	
	@PutMapping("/stockage") 
	public URLStreenge updateStockage(@RequestBody Stockage stockage) {
		serviceAnex.updateStockage(stockage);
		URLStreenge urlStreenge=new URLStreenge();
		urlStreenge.setUrl("/#");
		urlStreenge.setData(true);
		return urlStreenge;
	}
	
	@PostMapping("/categorie")
	public URLStreenge  createCategorie(@RequestBody Categorie categorie) {
		serviceAnex.addCategorie(categorie);
		return new URLStreenge();
	}
	
	@PutMapping("/categorie")
	public URLStreenge  updateCategorie(@RequestBody Categorie categorie) {
		serviceAnex.updateCategorie(categorie);
		return new URLStreenge();
	}
	
	@DeleteMapping("/categorie")
	public URLStreenge  deleteCategorie(@RequestBody Categorie categorie) {
		serviceAnex.deleteCategorie(categorie);
		return new URLStreenge();
	}
	
	@PostMapping("/taxe")
	public URLStreenge  createTaxe(@RequestBody Taxe taxe) {
		
		serviceAnex.addTaxe(taxe);
		return new URLStreenge();
	}
	
	
	
	@PutMapping("/taxe")
	public URLStreenge  updateTaxe(@RequestBody Taxe taxe) {
		serviceAnex.updateTaxe(taxe);
		return new URLStreenge();
	}
	
	@DeleteMapping("/taxe")
	public URLStreenge  deleteTaxe(@RequestBody Taxe taxe) {
		serviceAnex.deleteTaxe(taxe);
		return new URLStreenge();
	}
	
	@PostMapping("/stockage")
	public URLStreenge  createStockage(@RequestBody FormFilter formFilter) {
		serviceAnex.createNewStockage(formFilter);
		return new URLStreenge();
	}
}
