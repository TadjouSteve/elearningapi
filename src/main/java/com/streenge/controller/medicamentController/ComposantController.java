package com.streenge.controller.medicamentController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.streenge.model.produit.medicament.Composant;
import com.streenge.service.produitService.medicament.ComposantService;
import com.streenge.service.utils.URLStreenge;

@RestController
@CrossOrigin
public class ComposantController {
	@Autowired
	private ComposantService composantService;
	
	@PostMapping("/composant")
	public URLStreenge createProduit(@RequestBody Composant composant) {
		composant=composantService.createOrUpdate(composant);
		URLStreenge urlStreenge = new URLStreenge();
		urlStreenge.setData(composant);
		return urlStreenge;
	}
	
	@DeleteMapping("/composant")
	public URLStreenge  deleteCategorie(@RequestBody Composant composant) {
		if (composant!=null) {
			composantService.delete(composant.getId());
		}
		return new URLStreenge();
	}
}
