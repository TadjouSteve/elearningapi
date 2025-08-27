package com.streenge.service.utils.formInt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.streenge.model.admin.Utilisateur;
import com.streenge.service.utils.formData.FormGroupe;
import com.streenge.service.utils.formData.formMedicament.FormMedicament;

public class FormCreateProduit {

	private String	codeBarre		= "";
	private float	coutInitial		= 0;
	private String	description		= "";
	private String	emplacement		= "";
	private int		stockageID		= 1;
	private int		categorieID		= 1;
	private String	nom				= "";
	private String	nomUnite		= "";
	private float	pointCommande	= 0;
	private float	prixAchat		= 0;
	private float	prixVente		= 0;
	private float	prixVenteMin	= 0;
	private String	sku				= "";
	private float	stockInitial	= 0;
	private boolean	suivreStock		= true;
	private Date	dateAjout;

	private String type = null;

	private FormMedicament formMedicament = new FormMedicament();
	private List<FormGroupe> formGroupes = new ArrayList<>();

	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private Utilisateur utilisateur = null;

	public float getCoutInitial() {
		return coutInitial;
	}

	public void setCoutInitial(float coutInitial) {
		this.coutInitial = coutInitial;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmplacement() {
		return emplacement;
	}

	public void setEmplacement(String emplacement) {
		this.emplacement = emplacement;
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

	public float getPrixAchat() {
		return prixAchat;
	}

	public void setPrixAchat(float prixAchat) {
		this.prixAchat = prixAchat;
	}

	public float getPrixVente() {
		return prixVente;
	}

	public void setPrixVente(float prixVente) {
		this.prixVente = prixVente;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public boolean isSuivreStock() {
		return suivreStock;
	}

	public void setSuivreStock(boolean suivreStock) {
		this.suivreStock = suivreStock;
	}

	public Date getDateAjout() {
		return dateAjout;
	}

	public void setDateAjout(Date dateAjout) {
		this.dateAjout = dateAjout;
	}

	public int getStockageID() {
		return stockageID;
	}

	public void setStockageID(int stockageID) {
		this.stockageID = stockageID;
	}

	public int getCategorieID() {
		return categorieID;
	}

	public void setCategorieID(int categorieID) {
		this.categorieID = categorieID;
	}

	public String getCodeBarre() {
		return codeBarre;
	}

	public void setCodeBarre(String codeBarre) {
		this.codeBarre = codeBarre;
	}

	public String getNomUnite() {
		return nomUnite;
	}

	public void setNomUnite(String nomUnite) {
		this.nomUnite = nomUnite;
	}

	public float getStockInitial() {
		return stockInitial;
	}

	public void setStockInitial(float stockInitial) {
		this.stockInitial = stockInitial;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public float getPrixVenteMin() {
		return prixVenteMin;
	}

	public void setPrixVenteMin(float prixVenteMin) {
		this.prixVenteMin = prixVenteMin;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public FormMedicament getFormMedicament() {
		return formMedicament;
	}

	public void setFormMedicament(FormMedicament formMedicament) {
		this.formMedicament = formMedicament;
	}

	public List<FormGroupe> getFormGroupes() {
		return formGroupes;
	}

	public void setFormGroupes(List<FormGroupe> formGroupes) {
		this.formGroupes = formGroupes;
	}

	
}
