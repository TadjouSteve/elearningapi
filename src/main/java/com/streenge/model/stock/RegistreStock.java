package com.streenge.model.stock;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.streenge.model.admin.Stockage;
import com.streenge.model.admin.Utilisateur;
import com.streenge.model.produit.Produit;


/**
 * The persistent class for the registre_stock database table.
 * 
 */
@Entity
@Table(name="registre_stock")
@NamedQuery(name="RegistreStock.findAll", query="SELECT r FROM RegistreStock r")
public class RegistreStock implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name="ajustement_avenir")
	private float ajustementAvenir;

	@Column(name="ajustement_disponibe")
	private float ajustementDisponibe;

	@Column(name="ajustement_reel")
	private float ajustementReel;

	@Column(length=150)
	private String context;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@Column(name="id_document", length=30)
	private String idDocument;

	@Column(name="stock_avenir")
	private float stockAvenir;

	@Column(name="stock_disponible")
	private float stockDisponible;

	@Column(name="stock_reel")
	private float stockReel;

	//bi-directional many-to-one association to Utilisateur
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_utilisateur")
	private Utilisateur utilisateur;

	//bi-directional many-to-one association to Produit
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_produit", nullable=false)
	private Produit produit;

	//bi-directional many-to-one association to Stockage
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_stockage", nullable=false)
	private Stockage stockage;

	public RegistreStock() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getAjustementAvenir() {
		return this.ajustementAvenir;
	}

	public void setAjustementAvenir(float ajustementAvenir) {
		this.ajustementAvenir = ajustementAvenir;
	}

	public float getAjustementDisponibe() {
		return this.ajustementDisponibe;
	}

	public void setAjustementDisponibe(float ajustementDisponibe) {
		this.ajustementDisponibe = ajustementDisponibe;
	}

	public float getAjustementReel() {
		return this.ajustementReel;
	}

	public void setAjustementReel(float ajustementReel) {
		this.ajustementReel = ajustementReel;
	}

	public String getContext() {
		return this.context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getIdDocument() {
		return this.idDocument;
	}

	public void setIdDocument(String idDocument) {
		this.idDocument = idDocument;
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

	public Utilisateur getUtilisateur() {
		return this.utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
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

}