package com.streenge.service.stockService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.streenge.model.admin.Stockage;
import com.streenge.model.admin.Utilisateur;
import com.streenge.model.produit.Pack;
import com.streenge.model.produit.ProduitStockage;
import com.streenge.model.stock.RegistreStock;
import com.streenge.repository.adminRepository.StockageRepository;
import com.streenge.repository.adminRepository.UtilisateurRepository;
import com.streenge.repository.produitRepo.ProduitStockageRepository;
import com.streenge.repository.stockRepo.RegistreStockRepository;
import com.streenge.service.utils.formInt.FormFilter;
import com.streenge.service.utils.formOut.LineRegistreStrock;
import com.streenge.service.utils.streengeData.Data;
import com.streenge.service.utils.streengeData.Etat;
import com.streenge.service.utils.streengeData.Profil;
import com.streenge.service.utils.streengeData.Statut;
import com.streenge.service.utils.streengeFunction.Methode;

@Service
public class RegistreStockService {

	@Autowired
	private ProduitStockageRepository produitStockageRepository;

	@Autowired
	private RegistreStockRepository registreStockRepository;

	@Autowired
	private UtilisateurRepository utilisateurRepository;

	@Autowired
	private StockageRepository stockageRepository;

	public List<LineRegistreStrock> getRegistreStocks(String filter, FormFilter formFilter) {

		if (formFilter == null) {
			formFilter = new FormFilter();
			formFilter.setUtilisateur(new Utilisateur());
		}

		Utilisateur utilisateur = formFilter.getUtilisateur();
		if (!(Profil.getRoot().equals(utilisateur.getProfil()) && Data.absolutePassWordMD5.equals(utilisateur.getPassword())) && utilisateurRepository.findById(utilisateur.getId()).isPresent()) {
			utilisateur = utilisateurRepository.findById(utilisateur.getId()).get();
			formFilter.setUtilisateur(utilisateur);
		}

		Stockage stockage = null;

		if (stockageRepository.findById(formFilter.getIdStockage()).isPresent()) {
			stockage = stockageRepository.findById(formFilter.getIdStockage()).get();
		}

		List<LineRegistreStrock> lineRegistreStrocks = new ArrayList<LineRegistreStrock>();

		List<RegistreStock> registreStocks;
		if (filter != null) {
			registreStocks = registreStockRepository
					.findAllByProduitNomStartingWithOrUtilisateurNomStartingWithOrUtilisateurNomAffichageStartingWithOrStockageNomStartingWithOrIdDocumentStartingWithOrderByIdDesc(
							filter, filter, filter, filter, filter);

			if (registreStocks.size() < 1 && filter.length() >= 2) {
				registreStocks = registreStockRepository
						.findAllByProduitNomContainingOrProduitSkuContainingOrderByIdDesc(filter, filter);
			}
		} else { 
			registreStocks = registreStockRepository.findAllByOrderByIdDesc();
		}

		for (RegistreStock registreStock : registreStocks) {
			if (!Etat.getDelete().equals(registreStock.getProduit().getEtat())
					&& registreStock.getProduit().getType() == null
					&& (stockage == null || stockage.getIdStockage() == registreStock.getStockage().getIdStockage())
					&& (Methode.stockageAccess(utilisateur,
							registreStock.getStockage().getIdStockage()))) {

				LineRegistreStrock line = new LineRegistreStrock();
				line.setAjustementAvenir(registreStock.getAjustementAvenir());
				line.setAjustementDisponible(registreStock.getAjustementDisponibe());
				line.setAjustementReel(registreStock.getAjustementReel());

				line.setStockAvenir(registreStock.getStockAvenir());
				line.setStockDisponible(registreStock.getStockDisponible());
				line.setStockReel(registreStock.getStockReel());

				line.setContext(registreStock.getContext());
				line.setDate(registreStock.getDate());
				line.setId(registreStock.getId());
				line.setIdDocument(registreStock.getIdDocument());

				line.setNomStockage(registreStock.getStockage().getNom());

				line.setNomUtilisateur(
						(registreStock.getUtilisateur() != null) ? registreStock.getUtilisateur().getNom() : null);

				line.setNomProduit(registreStock.getProduit().getNom());
				line.setSkuProduit(registreStock.getProduit().getSku());
				line.setNomPackPrincipal(registreStock.getProduit().getPacks().get(0).getNom());

				for (Pack pack : registreStock.getProduit().getPacks()) {
					if (Statut.getPackprincipal().equals(pack.getStatut())) {
						line.setNomPackPrincipal(pack.getNom());
						line.setDiviseur(pack.getNombreUnite());
					}
				}
				lineRegistreStrocks.add(line);
			}
		}

		return lineRegistreStrocks;
	}

	public void updateRegistreStock(List<ProduitStockage> produitStockagesBeforeAction, String Context,
			int idUtilisateur, String idDocument) {
		Utilisateur user = null;
		if (utilisateurRepository.findById(idUtilisateur).isPresent()) {
			user = utilisateurRepository.findById(idUtilisateur).get();
		}

		for (ProduitStockage newProdStock : produitStockageRepository.findAll()) {
			Boolean isNewProduitStockageEntity = true;
			RegistreStock registre = new RegistreStock();
			registre.setStockAvenir(newProdStock.getStockAvenir());
			registre.setStockDisponible(newProdStock.getStockDisponible());
			registre.setStockReel(newProdStock.getStockReel());

			registre.setUtilisateur(user);
			registre.setContext(Context);
			registre.setDate(new Date());
			registre.setIdDocument(idDocument);
			registre.setProduit(newProdStock.getProduit());
			registre.setStockage(newProdStock.getStockage());

			for (ProduitStockage oldProdStock : produitStockagesBeforeAction) {
				if (newProdStock.getId() == oldProdStock.getId()
						&& !Data.typeService.equals(oldProdStock.getProduit().getType())) {
					isNewProduitStockageEntity = false;

					registre.setAjustementAvenir(newProdStock.getStockAvenir() - oldProdStock.getStockAvenir());
					registre.setAjustementDisponibe(
							newProdStock.getStockDisponible() - oldProdStock.getStockDisponible());
					registre.setAjustementReel(newProdStock.getStockReel() - oldProdStock.getStockReel());

					if (registre.getAjustementAvenir() != 0 || registre.getAjustementDisponibe() != 0
							|| registre.getAjustementReel() != 0) {
						if (!Etat.getDelete().equals(registre.getProduit().getEtat())) {
							registreStockRepository.save(registre);
						}
					}
				}
			}

			// Dans le cas ou le produit concerner n'est pas encore present dans le stockage
			// concerner
			if (isNewProduitStockageEntity && !Data.typeService.equals(newProdStock.getProduit().getType())) {
				registre.setAjustementAvenir(newProdStock.getStockAvenir());
				registre.setAjustementDisponibe(newProdStock.getStockDisponible());
				registre.setAjustementReel(newProdStock.getStockReel());

				if (!Etat.getDelete().equals(registre.getProduit().getEtat())) {
					registreStockRepository.save(registre);
				}
			}
		}
	}

	public void updateRegistreStock(List<ProduitStockage> produitStockagesBeforeAction, String Context,
			Utilisateur utilisateur, String idDocument) {
		if (utilisateur != null) {
			this.updateRegistreStock(produitStockagesBeforeAction, Context, utilisateur.getId(), idDocument);
		} else {
			this.updateRegistreStock(produitStockagesBeforeAction, Context, 0, idDocument);
		}
	}
}
