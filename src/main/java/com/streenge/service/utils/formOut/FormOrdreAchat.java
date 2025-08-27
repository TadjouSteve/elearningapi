package com.streenge.service.utils.formOut;

import com.streenge.service.utils.formInt.FormPanier;

public class FormOrdreAchat extends FormPanier {
	private String	statutLivraison	= "";
	private float	quantiteTotal	= 0;
	private float	quantiteRestant	= 0;

	private FormTable Livraisons = new FormTable();

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

	public FormTable getLivraisons() {
		return Livraisons;
	}

	public void setLivraisons(FormTable livraisons) {
		Livraisons = livraisons;
	}

	public float getQuantiteRestant() {
		return quantiteRestant;
	}

	public void setQuantiteRestant(float quantiteRestant) {
		this.quantiteRestant = quantiteRestant;
	}

}
