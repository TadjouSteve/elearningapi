package iri.elearningapi.model.mediaModel;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the image_article database table.
 * 
 */
@Entity
@Table(name="image_article")
@NamedQuery(name="ImageArticle.findAll", query="SELECT i FROM ImageArticle i")
public class ImageArticle implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@JsonIgnore
	@Lob
	@Column(name="image_data")
	private byte[] imageData;

	@Lob
	private String nom;
	
	@Lob
	private String titre;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	@Lob
	private String type;

	//bi-directional many-to-one association to Article
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="id_article")
	private Article article;

	public ImageArticle() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Article getArticle() {
		return this.article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}

}