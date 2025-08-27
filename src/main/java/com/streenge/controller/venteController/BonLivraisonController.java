package com.streenge.controller.venteController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.streenge.model.vente.BonLivraison;
import com.streenge.service.utils.URLStreenge;
import com.streenge.service.utils.formInt.FormPanier;
import com.streenge.service.utils.formOut.FormTable;
import com.streenge.service.venteService.BonLivraisonService;

@RestController
@CrossOrigin
public class BonLivraisonController {
	@Autowired
	private BonLivraisonService bonLivraisonService;

	@GetMapping("/bonlivraisons")
	public FormTable getAllBonLivraison(){
		return bonLivraisonService.generateListBonLivraison(null);
	}
	
	@GetMapping("/bonlivraisons/{filter}")
	public FormTable getAllBonLivraison(@PathVariable("filter") String filter){
		return bonLivraisonService.generateListBonLivraison(filter);
	}
	
	@PutMapping("/bonlivraison")
	public URLStreenge updateBonLivraison(@RequestBody FormPanier formPanier) {
		BonLivraison bonLivraison=bonLivraisonService.createOrUpdateBonLivraison(formPanier, false);
		URLStreenge urlStreenge=new URLStreenge();
		urlStreenge.setUrl("/bonlivraison/"+bonLivraison.getIdBonLivraison());
		urlStreenge.setData(bonLivraison);
		return urlStreenge; 
	}

	@GetMapping("/bonlivraison/{idBonlivraison}")
	public FormPanier getbonLivraison(@PathVariable("idBonlivraison") String idBonlivraison) {
		return bonLivraisonService.getPanierBonLivraison(idBonlivraison);
	}
	
	
	@PostMapping("/bonlivraison/statut/{idBonlivraison}/{statut}/{idUtilisateur}")
	public URLStreenge changeStatutBonLivraison(@PathVariable("idBonlivraison") String idBonlivraison,@PathVariable("statut") String statut,@PathVariable("idUtilisateur") int idUtilisateur) {
		bonLivraisonService.setStatutBonLivraison(idBonlivraison, statut,idUtilisateur);
		return new URLStreenge();
	} 
	
	
	@PostMapping("/bonlivraison/cancel/{idBonlivraison}/{idUtilisateur}")
	public URLStreenge cancelBonLivraison(@PathVariable("idBonlivraison") String idBonlivraison,@PathVariable("idUtilisateur") int idUtilisateur) {
		bonLivraisonService.cancelBonLivraison(idBonlivraison,idUtilisateur);
		return new URLStreenge();
	}
	
	@PostMapping("/bonlivraison/reactive/{idBonlivraison}/{idUtilisateur}")
	public URLStreenge reactiveBonLivraison(@PathVariable("idBonlivraison") String idBonlivraison) {
		bonLivraisonService.reactiveBonLivraison(idBonlivraison);
		return new URLStreenge(); 
	}
	
	@DeleteMapping("/bonlivraison/delete/{idBonlivraison}/{idUtilisateur}")
	public URLStreenge deleteBonLivraison(@PathVariable("idBonlivraison") String idBonlivraison,@PathVariable("idUtilisateur") int idUtilisateur) {
		bonLivraisonService.deleteBonLivraison(idBonlivraison,idUtilisateur);
		return new URLStreenge(); 
	}
	
}
