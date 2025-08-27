package iri.elearningapi.model.courModel;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import iri.elearningapi.model.userModel.Etudiant;

/*
import jakarta.persistence.Column;
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
 * The persistent class for the etudiant_module database table.
 * 
 */
@Entity
@Table(name="etudiant_module")
@NamedQuery(name="EtudiantModule.findAll", query="SELECT e FROM EtudiantModule e")
public class EtudiantModule implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	//bi-directional many-to-one association to Etudiant
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="id_etudiant")
	private Etudiant etudiant;

	//bi-directional many-to-one association to Module
	@ManyToOne
	@JoinColumn(name="id_module")
	private Module module;

	
	@Column(name="date_debut")
	private Date dateDebut;

	public EtudiantModule() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Etudiant getEtudiant() {
		return this.etudiant;
	}

	public void setEtudiant(Etudiant etudiant) {
		this.etudiant = etudiant;
	}

	public Module getModule() {
		return this.module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

}