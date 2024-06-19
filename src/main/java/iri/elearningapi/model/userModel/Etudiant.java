package iri.elearningapi.model.userModel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import iri.elearningapi.model.courModel.EtudiantChapitre;
import iri.elearningapi.model.courModel.EtudiantModule;


/*
import iri.elearningapi.model.courModel.EtudiantChapitre;
import iri.elearningapi.model.courModel.EtudiantModule;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
*/


/**
 * The persistent class for the etudiant database table.
 * 
 */
@Entity
@NamedQuery(name = "Etudiant.findAll", query = "SELECT e FROM Etudiant e")
public class Etudiant implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_inscription")
	private Date dateInscription;

	@Temporal(TemporalType.DATE)
	@Column(name = "date_naissance")
	private Date dateNaissance;

	@Column(unique = true)
	private String email;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_connexion")
	private Date lastConnexion;
	
	private int statut;

	@Column(name = "lieu_naissance")
	private String lieuNaissance;

	@Column(unique = true)
	private String matricule;

	private String nom;

	private String password;

	@Transient
	private String confirmPassword;
 
	private String prenom;

	private String region;

	@Column(unique = true)
	private String telephone;

	private String profession;

	@Column(name = "chiffre_affaire")
	private float chiffreAffaire;

	@Column(name = "nom_entreprise")
	private String nomEntreprise;

	// bi-directional many-to-one association to EtudiantChapitre
	@OneToMany(mappedBy = "etudiant")
	@JsonIgnore
	private List<EtudiantChapitre> etudiantChapitres;

	// bi-directional many-to-one association to EtudiantModule
	@JsonIgnore
	@OneToMany(mappedBy = "etudiant")
	private List<EtudiantModule> etudiantModules;

	public Etudiant() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDateInscription() {
		return this.dateInscription;
	}

	public void setDateInscription(Date dateInscription) {
		this.dateInscription = dateInscription;
	}

	public Date getDateNaissance() {
		return this.dateNaissance;
	}

	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getLastConnexion() {
		return this.lastConnexion;
	}

	public void setLastConnexion(Date lastConnexion) {
		this.lastConnexion = lastConnexion;
	}

	public String getLieuNaissance() {
		return this.lieuNaissance;
	}

	public void setLieuNaissance(String lieuNaissance) {
		this.lieuNaissance = lieuNaissance;
	}

	public String getMatricule() {
		return this.matricule;
	}

	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPrenom() {
		return this.prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getRegion() {
		return this.region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public List<EtudiantChapitre> getEtudiantChapitres() {
		return this.etudiantChapitres;
	}

	public void setEtudiantChapitres(List<EtudiantChapitre> etudiantChapitres) {
		this.etudiantChapitres = etudiantChapitres;
	}

	public EtudiantChapitre addEtudiantChapitre(EtudiantChapitre etudiantChapitre) {
		getEtudiantChapitres().add(etudiantChapitre);
		etudiantChapitre.setEtudiant(this);

		return etudiantChapitre;
	}

	public EtudiantChapitre removeEtudiantChapitre(EtudiantChapitre etudiantChapitre) {
		getEtudiantChapitres().remove(etudiantChapitre);
		etudiantChapitre.setEtudiant(null);

		return etudiantChapitre;
	}

	public List<EtudiantModule> getEtudiantModules() {
		return this.etudiantModules;
	}

	public void setEtudiantModules(List<EtudiantModule> etudiantModules) {
		this.etudiantModules = etudiantModules;
	}

	public EtudiantModule addEtudiantModule(EtudiantModule etudiantModule) {
		getEtudiantModules().add(etudiantModule);
		etudiantModule.setEtudiant(this);

		return etudiantModule;
	}

	public EtudiantModule removeEtudiantModule(EtudiantModule etudiantModule) {
		getEtudiantModules().remove(etudiantModule);
		etudiantModule.setEtudiant(null);

		return etudiantModule;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public float getChiffreAffaire() {
		return chiffreAffaire;
	}

	public void setChiffreAffaire(float chiffreAffaire) {
		this.chiffreAffaire = chiffreAffaire;
	}

	public String getNomEntreprise() {
		return nomEntreprise;
	}

	public void setNomEntreprise(String nomEntreprise) {
		this.nomEntreprise = nomEntreprise;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public int getStatut() {
		return statut;
	}

	public void setStatut(int statut) {
		this.statut = statut;
	}

}