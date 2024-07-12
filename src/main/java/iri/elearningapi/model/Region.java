package iri.elearningapi.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import iri.elearningapi.model.userModel.Etudiant;
import iri.elearningapi.model.userModel.Professeur;


/**
 * The persistent class for the region database table.
 * 
 */
@Entity
@NamedQuery(name="Region.findAll", query="SELECT r FROM Region r")
public class Region implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String code;

	@Lob
	private String description;

	private String nom;

	//bi-directional many-to-one association to Etudiant
	@JsonIgnore
	@OneToMany(mappedBy="region")
	private List<Etudiant> etudiants;

	//bi-directional many-to-one association to Professeur
	@JsonIgnore
	@OneToMany(mappedBy="region")
	private List<Professeur> professeurs;

	public Region() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public List<Etudiant> getEtudiants() {
		return this.etudiants;
	}

	public void setEtudiants(List<Etudiant> etudiants) {
		this.etudiants = etudiants;
	}

	

	public List<Professeur> getProfesseurs() {
		return this.professeurs;
	}

	public void setProfesseurs(List<Professeur> professeurs) {
		this.professeurs = professeurs;
	}

	public Professeur addProfesseur(Professeur professeur) {
		getProfesseurs().add(professeur);
		professeur.setRegion(this);

		return professeur;
	}

	public Professeur removeProfesseur(Professeur professeur) {
		getProfesseurs().remove(professeur);
		professeur.setRegion(null);

		return professeur;
	}

}