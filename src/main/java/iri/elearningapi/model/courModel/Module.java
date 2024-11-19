package iri.elearningapi.model.courModel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the module database table.
 * 
 */
@Entity
@NamedQuery(name="Module.findAll", query="SELECT m FROM Module m")
public class Module implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_module")
	private int idModule;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_Deblocage")
	private Date dateDeblocage;

	@Lob
	private String description;

	@Column(name="description_en")
	private String descriptionEn;

	private String etat;

	@Column(name="nom_image")
	private String nomImage;

	private String titre;

	@Column(name="titre_en")
	private String titreEn;

	//bi-directional many-to-one association to Chapitre
	@JsonIgnore
	@OneToMany(mappedBy="module")
	private List<Chapitre> chapitres;

	//bi-directional many-to-one association to EtudiantModule
	@OneToMany(mappedBy="module")
	@JsonIgnore
	private List<EtudiantModule> etudiantModules;


	//bi-directional many-to-one association to Libelle
	@OneToMany(mappedBy="module")
	private List<Libelle> libelles;

	//bi-directional many-to-one association to ProfesseurModule
	@OneToMany(mappedBy="module")
	@JsonIgnore
	private List<ProfesseurModule> professeurModules;

	//bi-directional many-to-one association to QuestionCour
	@OneToMany(mappedBy="module")
	@JsonIgnore
	private List<QuestionCour> questionCours;
	
	
	//bi-directional many-to-one association to GammeEtudiantModule
	@JsonIgnore
	@OneToMany(mappedBy="module")
	private List<GammeEtudiantModule> gammeEtudiantModules;

	
	@Transient
	private Boolean fistTime=false;
	
	@Transient
	private  Boolean isAccessible;
	
	@Transient
	private  int totalChapitre;
	
	@Transient
	private  int chapitreLu;
	
	@Transient
	private int qroTotal;
	
	@Transient
	private int qroRepondu;
	
	@Transient
	private Date dateStart;
	

	public Module() {
	}

	public int getIdModule() {
		return this.idModule;
	}

	public void setIdModule(int idModule) {
		this.idModule = idModule;
	}

	public Date getDateDeblocage() {
		return this.dateDeblocage;
	}

	public void setDateDeblocage(Date dateDeblocage) {
		this.dateDeblocage = dateDeblocage;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescriptionEn() {
		return this.descriptionEn;
	}

	public void setDescriptionEn(String descriptionEn) {
		this.descriptionEn = descriptionEn;
	}

	public String getEtat() {
		return this.etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	public String getNomImage() {
		return this.nomImage;
	}

	public void setNomImage(String nomImage) {
		this.nomImage = nomImage;
	}

	public String getTitre() {
		return this.titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getTitreEn() {
		return this.titreEn;
	}

	public void setTitreEn(String titreEn) {
		this.titreEn = titreEn;
	}

	public List<Chapitre> getChapitres() {
		return this.chapitres;
	}

	public void setChapitres(List<Chapitre> chapitres) {
		this.chapitres = chapitres;
	}

	public Chapitre addChapitre(Chapitre chapitre) {
		getChapitres().add(chapitre);
		chapitre.setModule(this);

		return chapitre;
	}

	public Chapitre removeChapitre(Chapitre chapitre) {
		getChapitres().remove(chapitre);
		chapitre.setModule(null);

		return chapitre;
	}

	public List<EtudiantModule> getEtudiantModules() {
		return this.etudiantModules;
	}

	public void setEtudiantModules(List<EtudiantModule> etudiantModules) {
		this.etudiantModules = etudiantModules;
	}

	public EtudiantModule addEtudiantModules(EtudiantModule etudiantModules) {
		getEtudiantModules().add(etudiantModules);
		etudiantModules.setModule(this);

		return etudiantModules;
	}

	public EtudiantModule removeEtudiantModules(EtudiantModule etudiantModules) {
		getEtudiantModules().remove(etudiantModules);
		etudiantModules.setModule(null);

		return etudiantModules;
	}

	

	public List<Libelle> getLibelles() {
		return this.libelles;
	}

	public void setLibelles(List<Libelle> libelles) {
		this.libelles = libelles;
	}

	public Libelle addLibelle(Libelle libelle) {
		getLibelles().add(libelle);
		libelle.setModule(this);

		return libelle;
	}

	public Libelle removeLibelle(Libelle libelle) {
		getLibelles().remove(libelle);
		libelle.setModule(null);

		return libelle;
	}

	public List<ProfesseurModule> getProfesseurModules() {
		return this.professeurModules;
	}

	public void setProfesseurModules(List<ProfesseurModule> professeurModules) {
		this.professeurModules = professeurModules;
	}

	public ProfesseurModule addProfesseurModule(ProfesseurModule professeurModule) {
		getProfesseurModules().add(professeurModule);
		professeurModule.setModule(this);

		return professeurModule;
	}

	public ProfesseurModule removeProfesseurModule(ProfesseurModule professeurModule) {
		getProfesseurModules().remove(professeurModule);
		professeurModule.setModule(null);

		return professeurModule;
	}

	public List<QuestionCour> getQuestionCours() {
		return this.questionCours;
	}

	public void setQuestionCours(List<QuestionCour> questionCours) {
		this.questionCours = questionCours;
	}

	public QuestionCour addQuestionCour(QuestionCour questionCour) {
		getQuestionCours().add(questionCour);
		questionCour.setModule(this);

		return questionCour;
	}

	public QuestionCour removeQuestionCour(QuestionCour questionCour) {
		getQuestionCours().remove(questionCour);
		questionCour.setModule(null);

		return questionCour;
	}

	public Boolean getFistTime() {
		return fistTime;
	}

	public void setFistTime(Boolean fistTime) {
		this.fistTime = fistTime;
	}

	public Boolean getIsAccessible() {
		return isAccessible;
	}

	public void setIsAccessible(Boolean isAccessible) {
		this.isAccessible = isAccessible;
	}

	public List<GammeEtudiantModule> getGammeEtudiantModules() {
		return this.gammeEtudiantModules;
	}

	public void setGammeEtudiantModules(List<GammeEtudiantModule> gammeEtudiantModules) {
		this.gammeEtudiantModules = gammeEtudiantModules;
	}

	public GammeEtudiantModule addGammeEtudiantModule(GammeEtudiantModule gammeEtudiantModule) {
		getGammeEtudiantModules().add(gammeEtudiantModule);
		gammeEtudiantModule.setModule(this);

		return gammeEtudiantModule;
	}

	public GammeEtudiantModule removeGammeEtudiantModule(GammeEtudiantModule gammeEtudiantModule) {
		getGammeEtudiantModules().remove(gammeEtudiantModule);
		gammeEtudiantModule.setModule(null);

		return gammeEtudiantModule;
	}

	public int getTotalChapitre() {
		return totalChapitre;
	}

	public void setTotalChapitre(int totalChapitre) {
		this.totalChapitre = totalChapitre;
	}

	public int getChapitreLu() {
		return chapitreLu;
	}

	public void setChapitreLu(int chapitreLu) {
		this.chapitreLu = chapitreLu;
	}

	public int getQroTotal() {
		return qroTotal;
	}

	public void setQroTotal(int qroTotal) {
		this.qroTotal = qroTotal;
	}

	public int getQroRepondu() {
		return qroRepondu;
	}

	public void setQroRepondu(int qroRepondu) {
		this.qroRepondu = qroRepondu;
	}

	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	

}