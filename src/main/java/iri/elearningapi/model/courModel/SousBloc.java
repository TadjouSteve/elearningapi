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
 * The persistent class for the sous_bloc database table.
 * 
 */
@Entity
@Table(name="sous_bloc")
@NamedQuery(name="SousBloc.findAll", query="SELECT s FROM SousBloc s")
public class SousBloc implements Serializable {
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

	//bi-directional many-to-one association to Item
	@OneToMany(mappedBy="sousBloc")
	private List<Item> items;

	//bi-directional many-to-one association to Bloc
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="id_bloc")
	private Bloc bloc;

	public SousBloc() {
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

	public List<Item> getItems() {
		return this.items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public Item addItem(Item item) {
		getItems().add(item);
		item.setSousBloc(this);

		return item;
	}

	public Item removeItem(Item item) {
		getItems().remove(item);
		item.setSousBloc(null);

		return item;
	}

	public Bloc getBloc() {
		return this.bloc;
	}

	public void setBloc(Bloc bloc) {
		this.bloc = bloc;
	}

}