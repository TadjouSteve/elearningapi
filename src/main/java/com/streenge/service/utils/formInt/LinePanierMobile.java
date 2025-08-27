package com.streenge.service.utils.formInt;

import com.streenge.service.utils.formOut.FormViewProduit;

public class LinePanierMobile {
	private int				id			= 0;
	private String			idProduit	= "";
	private float			quantite	= 0;
	private float			restant		= 0;
	private float			prix		= 0;
	private String			designation	= "";
	private FormViewProduit	formProduit	= new FormViewProduit();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIdProduit() {
		return idProduit;
	}

	public void setIdProduit(String idProduit) {
		this.idProduit = idProduit;
	}

	public float getQuantite() {
		return quantite;
	}

	public void setQuantite(float quantite) {
		this.quantite = quantite;
	}

	public float getPrix() {
		return prix;
	}

	public void setPrix(float prix) {
		this.prix = prix;
	}

	public FormViewProduit getFormProduit() {
		return formProduit;
	}

	public void setFormProduit(FormViewProduit formProduit) {
		this.formProduit = formProduit;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public float getRestant() {
		return restant;
	}

	public void setRestant(float restant) {
		this.restant = restant;
	}

}
