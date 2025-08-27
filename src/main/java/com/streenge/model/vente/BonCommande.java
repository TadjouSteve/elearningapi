package com.streenge.model.vente;

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
import com.streenge.model.LigneDocument;
import com.streenge.model.admin.Stockage;
import com.streenge.model.admin.Utilisateur;
import com.streenge.model.contact.Client;
import com.streenge.model.vente.facture.Facture;

/**
 * The persistent class for the bon_commande database table.
 * 
 */
@Entity
@Table(name = "bon_commande")
@NamedQuery(name = "BonCommande.findAll", query = "SELECT b FROM BonCommande b")
public class BonCommande implements Serializable {
	private static final long	serialVersionUID	= 1L;
	@Id
	@Column(name = "id_bon_commande", unique = true, nullable = false, length = 30)
	private String				idBonCommande;

	@Column(name = "adresse_facturation", length = 150)
	private String adresseFacturation;

	@Column(name = "adresse_livraison", length = 150)
	private String adresseLivraison;

	@Column(name = "condition_vente", length = 255)
	private String conditionVente;

	@Column(name = "cout_livraison")
	private float coutLivraison;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_creation")
	private Date dateCreation;

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

	@Column(name = "reference_externe", length = 150)
	private String referenceExterne;

	private float remise;

	@Column(name = "soumettre_a", length = 255)
	private String soumettreA;
	
	@Column(name = "pourcentage_remise")
	private float pourcentageRemise;

	@Column(length = 30)
	private String statut;

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

	// bi-directional many-to-one association to BonLivraison
	@JsonIgnore
	@OneToMany(mappedBy = "bonCommande", cascade = { CascadeType.ALL })
	private List<BonLivraison> bonLivraisons;

	// bi-directional many-to-one association to Facture
	@JsonIgnore
	@OneToMany(mappedBy = "bonCommande", cascade = { CascadeType.ALL })
	private List<Facture> factures;

	// bi-directional many-to-one association to LigneDocument
	@JsonIgnore
	@OneToMany(mappedBy = "bonCommande", cascade = { CascadeType.ALL })
	private List<LigneDocument> ligneDocuments;

	public BonCommande() {
	}

	public String getIdBonCommande() {
		return this.idBonCommande;
	}

	public void setIdBonCommande(String idBonCommande) {
		this.idBonCommande = idBonCommande;
	}

	public String getAdresseFacturation() {
		return this.adresseFacturation;
	}

	public void setAdresseFacturation(String adresseFacturation) {
		this.adresseFacturation = adresseFacturation;
	}

	public String getAdresseLivraison() {
		return this.adresseLivraison;
	}

	public void setAdresseLivraison(String adresseLivraison) {
		this.adresseLivraison = adresseLivraison;
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

	public String getReferenceExterne() {
		return this.referenceExterne;
	}

	public void setReferenceExterne(String referenceExterne) {
		this.referenceExterne = referenceExterne;
	}

	public float getRemise() {
		return this.remise;
	}

	public void setRemise(float remise) {
		this.remise = remise;
	}

	public String getSoumettreA() {
		return this.soumettreA;
	}

	public void setSoumettreA(String soumettreA) {
		this.soumettreA = soumettreA;
	}

	public String getStatut() {
		return this.statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
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

	public List<BonLivraison> getBonLivraisons() {
		return this.bonLivraisons;
	}

	public void setBonLivraisons(List<BonLivraison> bonLivraisons) {
		this.bonLivraisons = bonLivraisons;
	}

	public BonLivraison addBonLivraison(BonLivraison bonLivraison) {
		getBonLivraisons().add(bonLivraison);
		bonLivraison.setBonCommande(this);

		return bonLivraison;
	}

	public BonLivraison removeBonLivraison(BonLivraison bonLivraison) {
		getBonLivraisons().remove(bonLivraison);
		bonLivraison.setBonCommande(null);

		return bonLivraison;
	}

	public List<Facture> getFactures() {
		return this.factures;
	}

	public void setFactures(List<Facture> factures) {
		this.factures = factures;
	}

	public Facture addFacture(Facture facture) {
		getFactures().add(facture);
		facture.setBonCommande(this);

		return facture;
	}

	public Facture removeFacture(Facture facture) {
		getFactures().remove(facture);
		facture.setBonCommande(null);

		return facture;
	}

	public List<LigneDocument> getLigneDocuments() {
		return this.ligneDocuments;
	}

	public void setLigneDocuments(List<LigneDocument> ligneDocuments) {
		this.ligneDocuments = ligneDocuments;
	}

	public LigneDocument addLigneDocument(LigneDocument ligneDocument) {
		getLigneDocuments().add(ligneDocument);
		ligneDocument.setBonCommande(this);

		return ligneDocument;
	}

	public LigneDocument removeLigneDocument(LigneDocument ligneDocument) {
		getLigneDocuments().remove(ligneDocument);
		ligneDocument.setBonCommande(null);

		return ligneDocument;
	}

	public float getPourcentageRemise() {
		return pourcentageRemise;
	}

	public void setPourcentageRemise(float pourcentageRemise) {
		this.pourcentageRemise = pourcentageRemise;
	}

}