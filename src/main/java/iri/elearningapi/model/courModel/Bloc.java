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
*/
/**
 * The persistent class for the bloc database table.
 * 
 */
@Entity
@NamedQuery(name="Bloc.findAll", query="SELECT b FROM Bloc b")
public class Bloc implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String image;

	@Lob
	private String texte;

	@Lob
	private String titre;

	private String video;

	//bi-directional many-to-one association to Chapitre
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="id_chapitre")
	private Chapitre chapitre;


	//bi-directional many-to-one association to ChapitreEn
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="id_Chapitre_En")
	private ChapitreEn chapitreEn;

	//bi-directional many-to-one association to Item
	@OneToMany(mappedBy="bloc")
	private List<Item> items;

	//bi-directional many-to-one association to SousBloc
	@OneToMany(mappedBy="bloc")
	private List<SousBloc> sousBlocs;

	public Bloc() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getTexte() {
		return this.texte;
	}

	public void setTexte(String texte) {
		this.texte = texte;
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

	public Chapitre getChapitre() {
		return this.chapitre;
	}

	public void setChapitre(Chapitre chapitre) {
		this.chapitre = chapitre;
	}

	

	public ChapitreEn getChapitreEn() {
		return this.chapitreEn;
	}

	public void setChapitreEn(ChapitreEn chapitreEn) {
		this.chapitreEn = chapitreEn;
	}

	public List<Item> getItems() {
		return this.items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public Item addItem(Item item) {
		getItems().add(item);
		item.setBloc(this);

		return item;
	}

	public Item removeItem(Item item) {
		getItems().remove(item);
		item.setBloc(null);

		return item;
	}

	public List<SousBloc> getSousBlocs() {
		return this.sousBlocs;
	}

	public void setSousBlocs(List<SousBloc> sousBlocs) {
		this.sousBlocs = sousBlocs;
	}

	public SousBloc addSousBloc(SousBloc sousBloc) {
		getSousBlocs().add(sousBloc);
		sousBloc.setBloc(this);

		return sousBloc;
	}

	public SousBloc removeSousBloc(SousBloc sousBloc) {
		getSousBlocs().remove(sousBloc);
		sousBloc.setBloc(null);

		return sousBloc;
	}

}