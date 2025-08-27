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

import com.streenge.model.vente.facture.Facture;
import com.streenge.service.utils.URLStreenge;
import com.streenge.service.utils.formInt.FormPanier;
import com.streenge.service.utils.formOut.FormPaiement;
import com.streenge.service.utils.formOut.FormTable;
import com.streenge.service.venteService.FactureService;

@RestController
@CrossOrigin
public class FactureController {

	@Autowired
	private FactureService factureService;

	@GetMapping("/factures")
	public FormTable getAllFactues() {
		return factureService.generateListFacture(null);
	}
	
	@GetMapping("/factures/{filter}")
	public FormTable getAllFactues(@PathVariable("filter") String filter) {
		return factureService.generateListFacture(filter);
	}

	@GetMapping("/facture/{idFacture}")
	public FormPanier getFacture(@PathVariable("idFacture") String idFacture) {
		return factureService.getPanierFacture(idFacture);
	}

	@PostMapping("/facture")
	public URLStreenge createFacture(@RequestBody FormPanier formPanier) {
		Facture facture = factureService.createOrUpdateFacture(formPanier, true);
		URLStreenge urlStreenge = new URLStreenge();
		urlStreenge.setUrl("/facture/" + facture.getIdFacture());
		urlStreenge.setData(facture);
		return urlStreenge;
	}

	@PutMapping("/facture")
	public URLStreenge updateFacture(@RequestBody FormPanier formPanier) {
		Facture facture = factureService.createOrUpdateFacture(formPanier, false);
		URLStreenge urlStreenge = new URLStreenge();
		urlStreenge.setUrl("/facture/" + facture.getIdFacture());
		urlStreenge.setData(facture);
		return urlStreenge;
	}

	@PostMapping("/facture/paiement")
	public URLStreenge addPaiement(@RequestBody FormPaiement FormPaiement) {
		FormPaiement newFormPaiement = factureService.addPaiement(FormPaiement);
		URLStreenge urlStreenge = new URLStreenge();
		urlStreenge.setUrl("/facture/" + newFormPaiement.getIdFacture());
		urlStreenge.setData(newFormPaiement);
		return urlStreenge;
	}

	@PutMapping("/facture/paiement")
	public URLStreenge alterPaiement(@RequestBody FormPaiement FormPaiement) {
		FormPaiement newFormPaiement = factureService.alterPaiement(FormPaiement);
		URLStreenge urlStreenge = new URLStreenge();
		urlStreenge.setUrl("/facture/" + newFormPaiement.getIdFacture());
		urlStreenge.setData(newFormPaiement);
		return urlStreenge;
	}

	@DeleteMapping("/facture/paiement")
	public URLStreenge deletePaiement(@RequestBody FormPaiement FormPaiement) {
		boolean isDelete = factureService.deletePaiement(FormPaiement);
		URLStreenge urlStreenge = new URLStreenge();
		urlStreenge.setUrl("/facture/" + FormPaiement.getIdFacture());
		urlStreenge.setData(isDelete);
		return urlStreenge;
	}

	@PostMapping("/facture/cancel/{idFacture}/{idUtilisateur}")
	public URLStreenge cancelFacture(@PathVariable("idFacture") String idFacture,@PathVariable("idUtilisateur") int idUtilisateur) {
		factureService.cancelFacture(idFacture,idUtilisateur);
		return new URLStreenge();
	}

	@PostMapping("/facture/reactive/{idFacture}/{idUtilisateur}")
	public URLStreenge reactiveFacture(@PathVariable("idFacture") String idFacture,@PathVariable("idUtilisateur") int idUtilisateur) {
		factureService.reactiveFacture(idFacture,idUtilisateur);
		return new URLStreenge();
	}

}
