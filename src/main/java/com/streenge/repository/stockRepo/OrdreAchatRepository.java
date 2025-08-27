package com.streenge.repository.stockRepo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streenge.model.stock.OrdreAchat;

@Repository
public interface OrdreAchatRepository extends CrudRepository<OrdreAchat, String> {
	public List<OrdreAchat> findAllByOrderByIdOrdreAchatDesc();
	
	public List<OrdreAchat> findByFournisseurNomStartingWithOrderByIdOrdreAchatDesc(
			String prefixNomfournisseur);	
}
