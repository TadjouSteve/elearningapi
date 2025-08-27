package com.streenge.repository.produitRepo.medicamentRepo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.streenge.model.produit.medicament.ComposantMedicament;

@Repository
public interface ComposantMedicamentRepository extends CrudRepository<ComposantMedicament, Integer> {
	@Modifying
	@Transactional
	@Query("DELETE FROM ComposantMedicament l WHERE l.id = ?1 ")
	void deleteComposantMedicament(int id);
}
