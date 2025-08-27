package com.streenge.service.depenseService;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.streenge.model.admin.Entreprise;
import com.streenge.model.admin.Utilisateur;
import com.streenge.model.admin.money.Caisse;
import com.streenge.model.admin.money.HistoriqueCaisse;
import com.streenge.model.depense.FicheDepense;
import com.streenge.model.depense.LigneDepense;
import com.streenge.model.depense.PointDepense;
import com.streenge.model.produit.Pack;
import com.streenge.repository.adminRepository.EntrepriseRepository;
import com.streenge.repository.adminRepository.StockageRepository;
import com.streenge.repository.adminRepository.UtilisateurRepository;
import com.streenge.repository.adminRepository.moneyRepository.CaisseRepository;
import com.streenge.repository.adminRepository.moneyRepository.HistoriqueCaisseRepository;
import com.streenge.repository.depense.FicheDepenseRepository;
import com.streenge.repository.depense.LigneDepenseRepository;
import com.streenge.repository.depense.PointDepenseRepository;
import com.streenge.service.utils.erroclass.ErrorAPI;
import com.streenge.service.utils.erroclass.StreengeException;
import com.streenge.service.utils.formInt.FormFilter;
import com.streenge.service.utils.formInt.FormPanier;
import com.streenge.service.utils.formInt.LinePanier;
import com.streenge.service.utils.formOut.FormPack;
import com.streenge.service.utils.formOut.FormTable;
import com.streenge.service.utils.formOut.RowTable;
import com.streenge.service.utils.streengeData.ColorStatut;
import com.streenge.service.utils.streengeData.Etat;
import com.streenge.service.utils.streengeData.Profil;
import com.streenge.service.utils.streengeData.Statut;
import com.streenge.service.utils.streengeFunction.Methode;

@Service
public class FicheDepenseService {

	@Autowired
	private FicheDepenseRepository ficheDepenseRepository;

	@Autowired
	private EntrepriseRepository entrepriseRepository;

	@Autowired
	private UtilisateurRepository utilisateurRepository;

	@Autowired
	private LigneDepenseRepository ligneDepenseRepository;

	@Autowired
	private PointDepenseRepository pointDepenseRepository;

	@Autowired
	private CaisseRepository caisseRepository;

	@Autowired
	private HistoriqueCaisseRepository historiqueCaisseRepository;

	@Autowired
	private StockageRepository stockageRepository;

