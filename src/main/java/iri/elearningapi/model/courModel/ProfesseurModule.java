package iri.elearningapi.model.courModel;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import iri.elearningapi.model.userModel.Professeur;
/*
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
*/


/**
 * The persistent class for the professeur_module database table.
 * 
 */
@Entity
@Table(name="professeur_module")
@NamedQuery(name="ProfesseurModule.findAll", query="SELECT p FROM ProfesseurModule p")
public class ProfesseurModule implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	//bi-directional many-to-one association to Module
	@ManyToOne
	@JoinColumn(name="Moduleid_module")
	private Module module;

	//bi-directional many-to-one association to Professeur
	@ManyToOne
	@JoinColumn(name="Professeurid")
	private Professeur professeur;

	public ProfesseurModule() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Module getModule() {
		return this.module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public Professeur getProfesseur() {
		return this.professeur;
	}

	public void setProfesseur(Professeur professeur) {
		this.professeur = professeur;
	}

}