package com.streenge.repository.venteRepo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streenge.model.vente.BonLivraison;

@Repository
public interface BonLivraisonRepository extends CrudRepository<BonLivraison, String> {
	public List<BonLivraison> findAllByOrderByIdBonLivraisonDesc();
	
	public List<BonLivraison> findByBonCommandeClientNomStartingWithOrderByIdBonLivraisonDesc(
			String prefixNomClient);
	
	public List<BonLivraison> findByBonCommandeStockageNomStartingWithOrderByIdBonLivraisonDesc(String prefixStockageNom);
	
	public List<BonLivraison> findByIdBonLivraisonStartingWithOrUtilisateurNomStartingWithOrBonCommandeIdBonCommandeStartingWithOrStatutStartingWithOrderByIdBonLivraisonDesc(
			String prefixIdBonCommande, String prefixNomUser,String prefixidBonCommande, String statut);
}
