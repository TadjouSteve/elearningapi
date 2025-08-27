package iri.elearningapi.model.userModel;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import iri.elearningapi.model.courModel.GammeEtudiantModule;

/**
 * The persistent class for the gamme_etudiant database table.
 * 
 */
@Entity
@Table(name = "gamme_etudiant")
@NamedQuery(name = "GammeEtudiant.findAll", query = "SELECT g FROM GammeEtudiant g")
public class GammeEtudiant implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String code;

	@Lob
	private String description;

	private String nom;

	private int state;
	
	@Transient
	private int nombreEtudiant;

	// bi-directional many-to-one association to Etudiant
	@JsonIgnore
	@OneToMany(mappedBy = "gammeEtudiant")
	private List<Etudiant> etudiants;

	// bi-directional many-to-one association to GammeEtudiantModule
	@JsonIgnore
	@OneToMany(mappedBy = "gammeEtudiant")
	private List<GammeEtudiantModule> gammeEtudiantModules;

	// bi-directional many-to-one association to GammeEtudiantProfesseur
	@JsonIgnore
	@OneToMany(mappedBy = "gammeEtudiant")
	private List<GammeEtudiantProfesseur> gammeEtudiantProfesseurs;

	public GammeEtudiant() {
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

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
	public List<GammeEtudiantModule> getGammeEtudiantModules() {
		return this.gammeEtudiantModules;
	}

	public void setGammeEtudiantModules(List<GammeEtudiantModule> gammeEtudiantModules) {
		this.gammeEtudiantModules = gammeEtudiantModules;
	}

	public GammeEtudiantModule addGammeEtudiantModule(GammeEtudiantModule gammeEtudiantModule) {
		getGammeEtudiantModules().add(gammeEtudiantModule);
		gammeEtudiantModule.setGammeEtudiant(this);

		return gammeEtudiantModule;
	}

	public GammeEtudiantModule removeGammeEtudiantModule(GammeEtudiantModule gammeEtudiantModule) {
		getGammeEtudiantModules().remove(gammeEtudiantModule);
		gammeEtudiantModule.setGammeEtudiant(null);

		return gammeEtudiantModule;
	}

	public List<GammeEtudiantProfesseur> getGammeEtudiantProfesseurs() {
		return this.gammeEtudiantProfesseurs;
	}

	public void setGammeEtudiantProfesseurs(List<GammeEtudiantProfesseur> gammeEtudiantProfesseurs) {
		this.gammeEtudiantProfesseurs = gammeEtudiantProfesseurs;
	}

	public GammeEtudiantProfesseur addGammeEtudiantProfesseur(GammeEtudiantProfesseur gammeEtudiantProfesseur) {
		getGammeEtudiantProfesseurs().add(gammeEtudiantProfesseur);
		gammeEtudiantProfesseur.setGammeEtudiant(this);

		return gammeEtudiantProfesseur;
	}

	public GammeEtudiantProfesseur removeGammeEtudiantProfesseur(GammeEtudiantProfesseur gammeEtudiantProfesseur) {
		getGammeEtudiantProfesseurs().remove(gammeEtudiantProfesseur);
		gammeEtudiantProfesseur.setGammeEtudiant(null);

		return gammeEtudiantProfesseur;
	}

	public int getNombreEtudiant() {
		return nombreEtudiant;
	}

	public void setNombreEtudiant(int nombreEtudiant) {
		this.nombreEtudiant = nombreEtudiant;
	}

}