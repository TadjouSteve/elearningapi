package com.streenge.model.admin;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the utilisateur_stockage database table.
 * 
 */
@Entity
@Table(name="utilisateur_stockage")
@NamedQuery(name="UtilisateurStockage.findAll", query="SELECT u FROM UtilisateurStockage u")
public class UtilisateurStockage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int id;

	//bi-directional many-to-one association to Stockage
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_stockage")
	private Stockage stockage;

	//bi-directional many-to-one association to Utilisateur
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_Utilisateur")
	private Utilisateur utilisateur;

	public UtilisateurStockage() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Stockage getStockage() {
		return this.stockage;
	}

	public void setStockage(Stockage stockage) {
		this.stockage = stockage;
	}

	public Utilisateur getUtilisateur() {
		return this.utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

}