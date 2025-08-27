package com.streenge.controller.stockController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.streenge.model.stock.MouvementStock;
import com.streenge.service.stockService.MouvementStockService;
import com.streenge.service.utils.URLStreenge;
import com.streenge.service.utils.formInt.FormPanier;
import com.streenge.service.utils.formOut.FormTable;

@RestController
@CrossOrigin
public class MouvementStockController {

	@Autowired
	private MouvementStockService mouvementStockService;
	
	@GetMapping("/entrestocks")
	public FormTable getAllEntreStock() { 
		return mouvementStockService.generateListEntreOrSortieStock(null, true);
	} 
	
	@GetMapping("/entrestocks/{filter}")
	public FormTable getAllEntreStock(@PathVariable("filter") String filter) { 
		return mouvementStockService.generateListEntreOrSortieStock(filter, true);
	}
	
	@GetMapping("/sortiestocks")
	public FormTable getAllSortieStock() { 
		return mouvementStockService.generateListEntreOrSortieStock(null, false);
	} 
	
	@GetMapping("/sortiestocks/{filter}")
	public FormTable getAllSortieStock(@PathVariable("filter") String filter) { 
		return mouvementStockService.generateListEntreOrSortieStock(filter, false);
	}
	
	@GetMapping("/mouvementstocks")
	public FormTable getAllMouvementEntreStock() { 
		return mouvementStockService.generateListMouvementStock(null);
	}
	
	@GetMapping("/mouvementstocks/{filter}")
	public FormTable getAllMouvementEntreStock(@PathVariable("filter") String filter) { 
		return mouvementStockService.generateListMouvementStock(filter);
	}
	
	@GetMapping("/entrestock/{idEntreStock}")
	public FormPanier getEntreStock(@PathVariable("idEntreStock") String idEntreStock) {
		return mouvementStockService.getPanierManipulationStock(idEntreStock, true,false);
	}
	
	@GetMapping("/sortiestock/{idSortieStock}")
	public FormPanier getSortieStock(@PathVariable("idSortieStock") String idSortieStock) {
		return mouvementStockService.getPanierManipulationStock(idSortieStock, false,false);
	}
	
	

	@GetMapping("/mouvementstock/{idMouvementStock}")
	public FormPanier getMouvementEntreStock(@PathVariable("idMouvementStock") String idMouvementStock) {
		return mouvementStockService.getPanierManipulationStock(idMouvementStock, false,true);
	}
	
	
	@PostMapping("/entrestock")
	public URLStreenge createEntreStock(@RequestBody FormPanier formPanier) { 
		MouvementStock mouvement=mouvementStockService.createOrUpdateEntreOrSortieStock(formPanier, true, true);
		URLStreenge urlStreenge=new URLStreenge();
		urlStreenge.setUrl("/entrestock/"+mouvement.getIdMouvementStock());
		urlStreenge.setData("is Okeeee...!");
		return urlStreenge;
	}
	
	@PostMapping("/sortiestock")
	public URLStreenge createSortieStock(@RequestBody FormPanier formPanier) { 
		MouvementStock mouvement=mouvementStockService.createOrUpdateEntreOrSortieStock(formPanier, true, false);
		URLStreenge urlStreenge=new URLStreenge();
		urlStreenge.setUrl("/sortiestock/"+mouvement.getIdMouvementStock());
		urlStreenge.setData("is Okeeee...!");
		return urlStreenge;
	}
	
	@PostMapping("/mouvementstock")
	public URLStreenge createMouvementEntreStock(@RequestBody FormPanier formPanier) {
		MouvementStock mouvement=mouvementStockService.createOrUpdateMouvementEntreStock(formPanier, true);
		URLStreenge urlStreenge=new URLStreenge();
		urlStreenge.setUrl("/mouvementstock/"+mouvement.getIdMouvementStock());
		urlStreenge.setData("is Okeeee...!");
		return urlStreenge;
	}
	
	
	@PutMapping("/entrestock")
	public URLStreenge updateEntreStock(@RequestBody FormPanier formPanier) { 
		MouvementStock mouvement=mouvementStockService.createOrUpdateEntreOrSortieStock(formPanier, false, true);
		URLStreenge urlStreenge=new URLStreenge();
		urlStreenge.setUrl("/entrestock/"+mouvement.getIdMouvementStock());
		urlStreenge.setData("is Okeeee...!");
		return urlStreenge;
	}
	
