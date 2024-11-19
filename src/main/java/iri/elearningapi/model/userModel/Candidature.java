package iri.elearningapi.model.userModel;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * The persistent class for the candidature database table.
 * 
 */
@Entity
@NamedQuery(name = "Candidature.findAll", query = "SELECT c FROM Candidature c")
public class Candidature implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Temporal(TemporalType.DATE)
	private Date date;

	@Transient
	private String email;

	@Lob
	@Column(name = "annee_existance")
	private String anneeExistance;

	@Lob
	private String autoevaluation;

	@Lob
	@Column(name = "business_plan")
	private String businessPlan;

	@Lob
	@Column(name = "canaux_distribution")
	private String canauxDistribution;

	@Lob
	@Column(name = "chiffre_affaire")
	private String chiffreAffaire;

	@Lob
	@Column(name = "cout_fonctionnement")
	private String coutFonctionnement;

	@Lob
	@Column(name = "lien_video")
	private String lienVideo;

	@Lob
	@Column(name = "nom_entreprise")
	private String nomEntreprise;

	@Lob
	@Column(name = "nombre_employe")
	private String nombreEmploye;

	@Lob
	@Column(name = "plan_auto_rentabilite")
	private String planAutoRentabilite;

	@Lob
	@Column(name = "plan_optimisation")
	private String planOptimisation;

	@Lob
	@Column(name = "proposition_valeur")
	private String propositionValeur;

	@Lob
	@Column(name = "reference_client")
	private String referenceClient;

	@Lob
	@Column(name = "relation_client")
	private String relationClient;

	@Lob
	@Column(name = "ressources_cles")
	private String ressourcesCles;

	@Lob
	@Column(name = "secteur_activite")
	private String secteurActivite;

	@Lob
	@Column(name = "segment_clientele")
	private String segmentClientele;

	@Lob
	@Column(name = "source_revenu")
	private String sourceRevenu;

	private String facture;
	
	private String conformite;
	
	// Ajout des nouveaux champs
    @Column(name = "chiffre_affaire_prev", nullable = true)
    private int chiffreAffairePrev = 0;

    @Lob
    @Column(name = "distributeur")
    private String distributeur;

    @Lob
    @Column(name = "politique_autorentabilite")
    private String politiqueAutorentabilite;

    @Lob
    @Column(name = "politique_fonctionnement")
    private String politiqueFonctionnement;

    @Lob
    @Column(name = "rencontre_investisseur")
    private String rencontreInvestisseur;

    @Lob
    @Column(name = "type_investissement")
    private String typeInvestissement;

	// bi-directional many-to-one association to Etudiant
	@ManyToOne
	@JoinColumn(name = "id_etudiant")
	private Etudiant etudiant;

	@Transient
	private String matricule;

	public Candidature() {
	}

	public String getAnneeExistance() {
		return this.anneeExistance;
	}

	public void setAnneeExistance(String anneeExistance) {
		this.anneeExistance = anneeExistance;
	}

	public String getAutoevaluation() {
		return this.autoevaluation;
	}

	public void setAutoevaluation(String autoevaluation) {
		this.autoevaluation = autoevaluation;
	}

	public String getBusinessPlan() {
		return this.businessPlan;
	}

	public void setBusinessPlan(String businessPlan) {
		this.businessPlan = businessPlan;
	}

	public String getCanauxDistribution() {
		return this.canauxDistribution;
	}

	public void setCanauxDistribution(String canauxDistribution) {
		this.canauxDistribution = canauxDistribution;
	}

	public String getChiffreAffaire() {
		return this.chiffreAffaire;
	}

	public void setChiffreAffaire(String chiffreAffaire) {
		this.chiffreAffaire = chiffreAffaire;
	}

	public String getCoutFonctionnement() {
		return this.coutFonctionnement;
	}

	public void setCoutFonctionnement(String coutFonctionnement) {
		this.coutFonctionnement = coutFonctionnement;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLienVideo() {
		return this.lienVideo;
	}

	public void setLienVideo(String lienVideo) {
		this.lienVideo = lienVideo;
	}

	public String getNomEntreprise() {
		return this.nomEntreprise;
	}

	public void setNomEntreprise(String nomEntreprise) {
		this.nomEntreprise = nomEntreprise;
	}

	public String getNombreEmploye() {
		return this.nombreEmploye;
	}

	public void setNombreEmploye(String nombreEmploye) {
		this.nombreEmploye = nombreEmploye;
	}

	public String getPlanAutoRentabilite() {
		return this.planAutoRentabilite;
	}

	public void setPlanAutoRentabilite(String planAutoRentabilite) {
		this.planAutoRentabilite = planAutoRentabilite;
	}

	public String getPlanOptimisation() {
		return this.planOptimisation;
	}

	public void setPlanOptimisation(String planOptimisation) {
		this.planOptimisation = planOptimisation;
	}

	public String getPropositionValeur() {
		return this.propositionValeur;
	}

	public void setPropositionValeur(String propositionValeur) {
		this.propositionValeur = propositionValeur;
	}

	public String getReferenceClient() {
		return this.referenceClient;
	}

	public void setReferenceClient(String referenceClient) {
		this.referenceClient = referenceClient;
	}

	public String getRelationClient() {
		return this.relationClient;
	}

	public void setRelationClient(String relationClient) {
		this.relationClient = relationClient;
	}

	public String getRessourcesCles() {
		return this.ressourcesCles;
	}

	public void setRessourcesCles(String ressourcesCles) {
		this.ressourcesCles = ressourcesCles;
	}

	public String getSecteurActivite() {
		return this.secteurActivite;
	}

	public void setSecteurActivite(String secteurActivite) {
		this.secteurActivite = secteurActivite;
	}

	public String getSegmentClientele() {
		return this.segmentClientele;
	}

	public void setSegmentClientele(String segmentClientele) {
		this.segmentClientele = segmentClientele;
	}

	public String getSourceRevenu() {
		return this.sourceRevenu;
	}

	public void setSourceRevenu(String sourceRevenu) {
		this.sourceRevenu = sourceRevenu;
	}

	public Etudiant getEtudiant() {
		return this.etudiant;
	}

	public void setEtudiant(Etudiant etudiant) {
		this.etudiant = etudiant;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMatricule() {
		return matricule;
	}

	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}

	public String getFacture() {
		return facture;
	}

	public void setFacture(String facture) {
		this.facture = facture;
	}

	public String getConformite() {
		return conformite;
	}

	public void setConformite(String conformite) {
		this.conformite = conformite;
	}

	public int getChiffreAffairePrev() {
		return chiffreAffairePrev;
	}

	public void setChiffreAffairePrev(int chiffreAffairePrev) {
		this.chiffreAffairePrev = chiffreAffairePrev;
	}

	public String getDistributeur() {
		return distributeur;
	}

	public void setDistributeur(String distributeur) {
		this.distributeur = distributeur;
	}

	public String getPolitiqueAutorentabilite() {
		return politiqueAutorentabilite;
	}

	public void setPolitiqueAutorentabilite(String politiqueAutorentabilite) {
		this.politiqueAutorentabilite = politiqueAutorentabilite;
	}

	public String getPolitiqueFonctionnement() {
		return politiqueFonctionnement;
	}

	public void setPolitiqueFonctionnement(String politiqueFonctionnement) {
		this.politiqueFonctionnement = politiqueFonctionnement;
	}

	public String getRencontreInvestisseur() {
		return rencontreInvestisseur;
	}

	public void setRencontreInvestisseur(String rencontreInvestisseur) {
		this.rencontreInvestisseur = rencontreInvestisseur;
	}

	public String getTypeInvestissement() {
		return typeInvestissement;
	}

	public void setTypeInvestissement(String typeInvestissement) {
		this.typeInvestissement = typeInvestissement;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	

}