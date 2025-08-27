package com.streenge.controller.venteController;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.streenge.model.admin.Stockage;
import com.streenge.model.admin.Taxe;
import com.streenge.model.contact.Client;
import com.streenge.model.contact.Fournisseur;
import com.streenge.repository.adminRepository.StockageRepository;
import com.streenge.repository.adminRepository.TaxeRepository;
import com.streenge.repository.contactRepo.ClientRepository;
import com.streenge.repository.contactRepo.FournisseurRepository;
import com.streenge.repository.produitRepo.CategorieRepository;
import com.streenge.service.utils.formOut.MetaDataVente;
import com.streenge.service.utils.streengeData.Etat;

@RestController
@CrossOrigin
public class MetaDataForVenteController {
	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private FournisseurRepository fournisseurRepository;

	@Autowired
	private TaxeRepository taxeRepository;

	@Autowired
	private StockageRepository stockageRepository;
	
	@Autowired
	private CategorieRepository categorieRepository;

	@GetMapping("vente/metadata")
	public MetaDataVente getMetaDataVente() {

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
		
		dataVente.setNomClients(nomClients);
		dataVente.setCategories(categorieRepository.findAllByOrderByNomAsc());
		dataVente.setNomFournisseurs(nomFournisseurs);
		dataVente.setTaxes(taxes);
		dataVente.setStockages((List<Stockage>) stockageRepository.findAll());	
		return dataVente;
	}
}
