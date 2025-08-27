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
import com.streenge.model.stock.OrdreAchat;
import com.streenge.service.stockService.OrdreAchatService;
import com.streenge.service.utils.URLStreenge;
import com.streenge.service.utils.formInt.FormPanier;
import com.streenge.service.utils.formOut.FormOrdreAchat;
import com.streenge.service.utils.formOut.FormTable;

@RestController
@CrossOrigin
public class OrdreAchatController {
	@Autowired
	private OrdreAchatService ordreAchatService;
	
	@GetMapping("/ordreachats")
	public FormTable getAllOrdreAchat() { 
		return  ordreAchatService.generateListOrdreAchat(null);
	} 
	
	@GetMapping("/ordreachat/{idOrdreAchat}")
	public FormOrdreAchat getOrdreAchat(@PathVariable("idOrdreAchat") String idOrdreAchat) {
		return ordreAchatService.getPanierOrdreAchat(idOrdreAchat);
	}
	
	@GetMapping("/ordreachats/{filter}")
	public FormTable getAllOrdreAchat(@PathVariable("filter") String filter) { 
		return  ordreAchatService.generateListOrdreAchat(filter);
	}
	
	@PostMapping("/ordreachat")
	public URLStreenge createOrdreachat(@RequestBody FormPanier formPanier) {
		OrdreAchat ordreAchat=ordreAchatService.createOrUpdateOdreAchat(formPanier, true);
		URLStreenge urlStreenge=new URLStreenge();
		urlStreenge.setUrl("/ordreachat/"+ordreAchat.getIdOrdreAchat()); 
		urlStreenge.setData(ordreAchat.getIdOrdreAchat());
		return urlStreenge; 
	}
	
	@PutMapping("/ordreachat")
	public URLStreenge UpdateOrdreachat(@RequestBody FormPanier formPanier) {
		OrdreAchat ordreAchat=ordreAchatService.createOrUpdateOdreAchat(formPanier, false);
		URLStreenge urlStreenge=new URLStreenge();
		urlStreenge.setUrl("/ordreachat/"+ordreAchat.getIdOrdreAchat()); 
		urlStreenge.setData(ordreAchat.getIdOrdreAchat());
		return urlStreenge; 
	}
	
	@GetMapping("/ordreachat/formlivraison/{idOrdreAchat}")
	public FormPanier getFormToCreateLivraison(@PathVariable("idOrdreAchat") String idOrdreAchat) {
		return ordreAchatService.getpanierToCreateLivraison(idOrdreAchat);
	}
	
	@PostMapping("/ordreachat/formlivraison")
	public URLStreenge createLivraisonFromOrdreachat(@RequestBody FormPanier formPanier) {
		Livraison livraison=ordreAchatService.createLivraisonFromOrdreAchat(formPanier);
		URLStreenge urlStreenge=new URLStreenge();
		urlStreenge.setUrl("/livraison/"+livraison.getIdLivraison());
		urlStreenge.setData(livraison.getIdLivraison());
		return urlStreenge; 
	}
	
	@PostMapping("/ordreachat/cancel/{idOrdreAchat}/{idUtilisateur}")
	public URLStreenge cancelBonLivraison(@PathVariable("idOrdreAchat") String idOrdreAchat,@PathVariable("idUtilisateur") int idUtilisateur) {
		ordreAchatService.cancelOrdreAchat(idOrdreAchat,idUtilisateur);
		return new URLStreenge();
	}
	
	@PostMapping("/ordreachat/reactive/{idOrdreAchat}/{idUtilisateur}")
	public URLStreenge reactiveBonLivraison(@PathVariable("idOrdreAchat") String idOrdreAchat,@PathVariable("idUtilisateur") int idUtilisateur) {
		ordreAchatService.reactiveOrdreAchat(idOrdreAchat,idUtilisateur);
		return new URLStreenge(); 
	}
	
	@DeleteMapping("/ordreachat/delete/{idOrdreAchat}/{idUtilisateur}")
	public URLStreenge deleteBonLivraison(@PathVariable("idOrdreAchat") String idOrdreAchat,@PathVariable("idUtilisateur") int idUtilisateur) {
		ordreAchatService.deleteOrdreAchat(idOrdreAchat,idUtilisateur);
		return new URLStreenge(); 
	}
}
