package com.streenge.model.admin.money;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.streenge.model.admin.Entreprise;
import com.streenge.model.admin.Utilisateur;

/**
 * The persistent class for the paiement_abonnement database table.
 * 
 */
@Entity
@Table(name="paiement_abonnement")
@NamedQuery(name="PaiementAbonnement.findAll", query="SELECT p FROM PaiementAbonnement p")
public class PaiementAbonnement implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_expiration")
	private Date dateExpiration;

	@Lob
	private String description;

	private float montant;

	@Column(name="moyen_paiement")
	private String moyenPaiement;

	@Column(name="nombre_mois")
	private int nombreMois;

	@Column(name="numero_carte")
	private String numeroCarte;

	private String telephone;

	//bi-directional many-to-one association to Entreprise
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="id_entreprise")
	private Entreprise entreprise;

	//bi-directional many-to-one association to Utilisateur
	@JsonIgnore
	@JoinColumn(name="id_utilisateur")
	private Utilisateur utilisateur;

	public PaiementAbonnement() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDateExpiration() {
		return this.dateExpiration;
	}

	public void setDateExpiration(Date dateExpiration) {
		this.dateExpiration = dateExpiration;
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

	public String getMoyenPaiement() {
		return this.moyenPaiement;
	}

	public void setMoyenPaiement(String moyenPaiement) {
		this.moyenPaiement = moyenPaiement;
	}

	public int getNombreMois() {
		return this.nombreMois;
	}

	public void setNombreMois(int nombreMois) {
		this.nombreMois = nombreMois;
	}

	public String getNumeroCarte() {
		return this.numeroCarte;
	}

	public void setNumeroCarte(String numeroCarte) {
		this.numeroCarte = numeroCarte;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Entreprise getEntreprise() {
		return this.entreprise;
	}

	public void setEntreprise(Entreprise entreprise) {
		this.entreprise = entreprise;
	}

	public Utilisateur getUtilisateur() {
		return this.utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

}