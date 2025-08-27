package com.streenge.controller.adminControler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.streenge.model.admin.Utilisateur;
import com.streenge.service.adminService.UtilisateurService;
import com.streenge.service.utils.URLStreenge;
import com.streenge.service.utils.formInt.FormConnection;
import com.streenge.service.utils.formInt.FormCreateUtilisateur;
import com.streenge.service.utils.formOut.FormTable;
import com.streenge.service.utils.streengeFunction.Cryptage;

@RestController
@CrossOrigin
public class UtilisateurContoller {
	@Autowired
	private UtilisateurService utilisateurService;
	
	
	@PostMapping("/connection")
	public Object getConnection(@RequestBody FormConnection formConnection) {
		
		if (formConnection==null) {
			return null;
		}
		System.out.println("==Tantative de connexion== "+Cryptage.getMd5(formConnection.getPassword()));
		Object object=utilisateurService.connection(formConnection); 
		return object;
	}
	
	
	@GetMapping("/utilisateur/{idUser}")
	public Utilisateur getUtilisateur(@PathVariable("idUser") int idUser) {
		return utilisateurService.getUtilisateur(idUser);
	}
	
	
	@GetMapping("/utilisateurs")
	public FormTable getAllUtilisateur() {
		return utilisateurService.getAllUtilisateurs(null);
	}
	
	
	
	@GetMapping("/utilisateurs/{filter}")
	public FormTable getAllUtilisateur(@PathVariable("filter") String filter) {
		return utilisateurService.getAllUtilisateurs(filter);
	}
	
	@PostMapping("/utilisateur")
	public URLStreenge createUtilsateur(@RequestBody FormCreateUtilisateur formCreateUtilisateur) {
		URLStreenge urlStreenge=new URLStreenge();
		Utilisateur utilisateur = utilisateurService.createOrUpdateUtilisateur(formCreateUtilisateur, true);
		urlStreenge.setData(utilisateur);
		urlStreenge.setUrl("/utilisateur/"+utilisateur.getId());
		return urlStreenge;
	}
	
	@PutMapping("/utilisateur")
	public URLStreenge updateUtilsateur(@RequestBody FormCreateUtilisateur formCreateUtilisateur) {
		URLStreenge urlStreenge=new URLStreenge();
		Utilisateur utilisateur = utilisateurService.createOrUpdateUtilisateur(formCreateUtilisateur, false);
		urlStreenge.setData(utilisateur);
		urlStreenge.setUrl("/utilisateur/"+utilisateur.getId());
		return urlStreenge;
	}
	
	@PostMapping("/utilisateur/update/login")
	public URLStreenge updateLogin(@RequestBody FormConnection formConnection) {
		utilisateurService.updateLogin(formConnection);
		return new URLStreenge();
	} 
	
	
	@PostMapping("/utilisateur/update/password")
	public URLStreenge updatePassword(@RequestBody FormConnection formConnection) {
		utilisateurService.updatePassword(formConnection);
		return new URLStreenge();
	}
	
	@PostMapping("/utilisateur/changestatut/{idUser}")
	public URLStreenge updatePassword(@PathVariable("idUser") int idUser) {
		utilisateurService.changeStatutUtilisateur(idUser, null);
		return new URLStreenge();
	}
	
}
