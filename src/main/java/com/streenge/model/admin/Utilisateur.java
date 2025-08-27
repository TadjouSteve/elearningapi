package com.streenge.model.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.streenge.model.admin.money.PaiementAbonnement;
import com.streenge.model.depense.FicheDepense;
import com.streenge.model.stock.Livraison;
import com.streenge.model.stock.MouvementStock;
import com.streenge.model.stock.OrdreAchat;
import com.streenge.model.stock.RegistreStock;
import com.streenge.model.vente.BonCommande;
import com.streenge.model.vente.BonLivraison;
import com.streenge.model.vente.Devis;
import com.streenge.model.vente.facture.Facture;

/**
 * The persistent class for the utilisateur database table.
 * 
 */

@Entity
@NamedQuery(name = "Utilisateur.findAll", query = "SELECT u FROM Utilisateur u")
public class Utilisateur implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private int id;

	private String adresse;

	private String email;

	private String etat;

	private String login;

	private String nom;

	@Column(name = "non_affichage")
	private String nomAffichage;

	private String password;

	private String statut;

	private String telephone;
	
	@Column(name = "last_connection")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastConnection;

	@Column(name = "produit_rupture", length = 45)
	private String produitRupture;

	@Column(length = 45)
	private String profil;

	@Column(name = "change_prix", length = 45)
	private String changePrix;
	
	@Column(name = "prix_inferieur_min", length = 45)
	private String prixInferieurMin;
	
	@Column(name = "faire_reduction", length = 45)
	private String faireReduction;

	// bi-directional many-to-one association to BonCommande
	@JsonIgnore
	@OneToMany(mappedBy = "utilisateur")
	private List<BonCommande> bonCommandes;

	@JsonIgnore
	@OneToMany(mappedBy = "utilisateur", cascade = { CascadeType.ALL })
	private List<BonLivraison> bonLivraisons;
	
	@JsonIgnore
	@OneToMany(mappedBy="utilisateur")
	private List<FicheDepense> ficheDepenses;

	//bi-directional many-to-one association to FicheDepense
	@JsonIgnore
	@OneToMany(mappedBy="utilisateurAlter")
	private List<FicheDepense> ficheDepensesAlter;
	
	// bi-directional many-to-one association to Devis
	@JsonIgnore
	@OneToMany(mappedBy = "utilisateur")
	private List<Devis> devis;

	// bi-directional many-to-one association to Facture
	@JsonIgnore
	@OneToMany(mappedBy = "utilisateur")
	private List<Facture> factures;

	// bi-directional many-to-one association to OrdreAchatService
	@JsonIgnore
	@OneToMany(mappedBy = "utilisateur")
	private List<OrdreAchat> ordreAchats;
	
	
	@JsonIgnore
	@OneToMany(mappedBy = "utilisateur", cascade = { CascadeType.ALL })
	private List<Livraison> livraisons;

	// bi-directional many-to-one association to MouvementStock
	@JsonIgnore
	@OneToMany(mappedBy = "utilisateur", cascade = { CascadeType.ALL })
	private List<MouvementStock> mouvementStocks;

	// bi-directional many-to-one association to RegistreStock
	@JsonIgnore
	@OneToMany(mappedBy = "utilisateur", cascade = { CascadeType.ALL })
	private List<RegistreStock> registreStocks;

	@JsonIgnore
	@OneToMany(mappedBy = "utilisateur", cascade = { CascadeType.ALL })
	private List<UtilisateurStockage> utilisateurStockages;

	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@Transient
	private List<Stockage> stockages = new ArrayList<Stockage>();
	
	@JsonIgnore
	@OneToMany(mappedBy="utilisateur")
	private List<PaiementAbonnement> paiementAbonnements;
	
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@Transient
	private Entreprise entreprise = new Entreprise();

	@Transient
	private String confirmPassword;

	public Utilisateur() {
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

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getNomAffichage() {
		return this.nomAffichage;
	}

	public void setNomAffichage(String nomAffichage) {
		this.nomAffichage = nomAffichage;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatut() {
		return this.statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getChangePrix() {
		return this.changePrix;
	}

	public void setChangePrix(String changePrix) {
		this.changePrix = changePrix;
	}

	public String getProduitRupture() {
		return this.produitRupture;
	}

	public void setProduitRupture(String produitRupture) {
		this.produitRupture = produitRupture;
	}

	public String getProfil() {
		return this.profil;
	}

	public void setProfil(String profil) {
		this.profil = profil;
	}

	public List<BonCommande> getBonCommandes() {
		return this.bonCommandes;
	}

	public void setBonCommandes(List<BonCommande> bonCommandes) {
		this.bonCommandes = bonCommandes;
	}

	public BonCommande addBonCommande(BonCommande bonCommande) {
		getBonCommandes().add(bonCommande);
		bonCommande.setUtilisateur(this);

		return bonCommande;
	}

	public BonCommande removeBonCommande(BonCommande bonCommande) {
		getBonCommandes().remove(bonCommande);
		bonCommande.setUtilisateur(null);

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
		devi.setUtilisateur(this);

		return devi;
	}

	public Devis removeDevi(Devis devi) {
		getDevis().remove(devi);
		devi.setUtilisateur(null);

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
		facture.setUtilisateur(this);

		return facture;
	}

	public Facture removeFacture(Facture facture) {
		getFactures().remove(facture);
		facture.setUtilisateur(null);

		return facture;
	}

	public List<OrdreAchat> getOrdreAchats() {
		return this.ordreAchats;
	}

	public void setOrdreAchats(List<OrdreAchat> ordreAchats) {
		this.ordreAchats = ordreAchats;
	}

	public OrdreAchat addOrdreAchat(OrdreAchat ordreAchat) {
		getOrdreAchats().add(ordreAchat);
		ordreAchat.setUtilisateur(this);

		return ordreAchat;
	}

	public OrdreAchat removeOrdreAchat(OrdreAchat ordreAchat) {
		getOrdreAchats().remove(ordreAchat);
		ordreAchat.setUtilisateur(null);

		return ordreAchat;
	}

	public List<MouvementStock> getMouvementStocks() {
		return this.mouvementStocks;
	}

	public void setMouvementStocks(List<MouvementStock> mouvementStocks) {
		this.mouvementStocks = mouvementStocks;
	}

	public MouvementStock addMouvementStock(MouvementStock mouvementStock) {
		getMouvementStocks().add(mouvementStock);
		mouvementStock.setUtilisateur(this);

		return mouvementStock;
	}

	public MouvementStock removeMouvementStock(MouvementStock mouvementStock) {
		getMouvementStocks().remove(mouvementStock);
		mouvementStock.setUtilisateur(null);

		return mouvementStock;
	}

	public List<RegistreStock> getRegistreStocks() {
		return this.registreStocks;
	}

	public void setRegistreStocks(List<RegistreStock> registreStocks) {
		this.registreStocks = registreStocks;
	}

	public RegistreStock addRegistreStock(RegistreStock registreStock) {
		getRegistreStocks().add(registreStock);
		registreStock.setUtilisateur(this);

		return registreStock;
	}

	public RegistreStock removeRegistreStock(RegistreStock registreStock) {
		getRegistreStocks().remove(registreStock);
		registreStock.setUtilisateur(null);

		return registreStock;
	}

	public List<UtilisateurStockage> getUtilisateurStockages() {
		return this.utilisateurStockages;
	}

	public void setUtilisateurStockages(List<UtilisateurStockage> utilisateurStockages) {
		this.utilisateurStockages = utilisateurStockages;
	}

	public UtilisateurStockage addUtilisateurStockage(UtilisateurStockage utilisateurStockage) {
		getUtilisateurStockages().add(utilisateurStockage);
		utilisateurStockage.setUtilisateur(this);

		return utilisateurStockage;
	}

	public UtilisateurStockage removeUtilisateurStockage(UtilisateurStockage utilisateurStockage) {
		getUtilisateurStockages().remove(utilisateurStockage);
		utilisateurStockage.setUtilisateur(null);

		return utilisateurStockage;
	}
	
	public List<BonLivraison> getBonLivraisons() {
		return this.bonLivraisons;
	}

	public void setBonLivraisons(List<BonLivraison> bonLivraisons) {
		this.bonLivraisons = bonLivraisons;
	}

	public BonLivraison addBonLivraison(BonLivraison bonLivraison) {
		getBonLivraisons().add(bonLivraison);
		bonLivraison.setUtilisateur(this);

		return bonLivraison;
	}

	public BonLivraison removeBonLivraison(BonLivraison bonLivraison) {
		getBonLivraisons().remove(bonLivraison);
		bonLivraison.setUtilisateur(null);

		return bonLivraison;
	}
	
	public List<Livraison> getLivraisons() {
		return this.livraisons;
	}

	public void setLivraisons(List<Livraison> livraisons) {
		this.livraisons = livraisons;
	}

	public Livraison addLivraison(Livraison livraison) {
		getLivraisons().add(livraison);
		livraison.setUtilisateur(this);

		return livraison;
	}

	public Livraison removeLivraison(Livraison livraison) {
		getLivraisons().remove(livraison);
		livraison.setUtilisateur(null);

		return livraison;
	}

	public List<Stockage> getStockages() {
		return stockages;
	}

	public void setStockages(List<Stockage> stockages) {
		this.stockages = stockages;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getPrixInferieurMin() {
		return prixInferieurMin;
	}

	public void setPrixInferieurMin(String prixInferieurMin) {
		this.prixInferieurMin = prixInferieurMin;
	}

	public String getFaireReduction() {
		return faireReduction;
	}

	public void setFaireReduction(String faireReduction) {
		this.faireReduction = faireReduction;
	}

	public Entreprise getEntreprise() {
		return entreprise;
	}

	public void setEntreprise(Entreprise entreprise) {
		this.entreprise = entreprise;
	}

	public List<FicheDepense> getFicheDepenses() {
		return ficheDepenses;
	}

	public void setFicheDepenses(List<FicheDepense> ficheDepenses) {
		this.ficheDepenses = ficheDepenses;
	}

	public List<FicheDepense> getFicheDepensesAlter() {
		return ficheDepensesAlter;
	}

	public void setFicheDepensesAlter(List<FicheDepense> ficheDepensesAlter) {
		this.ficheDepensesAlter = ficheDepensesAlter;
	}

	public Date getLastConnection() {
		return lastConnection;
	}

	public void setLastConnection(Date lastConnection) {
		this.lastConnection = lastConnection;
	}

	public List<PaiementAbonnement> getPaiementAbonnements() {
		return paiementAbonnements;
	}

	public void setPaiementAbonnements(List<PaiementAbonnement> paiementAbonnements) {
		this.paiementAbonnements = paiementAbonnements;
	}

}