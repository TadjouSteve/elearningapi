package iri.elearningapi.model.courModel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

/*
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
*/

/**
 * The persistent class for the chapitre database table.
 * 
 */
@Entity
@NamedQuery(name="Chapitre.findAll", query="SELECT c FROM Chapitre c")
public class Chapitre implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_chapitre")
	private int idChapitre;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_ajout")
	private Date dateAjout;

	@Lob
	private String description;
	
	@Lob
	private String texte;
	
	@Lob
	private String image;

	@Lob
	private String pdf;

	@Lob
	private String preanbule;

	private int qcm;

	@Lob
	private String titre;

	@Lob
	private String video;

	//bi-directional many-to-one association to Bloc
	@OneToMany(mappedBy="chapitre")
	private List<Bloc> blocs;

	
	//bi-directional many-to-one association to Module
	@ManyToOne
	@JoinColumn(name="id_module")
	private Module module;

	//bi-directional many-to-one association to ChapitreEn
	@OneToMany(mappedBy="chapitre")
	private List<ChapitreEn> chapitreEns;

	//bi-directional many-to-one association to EtudiantChapitre
	@OneToMany(mappedBy="chapitre")
	@JsonIgnore
	private List<EtudiantChapitre> etudiantChapitres;

	//bi-directional many-to-one association to Qcm
	@OneToMany(mappedBy="chapitre")
	private List<Qcm> qcms;

	//bi-directional many-to-one association to QuestionCour
	@OneToMany(mappedBy="chapitre")
	@JsonIgnore
	private List<QuestionCour> questionCours;

	public Chapitre() {
	}

	public int getIdChapitre() {
		return this.idChapitre;
	}

	public void setIdChapitre(int idChapitre) {
		this.idChapitre = idChapitre;
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

	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPdf() {
		return this.pdf;
	}

	public void setPdf(String pdf) {
		this.pdf = pdf;
	}

	public String getPreanbule() {
		return this.preanbule;
	}

	public void setPreanbule(String preanbule) {
		this.preanbule = preanbule;
	}

	public int getQcm() {
		return this.qcm;
	}

	public void setQcm(int qcm) {
		this.qcm = qcm;
	}

	public String getTitre() {
		return this.titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getVideo() {
		return this.video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public List<Bloc> getBlocs() {
		return this.blocs;
	}

	public void setBlocs(List<Bloc> blocs) {
		this.blocs = blocs;
	}

	public Bloc addBlocs(Bloc blocs) {
		getBlocs().add(blocs);
		blocs.setChapitre(this);

		return blocs;
	}

	public Bloc removeBlocs(Bloc bloc) {
		getBlocs().remove(bloc);
		bloc.setChapitre(null);

		return bloc;
	}


	public Module getModule() {
		return this.module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public List<ChapitreEn> getChapitreEns() {
		return this.chapitreEns;
	}

	public void setChapitreEns(List<ChapitreEn> chapitreEns) {
		this.chapitreEns = chapitreEns;
	}

	public ChapitreEn addChapitreEn(ChapitreEn chapitreEn) {
		getChapitreEns().add(chapitreEn);
		chapitreEn.setChapitre(this);

		return chapitreEn;
	}

	public ChapitreEn removeChapitreEn(ChapitreEn chapitreEn) {
		getChapitreEns().remove(chapitreEn);
		chapitreEn.setChapitre(null);

		return chapitreEn;
	}

	public List<EtudiantChapitre> getEtudiantChapitres() {
		return this.etudiantChapitres;
	}

	public void setEtudiantChapitres(List<EtudiantChapitre> etudiantChapitres) {
		this.etudiantChapitres = etudiantChapitres;
	}

	public EtudiantChapitre addEtudiantChapitre(EtudiantChapitre etudiantChapitre) {
		getEtudiantChapitres().add(etudiantChapitre);
		etudiantChapitre.setChapitre(this);

		return etudiantChapitre;
	}

	public EtudiantChapitre removeEtudiantChapitre(EtudiantChapitre etudiantChapitre) {
		getEtudiantChapitres().remove(etudiantChapitre);
		etudiantChapitre.setChapitre(null);

		return etudiantChapitre;
	}

	public List<Qcm> getQcms() {
		return this.qcms;
	}

	public void setQcms(List<Qcm> qcms) {
		this.qcms = qcms;
	}

	public Qcm addQcm(Qcm qcm) {
		getQcms().add(qcm);
		qcm.setChapitre(this);

		return qcm;
	}

	public Qcm removeQcm(Qcm qcm) {
		getQcms().remove(qcm);
		qcm.setChapitre(null);

		return qcm;
	}

	public List<QuestionCour> getQuestionCours() {
		return this.questionCours;
	}

	public void setQuestionCours(List<QuestionCour> questionCours) {
		this.questionCours = questionCours;
	}

	public QuestionCour addQuestionCour(QuestionCour questionCour) {
		getQuestionCours().add(questionCour);
		questionCour.setChapitre(this);

		return questionCour;
	}

	public QuestionCour removeQuestionCour(QuestionCour questionCour) {
		getQuestionCours().remove(questionCour);
		questionCour.setChapitre(null);

		return questionCour;
	}

	public String getTexte() {
		return texte;
	}

	public void setTexte(String texte) {
		this.texte = texte;
	}


}