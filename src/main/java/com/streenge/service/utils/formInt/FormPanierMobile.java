package com.streenge.service.utils.formInt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FormPanierMobile {

	private String					idClient		= "";
	private String					idBonCommande	= "";
	private String					nomClient		= "";
	private Date					dateAjout;
	private List<LinePanierMobile>	lines			= new ArrayList<>();
	private String					statutPaiement	= "";

	private String	statut		= "";
	private String	colorStatut	= "";

	public String getIdClient() {
		return idClient;
	}

	public void setIdClient(String idClient) {
		this.idClient = idClient;
	}

	public Date getDateAjout() {
		return dateAjout;
	}

	public void setDateAjout(Date dateAjout) {
		this.dateAjout = dateAjout;
	}

	public List<LinePanierMobile> getLines() {
		return lines;
	}

	public void setLines(List<LinePanierMobile> lines) {
		this.lines = lines;
	}
	
	public void addLine(LinePanierMobile line) {
		if (this.lines != null && line != null) {
			this.lines.add(line);
		} else if (line != null) {
			this.lines = new ArrayList<>();
			this.lines.add(line);
		}
	}

	public String getIdBonCommande() {
		return idBonCommande;
	}

	public void setIdBonCommande(String idBonCommande) {
		this.idBonCommande = idBonCommande;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public String getColorStatut() {
		return colorStatut;
	}

	public void setColorStatut(String colorStatut) {
		this.colorStatut = colorStatut;
	}

	public String getNomClient() {
		return nomClient;
	}

	public void setNomClient(String nomClient) {
		this.nomClient = nomClient;
	}

	public String getStatutPaiement() {
		return statutPaiement;
	}

	public void setStatutPaiement(String statutPaiement) {
		this.statutPaiement = statutPaiement;
	}

}
