package com.streenge.service.adminService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.streenge.model.admin.Stockage;
import com.streenge.repository.adminRepository.StockageRepository;

@Service
public class StockageService {
	@Autowired
	private StockageRepository stockageRepository;
	
	public Stockage getStockage(int id) {
		Optional<Stockage> optionalstock=stockageRepository.findById(id);
		
		if (optionalstock.isPresent()) {
			return optionalstock.get();
		}else {
			return stockageRepository.findById(1).get();
		}
	}
}
