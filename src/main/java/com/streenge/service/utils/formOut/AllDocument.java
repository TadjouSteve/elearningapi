package com.streenge.service.utils.formOut;

import java.util.ArrayList;
import java.util.List;

import com.streenge.model.admin.money.HistoriqueSoldeClient;

public class AllDocument {

	private FormTable	devis			= new FormTable();
	private FormTable	bonCommandes	= new FormTable();
	private FormTable	bonLivraisons	= new FormTable();
	private FormTable	factures		= new FormTable();
	private FormTable	ordreAchats		= new FormTable();
	private FormTable	livraisons		= new FormTable();
	private FormTable	entreStocks		= new FormTable();
	private FormTable	sortieStocks	= new FormTable();
	private FormTable	mouvementStocks	= new FormTable();

	private FormTable					mouvementSoldeClients	= new FormTable();
	private List<HistoriqueSoldeClient>	historiqueSoldeClients	= new ArrayList<>();

	public AllDocument() {
		// TODO Auto-generated constructor stub
	}

	public FormTable getDevis() {
		return devis;
	}

	public void setDevis(FormTable devis) {
		this.devis = devis;
	}

	public FormTable getBonCommandes() {
		return bonCommandes;
	}

	public void setBonCommandes(FormTable bonCommandes) {
		this.bonCommandes = bonCommandes;
	}

	public FormTable getBonLivraisons() {
		return bonLivraisons;
	}

	public void setBonLivraisons(FormTable bonLivraisons) {
		this.bonLivraisons = bonLivraisons;
	}

	public FormTable getFactures() {
		return factures;
	}

	public void setFactures(FormTable factures) {
		this.factures = factures;
	}

	public FormTable getOrdreAchats() {
		return ordreAchats;
	}

	public void setOrdreAchats(FormTable ordreAchats) {
		this.ordreAchats = ordreAchats;
	}

	public FormTable getLivraisons() {
		return livraisons;
	}

	public void setLivraisons(FormTable livraisons) {
		this.livraisons = livraisons;
	}

	public FormTable getEntreStocks() {
		return entreStocks;
	}

	public void setEntreStocks(FormTable entreStocks) {
		this.entreStocks = entreStocks;
	}

	public FormTable getSortieStocks() {
		return sortieStocks;
	}

	public void setSortieStocks(FormTable sortieStocks) {
		this.sortieStocks = sortieStocks;
	}

	public FormTable getMouvementStocks() {
		return mouvementStocks;
	}

	public void setMouvementStocks(FormTable mouvementStocks) {
		this.mouvementStocks = mouvementStocks;
	}

	public List<HistoriqueSoldeClient> getHistoriqueSoldeClients() {
		return historiqueSoldeClients;
	}

	public void setHistoriqueSoldeClients(List<HistoriqueSoldeClient> historiqueSoldeClients) {
		this.historiqueSoldeClients = historiqueSoldeClients;
	}

	public FormTable getMouvementSoldeClients() {
		return mouvementSoldeClients;
	}

	public void setMouvementSoldeClients(FormTable mouvementSoldeClients) {
		this.mouvementSoldeClients = mouvementSoldeClients;
	}

}
