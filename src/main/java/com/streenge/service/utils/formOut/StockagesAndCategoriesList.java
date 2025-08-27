package com.streenge.service.utils.formOut;

import java.util.ArrayList;
import java.util.List;

import com.streenge.model.admin.Stockage;
import com.streenge.model.produit.Categorie;
import com.streenge.model.produit.Groupe;
import com.streenge.model.produit.medicament.Composant;

public class StockagesAndCategoriesList {
	private List<Stockage>	stockages;
	private List<Categorie>	categories;
	// private String nom="lover steve";
	private List<Groupe>	groupes		= new ArrayList<Groupe>();
	private List<Composant>	composants	= new ArrayList<Composant>();

	public StockagesAndCategoriesList() {

	}

	public List<Stockage> getStockages() {
		return stockages;
	}

	public void setStockages(List<Stockage> stockages) {
		this.stockages = stockages;
	}

	public List<Categorie> getCategories() {
		return categories;
	}

	public void setCategories(List<Categorie> categories) {
		this.categories = categories;
	}

	public List<Groupe> getGroupes() {
		return groupes;
	}

	public void setGroupes(List<Groupe> groupes) {
		this.groupes = groupes;
	}

	public List<Composant> getComposants() {
		return composants;
	}

	public void setComposants(List<Composant> composants) {
		this.composants = composants;
	}

}
