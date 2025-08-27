package com.streenge.service.utils.formOut;

import java.util.ArrayList;
import java.util.List;

import com.streenge.model.admin.Stockage;
import com.streenge.model.admin.Taxe;
import com.streenge.model.admin.money.Caisse;
import com.streenge.model.produit.Categorie;
import com.streenge.model.produit.Groupe;
import com.streenge.model.produit.medicament.Composant;

public class MetaDataVente {
	private List<String>	nomClients		= new ArrayList<String>();
	private List<String>	nomFournisseurs	= new ArrayList<String>();
	private List<Categorie>	categories		= new ArrayList<Categorie>();
	private List<Stockage>	Stockages		= new ArrayList<Stockage>();
	private List<Taxe>		taxes			= new ArrayList<Taxe>();
	private List<Caisse>	caisses			= new ArrayList<Caisse>();
	private List<Groupe>	groupes			= new ArrayList<Groupe>();
	private List<Composant>	composants		= new ArrayList<Composant>();

	public List<String> getNomClients() {
		return nomClients;
	}

	public void setNomClients(List<String> nomClients) {
		this.nomClients = nomClients;
	}

	public List<String> getNomFournisseurs() {
		return nomFournisseurs;
	}

	public void setNomFournisseurs(List<String> nomFournisseurs) {
		this.nomFournisseurs = nomFournisseurs;
	}

	public List<Taxe> getTaxes() {
		return taxes;
	}

	public void setTaxes(List<Taxe> taxes) {
		this.taxes = taxes;
	}

	public List<Stockage> getStockages() {
		return Stockages;
	}

	public void setStockages(List<Stockage> stockages) {
		Stockages = stockages;
	}

	public List<Caisse> getCaisses() {
		return caisses;
	}

	public void setCaisses(List<Caisse> caisses) {
		this.caisses = caisses;
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
