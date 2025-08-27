package com.streenge.controller.stockController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.streenge.service.stockService.NiveauStockService;
import com.streenge.service.utils.formInt.FormFilter;
import com.streenge.service.utils.formOut.FormViewProduit;

@RestController
@CrossOrigin
public class NiveauStockController {

	@Autowired
	private NiveauStockService niveauStockService;
	
	@GetMapping("/niveaustock")
	public List<FormViewProduit> getAllProduitStock() {
		return niveauStockService.getAllFormProduit(null);
	}
	
	
	
	@GetMapping("/niveaustock/{filter}")
	public List<FormViewProduit> getAllProduitStock(@PathVariable("filter") String filter) {
		return niveauStockService.getAllFormProduit(filter);
	}
	
	@PostMapping("/niveaustock")
	public List<FormViewProduit> getAllProduitStock01(@RequestBody FormFilter formFilter) {
		return niveauStockService.getAllFormProduit(null,formFilter);
	}
	
	@PostMapping("/niveaustock/{filter}")
	public List<FormViewProduit> getAllProduitStock01(@PathVariable("filter") String filter, @RequestBody FormFilter formFilter) {
		return niveauStockService.getAllFormProduit(filter,formFilter);
	}
}
