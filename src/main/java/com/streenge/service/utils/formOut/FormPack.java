package com.streenge.service.utils.formOut;

public class FormPack {
	private int		idPack		= 0;
	private String	nom			= "";	// principallement utiliser dans la vue d'un produit
	private float	nombreUnite	= 0;	// principallement utiliser dans la vue d'un produit
	private String	designation	= "";
	private String	sku			= "";
	private String	type		= "";

	private String	idProduit	= "";
	private boolean	isPrincipal	= false;

	private String	etat	= "";
	private String	statut	= "";

	private float	prixVente		= 0;
	private float	prixVenteMin	= 0;
	private float	prixAchat		= 0;

	private float coutUMP = 0;

	private float	reel		= 0;
	private float	disponible	= 0;
	private float	avenir		= 0;

	public int getIdPack() {
		return idPack;
	}

	public void setIdPack(int idPack) {
		this.idPack = idPack;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	public float getPrixVente() {
		return prixVente;
	}

	public void setPrixVente(float prixVente) {
		this.prixVente = prixVente;
	}

	public float getPrixAchat() {
		return prixAchat;
	}

	public void setPrixAchat(float prixAchat) {
		this.prixAchat = prixAchat;
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

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getIdProduit() {
		return idProduit;
	}

	public void setIdProduit(String idProduit) {
		this.idProduit = idProduit;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public float getNombreUnite() {
		return nombreUnite;
	}

	public void setNombreUnite(float nombreUnite) {
		this.nombreUnite = nombreUnite;
	}

	public boolean isPrincipal() {
		return isPrincipal;
	}

	public void setPrincipal(boolean isPrincipal) {
		this.isPrincipal = isPrincipal;
	}

	public float getPrixVenteMin() {
		return prixVenteMin;
	}

	public void setPrixVenteMin(float prixVenteMin) {
		this.prixVenteMin = prixVenteMin;
	}

	public float getCoutUMP() {
		return coutUMP;
	}

	public void setCoutUMP(float coutUMP) {
		this.coutUMP = coutUMP;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