	public FicheDepense createOrUpdateFicheDepense(FormPanier formPanier, Boolean isNew) {

		float montantPrecedent = 0;
		float montantFinal = 0;

		Utilisateur utilisateur = formPanier.getUtilisateur();
		if (utilisateur != null && utilisateur.getPassword().isBlank()) {
			utilisateur.setPassword(".");
		}
		if (utilisateur != null && utilisateur.getProfil() != Profil.getRoot()
				&& utilisateurRepository.findById(utilisateur.getId()).isPresent() && utilisateur.getPassword()
						.equals(utilisateurRepository.findById(utilisateur.getId()).get().getPassword())) {
			utilisateur = utilisateurRepository.findById(utilisateur.getId()).get();
			formPanier.setUtilisateur(utilisateur);
		} else {
			throw new StreengeException(
					new ErrorAPI("Impossible de creer/modifier ce bon de commande...! Utilisateur Non defini..!"));
		}

		Caisse previewCaisse = null;

		if (formPanier.getMotif() != null && !formPanier.getMotif().isBlank() && formPanier.getMotif().length() < 2) {
			throw new StreengeException(
					new ErrorAPI("Le motif de la fiche de depense doit contenir au moins 3 carractères...!"));
		} else if (formPanier.getMotif() != null && formPanier.getMotif().length() > 150) {
			throw new StreengeException(new ErrorAPI(
					"Motif trop long...! Le motif de la fiche de depense  doit contenier au plus 150 carractères..!"));
		}

		if (formPanier.getLines().isEmpty()) {
			throw new StreengeException(
					new ErrorAPI("La fiche de depense doit contenir au moins un (01) point de depense "));
		}

		FicheDepense ficheDepense;
		if (isNew) {
			ficheDepense = new FicheDepense();
			ficheDepense.setIdFicheDepense(Methode.createID(ficheDepenseRepository, "FD"));
			ficheDepense.setDate(new Date());
			ficheDepense.setEtat(Etat.getActif());
			ficheDepense.setStatut(Statut.getAccepter());
			formPanier.setDateCreation(new Date());

			if (formPanier.getUtilisateur() != null
					&& utilisateurRepository.findById(formPanier.getUtilisateur().getId()).isPresent()) {
				ficheDepense.setUtilisateur(formPanier.getUtilisateur());
			} else {
				throw new StreengeException(new ErrorAPI("Utilisateur non definie"));
			}
		} else {
			ficheDepense = ficheDepenseRepository.findById(formPanier.getIdFicheDepense().toUpperCase()).get();
			if (formPanier.getUtilisateurAlter() != null
					&& utilisateurRepository.findById(formPanier.getUtilisateurAlter().getId()).isPresent()) {
				ficheDepense.setUtilisateurAlter(formPanier.getUtilisateurAlter());
			} else {
				throw new StreengeException(new ErrorAPI("Utilisateur operant la modification non definie"));
			}

			previewCaisse = ficheDepense.getCaisse();
			for (LigneDepense ligne : ficheDepense.getLigneDepenses()) {
				montantPrecedent += ligne.getMontant();
				ligneDepenseRepository.deleteLigneDepense(ligne.getId());
			}
			ficheDepense.setDateModification(new Date());
		}

		/*
		 * if (formPanier.getCaisse() != null &&
		 * caisseRepository.findById(formPanier.getCaisse().getId()).isPresent()) {
		 * ficheDepense.setCaisse(caisseRepository.findById(formPanier.getCaisse().getId
		 * ()).get()); }
		 */

		List<LigneDepense> ligneDepenses = new ArrayList<>();
		for (LinePanier linePanier : formPanier.getLines()) {
			montantFinal += linePanier.getPrix();
			LigneDepense ligneDepense = new LigneDepense();
			ligneDepense.setDesignation(linePanier.getDesignation());
			ligneDepense.setFicheDepense(ficheDepense);
			ligneDepense.setMontant(linePanier.getPrix());
			ligneDepense.setPointDepense(pointDepenseRepository.findById(linePanier.getIdPointDepense()).get());
			ligneDepenses.add(ligneDepense);
		}

		ficheDepense.setLigneDepenses(ligneDepenses);

		ficheDepense.setNote(formPanier.getNote());
		ficheDepense.setMotif(formPanier.getMotif());
		if (stockageRepository.findById(formPanier.getIdStockage()).isPresent()) {
			ficheDepense.setStockage(stockageRepository.findById(formPanier.getIdStockage()).get());
		} else {
			ErrorAPI errorAPI = new ErrorAPI();
			errorAPI.setMessage(
					"L'enregistrement de la fiche de depense  n'est pas possible, Le stockage choisi N'existe pas...! Changer de lieu de stockage et cree une nouvelle fiche de depense..! ");
			throw new StreengeException(errorAPI);
		}

		if (formPanier.isUseCaisse() && !ficheDepense.getStockage().getCaisses().isEmpty()) {
			ficheDepense.setCaisse(ficheDepense.getStockage().getCaisses().get(0));
		} else if (!formPanier.isUseCaisse()) {
			ficheDepense.setCaisse(null);
		}
		// ficheDepense.setStockage(formPanier.getStockage());

		try {
			ficheDepense = ficheDepenseRepository.save(ficheDepense);
			ajustementCaisse(ficheDepense, previewCaisse, ficheDepense.getCaisse(), montantFinal, montantPrecedent);
		} catch (DataIntegrityViolationException e) {
			System.out.println("Impossible de sucegarder la fiche de depense");
		}

		return ficheDepense;
	}

