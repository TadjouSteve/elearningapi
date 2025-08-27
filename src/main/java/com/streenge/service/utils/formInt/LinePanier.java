package com.streenge.service.utils.formInt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.streenge.model.admin.Taxe;

public class LinePanier {

	private int		indexPack	= -1;
	private int		idPack		= 0;
	private String	designation	= "";
	private float	prix		= 0;
	private float	quantite	= 0;
	private float	remise		= 0;
	private Taxe	taxe		= null;
	private float	prixMin		= 0;
	private float	prixAchat	= 0;
	private float	coutUMP		= 0;
	private float	restant		= 0;
	private float	quantiteMin	= -1;
	private float	quantiteMax	= -1;
	
	private String nomCategorie=null;
	
	private Boolean isDelete=false;

	// specifiquement pour les inventaire
	private float	pointCommande	= 0;
	private String	emplacement		= "";
	private String	sku				= "";

	// specifiquement pour les rapport par mail
	@JsonIgnore
	private String idProduit = "";
	@JsonIgnore
	private float  diviseur	= 1;
	
	// specifiquement pour les fiche de depense
	private int idPointDepense=0;

	public LinePanier() {
		// TODO Auto-generated constructor stub
	}

	public int getIndexPack() {
		return indexPack;
	}

	public void setIndexPack(int indexPack) {
		this.indexPack = indexPack;
	}

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

	public float getPrix() {
		return prix;
	}

	public void setPrix(float prix) {
		this.prix = prix;
	}

	public float getQuantite() {
		return quantite;
	}

	public void setQuantite(float quantite) {
		this.quantite = quantite;
	}

	public float getRemise() {
		return remise;
	}

	public void setRemise(float remise) {
		this.remise = remise;
	}

	public Taxe getTaxe() {
		return taxe;
	}

	public void setTaxe(Taxe taxe) {
		this.taxe = taxe;
	}

	public float getPrixMin() {
		return prixMin;
	}

	public void setPrixMin(float prixMin) {
		this.prixMin = prixMin;
	}

	public float getQuantiteMin() {
		return quantiteMin;
	}

	public void setQuantiteMin(float quantiteMin) {
		this.quantiteMin = quantiteMin;
	}

	public float getQuantiteMax() {
		return quantiteMax;
	}

	public void setQuantiteMax(float quantiteMax) {
		this.quantiteMax = quantiteMax;
	}

	public float getPrixAchat() {
		return prixAchat;
	}

	public void setPrixAchat(float prixAchat) {
		this.prixAchat = prixAchat;
	}

	public float getRestant() {
		return restant;
	}

	public void setRestant(float restant) {
		this.restant = restant;
	}

	public float getCoutUMP() {
		return coutUMP;
	}

	public void setCoutUMP(float coutUMP) {
		this.coutUMP = coutUMP;
	}

	public float getPointCommande() {
		return pointCommande;
	}

	public void setPointCommande(float pointCommande) {
		this.pointCommande = pointCommande;
	}

	public String getEmplacement() {
		return emplacement;
	}

	public void setEmplacement(String emplacement) {
		this.emplacement = emplacement;
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

	public float getDiviseur() {
		return diviseur;
	}

	public void setDiviseur(float diviseur) {
		this.diviseur = diviseur;
	}

	public int getIdPointDepense() {
		return idPointDepense;
	}

	public void setIdPointDepense(int idPointDepense) {
		this.idPointDepense = idPointDepense;
	}

	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}

	

	public String getNomCategorie() {
		return nomCategorie;
	}

	public void setNomCategorie(String nomCategorie) {
		this.nomCategorie = nomCategorie;
	}

}
