package com.streenge.service.utils.streengeData;

public class Statut {

	// Ne pas changer ces valeurs tres dangereux
	// Je vous previens vous risquer avoir des erreurs bizzares
	private static final String	ancienClient	= "Ancien Client";
	private static final String	nouveauClient	= "Nouveau Client";
	private static final String	prospect		= "Prospect";

	private static final String	packPrincipal	= "principal";
	private static final String	packUnitaire	= "unitaire";

	private static final String	nonPaye				= "Non payé";
	private static final String	PartiellementPaye	= "Partiellement payé";
	private static final String	Paye				= "Payé";
	private static final String	annuler				= "Annulé";

	private static final String	nonLivre			= "Non livré";
	private static final String	EnPreparation		= "En préparation";
	private static final String	PartiellementLivre	= "Partiellement livré";
	private static final String	Livre				= "Livré";

	private static final String	EnAttente			= "En attente";
	private static final String	rejeter				= "Rejeté";
	private static final String	accepter			= "Accepté";
	private static final String	accepterTransformer	= "Accepté et transformé";

	private static final String	Actif		= "Actif";
	private static final String	Terminer	= "Terminé";

	private static final String	Creditteur	= "Créditeur";
	private static final String	Debiteur	= "Débiteur";

	private static final String Bloquer = "Bloqué";

	public static String getAncienclient() {
		return ancienClient;
	}

	public static String getNouveauclient() {
		return nouveauClient;
	}

	public static String getNonpaye() {
		return nonPaye;
	}

	public static String getPartiellementpaye() {
		return PartiellementPaye;
	}

	public static String getPaye() {
		return Paye;
	}

	public static String getAnnuler() {
		return annuler;
	}

	public static String getNonlivre() {
		return nonLivre;
	}

	public static String getPartiellementlivre() {
		return PartiellementLivre;
	}

	public static String getLivre() {
		return Livre;
	}

	public String getAncienClient() {
		return ancienClient;
	}

	public static String getNouveauClient() {
		return nouveauClient;
	}

	public static String getProspect() {
		return prospect;
	}

	public static String getPackprincipal() {
		return packPrincipal;
	}

	public static String getPackunitaire() {
		return packUnitaire;
	}

	public static String getEnattente() {
		return EnAttente;
	}

	public static String getAccepter() {
		return accepter;
	}

	public static String getAcceptertransformer() {
		return accepterTransformer;
	}

	public static String getRejeter() {
		return rejeter;
	}

	public static String getActif() {
		return Actif;
	}

	public static String getTerminer() {
		return Terminer;
	}

	public static String getEnpreparation() {
		return EnPreparation;
	}

	public static String getBloquer() {
		return Bloquer;
	}

	public static String getCreditteur() {
		return Creditteur;
	}

	public static String getDebiteur() {
		return Debiteur;
	}

}
