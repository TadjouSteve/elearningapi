package com.streenge.model.vente.facture;

import java.io.Serializable;

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
import com.streenge.model.admin.Taxe;
import com.streenge.model.produit.Pack;

/**
 * The persistent class for the ligne_facture database table.
 * 
 */
@Entity
@Table(name = "ligne_facture")
@NamedQuery(name = "LigneFacture.findAll", query = "SELECT l FROM LigneFacture l")
public class LigneFacture implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private float cout_UMP;

	@Column(length = 100)
	private String designation;

	private float prix;

	private float quantite;

	private float remise;

	// bi-directional many-to-one association to Facture
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_facture", nullable = false)
	private Facture facture;

	// bi-directional many-to-one association to Pack
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_pack", nullable = false)
	private Pack pack;

	// bi-directional many-to-one association to Taxe
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_taxe")
	private Taxe taxe;

	public LigneFacture() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getCout_UMP() {
		return this.cout_UMP;
	}

	public void setCout_UMP(float cout_UMP) {
		this.cout_UMP = cout_UMP;
	}

	public String getDesignation() {
		return this.designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public float getPrix() {
		return this.prix;
	}

	public void setPrix(float prix) {
		this.prix = prix;
	}

	public float getQuantite() {
		return this.quantite;
	}

	public void setQuantite(float quantite) {
		this.quantite = quantite;
	}

	public float getRemise() {
		return this.remise;
	}

	public void setRemise(float remise) {
		this.remise = remise;
	}

	public Facture getFacture() {
		return this.facture;
	}

	public void setFacture(Facture facture) {
		this.facture = facture;
	}

	public Pack getPack() {
		return this.pack;
	}

	public void setPack(Pack pack) {
		this.pack = pack;
	}

	public Taxe getTaxe() {
		return this.taxe;
	}

	public void setTaxe(Taxe taxe) {
		this.taxe = taxe;
	}

}