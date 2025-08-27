package com.streenge.repository.adminRepository.moneyRepository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streenge.model.admin.money.MouvementSoldeClient;

@Repository
public interface MouvementSoldeClientRepository extends CrudRepository<MouvementSoldeClient, String> {
	public List<MouvementSoldeClient> findByClientIdClientOrderByDateDesc(
			String IdClient);
}
