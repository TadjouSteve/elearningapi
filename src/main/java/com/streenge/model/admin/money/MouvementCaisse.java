package com.streenge.model.admin.money;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.streenge.model.admin.Utilisateur;


/**
 * The persistent class for the mouvement_caisse database table.
 * 
 */
@Entity
@Table(name="mouvement_caisse")
@NamedQuery(name="MouvementCaisse.findAll", query="SELECT m FROM MouvementCaisse m")
public class MouvementCaisse implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_mouvement_caisse", unique=true, nullable=false, length=30)
	private String idMouvementCaisse;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@Column(length=200)
	private String description;

	@Column(name="id_document", length=30)
	private String idDocument;

	private float montant;

	@Column(length=200)
	private String motif;

	@Column(length=30)
	private String type;

	//bi-directional many-to-one association to Utilisateur
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_utilisateur")
	private Utilisateur utilisateur;

	//bi-directional many-to-one association to Caisse
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_caisse")
	private Caisse caisse;

	public MouvementCaisse() {
	}

	public String getIdMouvementCaisse() {
		return this.idMouvementCaisse;
	}

	public void setIdMouvementCaisse(String idMouvementCaisse) {
		this.idMouvementCaisse = idMouvementCaisse;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIdDocument() {
		return this.idDocument;
	}

	public void setIdDocument(String idDocument) {
		this.idDocument = idDocument;
	}

	public float getMontant() {
		return this.montant;
	}

	public void setMontant(float montant) {
		this.montant = montant;
	}

	public String getMotif() {
		return this.motif;
	}

	public void setMotif(String motif) {
		this.motif = motif;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
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