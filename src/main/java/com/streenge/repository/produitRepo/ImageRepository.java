package com.streenge.repository.produitRepo;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.streenge.model.produit.Image;

@Repository
public interface ImageRepository extends CrudRepository<Image, Integer> {
	Optional<Image> findByNom(String fileName);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM Image l WHERE l.id = ?1 ")
	void deleteImaget(int id);
}
