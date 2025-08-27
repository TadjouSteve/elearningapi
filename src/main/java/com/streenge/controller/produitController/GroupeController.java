package com.streenge.controller.produitController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.streenge.model.produit.Groupe;
import com.streenge.service.produitService.GroupeService;
import com.streenge.service.utils.URLStreenge;

@RestController
@CrossOrigin
public class GroupeController {
	@Autowired
	private GroupeService groupeService;

	@PostMapping("/groupe")
	public URLStreenge createProduit(@RequestBody Groupe groupe) {
		groupe = groupeService.createOrUpdate(groupe);
		URLStreenge urlStreenge = new URLStreenge();
		urlStreenge.setData(groupe);
		return urlStreenge;
	}

	@DeleteMapping("/groupe")
	public URLStreenge deleteCategorie(@RequestBody Groupe groupe) {
		groupeService.delete(groupe);
		return new URLStreenge();
	}
}
