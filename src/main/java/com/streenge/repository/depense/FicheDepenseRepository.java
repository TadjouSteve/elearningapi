package com.streenge.repository.depense;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streenge.model.depense.FicheDepense;

@Repository
public interface FicheDepenseRepository extends CrudRepository<FicheDepense, String> {
	public List<FicheDepense> findAllByOrderByIdFicheDepenseAsc();
	
	public List<FicheDepense> findByStockageNomStartingWithOrderByIdFicheDepenseDesc(String prefixStockageNom);

	public List<FicheDepense> findByIdFicheDepenseStartingWithOrUtilisateurNomStartingWithOrStatutStartingWithOrderByIdFicheDepenseDesc(
			String prefixIdBonCommande, String prefixNomUser, String statut);
	
	public List<FicheDepense> findAllByDateBetween(Date dateDebut,Date dateFin);
}
