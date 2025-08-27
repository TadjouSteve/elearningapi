package com.streenge.controller.contactController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.streenge.model.admin.money.MouvementSoldeClient;
import com.streenge.service.contactService.MouvementSoldeClientService;
import com.streenge.service.utils.URLStreenge;
import com.streenge.service.utils.formInt.FormDepot;

@RestController
@CrossOrigin
public class MouvementSoldeClientController {
	@Autowired
	private MouvementSoldeClientService mouvementSoldeClientService; 
	
	
	@GetMapping("/depot/{idDepot}")
	public FormDepot getDepotCompteClient(@PathVariable("idDepot") String idDepot) {
		return mouvementSoldeClientService.getFormDepot(idDepot);
	}
	
	@GetMapping("/retrait/{idRetrait}")
	public FormDepot getRetraitCompteClient(@PathVariable("idRetrait") String idRetrait) {
		return mouvementSoldeClientService.getFormDepot(idRetrait);
	}
	
	@PostMapping("/depot")
	public URLStreenge depotCompteClient(@RequestBody FormDepot formDepot) {
		MouvementSoldeClient mouvementSoldeClient = new MouvementSoldeClient();
		if (formDepot!=null) {
			mouvementSoldeClient=mouvementSoldeClientService.createDepotOrRetraitCompteClient(formDepot,true);
		}
		URLStreenge urlStreenge = new URLStreenge();
		urlStreenge.setUrl("/client/depot/"+mouvementSoldeClient.getIdMouvementSoldeClient());
		urlStreenge.setData(mouvementSoldeClient.getIdMouvementSoldeClient());
		return urlStreenge;
	}
	
	@PostMapping("/retrait")
	public URLStreenge retraitCompteClient(@RequestBody FormDepot formDepot) {
		MouvementSoldeClient mouvementSoldeClient = new MouvementSoldeClient();
		if (formDepot!=null) {
		 mouvementSoldeClient= mouvementSoldeClientService.createDepotOrRetraitCompteClient(formDepot,false);
		}
		URLStreenge urlStreenge = new URLStreenge();
		urlStreenge.setUrl("/client/retrait/"+mouvementSoldeClient.getIdMouvementSoldeClient());
		urlStreenge.setData(mouvementSoldeClient.getIdMouvementSoldeClient());
		return urlStreenge;
	}

}
