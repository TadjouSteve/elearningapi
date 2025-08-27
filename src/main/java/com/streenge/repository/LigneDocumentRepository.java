package com.streenge.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.streenge.model.LigneDocument;

@Repository
public interface LigneDocumentRepository extends CrudRepository<LigneDocument, Integer> {
	@Modifying
	@Transactional
	@Query("DELETE FROM LigneDocument l WHERE l.id = ?1 ")
	void deleteLigneDocument(int id);
}
