package com.streenge.repository.produitRepo.medicamentRepo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streenge.model.produit.medicament.Composant;

@Repository
public interface ComposantRepository extends CrudRepository<Composant, Integer> {
	public List<Composant> findByNom(String nom);
	public List<Composant> findAllByOrderByNomAsc();
}
