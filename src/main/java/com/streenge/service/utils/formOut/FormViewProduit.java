package com.streenge.service.utils.formOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.streenge.model.produit.Image;
import com.streenge.service.utils.formData.FormGroupe;
import com.streenge.service.utils.formData.formMedicament.FormMedicament;

public class FormViewProduit {
	private String			id				= "";
	private String			nom				= "";
	private String			nomUnite		= "";
	private String			sku				= "";
	private String			codeBarre		= "";
	private String			description		= "";
	private String			categorie		= "";
	private String			type			= "";
	private float			pointCommande	= 0;
	private float			disponible		= 0;
	private float			reel			= 0;
	private float			avenir			= 0;
	private float			coutUMP			= 0;
	private float			stockInitial	= 0;
	private float			coutInitial		= 0;
	private float			prixVente		= 0;
	private float			prixVenteMin	= 0;
	private float			prixAchat		= 0;
	private List<FormStock>	stocks			= null;
	private List<FormPack>	packs			= null;

	private FormMedicament formMedicament = new FormMedicament();
	private List<FormGroupe> formGroupes = new ArrayList<>();

	private String	nomPackPrincipale			= "";
	private float	nombreUnitePackPrincipale	= 1;

	private List<String> listCategories = new ArrayList<>();

	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private List<Image> imgages = new ArrayList<>();

	// Speiallement pour aficher le niveau de stock, pour aficher la petiite table
	// qui liste dans quel bon de commende est reserver le produit, aussi dns quell
	// Ordre d'achat on attent le produt
	private HashMap<String, Float>	listBonCommande	= new HashMap<>();
	private HashMap<String, Float>	listOrdreAchat	= new HashMap<>();

	public FormViewProduit() {
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getCodeBarre() {
		return codeBarre;
	}

	public void setCodeBarre(String codeBarre) {
		this.codeBarre = codeBarre;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategorie() {
		return categorie;
	}

	public void setCategorie(String categorie) {
		this.categorie = categorie;
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

	public float getCoutUMP() {
		return coutUMP;
	}

	public void setCoutUMP(float coutUMP) {
		this.coutUMP = coutUMP;
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

	public List<FormStock> getStocks() {
		return stocks;
	}

	public void setStocks(List<FormStock> stocks) {
		this.stocks = stocks;
	}

	public String getNomUnite() {
		return nomUnite;
	}

	public void setNomUnite(String nomUnite) {
		this.nomUnite = nomUnite;
	}

	public List<FormPack> getPacks() {
		return packs;
	}

	public void setPacks(List<FormPack> packs) {
		this.packs = packs;
	}

	public float getPointCommande() {
		return pointCommande;
	}

	public void setPointCommande(float pointCommande) {
		this.pointCommande = pointCommande;
	}

	public List<String> getListCategories() {
		return listCategories;
	}

	public void setListCategories(List<String> listCategories) {
		this.listCategories = listCategories;
	}

	public String getNomPackPrincipale() {
		return nomPackPrincipale;
	}

	public void setNomPackPrincipale(String nomPackPrincipale) {
		this.nomPackPrincipale = nomPackPrincipale;
	}

	public float getNombreUnitePackPrincipale() {
		return nombreUnitePackPrincipale;
	}

	public void setNombreUnitePackPrincipale(float nombreUnitePackPrincipale) {
		this.nombreUnitePackPrincipale = nombreUnitePackPrincipale;
	}

	public float getCoutInitial() {
		return coutInitial;
	}

	public void setCoutInitial(float coutInitial) {
		this.coutInitial = coutInitial;
	}

	public float getStockInitial() {
		return stockInitial;
	}

	public void setStockInitial(float stockInitial) {
		this.stockInitial = stockInitial;
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

	public List<Image> getImgages() {
		return imgages;
	}

	public void setImgages(List<Image> imgages) {
		this.imgages = imgages;
	}

	public HashMap<String, Float> getListBonCommande() {
		return listBonCommande;
	}

	public void setListBonCommande(HashMap<String, Float> listBonCommande) {
		this.listBonCommande = listBonCommande;
	}

	public HashMap<String, Float> getListOrdreAchat() {
		return listOrdreAchat;
	}

	public void setListOrdreAchat(HashMap<String, Float> listOrdreAchat) {
		this.listOrdreAchat = listOrdreAchat;
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
