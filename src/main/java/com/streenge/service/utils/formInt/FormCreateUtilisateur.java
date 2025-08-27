package com.streenge.service.utils.formInt;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.streenge.model.admin.Stockage;
import com.streenge.model.admin.Utilisateur;

public class FormCreateUtilisateur {
	
	private Utilisateur		utilisateur	= new Utilisateur();
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})	
	private List<Stockage>	stockages	= new ArrayList<Stockage>();

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public List<Stockage> getStockages() {
		return stockages;
	}

	public void setStockages(List<Stockage> stockages) {
		this.stockages = stockages;
	}

}
