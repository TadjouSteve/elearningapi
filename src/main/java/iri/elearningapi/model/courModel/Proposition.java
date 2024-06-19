package iri.elearningapi.model.courModel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

import com.fasterxml.jackson.annotation.JsonIgnore;

/*
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
*/



/**
 * The persistent class for the proposition database table.
 * 
 */
@Entity
@NamedQuery(name="Proposition.findAll", query="SELECT p FROM Proposition p")
public class Proposition implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	
	private int etat;

	@Lob
	private String valeur;

	@Lob
	@Column(name="valeur_en")
	private String valeurEn;

	//bi-directional many-to-one association to Qcm
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="id_QCM")
	private Qcm qcm;

	public Proposition() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEtat() {
		return this.etat;
	}

	public void setEtat(int etat) {
		this.etat = etat;
	}

	public String getValeur() {
		return this.valeur;
	}

	public void setValeur(String valeur) {
		this.valeur = valeur;
	}

	public String getValeurEn() {
		return this.valeurEn;
	}

	public void setValeurEn(String valeurEn) {
		this.valeurEn = valeurEn;
	}

	public Qcm getQcm() {
		return this.qcm;
	}

	public void setQcm(Qcm qcm) {
		this.qcm = qcm;
	}

}