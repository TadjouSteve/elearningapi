package com.streenge.service.utils.formData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.streenge.service.utils.formOut.FormViewProduit;

public class FormGroupe {

	private int		id;
	private String	description	= "";
	private String	nom			= "";
	private Date	dateAjout	= null;
	private int		priorite	= 0;

	private List<FormViewProduit> formProduits = new ArrayList<>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Date getDateAjout() {
		return dateAjout;
	}

	public void setDateAjout(Date dateAjout) {
		this.dateAjout = dateAjout;
	}

	public int getPriorite() {
		return priorite;
	}

	public void setPriorite(int priorite) {
		this.priorite = priorite;
	}

	public List<FormViewProduit> getFormProduits() {
		return formProduits;
	}

	public void setFormProduits(List<FormViewProduit> formProduits) {
		this.formProduits = formProduits;
	}

}
