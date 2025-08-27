package com.streenge.controller.stockController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.streenge.service.stockService.ManageStockService;
import com.streenge.service.utils.URLStreenge;
import com.streenge.service.utils.formOut.FormViewProduit;

@RestController
@CrossOrigin
public class ManageStockController {

	@Autowired
	private ManageStockService manageStockService;
	
	@PutMapping("/updatestocks/{idUser}")
	public URLStreenge updateStocks(@RequestBody List<FormViewProduit> formViewProduits,@PathVariable("idUser") int idUser) {
		manageStockService.updateStock(formViewProduits,idUser);
		URLStreenge urlStreenge=new URLStreenge();
		urlStreenge.setUrl("/niveaustocks");
		return urlStreenge;
	}
}
