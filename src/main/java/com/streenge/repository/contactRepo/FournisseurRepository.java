package com.streenge.repository.contactRepo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streenge.model.contact.Fournisseur;

@Repository
public interface FournisseurRepository extends CrudRepository<Fournisseur, String> {
	public List<Fournisseur> findAllByOrderByNomAsc();
	public List<Fournisseur> findByNom(String nom);
	public List<Fournisseur> findByIdFournisseur(String idFournisseur);
	public List<Fournisseur> findByNomStartingWithOrderByNomAsc(String prefix);
}
