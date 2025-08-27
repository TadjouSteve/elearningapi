package com.streenge.repository.produitRepo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streenge.model.produit.ProduitStockage;

@Repository
public interface ProduitStockageRepository extends CrudRepository<ProduitStockage, Integer> {

}
