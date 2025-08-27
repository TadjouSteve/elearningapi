package com.streenge.service.utils.formInt;

import java.util.Date;

import com.streenge.model.admin.Entreprise;

public class FormCreateContact {
	private String	id			= "";
	private String	nom			= "";
	private String	telephone	= "";
	private String	email		= "";
	private String	description	= "";
	private String	pays		= "";
	private String	adresse1	= "";
	private String	adresse2	= "";
	private String	ville		= "";
	private Date	dateAjout;

	// new
	private String	cni			= "";
	private String	niu			= "";
	private String	rccm		= "";
	private String	boitePostal	= "";

	// private Date date=new Date();

	// ceci me permet d'avoir access directement au information de l'entreprise
	// notament pour rediger le statut de compte
	private Entreprise entreprise = new Entreprise();

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPays() {
		return pays;
	}

	public void setPays(String pays) {
		this.pays = pays;
	}

	public String getAdresse1() {
		return adresse1;
	}

	public void setAdresse1(String adresse1) {
		this.adresse1 = adresse1;
	}

	public String getAdresse2() {
		return adresse2;
	}

	public void setAdresse2(String adresse2) {
		this.adresse2 = adresse2;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public Date getDateAjout() {
		return dateAjout;
	}

	public void setDateAjout(Date dateAjout) {
		this.dateAjout = dateAjout;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Entreprise getEntreprise() {
		return entreprise;
	}

	public void setEntreprise(Entreprise entreprise) {
		this.entreprise = entreprise;
	}

	public String getCni() {
		return cni;
	}

	public void setCni(String cni) {
		this.cni = cni;
	}

	public String getNiu() {
		return niu;
	}

	public void setNiu(String niu) {
		this.niu = niu;
	}

	public String getRccm() {
		return rccm;
	}

	public void setRccm(String rccm) {
		this.rccm = rccm;
	}

	public String getBoitePostal() {
		return boitePostal;
	}

	public void setBoitePostal(String boitePostal) {
		this.boitePostal = boitePostal;
	}
	
	

}
