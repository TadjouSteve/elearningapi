package iri.elearningapi.model.userModel;

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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import iri.elearningapi.model.Rapport;
import iri.elearningapi.model.Region;
import iri.elearningapi.model.courModel.EtudiantChapitre;
import iri.elearningapi.model.courModel.EtudiantModule;
import iri.elearningapi.model.courModel.QroEtudiant;

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
	
	@Column(name="password_clear")
	private String passwordClear;

	@Transient
	private String confirmPassword;

	private String prenom;

	// private String region;

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

	@ManyToOne
	@JoinColumn(name = "id_Gamme_Etudiant")
	private GammeEtudiant gammeEtudiant;

	// bi-directional many-to-one association to Region
	@ManyToOne
	@JoinColumn(name = "id_Region")
	private Region region;

	// bi-directional many-to-one association to QroEtudiant
	@JsonIgnore
	@OneToMany(mappedBy = "etudiant")
	private List<QroEtudiant> qroEtudiants;

	// bi-directional many-to-one association to Information
	@JsonIgnore
	@OneToMany(mappedBy = "etudiant",fetch = FetchType.EAGER)
	private List<Information> informations;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_confiramtion")
	private Date dateConfiramtion;

	@JsonIgnore
	@Column(name="lien_confirmation")
	private String lienConfirmation;
	
	@JsonIgnore
	@Column(name="code_change_password")
	private String codeChangePassword;
	
	@JsonIgnore
	@OneToMany(mappedBy="etudiant")
	private List<Rapport> rapports;
	
	//bi-directional many-to-one association to Candidature
	@JsonIgnore
	@OneToMany(mappedBy="etudiant")
	private List<Candidature> candidatures;
	
	private int confirmation;
	
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

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public GammeEtudiant getGammeEtudiant() {
		return gammeEtudiant;
	}

	public void setGammeEtudiant(GammeEtudiant gammeEtudiant) {
		this.gammeEtudiant = gammeEtudiant;
	}

	public List<QroEtudiant> getQroEtudiants() {
		return qroEtudiants;
	}

	public void setQroEtudiants(List<QroEtudiant> qroEtudiants) {
		this.qroEtudiants = qroEtudiants;
	}
	
	public List<Information> getInformations() {
		return this.informations;
	}

	public void setInformations(List<Information> informations) {
		this.informations = informations;
	}

	public Information addInformation(Information information) {
		getInformations().add(information);
		information.setEtudiant(this);

		return information;
	}

	public Information removeInformation(Information information) {
		getInformations().remove(information);
		information.setEtudiant(null);

		return information;
	}

	public Date getDateConfiramtion() {
		return dateConfiramtion;
	}

	public void setDateConfiramtion(Date dateConfiramtion) {
		this.dateConfiramtion = dateConfiramtion;
	}

	public String getLienConfirmation() {
		return lienConfirmation;
	}

	public void setLienConfirmation(String lienConfirmation) {
		this.lienConfirmation = lienConfirmation;
	}

	public int getConfirmation() {
		return confirmation;
	}

	public void setConfirmation(int confirmation) {
		this.confirmation = confirmation;
	}

	public String getPasswordClear() {
		return passwordClear;
	}

	public void setPasswordClear(String passwordClear) {
		this.passwordClear = passwordClear;
	}

	public String getCodeChangePassword() {
		return codeChangePassword;
	}

	public void setCodeChangePassword(String codeChangePassword) {
		this.codeChangePassword = codeChangePassword;
	}

	public List<Rapport> getRapports() {
		return rapports;
	}

	public void setRapports(List<Rapport> rapports) {
		this.rapports = rapports;
	}

	public List<Candidature> getCandidatures() {
		return candidatures;
	}

	public void setCandidatures(List<Candidature> candidatures) {
		this.candidatures = candidatures;
	}

}