	public FormPanier getFormFicheDepense(String idFicheDepense) {
		FormPanier formPanier = new FormPanier();
		List<LinePanier> linePaniers = new ArrayList<LinePanier>();
		// List<FormPack> listFormPacks = new ArrayList<FormPack>();

		LinePanier linePanier = null;
		FicheDepense ficheDepense = new FicheDepense();
		if (ficheDepenseRepository.findById(idFicheDepense).isPresent()) {
			ficheDepense = ficheDepenseRepository.findById(idFicheDepense).get();
		} else {
			throw new StreengeException(new ErrorAPI("Cette fiche de depense n'existe pas...!"));
		}

		List<LigneDepense> ligneDepenses = ficheDepense.getLigneDepenses();
		for (LigneDepense ligneDepense : ligneDepenses) {
			linePanier = new LinePanier();
			linePanier.setDesignation(ligneDepense.getDesignation());
			linePanier.setPrix(ligneDepense.getMontant());
			linePanier.setIdPack(ligneDepense.getPointDepense().getId());
			linePanier.setSku(ligneDepense.getPointDepense().getReference());
			linePanier.setQuantite(1);
			linePanier.setIdPointDepense(ligneDepense.getPointDepense().getId());

			FormPack formPack = new FormPack();
			formPack.setIdPack(ligneDepense.getPointDepense().getId());
			formPack.setSku(ligneDepense.getPointDepense().getReference());

			formPanier.getPacksPresent().add(formPack);

			linePanier.setIndexPack(formPanier.getPacksPresent().size() - 1);

			linePaniers.add(linePanier);
		}

		formPanier.setLines(linePaniers);

		formPanier.setStatut(ficheDepense.getStatut());
		formPanier.setColorStatut(ColorStatut.getColor(ficheDepense.getStatut()));
		formPanier.setIdFicheDepense(idFicheDepense);
		formPanier.setUtilisateur(ficheDepense.getUtilisateur());
		formPanier.setUtilisateurAlter(ficheDepense.getUtilisateurAlter());
		formPanier.setCaisse(ficheDepense.getCaisse());
		formPanier.setDateCreation(ficheDepense.getDate());
		formPanier.setDateModification(ficheDepense.getDateModification());
		formPanier.setNote(ficheDepense.getNote());
		formPanier.setMotif(ficheDepense.getMotif());
		formPanier.setStockage(ficheDepense.getStockage());
		formPanier.setNomStockage(ficheDepense.getStockage().getNom());

		if (ficheDepense.getCaisse() != null) {
			formPanier.setUseCaisse(true);
		}

		if (!((List<Entreprise>) entrepriseRepository.findAll()).isEmpty()) {
			formPanier.setEntreprise(((List<Entreprise>) entrepriseRepository.findAll()).get(0));
		}

		return formPanier;
	}

	public FormTable getListFicheDepense(FormFilter formfilter) {

		if (formfilter.getUtilisateur() == null) {
			formfilter.setUtilisateur(new Utilisateur());
		}

		SimpleDateFormat formater = new SimpleDateFormat("dd MMMM yyyy");
		DecimalFormat df = new DecimalFormat("###,###,###,###.##");
		List<RowTable> rows = new ArrayList<RowTable>();

		List<FicheDepense> ficheDepenses = new ArrayList<>();
		String filter = formfilter.getFilter();
		if (filter != null) {
			filter = filter.trim();
			ficheDepenses = ficheDepenseRepository
					.findByIdFicheDepenseStartingWithOrUtilisateurNomStartingWithOrStatutStartingWithOrderByIdFicheDepenseDesc(
							filter, filter, filter);

		} else {
			ficheDepenses = ficheDepenseRepository.findAllByOrderByIdFicheDepenseAsc();
		}

		for (FicheDepense ficheDepense : ficheDepenses) {
			if (!Etat.getDelete().equals(ficheDepense.getEtat()) && (Methode.userLevel(formfilter.getUtilisateur()) > 2
					|| (Methode.userLevel(formfilter.getUtilisateur()) > 2 && Methode
							.stockageAccess(formfilter.getUtilisateur(), ficheDepense.getStockage().getIdStockage()))
					|| formfilter.getUtilisateur().getId() == ficheDepense.getUtilisateur().getId())) {
				RowTable row = new RowTable();
				List<String> dataList = new ArrayList<String>();
				dataList.add(ficheDepense.getIdFicheDepense());
				dataList.add((ficheDepense.getUtilisateur() != null) ? ficheDepense.getUtilisateur().getNom() : null);
				dataList.add(ficheDepense.getStockage().getNom());
				dataList.add(formater.format(ficheDepense.getDate()));
				dataList.add((ficheDepense.getCaisse() != null)
						? ("Caisse " + ficheDepense.getCaisse().getStockage().getNom())
						: "Externe");
				dataList.add(df.format(getTotalFicheDepense(ficheDepense)));

				row.setData(dataList);
				row.setStatut(ficheDepense.getStatut());
				row.setColorStatut(ColorStatut.getColor(ficheDepense.getStatut()));
				row.setId(ficheDepense.getIdFicheDepense());
				row.setIdUtilisateur(
						(ficheDepense.getUtilisateur() != null) ? ficheDepense.getUtilisateur().getId() : 0);
				rows.add(row);
			}
		}

		List<String> colunm = new ArrayList<String>();
		colunm.add("FICHE DEPENSE #");
		colunm.add("UTILISATEUR");
		colunm.add("LIEU DE STOCKAGE");
		colunm.add("DATE DE CRÉATION");
		colunm.add("CAISSE DE RETRAIT");
		colunm.add("TOTAL");

		FormTable formTable = new FormTable();
		formTable.setColunm(colunm);
		formTable.setRows(rows);

		return formTable;
	}

