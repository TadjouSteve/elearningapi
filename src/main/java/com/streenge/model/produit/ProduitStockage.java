package com.streenge.model.produit;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.streenge.model.admin.Stockage;

/**
 * The persistent class for the produit_stockage database table.
 * 
 */
@Entity
@Table(name = "produit_stockage")
@NamedQuery(name = "ProduitStockage.findAll", query = "SELECT p FROM ProduitStockage p")
public class ProduitStockage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private int id;

	@Column(length = 100)
	private String emplacement;

	@Column(name = "point_commande")
	private float pointCommande = 0;

	@Column(name = "stock_avenir")
	private float stockAvenir;

	@Column(name = "stock_disponible")
	private float stockDisponible;

	@Column(name = "stock_reel")
	private float stockReel;

	// bi-directional many-to-one association to Produit
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "id_produit", nullable = false)
	private Produit produit;

	// bi-directional many-to-one association to Stockage
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "id_stockage", nullable = false)
	private Stockage stockage;

	public ProduitStockage() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmplacement() {
		return this.emplacement;
	}

	public void setEmplacement(String emplacement) {
		this.emplacement = emplacement;
	}

	public float getStockAvenir() {
		return this.stockAvenir;
	}

	public void setStockAvenir(float stockAvenir) {
		this.stockAvenir = stockAvenir;
	}

	public float getStockDisponible() {
		return this.stockDisponible;
	}

	public void setStockDisponible(float stockDisponible) {
		this.stockDisponible = stockDisponible;
	}

	public float getStockReel() {
		return this.stockReel;
	}

	public void setStockReel(float stockReel) {
		this.stockReel = stockReel;
	}

	public Produit getProduit() {
		return this.produit;
	}

	public void setProduit(Produit produit) {
		this.produit = produit;
	}

	public Stockage getStockage() {
		return this.stockage;
	}

	public void setStockage(Stockage stockage) {
		this.stockage = stockage;
	}

	public float getPointCommande() {
		return pointCommande;
	}

	public void setPointCommande(float pointCommande) {
		this.pointCommande = pointCommande;
	}

}