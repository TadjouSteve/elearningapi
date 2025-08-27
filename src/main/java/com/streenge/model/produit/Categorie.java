package com.streenge.model.produit;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

import java.util.List;


/**
 * The persistent class for the categorie database table.
 * 
 */
@Data
@Entity
@Table(name="categorie")
@NamedQuery(name="Categorie.findAll", query="SELECT c FROM Categorie c")
public class Categorie implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_categorie", unique=true, nullable=false, length=30)
	private String idCategorie;
	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(length=200)
	private String description;

	@Column(nullable=false, length=50)
	private String nom;

	//bi-directional many-to-one association to Produit
	@JsonIgnore
	@OneToMany(mappedBy="categorie",cascade = {CascadeType.ALL})
	private List<Produit> produits;

	public Categorie() {
	}

	public String getIdCategorie() {
		return this.idCategorie;
	}

	public void setIdCategorie(String idCategorie) {
		this.idCategorie = idCategorie;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public List<Produit> getProduits() {
		return this.produits;
	}

	public void setProduits(List<Produit> produits) {
		this.produits = produits;
	}

	public Produit addProduit(Produit produit) {
		getProduits().add(produit);
		produit.setCategorie(this);

		return produit;
	}

	public Produit removeProduit(Produit produit) {
		getProduits().remove(produit);
		produit.setCategorie(null);
		return produit;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}