	public float getTotalFicheDepense(FicheDepense ficheDepense) {
		float total = 0;
		for (LigneDepense ligneDepense : ficheDepense.getLigneDepenses()) {
			total += ligneDepense.getMontant();
		}
		return total;
	}

	private void ajustementCaisse(FicheDepense ficheDepense, Caisse previewCaisse, Caisse newCaisse, float montantFinal,
			float montantPrecedent) {
		HistoriqueCaisse historiqueCaisseFirst = new HistoriqueCaisse();
		HistoriqueCaisse historiqueCaisseSecond = new HistoriqueCaisse();
		if (previewCaisse == null && newCaisse != null) {
			newCaisse = caisseRepository.findById(newCaisse.getId()).get();
			newCaisse.setSolde(newCaisse.getSolde() - montantFinal);
			historiqueCaisseFirst.setSolde(newCaisse.getSolde());
			historiqueCaisseFirst.setAjustement(montantFinal * -1);
			historiqueCaisseFirst.setCaisse(newCaisse);
			historiqueCaisseFirst.setDate(new Date());
			historiqueCaisseFirst.setIdDocument(ficheDepense.getIdFicheDepense());
			historiqueCaisseFirst
					.setUtilisateur((ficheDepense.getUtilisateurAlter() != null) ? ficheDepense.getUtilisateurAlter()
							: ficheDepense.getUtilisateur());
			historiqueCaisseFirst
					.setContext("Retrait de caisse pour payer la fiche de depense " + ficheDepense.getIdFicheDepense());

			caisseRepository.save(newCaisse);
			historiqueCaisseRepository.save(historiqueCaisseFirst);
		} else if (previewCaisse != null && newCaisse != null && newCaisse.getId() == previewCaisse.getId()) {
			if (montantFinal != montantPrecedent) {
				Caisse caisse = caisseRepository.findById(previewCaisse.getId()).get();
				caisse.setSolde(caisse.getSolde() + (montantPrecedent - montantFinal));
				historiqueCaisseFirst.setSolde(caisse.getSolde());
				historiqueCaisseFirst.setAjustement(montantPrecedent - montantFinal);
				historiqueCaisseFirst.setCaisse(caisse);
				historiqueCaisseFirst.setDate(new Date());
				historiqueCaisseFirst.setIdDocument(ficheDepense.getIdFicheDepense());
				historiqueCaisseFirst.setUtilisateur(
						(ficheDepense.getUtilisateurAlter() != null) ? ficheDepense.getUtilisateurAlter()
								: ficheDepense.getUtilisateur());
				historiqueCaisseFirst
						.setContext("Reajustement du solde suite à une modification du montant de la fiche de depense "
								+ ficheDepense.getIdFicheDepense());

				caisseRepository.save(caisse);
				historiqueCaisseRepository.save(historiqueCaisseFirst);
			}

		} else if (previewCaisse != null) {
			previewCaisse = caisseRepository.findById(previewCaisse.getId()).get();
			previewCaisse.setSolde(previewCaisse.getSolde() + montantPrecedent);

			historiqueCaisseFirst.setSolde(previewCaisse.getSolde());
			historiqueCaisseFirst.setAjustement(montantPrecedent * 1);
			historiqueCaisseFirst.setCaisse(previewCaisse);
			historiqueCaisseFirst.setDate(new Date());
			historiqueCaisseFirst.setIdDocument(ficheDepense.getIdFicheDepense());
			historiqueCaisseFirst
					.setUtilisateur((ficheDepense.getUtilisateurAlter() != null) ? ficheDepense.getUtilisateurAlter()
							: ficheDepense.getUtilisateur());
			historiqueCaisseFirst.setContext(
					"Reajustement du solde suite au changement de la caisse de retrait pour la fiche de depense "
							+ ficheDepense.getIdFicheDepense());

			caisseRepository.save(previewCaisse);
			historiqueCaisseRepository.save(historiqueCaisseFirst);

			if (newCaisse != null) {
				newCaisse = caisseRepository.findById(newCaisse.getId()).get();
				newCaisse.setSolde(newCaisse.getSolde() - montantFinal);
				historiqueCaisseSecond.setSolde(newCaisse.getSolde());
				historiqueCaisseSecond.setAjustement(montantFinal * -1);
				historiqueCaisseSecond.setCaisse(newCaisse);
				historiqueCaisseSecond.setDate(new Date());
				historiqueCaisseSecond.setIdDocument(ficheDepense.getIdFicheDepense());
				historiqueCaisseSecond.setUtilisateur(
						(ficheDepense.getUtilisateurAlter() != null) ? ficheDepense.getUtilisateurAlter()
								: ficheDepense.getUtilisateur());
				historiqueCaisseSecond.setContext(
						"Retrait de caisse pour payer la fiche de depense " + ficheDepense.getIdFicheDepense()
								+ " ,suite à une modifiction ou la caisse de retrait à été changer");

				caisseRepository.save(newCaisse);
				historiqueCaisseRepository.save(historiqueCaisseSecond);
			}

		}
	}

