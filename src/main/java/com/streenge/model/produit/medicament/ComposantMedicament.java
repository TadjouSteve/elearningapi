package com.streenge.model.produit.medicament;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the composant_medicament database table.
 * 
 */
@Entity
@Table(name="composant_medicament")
@NamedQuery(name="ComposantMedicament.findAll", query="SELECT c FROM ComposantMedicament c")
public class ComposantMedicament implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String dosage;

	//bi-directional many-to-one association to Composant
	@ManyToOne
	@JoinColumn(name="id_Composant")
	private Composant composant;

	//bi-directional many-to-one association to Medicament
	@ManyToOne
	@JoinColumn(name="id_Medicament")
	private Medicament medicament;

	public ComposantMedicament() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDosage() {
		return this.dosage;
	}

	public void setDosage(String dosage) {
		this.dosage = dosage;
	}

	public Composant getComposant() {
		return this.composant;
	}

	public void setComposant(Composant composant) {
		this.composant = composant;
	}

	public Medicament getMedicament() {
		return this.medicament;
	}

	public void setMedicament(Medicament medicament) {
		this.medicament = medicament;
	}

}