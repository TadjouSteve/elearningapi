package com.streenge.service.utils.formOut;

import com.streenge.service.utils.formInt.FormPanier;

public class FormBonCommande extends FormPanier {

	private String	statutLivraison	= "";
	private float	quantiteTotal	= 0;
	private float	quantiteLivre	= 0;

	private String	statutPaiement		= "";
	private float	sommeTotal			= 0;
	private float	sommeFacture		= 0;
	private float	restantToFacture	= 0;

	private float quantiteRetourne = 0;

	private FormTable	factures		= new FormTable();
	private FormTable	retourProduits	= new FormTable();
	private FormTable	bonLivraisons	= new FormTable();

	public String getStatutLivraison() {
		return statutLivraison;
	}

	public void setStatutLivraison(String statutLivraison) {
		this.statutLivraison = statutLivraison;
	}

	public float getQuantiteTotal() {
		return quantiteTotal;
	}

	public void setQuantiteTotal(float quantiteTotal) {
		this.quantiteTotal = quantiteTotal;
	}

	public String getStatutPaiement() {
		return statutPaiement;
	}

	public void setStatutPaiement(String statutPaiement) {
		this.statutPaiement = statutPaiement;
	}

	public float getSommeTotal() {
		return sommeTotal;
	}

	public void setSommeTotal(float sommeTotal) {
		this.sommeTotal = sommeTotal;
	}

	public float getSommeFacture() {
		return sommeFacture;
	}

	public void setSommeFacture(float sommefacture) {
		this.sommeFacture = sommefacture;
	}

	public FormTable getFactures() {
		return factures;
	}

	public void setFactures(FormTable factures) {
		this.factures = factures;
	}

	public FormTable getRetourProduits() {
		return retourProduits;
	}

	public void setRetourProduits(FormTable retourProduits) {
		this.retourProduits = retourProduits;
	}

	public FormTable getBonLivraisons() {
		return bonLivraisons;
	}

	public void setBonLivraisons(FormTable bonLivraisons) {
		this.bonLivraisons = bonLivraisons;
	}

	public float getQuantiteLivre() {
		return quantiteLivre;
	}

	public void setQuantiteLivre(float quantiteLivre) {
		this.quantiteLivre = quantiteLivre;
	}

	public float getQuantiteRetourne() {
		return quantiteRetourne;
	}

	public void setQuantiteRetourne(float quantiteRetourne) {
		this.quantiteRetourne = quantiteRetourne;
	}

	public float getRestantToFacture() {
		return restantToFacture;
	}

	public void setRestantToFacture(float restantToFacture) {
		this.restantToFacture = restantToFacture;
	}

}
