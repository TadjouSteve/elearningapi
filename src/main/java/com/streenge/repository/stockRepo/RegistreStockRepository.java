package com.streenge.repository.stockRepo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streenge.model.stock.RegistreStock;

@Repository
public interface RegistreStockRepository extends CrudRepository<RegistreStock, Integer> {
	public List<RegistreStock> findAllByOrderByIdDesc();
	public List<RegistreStock> findAllByProduitNomStartingWithOrUtilisateurNomStartingWithOrUtilisateurNomAffichageStartingWithOrStockageNomStartingWithOrIdDocumentStartingWithOrderByIdDesc(String filter1,String filter2,String filter3,String filter4,String filter5);
	public List<RegistreStock> findAllByProduitNomContainingOrProduitSkuContainingOrderByIdDesc(String filter,String filter2);
}
