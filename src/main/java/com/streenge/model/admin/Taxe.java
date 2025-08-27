package com.streenge.model.admin;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the taxe database table.
 * 
 */
@Entity
@Table(name="taxe")
@NamedQuery(name="Taxe.findAll", query="SELECT t FROM Taxe t")
public class Taxe implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(length=30)
	private String abreviation;

	@Column(length=30)
	private String etat;

	@Column(length=50)
	private String nom;

	private float taux;

	public Taxe() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAbreviation() {
		return this.abreviation;
	}

	public void setAbreviation(String abreviation) {
		this.abreviation = abreviation;
	}

	public String getEtat() {
		return this.etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public float getTaux() {
		return this.taux;
	}

	public void setTaux(float taux) {
		this.taux = taux;
	}

}