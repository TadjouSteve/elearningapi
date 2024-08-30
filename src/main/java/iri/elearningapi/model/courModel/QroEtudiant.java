package iri.elearningapi.model.courModel;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import iri.elearningapi.model.userModel.Etudiant;


/**
 * The persistent class for the qro_etudiant database table.
 * 
 */
@Entity
@Table(name="qro_etudiant")
@NamedQuery(name="QroEtudiant.findAll", query="SELECT q FROM QroEtudiant q")
public class QroEtudiant implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_ajout")
	private Date dateAjout;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_modification")
	private Date dateModification;

	@Lob
	private String reponse;

	//bi-directional many-to-one association to Etudiant
	@ManyToOne
	@JoinColumn(name="id_Etudiant")
	private Etudiant etudiant;

	//bi-directional many-to-one association to Qro
	@ManyToOne
	@JoinColumn(name="id_QRO")
	private Qro qro;

	public QroEtudiant() {
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

	public Date getDateModification() {
		return this.dateModification;
	}

	public void setDateModification(Date dateModification) {
		this.dateModification = dateModification;
	}

	public String getReponse() {
		return this.reponse;
	}

	public void setReponse(String reponse) {
		this.reponse = reponse;
	}

	public Etudiant getEtudiant() {
		return this.etudiant;
	}

	public void setEtudiant(Etudiant etudiant) {
		this.etudiant = etudiant;
	}

	public Qro getQro() {
		return this.qro;
	}

	public void setQro(Qro qro) {
		this.qro = qro;
	}

}