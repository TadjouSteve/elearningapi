package com.streenge.model.stock;

import java.io.Serializable;
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
import com.streenge.model.LigneDocument;
import com.streenge.model.admin.Stockage;
import com.streenge.model.admin.Utilisateur;
import com.streenge.model.contact.Fournisseur;

/**
 * The persistent class for the ordre_achat database table.
 * 
 */
@Entity
@Table(name = "ordre_achat")
@NamedQuery(name = "OrdreAchat.findAll", query = "SELECT o FROM OrdreAchat o")
public class OrdreAchat implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id_ordre_achat", unique = true, nullable = false, length = 30)
	private String idOrdreAchat;

	@Column(name = "adresse_livraison", length = 150)
	private String adresseLivraison;

	@Column(name = "condition_vente", length = 255)
	private String conditionVente;

	@Column(name = "cout_livraison")
	private float coutLivraison;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_creation")
	private Date dateCreation;

	@Temporal(TemporalType.DATE)
	@Column(name = "date_livriason")
	private Date dateLivraison;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_modification")
	private Date dateModification;

	@Column(length = 255)
	private String description;

	@Column(length = 30)
	private String etat;

	@Column(length = 255)
	private String note;

	private float remise;

	@Column(length = 30)
	private String statut;

	// bi-directional many-to-one association to LigneDocument
	@JsonIgnore
	@OneToMany(mappedBy = "ordreAchat",cascade = {CascadeType.ALL})
	private List<LigneDocument> ligneDocuments;

	// bi-directional many-to-one association to Livraison
	@JsonIgnore
	@OneToMany(mappedBy = "ordreAchat",cascade = {CascadeType.ALL})
	private List<Livraison> livraisons;

	// bi-directional many-to-one association to Fournisseur
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_fournisseur")
	private Fournisseur fournisseur;

	// bi-directional many-to-one association to Utilisateur
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_utilisateur")
	private Utilisateur utilisateur;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "id_stockage", nullable = false)
	private Stockage stockage;

	public OrdreAchat() {
	}

	public String getIdOrdreAchat() {
		return this.idOrdreAchat;
	}

	public void setIdOrdreAchat(String idOrdreAchat) {
		this.idOrdreAchat = idOrdreAchat;
	}

	public String getAdresseLivraison() {
		return this.adresseLivraison;
	}

	public void setAdresseLivraison(String adresseLivraison) {
		this.adresseLivraison = adresseLivraison;
	}

	public String getConditionVente() {
		return this.conditionVente;
	}

	public void setConditionVente(String conditionVente) {
		this.conditionVente = conditionVente;
	}

	public float getCoutLivraison() {
		return this.coutLivraison;
	}

	public void setCoutLivraison(float coutLivraison) {
		this.coutLivraison = coutLivraison;
	}

	public Date getDateCreation() {
		return this.dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public Date getDateLivraison() {
		return this.dateLivraison;
	}

	public void setDateLivraison(Date dateLivraison) {
		this.dateLivraison = dateLivraison;
	}

	public Date getDateModification() {
		return this.dateModification;
	}

	public void setDateModification(Date dateModification) {
		this.dateModification = dateModification;
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

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public float getRemise() {
		return this.remise;
	}

	public void setRemise(float remise) {
		this.remise = remise;
	}

	public String getStatut() {
		return this.statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public List<LigneDocument> getLigneDocuments() {
		return this.ligneDocuments;
	}

	public void setLigneDocuments(List<LigneDocument> ligneDocuments) {
		this.ligneDocuments = ligneDocuments;
	}

	public LigneDocument addLigneDocument(LigneDocument ligneDocument) {
		getLigneDocuments().add(ligneDocument);
		ligneDocument.setOrdreAchat(this);

		return ligneDocument;
	}

	public LigneDocument removeLigneDocument(LigneDocument ligneDocument) {
		getLigneDocuments().remove(ligneDocument);
		ligneDocument.setOrdreAchat(null);

		return ligneDocument;
	}

	public List<Livraison> getLivraisons() {
		return this.livraisons;
	}

	public void setLivraisons(List<Livraison> livraisons) {
		this.livraisons = livraisons;
	}

	public Livraison addLivraison(Livraison livraison) {
		getLivraisons().add(livraison);
		livraison.setOrdreAchat(this);

		return livraison;
	}

	public Livraison removeLivraison(Livraison livraison) {
		getLivraisons().remove(livraison);
		livraison.setOrdreAchat(null);

		return livraison;
	}

	public Fournisseur getFournisseur() {
		return this.fournisseur;
	}

	public void setFournisseur(Fournisseur fournisseur) {
		this.fournisseur = fournisseur;
	}

	public Utilisateur getUtilisateur() {
		return this.utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public Stockage getStockage() {
		return stockage;
	}

	public void setStockage(Stockage stockage) {
		this.stockage = stockage;
	}

}