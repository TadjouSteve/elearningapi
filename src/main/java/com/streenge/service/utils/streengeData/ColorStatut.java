package com.streenge.service.utils.streengeData;

public class ColorStatut {

	private static String	terminer	= "green";
	private static String	partiel		= "#FF8C00";
	private static String	annuler		= "red";

	public static String getColor(String statut) {
		String color = partiel;
		if (Statut.getPaye().equals(statut) || Statut.getLivre().equals(statut)
				|| Statut.getAcceptertransformer().equals(statut) || Statut.getAccepter().equals(statut)
				|| Statut.getTerminer().equals(statut) || Statut.getCreditteur().equals(statut)
				|| Type.DEPOT.equals(statut)) {
			color = terminer;
		} else if (Statut.getAnnuler().equals(statut) || Statut.getRejeter().equals(statut)
				|| Statut.getBloquer().equals(statut) || Statut.getDebiteur().equals(statut)
				|| Type.RETRAIT.equals(statut)) {
			color = annuler;
		}

		return color;
	}

}
