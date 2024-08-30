package iri.elearningapi.model.courModel;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


/**
 * The persistent class for the qro database table.
 * 
 */
@Entity
@NamedQuery(name="Qro.findAll", query="SELECT q FROM Qro q")
public class Qro implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Lob
	private String intitule;

	@Lob
	@Column(name="intitule_en")
	private String intituleEn;

	@Lob
	private String note;

	//bi-directional many-to-one association to Chapitre
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="id_chapitre")
	private Chapitre chapitre;

	//bi-directional many-to-one association to QroEtudiant
	@JsonIgnore
	@OneToMany(mappedBy="qro")
	private List<QroEtudiant> qroEtudiants;

	public Qro() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIntitule() {
		return this.intitule;
	}

	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}

	public String getIntituleEn() {
		return this.intituleEn;
	}

	public void setIntituleEn(String intituleEn) {
		this.intituleEn = intituleEn;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Chapitre getChapitre() {
		return this.chapitre;
	}

	public void setChapitre(Chapitre chapitre) {
		this.chapitre = chapitre;
	}

	public List<QroEtudiant> getQroEtudiants() {
		return this.qroEtudiants;
	}

	public void setQroEtudiants(List<QroEtudiant> qroEtudiants) {
		this.qroEtudiants = qroEtudiants;
	}

	public QroEtudiant addQroEtudiant(QroEtudiant qroEtudiant) {
		getQroEtudiants().add(qroEtudiant);
		qroEtudiant.setQro(this);

		return qroEtudiant;
	}

	public QroEtudiant removeQroEtudiant(QroEtudiant qroEtudiant) {
		getQroEtudiants().remove(qroEtudiant);
		qroEtudiant.setQro(null);

		return qroEtudiant;
	}

}