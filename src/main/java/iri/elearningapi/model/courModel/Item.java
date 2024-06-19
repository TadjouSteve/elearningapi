package iri.elearningapi.model.courModel;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

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
*/

/**
 * The persistent class for the item database table.
 * 
 */
@Entity
@NamedQuery(name="Item.findAll", query="SELECT i FROM Item i")
public class Item implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Lob
	private String texte;

	//bi-directional many-to-one association to Bloc
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="id_bloc")
	private Bloc bloc;

	//bi-directional many-to-one association to SousBloc
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="id_sous_bloc")
	private SousBloc sousBloc;

	public Item() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTexte() {
		return this.texte;
	}

	public void setTexte(String texte) {
		this.texte = texte;
	}

	public Bloc getBloc() {
		return this.bloc;
	}

	public void setBloc(Bloc bloc) {
		this.bloc = bloc;
	}

	public SousBloc getSousBloc() {
		return this.sousBloc;
	}

	public void setSousBloc(SousBloc sousBloc) {
		this.sousBloc = sousBloc;
	}

}