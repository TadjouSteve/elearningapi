package com.streenge.model.produit;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.streenge.model.LigneDocument;
import com.streenge.model.vente.facture.LigneFacture;

/**
 * The persistent class for the pack database table.
 * 
 */
@Entity
@Table(name = "pack")
@NamedQuery(name = "Pack.findAll", query = "SELECT p FROM Pack p")
public class Pack implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private int id;

	@Column(length = 30)
	private String etat;

	@Column(nullable = false, length = 30)
	private String nom;

	@Column(name = "nombre_unite")
	private float nombreUnite;

	@Column(name = "prix_achat")
	private float prixAchat;

	@Column(name = "prix_vente")
	private float prixVente;
	
	@Column(name="prix_vente_min")
	private float prixVenteMin;
	
	@Column(length = 30)
	private String type;

	@Column(length = 30)
	private String statut;

	// bi-directional many-to-one association to Produit
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "id_produit", nullable = false)
	private Produit produit;

	// @OneToMany(mappedBy="pack")
	@JsonIgnore
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@OneToMany(mappedBy = "pack", cascade = { CascadeType.ALL })
	private List<LigneDocument> ligneDocuments;

	// bi-directional many-to-one association to LigneFacture
	@JsonIgnore
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@OneToMany(mappedBy = "pack", cascade = { CascadeType.ALL })
	private List<LigneFacture> ligneFactures;

	public Pack() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
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

	public float getNombreUnite() {
		return this.nombreUnite;
	}

	public void setNombreUnite(float nombreUnite) {
		this.nombreUnite = nombreUnite;
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

	public String getStatut() {
		return this.statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public Produit getProduit() {
		return this.produit;
	}

	public void setProduit(Produit produit) {
		this.produit = produit;
	}

	public List<LigneFacture> getLigneFactures() {
		return this.ligneFactures;
	}

	public void setLigneFactures(List<LigneFacture> ligneFactures) {
		this.ligneFactures = ligneFactures;
	}

	public LigneFacture addLigneFacture(LigneFacture ligneFacture) {
		getLigneFactures().add(ligneFacture);
		ligneFacture.setPack(this);

		return ligneFacture;
	}

	public LigneFacture removeLigneFacture(LigneFacture ligneFacture) {
		getLigneFactures().remove(ligneFacture);
		ligneFacture.setPack(null);

		return ligneFacture;
	}

	public List<LigneDocument> getLigneDocuments() {
		return ligneDocuments;
	}

	public void setLigneDocuments(List<LigneDocument> ligneDocuments) {
		this.ligneDocuments = ligneDocuments;
	}
	
	public LigneDocument addLigneDocument(LigneDocument ligneDocument) {
		getLigneDocuments().add(ligneDocument);
		ligneDocument.setPack(this);

		return ligneDocument;
	}

	public LigneDocument removeLigneDocument(LigneDocument ligneDocument) {
		getLigneDocuments().remove(ligneDocument);
		ligneDocument.setPack(null);

		return ligneDocument;
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

}