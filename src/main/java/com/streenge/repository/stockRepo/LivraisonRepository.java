package com.streenge.repository.stockRepo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.streenge.model.stock.Livraison;

public interface LivraisonRepository extends CrudRepository<Livraison, String> {
	public List<Livraison> findAllByOrderByIdLivraisonDesc();
	
	public List<Livraison> findByOrdreAchatFournisseurNomStartingWithOrderByIdLivraisonDesc(
			String prefixNomfournisseur);
}
