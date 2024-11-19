package iri.elearningapi.utils.form.formOut;

import iri.elearningapi.utils.elearningData.Etat;

public class FormChapitre {
	private int idChapitre = 0;
	private int idModule = 0;
	private int ordre = 0;
	private String etat = Etat.ACTIF.name();
	private String titre = "";
	private String titreEn = "";
	private boolean firstTime = true;
	private int totalQcm = 0;
	private int totalQcmValide = 0;
	private String imageURL = "";
	private String titreModule = "";
	
	private int totalQRO = 0;
	private int totalQroRepondu = 0;

	public int getIdChapitre() {
		return idChapitre;
	}

	public void setIdChapitre(int idChapitre) {
		this.idChapitre = idChapitre;
	}

	public int getIdModule() {
		return idModule;
	}

	public void setIdModule(int idModule) {
		this.idModule = idModule;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getTitreEn() {
		return titreEn;
	}

	public void setTitreEn(String titreEn) {
		this.titreEn = titreEn;
	}

	public int getTotalQcm() {
		return totalQcm;
	}

	public void setTotalQcm(int totalQcm) {
		this.totalQcm = totalQcm;
	}

	public int getTotalQcmValide() {
		return totalQcmValide;
	}

	public void setTotalQcmValide(int totalQcmValide) {
		this.totalQcmValide = totalQcmValide;
	}

	
	public boolean isFirstTime() {
		return firstTime;
	}

	public void setFirstTime(boolean firstTime) {
		this.firstTime = firstTime;
	}

	public String getTitreModule() {
		return titreModule;
	}

	public void setTitreModule(String titreModule) {
		this.titreModule = titreModule;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	public int getOrdre() {
		return ordre;
	}

	public void setOrdre(int ordre) {
		this.ordre = ordre;
	}

	public int getTotalQRO() {
		return totalQRO;
	}

	public void setTotalQRO(int totalQRO) {
		this.totalQRO = totalQRO;
	}

	public int getTotalQroRepondu() {
		return totalQroRepondu;
	}

	public void setTotalQroRepondu(int totalQroRepondu) {
		this.totalQroRepondu = totalQroRepondu;
	}

}
