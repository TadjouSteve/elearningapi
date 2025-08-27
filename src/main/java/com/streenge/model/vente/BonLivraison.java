package com.streenge.model.vente;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
 * The persistent class for the bon_livraison database table.
 * 
 */
@Entity
@Table(name="bon_livraison")
@NamedQuery(name="BonLivraison.findAll", query="SELECT b FROM BonLivraison b")
public class BonLivraison implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_bon_livraison", unique=true, nullable=false, length=30)
	private String idBonLivraison;

	@Column(name="adresse_livraison", length=150)
	private String adresseLivraison;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_creation")
	private Date dateCreation;

	@Temporal(TemporalType.DATE)
	@Column(name="date_livraison")
	private Date dateLivraison;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_modification")
	private Date dateModification;

	@Lob
	private String description;

	@Column(length=30)
	private String etat;

	@Lob
	private String note;

	@Column(length=30)
	private String statut;

	//bi-directional many-to-one association to BonCommande
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_bon_commande", nullable=false)
	private BonCommande bonCommande;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "id_utilisateur")
	private Utilisateur utilisateur;
	
	//bi-directional many-to-one association to LigneDocument
	@JsonIgnore
	@OneToMany(mappedBy="bonLivraison",cascade = { CascadeType.ALL })
	private List<LigneDocument> ligneDocuments;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "id_stockage")
	private Stockage stockage;

	public BonLivraison() {
	}

	public String getIdBonLivraison() {
		return this.idBonLivraison;
	}

	public void setIdBonLivraison(String idBonLivraison) {
		this.idBonLivraison = idBonLivraison;
	}

	public String getAdresseLivraison() {
		return this.adresseLivraison;
	}

	public void setAdresseLivraison(String adresseLivraison) {
		this.adresseLivraison = adresseLivraison;
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

	public String getStatut() {
		return this.statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}
	
	public Utilisateur getUtilisateur() {
		return this.utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public BonCommande getBonCommande() {
		return this.bonCommande;
	}

	public void setBonCommande(BonCommande bonCommande) {
		this.bonCommande = bonCommande;
	}

	public List<LigneDocument> getLigneDocuments() {
		return this.ligneDocuments;
	}

	public void setLigneDocuments(List<LigneDocument> ligneDocuments) {
		this.ligneDocuments = ligneDocuments;
	}

	public LigneDocument addLigneDocument(LigneDocument ligneDocument) {
		getLigneDocuments().add(ligneDocument);
		ligneDocument.setBonLivraison(this);

		return ligneDocument;
	}

	public LigneDocument removeLigneDocument(LigneDocument ligneDocument) {
		getLigneDocuments().remove(ligneDocument);
		ligneDocument.setBonLivraison(null);

		return ligneDocument;
	}
	
	public Stockage getStockage() {
		return stockage;
	}

	public void setStockage(Stockage stockage) {
		this.stockage = stockage;
	}

}