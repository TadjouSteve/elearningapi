package com.streenge.model.admin;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.streenge.model.admin.money.Caisse;
import com.streenge.model.produit.ProduitStockage;
import com.streenge.model.vente.facture.Facture;


/**
 * The persistent class for the stockage database table.
 * 
 */
@Entity
@Table(name="stockage")
@NamedQuery(name="Stockage.findAll", query="SELECT s FROM Stockage s")
public class Stockage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_stockage", unique=true, nullable=false)
	private int idStockage;

	@Column(length=80)
	private String adresse;

	@Column(length=255)
	private String description;

	@Column(length=30)
	private String etat;

	@Column(nullable=false, length=30)
	private String nom;

	@Column(length=30)
	private String statut;
	
	@Column(length=45)
	private String telephone;

	//bi-directional many-to-one association to ProduitStockage
	@JsonIgnore
	@OneToMany(mappedBy="stockage",cascade = {CascadeType.ALL})
	private List<ProduitStockage> produitStockages;

	@JsonIgnore
	@OneToMany(mappedBy="stockage")
	private List<Facture> factures;
	
	@JsonIgnore
	@OneToMany(mappedBy="stockage",cascade = { CascadeType.ALL })
	private List<UtilisateurStockage> utilisateurStockages;
	
	@JsonIgnore
	@OneToMany(mappedBy="stockage")
	private List<Caisse> caisses;
	
	public Stockage() {
	}

	public int getIdStockage() {
		return this.idStockage;
	}

	public void setIdStockage(int idStockage) {
		this.idStockage = idStockage;
	}

	public String getAdresse() {
		return this.adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
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

	public String getStatut() {
		return this.statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public List<ProduitStockage> getProduitStockages() {
		return this.produitStockages;
	}

	public void setProduitStockages(List<ProduitStockage> produitStockages) {
		this.produitStockages = produitStockages;
	}

	public ProduitStockage addProduitStockage(ProduitStockage produitStockage) {
		getProduitStockages().add(produitStockage);
		produitStockage.setStockage(this);

		return produitStockage;
	}

	public ProduitStockage removeProduitStockage(ProduitStockage produitStockage) {
		getProduitStockages().remove(produitStockage);
		produitStockage.setStockage(null);

		return produitStockage;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public List<Facture> getFactures() {
		return factures;
	}

	public void setFactures(List<Facture> factures) {
		this.factures = factures;
	}
	
	public List<UtilisateurStockage> getUtilisateurStockages() {
		return this.utilisateurStockages;
	}

	public void setUtilisateurStockages(List<UtilisateurStockage> utilisateurStockages) {
		this.utilisateurStockages = utilisateurStockages;
	}

	public UtilisateurStockage addUtilisateurStockage(UtilisateurStockage utilisateurStockage) {
		getUtilisateurStockages().add(utilisateurStockage);
		utilisateurStockage.setStockage(this);

		return utilisateurStockage;
	}

	public UtilisateurStockage removeUtilisateurStockage(UtilisateurStockage utilisateurStockage) {
		getUtilisateurStockages().remove(utilisateurStockage);
		utilisateurStockage.setStockage(null);

		return utilisateurStockage;
	}
	
	public List<Caisse> getCaisses() {
		return this.caisses;
	}

	public void setCaisses(List<Caisse> caisses) {
		this.caisses = caisses;
	}

	public Caisse addCaiss(Caisse caiss) {
		getCaisses().add(caiss);
		caiss.setStockage(this);

		return caiss;
	}

	public Caisse removeCaiss(Caisse caiss) {
		getCaisses().remove(caiss);
		caiss.setStockage(null);

		return caiss;
	}

}