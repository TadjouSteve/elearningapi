package com.streenge.service.utils.formOut;

public class FormStock {
	private int		idProduitStockage	= 0;
	
	private String	nom					= "";
	private String	emplacement			= "";
	private float	pointCommande		= 0;
	private float	disponible			= 0;
	private float	reel				= 0;
	private float	ajustement			= 0;
	private float	avenir				= 0;
	private float	reserve				= 0;
	
	private int		idStockage	= 0;

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getEmplacement() {
		return emplacement;
	}

	public void setEmplacement(String emplacement) {
		this.emplacement = emplacement;
	}

	public float getDisponible() {
		return disponible;
	}

	public void setDisponible(float disponible) {
		this.disponible = disponible;
	}

	public float getReel() {
		return reel;
	}

	public void setReel(float reel) {
		this.reel = reel;
	}

	public float getAvenir() {
		return avenir;
	}

	public void setAvenir(float avenir) {
		this.avenir = avenir;
	}

	public float getReserve() {
		return reserve;
	}

	public void setReserve(float reserve) {
		this.reserve = reserve;
	}

	public float getPointCommande() {
		return pointCommande;
	}

	public void setPointCommande(float pointCommande) {
		this.pointCommande = pointCommande;
	}

	public int getIdProduitStockage() {
		return idProduitStockage;
	}

	public void setIdProduitStockage(int idProduitStockage) {
		this.idProduitStockage = idProduitStockage;
	}

	public float getAjustement() {
		return ajustement;
	}

	public void setAjustement(float ajustement) {
		this.ajustement = ajustement;
	}	

	public int getIdStockage() {
		return idStockage;
	}

	public void setIdStockage(int idStockage) {
		this.idStockage = idStockage;
	}

}
