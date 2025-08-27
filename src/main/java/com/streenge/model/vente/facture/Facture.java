package com.streenge.model.vente.facture;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.streenge.model.admin.Stockage;
import com.streenge.model.admin.Utilisateur;
import com.streenge.model.contact.Client;
import com.streenge.model.vente.BonCommande;
import com.streenge.model.vente.Devis;

/**
 * The persistent class for the facture database table.
 * 
 */
@Entity
@Table(name = "facture")
@NamedQuery(name = "Facture.findAll", query = "SELECT f FROM Facture f")
public class Facture implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id_facture", unique = true, nullable = false, length = 30)
	private String idFacture;

	@Column(length = 150)
	private String adresse;

	@Column(name = "condition_vente", length = 255)
	private String conditionVente;

	@Column(name = "cout_livraison")
	private float coutLivraison;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_creation")
	private Date dateCreation;

	@Temporal(TemporalType.DATE)
	@Column(name = "date_echeance")
	private Date dateEcheance;

	@Temporal(TemporalType.DATE)
	@Column(name = "date_livraison")
	private Date dateLivraison;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_modification")
	private Date dateModification;

	@Lob
	private String description;

	@Column(length = 30)
	private String etat;

	@Lob
	private String note;
	
	
	@Column(name = "soumettre_a", length = 255)
	private String soumettreA;
	
	@Column(name = "pourcentage_remise")
	private float pourcentageRemise;

	@Column(length = 30)
	private String statut;

	// bi-directional many-to-one association to BonCommande
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "id_bon_commande")
	private BonCommande bonCommande;

	// bi-directional many-to-one association to Client
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "id_client")
	private Client client;

	// bi-directional many-to-one association to Devis
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "id_devis")
	private Devis devi;

	// bi-directional many-to-one association to Stockage
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "id_stockage", nullable = false)
	private Stockage stockage;

	// bi-directional many-to-one association to Utilisateur
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "id_utilisateur")
	private Utilisateur utilisateur;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "id_utilisateur_alter")
	private Utilisateur utilisateurAlter;

	// bi-directional many-to-one association to LigneFacture
	@JsonIgnore
	@OneToMany(mappedBy = "facture", cascade = { CascadeType.ALL })
	private List<LigneFacture> ligneFactures;

	// bi-directional many-to-one association to Paiement
	@JsonIgnore
	@OneToMany(mappedBy = "facture", cascade = { CascadeType.ALL })
	private List<Paiement> paiements;

	public Facture() {
	}

	public String getIdFacture() {
		return this.idFacture;
	}

	public void setIdFacture(String idFacture) {
		this.idFacture = idFacture;
	}

	public String getAdresse() {
		return this.adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getConditionVente() {
		return this.conditionVente;
	}

	public void setConditionVente(String conditionVente) {
		this.conditionVente = conditionVente;
	}

	public float getCoutLivraison() {
		return this.coutLivraison;
	}

	public void setCoutLivraison(float coutLivraison) {
		this.coutLivraison = coutLivraison;
	}

	public Date getDateCreation() {
		return this.dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public Date getDateEcheance() {
		return this.dateEcheance;
	}

	public void setDateEcheance(Date dateEcheance) {
		this.dateEcheance = dateEcheance;
	}

	public Date getDateLivraison() {
		return this.dateLivraison;
	}

	public void setDateLivraison(Date dateLivraison) {
		this.dateLivraison = dateLivraison;
	}

	public Date getDateModification() {
		return this.dateModification;
	}

	public void setDateModification(Date dateModification) {
		this.dateModification = dateModification;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEtat() {
		return this.etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getStatut() {
		return this.statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public BonCommande getBonCommande() {
		return this.bonCommande;
	}

	public void setBonCommande(BonCommande bonCommande) {
		this.bonCommande = bonCommande;
	}

	public Client getClient() {
		return this.client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Devis getDevi() {
		return this.devi;
	}

	public void setDevi(Devis devi) {
		this.devi = devi;
	}

	public Stockage getStockage() {
		return this.stockage;
	}

	public void setStockage(Stockage stockage) {
		this.stockage = stockage;
	}

	public Utilisateur getUtilisateur() {
		return this.utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public List<LigneFacture> getLigneFactures() {
		return this.ligneFactures;
	}

	public void setLigneFactures(List<LigneFacture> ligneFactures) {
		this.ligneFactures = ligneFactures;
	}

	public LigneFacture addLigneFacture(LigneFacture ligneFacture) {
		getLigneFactures().add(ligneFacture);
		ligneFacture.setFacture(this);

		return ligneFacture;
	}

	public LigneFacture removeLigneFacture(LigneFacture ligneFacture) {
		getLigneFactures().remove(ligneFacture);
		ligneFacture.setFacture(null);

		return ligneFacture;
	}

	public List<Paiement> getPaiements() {
		return this.paiements;
	}

	public void setPaiements(List<Paiement> paiements) {
		this.paiements = paiements;
	}

	public Paiement addPaiement(Paiement paiement) {
		getPaiements().add(paiement);
		paiement.setFacture(this);

		return paiement;
	}

	public Paiement removePaiement(Paiement paiement) {
		getPaiements().remove(paiement);
		paiement.setFacture(null);

		return paiement;
	}

	public String getSoumettreA() {
		return soumettreA;
	}

	public void setSoumettreA(String soumettreA) {
		this.soumettreA = soumettreA;
	}

	public float getPourcentageRemise() {
		return pourcentageRemise;
	}

	public void setPourcentageRemise(float pourcentageRemise) {
		this.pourcentageRemise = pourcentageRemise;
	}
}