package com.streenge.service.venteService;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.streenge.model.LigneDocument;
import com.streenge.model.admin.Entreprise;
import com.streenge.model.admin.Taxe;
import com.streenge.model.admin.Utilisateur;
import com.streenge.model.produit.Pack;
import com.streenge.model.produit.ProduitStockage;
import com.streenge.model.vente.BonCommande;
import com.streenge.model.vente.Devis;
import com.streenge.model.vente.facture.Facture;
import com.streenge.repository.LigneDocumentRepository;
import com.streenge.repository.adminRepository.EntrepriseRepository;
import com.streenge.repository.adminRepository.StockageRepository;
import com.streenge.repository.adminRepository.TaxeRepository;
import com.streenge.repository.adminRepository.UtilisateurRepository;
import com.streenge.repository.contactRepo.ClientRepository;
import com.streenge.repository.produitRepo.PackRepository;
import com.streenge.repository.venteRepo.DevisRepository;
import com.streenge.service.contactService.ClientService;
import com.streenge.service.utils.erroclass.ErrorAPI;
import com.streenge.service.utils.erroclass.StreengeException;
import com.streenge.service.utils.formInt.FormPanier;
import com.streenge.service.utils.formInt.LinePanier;
import com.streenge.service.utils.formOut.FormPack;
import com.streenge.service.utils.formOut.FormTable;
import com.streenge.service.utils.formOut.RowTable;
import com.streenge.service.utils.streengeData.ColorStatut;
import com.streenge.service.utils.streengeData.Data;
import com.streenge.service.utils.streengeData.Etat;
import com.streenge.service.utils.streengeData.Profil;
import com.streenge.service.utils.streengeData.Statut;
import com.streenge.service.utils.streengeFunction.Methode;

@Service
public class DevisService {

	@Autowired
	private DevisRepository devisRepository;

	@Autowired
	private EntrepriseRepository entrepriseRepository;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private StockageRepository stockageRepository;

	@Autowired
	private PackRepository packRepository;

	@Autowired
	private TaxeRepository taxeRepository;

	@Autowired
	private LigneDocumentRepository ligneDocumentRepository;

	@Autowired
	private ClientService clientService;

	@Autowired
	private FactureService factureService;

	@Autowired
	private BonCommandeService bonCommandeService;
	
	@Autowired
	private UtilisateurRepository utilisateurRepository;

