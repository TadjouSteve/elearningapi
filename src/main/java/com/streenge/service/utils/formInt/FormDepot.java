package com.streenge.service.utils.formInt;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.streenge.model.admin.Entreprise;
import com.streenge.model.admin.Stockage;
import com.streenge.model.admin.Utilisateur;

public class FormDepot {
	private String	idClient	= "";
	private int		idCaisse	= 0;
	private String	motif		= "";
	private float	montant		= 0;
	private int		idStockage	= 0;
	private String	description	= "";
	private String	idDepot		= "";
	private String	type		= "";
	private Date	date		= new Date();

	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private Utilisateur utilisateur;

	private FormCreateContact	contact		= null;
	private Entreprise			entreprise	= new Entreprise();

	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private Stockage stockage = new Stockage();

	public FormDepot() {
		super();
	}

	public String getIdClient() {
		return idClient;
	}

	public void setIdClient(String idClient) {
		this.idClient = idClient;
	}

	public String getMotif() {
		return motif;
	}

	public void setMotif(String motif) {
		this.motif = motif;
	}

	public float getMontant() {
		return montant;
	}

	public void setMontant(float montant) {
		this.montant = montant;
	}

	public int getIdStockage() {
		return idStockage;
	}

	public void setIdStockage(int idStockage) {
		this.idStockage = idStockage;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public FormCreateContact getContact() {
		return contact;
	}

	public void setContact(FormCreateContact contact) {
		this.contact = contact;
	}

	public Entreprise getEntreprise() {
		return entreprise;
	}

	public void setEntreprise(Entreprise entreprise) {
		this.entreprise = entreprise;
	}

	public Stockage getStockage() {
		return stockage;
	}

	public void setStockage(Stockage stockage) {
		this.stockage = stockage;
	}

	public String getIdDepot() {
		return idDepot;
	}

	public void setIdDepot(String idDepot) {
		this.idDepot = idDepot;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getIdCaisse() {
		return idCaisse;
	}

	public void setIdCaisse(int idCaisse) {
		this.idCaisse = idCaisse;
	}

}
