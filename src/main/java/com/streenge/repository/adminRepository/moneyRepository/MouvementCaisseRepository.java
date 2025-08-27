package com.streenge.repository.adminRepository.moneyRepository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streenge.model.admin.money.MouvementCaisse;

@Repository
public interface MouvementCaisseRepository extends CrudRepository<MouvementCaisse, String> {
	public List<MouvementCaisse> findByCaisseIdOrderByDateDesc(
			int IdCaisse);
}
