package com.streenge.service.utils.formOut;

import java.util.Date;

import com.streenge.model.admin.Utilisateur;

public class FormPaiement {
	private int		idPaiement			= 0;
	private String	idFacture			= "";
	private String	nomMethodePaiement	= "";
	private Date	datePaiement		= null;
	private float	montant				= 0;
	// private String moyenPaiement = "";
	private String description = "";
	
	private Utilisateur utilisateur=null;
	private String	nomUtilisateur			= "";

	public Date getDatePaiement() {
		return datePaiement;
	}

	public void setDatePaiement(Date datePaiement) {
		this.datePaiement = datePaiement;
	}

	public float getMontant() {
		return montant;
	}

	public void setMontant(float montant) {
		this.montant = montant;
	}

	/*
	 * public String getMoyenPaiement() { return moyenPaiement; }
	 * 
	 * public void setMoyenPaiement(String moyenPaiement) { this.moyenPaiement =
	 * moyenPaiement; }
	 */

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIdFacture() {
		return idFacture;
	}

	public void setIdFacture(String idFacture) {
		this.idFacture = idFacture;
	}

	public String getNomMethodePaiement() {
		return nomMethodePaiement;
	}

	public void setNomMethodePaiement(String nomMethodePaiement) {
		this.nomMethodePaiement = nomMethodePaiement;
	}

	public int getIdPaiement() {
		return idPaiement;
	}

	public void setIdPaiement(int idPaiement) {
		this.idPaiement = idPaiement;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public String getNomUtilisateur() {
		return nomUtilisateur;
	}

	public void setNomUtilisateur(String nomUtilisateur) {
		this.nomUtilisateur = nomUtilisateur;
	}

}