	public List<FormPack> getFormPacks(FormFilter formfilter) {

		if (formfilter.getUtilisateur() == null) {
			formfilter.setUtilisateur(new Utilisateur());
		}

		String filter = formfilter.getFilter();

		List<FormPack> listFormPacks = new ArrayList<FormPack>();
		// List<FormProduit> listFormProduits = new ArrayList<FormProduit>();
		List<PointDepense> listProduits;
		if (filter != null) {
			filter = filter.trim();
			listProduits = pointDepenseRepository.findByNomStartingWithOrReferenceStartingWithOrderByNomAsc(filter,
					filter);
			if (listProduits.size() < 1 && filter.length() >= 2) {
				listProduits = pointDepenseRepository.findByNomContainingOrReferenceContainingOrderByNomAsc(filter,
						filter);
			}

		} else {
			listProduits = pointDepenseRepository.findAllByOrderByNomAsc();
		}

		for (PointDepense pointDepense : listProduits) {
			if (!pointDepense.getEtat().equals(Etat.getDelete())
					&& Methode.userLevel(formfilter.getUtilisateur()) >= pointDepense.getUserLevel()) {
				Pack pack = new Pack();
				pack.setId(pointDepense.getId());
				pack.setNom("");
				FormPack formPack = new FormPack();
				formPack.setIdPack(pack.getId());
				formPack.setNombreUnite(pack.getNombreUnite());
				formPack.setSku(pointDepense.getReference());
				formPack.setIdProduit(String.valueOf(pointDepense.getId()));
				formPack.setPrixVente(pointDepense.getCoutMoyen());
				formPack.setType("point de depense");
				formPack.setDesignation(" " + Methode.upperCaseFirst(pointDepense.getNom().toLowerCase()));
				listFormPacks.add(formPack);
			}
		}

		return listFormPacks;
	}

