package iri.elearningapi.utils.form.formOut;

import java.util.ArrayList;
import java.util.List;

import iri.elearningapi.model.courModel.Module;
import iri.elearningapi.model.userModel.GammeEtudiant;

public class FormViewModule {
	private Module module;
	private int nombreCour=0;
	private int nombreQCM=0;
	
	private List<GammeEtudiant>gammeEtudiants= new ArrayList<GammeEtudiant>();
	
	
	//ceci est la juste pour des facilitation solution pas optimal
	private List<GammeEtudiant>allGammeEtudiants= new ArrayList<GammeEtudiant>();

	
	private List<FormChapitre> chapitres=new ArrayList<FormChapitre>();

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public int getNombreCour() {
		return nombreCour;
	}

	public void setNombreCour(int nombreCour) {
		this.nombreCour = nombreCour;
	}

	public int getNombreQCM() {
		return nombreQCM;
	}

	public void setNombreQCM(int nombreQCM) {
		this.nombreQCM = nombreQCM;
	}

	public List<GammeEtudiant> getGammeEtudiants() {
		return gammeEtudiants;
	}

	public void setGammeEtudiants(List<GammeEtudiant> gammeEtudiants) {
		this.gammeEtudiants = gammeEtudiants;
	}

	public List<FormChapitre> getChapitres() {
		return chapitres;
	}

	public void setChapitres(List<FormChapitre> chapitres) {
		this.chapitres = chapitres;
	}
	
	public void addGammeEtudiant(GammeEtudiant gammeEtudiant) {
		if (gammeEtudiant!=null) {
			if (this.gammeEtudiants==null) {
				this.gammeEtudiants=new ArrayList<GammeEtudiant>();
			}
			
			if (!this.gammeEtudiants.contains(gammeEtudiant)) {
				this.gammeEtudiants.add(gammeEtudiant);
			}
		}
	}
	
	public void addFormChapitre(FormChapitre formChapitre) {
		if (formChapitre!=null) {
			if (this.chapitres==null) {
				this.chapitres=new ArrayList<FormChapitre>();
			}
			
			if (!this.chapitres.contains(formChapitre)) {
				this.chapitres.add(formChapitre);
			}
		}
	}

	public List<GammeEtudiant> getAllGammeEtudiants() {
		return allGammeEtudiants;
	}

	public void setAllGammeEtudiants(List<GammeEtudiant> allGammeEtudiants) {
		this.allGammeEtudiants = allGammeEtudiants;
	}
	
	
}
