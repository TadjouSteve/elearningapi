package com.streenge.model.admin.money;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.streenge.model.admin.Utilisateur;


/**
 * The persistent class for the historique_caisse database table.
 * 
 */
@Entity
@Table(name="historique_caisse")
@NamedQuery(name="HistoriqueCaisse.findAll", query="SELECT h FROM HistoriqueCaisse h")
public class HistoriqueCaisse implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int id;

	private float ajustement;

	@Column(length=200)
	private String context;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@Column(name="id_document")
	private String idDocument;

	@Column(length=200)
	private String motif;

	private float solde;

	@Column(length=30)
	private String type;

	//bi-directional many-to-one association to Caisse
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_caisse")
	private Caisse caisse;

	//bi-directional many-to-one association to Utilisateur
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_utilisateur")
	private Utilisateur utilisateur;

	public HistoriqueCaisse() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getAjustement() {
		return this.ajustement;
	}

	public void setAjustement(float ajustement) {
		this.ajustement = ajustement;
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

	public String getIdDocument() {
		return this.idDocument;
	}

	public void setIdDocument(String idDocument) {
		this.idDocument = idDocument;
	}

	public String getMotif() {
		return this.motif;
	}

	public void setMotif(String motif) {
		this.motif = motif;
	}

	public float getSolde() {
		return this.solde;
	}

	public void setSolde(float solde) {
		this.solde = solde;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Caisse getCaisse() {
		return this.caisse;
	}

	public void setCaisse(Caisse caisse) {
		this.caisse = caisse;
	}

	public Utilisateur getUtilisateur() {
		return this.utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

}