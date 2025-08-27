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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.streenge.model.LigneDocument;
import com.streenge.model.admin.Stockage;
import com.streenge.model.admin.Utilisateur;

/**
 * The persistent class for the mouvement_stock database table.
 * 
 */
@Entity
@Table(name = "mouvement_stock")
@NamedQuery(name = "MouvementStock.findAll", query = "SELECT m FROM MouvementStock m")
public class MouvementStock implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id_mouvement_stock", unique = true, nullable = false, length = 30)
	private String idMouvementStock;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_creation")
	private Date dateCreation;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_modification")
	private Date dateModification;

	@Lob
	private String description;

	@Column(length = 30)
	private String etat;

	@Column(length = 30)
	private String statut;

	@Lob
	private String note;

	// bi-directional many-to-one association to LigneDocument
	@OneToMany(mappedBy = "mouvementStock",cascade = {CascadeType.ALL})
	private List<LigneDocument> ligneDocuments;

	// bi-directional many-to-one association to Utilisateur
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_utilisateur")
	private Utilisateur utilisateur;

	// bi-directional many-to-one association to Stockage
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_stockage", nullable = false)
	private Stockage stockage;

	// bi-directional many-to-one association to Stockage
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_stockage_arrive")
	private Stockage stockageArrive;

	public MouvementStock() {
	}

	public String getIdMouvementStock() {
		return this.idMouvementStock;
	}

	public void setIdMouvementStock(String idMouvementStock) {
		this.idMouvementStock = idMouvementStock;
	}

	public Date getDateCreation() {
		return this.dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
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

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getEtat() {
		return this.etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
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
		ligneDocument.setMouvementStock(this);

		return ligneDocument;
	}

	public LigneDocument removeLigneDocument(LigneDocument ligneDocument) {
		getLigneDocuments().remove(ligneDocument);
		ligneDocument.setMouvementStock(null);

		return ligneDocument;
	}

	public Stockage getStockage() {
		return this.stockage;
	}

	public void setStockage(Stockage stockage) {
		this.stockage = stockage;
	}

	public Utilisateur getUtilisateur() {
		return this.utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public Stockage getStockageArrive() {
		return stockageArrive;
	}

	public void setStockageArrive(Stockage stockageArrive) {
		this.stockageArrive = stockageArrive;
	}

}