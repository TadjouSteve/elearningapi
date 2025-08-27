package com.streenge.model.produit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.streenge.model.produit.medicament.Medicament;

import lombok.Data;

/**
 * The persistent class for the produit database table.
 * 
 */
@Data
@Entity
@Table(name = "produit")
@NamedQuery(name = "Produit.findAll", query = "SELECT p FROM Produit p")
public class Produit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id_produit", unique = true, nullable = false, length = 30)
	private String idProduit;

	@Column(name = "code_barre", length = 100)
	private String codeBarre;

	@Column(name = "cout_initial")
	private float coutInitial;

	private float cout_UMP;

	@Temporal(TemporalType.DATE)
	@Column(name = "date_ajout")
	private Date dateAjout;

	@Column(length = 255)
	private String description;

	@Column(length = 30)
	private String etat;

	@Column(nullable = false, length = 50)
	private String nom;

	@Column(name = "nom_unite", length = 30)
	private String nomUnite;

	@Column(name = "point_commande")
	private int pointCommande;

	@Column(name = "prix_achat")
	private float prixAchat;

	@Column(name = "prix_vente")
	private float prixVente;

	@Column(name = "prix_vente_min")
	private float prixVenteMin;

	@Column(length = 50)
	private String sku;

	@Column(name = "stock_initial")
	private float stockInitial;

	@Column(name = "suivre_stock")
	private byte suivreStock;

	@Column(length = 45)
	private String type;

	// bi-directional many-to-one association to Pack

	@OneToMany(mappedBy = "produit", cascade = { CascadeType.ALL })
	private List<Pack> packs = new ArrayList<Pack>();

	// bi-directional many-to-one association to Categorie
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "id_categorie")
	private Categorie categorie;

	// bi-directional many-to-one association to ProduitStockage

	@OneToMany(mappedBy = "produit", cascade = { CascadeType.ALL })
	private List<ProduitStockage> produitStockages = new ArrayList<ProduitStockage>();

	// bi-directional many-to-one association to Image
	@OneToMany(mappedBy = "produit", cascade = { CascadeType.ALL })
	private List<Image> images;

	// bi-directional many-to-one association to GroupeProduit
	@OneToMany(mappedBy = "produit", cascade = { CascadeType.ALL })
	private List<GroupeProduit> groupeProduits;

	@ManyToOne
	@JoinColumn(name = "id_Medicament")
	private Medicament medicament;

	public Produit() {
	}

	public String getIdProduit() {
		return this.idProduit;
	}

	public void setIdProduit(String idProduit) {
		this.idProduit = idProduit;
	}

	public String getCodeBarre() {
		return this.codeBarre;
	}

	public void setCodeBarre(String codeBarre) {
		this.codeBarre = codeBarre;
	}

	public float getCoutInitial() {
		return this.coutInitial;
	}

	public void setCoutInitial(float coutInitial) {
		this.coutInitial = coutInitial;
	}

	public float getCout_UMP() {
		return this.cout_UMP;
	}

	public void setCout_UMP(float cout_UMP) {
		this.cout_UMP = cout_UMP;
	}

	public Date getDateAjout() {
		return this.dateAjout;
	}

	public void setDateAjout(Date dateAjout) {
		this.dateAjout = dateAjout;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEtat() {
		return this.etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getNomUnite() {
		return this.nomUnite;
	}

	public void setNomUnite(String nomUnite) {
		this.nomUnite = nomUnite;
	}

	public int getPointCommande() {
		return this.pointCommande;
	}

	public void setPointCommande(int pointCommande) {
		this.pointCommande = pointCommande;
	}

	public float getPrixAchat() {
		return this.prixAchat;
	}

	public void setPrixAchat(float prixAchat) {
		this.prixAchat = prixAchat;
	}

	public float getPrixVente() {
		return this.prixVente;
	}

	public void setPrixVente(float prixVente) {
		this.prixVente = prixVente;
	}

	public String getSku() {
		return this.sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public float getStockInitial() {
		return this.stockInitial;
	}

	public void setStockInitial(float stockInitial) {
		this.stockInitial = stockInitial;
	}

	public byte getSuivreStock() {
		return this.suivreStock;
	}

	public void setSuivreStock(byte suivreStock) {
		this.suivreStock = suivreStock;
	}

	public List<Pack> getPacks() {
		return this.packs;
	}

	public void setPacks(List<Pack> packs) {
		this.packs = packs;
	}

	public Pack addPack(Pack pack) {
		getPacks().add(pack);
		pack.setProduit(this);

		return pack;
	}

	public Pack removePack(Pack pack) {
		getPacks().remove(pack);
		pack.setProduit(null);

		return pack;
	}

	public Categorie getCategorie() {
		return this.categorie;
	}

	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}

	public List<ProduitStockage> getProduitStockages() {
		return this.produitStockages;
	}

	public void setProduitStockages(List<ProduitStockage> produitStockages) {
		this.produitStockages = produitStockages;
	}

	public ProduitStockage addProduitStockage(ProduitStockage produitStockage) {
		getProduitStockages().add(produitStockage);
		produitStockage.setProduit(this);

		return produitStockage;
	}

	public ProduitStockage removeProduitStockage(ProduitStockage produitStockage) {
		getProduitStockages().remove(produitStockage);
		produitStockage.setProduit(null);

		return produitStockage;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public float getPrixVenteMin() {
		return prixVenteMin;
	}

	public void setPrixVenteMin(float prixVenteMin) {
		this.prixVenteMin = prixVenteMin;
	}

	public List<Image> getImages() {
		return this.images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public Image addImage(Image image) {
		getImages().add(image);
		image.setProduit(this);

		return image;
	}

	public Image removeImage(Image image) {
		getImages().remove(image);
		image.setProduit(null);

		return image;
	}

	public Medicament getMedicament() {
		return this.medicament;
	}

	public void setMedicament(Medicament medicament) {
		this.medicament = medicament;
	}

	public List<GroupeProduit> getGroupeProduits() {
		return this.groupeProduits;
	}

	public void setGroupeProduits(List<GroupeProduit> groupeProduits) {
		this.groupeProduits = groupeProduits;
	}

	public GroupeProduit addGroupeProduit(GroupeProduit groupeProduit) {
		getGroupeProduits().add(groupeProduit);
		groupeProduit.setProduit(this);

		return groupeProduit;
	}

	public GroupeProduit removeGroupeProduit(GroupeProduit groupeProduit) {
		getGroupeProduits().remove(groupeProduit);
		groupeProduit.setProduit(null);

		return groupeProduit;
	}

}