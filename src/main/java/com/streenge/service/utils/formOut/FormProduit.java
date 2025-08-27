package com.streenge.service.utils.formOut;

public class FormProduit {
	private String	idProduit	= "";
	private String	nom			= "";
	private String	sku			= "";
	private String	type		= "";

	private Boolean isDelete = false;

	private float	reel			= 0;
	private float	disponible		= 0;
	private float	avenir			= 0;
	private float	pointCommande	= 0;

	public String getIdProduit() {
		return idProduit;
	}

	public void setIdProduit(String idProduit) {
		this.idProduit = idProduit;
	}

	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}

	public float getReel() {
		return reel;
	}

	public void setReel(float reel) {
		this.reel = reel;
	}

	public float getDisponible() {
		return disponible;
	}

	public void setDisponible(float disponible) {
		this.disponible = disponible;
	}

	public float getAvenir() {
		return avenir;
	}

	public void setAvenir(float avenir) {
		this.avenir = avenir;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public float getPointCommande() {
		return pointCommande;
	}

	public void setPointCommande(float pointCommande) {
		this.pointCommande = pointCommande;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