	public Devis createOrUpdateDevis(FormPanier formPanier, boolean isNewdevi) {
		Methode.controlFormPanier(formPanier, true);
		if (formPanier.getNote()!=null) {
			formPanier.getNote().trim();
		}
		
		if (formPanier.getDescription()!=null) {
			formPanier.getDescription().trim();
		}
		
		Utilisateur utilisateur = formPanier.getUtilisateur();
		if (utilisateur!=null && utilisateur.getPassword().isBlank()) {
			utilisateur.setPassword(".");
		}
		if (utilisateur != null && utilisateur.getProfil() != Profil.getRoot()
				&& utilisateurRepository.findById(utilisateur.getId()).isPresent() && utilisateur.getPassword()
						.equals(utilisateurRepository.findById(utilisateur.getId()).get().getPassword())) {
			utilisateur = utilisateurRepository.findById(utilisateur.getId()).get();
			formPanier.setUtilisateur(utilisateur);
		} else {
			throw new StreengeException(
					new ErrorAPI("Impossible de creer/modifier ce devis...! Utilisateur Non defini..!"));
		}
		
		Devis devi;
		if (isNewdevi) {
			devi = new Devis();
			devi.setIdDevis(Methode.createID(devisRepository, "DV"));
			formPanier.setDateCreation(new Date());
		} else {
			devi = devisRepository.findById(formPanier.getIdDevis().toUpperCase()).get();
			for (LigneDocument ligne : devi.getLigneDocuments()) {
				ligneDocumentRepository.deleteLigneDocument(ligne.getId());
			}
			devi.setDateModification(new Date());
		}
		if (!clientRepository.findByNom(formPanier.getNomClient()).isEmpty()) {
			devi.setClient(clientRepository.findByNom(formPanier.getNomClient()).get(0));
		}else {
			devi.setClient(null);
		}
		// set
		// facture.setConditionVente(formPanier.getNote());
		devi.setNote(formPanier.getNote());
		devi.setCoutLivraison(formPanier.getCoutLivraison());
		devi.setDateCreation(formPanier.getDateCreation());
		devi.setDateExpiration(formPanier.getDateExpiration());
		devi.setDateLivraison(formPanier.getDateLivraison());
		devi.setDescription(formPanier.getDescription());
		devi.setSoumettreA(formPanier.getSoumettreA());
		devi.setPourcentageRemise(formPanier.getPourcentageRemise());
		devi.setUtilisateur(formPanier.getUtilisateur());
		if (isNewdevi) {
			devi.setEtat(Etat.getActif());
			devi.setStatut(Statut.getEnattente());
		}
		if (stockageRepository.findById(formPanier.getIdStockage()).isPresent()) {
			devi.setStockage(stockageRepository.findById(formPanier.getIdStockage()).get());
		} else {
			ErrorAPI errorAPI = new ErrorAPI();
			errorAPI.setMessage(
					"L'enregistrement du devis n'est pas possible, Le stockage choisi N'existe pas...! Changer de lieu de stockage et cree une nouvelle facture..! ");
			throw new StreengeException(errorAPI);
		}

		List<LigneDocument> ligneDevis = new ArrayList<LigneDocument>();
		List<LinePanier> lines = formPanier.getLines();
		for (LinePanier line : lines) {

			LigneDocument ligneDevi = new LigneDocument();
			ligneDevi.setDevi(devi);
			ligneDevi.setDesignation(line.getDesignation());
			ligneDevi.setPrix(line.getPrix());
			ligneDevi.setQuantite(line.getQuantite());
			ligneDevi.setRemise(line.getRemise());
 
			if (line.getTaxe() != null)
				ligneDevi.setTaxe(taxeRepository.findById(line.getTaxe().getId()).get());

			if (packRepository.findById(line.getIdPack()).isPresent()) {
				ligneDevi.setPack(packRepository.findById(line.getIdPack()).get());
			} else {
				ErrorAPI errorAPI = new ErrorAPI();
				errorAPI.setMessage("L'enregistrement du devis n'est pas possible,Le produit: "
						+ line.getDesignation().toUpperCase()
						+ " N'existe pas où alors ce produit à été récemment supprimer");
				throw new StreengeException(errorAPI);
			}
			ligneDevis.add(ligneDevi);
		}

		devi.setLigneDocuments(ligneDevis);

		try {
			devi = devisRepository.save(devi);
		} catch (DataIntegrityViolationException e) {
			System.out.println("Devis already exist");
		}
		return devi;
	}

