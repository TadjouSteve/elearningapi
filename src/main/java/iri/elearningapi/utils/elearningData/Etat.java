package iri.elearningapi.utils.elearningData;

public enum Etat {
	ACTIF(1), SUPPRIME(0), DELETE(0),SUSPENDU(-1),INACTIF(-1);

	private final int valeur;

	Etat(int valeur) {
		this.valeur = valeur;
	}

	public int getValeur() {
		return this.valeur;
	}
}
