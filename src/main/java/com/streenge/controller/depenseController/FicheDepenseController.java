package com.streenge.controller.depenseController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.streenge.model.depense.FicheDepense;
import com.streenge.service.depenseService.FicheDepenseService;
import com.streenge.service.utils.URLStreenge;
import com.streenge.service.utils.formInt.FormFilter;
import com.streenge.service.utils.formInt.FormPanier;
import com.streenge.service.utils.formOut.FormPack;
import com.streenge.service.utils.formOut.FormTable;

@RestController
@CrossOrigin
public class FicheDepenseController {
	@Autowired
	private FicheDepenseService ficheDepenseService;
	
	@PostMapping("/fichedepenses")
	public FormTable getAllFiches(@RequestBody FormFilter formFilter) {
		return ficheDepenseService.getListFicheDepense(formFilter);
	}
	
	@GetMapping("/fichedepense/{idFicheDepense}")
	public FormPanier getpanierFicheDepense(@PathVariable("idFicheDepense") String idFicheDepense) {
		return ficheDepenseService.getFormFicheDepense(idFicheDepense);
	}
	
	@PostMapping("/fichedepense")
	public URLStreenge createFichedepense(@RequestBody FormPanier formPanier) {
		FicheDepense ficheDepense=ficheDepenseService.createOrUpdateFicheDepense(formPanier, true);
		URLStreenge urlStreenge = new URLStreenge();
		urlStreenge.setUrl("/fichedepense/" + ficheDepense.getIdFicheDepense());
		urlStreenge.setData(ficheDepense);
		return urlStreenge;
	}

	
	@PutMapping("/fichedepense")
	public URLStreenge updtateFichedepense(@RequestBody FormPanier formPanier) {
		FicheDepense ficheDepense=ficheDepenseService.createOrUpdateFicheDepense(formPanier, false);
		URLStreenge urlStreenge = new URLStreenge();
		urlStreenge.setUrl("/fichedepense/" + ficheDepense.getIdFicheDepense());
		urlStreenge.setData(ficheDepense);
		return urlStreenge;
	}
	
	@PostMapping("/depenses/listpacks/")
	public List<FormPack> getPacksFilter(@RequestBody FormFilter formFilter) {
		return ficheDepenseService.getFormPacks(formFilter);
	}
	
	@PostMapping("/depense/annuler/{idFicheDepense}")
	public URLStreenge annulerFicheDepense(@RequestBody FormFilter formFilter,@PathVariable("idFicheDepense") String idFicheDepense) {
		ficheDepenseService.annulerFicheDepense(formFilter, idFicheDepense);
		return new URLStreenge();
	}
	
	@PostMapping("/depense/reactive/{idFicheDepense}")
	public URLStreenge reactiveFicheDepense(@RequestBody FormFilter formFilter,@PathVariable("idFicheDepense") String idFicheDepense) {
		ficheDepenseService.activationFicheDepense(formFilter, idFicheDepense);
		return new URLStreenge(); 
	}
	
	
	
}
