package com.streenge.repository.venteRepo.factureRepo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streenge.model.vente.facture.MethodePaiement;


@Repository
public interface MethodePaiementRepository extends CrudRepository<MethodePaiement, Integer> {
	public List<MethodePaiement> findByNom(String nom);
}
