package com.streenge.model.produit.medicament;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
//import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the composant database table.
 * 
 */
@Entity
@NamedQuery(name="Composant.findAll", query="SELECT c FROM Composant c")
public class Composant implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_ajout")
	private Date dateAjout;

	@Lob
	private String description;

	@Lob
	private String indication;

	@Column(nullable = false, length = 250)
	private String nom;

	@Lob
	private String precaution;

	//bi-directional many-to-one association to ComposantMedicament
	@JsonIgnore
	@OneToMany(mappedBy="composant", cascade = { CascadeType.ALL })
	private List<ComposantMedicament> composantMedicaments;

	public Composant() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDateAjout() {
		return this.dateAjout;
	}

	public void setDateAjout(Date dateAjout) {
		this.dateAjout = dateAjout;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIndication() {
		return this.indication;
	}

	public void setIndication(String indication) {
		this.indication = indication;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrecaution() {
		return this.precaution;
	}

	public void setPrecaution(String precaution) {
		this.precaution = precaution;
	}

	public List<ComposantMedicament> getComposantMedicaments() {
		return this.composantMedicaments;
	}

	public void setComposantMedicaments(List<ComposantMedicament> composantMedicaments) {
		this.composantMedicaments = composantMedicaments;
	}

	public ComposantMedicament addComposantMedicament(ComposantMedicament composantMedicament) {
		getComposantMedicaments().add(composantMedicament);
		composantMedicament.setComposant(this);

		return composantMedicament;
	}

	public ComposantMedicament removeComposantMedicament(ComposantMedicament composantMedicament) {
		getComposantMedicaments().remove(composantMedicament);
		composantMedicament.setComposant(null);

		return composantMedicament;
	}

}