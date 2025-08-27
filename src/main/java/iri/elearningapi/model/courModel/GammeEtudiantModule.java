package iri.elearningapi.model.courModel;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import iri.elearningapi.model.userModel.GammeEtudiant;


/**
 * The persistent class for the gamme_etudiant_module database table.
 * 
 */
@Entity
@Table(name="gamme_etudiant_module")
@NamedQuery(name="GammeEtudiantModule.findAll", query="SELECT g FROM GammeEtudiantModule g")
public class GammeEtudiantModule implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	//bi-directional many-to-one association to GammeEtudiant
	@ManyToOne
	@JoinColumn(name="id_Gamme_Etudiant")
	private GammeEtudiant gammeEtudiant;

	//bi-directional many-to-one association to Module
	@ManyToOne
	@JoinColumn(name="id_module")
	private Module module;

	public GammeEtudiantModule() {
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

	public Module getModule() {
		return this.module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

}