package com.streenge.service.utils.formOut;

public class FormDashboard {
	private float	livreCeMois			= 0;
	private float	factureMoisDernier	= 0;
	private float	factureCeMois		= 0;
	private float	factureCetteSemaine	= 0;
	private float	factureAujourdhui			= 0;

	private float	nonbrefactureMoisDernier	= 0;
	private float	nonbrefactureCeMois			= 0;
	private float	nonbrefactureCetteSemaine	= 0;
	private float	nonbrefactureAujourdhui		= 0;

	private AllDocument allDocument = new AllDocument();

	public float getLivreCeMois() {
		return livreCeMois;
	}

	public void setLivreCeMois(float livreCeMois) {
		this.livreCeMois = livreCeMois;
	}

	public float getFactureMoisDernier() {
		return factureMoisDernier;
	}

	public void setFactureMoisDernier(float factureMoisDernier) {
		this.factureMoisDernier = factureMoisDernier;
	}

	public float getFactureCeMois() {
		return factureCeMois;
	}

	public void setFactureCeMois(float factureCeMois) {
		this.factureCeMois = factureCeMois;
	}

	public float getFactureCetteSemaine() {
		return factureCetteSemaine;
	}

	public void setFactureCetteSemaine(float factureCetteSemaine) {
		this.factureCetteSemaine = factureCetteSemaine;
	}

	public AllDocument getAllDocument() {
		return allDocument;
	}

	public void setAllDocument(AllDocument allDocument) {
		this.allDocument = allDocument;
	}

	public float getNonbrefactureMoisDernier() {
		return nonbrefactureMoisDernier;
	}

	public void setNonbrefactureMoisDernier(float nonbrefactureMoisDernier) {
		this.nonbrefactureMoisDernier = nonbrefactureMoisDernier;
	}

	public float getNonbrefactureCeMois() {
		return nonbrefactureCeMois;
	}

	public void setNonbrefactureCeMois(float nonbrefactureCeMois) {
		this.nonbrefactureCeMois = nonbrefactureCeMois;
	}

	public float getNonbrefactureCetteSemaine() {
		return nonbrefactureCetteSemaine;
	}

	public void setNonbrefactureCetteSemaine(float nonbrefactureCetteSemaine) {
		this.nonbrefactureCetteSemaine = nonbrefactureCetteSemaine;
	}

	

	public float getNonbrefactureAujourdhui() {
		return nonbrefactureAujourdhui;
	}

	public void setNonbrefactureAujourdhui(float nonbrefactureAujourdhui) {
		this.nonbrefactureAujourdhui = nonbrefactureAujourdhui;
	}

	public float getFactureAujourdhui() {
		return factureAujourdhui;
	}

	public void setFactureAujourdhui(float factureAujourdhui) {
		this.factureAujourdhui = factureAujourdhui;
	}

}
