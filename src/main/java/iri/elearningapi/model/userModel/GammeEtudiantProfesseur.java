package iri.elearningapi.model.userModel;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the gamme_etudiant_professeur database table.
 * 
 */
@Entity
@Table(name="gamme_etudiant_professeur")
@NamedQuery(name="GammeEtudiantProfesseur.findAll", query="SELECT g FROM GammeEtudiantProfesseur g")
public class GammeEtudiantProfesseur implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	//bi-directional many-to-one association to GammeEtudiant
	@ManyToOne
	@JoinColumn(name="id_Gamme_Etudiant")
	private GammeEtudiant gammeEtudiant;

	//bi-directional many-to-one association to Professeur
	@ManyToOne
	@JoinColumn(name="id_Professeur")
	private Professeur professeur;

	public GammeEtudiantProfesseur() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public GammeEtudiant getGammeEtudiant() {
		return this.gammeEtudiant;
	}

	public void setGammeEtudiant(GammeEtudiant gammeEtudiant) {
		this.gammeEtudiant = gammeEtudiant;
	}

	public Professeur getProfesseur() {
		return this.professeur;
	}

	public void setProfesseur(Professeur professeur) {
		this.professeur = professeur;
	}

}