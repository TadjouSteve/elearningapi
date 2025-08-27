package iri.elearningapi.model.userModel;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the information database table.
 * 
 */
@Entity
@NamedQuery(name="Information.findAll", query="SELECT i FROM Information i")
public class Information implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Lob
	@Column(name="amelioration_durant")
	private String ameliorationDurant;

	@Column(name="annee_entreprise")
	private int anneeEntreprise;

	@Column(name="annee_experience")
	private int anneeExperience;

	@Lob
	@Column(name="autre_commentaire")
	private String autreCommentaire;

	@Lob
	@Column(name="bien_fonde_universitaire")
	private String bienFondeUniversitaire;

	@Lob
	@Column(name="chiffre_affaire")
	private String chiffreAffaire;

	@Lob
	@Column(name="connaissance_marche_local")
	private String connaissanceMarcheLocal;

	@Lob
	@Column(name="definition_entrepreneuriat")
	private String definitionEntrepreneuriat;

	@Lob
	private String departement;

	@Lob
	@Column(name="disposer_accompagnement")
	private String disposerAccompagnement;

	@Lob
	@Column(name="domaine_activite")
	private String domaineActivite;

	@Lob
	@Column(name="domaine_famille")
	private String domaineFamille;

	@Lob
	@Column(name="entreprise_creer")
	private String entrepriseCreer;

	@Lob
	@Column(name="etudiant_entrepreneur")
	private String etudiantEntrepreneur;

	@Lob
	private String faculte;

	@Lob
	private String filiere;

	@Lob
	private String fonction;

	@Lob
	@Column(name="format_formation")
	private String formatFormation;

	@Lob
	private String grade;

	@Lob
	@Column(name="idee_projet")
	private String ideeProjet;

	@Lob
	@Column(name="marche_cible")
	private String marcheCible;

	@Column(name="menbre_famille")
	private int menbreFamille;

	@Lob
	private String motivation;

	@Lob
	@Column(name="niveau_academique")
	private String niveauAcademique;

	@Lob
	@Column(name="niveau_connaissance")
	private String niveauConnaissance;

	@Lob
	@Column(name="niveau_connexion")
	private String niveauConnexion;

	@Lob
	@Column(name="niveau_etude_famille")
	private String niveauEtudeFamille;

	@Lob
	@Column(name="nom_entreprise")
	private String nomEntreprise;

	@Lob
	@Column(name="nombre_entrepreneur_famille")
	private String nombreEntrepreneurFamille;

	@Lob
	@Column(name="obstacle_famille")
	private String obstacleFamille;

	@Lob
	@Column(name="preference_horaire")
	private String preferenceHoraire;

	@Lob
	private String profession;

	@Lob
	@Column(name="secteur_activite")
	private String secteurActivite;

	@Lob
	private String sexe;

	@Lob
	@Column(name="souhait_acquisition_durant")
	private String souhaitAcquisitionDurant;

	@Lob
	@Column(name="souhait_acquisition_fin")
	private String souhaitAcquisitionFin;

	@Lob
	@Column(name="souhait_formation")
	private String souhaitFormation;

	@Lob
	@Column(name="taille_entreprise")
	private String tailleEntreprise;

	@Lob
	@Column(name="tranche_age")
	private String trancheAge;

	@Lob
	private String universite;

	//bi-directional many-to-one association to Etudiant
	@ManyToOne
	@JoinColumn(name="id_etudiant")
	private Etudiant etudiant;

	public Information() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAmeliorationDurant() {
		return this.ameliorationDurant;
	}

	public void setAmeliorationDurant(String ameliorationDurant) {
		this.ameliorationDurant = ameliorationDurant;
	}

	public int getAnneeEntreprise() {
		return this.anneeEntreprise;
	}

	public void setAnneeEntreprise(int anneeEntreprise) {
		this.anneeEntreprise = anneeEntreprise;
	}

	public int getAnneeExperience() {
		return this.anneeExperience;
	}

	public void setAnneeExperience(int anneeExperience) {
		this.anneeExperience = anneeExperience;
	}

	public String getAutreCommentaire() {
		return this.autreCommentaire;
	}

	public void setAutreCommentaire(String autreCommentaire) {
		this.autreCommentaire = autreCommentaire;
	}

	public String getBienFondeUniversitaire() {
		return this.bienFondeUniversitaire;
	}

	public void setBienFondeUniversitaire(String bienFondeUniversitaire) {
		this.bienFondeUniversitaire = bienFondeUniversitaire;
	}

	public String getChiffreAffaire() {
		return this.chiffreAffaire;
	}

	public void setChiffreAffaire(String chiffreAffaire) {
		this.chiffreAffaire = chiffreAffaire;
	}

	public String getConnaissanceMarcheLocal() {
		return this.connaissanceMarcheLocal;
	}

	public void setConnaissanceMarcheLocal(String connaissanceMarcheLocal) {
		this.connaissanceMarcheLocal = connaissanceMarcheLocal;
	}

	public String getDefinitionEntrepreneuriat() {
		return this.definitionEntrepreneuriat;
	}

	public void setDefinitionEntrepreneuriat(String definitionEntrepreneuriat) {
		this.definitionEntrepreneuriat = definitionEntrepreneuriat;
	}

	public String getDepartement() {
		return this.departement;
	}

	public void setDepartement(String departement) {
		this.departement = departement;
	}

	public String getDisposerAccompagnement() {
		return this.disposerAccompagnement;
	}

	public void setDisposerAccompagnement(String disposerAccompagnement) {
		this.disposerAccompagnement = disposerAccompagnement;
	}

	public String getDomaineActivite() {
		return this.domaineActivite;
	}

	public void setDomaineActivite(String domaineActivite) {
		this.domaineActivite = domaineActivite;
	}

	public String getDomaineFamille() {
		return this.domaineFamille;
	}

	public void setDomaineFamille(String domaineFamille) {
		this.domaineFamille = domaineFamille;
	}

	public String getEntrepriseCreer() {
		return this.entrepriseCreer;
	}

	public void setEntrepriseCreer(String entrepriseCreer) {
		this.entrepriseCreer = entrepriseCreer;
	}

	public String getEtudiantEntrepreneur() {
		return this.etudiantEntrepreneur;
	}

	public void setEtudiantEntrepreneur(String etudiantEntrepreneur) {
		this.etudiantEntrepreneur = etudiantEntrepreneur;
	}

	public String getFaculte() {
		return this.faculte;
	}

	public void setFaculte(String faculte) {
		this.faculte = faculte;
	}

	public String getFiliere() {
		return this.filiere;
	}

	public void setFiliere(String filiere) {
		this.filiere = filiere;
	}

	public String getFonction() {
		return this.fonction;
	}

	public void setFonction(String fonction) {
		this.fonction = fonction;
	}

	public String getFormatFormation() {
		return this.formatFormation;
	}

	public void setFormatFormation(String formatFormation) {
		this.formatFormation = formatFormation;
	}

	public String getGrade() {
		return this.grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getIdeeProjet() {
		return this.ideeProjet;
	}

	public void setIdeeProjet(String ideeProjet) {
		this.ideeProjet = ideeProjet;
	}

	public String getMarcheCible() {
		return this.marcheCible;
	}

	public void setMarcheCible(String marcheCible) {
		this.marcheCible = marcheCible;
	}

	public int getMenbreFamille() {
		return this.menbreFamille;
	}

	public void setMenbreFamille(int menbreFamille) {
		this.menbreFamille = menbreFamille;
	}

	public String getMotivation() {
		return this.motivation;
	}

	public void setMotivation(String motivation) {
		this.motivation = motivation;
	}

	public String getNiveauAcademique() {
		return this.niveauAcademique;
	}

	public void setNiveauAcademique(String niveauAcademique) {
		this.niveauAcademique = niveauAcademique;
	}

	public String getNiveauConnaissance() {
		return this.niveauConnaissance;
	}

	public void setNiveauConnaissance(String niveauConnaissance) {
		this.niveauConnaissance = niveauConnaissance;
	}

	public String getNiveauConnexion() {
		return this.niveauConnexion;
	}

	public void setNiveauConnexion(String niveauConnexion) {
		this.niveauConnexion = niveauConnexion;
	}

	public String getNiveauEtudeFamille() {
		return this.niveauEtudeFamille;
	}

	public void setNiveauEtudeFamille(String niveauEtudeFamille) {
		this.niveauEtudeFamille = niveauEtudeFamille;
	}

	public String getNomEntreprise() {
		return this.nomEntreprise;
	}

	public void setNomEntreprise(String nomEntreprise) {
		this.nomEntreprise = nomEntreprise;
	}

	public String getNombreEntrepreneurFamille() {
		return this.nombreEntrepreneurFamille;
	}

	public void setNombreEntrepreneurFamille(String nombreEntrepreneurFamille) {
		this.nombreEntrepreneurFamille = nombreEntrepreneurFamille;
	}

	public String getObstacleFamille() {
		return this.obstacleFamille;
	}

	public void setObstacleFamille(String obstacleFamille) {
		this.obstacleFamille = obstacleFamille;
	}

	public String getPreferenceHoraire() {
		return this.preferenceHoraire;
	}

	public void setPreferenceHoraire(String preferenceHoraire) {
		this.preferenceHoraire = preferenceHoraire;
	}

	public String getProfession() {
		return this.profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getSecteurActivite() {
		return this.secteurActivite;
	}

	public void setSecteurActivite(String secteurActivite) {
		this.secteurActivite = secteurActivite;
	}

	public String getSexe() {
		return this.sexe;
	}

	public void setSexe(String sexe) {
		this.sexe = sexe;
	}

	public String getSouhaitAcquisitionDurant() {
		return this.souhaitAcquisitionDurant;
	}

	public void setSouhaitAcquisitionDurant(String souhaitAcquisitionDurant) {
		this.souhaitAcquisitionDurant = souhaitAcquisitionDurant;
	}

	public String getSouhaitAcquisitionFin() {
		return this.souhaitAcquisitionFin;
	}

	public void setSouhaitAcquisitionFin(String souhaitAcquisitionFin) {
		this.souhaitAcquisitionFin = souhaitAcquisitionFin;
	}

	public String getSouhaitFormation() {
		return this.souhaitFormation;
	}

	public void setSouhaitFormation(String souhaitFormation) {
		this.souhaitFormation = souhaitFormation;
	}

	public String getTailleEntreprise() {
		return this.tailleEntreprise;
	}

	public void setTailleEntreprise(String tailleEntreprise) {
		this.tailleEntreprise = tailleEntreprise;
	}

	public String getTrancheAge() {
		return this.trancheAge;
	}

	public void setTrancheAge(String trancheAge) {
		this.trancheAge = trancheAge;
	}

	public String getUniversite() {
		return this.universite;
	}

	public void setUniversite(String universite) {
		this.universite = universite;
	}

	public Etudiant getEtudiant() {
		return this.etudiant;
	}

	public void setEtudiant(Etudiant etudiant) {
		this.etudiant = etudiant;
	}

}