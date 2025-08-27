package com.streenge.controller.contactController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.streenge.model.contact.Fournisseur;
import com.streenge.service.contactService.FournisseurService;
import com.streenge.service.utils.URLStreenge;
import com.streenge.service.utils.formInt.FormCreateContact;
import com.streenge.service.utils.formOut.FormTable;

@RestController
@CrossOrigin
public class FournisseurController {
	@Autowired
	private FournisseurService fournisseurService;
	
	@GetMapping("/fournisseurs")
	public FormTable getAllFournisseurs(){
		return fournisseurService.getAllFournisseurs(null); 
	}
	
	@GetMapping("/fournisseurs/{filter}")
	public FormTable getAllFournisseursFilter(@PathVariable("filter") String filter){
		return fournisseurService.getAllFournisseurs(filter); 
	}
	
	@PostMapping("/fournisseur")
	public URLStreenge createFournisseur(@RequestBody FormCreateContact form) {
		URLStreenge urlStreenge=new URLStreenge();
		Fournisseur fournisseur=fournisseurService.createFournisseur(form);
				
		if (fournisseur!=null) {
			urlStreenge.setData(fournisseur.getNom());
			urlStreenge.setUrl("/fournisseur/"+fournisseur.getIdFournisseur());
		}
		return urlStreenge;
	}

	@PutMapping("/fournisseur")
	public URLStreenge AlterFournisseur(@RequestBody FormCreateContact form) {
		URLStreenge urlStreenge = new URLStreenge();
		Fournisseur fournisseur = fournisseurService.alterFournisseur(form);

		if (fournisseur != null) {
			urlStreenge.setData(fournisseur.getNom());
			urlStreenge.setUrl("/fournisseur/"+fournisseur.getIdFournisseur());
		}
		return urlStreenge;
	}
	
	@GetMapping("/fournisseur/{id}")
	public FormCreateContact geFormContact(@PathVariable("id") String id) {
		return fournisseurService.getFournissuer(id);
	}
}
