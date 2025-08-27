package com.streenge.repository.venteRepo.factureRepo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.streenge.model.vente.facture.LigneFacture;

@Repository
public interface LigneFactureRepository extends CrudRepository<LigneFacture, Integer> {
	
	public List<LigneFacture> findAllByOrderById();
	
	public List<LigneFacture> findByPackProduitIdProduitOrderById(String idProduit);
	
	public List<LigneFacture> findByFactureClientIdClientOrderById(String idClient);
	
	public List<LigneFacture> findByFactureIdFactureOrderById(String idFacture);
	
	public List<LigneFacture> findByPackProduitIdProduitAndFactureDateCreationBetweenOrderById(String idProduit,Date dateDebut,Date dateFin);//PackProduitIdProduitAndFactureDateCreationBetween
	
	//public List<Facture> findByIdFactureStartingWithOrBonCommandeIdBonCommandeStartingWithOrStatutStartingWithOrderByIdFactureDesc(
	//String prefixIDFacture, String prefixIdBonCommde, String statut);
	
	//public List<Facture> findAllByDateCreationBetween(Date dateDebut,Date dateFin);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM LigneFacture p WHERE p.id = ?1 ")
	void deleteLigneFacture(int id);
}
