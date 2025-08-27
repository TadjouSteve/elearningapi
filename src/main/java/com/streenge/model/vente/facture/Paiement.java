package com.streenge.model.vente.facture;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.streenge.model.admin.Utilisateur;
import com.streenge.model.admin.money.Caisse;


/**
 * The persistent class for the paiement database table.
 * 
 */
@Entity
@Table(name="paiement")
@NamedQuery(name="Paiement.findAll", query="SELECT p FROM Paiement p")
public class Paiement implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int id;

	@Temporal(TemporalType.DATE)
	@Column(name="date_paiement")
	private Date datePaiement;

	
	private String description;

	private float montant;

	//bi-directional many-to-one association to Facture
	@ManyToOne(fetch=FetchType.LAZY, cascade = {CascadeType.ALL})
	@JoinColumn(name="id_facture", nullable=false)
	private Facture facture;

	//bi-directional many-to-one association to MethodePaiement
	@ManyToOne(fetch=FetchType.LAZY, cascade = {CascadeType.ALL})
	@JoinColumn(name="id_methode_paiement", nullable=true)
	private MethodePaiement methodePaiement;
	
	//@Column(length=255)
	private String note;

	//@Column(length=255)
	private String type;

	//bi-directional many-to-one association to Utilisateur
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_utilisateur")
	private Utilisateur utilisateur;

	//bi-directional many-to-one association to Caisse
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_caisse")
	private Caisse caisse;

	public Paiement() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDatePaiement() {
		return this.datePaiement;
	}

	public void setDatePaiement(Date datePaiement) {
		this.datePaiement = datePaiement;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getMontant() {
		return this.montant;
	}

	public void setMontant(float montant) {
		this.montant = montant;
	}

	public Facture getFacture() {
		return this.facture;
	}

	public void setFacture(Facture facture) {
		this.facture = facture;
	}

	public MethodePaiement getMethodePaiement() {
		return this.methodePaiement;
	}

	public void setMethodePaiement(MethodePaiement methodePaiement) {
		this.methodePaiement = methodePaiement;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	public Utilisateur getUtilisateur() {
		return this.utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public Caisse getCaisse() {
		return this.caisse;
	}

	public void setCaisse(Caisse caisse) {
		this.caisse = caisse;
	}

}