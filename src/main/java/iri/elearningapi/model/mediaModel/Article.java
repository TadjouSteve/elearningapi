package iri.elearningapi.model.mediaModel;

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


/**
 * The persistent class for the article database table.
 * 
 */
@Entity
@NamedQuery(name="Article.findAll", query="SELECT a FROM Article a")
public class Article implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Lob
	private String auteur;

	private String categorie;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_publication")
	private Date datePublication;

	private int etat;

	@Lob
	private String image_URL;

	private String lien;

	@Lob
	@Column(name="sous_titre")
	private String sousTitre;

	private String statut;

	@Lob
	private String texte;

	@Lob
	private String titre;

	@Lob
	@Column(name="titre_auteur")
	private String titreAuteur;

	private String type;

	@Lob
	private String video_URL;

	//bi-directional many-to-one association to Rubrique
	@ManyToOne
	@JoinColumn(name="id_rubrique")
	private Rubrique rubrique;

	//bi-directional many-to-one association to ImageArticle
	@OneToMany(mappedBy="article")
	private List<ImageArticle> imageArticles;

	public Article() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAuteur() {
		return this.auteur;
	}

	public void setAuteur(String auteur) {
		this.auteur = auteur;
	}

	public String getCategorie() {
		return this.categorie;
	}

	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getEtat() {
		return this.etat;
	}

	public void setEtat(int etat) {
		this.etat = etat;
	}

	public String getImage_URL() {
		return this.image_URL;
	}

	public void setImage_URL(String image_URL) {
		this.image_URL = image_URL;
	}

	public String getLien() {
		return this.lien;
	}

	public void setLien(String lien) {
		this.lien = lien;
	}

	public String getSousTitre() {
		return this.sousTitre;
	}

	public void setSousTitre(String sousTitre) {
		this.sousTitre = sousTitre;
	}

	public String getStatut() {
		return this.statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
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

	public String getTitreAuteur() {
		return this.titreAuteur;
	}

	public void setTitreAuteur(String titreAuteur) {
		this.titreAuteur = titreAuteur;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVideo_URL() {
		return this.video_URL;
	}

	public void setVideo_URL(String video_URL) {
		this.video_URL = video_URL;
	}

	public Rubrique getRubrique() {
		return this.rubrique;
	}

	public void setRubrique(Rubrique rubrique) {
		this.rubrique = rubrique;
	}

	public List<ImageArticle> getImageArticles() {
		return this.imageArticles;
	}

	public void setImageArticles(List<ImageArticle> imageArticles) {
		this.imageArticles = imageArticles;
	}

	public ImageArticle addImageArticle(ImageArticle imageArticle) {
		getImageArticles().add(imageArticle);
		imageArticle.setArticle(this);

		return imageArticle;
	}

	public ImageArticle removeImageArticle(ImageArticle imageArticle) {
		getImageArticles().remove(imageArticle);
		imageArticle.setArticle(null);

		return imageArticle;
	}

	public Date getDatePublication() {
		return datePublication;
	}

	public void setDatePublication(Date datePublication) {
		this.datePublication = datePublication;
	}

}