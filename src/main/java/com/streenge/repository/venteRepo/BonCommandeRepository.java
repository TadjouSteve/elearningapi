package com.streenge.repository.venteRepo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streenge.model.vente.BonCommande;

@Repository
public interface BonCommandeRepository extends CrudRepository<BonCommande, String> {
	public List<BonCommande> findAllByOrderByIdBonCommandeDesc();

	public List<BonCommande> findByClientNomStartingWithOrderByIdBonCommandeDesc(String prefixNomClient);

	public List<BonCommande> findByStockageNomStartingWithOrderByIdBonCommandeDesc(String prefixStockageNom);

	public List<BonCommande> findByIdBonCommandeStartingWithOrUtilisateurNomStartingWithOrStatutStartingWithOrderByIdBonCommandeDesc(
			String prefixIdBonCommande, String prefixNomUser, String statut);
}
