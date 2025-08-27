package com.streenge.model.vente.facture;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the methode_paiement database table.
 * 
 */
@Entity
@Table(name="methode_paiement")
@NamedQuery(name="MethodePaiement.findAll", query="SELECT m FROM MethodePaiement m")
public class MethodePaiement implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(length=150)
	private String description;

	@Column(length=50)
	private String nom;

	//bi-directional many-to-one association to Paiement
	@OneToMany(mappedBy="methodePaiement", cascade = {CascadeType.ALL})
	private List<Paiement> paiements;

	public MethodePaiement() {
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

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public List<Paiement> getPaiements() {
		return this.paiements;
	}

	public void setPaiements(List<Paiement> paiements) {
		this.paiements = paiements;
	}

	public Paiement addPaiement(Paiement paiement) {
		getPaiements().add(paiement);
		paiement.setMethodePaiement(this);

		return paiement;
	}

	public Paiement removePaiement(Paiement paiement) {
		getPaiements().remove(paiement);
		paiement.setMethodePaiement(null);

		return paiement;
	}

}