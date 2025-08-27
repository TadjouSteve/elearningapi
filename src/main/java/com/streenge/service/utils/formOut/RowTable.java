package com.streenge.service.utils.formOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RowTable {
	private List<String>			data			= new ArrayList<String>();
	private String					id				= "";
	private String					statut			= "actif";
	private String					colorStatut		= "orange";
	private int						idUtilisateur	= 0;
	private int						idStockage		= 0;
	private HashMap<String, Float>	progressList	= new HashMap<>();

	public List<String> getData() {
		return data;
	}

	public void setData(List<String> data) {
		this.data = data;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getColorStatut() {
		return colorStatut;
	}

	public void setColorStatut(String colorStatus) {
		this.colorStatut = colorStatus;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public int getIdUtilisateur() {
		return idUtilisateur;
	}

	public void setIdUtilisateur(int idUtilisateur) {
		this.idUtilisateur = idUtilisateur;
	}

	public HashMap<String, Float> getProgressList() {
		return progressList;
	}

	public void setProgressList(HashMap<String, Float> progressList) {
		this.progressList = progressList;
	}

	public int getIdStockage() {
		return idStockage;
	}

	public void setIdStockage(int idStockage) {
		this.idStockage = idStockage;
	}

}
