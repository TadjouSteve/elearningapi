package iri.elearningapi.utils.form.formOut;

public class FormChapitre {
	private int		idChapitre		= 0;
	private int		idModule		= 0;
	private String	titre			= "";
	private String	titreEn			= "";
	private boolean	firstTime		= true;
	private int		totalQcm		= 0;
	private int		totalQcmValide	= 0;
	private String	image			= "";

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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean isFirstTime() {
		return firstTime;
	}

	public void setFirstTime(boolean firstTime) {
		this.firstTime =firstTime;
	}

}
