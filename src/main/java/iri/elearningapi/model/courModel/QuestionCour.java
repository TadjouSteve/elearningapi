package iri.elearningapi.model.courModel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

import iri.elearningapi.model.userModel.Etudiant;

/*
import iri.elearningapi.model.userModel.Etudiant;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
*/


/**
 * The persistent class for the question_cour database table.
 * 
 */
@Entity
@Table(name="question_cour")
@NamedQuery(name="QuestionCour.findAll", query="SELECT q FROM QuestionCour q")
public class QuestionCour implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@Lob
	private String texte;

	//bi-directional many-to-one association to Chapitre
	@ManyToOne
	@JoinColumn(name="id_chapitre")
	private Chapitre chapitre;

	//bi-directional many-to-one association to Etudiant
	@ManyToOne
	@JoinColumn(name="id_Etudiant")
	private Etudiant etudiant;

	//bi-directional many-to-one association to Module
	@ManyToOne
	@JoinColumn(name="id_module")
	private Module module;

	//bi-directional many-to-one association to Reponse
	@JsonIgnore
	@OneToMany(mappedBy="questionCour")
	private List<Reponse> reponses;

	public QuestionCour() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTexte() {
		return this.texte;
	}

	public void setTexte(String texte) {
		this.texte = texte;
	}

	public Chapitre getChapitre() {
		return this.chapitre;
	}

	public void setChapitre(Chapitre chapitre) {
		this.chapitre = chapitre;
	}

	public Etudiant getEtudiant() {
		return this.etudiant;
	}

	public void setEtudiant(Etudiant etudiant) {
		this.etudiant = etudiant;
	}

	public Module getModule() {
		return this.module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public List<Reponse> getReponses() {
		return this.reponses;
	}

	public void setReponses(List<Reponse> reponses) {
		this.reponses = reponses;
	}

	public Reponse addRepons(Reponse repons) {
		getReponses().add(repons);
		repons.setQuestionCour(this);

		return repons;
	}

	public Reponse removeRepons(Reponse repons) {
		getReponses().remove(repons);
		repons.setQuestionCour(null);

		return repons;
	}

}