package com.streenge.repository.venteRepo.factureRepo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.streenge.model.vente.facture.Paiement;

@Repository
public interface PaiementRepository extends CrudRepository<Paiement, Integer> {

	@Modifying
	@Transactional
	@Query("DELETE FROM Paiement p WHERE p.id = ?1 ")
	void deletePaiement(int id);
}
