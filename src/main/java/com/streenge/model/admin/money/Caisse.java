package com.streenge.model.admin.money;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.streenge.model.admin.Stockage;
import com.streenge.model.vente.facture.Paiement;


/**
 * The persistent class for the caisse database table.
 * 
 */
@Entity
@Table(name="caisse")
@NamedQuery(name="Caisse.findAll", query="SELECT c FROM Caisse c")
public class Caisse implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(length=150)
	private String description;

	private float solde;

	//bi-directional many-to-one association to Stockage
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name="Stockageid_stockage", nullable=false)
	private Stockage stockage;

	//bi-directional many-to-one association to HistoriqueCaisse
	@JsonIgnore
	@OneToMany(mappedBy="caisse")
	private List<HistoriqueCaisse> historiqueCaisses;

	//bi-directional many-to-one association to MouvementCaisse
	@JsonIgnore
	@OneToMany(mappedBy="caisse")
	private List<MouvementCaisse> mouvementCaisses;

	//bi-directional many-to-one association to Paiement
	@JsonIgnore
	@OneToMany(mappedBy="caisse")
	private List<Paiement> paiements;

	public Caisse() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getSolde() {
		return this.solde;
	}

	public void setSolde(float solde) {
		this.solde = solde;
	}

	public Stockage getStockage() {
		return this.stockage;
	}

	public void setStockage(Stockage stockage) {
		this.stockage = stockage;
	}

	public List<HistoriqueCaisse> getHistoriqueCaisses() {
		return this.historiqueCaisses;
	}

	public void setHistoriqueCaisses(List<HistoriqueCaisse> historiqueCaisses) {
		this.historiqueCaisses = historiqueCaisses;
	}

	public HistoriqueCaisse addHistoriqueCaiss(HistoriqueCaisse historiqueCaiss) {
		getHistoriqueCaisses().add(historiqueCaiss);
		historiqueCaiss.setCaisse(this);

		return historiqueCaiss;
	}

	public HistoriqueCaisse removeHistoriqueCaiss(HistoriqueCaisse historiqueCaiss) {
		getHistoriqueCaisses().remove(historiqueCaiss);
		historiqueCaiss.setCaisse(null);

		return historiqueCaiss;
	}

	public List<MouvementCaisse> getMouvementCaisses() {
		return this.mouvementCaisses;
	}

	public void setMouvementCaisses(List<MouvementCaisse> mouvementCaisses) {
		this.mouvementCaisses = mouvementCaisses;
	}

	public MouvementCaisse addMouvementCaiss(MouvementCaisse mouvementCaiss) {
		getMouvementCaisses().add(mouvementCaiss);
		mouvementCaiss.setCaisse(this);

		return mouvementCaiss;
	}

	public MouvementCaisse removeMouvementCaiss(MouvementCaisse mouvementCaiss) {
		getMouvementCaisses().remove(mouvementCaiss);
		mouvementCaiss.setCaisse(null);

		return mouvementCaiss;
	}

	public List<Paiement> getPaiements() {
		return this.paiements;
	}

	public void setPaiements(List<Paiement> paiements) {
		this.paiements = paiements;
	}

	public Paiement addPaiement(Paiement paiement) {
		getPaiements().add(paiement);
		paiement.setCaisse(this);

		return paiement;
	}

	public Paiement removePaiement(Paiement paiement) {
		getPaiements().remove(paiement);
		paiement.setCaisse(null);

		return paiement;
	}

}