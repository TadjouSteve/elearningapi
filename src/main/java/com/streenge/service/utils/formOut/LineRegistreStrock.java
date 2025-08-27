package com.streenge.service.utils.formOut;

import java.util.Date;

public class LineRegistreStrock {

	private int id = 0;

	private float	ajustementAvenir;
	private float	ajustementDisponible;
	private float	ajustementReel;

	private String	context;
	private Date	date;
	private String	idDocument;

	private float	stockAvenir;
	private float	stockDisponible;
	private float	stockReel;

	private String nomUtilisateur;

	private String	nomProduit;
	private String	skuProduit;
	private float	diviseur	= 1;

	public String getNomPackPrincipal() {
		return nomPackPrincipal;
	}

	public void setNomPackPrincipal(String nomPackPrincipal) {
		this.nomPackPrincipal = nomPackPrincipal;
	}

	private String nomPackPrincipal = "";

	private String nomStockage;

	public LineRegistreStrock() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getAjustementAvenir() {
		return ajustementAvenir;
	}

	public void setAjustementAvenir(float ajustementAvenir) {
		this.ajustementAvenir = ajustementAvenir;
	}

	public float getAjustementDisponible() {
		return ajustementDisponible;
	}

	public void setAjustementDisponible(float ajustementDisponible) {
		this.ajustementDisponible = ajustementDisponible;
	}

	public float getAjustementReel() {
		return ajustementReel;
	}

	public void setAjustementReel(float ajustementReel) {
		this.ajustementReel = ajustementReel;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getIdDocument() {
		return idDocument;
	}

	public void setIdDocument(String idDocument) {
		this.idDocument = idDocument;
	}

	public float getStockAvenir() {
		return stockAvenir;
	}

	public void setStockAvenir(float stockAvenir) {
		this.stockAvenir = stockAvenir;
	}

	public float getStockDisponible() {
		return stockDisponible;
	}

	public void setStockDisponible(float stockDisponible) {
		this.stockDisponible = stockDisponible;
	}

	public float getStockReel() {
		return stockReel;
	}

	public void setStockReel(float stockReel) {
		this.stockReel = stockReel;
	}

	public String getNomUtilisateur() {
		return nomUtilisateur;
	}

	public void setNomUtilisateur(String nomUtilisateur) {
		this.nomUtilisateur = nomUtilisateur;
	}

	public String getNomProduit() {
		return nomProduit;
	}

	public void setNomProduit(String nomProduit) {
		this.nomProduit = nomProduit;
	}

	public String getSkuProduit() {
		return skuProduit;
	}

	public void setSkuProduit(String skuProduit) {
		this.skuProduit = skuProduit;
	}

	public String getNomStockage() {
		return nomStockage;
	}

	public void setNomStockage(String nomStockage) {
		this.nomStockage = nomStockage;
	}

	public float getDiviseur() {
		return diviseur;
	}

	public void setDiviseur(float diviseur) {
		this.diviseur = diviseur;
	}

}
