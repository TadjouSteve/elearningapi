package com.streenge.repository.produitRepo;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.streenge.model.produit.GroupeProduit;

@Repository
public interface GroupeProduitRepository extends CrudRepository<GroupeProduit, Integer> {

	public List<GroupeProduit> findAllByOrderByPrioriteAsc();

	public List<GroupeProduit> findAllByGroupeIdOrderByPrioriteAsc(int idGroupe);

	public List<GroupeProduit> findAllByProduitIdProduitOrderByPrioriteAsc(String idProduit);

	@Modifying
	@Transactional
	@Query("DELETE FROM GroupeProduit l WHERE l.id = ?1 ")
	void deleteGroupeProduit(int id);
}
