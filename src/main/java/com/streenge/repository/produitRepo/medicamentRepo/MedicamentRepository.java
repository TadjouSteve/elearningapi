package com.streenge.repository.produitRepo.medicamentRepo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streenge.model.produit.medicament.Medicament;


@Repository
public interface MedicamentRepository extends CrudRepository<Medicament, Integer> {
	public List<Medicament> findByNomCommercial(String nomCommercial);
	public List<Medicament> findAllByOrderByNomCommercialAsc();
}
