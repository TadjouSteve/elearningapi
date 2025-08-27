package com.streenge.controller.adminControler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.streenge.service.adminService.RapportService;
import com.streenge.service.utils.formInt.FormFilter;
import com.streenge.service.utils.formOut.FormTable;

@CrossOrigin
@RestController
public class RapportController {
	@Autowired
	private RapportService rapportService;

	@PostMapping("/rapport/vente/produit/")
	public FormTable getRapportVenteProduits(@RequestBody FormFilter formFilter) {
		return rapportService.generateRapportVenteProduit(null, formFilter);
	}

	@PostMapping("/rapport/vente/produit/{filterProduit}")
	public FormTable getRapportVenteProduits(@PathVariable("filterProduit") String filterProduit,
			@RequestBody FormFilter formFilter) {
		return rapportService.generateRapportVenteProduit(filterProduit, formFilter);
	}
	
	@PostMapping("/rapport/vente/service/")
	public FormTable getRapportVenteService(@RequestBody FormFilter formFilter) {
		return rapportService.generateRapportVenteService(null, formFilter);
	}
	
	@PostMapping("/rapport/vente/service/{filterService}")
	public FormTable getRapportVenteService(@PathVariable("filterService") String filterService,
			@RequestBody FormFilter formFilter) {
		return rapportService.generateRapportVenteService(filterService, formFilter);
	}

	@PostMapping("/rapport/vente/client/")
	public FormTable getRapportVenteClients(@RequestBody FormFilter formFilter) {
		return rapportService.generateRapportVenteClients(null, formFilter);
	}

	@PostMapping("/rapport/vente/client/{filterClient}")
	public FormTable getRapportVenteClients(@PathVariable("filterClient") String filterClient,
			@RequestBody FormFilter formFilter) {
		return rapportService.generateRapportVenteClients(filterClient, formFilter);
	}
	
	@PostMapping("/rapport/vente/utilisateur/")
	public FormTable getRapportVenteUtilisateur(@RequestBody FormFilter formFilter) {
		return rapportService.generateRapportVenteUtilisateur(null, formFilter);
	}

	@PostMapping("/rapport/vente/utilisateur/{filterUtilisateur}")
	public FormTable getRapportVenteUtilisateur(@PathVariable("filterUtilisateur") String filterUtilisateur,
			@RequestBody FormFilter formFilter) {
		return rapportService.generateRapportVenteUtilisateur(filterUtilisateur, formFilter);
	}
	
	@PostMapping("/rapport/vente/periode/")
	public FormTable getRapportVentePeriode(@RequestBody FormFilter formFilter) {
		return rapportService.generateRapporVentePeriode(null, formFilter);
	}
	
	@PostMapping("/rapport/facture/nonpaye/")
	public FormTable getRapportFactureNonPaye(@RequestBody FormFilter formFilter) {
		return rapportService.generateRapportFactureNonPaye(null, formFilter);
	}
	
	@PostMapping("/rapport/facture/nonpaye/{filter}")
	public FormTable getRapportFactureNonPaye(@PathVariable("filter") String filter, @RequestBody FormFilter formFilter) {
		return rapportService.generateRapportFactureNonPaye(filter, formFilter);
	}
	
	@PostMapping("/rapport/depense/pointdepense/")
	public FormTable getRapportDepensePointDepense(@RequestBody FormFilter formFilter) {
		return rapportService.generateRapportDepensePointDepense(null, formFilter);
	}
	
	@PostMapping("/rapport/depense/pointdepense/{filter}")
	public FormTable getRapportDepensePointDepense(@PathVariable("filter") String filter,@RequestBody FormFilter formFilter) {
		return rapportService.generateRapportDepensePointDepense(filter, formFilter);
	}
	
	@PostMapping("/rapport/depense/utilisateur/")
	public FormTable getRapportDepenseParUtilisateur(@RequestBody FormFilter formFilter) {
		return rapportService.generateRapportDepenseParUtilisateur(null, formFilter);
	}
	
	@PostMapping("/rapport/depense/utilisateur/{filter}")
	public FormTable getRapportDepenseParUtilisateur(@PathVariable("filter") String filter,@RequestBody FormFilter formFilter) {
		return rapportService.generateRapportDepenseParUtilisateur(filter, formFilter);
	}
	
	@PostMapping("/rapport/depense/periode/")
	public FormTable getRapportDepensePeriode(@RequestBody FormFilter formFilter) {
		return rapportService.generateRapporDepensePeriode(null, formFilter);
	}
	
}
