package com.streenge.repository.venteRepo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streenge.model.vente.Devis;

@Repository
public interface DevisRepository extends CrudRepository<Devis, String> {
	public List<Devis> findAllByOrderByIdDevisDesc();

	public List<Devis> findByClientNomStartingWithOrderByIdDevisDesc(String prefixNomClient);
	
	public List<Devis> findByStockageNomStartingWithOrderByIdDevisDesc(String prefixStockageNom);

	public List<Devis> findByIdDevisStartingWithOrUtilisateurNomStartingWithOrStatutStartingWithOrderByIdDevisDesc(
			String prefixIDDevis, String prefixNomUser, String statut);
}
