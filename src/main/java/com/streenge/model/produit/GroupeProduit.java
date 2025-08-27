package com.streenge.model.produit;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the groupe_produit database table.
 * 
 */
@Entity
@Table(name="groupe_produit")
@NamedQuery(name="GroupeProduit.findAll", query="SELECT g FROM GroupeProduit g")
public class GroupeProduit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private int priorite;

	//bi-directional many-to-one association to Groupe
	@ManyToOne
	@JoinColumn(name="id_groupe")
	private Groupe groupe;

	//bi-directional many-to-one association to Produit
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="id_produit")
	private Produit produit;

	public GroupeProduit() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPriorite() {
		return this.priorite;
	}

	public void setPriorite(int priorite) {
		this.priorite = priorite;
	}

	public Groupe getGroupe() {
		return this.groupe;
	}

	public void setGroupe(Groupe groupe) {
		this.groupe = groupe;
	}

	public Produit getProduit() {
		return this.produit;
	}

	public void setProduit(Produit produit) {
		this.produit = produit;
	}

}