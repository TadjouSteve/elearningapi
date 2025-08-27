package com.streenge.repository.adminRepository.moneyRepository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streenge.model.admin.money.Caisse;

@Repository
public interface CaisseRepository extends CrudRepository<Caisse, Integer> {

}
