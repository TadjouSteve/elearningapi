package iri.elearningapi.model.courModel;

import java.io.Serializable;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

/*
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
*/


/**
 * The persistent class for the chapitre_en database table.
 * 
 */
@Entity
@Table(name="chapitre_en")
@NamedQuery(name="ChapitreEn.findAll", query="SELECT c FROM ChapitreEn c")
public class ChapitreEn implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

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
	@OneToMany(mappedBy="chapitreEn")
	private List<Bloc> blocs;

	//bi-directional many-to-one association to Chapitre
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="id_chapitre")
	private Chapitre chapitre;

	public ChapitreEn() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Bloc addBloc(Bloc bloc) {
		getBlocs().add(bloc);
		bloc.setChapitreEn(this);

		return bloc;
	}

	public Bloc removeBloc(Bloc bloc) {
		getBlocs().remove(bloc);
		bloc.setChapitreEn(null);

		return bloc;
	}

	public Chapitre getChapitre() {
		return this.chapitre;
	}

	public void setChapitre(Chapitre chapitre) {
		this.chapitre = chapitre;
	}

	public String getTexte() {
		return texte;
	}

	public void setTexte(String texte) {
		this.texte = texte;
	}

}