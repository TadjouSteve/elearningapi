package com.streenge.model.admin;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.streenge.model.admin.money.PaiementAbonnement;
import com.streenge.model.admin.money.TarifAbonnement;


/**
 * The persistent class for the entreprise database table.
 * 
 */
@Entity
@Table(name="entreprise")
@NamedQuery(name="Entreprise.findAll", query="SELECT e FROM Entreprise e")
public class Entreprise implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(length=200)
	private String adresse;
	
	@Column(length=200)
	private String adresse2;

	@Column(length=100)
	private String devise;

	@Column(length=100)
	private String email;

	@Column(length=100)
	private String nom;
	
	@Column(length=100)
	private String niu;
	
	@Column(length=100)
	private String rccm;
	
	@Column(length = 40)
	private String etat;

	@Column(length=50)
	private String pays;
	
	@Column(length=100)
	private String ville;

	@Column(name="site_web", length=80)
	private String siteWeb;

	@Column(length=100)
	private String telephone;
	
	@Lob
	private String activite;
	
	@Column(name = "ticket_first")
	private String ticketFirst;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_limite")
	private Date dateLimite;
	
	private int level;
	
	@Column(name = "boite_postal")
	private String boitePostal;
	
	@JsonIgnore
	@OneToMany(mappedBy="entreprise")
	private List<PaiementAbonnement> paiementAbonnements;

	//bi-directional many-to-one association to TarifAbonnement
	@OneToMany(mappedBy="entreprise")
	private List<TarifAbonnement> tarifAbonnements;
	
	@Transient
	private boolean paiementOk=true;

	public Entreprise() {
		if (dateLimite!=null && dateLimite.before(new Date()) ) {
			paiementOk=false;
		}else {
			paiementOk=true;
		}
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAdresse() {
		return this.adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getDevise() {
		return this.devise;
	}

	public void setDevise(String devise) {
		this.devise = devise;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPays() {
		return this.pays;
	}

	public void setPays(String pays) {
		this.pays = pays;
	}

	public String getSiteWeb() {
		return this.siteWeb;
	}

	public void setSiteWeb(String siteWeb) {
		this.siteWeb = siteWeb;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getNiu() {
		return niu;
	}

	public void setNiu(String niu) {
		this.niu = niu;
	}

	public String getAdresse2() {
		return adresse2;
	}

	public void setAdresse2(String adresse2) {
		this.adresse2 = adresse2;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public String getActivite() {
		return activite;
	}

	public void setActivite(String activite) {
		this.activite = activite;
	}

	public String getTicketFirst() {
		return ticketFirst;
	}

	public void setTicketFirst(String ticketFirst) {
		this.ticketFirst = ticketFirst;
	}

	public Date getDateLimite() {
		return dateLimite;
	}

	public void setDateLimite(Date dateLimite) {
		this.dateLimite = dateLimite;
	}

	public String getRccm() {
		return rccm;
	}

	public void setRccm(String rccm) {
		this.rccm = rccm;
	}

	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	public List<PaiementAbonnement> getPaiementAbonnements() {
		return paiementAbonnements;
	}

	public void setPaiementAbonnements(List<PaiementAbonnement> paiementAbonnements) {
		this.paiementAbonnements = paiementAbonnements;
	}

	public List<TarifAbonnement> getTarifAbonnements() {
		return tarifAbonnements;
	}

	public void setTarifAbonnements(List<TarifAbonnement> tarifAbonnements) {
		this.tarifAbonnements = tarifAbonnements;
	}

	public boolean isPaiementOk() {
		return paiementOk;
	}

	public void setPaiementOk(boolean paiementOk) {
		this.paiementOk = paiementOk;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getBoitePostal() {
		return boitePostal;
	}

	public void setBoitePostal(String boitePostal) {
		this.boitePostal = boitePostal;
	}

}