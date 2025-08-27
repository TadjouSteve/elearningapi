package com.streenge.repository.adminRepository.moneyRepository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streenge.model.admin.money.HistoriqueSoldeClient;

@Repository
public interface HistoriqueSoldeClientRepository extends CrudRepository<HistoriqueSoldeClient, Integer> {
	public List<HistoriqueSoldeClient> findByClientIdClientOrderByIdDesc(
			String IdClient);
}
