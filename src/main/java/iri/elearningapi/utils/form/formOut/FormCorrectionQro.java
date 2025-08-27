package iri.elearningapi.utils.form.formOut;

import java.util.ArrayList;
import java.util.List;

import iri.elearningapi.model.courModel.QroEtudiant;
import iri.elearningapi.model.userModel.Etudiant;

public class FormCorrectionQro {
	private Etudiant etudiant;
	private List<QroEtudiant> qroEtudiants= new ArrayList<QroEtudiant>();
	public Etudiant getEtudiant() {
		return etudiant;
	}
	public void setEtudiant(Etudiant etudiant) {
		this.etudiant = etudiant;
	}
	public List<QroEtudiant> getQroEtudiants() {
		return qroEtudiants;
	}
	public void setQroEtudiants(List<QroEtudiant> qroEtudiants) {
		this.qroEtudiants = qroEtudiants;
	}
	
	
}
