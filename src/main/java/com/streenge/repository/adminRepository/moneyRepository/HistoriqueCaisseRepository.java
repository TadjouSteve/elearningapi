package com.streenge.repository.adminRepository.moneyRepository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streenge.model.admin.money.HistoriqueCaisse;

@Repository
public interface HistoriqueCaisseRepository extends CrudRepository<HistoriqueCaisse, Integer> {
	public List<HistoriqueCaisse> findByCaisseIdOrderByIdDesc(
			int idCaisse);
}
