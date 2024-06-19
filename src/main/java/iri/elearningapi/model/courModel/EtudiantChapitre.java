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

//import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * The persistent class for the etudiant_chapitre database table.
 * 
 */
@Entity
@Table(name="etudiant_chapitre")
@NamedQuery(name="EtudiantChapitre.findAll", query="SELECT e FROM EtudiantChapitre e")
public class EtudiantChapitre implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String etat;

	@Column(name="qcm_valide")
	private int qcmValide;

	//bi-directional many-to-one association to Chapitre
	@ManyToOne
	@JoinColumn(name="id_chapitre")
	private Chapitre chapitre;

	//bi-directional many-to-one association to Etudiant
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="id_etudiant")
	private Etudiant etudiant;
	
	@Column(name="date_debut")
	private Date dateDebut;
	
	@Column(name="date_validation_qcm")
	private Date dateValidationQcm;

	public EtudiantChapitre() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEtat() {
		return this.etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	public int getQcmValide() {
		return this.qcmValide;
	}

	public void setQcmValide(int qcmValide) {
		this.qcmValide = qcmValide;
	}

	public Chapitre getChapitre() {
		return this.chapitre;
	}

	public void setChapitre(Chapitre chapitre) {
		this.chapitre = chapitre;
	}

	public Etudiant getEtudiant() {
		return this.etudiant;
	}

	public void setEtudiant(Etudiant etudiant) {
		this.etudiant = etudiant;
	}

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Date getDateValidationQcm() {
		return dateValidationQcm;
	}

	public void setDateValidationQcm(Date dateValidationQcm) {
		this.dateValidationQcm = dateValidationQcm;
	}

}