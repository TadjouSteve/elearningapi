package com.streenge.controller.adminControler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.streenge.model.admin.money.MouvementCaisse;
import com.streenge.service.adminService.CaisseService;
import com.streenge.service.utils.URLStreenge;
import com.streenge.service.utils.formInt.FormDepot;
import com.streenge.service.utils.formOut.FormCaisse;
import com.streenge.service.utils.formOut.FormTable;

@RestController
@CrossOrigin
public class CaisseController {
	@Autowired
	private CaisseService caisseService;
	
	@GetMapping("/caisses/user/{idUser}")
	public FormTable getCaisses(@PathVariable("idUser")  int idUser) {
		return caisseService.genereteListCaisse(idUser, false);
	}
	
	@GetMapping("/caisses/root/")
	public FormTable getCaisses() {
		return caisseService.genereteListCaisse(0, true);
	}
	
	@GetMapping("/caisse/{idCaisse}")
	public FormCaisse getCaisse(@PathVariable("idCaisse")  int idCaisse) {
		return caisseService.getFormCaisse(idCaisse);
	}
	
	@PostMapping("/caisse/money/depot/")
	public URLStreenge depotCaisse(@RequestBody FormDepot formDepot) {
		MouvementCaisse mouvementCaisse=new MouvementCaisse();
		if (formDepot!=null) {
			mouvementCaisse=caisseService.createDepotOrRetraitCaisse(formDepot, true);
		}
		URLStreenge urlStreenge = new URLStreenge();
		urlStreenge.setUrl("/caisse/mouvement/"+mouvementCaisse.getIdMouvementCaisse());
		urlStreenge.setData(mouvementCaisse.getIdMouvementCaisse());
		return urlStreenge;
	}
	
	@PostMapping("/caisse/money/retrait/")
	public URLStreenge retraitCaisse(@RequestBody FormDepot formDepot) {
		MouvementCaisse mouvementCaisse=new MouvementCaisse();
		if (formDepot!=null) {
			mouvementCaisse=caisseService.createDepotOrRetraitCaisse(formDepot, false);
		}
		URLStreenge urlStreenge = new URLStreenge();
		urlStreenge.setUrl("/caisse/mouvement/"+mouvementCaisse.getIdMouvementCaisse());
		urlStreenge.setData(mouvementCaisse.getIdMouvementCaisse());
		return urlStreenge;
	}
	
	@GetMapping("/caisse/money/depot/{idMouvement}")
	public FormDepot getMouvementCaisse(@PathVariable("idMouvement") String idMouvement) {
		return caisseService.getFormDepotCaisse(idMouvement);
	}
	
}


