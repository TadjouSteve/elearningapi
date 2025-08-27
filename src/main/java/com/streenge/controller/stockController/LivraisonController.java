package com.streenge.controller.stockController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.streenge.model.stock.Livraison;
import com.streenge.service.stockService.LivraisonService;
import com.streenge.service.utils.URLStreenge;
import com.streenge.service.utils.formInt.FormPanier;
import com.streenge.service.utils.formOut.FormTable;

@RestController
@CrossOrigin
public class LivraisonController {

	@Autowired
	private LivraisonService livraisonService;
	
	@GetMapping("/livraisons")
	public FormTable getAllLivraison(){
		return livraisonService.generateListLivraison(null);
	}
	
	@GetMapping("/livraisons/{filter}")
	public FormTable getAllLivraison(@PathVariable("filter") String filter){
		return livraisonService.generateListLivraison(filter);
	}
	
	@PutMapping("/livraison")
	public URLStreenge updateBonLivraison(@RequestBody FormPanier formPanier) {
		Livraison livraison=livraisonService.createOrUpdateLivraison(formPanier, false);
		URLStreenge urlStreenge=new URLStreenge();
		urlStreenge.setUrl("/livraison/"+livraison.getIdLivraison());
		urlStreenge.setData(livraison.getIdLivraison());
		return urlStreenge; 
	}
	
	@GetMapping("/livraison/{idLivraison}")
	public FormPanier getLivraison(@PathVariable("idLivraison") String idLivraison) {
		return livraisonService.getPanierLivraison(idLivraison);
	}
	
	@PostMapping("/livraison/cancel/{idLivraison}/{idUtilisateur}")
	public URLStreenge cancelBonLivraison(@PathVariable("idLivraison") String idLivraison,@PathVariable("idUtilisateur") int idUtilisateur) {
		livraisonService.cancelLivraison(idLivraison,idUtilisateur);
		return new URLStreenge();
	}
	
	@PostMapping("/livraison/reactive/{idLivraison}/{idUtilisateur}")
	public URLStreenge reactiveBonLivraison(@PathVariable("idLivraison") String idLivraison,@PathVariable("idUtilisateur") int idUtilisateur) {
		livraisonService.reactiveLivraison(idLivraison,idUtilisateur);
		return new URLStreenge(); 
	}
	
	@DeleteMapping("/livraison/delete/{idLivraison}/{idUtilisateur}")
	public URLStreenge deleteBonLivraison(@PathVariable("idLivraison") String idLivraison,@PathVariable("idUtilisateur") int idUtilisateur) {
		livraisonService.deleteLivraison(idLivraison,idUtilisateur);
		return new URLStreenge(); 
	}
	
}
