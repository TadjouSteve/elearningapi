package com.streenge.service.utils.formOut;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.streenge.model.admin.Stockage;
import com.streenge.model.admin.money.HistoriqueCaisse;

public class FormCaisse {
	private int			id;
	private float		solde;
	private String		statut;
	private String		description		= "";
	private FormTable	mouvementCaisse	= new FormTable();

	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private Stockage				stockage			= new Stockage();
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private List<HistoriqueCaisse>	historiqueCaisses	= new ArrayList<>();

	private int	nombreFacture					= 0;
	private int	nombreFacturePaye				= 0;
	private int	nombreFactureNonPaye			= 0;
	private int	nombreFacturePartiellementPaye	= 0;
	private int	nombreFactureAnnule				= 0;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Stockage getStockage() {
		return stockage;
	}

	public void setStockage(Stockage stockage) {
		this.stockage = stockage;
	}

	public float getSolde() {
		return solde;
	}

	public void setSolde(float solde) {
		this.solde = solde;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public List<HistoriqueCaisse> getHistoriqueCaisses() {
		return historiqueCaisses;
	}

	public void setHistoriqueCaisses(List<HistoriqueCaisse> historiqueCaisses) {
		this.historiqueCaisses = historiqueCaisses;
	}

	public FormTable getMouvementCaisse() {
		return mouvementCaisse;
	}

	public void setMouvementCaisse(FormTable mouvementCaisse) {
		this.mouvementCaisse = mouvementCaisse;
	}

	public int getNombreFacture() {
		return nombreFacture;
	}

	public void setNombreFacture(int nombreFacture) {
		this.nombreFacture = nombreFacture;
	}

	public int getNombreFacturePaye() {
		return nombreFacturePaye;
	}

	public void setNombreFacturePaye(int nombreFacturePaye) {
		this.nombreFacturePaye = nombreFacturePaye;
	}

	public int getNombreFactureNonPaye() {
		return nombreFactureNonPaye;
	}

	public void setNombreFactureNonPaye(int nombreFactureNonPaye) {
		this.nombreFactureNonPaye = nombreFactureNonPaye;
	}

	public int getNombreFacturePartiellementPaye() {
		return nombreFacturePartiellementPaye;
	}

	public void setNombreFacturePartiellementPaye(int nombreFacturePartiellementPaye) {
		this.nombreFacturePartiellementPaye = nombreFacturePartiellementPaye;
	}

	public int getNombreFactureAnnule() {
		return nombreFactureAnnule;
	}

	public void setNombreFactureAnnule(int nombreFactureAnnule) {
		this.nombreFactureAnnule = nombreFactureAnnule;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
