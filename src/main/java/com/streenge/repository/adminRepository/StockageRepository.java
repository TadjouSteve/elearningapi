package com.streenge.repository.adminRepository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streenge.model.admin.Stockage;

@Repository
public interface StockageRepository extends CrudRepository<Stockage, Integer> {
	public List<Stockage> findByNom(String nom);
	
}
