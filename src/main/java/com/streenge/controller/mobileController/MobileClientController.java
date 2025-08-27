package com.streenge.controller.mobileController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.streenge.service.mobileService.mobileClientService;
import com.streenge.service.utils.URLStreenge;
import com.streenge.service.utils.formData.FormGroupe;
import com.streenge.service.utils.formInt.FormPanierMobile;
import com.streenge.service.utils.formOut.FormViewProduit;

@RestController
@CrossOrigin
public class MobileClientController {
	@Autowired
	private mobileClientService mobileClientService;
	
	@GetMapping("/mobile/groupes")
	public List<FormGroupe> getListGroupe() {
		return mobileClientService.getListFormGroupeWithProduit();
	}
	
	@GetMapping("/mobile/produits")
	public List<FormViewProduit> researchProduitsMobile(){
		return mobileClientService.researchProduitsByMolie(null);
	}
	
	@GetMapping("/mobile/produits/{filter}")
	public List<FormViewProduit> researchProduitsMobile(@PathVariable("filter") String filter){
		return mobileClientService.researchProduitsByMolie(filter);
	}
	
	@PostMapping("/mobile/commande")
	public URLStreenge createCommandeFromMobile(@RequestBody FormPanierMobile formPanierMobile ) {
		mobileClientService.createBonCommande(formPanierMobile);
		URLStreenge urlStreenge=new URLStreenge();
		urlStreenge.setUrl(""); 
		urlStreenge.setData(true);
		return urlStreenge;
	}
	
	@GetMapping("/mobile/commandes/{idClient}")
	public List<FormPanierMobile> getCommandeMobile(@PathVariable("idClient") String idClient){
		return mobileClientService.getBonCommandeClientMobile(idClient);
	}
	
	
	@DeleteMapping("/mobile/commande/{idBonCommande}/{idClient}")
	public Boolean annulerCommandeParClientMobile(@PathVariable("idBonCommande") String idBonCommande,@PathVariable("idClient") String idClient){
		mobileClientService.annulationBomCommandeParClientMobile(idBonCommande,idClient);
		return true;
	}
	
	
	
}
