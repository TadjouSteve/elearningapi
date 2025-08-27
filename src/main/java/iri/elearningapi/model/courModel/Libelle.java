package iri.elearningapi.model.courModel;

import java.io.Serializable;

import javax.persistence.Column;
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
import jakarta.persistence.Column;
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
 * The persistent class for the libelle database table.
 * 
 */
@Entity
@NamedQuery(name="Libelle.findAll", query="SELECT l FROM Libelle l")
public class Libelle implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Lob
	private String texte;
	

	@Lob
	@Column(name="texte_en")
	private String texteEn;

	@Lob
	private String titre;

	@Lob
	@Column(name="titre_en")
	private String titreEn;

	//bi-directional many-to-one association to Module
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="id_module")
	private Module module;

	public Libelle() {
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

	public String getTexteEn() {
		return this.texteEn;
	}

	public void setTexteEn(String texteEn) {
		this.texteEn = texteEn;
	}

	public String getTitre() {
		return this.titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getTitreEn() {
		return this.titreEn;
	}

	public void setTitreEn(String titreEn) {
		this.titreEn = titreEn;
	}

	public Module getModule() {
		return this.module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

}