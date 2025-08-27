package com.streenge.repository.adminRepository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streenge.model.admin.Entreprise;

@Repository
public interface EntrepriseRepository extends CrudRepository<Entreprise, Integer> {

}
