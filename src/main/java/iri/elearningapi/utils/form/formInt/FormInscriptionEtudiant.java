package iri.elearningapi.utils.form.formInt;

import iri.elearningapi.model.userModel.Etudiant;
import iri.elearningapi.model.userModel.Information;

public class FormInscriptionEtudiant {
	private Etudiant etudiant;
	private Information information;

	public Etudiant getEtudiant() {
		return etudiant;
	}

	public void setEtudiant(Etudiant etudiant) {
		this.etudiant = etudiant;
	}

	public Information getInformation() {
		return information;
	}

	public void setInformation(Information information) {
		this.information = information;
	}

}
