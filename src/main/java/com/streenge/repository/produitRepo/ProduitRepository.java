package com.streenge.repository.produitRepo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streenge.model.produit.Produit;

@Repository
public interface ProduitRepository extends CrudRepository<Produit, String> {

	// public findAllByOrderByIdAsc();
	public List<Produit> findAllByOrderByNomAsc();

	public List<Produit> findByNom(String nom);

	public List<Produit> findBySku(String sku);

	public List<Produit> findByCodeBarre(String codeBarre);//OrCategorieNomStartingWhith
	public List<Produit> findByNomStartingWithOrSkuStartingWithOrderByNomAsc(String prefix,String patternSku);
	public List<Produit> findByCategorieNomStartingWithOrderByNomAsc(String patterNomCategorie);
	
	public List<Produit> findByNomStartingWithOrSkuStartingWithOrCodeBarreStartingWithOrderByNomAsc(String prefix,String patternSku,String codeBarre);
	
	public List<Produit> findByNomContainingOrSkuContainingOrCodeBarreContaining(String patterNom, String patternSku, String patterCodeBarre);
}
