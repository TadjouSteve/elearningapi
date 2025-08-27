package com.streenge.model.produit;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

//import model.GroupeProduit;


/**
 * The persistent class for the groupe database table.
 * 
 */
@Entity
@NamedQuery(name="Groupe.findAll", query="SELECT g FROM Groupe g")
public class Groupe implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_ajout")
	private Date dateAjout;

	@Lob
	private String description;

	@Column(nullable = false, length = 250)
	private String nom;

	private int priorite;

	//bi-directional many-to-one association to GroupeProduit
	@JsonIgnore
	@OneToMany(mappedBy="groupe", cascade = { CascadeType.ALL })
	private List<GroupeProduit> groupeProduits;

	public Groupe() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDateAjout() {
		return this.dateAjout;
	}

	public void setDateAjout(Date dateAjout) {
		this.dateAjout = dateAjout;
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

	public int getPriorite() {
		return this.priorite;
	}

	public void setPriorite(int priorite) {
		this.priorite = priorite;
	}

	public List<GroupeProduit> getGroupeProduits() {
		return this.groupeProduits;
	}

	public void setGroupeProduits(List<GroupeProduit> groupeProduits) {
		this.groupeProduits = groupeProduits;
	}

	public GroupeProduit addGroupeProduit(GroupeProduit groupeProduit) {
		getGroupeProduits().add(groupeProduit);
		groupeProduit.setGroupe(this);

		return groupeProduit;
	}

	public GroupeProduit removeGroupeProduit(GroupeProduit groupeProduit) {
		getGroupeProduits().remove(groupeProduit);
		groupeProduit.setGroupe(null);

		return groupeProduit;
	}

}