package iri.elearningapi.utils.form.formOut;

import java.util.ArrayList;
import java.util.List;

public class FormModule {
	private int idModule;
	private String titre = "";
	private String titreEn = "";
	
	private List<FormChapitre> chapitres=new ArrayList<>();

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

	public List<FormChapitre> getChapitres() {
		return chapitres;
	}

	public void setChapitres(List<FormChapitre> chapitres) {
		this.chapitres = chapitres;
	}
	
	
}
