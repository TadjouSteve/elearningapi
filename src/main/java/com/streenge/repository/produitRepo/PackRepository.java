package com.streenge.repository.produitRepo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streenge.model.produit.Pack;

@Repository
public interface PackRepository extends CrudRepository<Pack, Integer> {

}
