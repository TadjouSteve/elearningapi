package com.streenge.service.utils.formOut;

import java.util.Date;

import com.streenge.service.utils.formInt.FormCreateContact;

public class FormClient extends FormCreateContact {
	private int		nombreFacture					= 0;
	private int		nombreFacturePaye				= 0;
	private int		nombreFactureNonPaye			= 0;
	private int		nombreFacturePartiellementPaye	= 0;
	private int		nombreFactureAnnule				= 0;
	private float	solde							= 0;
	private Date	date							= null;

	public float getSolde() {
		return solde;
	}

	public void setSolde(float solde) {
		this.solde = solde;
	}

	public int getNombreFacture() {
		return nombreFacture;
	}

	public void setNombreFacture(int nombreFacture) {
		this.nombreFacture = nombreFacture;
	}

	public int getNombreFactureNonPaye() {
		return nombreFactureNonPaye;
	}

	public void setNombreFactureNonPaye(int nombreFactureNonPaye) {
		this.nombreFactureNonPaye = nombreFactureNonPaye;
	}

	public int getNombreFacturePartiellementPaye() {
		return nombreFacturePartiellementPaye;
	}

	public void setNombreFacturePartiellementPaye(int nombreFacturePartiellementPaye) {
		this.nombreFacturePartiellementPaye = nombreFacturePartiellementPaye;
	}

	public int getNombreFactureAnnule() {
		return nombreFactureAnnule;
	}

	public void setNombreFactureAnnule(int nombreFactureAnnule) {
		this.nombreFactureAnnule = nombreFactureAnnule;
	}

	public int getNombreFacturePaye() {
		return nombreFacturePaye;
	}

	public void setNombreFacturePaye(int nombreFacturePaye) {
		this.nombreFacturePaye = nombreFacturePaye;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
