package com.streenge.model.depense;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.streenge.model.admin.Utilisateur;


/**
 * The persistent class for the point_depense database table.
 * 
 */
@Entity
@Table(name="point_depense")
@NamedQuery(name="PointDepense.findAll", query="SELECT p FROM PointDepense p")
public class PointDepense implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name="cout_moyen")
	private float coutMoyen;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_ajout")
	private Date dateAjout;

	@Lob
	private String description;

	@Column(length=20)
	private String etat;

	@Column(length=100)
	private String nom;

	@Column(length=30)
	private String reference;

	@Column(length=20)
	private String statut;
	
	@Column(name="user_level")
	private int userLevel;

	//bi-directional many-to-one association to LigneDepense
	@JsonIgnore
	@OneToMany(mappedBy="pointDepense")
	private List<LigneDepense> ligneDepenses;
	
	//bi-directional many-to-one association to Utilisateur
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_utilisateur_alter")
	private Utilisateur utilisateurAlter;
	
	@Transient
	private String profilMin;

	public PointDepense() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getCoutMoyen() {
		return this.coutMoyen;
	}

	public void setCoutMoyen(float coutMoyen) {
		this.coutMoyen = coutMoyen;
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
	
	public int getUserLevel() {
		return this.userLevel;
	}

	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}

	public String getEtat() {
		return this.etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getReference() {
		return this.reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getStatut() {
		return this.statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public List<LigneDepense> getLigneDepenses() {
		return this.ligneDepenses;
	}

	public void setLigneDepenses(List<LigneDepense> ligneDepenses) {
		this.ligneDepenses = ligneDepenses;
	}

	public LigneDepense addLigneDepens(LigneDepense ligneDepens) {
		getLigneDepenses().add(ligneDepens);
		ligneDepens.setPointDepense(this);

		return ligneDepens;
	}

	public LigneDepense removeLigneDepens(LigneDepense ligneDepens) {
		getLigneDepenses().remove(ligneDepens);
		ligneDepens.setPointDepense(null);

		return ligneDepens;
	}

	public Utilisateur getUtilisateurAlter() {
		return utilisateurAlter;
	}

	public void setUtilisateurAlter(Utilisateur utilisateurAlter) {
		this.utilisateurAlter = utilisateurAlter;
	}

	public String getProfilMin() {
		return profilMin;
	}

	public void setProfilMin(String profilMin) {
		this.profilMin = profilMin;
	}
	
}