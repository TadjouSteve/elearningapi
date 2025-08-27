package com.streenge.repository.produitRepo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streenge.model.produit.Categorie;

@Repository
public interface CategorieRepository extends CrudRepository<Categorie, String> {

	public List<Categorie> findByNom(String nom);
	public List<Categorie> findAllByOrderByNomAsc();
	
}
