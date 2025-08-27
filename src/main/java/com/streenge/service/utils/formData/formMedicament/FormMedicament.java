package com.streenge.service.utils.formData.formMedicament;

import java.util.ArrayList;
import java.util.List;

public class FormMedicament {
	private int		id;
	private String	nomLaboratoire	= "";
	private String	nomCommercial	= "";
	private String	description		= "";
	private String	posologie		= "";
	private String	pack			= "";
	private String	avertissement	= "";
	private String	idProduit		= "";

	private List<FormComposant> composants = new ArrayList<>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNomLaboratoire() {
		return nomLaboratoire;
	}

	public void setNomLaboratoire(String nomLaboratoire) {
		this.nomLaboratoire = nomLaboratoire;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPosologie() {
		return posologie;
	}

	public void setPosologie(String posologie) {
		this.posologie = posologie;
	}

	public String getPack() {
		return pack;
	}

	public void setPack(String pack) {
		this.pack = pack;
	}

	public String getAvertissement() {
		return avertissement;
	}

	public void setAvertissement(String avertissement) {
		this.avertissement = avertissement;
	}

	public List<FormComposant> getComposants() {
		return composants;
	}

	public void setComposants(List<FormComposant> composants) {
		this.composants = composants;
	}

	public String getNomCommercial() {
		return nomCommercial;
	}

	public void setNomCommercial(String nomCommercial) {
		this.nomCommercial = nomCommercial;
	}

	public String getIdProduit() {
		return idProduit;
	}

	public void setIdProduit(String idProduit) {
		this.idProduit = idProduit;
	}

}
