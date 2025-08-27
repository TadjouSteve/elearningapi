package com.streenge.controller.stockController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.streenge.service.stockService.RegistreStockService;
import com.streenge.service.utils.formInt.FormFilter;

@RestController
@CrossOrigin
public class RegistreStockController {
	
	@Autowired
	private RegistreStockService registreStockService;
	
	@GetMapping("/registrestocks")
	public Object getRegistreStock() {
		return registreStockService.getRegistreStocks(null,null);
	}
	
	@GetMapping("/registrestocks/{filter}")
	public Object getRegistreStock(@PathVariable("filter") String filter) {
		return registreStockService.getRegistreStocks(filter,null);
	}
	
	
	@PostMapping("/registrestocks")
	public Object getRegistreStock(@RequestBody FormFilter formFilter) {
		return registreStockService.getRegistreStocks(null,formFilter);
	}
	
	@PostMapping("/registrestocks/{filter}")
	public Object getRegistreStock(@PathVariable("filter") String filter,@RequestBody FormFilter formFilter) {
		return registreStockService.getRegistreStocks(filter,formFilter);
	}

}
