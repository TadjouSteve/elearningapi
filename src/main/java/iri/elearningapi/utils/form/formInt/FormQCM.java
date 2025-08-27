package iri.elearningapi.utils.form.formInt;

public class FormQCM {
	private int idChapitre = 0;
	private int id = 0;
	private String intitule;
	private String intituleEn;
	private String description;

	public int getIdChapitre() {
		return idChapitre;
	}

	public void setIdChapitre(int idChapitre) {
		this.idChapitre = idChapitre;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIntitule() {
		return intitule;
	}

	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}

	public String getIntituleEn() {
		return intituleEn;
	}

	public void setIntituleEn(String intituleEn) {
		this.intituleEn = intituleEn;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
