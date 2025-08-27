package com.streenge.service.utils.formInt;

import java.util.Date;

import com.streenge.model.admin.Utilisateur;
import com.streenge.service.utils.streengeData.Profil;

public class FormFilter {
	private String	idClient	= "";
	private String	nomClient	= "";

	private String	idProduit		= "";
	private String	idCategorie		= "";
	private String	nomUtilisateur	= "";
	private String	nomCategorie	= null;

	private int idStockage = 0;

	private int		periode		= 3;
	private Date	date		= null;
	private Date	dateDebut	= null;
	private Date	dateFin		= new Date();

	// pour un filtre plus puissant, notament pour de post avec plus de parametre
	// qu'un simple param ou filter

	private int		idUtilisateur	= 0;
	private String	profil			= Profil.getVendeur();
	private String	filter			= "";

	private Utilisateur utilisateur = null;

	public String getIdClient() {
		return idClient;
	}

	public void setIdClient(String idClient) {
		this.idClient = idClient;
	}

	public String getNomClient() {
		return nomClient;
	}

	public void setNomClient(String nomClient) {
		this.nomClient = nomClient;
	}

	public String getIdProduit() {
		return idProduit;
	}

	public void setIdProduit(String idProduit) {
		this.idProduit = idProduit;
	}

	public String getNomUtilisateur() {
		return nomUtilisateur;
	}

	public void setNomUtilisateur(String nomUtilisateur) {
		this.nomUtilisateur = nomUtilisateur;
	}

	public int getIdStockage() {
		return idStockage;
	}

	public void setIdStockage(int idStockage) {
		this.idStockage = idStockage;
	}

	public int getPeriode() {
		return periode;
	}

	public void setPeriode(int periode) {
		this.periode = periode;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public int getIdUtilisateur() {
		return idUtilisateur;
	}

	public void setIdUtilisateur(int idUtilisateur) {
		this.idUtilisateur = idUtilisateur;
	}

	public String getProfil() {
		return profil;
	}

	public void setProfil(String profil) {
		this.profil = profil;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public String getNomCategorie() {
		return nomCategorie;
	}

	public void setNomCategorie(String nomCategorie) {
		this.nomCategorie = nomCategorie;
	}

	public String getIdCategorie() {
		return idCategorie;
	}

	public void setIdCategorie(String idCategorie) {
		this.idCategorie = idCategorie;
	}
}