	public FormPanier getPanierDevis(String idDevis) {
		// updateStatutFacture(idFacture);
		FormPanier formPanier = new FormPanier();
		List<LinePanier> linePaniers = new ArrayList<LinePanier>();
		List<FormPack> listFormPacks = new ArrayList<FormPack>();
		// List<FormProduit> listFormProduits = new ArrayList<FormProduit>();

		LinePanier linePanier = null;
		Devis devi = devisRepository.findById(idDevis.toUpperCase()).get();

		List<LigneDocument> ligneDevis = devi.getLigneDocuments();
		for (LigneDocument ligneDevi : ligneDevis) {
			linePanier = new LinePanier();

			linePanier.setDesignation(ligneDevi.getDesignation());
			linePanier.setPrix(ligneDevi.getPrix());
			linePanier.setPrixMin(ligneDevi.getPack().getPrixVenteMin());
			linePanier.setQuantite(ligneDevi.getQuantite());
			linePanier.setRemise(ligneDevi.getRemise());
			if (ligneDevi.getTaxe() != null) {
				Taxe taxe = ligneDevi.getTaxe();
				Taxe taxe1 = new Taxe();
				taxe1.setId(taxe.getId());
				taxe1.setAbreviation(taxe.getAbreviation());
				taxe1.setTaux(taxe.getTaux());
				taxe1.setNom(taxe.getNom());
				linePanier.setTaxe(taxe1);
			}
			linePanier.setIdPack(ligneDevi.getPack().getId());
			System.out.println("==Designation==" + linePanier.getDesignation());
			int i = 0;
			for (FormPack pack : listFormPacks) {
				if (pack.getIdPack() == linePanier.getIdPack()) {
					linePanier.setIndexPack(i);
				}
				i++;
			}

			if (linePanier.getIndexPack() == -1) {
				Pack pack = ligneDevi.getPack();
				FormPack formPack = new FormPack();
				formPack.setIdPack(pack.getId());
				formPack.setSku(pack.getProduit().getSku());
				formPack.setNombreUnite(pack.getNombreUnite());
				formPack.setIdProduit(pack.getProduit().getIdProduit());
				formPack.setDesignation(ligneDevi.getDesignation());
				formPack.setType(pack.getProduit().getType());
				if (pack.getPrixVente() > 0) {
					formPack.setPrixVente(pack.getPrixVente());
				} else {
					formPack.setPrixVente(pack.getProduit().getPrixVente() * pack.getNombreUnite());
				}
				if (pack.getPrixAchat() > 0) {
					formPack.setPrixAchat(pack.getPrixAchat());
				} else {
					formPack.setPrixAchat(pack.getProduit().getPrixAchat() * pack.getNombreUnite());
				}

				formPack.setPrixVenteMin(ligneDevi.getPack().getPrixVenteMin());
				formPack.setCoutUMP(pack.getProduit().getCout_UMP() * pack.getNombreUnite());

				for (ProduitStockage produitStock : pack.getProduit().getProduitStockages()) {
					if (produitStock.getStockage().getIdStockage() == devi.getStockage().getIdStockage()) {
						formPack.setDisponible(produitStock.getStockDisponible() / pack.getNombreUnite());
						formPack.setReel(produitStock.getStockReel() / pack.getNombreUnite());
						formPack.setAvenir(produitStock.getStockAvenir() / pack.getNombreUnite());
					}
				}

				listFormPacks.add(formPack);
				linePanier.setIndexPack(listFormPacks.size() - 1);
			}
			
			if (Etat.getDelete().equals(ligneDevi.getPack().getProduit().getEtat())) {
				linePanier.setIsDelete(true);
			}

			linePaniers.add(linePanier);
		}

		formPanier.setIdDevis(devi.getIdDevis());
		formPanier.setNomClient((devi.getClient() != null) ? devi.getClient().getNom() : "");
		formPanier.setContact(
				(devi.getClient() != null) ? clientService.getClient(devi.getClient().getIdClient()) : null);
		formPanier.setCoutLivraison(devi.getCoutLivraison());
		formPanier.setDateCreation(devi.getDateCreation());
		formPanier.setDateModification(devi.getDateModification());
		formPanier.setDateLivraison(devi.getDateLivraison());
		formPanier.setDateExpiration(devi.getDateExpiration());
		formPanier.setDescription(devi.getDescription());
		formPanier.setIdStockage(devi.getStockage().getIdStockage());
		formPanier.setNomStockage(devi.getStockage().getNom());
		formPanier.setStockage(devi.getStockage());
		formPanier.setNote(devi.getNote());
		formPanier.setSoumettreA(devi.getSoumettreA());
		formPanier.setPourcentageRemise(devi.getPourcentageRemise());
		formPanier.setTaxe((linePaniers!=null && !linePaniers.isEmpty())?linePaniers.get(0).getTaxe():null);
		formPanier.setUtilisateur(devi.getUtilisateur());
		formPanier.setStatut(devi.getStatut());
		formPanier.setColorStatut(ColorStatut.getColor(devi.getStatut()));

		// Choix de l'entreprise a afficher sur les documents
		if (!((List<Entreprise>) entrepriseRepository.findAll()).isEmpty()) {
			formPanier.setEntreprise(((List<Entreprise>) entrepriseRepository.findAll()).get(0));
		}

		if (!devi.getFactures().isEmpty()) {
			formPanier.setIdFacture(devi.getFactures().get(0).getIdFacture());
		} else if (!devi.getBonCommandes().isEmpty()) {
			formPanier.setIdBonCommande(devi.getBonCommandes().get(0).getIdBonCommande());
		}

		// formPanier.setPaiements(formPaiements);
		formPanier.setLines(linePaniers);
		formPanier.setPacksPresent(listFormPacks);

		return formPanier;
	}

	public void changeStatutDevis(String idDevis, Boolean isAccepted) {
		Devis devis;
		if (devisRepository.findById(idDevis).isPresent()) {
			devis = devisRepository.findById(idDevis).get();
			devis.setStatut((isAccepted ? Statut.getAccepter() : Statut.getRejeter()));
			devisRepository.save(devis);
		} else {
			throw new StreengeException(new ErrorAPI("Le devis que vous essayer de mettre a jour n'existe pas"));
		}
	}

