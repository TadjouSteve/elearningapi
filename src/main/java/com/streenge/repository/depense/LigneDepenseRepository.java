package com.streenge.repository.depense;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.streenge.model.depense.LigneDepense;

@Repository
public interface LigneDepenseRepository extends CrudRepository<LigneDepense, Integer> {
	@Modifying
	@Transactional
	@Query("DELETE FROM LigneDepense l WHERE l.id = ?1 ")
	void deleteLigneDepense(int id);
}
