package com.streenge.model.admin.money;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.streenge.model.admin.Entreprise;

/**
 * The persistent class for the tarif_abonnement database table.
 * 
 */
@Entity
@Table(name="tarif_abonnement")
@NamedQuery(name="TarifAbonnement.findAll", query="SELECT t FROM TarifAbonnement t")
public class TarifAbonnement implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Lob
	private String description;

	@Column(name="nombre_mois")
	private int nombreMois;

	@Lob
	private String note;

	@Column(name="par_mois")
	private String parMois;

	private float prix;

	@Lob
	private String statut;

	//bi-directional many-to-one association to Entreprise
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="id_entreprise")
	private Entreprise entreprise;

	public TarifAbonnement() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getNombreMois() {
		return this.nombreMois;
	}

	public void setNombreMois(int nombreMois) {
		this.nombreMois = nombreMois;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getParMois() {
		return this.parMois;
	}

	public void setParMois(String parMois) {
		this.parMois = parMois;
	}

	public float getPrix() {
		return this.prix;
	}

	public void setPrix(float prix) {
		this.prix = prix;
	}

	public String getStatut() {
		return this.statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public Entreprise getEntreprise() {
		return this.entreprise;
	}

	public void setEntreprise(Entreprise entreprise) {
		this.entreprise = entreprise;
	}

}