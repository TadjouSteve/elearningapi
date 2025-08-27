package com.streenge.service.utils.formInt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.streenge.model.admin.Entreprise;
import com.streenge.model.admin.Stockage;
import com.streenge.model.admin.Taxe;
import com.streenge.model.admin.Utilisateur;
import com.streenge.model.admin.money.Caisse;
import com.streenge.model.produit.Categorie;
import com.streenge.service.utils.formOut.FormPack;
import com.streenge.service.utils.formOut.FormPaiement;
import com.streenge.service.utils.formOut.FormProduit;

public class FormPanier {

	private String				idFacture			= "";
	private String				idDevis				= "";
	private String				idBonCommande		= "";
	private String				idBonLivraison		= "";
	private String				idOrdreAchat		= "";
	private String				idLivraison			= "";
	private String				idEntreStock		= "";
	private String				idSortieStock		= "";
	private String				idMouvementStock	= "";
	private String				idFicheDepense		= "";
	private List<LinePanier>	lines				= new ArrayList<LinePanier>();
	private List<FormPack>		packsPresent		= new ArrayList<FormPack>();
	private List<FormProduit>	produitsPresent		= new ArrayList<FormProduit>();
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private List<Categorie>		categories			= new ArrayList<Categorie>();
	private float				coutLivraison		= 0;
	private String				nomClient			= "";
	private String				soumettreA			= "";
	private String				nomFournisseur		= "";
	private String				nomStockage			= "";
	private int					idStockage			= 1;
	private float               pourcentageRemise   = 0;
	private Taxe				taxe				= null;
	private Taxe				taxeLivraison		= null;
	private String				adresse				= "";
	private String				note				= "";
	private String				description			= "";
	private String				motif				= "";
	private Date				echeance			= null;
	private Date				dateCreation		= null;
	private Date				dateExpiration		= null;
	private Date				dateLivraison		= null;

	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private Caisse	caisse		= null;
	private boolean	useCaisse	= false;

	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private Utilisateur utilisateur = null;

	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private Utilisateur utilisateurAlter = null;

	private String	statut		= "";
	private String	colorStatut	= "";

	private FormCreateContact	contact		= null;
	private Entreprise			entreprise	= new Entreprise();
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private Stockage			stockage	= new Stockage();

	private boolean	addLine			= true;
	private boolean	changeStockage	= true;

	private String	nomStockageArriver	= "";
	private int		idStockageArriver	= 0;

