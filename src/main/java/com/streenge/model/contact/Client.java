package com.streenge.model.contact;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.streenge.model.admin.money.HistoriqueSoldeClient;
import com.streenge.model.admin.money.MouvementSoldeClient;
import com.streenge.model.vente.BonCommande;
import com.streenge.model.vente.Devis;
import com.streenge.model.vente.facture.Facture;

/**
 * The persistent class for the client database table.
 * 
 */
@Entity
@Table(name = "client")
@NamedQuery(name = "Client.findAll", query = "SELECT c FROM Client c")
public class Client implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id_client", unique = true, nullable = false, length = 30)
	private String idClient;

	@Column(length = 100)
	private String adresse;

	@Temporal(TemporalType.DATE)
	@Column(name = "date_ajout")
	private Date dateAjout;

	@Column(length = 255)
	private String description;

	@Column(length = 30)
	private String email;

	@Column(length = 30)
	private String etat;

	@Column(nullable = false, length = 50)
	private String nom;

	@Column(length = 30)
	private String statut;

	@Column(length = 30)
	private String telephone;
	
	private String cni;
	
	private String niu;
	
	private String rccm;
	
	@Column(name = "boite_postal")
	private String boitePostal;

	private float solde;
	
	@OneToMany(mappedBy="client")
	private List<Message> messages;

	// bi-directional many-to-one association to BonCommande
	@JsonIgnore
	@OneToMany(mappedBy = "client")
	private List<BonCommande> bonCommandes;

	// bi-directional many-to-one association to Devis
	@JsonIgnore
	@OneToMany(mappedBy = "client")
	private List<Devis> devis;

	// bi-directional many-to-one association to Facture
	@JsonIgnore
	@OneToMany(mappedBy = "client")
	private List<Facture> factures;

	@JsonIgnore
	@OneToMany(mappedBy = "client")
	private List<HistoriqueSoldeClient> historiqueSoldeClients;

	// bi-directional many-to-one association to MouvementSoldeClient
	@JsonIgnore
	@OneToMany(mappedBy = "client")
	private List<MouvementSoldeClient> mouvementSoldeClients;

	public Client() {
	}

	public String getIdClient() {
		return this.idClient;
	}

	public void setIdClient(String idClient) {
		this.idClient = idClient;
	}

	public String getAdresse() {
		return this.adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public Date getDateAjout() {
		return this.dateAjout;
	}

	public void setDateAjout(Date dateAjout) {
		this.dateAjout = dateAjout;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEtat() {
		return this.etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getStatut() {
		return this.statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public float getSolde() {
		return this.solde;
	}

	public void setSolde(float solde) {
		this.solde = solde;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public List<BonCommande> getBonCommandes() {
		return this.bonCommandes;
	}

	public void setBonCommandes(List<BonCommande> bonCommandes) {
		this.bonCommandes = bonCommandes;
	}

	public BonCommande addBonCommande(BonCommande bonCommande) {
		getBonCommandes().add(bonCommande);
		bonCommande.setClient(this);

		return bonCommande;
	}

	public BonCommande removeBonCommande(BonCommande bonCommande) {
		getBonCommandes().remove(bonCommande);
		bonCommande.setClient(null);

		return bonCommande;
	}

	public List<Devis> getDevis() {
		return this.devis;
	}

	public void setDevis(List<Devis> devis) {
		this.devis = devis;
	}

	public Devis addDevi(Devis devi) {
		getDevis().add(devi);
		devi.setClient(this);

		return devi;
	}

	public Devis removeDevi(Devis devi) {
		getDevis().remove(devi);
		devi.setClient(null);

		return devi;
	}

	public List<Facture> getFactures() {
		return this.factures;
	}

	public void setFactures(List<Facture> factures) {
		this.factures = factures;
	}

	public Facture addFacture(Facture facture) {
		getFactures().add(facture);
		facture.setClient(this);

		return facture;
	}

	public Facture removeFacture(Facture facture) {
		getFactures().remove(facture);
		facture.setClient(null);

		return facture;
	}

	public List<HistoriqueSoldeClient> getHistoriqueSoldeClients() {
		return this.historiqueSoldeClients;
	}

	public void setHistoriqueSoldeClients(List<HistoriqueSoldeClient> historiqueSoldeClients) {
		this.historiqueSoldeClients = historiqueSoldeClients;
	}

	public HistoriqueSoldeClient addHistoriqueSoldeClient(HistoriqueSoldeClient historiqueSoldeClient) {
		getHistoriqueSoldeClients().add(historiqueSoldeClient);
		historiqueSoldeClient.setClient(this);

		return historiqueSoldeClient;
	}

	public HistoriqueSoldeClient removeHistoriqueSoldeClient(HistoriqueSoldeClient historiqueSoldeClient) {
		getHistoriqueSoldeClients().remove(historiqueSoldeClient);
		historiqueSoldeClient.setClient(null);

		return historiqueSoldeClient;
	}

	public List<MouvementSoldeClient> getMouvementSoldeClients() {
		return this.mouvementSoldeClients;
	}

	public void setMouvementSoldeClients(List<MouvementSoldeClient> mouvementSoldeClients) {
		this.mouvementSoldeClients = mouvementSoldeClients;
	}

	public MouvementSoldeClient addMouvementSoldeClient(MouvementSoldeClient mouvementSoldeClient) {
		getMouvementSoldeClients().add(mouvementSoldeClient);
		mouvementSoldeClient.setClient(this);

		return mouvementSoldeClient;
	}

	public MouvementSoldeClient removeMouvementSoldeClient(MouvementSoldeClient mouvementSoldeClient) {
		getMouvementSoldeClients().remove(mouvementSoldeClient);
		mouvementSoldeClient.setClient(null);

		return mouvementSoldeClient;
	}

	public String getCni() {
		return cni;
	}

	public void setCni(String cni) {
		this.cni = cni;
	}

	public String getNiu() {
		return niu;
	}

	public void setNiu(String niu) {
		this.niu = niu;
	}

	public String getRccm() {
		return rccm;
	}

	public void setRccm(String rccm) {
		this.rccm = rccm;
	}

	public String getBoitePostal() {
		return boitePostal;
	}

	public void setBoitePostal(String boitePostal) {
		this.boitePostal = boitePostal;
	}
	
	public List<Message> getMessages() {
		return this.messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public Message addMessage(Message message) {
		getMessages().add(message);
		message.setClient(this);

		return message;
	}

	public Message removeMessage(Message message) {
		getMessages().remove(message);
		message.setClient(null);

		return message;
	}

}