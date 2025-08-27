package com.streenge.repository.produitRepo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streenge.model.produit.Groupe;


@Repository
public interface GroupeRepository extends CrudRepository<Groupe, Integer> {
	public List<Groupe> findByNom(String nom);
	public List<Groupe> findAllByOrderByNomAsc();
	public List<Groupe> findAllByOrderByPrioriteAsc();
}
