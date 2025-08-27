package com.streenge.service.produitService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.streenge.model.produit.Groupe;
import com.streenge.model.produit.GroupeProduit;
import com.streenge.model.produit.Produit;
import com.streenge.repository.produitRepo.GroupeProduitRepository;
import com.streenge.repository.produitRepo.GroupeRepository;
import com.streenge.repository.produitRepo.ProduitRepository;
import com.streenge.service.utils.erroclass.ErrorAPI;
import com.streenge.service.utils.erroclass.StreengeException;
import com.streenge.service.utils.formData.FormGroupe;
import com.streenge.service.utils.formOut.FormViewProduit;
import com.streenge.service.utils.streengeData.Etat;
import com.streenge.service.utils.streengeFunction.Methode;

@Service
public class GroupeService {
	@Autowired
	private GroupeRepository groupeRepository;
	
	@Autowired
	private GroupeProduitRepository groupeProduitRepository;
	
	@Autowired
	private ProduitRepository produitRepository;
 
	
	@Autowired
	ProduitService produitService;
	
	public Groupe createOrUpdate(Groupe groupe) {
		
		if (groupe!=null) {
			controlForm(groupe);
			groupe.setNom(Methode.upperCaseFirst(groupe.getNom()));
			groupe = groupeRepository.save(groupe);
		}
		return groupe;
	}
	
	public void delete(Groupe groupe) {
		if (groupe!=null) {
			delete(groupe.getId());
		}
	}
	
	public void delete(int idGroupe) {
		if (groupeRepository.findById(idGroupe).isPresent()) {
			Groupe groupe = groupeRepository.findById(idGroupe).get();
			List<GroupeProduit> listGroupProduit = groupe.getGroupeProduits();
			
			for (GroupeProduit groupeProduit : listGroupProduit) {
				groupeProduitRepository.deleteGroupeProduit(groupeProduit.getId());
			}
			
			groupeRepository.deleteById(idGroupe);
			
		}
	}
	
	private void controlForm(Groupe groupe) {
		if (groupe.getNom() == null || groupe.getNom().isBlank() || groupe.getNom().length() < 3) {
			throw new StreengeException(new ErrorAPI(
					"Le Non du Groupe ne doit pas êre vide, ou contenir moins de trois(03) Caractères"));
		}

		if (groupe.getNom() == null || groupe.getNom().isBlank() || groupe.getNom().length() > 50) {
			throw new StreengeException(new ErrorAPI("Le Non du Groupe est trop long, maximun 50 Caracteres"));
		}

		if (!groupeRepository.findByNom(groupe.getNom()).isEmpty()) {
			Groupe groupe2 = groupeRepository.findByNom(groupe.getNom()).get(0);
			if (groupe2.getNom().equals(groupe.getNom()) && groupe2.getId() != groupe.getId()) {
				throw new StreengeException(new ErrorAPI("Un autre Groupe existe deja avec ce nom...!"));
			}
		}
	}
	
	public void AddProduitToGroupe(List<FormGroupe> formGroupes,Produit produit) {
		
		if (formGroupes!=null && produit!=null && produitRepository.existsById(produit.getIdProduit())) {
			produit=produitRepository.findById(produit.getIdProduit()).get();
			List<GroupeProduit> groupeProduits=produit.getGroupeProduits();
			if (groupeProduits!=null) {
				for (GroupeProduit groupeProduit : groupeProduits) {
					groupeProduitRepository.deleteGroupeProduit(groupeProduit.getId());
				}
			}
			
			for (FormGroupe formGroupe : formGroupes) {
				if (groupeRepository.existsById(formGroupe.getId())) {
					GroupeProduit groupeProduit =new GroupeProduit();
					groupeProduit.setGroupe(groupeRepository.findById(formGroupe.getId()).get());
					groupeProduit.setProduit(produit);
					groupeProduit.setPriorite(formGroupe.getPriorite());
					groupeProduitRepository.save(groupeProduit);
				}
			}
			
		}
	}
	
	public List<FormGroupe> getFormGroupesForProduit(String idProduit) {
		List<FormGroupe> formGroupes=new ArrayList<>();
		if (produitRepository.existsById(idProduit) && produitRepository.findById(idProduit).get().getGroupeProduits()!=null) {
			for (GroupeProduit groupeProduit : produitRepository.findById(idProduit).get().getGroupeProduits()) {
				FormGroupe formGroupe=new FormGroupe();
				formGroupe.setId(groupeProduit.getGroupe().getId());
				formGroupe.setDescription(groupeProduit.getGroupe().getDescription());
				formGroupe.setNom(groupeProduit.getGroupe().getNom());
				formGroupe.setPriorite(groupeProduit.getPriorite());
				formGroupes.add(formGroupe);
			}
		}
		
		return formGroupes;
	}
	
	public FormGroupe getFormGroupeWithListFormProduitsForMobile(int idGroupe) {
		FormGroupe formGroupe=new FormGroupe();
		if (groupeRepository.existsById(idGroupe)) {
			Groupe groupe = groupeRepository.findById(idGroupe).get();
			formGroupe.setId(groupe.getId());
			formGroupe.setDescription(groupe.getDescription());
			formGroupe.setNom(groupe.getNom());
			formGroupe.setPriorite(groupe.getPriorite());
			
			for (GroupeProduit groupeProduit : groupeProduitRepository.findAllByGroupeIdOrderByPrioriteAsc(idGroupe)) {
				if (!Etat.getDelete().equals(groupeProduit.getProduit().getEtat())) {
					FormViewProduit formViewProduit = produitService.generateSimpleFormViewProduitForMobile(groupeProduit.getProduit().getIdProduit());
					formGroupe.getFormProduits().add(formViewProduit);
				}
			}
		}
		
		return formGroupe;
	}
}
