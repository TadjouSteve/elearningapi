package com.streenge.model;

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

import com.streenge.model.admin.Taxe;
import com.streenge.model.produit.Pack;
import com.streenge.model.stock.Livraison;
import com.streenge.model.stock.MouvementStock;
import com.streenge.model.stock.OrdreAchat;
import com.streenge.model.vente.BonCommande;
import com.streenge.model.vente.BonLivraison;
import com.streenge.model.vente.Devis;


/**
 * The persistent class for the ligne_document database table.
 * 
 */
@Entity
@Table(name="ligne_document")
@NamedQuery(name="LigneDocument.findAll", query="SELECT l FROM LigneDocument l")
public class LigneDocument implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private float cout_UMP;

	@Column(length=100)
	private String designation;

	private float prix;

	private float quantite;

	private float remise;

	//bi-directional many-to-one association to BonCommande
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="iid_bon_commande")
	private BonCommande bonCommande;

	//bi-directional many-to-one association to BonLivraison
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_bon_livraison")
	private BonLivraison bonLivraison;

	//bi-directional many-to-one association to Devi
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_devis")
	private Devis devi;

	//bi-directional many-to-one association to Livraison
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_livraison")
	private Livraison livraison;

	//bi-directional many-to-one association to MouvementStock
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_mouvement_stock")
	private MouvementStock mouvementStock;

	//bi-directional many-to-one association to OrdreAchat
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_ordre_achat")
	private OrdreAchat ordreAchat;

	//bi-directional many-to-one association to Pack
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_pack", nullable=false)
	private Pack pack;

	//bi-directional many-to-one association to Taxe
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_taxe")
	private Taxe taxe;

	public LigneDocument() {
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

	public BonCommande getBonCommande() {
		return this.bonCommande;
	}

	public void setBonCommande(BonCommande bonCommande) {
		this.bonCommande = bonCommande;
	}

	public BonLivraison getBonLivraison() {
		return this.bonLivraison;
	}

	public void setBonLivraison(BonLivraison bonLivraison) {
		this.bonLivraison = bonLivraison;
	}

	public Devis getDevi() {
		return this.devi;
	}

	public void setDevi(Devis devi) {
		this.devi = devi;
	}

	public Livraison getLivraison() {
		return this.livraison;
	}

	public void setLivraison(Livraison livraison) {
		this.livraison = livraison;
	}

	public MouvementStock getMouvementStock() {
		return this.mouvementStock;
	}

	public void setMouvementStock(MouvementStock mouvementStock) {
		this.mouvementStock = mouvementStock;
	}

	public OrdreAchat getOrdreAchat() {
		return this.ordreAchat;
	}

	public void setOrdreAchat(OrdreAchat ordreAchat) {
		this.ordreAchat = ordreAchat;
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