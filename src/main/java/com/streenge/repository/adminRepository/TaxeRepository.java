package com.streenge.repository.adminRepository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streenge.model.admin.Taxe;

@Repository
public interface TaxeRepository extends CrudRepository<Taxe, Integer> {
	public List<Taxe> findAllByOrderByNomAsc();
}
