package com.streenge.service.produitService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.streenge.model.produit.Categorie;
import com.streenge.repository.produitRepo.CategorieRepository;

@Service
public class CategorieServise {
	@Autowired
	private CategorieRepository categorieRepository;
	
	public Categorie getCategorie(int id) {
		Categorie categorie=new Categorie();
		List<Categorie> listCategories=(List<Categorie>) categorieRepository.findAll();
		for (Categorie categorie01 : listCategories) {
			System.out.println("ID ==categorie==:"+categorie01.getId()+" || ==NOm== "+categorie01.getNom());
			if (categorie01.getId()==id) {
				categorie=categorie01;
				System.out.println("INFO Service NOM= "+categorie.getNom()+" || id= "+categorie.getId());
				break;
			}
		}
		
		return categorie; 
	}
}