	public Facture tranformDevisToFacture(String idDevis, Utilisateur utilisateur) {
		if (devisRepository.findById(idDevis).isPresent()
				&& devisRepository.findById(idDevis).get().getFactures().isEmpty()
				&& devisRepository.findById(idDevis).get().getBonCommandes().isEmpty()) {
			FormPanier formPanier = getPanierDevis(idDevis);

			formPanier.setUtilisateur(utilisateur);
			Facture facture = factureService.createOrUpdateFacture(formPanier, true);
			Devis devis = devisRepository.findById(idDevis).get();
			devis.setStatut(Statut.getAcceptertransformer());
			List<Facture> factures = new ArrayList<Facture>();
			factures.add(facture);
			devis.setFactures(factures);
			devisRepository.save(devis);
			return facture;
		} else {
			throw new StreengeException(new ErrorAPI("Impossible de créer une facture á partir de ce devis."
					+ (devisRepository.findById(idDevis).isPresent()
							? " Ce devis a déjâ été transformer par un autre utilisateur"
							: "")));
		}
	}

	public BonCommande tranformDevisToBonCommande(String idDevis, Utilisateur utilisateur) {
		if (devisRepository.findById(idDevis).isPresent()
				&& devisRepository.findById(idDevis).get().getFactures().isEmpty()
				&& devisRepository.findById(idDevis).get().getBonCommandes().isEmpty()) {
			FormPanier formPanier = getPanierDevis(idDevis);

			formPanier.setUtilisateur(utilisateur);
			BonCommande bonCommande = bonCommandeService.createOrUpdateBonCommande(formPanier, true);
			Devis devis = devisRepository.findById(idDevis).get();
			devis.setStatut(Statut.getAcceptertransformer());
			List<BonCommande> bonCommandes = new ArrayList<BonCommande>();
			bonCommandes.add(bonCommande);
			devis.setBonCommandes(bonCommandes);
			devisRepository.save(devis);
			return bonCommande;
		} else {
			throw new StreengeException(new ErrorAPI("Impossible de créer une Bon de commande á partir de ce devis."
					+ (devisRepository.findById(idDevis).isPresent()
							? " Ce devis a déjâ été transformer par un autre utilisateur"
							: "")));
		}
	}

	public FormTable generateListDevis(String filter) {
		List<Devis> listDevis = new ArrayList<Devis>();
		List<RowTable> rows = new ArrayList<RowTable>();
		SimpleDateFormat formater = new SimpleDateFormat("dd MMMM yyyy");
		DecimalFormat df = new DecimalFormat("###,###,###,###.##");

		if (filter != null) {
			listDevis = devisRepository.findByClientNomStartingWithOrderByIdDevisDesc(filter);
			if (listDevis.size() < 1) {
				listDevis = devisRepository
						.findByIdDevisStartingWithOrUtilisateurNomStartingWithOrStatutStartingWithOrderByIdDevisDesc(
								filter, filter, filter);
			}
			if (listDevis.size() < 1) {
				listDevis = devisRepository.findByStockageNomStartingWithOrderByIdDevisDesc(filter);
			}
		} else {
			listDevis = devisRepository.findAllByOrderByIdDevisDesc();
		}

		for (Devis devi : listDevis) {
			if (devi.getEtat().equals(Etat.getActif())) {
				RowTable row = new RowTable();
				List<String> dataList = new ArrayList<String>();
				dataList.add(devi.getIdDevis());
				dataList.add((devi.getClient() != null) ? devi.getClient().getNom() : null);
				dataList.add(devi.getStockage().getNom());
				dataList.add(formater.format(devi.getDateCreation()));
				dataList.add(df.format(Methode.getQuantiteTotalDocument(devi.getLigneDocuments())));
				dataList.add(
						df.format(Methode.getMomtantTolalDocument(devi.getLigneDocuments(), devi.getCoutLivraison()))
								+ " " + Data.getDevise());
				row.setData(dataList);
				row.setStatut(devi.getStatut());
				row.setColorStatut(ColorStatut.getColor(devi.getStatut()));
				row.setId(devi.getIdDevis());
				row.setIdUtilisateur((devi.getUtilisateur() != null) ? devi.getUtilisateur().getId() : 0);
				row.setIdStockage(devi.getStockage().getIdStockage());
				rows.add(row);
			}
		}

		List<String> colunm = new ArrayList<String>();
		colunm.add("DEVIS #");
		colunm.add("CLIENT");
		colunm.add("LIEU DE STOCKAGE");
		colunm.add("DATE DE CRÉATION");
		colunm.add("QUANTITÉS");
		colunm.add("TOTAL");

		FormTable formTable = new FormTable();
		formTable.setColunm(colunm);
		formTable.setRows(rows);

		return formTable;
	}
}