	public void annulerFicheDepense(FormFilter formFilter, String idFicheDepense) {
		//System.out.println("Tentative d'annulation de la fiche");
		Utilisateur utilisateur = formFilter.getUtilisateur();
		if (utilisateur != null && utilisateur.getPassword().isBlank()) {
			utilisateur.setPassword(".");
		}
		if (utilisateur != null && utilisateur.getProfil() != Profil.getRoot()
				&& utilisateurRepository.findById(utilisateur.getId()).isPresent() && utilisateur.getPassword()
						.equals(utilisateurRepository.findById(utilisateur.getId()).get().getPassword())) {
			utilisateur = utilisateurRepository.findById(utilisateur.getId()).get();
			formFilter.setUtilisateur(utilisateur);
		} else {
			throw new StreengeException(
					new ErrorAPI("Impossible d'annuler cette fiche de depense...! Utilisateur Non defini..!"));
		}

		FicheDepense ficheDepense = new FicheDepense();
		if (ficheDepenseRepository.findById(idFicheDepense).isPresent()) {
			ficheDepense = ficheDepenseRepository.findById(idFicheDepense).get();
		} else {
			throw new StreengeException(new ErrorAPI("Cette fiche de depense n'existe pas...!"));
		}

		if (formFilter != null && !Statut.getAnnuler().equals(ficheDepense.getStatut())
				&& Methode.userLevel(formFilter.getUtilisateur()) > 1
				&& Methode.stockageAccess(formFilter.getUtilisateur(), ficheDepense.getStockage().getIdStockage())) {

			if (ficheDepense.getCaisse()!=null) {
				HistoriqueCaisse historiqueCaisse = new HistoriqueCaisse();
				Caisse newCaisse = caisseRepository.findById(ficheDepense.getCaisse().getId()).get();
				newCaisse.setSolde(newCaisse.getSolde() + getTotalFicheDepense(ficheDepense));
				historiqueCaisse.setSolde(newCaisse.getSolde());
				historiqueCaisse.setAjustement(getTotalFicheDepense(ficheDepense));
				historiqueCaisse.setCaisse(newCaisse);
				historiqueCaisse.setDate(new Date());
				historiqueCaisse.setIdDocument(ficheDepense.getIdFicheDepense());
				historiqueCaisse
						.setUtilisateur(utilisateur);
				historiqueCaisse
						.setContext("Reajustement du solde de caisse suite à l'annulation de la fiche de Depense ("+ficheDepense.getIdFicheDepense()+")");

				caisseRepository.save(newCaisse);
				historiqueCaisseRepository.save(historiqueCaisse);
			}
			
			ficheDepense.setStatut(Statut.getAnnuler());
			ficheDepenseRepository.save(ficheDepense);
			//System.out.println("Annulation de fiche de depense terminer");

		}else {
			throw new StreengeException(new ErrorAPI("Vous n'êtes pas abiliter à effectuer cette manipulation...!"));
		}
		
	}
	
	public void activationFicheDepense(FormFilter formFilter, String idFicheDepense) {

		Utilisateur utilisateur = formFilter.getUtilisateur();
		if (utilisateur != null && utilisateur.getPassword().isBlank()) {
			utilisateur.setPassword(".");
		}
		if (utilisateur != null && utilisateur.getProfil() != Profil.getRoot()
				&& utilisateurRepository.findById(utilisateur.getId()).isPresent() && utilisateur.getPassword()
						.equals(utilisateurRepository.findById(utilisateur.getId()).get().getPassword())) {
			utilisateur = utilisateurRepository.findById(utilisateur.getId()).get();
			formFilter.setUtilisateur(utilisateur);
		} else {
			throw new StreengeException(
					new ErrorAPI("Impossible de reactiver cette fiche de depense...! Utilisateur Non defini..!"));
		}

		FicheDepense ficheDepense = new FicheDepense();
		if (ficheDepenseRepository.findById(idFicheDepense).isPresent()) {
			ficheDepense = ficheDepenseRepository.findById(idFicheDepense).get();
		} else {
			throw new StreengeException(new ErrorAPI("Cette fiche de depense n'existe pas...!"));
		}

		if (formFilter != null && Statut.getAnnuler().equals(ficheDepense.getStatut())
				&& Methode.userLevel(formFilter.getUtilisateur()) > 1
				&& Methode.stockageAccess(formFilter.getUtilisateur(), ficheDepense.getStockage().getIdStockage())) {

			if (ficheDepense.getCaisse()!=null) {
				HistoriqueCaisse historiqueCaisse = new HistoriqueCaisse();
				Caisse newCaisse = caisseRepository.findById(ficheDepense.getCaisse().getId()).get();
				newCaisse.setSolde(newCaisse.getSolde() - getTotalFicheDepense(ficheDepense));
				historiqueCaisse.setSolde(newCaisse.getSolde());
				historiqueCaisse.setAjustement(getTotalFicheDepense(ficheDepense)*-1);
				historiqueCaisse.setCaisse(newCaisse);
				historiqueCaisse.setDate(new Date());
				historiqueCaisse.setIdDocument(ficheDepense.getIdFicheDepense());
				historiqueCaisse
						.setUtilisateur(utilisateur);
				historiqueCaisse
						.setContext("Réactivation de la fiche de Depense ("+ficheDepense.getIdFicheDepense()+")");

				caisseRepository.save(newCaisse);
				historiqueCaisseRepository.save(historiqueCaisse);
			}
			
			ficheDepense.setStatut(Statut.getAccepter());
			ficheDepenseRepository.save(ficheDepense);
			
		}else {
			throw new StreengeException(new ErrorAPI("Vous n'êtes pas abiliter à effectuer cette manipulation...!"));
		}

	}

}
