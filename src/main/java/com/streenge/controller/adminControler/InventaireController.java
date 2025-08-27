package com.streenge.controller.adminControler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.streenge.service.adminService.InventaireService;
import com.streenge.service.utils.formInt.FormFilter;
import com.streenge.service.utils.formInt.FormPanier;

@RestController
@CrossOrigin
public class InventaireController {
	@Autowired
	private InventaireService  inventaireService;
	
	@PostMapping("/inventaire/")
	public FormPanier getInventaire(@RequestBody FormFilter formFilter) {
		return inventaireService.getInventaire(formFilter);
	}
}
