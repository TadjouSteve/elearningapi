package com.streenge.controller.venteController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.streenge.model.admin.Utilisateur;
import com.streenge.model.vente.BonCommande;
import com.streenge.model.vente.Devis;
import com.streenge.model.vente.facture.Facture;
import com.streenge.service.utils.URLStreenge;
import com.streenge.service.utils.formInt.FormPanier;
import com.streenge.service.utils.formOut.FormTable;
import com.streenge.service.venteService.DevisService;

@RestController
@CrossOrigin
public class DevisControler {
	
	@Autowired
	private DevisService devisService;
	
	
	@GetMapping("/devis")
	public FormTable getAllDevis() { 
		return devisService.generateListDevis(null);
	}
	
	
	@GetMapping("/devis/{filter}")
	public FormTable getAllDevis(@PathVariable("filter") String filter) {
		return devisService.generateListDevis(filter);
	}
	
	@GetMapping("/devi/{idDevis}")
	public FormPanier getDevi(@PathVariable("idDevis") String idDevis) {
		return devisService.getPanierDevis(idDevis);
	}
	
	
	@PostMapping("/devi")
	public URLStreenge createDevis(@RequestBody FormPanier formPanier) {
		Devis devi= devisService.createOrUpdateDevis(formPanier, true);
		URLStreenge urlStreenge=new URLStreenge();
		urlStreenge.setUrl("/devis/"+devi.getIdDevis()); 
		urlStreenge.setData(devi);
		return urlStreenge; 
	}
	
	@PutMapping("/devi")
	public URLStreenge updateDevis(@RequestBody FormPanier formPanier) {
		Devis devi= devisService.createOrUpdateDevis(formPanier, false);
		URLStreenge urlStreenge=new URLStreenge();
		urlStreenge.setUrl("/devis/"+devi.getIdDevis()); 
		urlStreenge.setData(devi);
		return urlStreenge; 
	}
	
	@PutMapping("/devis/accepte/{idDevis}")
	public URLStreenge accepteDevis(@PathVariable("idDevis") String idDevis) {
		devisService.changeStatutDevis(idDevis, true);
		return new URLStreenge();
	}
	
	@PutMapping("/devis/rejete/{idDevis}")
	public URLStreenge rejeteDevis(@PathVariable("idDevis") String idDevis) {
		devisService.changeStatutDevis(idDevis, false);
		return new URLStreenge();
	}
	
	@PostMapping("/devis/createfacture/{idDevis}")
	public URLStreenge createFactureFromDevis(@PathVariable("idDevis") String idDevis, @RequestBody Utilisateur utilisateur) {
		Facture facture= devisService.tranformDevisToFacture(idDevis,utilisateur);
		URLStreenge urlStreenge=new URLStreenge();
		urlStreenge.setUrl("/facture/"+facture.getIdFacture());
		urlStreenge.setData(facture);
		return urlStreenge; 
	}
	
	@PostMapping("/devis/createcommande/{idDevis}")
	public URLStreenge createBonCommandeFromDevis(@PathVariable("idDevis") String idDevis, @RequestBody Utilisateur utilisateur) {
		BonCommande bonCommande=devisService.tranformDevisToBonCommande(idDevis,utilisateur);
		URLStreenge urlStreenge=new URLStreenge();
		urlStreenge.setUrl("/boncommande/"+bonCommande.getIdBonCommande());
		urlStreenge.setData(bonCommande);
		return urlStreenge; 
	}
	
}