	private List<FormPaiement> paiements = new ArrayList<FormPaiement>();

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreaction) {
		this.dateCreation = dateCreaction;
	}

	public Date getDateModification() {
		return dateModification;
	}

	public void setDateModification(Date dateModification) {
		this.dateModification = dateModification;
	}

	private Date dateModification = null;

	public FormPanier() {
		// TODO Auto-generated constructor stub
	}

	public List<LinePanier> getLines() {
		return lines;
	}

	public void setLines(List<LinePanier> lines) {
		this.lines = lines;
	}

	public void addLine(LinePanier linePanier) {
		if (this.lines != null && linePanier != null) {
			this.lines.add(linePanier);
		} else if (linePanier != null) {
			this.lines = new ArrayList<>();
			this.lines.add(linePanier);
		}
	}

	public List<FormPack> getPacksPresent() {
		return packsPresent;
	}

	public void setPacksPresent(List<FormPack> packsPresent) {
		this.packsPresent = packsPresent;
	}

	public float getCoutLivraison() {
		return coutLivraison;
	}

	public void setCoutLivraison(float coutLivraison) {
		this.coutLivraison = coutLivraison;
	}

	public String getNomClient() {
		return nomClient;
	}

	public void setNomClient(String nomClient) {
		this.nomClient = nomClient;
	}

	public String getNomFournisseur() {
		return nomFournisseur;
	}

	public void setNomFournisseur(String nomFournisseur) {
		this.nomFournisseur = nomFournisseur;
	}

	public int getIdStockage() {
		return idStockage;
	}

	public void setIdStockage(int idStockage) {
		this.idStockage = idStockage;
	}

	public Taxe getTaxe() {
		return taxe;
	}

	public void setTaxe(Taxe taxe) {
		this.taxe = taxe;
	}

	public Taxe getTaxeLivraison() {
		return taxeLivraison;
	}

	public void setTaxeLivraison(Taxe taxeLivraison) {
		this.taxeLivraison = taxeLivraison;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getEcheance() {
		return echeance;
	}

	public void setEcheance(Date echeance) {
		this.echeance = echeance;
	}

	public String getIdFacture() {
		return idFacture;
	}

	public void setIdFacture(String idPanier) {
		this.idFacture = idPanier;
	}

	public String getNomStockage() {
		return nomStockage;
	}

	public void setNomStockage(String nomStockage) {
		this.nomStockage = nomStockage;
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

	public List<FormPaiement> getPaiements() {
		return paiements;
	}

	public void setPaiements(List<FormPaiement> paiements) {
		this.paiements = paiements;
	}

	public FormCreateContact getContact() {
		return contact;
	}

	public void setContact(FormCreateContact contact) {
		this.contact = contact;
	}

	public Entreprise getEntreprise() {
		return entreprise;
	}

	public void setEntreprise(Entreprise entreprise) {
		this.entreprise = entreprise;
	}

	public List<FormProduit> getProduitsPresent() {
		return produitsPresent;
	}

	public void setProduitsPresent(List<FormProduit> produitsPresent) {
		this.produitsPresent = produitsPresent;
	}

	public Date getDateExpiration() {
		return dateExpiration;
	}

	public void setDateExpiration(Date dateExpiration) {
		this.dateExpiration = dateExpiration;
	}

	public String getIdDevis() {
		return idDevis;
	}

	public void setIdDevis(String idDevis) {
		this.idDevis = idDevis;
	}

	public String getIdBonCommande() {
		return idBonCommande;
	}

	public void setIdBonCommande(String idBonCommande) {
		this.idBonCommande = idBonCommande;
	}

	public String getSoumettreA() {
		return soumettreA;
	}

	public void setSoumettreA(String soumettreA) {
		this.soumettreA = soumettreA;
	}

	public Date getDateLivraison() {
		return dateLivraison;
	}

	public void setDateLivraison(Date dateLivraison) {
		this.dateLivraison = dateLivraison;
	}

	public String getIdBonLivraison() {
		return idBonLivraison;
	}

	public void setIdBonLivraison(String idBonLivraison) {
		this.idBonLivraison = idBonLivraison;
	}

	public String getIdOrdreAchat() {
		return idOrdreAchat;
	}

	public void setIdOrdreAchat(String idOrdreAchat) {
		this.idOrdreAchat = idOrdreAchat;
	}

	public boolean isAddLine() {
		return addLine;
	}

	public void setAddLine(boolean addLine) {
		this.addLine = addLine;
	}

	public String getIdLivraison() {
		return idLivraison;
	}

	public void setIdLivraison(String idLivraison) {
		this.idLivraison = idLivraison;
	}

	public String getIdEntreStock() {
		return idEntreStock;
	}

	public void setIdEntreStock(String idEntreStock) {
		this.idEntreStock = idEntreStock;
	}

	public String getIdSortieStock() {
		return idSortieStock;
	}

	public void setIdSortieStock(String idSortieStock) {
		this.idSortieStock = idSortieStock;
	}

	public String getIdMouvementStock() {
		return idMouvementStock;
	}

	public void setIdMouvementStock(String idMouvementStock) {
		this.idMouvementStock = idMouvementStock;
	}

	public boolean isChangeStockage() {
		return changeStockage;
	}

	public void setChangeStockage(boolean changeStockage) {
		this.changeStockage = changeStockage;
	}

	public String getNomStockageArriver() {
		return nomStockageArriver;
	}

	public void setNomStockageArriver(String nomStockageArriver) {
		this.nomStockageArriver = nomStockageArriver;
	}

	public int getIdStockageArriver() {
		return idStockageArriver;
	}

	public void setIdStockageArriver(int idStockageArriver) {
		this.idStockageArriver = idStockageArriver;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public Utilisateur getUtilisateurAlter() {
		return utilisateurAlter;
	}

	public void setUtilisateurAlter(Utilisateur utilisateurAlter) {
		this.utilisateurAlter = utilisateurAlter;
	}

	public Stockage getStockage() {
		return stockage;
	}

	public void setStockage(Stockage stockage) {
		this.stockage = stockage;
	}

	public String getIdFicheDepense() {
		return idFicheDepense;
	}

	public void setIdFicheDepense(String idFicheDepense) {
		this.idFicheDepense = idFicheDepense;
	}

	public String getMotif() {
		return motif;
	}

	public void setMotif(String motif) {
		this.motif = motif;
	}

	public Caisse getCaisse() {
		return caisse;
	}

	public void setCaisse(Caisse caisse) {
		this.caisse = caisse;
	}

	public boolean isUseCaisse() {
		return useCaisse;
	}

	public void setUseCaisse(boolean useCaisse) {
		this.useCaisse = useCaisse;
	}

	public List<Categorie> getCategories() {
		return categories;
	}

	public void setCategories(List<Categorie> categories) {
		this.categories = categories;
	}

	public float getPourcentageRemise() {
		return pourcentageRemise;
	}

	public void setPourcentageRemise(float pourcentageRemise) {
		this.pourcentageRemise = pourcentageRemise;
	}

}
