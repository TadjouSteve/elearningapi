package com.streenge.controller.venteController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.streenge.model.vente.BonCommande;
import com.streenge.model.vente.BonLivraison;
import com.streenge.model.vente.facture.Facture;
import com.streenge.service.utils.URLStreenge;
import com.streenge.service.utils.formInt.FormPanier;
import com.streenge.service.utils.formOut.FormBonCommande;
import com.streenge.service.utils.formOut.FormTable;
import com.streenge.service.venteService.BonCommandeService;

@RestController
@CrossOrigin
public class BonCommandeController {
	@Autowired
	private BonCommandeService bonCommandeService;
	
	@GetMapping("/boncommandes")
	public FormTable getAllBonCommandes() { 
		return  bonCommandeService.generateListBonCommande(null);
	}
	
	@GetMapping("/boncommandes/{filter}")
	public FormTable getAllBonCommandes(@PathVariable("filter") String filter) { 
		return  bonCommandeService.generateListBonCommande(filter);
	}
	
	
	@GetMapping("/boncommande/{idBonCommande}")
	public FormBonCommande getBonCommande(@PathVariable("idBonCommande") String idBonCommande) {
		return bonCommandeService.getPanierBonCommande(idBonCommande);
	}
	
	
	@PostMapping("/boncommande")
	public URLStreenge createBonCommande(@RequestBody FormPanier formPanier) {
		BonCommande bonCommande=bonCommandeService.createOrUpdateBonCommande(formPanier, true);
		URLStreenge urlStreenge=new URLStreenge();
		urlStreenge.setUrl("/boncommande/"+bonCommande.getIdBonCommande()); 
		urlStreenge.setData(bonCommande.getIdBonCommande());
		return urlStreenge; 
	}
	
	@PutMapping("/boncommande")
	public URLStreenge updateBonCommande(@RequestBody FormPanier formPanier) {
		BonCommande bonCommande=bonCommandeService.createOrUpdateBonCommande(formPanier, false);
		URLStreenge urlStreenge=new URLStreenge();
		urlStreenge.setUrl("/boncommande/"+bonCommande.getIdBonCommande()); 
		urlStreenge.setData(bonCommande.getIdBonCommande());
		return urlStreenge; 
	}
	
	@GetMapping("/boncommande/formfacture/{idBonCommande}")
	public FormPanier getFormToCreateFacture(@PathVariable("idBonCommande") String idBonCommande) {
		return bonCommandeService.getpanierToCreateFacture(idBonCommande);
	}
	
	@PostMapping("/boncommande/formfacture/")
	public URLStreenge createFactureFromBonCommande(@RequestBody FormPanier formPanier) {
		Facture facture= bonCommandeService.createFactureFromBonCommande(formPanier);
		URLStreenge urlStreenge=new URLStreenge();
		urlStreenge.setUrl("/facture/"+facture.getIdFacture());
		urlStreenge.setData(facture);
		return urlStreenge; 
	}
	
	
	@GetMapping("/boncommande/formbonlivraison/{idBonCommande}")
	public FormPanier getFormToCreateBonLivraison(@PathVariable("idBonCommande") String idBonCommande) {
		return bonCommandeService.getpanierToCreateBonLivraison(idBonCommande);
	}
	
	@PostMapping("/boncommande/formbonlivraison/")
	public URLStreenge createBonLivraisonFromBonCommande(@RequestBody FormPanier formPanier) {
		BonLivraison bonLivraison = bonCommandeService.createBonLivraisonFromBonCommande(formPanier);
		URLStreenge urlStreenge=new URLStreenge();
		urlStreenge.setUrl("/bonlivraison/"+bonLivraison.getIdBonLivraison());
		urlStreenge.setData(bonLivraison);
		return urlStreenge; 
	}
	
	
	@PostMapping("/boncommande/cancel/{idBonCommande}/{idUtilisateur}")
	public URLStreenge cancelBonCommande(@PathVariable("idBonCommande") String idBonCommande,@PathVariable("idUtilisateur") int idUtilisateur) {
		bonCommandeService.cancelBonCommande(idBonCommande,idUtilisateur);
		return new URLStreenge();
	}

	@PostMapping("/boncommande/reactive/{idBonCommande}/{idUtilisateur}")
	public URLStreenge reactiveBonCommande(@PathVariable("idBonCommande") String idBonCommande,@PathVariable("idUtilisateur") int idUtilisateur) {
		bonCommandeService.reactiveBonCommande(idBonCommande,idUtilisateur);
		return new URLStreenge();
	}
	
}

