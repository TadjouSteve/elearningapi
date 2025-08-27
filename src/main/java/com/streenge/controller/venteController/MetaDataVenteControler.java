package com.streenge.controller.venteController;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class MetaDataVenteControler {
	/*
	 @Autowired
	 
	private ClientRepository clientRepository;

	@Autowired
	private FournisseurRepository fournisseurRepository;

	@Autowired
	private TaxeRepository taxeRepository;

	@Autowired
	private StockageRepository stockageRepository;
	
	@Autowired
	private CaisseRepository caisseRepository;

	/*@GetMapping("vent/metadata")
	public MetaDataVente getDataVente() {

		MetaDataVente dataVente = new MetaDataVente();
		List<String> nomClients = new ArrayList<>();
		List<String> nomFournisseurs = new ArrayList<>();

		List<Client> clients = clientRepository.findAllByOrderByNomAsc();
		for (Client client : clients) {
			if (!client.getEtat().equals(Etat.getDelete()))
				nomClients.add(client.getNom());
		}

		List<Fournisseur> fournisseurs = fournisseurRepository.findAllByOrderByNomAsc();
		for (Fournisseur fournisseur : fournisseurs) {
			if (!fournisseur.getEtat().equals(Etat.getDelete()))
				nomFournisseurs.add(fournisseur.getNom());
		}

		List<Taxe>taxes=new ArrayList<Taxe>();
		
		for (Taxe taxe : taxeRepository.findAllByOrderByNomAsc()) {
			if (!Etat.getDelete().equals(taxe.getEtat())) {
				taxes.add(taxe);
			}
		}
		
		List<Caisse>caisses =new ArrayList<Caisse>();
		
		for (Caisse caisse : caisseRepository.findAll()) {
			caisse.setSolde(0);
			caisses.add(caisse);
			
		}
		
		dataVente.setNomClients(nomClients);
		dataVente.setNomFournisseurs(nomFournisseurs);
		dataVente.setTaxes(taxes);
		dataVente.setStockages((List<Stockage>) stockageRepository.findAll());
		//dataVente.setCaisses(caisses);

		return dataVente;
	}*/

}
