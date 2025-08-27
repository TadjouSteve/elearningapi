package com.streenge.repository.depense;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streenge.model.depense.PointDepense;

@Repository
public interface PointDepenseRepository extends CrudRepository<PointDepense, Integer> {
	public List<PointDepense> findAllByOrderByNomAsc();
	public List<PointDepense> findByNom(String nom);
	
	public List<PointDepense> findByReference(String reference);
	public List<PointDepense> findByNomStartingWithOrReferenceStartingWithOrderByNomAsc(String prefix,String patternReference);
	
	public List<PointDepense> findByNomContainingOrReferenceContainingOrderByNomAsc(String prefix,String patternReference);
}
