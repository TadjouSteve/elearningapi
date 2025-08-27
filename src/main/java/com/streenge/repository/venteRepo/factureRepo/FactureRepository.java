package com.streenge.repository.venteRepo.factureRepo;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streenge.model.vente.facture.Facture;

@Repository
public interface FactureRepository extends CrudRepository<Facture, String> {
	public List<Facture> findAllByOrderByIdFactureDesc();

	public List<Facture> findByClientNomStartingWithOrderByIdFactureDesc(String prefixNomClient);
	
	public List<Facture> findByStockageNomStartingWithOrderByIdFactureDesc(String prefixStockageNom);

	public List<Facture> findByIdFactureStartingWithOrBonCommandeIdBonCommandeStartingWithOrStatutStartingWithOrderByIdFactureDesc(
			String prefixIDFacture, String prefixIdBonCommde, String statut);
	
	public List<Facture> findByIdFactureStartingWithOrBonCommandeIdBonCommandeStartingWithOrUtilisateurNomStartingWithOrStatutStartingWithOrderByIdFactureDesc(
			String prefixIDFacture, String prefixIdBonCommde,String prefixNomUser, String statut);
	
	public List<Facture> findAllByDateCreationBetween(Date dateDebut,Date dateFin);//
}
