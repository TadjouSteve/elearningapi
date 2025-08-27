package com.streenge.model.depense;

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
import com.streenge.model.admin.Stockage;
import com.streenge.model.admin.Utilisateur;
import com.streenge.model.admin.money.Caisse;


/**
 * The persistent class for the fiche_depense database table.
 * 
 */
@Entity
@Table(name="fiche_depense")
@NamedQuery(name="FicheDepense.findAll", query="SELECT f FROM FicheDepense f")
public class FicheDepense implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_fiche_depense", unique=true, nullable=false, length=30)
	private String idFicheDepense;//new

	@Column(length=255)
	private String context; 

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_modification")
	private Date dateModification;

	@Column(length=30)
	private String etat;

	@Column(length=255)
	private String motif;

	@Lob
	private String note;

	@Column(length=30)
	private String statut;

	//bi-directional many-to-one association to Utilisateur
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_utilisateur_alter")
	private Utilisateur utilisateurAlter;

	//bi-directional many-to-one association to Utilisateur
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_utilisateur")
	private Utilisateur utilisateur;

	//bi-directional many-to-one association to Stockage
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_stockage", nullable=false)
	private Stockage stockage;

	//bi-directional many-to-one association to Caisse
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_caisse")
	private Caisse caisse;

	//bi-directional many-to-one association to LigneDepense
	@JsonIgnore
	@OneToMany(mappedBy="ficheDepense",cascade = { CascadeType.ALL })
	private List<LigneDepense> ligneDepenses;

	public FicheDepense() {
	}

	public String getIdFicheDepense() {
		return this.idFicheDepense;
	}

	public void setIdFicheDepense(String idFicheDepense) {
		this.idFicheDepense = idFicheDepense;
	}

	public String getContext() {
		return this.context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getEtat() {
		return this.etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	public String getMotif() {
		return this.motif;
	}

	public void setMotif(String motif) {
		this.motif = motif;
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

	

	public Stockage getStockage() {
		return this.stockage;
	}

	public void setStockage(Stockage stockage) {
		this.stockage = stockage;
	}

	public Caisse getCaisse() {
		return this.caisse;
	}

	public void setCaisse(Caisse caisse) {
		this.caisse = caisse;
	}

	public List<LigneDepense> getLigneDepenses() {
		return this.ligneDepenses;
	}

	public void setLigneDepenses(List<LigneDepense> ligneDepenses) {
		this.ligneDepenses = ligneDepenses;
	}

	public LigneDepense addLigneDepens(LigneDepense ligneDepens) {
		getLigneDepenses().add(ligneDepens);
		ligneDepens.setFicheDepense(this);

		return ligneDepens;
	}

	public LigneDepense removeLigneDepens(LigneDepense ligneDepens) {
		getLigneDepenses().remove(ligneDepens);
		ligneDepens.setFicheDepense(null);

		return ligneDepens;
	}

	public Utilisateur getUtilisateurAlter() {
		return utilisateurAlter;
	}

	public void setUtilisateurAlter(Utilisateur utilisateurAlter) {
		this.utilisateurAlter = utilisateurAlter;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public Date getDateModification() {
		return dateModification;
	}

	public void setDateModification(Date dateModification) {
		this.dateModification = dateModification;
	}

}