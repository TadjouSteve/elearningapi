package com.streenge.repository.stockRepo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streenge.model.stock.MouvementStock;

@Repository
public interface MouvementStockRepository extends CrudRepository<MouvementStock, String> {
	public List<MouvementStock> findAllByIdMouvementStockStartingWithOrderByIdMouvementStockDesc(String beginId);//IdMouvementStockStartingWith
}
