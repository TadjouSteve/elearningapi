package com.streenge.model.contact;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.streenge.model.stock.OrdreAchat;

/**
 * The persistent class for the fournisseur database table.
 * 
 */
@Entity
@Table(name = "fournisseur")
@NamedQuery(name = "Fournisseur.findAll", query = "SELECT f FROM Fournisseur f")
public class Fournisseur implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id_fournisseur", unique = true, nullable = false, length = 30)
	private String idFournisseur;

	@Column(length = 100)
	private String adresse;

	@Temporal(TemporalType.DATE)
	@Column(name = "date_ajout")
	private Date dateAjout;

	@Column(length = 255)
	private String description;

	@Column(length = 30)
	private String email;

	@Column(length = 30)
	private String etat;

	@Column(nullable = false, length = 50)
	private String nom;

	@Column(length = 30)
	private String telephone;

	// bi-directional many-to-one association to OrdreAchatService
	@OneToMany(mappedBy = "fournisseur")
	private List<OrdreAchat> ordreAchats;

	public Fournisseur() {
	}

	public String getIdFournisseur() {
		return this.idFournisseur;
	}

	public void setIdFournisseur(String idFournisseur) {
		this.idFournisseur = idFournisseur;
	}

	public String getAdresse() {
		return this.adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public Date getDateAjout() {
		return this.dateAjout;
	}

	public void setDateAjout(Date dateAjout) {
		this.dateAjout = dateAjout;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEtat() {
		return this.etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public List<OrdreAchat> getOrdreAchats() {
		return this.ordreAchats;
	}

	public void setOrdreAchats(List<OrdreAchat> ordreAchats) {
		this.ordreAchats = ordreAchats;
	}

	public OrdreAchat addOrdreAchat(OrdreAchat ordreAchat) {
		getOrdreAchats().add(ordreAchat);
		ordreAchat.setFournisseur(this);

		return ordreAchat;
	}

	public OrdreAchat removeOrdreAchat(OrdreAchat ordreAchat) {
		getOrdreAchats().remove(ordreAchat);
		ordreAchat.setFournisseur(null);

		return ordreAchat;
	}

}