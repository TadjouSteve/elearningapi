package com.streenge.model.produit.medicament;

import java.io.Serializable;
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.streenge.model.produit.Produit;


/**
 * The persistent class for the medicament database table.
 * 
 */
@Entity
@NamedQuery(name="Medicament.findAll", query="SELECT m FROM Medicament m")
public class Medicament implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Lob
	private String avertissement;

	@Lob
	private String description;

	@Column(name="nom_commercial")
	private String nomCommercial;

	@Column(name="nom_laboratoire")
	private String nomLaboratoire;

	private String pack;

	@Lob
	private String posologie;

	//bi-directional many-to-one association to ComposantMedicament
	@JsonIgnore
	@OneToMany(mappedBy="medicament", cascade = { CascadeType.ALL })
	private List<ComposantMedicament> composantMedicaments;


	//bi-directional many-to-one association to Produit
	@JsonIgnore
	@OneToMany(mappedBy="medicament")
	private List<Produit> produits;

	public Medicament() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAvertissement() {
		return this.avertissement;
	}

	public void setAvertissement(String avertissement) {
		this.avertissement = avertissement;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNomCommercial() {
		return this.nomCommercial;
	}

	public void setNomCommercial(String nomCommercial) {
		this.nomCommercial = nomCommercial;
	}

	public String getNomLaboratoire() {
		return this.nomLaboratoire;
	}

	public void setNomLaboratoire(String nomLaboratoire) {
		this.nomLaboratoire = nomLaboratoire;
	}

	public String getPack() {
		return this.pack;
	}

	public void setPack(String pack) {
		this.pack = pack;
	}

	public String getPosologie() {
		return this.posologie;
	}

	public void setPosologie(String posologie) {
		this.posologie = posologie;
	}

	public List<ComposantMedicament> getComposantMedicaments() {
		return this.composantMedicaments;
	}

	public void setComposantMedicaments(List<ComposantMedicament> composantMedicaments) {
		this.composantMedicaments = composantMedicaments;
	}

	public ComposantMedicament addComposantMedicament(ComposantMedicament composantMedicament) {
		getComposantMedicaments().add(composantMedicament);
		composantMedicament.setMedicament(this);

		return composantMedicament;
	}

	public ComposantMedicament removeComposantMedicament(ComposantMedicament composantMedicament) {
		getComposantMedicaments().remove(composantMedicament);
		composantMedicament.setMedicament(null);

		return composantMedicament;
	}

	

	public List<Produit> getProduits() {
		return this.produits;
	}

	public void setProduits(List<Produit> produits) {
		this.produits = produits;
	}

	public Produit addProduit(Produit produit) {
		getProduits().add(produit);
		produit.setMedicament(this);

		return produit;
	}

	public Produit removeProduit(Produit produit) {
		getProduits().remove(produit);
		produit.setMedicament(null);

		return produit;
	}

}