	@PutMapping("/sortiestock")
	public URLStreenge updateSortieStock(@RequestBody FormPanier formPanier) { 
		MouvementStock mouvement=mouvementStockService.createOrUpdateEntreOrSortieStock(formPanier, false, false);
		URLStreenge urlStreenge=new URLStreenge();
		urlStreenge.setUrl("/sortiestock/"+mouvement.getIdMouvementStock());
		urlStreenge.setData("is Okeeee...!");
		return urlStreenge;
	}
	
	@PutMapping("/mouvementstock")
	public URLStreenge UpdateMouvementEntreStock(@RequestBody FormPanier formPanier) {
		MouvementStock mouvement=mouvementStockService.createOrUpdateMouvementEntreStock(formPanier, false);
		URLStreenge urlStreenge=new URLStreenge();
		urlStreenge.setUrl("/mouvementstock/"+mouvement.getIdMouvementStock());
		urlStreenge.setData("is Okeeee...!");
		return urlStreenge;
	}
	
	@PostMapping("/entrestock/cancel/{idEntreStock}/{idUtilisateur}")
	public URLStreenge cancelEntrestock(@PathVariable("idEntreStock") String idEntreStock,@PathVariable("idUtilisateur") int idUtilisateur) {
		mouvementStockService.cancelEntreOrSortieStock(idEntreStock, true,idUtilisateur);
		return new URLStreenge();
	}
	
	@PostMapping("/sortiestock/cancel/{idSortieStock}/{idUtilisateur}")
	public URLStreenge cancelSortiestock(@PathVariable("idSortieStock") String idSortieStock,@PathVariable("idUtilisateur") int idUtilisateur) {
		mouvementStockService.cancelEntreOrSortieStock(idSortieStock, false,idUtilisateur);
		return new URLStreenge();
	}
	
	@PostMapping("/mouvementstock/cancel/{idMouvementStock}/{idUtilisateur}")
	public URLStreenge cancelMouvementEntreStock(@PathVariable("idMouvementStock") String idMouvementStock,@PathVariable("idUtilisateur") int idUtilisateur) {
		mouvementStockService.cancelMouvementEntreStock(idMouvementStock,idUtilisateur);
		return new URLStreenge();
	}
	
	
	@PostMapping("/entrestock/reactive/{idEntreStock}/{idUtilisateur}")
	public URLStreenge reactiveEntrestock(@PathVariable("idEntreStock") String idEntreStock,@PathVariable("idUtilisateur") int idUtilisateur) {
		mouvementStockService.reactiveEntreOrSortieStock(idEntreStock, true,idUtilisateur);
		return new URLStreenge();
	}
	
	@PostMapping("/sortiestock/reactive/{idSortieStock}/{idUtilisateur}")
	public URLStreenge reactiveSortiestock(@PathVariable("idSortieStock") String idSortieStock,@PathVariable("idUtilisateur") int idUtilisateur) {
		mouvementStockService.reactiveEntreOrSortieStock(idSortieStock, false,idUtilisateur);
		return new URLStreenge();
	}
	
	@PostMapping("/mouvementstock/reactive/{idMouvementStock}/{idUtilisateur}")
	public URLStreenge reactiveMouvementEntreStock(@PathVariable("idMouvementStock") String idMouvementStock,@PathVariable("idUtilisateur") int idUtilisateur) {
		mouvementStockService.reactiveMouvementEntreStock(idMouvementStock,idUtilisateur);
		return new URLStreenge();
	}
	
}
