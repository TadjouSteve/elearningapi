package com.streenge.repository.adminRepository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.streenge.model.admin.UtilisateurStockage;

@Repository
public interface UtilisateurStockageRepository extends CrudRepository<UtilisateurStockage, Integer> {
	
	List<UtilisateurStockage> findByUtilisateurId(int idUser);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM UtilisateurStockage l WHERE l.id = ?1 ")
	void deleteUtilisateurStockage(int id);
}
