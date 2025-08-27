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

/**
 * The persistent class for the livraison database table.
 * 
 */
@Entity
@Table(name = "livraison")
@NamedQuery(name = "Livraison.findAll", query = "SELECT l FROM Livraison l")
public class Livraison implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id_livraison", unique = true, nullable = false, length = 30)
	private String idLivraison;

	@Column(name = "adresse_livraison", length = 150)
	private String adresseLivraison;

	@Column(name = "cout_livraison")
	private float coutLivraison;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_creation")
	private Date dateCreation;

	@Temporal(TemporalType.DATE)
	@Column(name = "date_livraison")
	private Date dateLivraison;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_modification")
	private Date dateModification;
	
	@Column(length=255)
	private String description;
	
	@Column(length = 30)
	private String etat;

	@Column(length = 255)
	private String note;

	@Column(name = "reference_externe", length = 150)
	private String referenceExterne;

	private float remise;

	@Column(length = 30)
	private String statut;

	// bi-directional many-to-one association to LigneDocument
	@JsonIgnore
	@OneToMany(mappedBy = "livraison",cascade = { CascadeType.ALL })
	private List<LigneDocument> ligneDocuments;

	// bi-directional many-to-one association to OrdreAchatService
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_ordre_achat", nullable = false)
	private OrdreAchat ordreAchat;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "id_stockage")
	private Stockage stockage;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "id_utilisateur")
	private Utilisateur utilisateur;

	public Livraison() {
	}

	public String getIdLivraison() {
		return this.idLivraison;
	}

	public void setIdLivraison(String idLivraison) {
		this.idLivraison = idLivraison;
	}

	public String getAdresseLivraison() {
		return this.adresseLivraison;
	}

	public void setAdresseLivraison(String adresseLivraison) {
		this.adresseLivraison = adresseLivraison;
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

	public String getReferenceExterne() {
		return this.referenceExterne;
	}

	public void setReferenceExterne(String referenceExterne) {
		this.referenceExterne = referenceExterne;
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
		ligneDocument.setLivraison(this);

		return ligneDocument;
	}

	public LigneDocument removeLigneDocument(LigneDocument ligneDocument) {
		getLigneDocuments().remove(ligneDocument);
		ligneDocument.setLivraison(null);

		return ligneDocument;
	}

	public OrdreAchat getOrdreAchat() {
		return this.ordreAchat;
	}

	public void setOrdreAchat(OrdreAchat ordreAchat) {
		this.ordreAchat = ordreAchat;
	}
	
	public Utilisateur getUtilisateur() {
		return this.utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Stockage getStockage() {
		return stockage;
	}

	public void setStockage(Stockage stockage) {
		this.stockage = stockage;
	}

}