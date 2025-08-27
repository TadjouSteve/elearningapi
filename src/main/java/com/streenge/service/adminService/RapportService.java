package com.streenge.service.adminService;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.streenge.model.admin.Stockage;
import com.streenge.model.admin.Utilisateur;
import com.streenge.model.contact.Client;
import com.streenge.model.depense.FicheDepense;
import com.streenge.model.depense.LigneDepense;
import com.streenge.model.depense.PointDepense;
import com.streenge.model.produit.Pack;
import com.streenge.model.produit.Produit;
import com.streenge.model.vente.facture.Facture;
import com.streenge.model.vente.facture.LigneFacture;
import com.streenge.repository.adminRepository.StockageRepository;
import com.streenge.repository.adminRepository.UtilisateurRepository;
import com.streenge.repository.contactRepo.ClientRepository;
import com.streenge.repository.depense.FicheDepenseRepository;
import com.streenge.repository.depense.PointDepenseRepository;
import com.streenge.repository.produitRepo.ProduitRepository;
import com.streenge.repository.venteRepo.factureRepo.FactureRepository;
import com.streenge.service.depenseService.FicheDepenseService;
import com.streenge.service.utils.formInt.FormFilter;
import com.streenge.service.utils.formOut.FormTable;
import com.streenge.service.utils.formOut.RowTable;
import com.streenge.service.utils.streengeData.ColorStatut;
import com.streenge.service.utils.streengeData.Data;
import com.streenge.service.utils.streengeData.Etat;
import com.streenge.service.utils.streengeData.Periode;
import com.streenge.service.utils.streengeData.Statut;
import com.streenge.service.utils.streengeFunction.Methode;
import com.streenge.service.venteService.FactureService;

@Service
public class RapportService {
	@Autowired
	private ProduitRepository produitRepository;

	@Autowired
	private StockageRepository stockageRepository;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private UtilisateurRepository utilisateurRepository;

	@Autowired
	private FactureRepository factureRepository;

	@Autowired
	private FactureService factureService;

	@Autowired
	private FicheDepenseRepository ficheDepenseRepository;

	@Autowired
	private FicheDepenseService ficheDepenseService;

	@Autowired
	private PointDepenseRepository pointDepenseRepository;

	public FormTable generateRapportDepenseParUtilisateur(String filter, FormFilter formFilter) {
		if (formFilter == null) {
			formFilter = new FormFilter();
		}

		List<RowTable> rows = new ArrayList<RowTable>();
		List<Utilisateur> listfUser = new ArrayList<Utilisateur>();
		DecimalFormat df = new DecimalFormat("###,###,###,###.##");
		List<Date> dates = Methode.getIntevalDateForPeriode(formFilter.getPeriode());
		Date dateDebut = dates.get(0);
		Date dateFin = dates.get(1);

		Stockage stockage = null;

		if (stockageRepository.findById(formFilter.getIdStockage()).isPresent()) {
			stockage = stockageRepository.findById(formFilter.getIdStockage()).get();
		}

		if (filter != null) {
			filter = filter.trim();
			listfUser = utilisateurRepository
					.findByNomStartingWithOrNomAffichageStartingWithOrTelephoneStartingWithOrderByNomAsc(filter, filter,
							filter);
			if (listfUser.size() < 1) {
				listfUser = utilisateurRepository.findByStatutStartingWithOrderByNomAsc(filter);
			}
		} else {
			listfUser = utilisateurRepository.findAllByOrderByNomAsc();
		}

		int nombreFicheDepenseTotal = 0;
		float totalGlobal = 0;
		for (Utilisateur utilisateur : listfUser) {
			if (!Etat.getDelete().equals(utilisateur.getEtat())) {
				float total = 0;
				int nombreFicheDepense = 0;
				for (FicheDepense ficheDepense : utilisateur.getFicheDepenses()) {
					if (!Statut.getAnnuler().equals(ficheDepense.getStatut()) && ficheDepense.getDate().after(dateDebut)
							&& ficheDepense.getDate().before(dateFin) && (stockage == null
									|| ficheDepense.getStockage().getIdStockage() == stockage.getIdStockage())) {
						nombreFicheDepense++;
						nombreFicheDepenseTotal++;
						total += ficheDepenseService.getTotalFicheDepense(ficheDepense);
						totalGlobal += ficheDepenseService.getTotalFicheDepense(ficheDepense);
					}
				}
				List<String> dataList = new ArrayList<String>();
				RowTable row = new RowTable();
				dataList.add(utilisateur.getNom());
				dataList.add(utilisateur.getProfil());
				dataList.add(Periode.getPeriodeName(formFilter.getPeriode()));
				if (stockage != null) {
					dataList.add(stockage.getNom());
				}
				dataList.add(df.format(nombreFicheDepense));
				dataList.add(df.format(total) + " " + Data.devise);

				row.setData(dataList);
				row.setId(String.valueOf(utilisateur.getId()));
				rows.add(row);
			}
		}

		List<String> dataList = new ArrayList<String>();
		RowTable row02 = new RowTable();
		dataList.add("Total");
		dataList.add("");
		dataList.add("");
		if (stockage != null) {
			dataList.add("");
		}
		dataList.add(df.format(nombreFicheDepenseTotal));
		dataList.add(df.format(totalGlobal) + "  " + Data.getDevise());
		row02.setData(dataList);

		rows.add(row02);

		List<String> colunm = new ArrayList<String>();
		colunm.add("UTILISATEUR #");
		colunm.add("PROFIL");
		colunm.add("PERIODE");
		if (stockage != null) {
			colunm.add("STOCKAGE");
		}
		colunm.add("Nbs Fiche Depense");
		colunm.add("TOTAL");

		FormTable formTable = new FormTable();
		formTable.setColunm(colunm);
		formTable.setRows(rows);

		return formTable;
	}

	public FormTable generateRapportDepensePointDepense(String filter, FormFilter formFilter) {
		if (formFilter == null) {
			formFilter = new FormFilter();
		}

		List<RowTable> rows = new ArrayList<RowTable>();
		List<PointDepense> listPointDepenses;
		DecimalFormat df = new DecimalFormat("###,###,###,###.##");
		List<Date> dates = Methode.getIntevalDateForPeriode(formFilter.getPeriode());
		Date dateDebut = dates.get(0);
		Date dateFin = dates.get(1);

		Stockage stockage = null;

		if (stockageRepository.findById(formFilter.getIdStockage()).isPresent()) {
			stockage = stockageRepository.findById(formFilter.getIdStockage()).get();
		}

		if (filter != null) {
			filter = filter.trim();
			listPointDepenses = pointDepenseRepository.findByNomStartingWithOrReferenceStartingWithOrderByNomAsc(filter,
					filter);
			if (listPointDepenses.size() < 1 && filter.length() > 2) {
				listPointDepenses = pointDepenseRepository.findByNomContainingOrReferenceContainingOrderByNomAsc(filter,
						filter);
			}
		} else {
			listPointDepenses = pointDepenseRepository.findAllByOrderByNomAsc();
		}

		float totalSommeDepense = 0;
		for (PointDepense pointDepense : listPointDepenses) {
			if (!Etat.getDelete().equals(pointDepense.getEtat())) {
				float total = 0;
				List<LigneDepense> ligneDepenses = pointDepense.getLigneDepenses();
				for (LigneDepense ligneDepense : ligneDepenses) {
					if (!Statut.getAnnuler().equals(ligneDepense.getFicheDepense().getStatut())
							&& ligneDepense.getFicheDepense().getDate().after(dateDebut)
							&& ligneDepense.getFicheDepense().getDate().before(dateFin)
							&& (stockage == null || ligneDepense.getFicheDepense().getStockage()
									.getIdStockage() == stockage.getIdStockage())) {
						total += ligneDepense.getMontant();
						totalSommeDepense += ligneDepense.getMontant();
					}
				}
				if (total > 0) {
					List<String> dataList = new ArrayList<String>();
					RowTable row = new RowTable();
					dataList.add(pointDepense.getNom());
					dataList.add(pointDepense.getReference());
					dataList.add(Periode.getPeriodeName(formFilter.getPeriode()));
					if (stockage != null) {
						dataList.add(stockage.getNom());
					}
					dataList.add(df.format(total) + " " + Data.devise);

					row.setData(dataList);
					row.setId(String.valueOf(pointDepense.getId()));
					rows.add(row);
				}
			}
		}

		List<String> dataList = new ArrayList<String>();
		RowTable row02 = new RowTable();
		dataList.add("Total");
		dataList.add("");
		dataList.add("");
		if (stockage != null) {
			dataList.add("");
		}
		dataList.add(df.format(totalSommeDepense) + "  " + Data.getDevise());
		row02.setData(dataList);

		rows.add(row02);

		List<String> colunm = new ArrayList<String>();
		colunm.add("POINT DE DEPENSE #");
		colunm.add("RÉFERENCE");
		colunm.add("PERIODE");
		if (stockage != null) {
			colunm.add("STOCKAGE");
		}
		colunm.add("TOTAL");

		FormTable formTable = new FormTable();
		formTable.setColunm(colunm);
		formTable.setRows(rows);

		return formTable;
	}

	public FormTable generateRapportVenteProduit(String filterProduit, FormFilter formFilter) {

		if (formFilter == null) {
			formFilter = new FormFilter();
		}

		List<RowTable> rows = new ArrayList<RowTable>();
		List<Produit> listProduits;
		DecimalFormat df = new DecimalFormat("###,###,###,###.##");
		List<Date> dates = Methode.getIntevalDateForPeriode(formFilter.getPeriode());
		Date dateDebut = dates.get(0);
		Date dateFin = dates.get(1);

		Stockage stockage = null;

		if (stockageRepository.findById(formFilter.getIdStockage()).isPresent()) {
			stockage = stockageRepository.findById(formFilter.getIdStockage()).get();
		}

		if (filterProduit != null) {
			filterProduit = filterProduit.trim();
			listProduits = produitRepository.findByNomStartingWithOrSkuStartingWithOrderByNomAsc(filterProduit,
					filterProduit);
			if (listProduits.size() < 1) {
				listProduits = produitRepository.findByCategorieNomStartingWithOrderByNomAsc(filterProduit);
			}

			if (listProduits.size() < 1 && filterProduit.length() >= 3) {
				listProduits = produitRepository.findByNomContainingOrSkuContainingOrCodeBarreContaining(filterProduit,
						filterProduit, filterProduit);
			}

		} else {
			listProduits = produitRepository.findAllByOrderByNomAsc();
		}

		float quantiteVenduGlobal = 0;
		float totalSommeVenduGlobal = 0;
		float totalBeneficeGlobal = 0;
		for (Produit produit : listProduits) {
			if (!produit.getEtat().equals(Etat.getDelete()) && produit.getType() == null) {
				List<String> dataList = new ArrayList<String>();
				String nomUnite = produit.getNomUnite();
				float diviseur = 1;
				RowTable row = new RowTable();
				dataList.add(produit.getNom());
				dataList.add(produit.getSku());
				dataList.add(Periode.getPeriodeName(formFilter.getPeriode()));

				if (stockage != null) {
					dataList.add(stockage.getNom());
				}
				float quantiteVendu = 0;
				float totalSommeVendu = 0;
				float totalBenefice = 0;
				List<Pack> packs = produit.getPacks();
				for (Pack pack : packs) {

					if ((Statut.getPackprincipal().equals(pack.getStatut())) && (pack.getNombreUnite() != 0)) {
						diviseur = pack.getNombreUnite();
						nomUnite = pack.getNom();
					}
					List<LigneFacture> ligneFactures = pack.getLigneFactures();
					for (LigneFacture line : ligneFactures) {
						Facture facture = line.getFacture();
						if (!Statut.getAnnuler().equals(facture.getStatut())
								&& facture.getDateCreation().after(dateDebut)
								&& facture.getDateCreation().before(dateFin)) {

							if (stockage != null) {
								if (facture.getStockage().getIdStockage() == stockage.getIdStockage()) {
									totalSommeVendu = totalSommeVendu + Methode.getTotalLigneDocument(line);
									totalBenefice = totalBenefice + (Methode.getTotalLigneDocument(line)
											- (line.getCout_UMP() * line.getQuantite()));
									quantiteVendu = quantiteVendu + (line.getQuantite() * pack.getNombreUnite());
								}
							} else {
								totalSommeVendu = totalSommeVendu + Methode.getTotalLigneDocument(line);
								totalBenefice = totalBenefice + (Methode.getTotalLigneDocument(line)
										- (line.getCout_UMP() * line.getQuantite()));
								quantiteVendu = quantiteVendu + (line.getQuantite() * pack.getNombreUnite());
							}
						}
					}
				}

				dataList.add(df.format(quantiteVendu / diviseur) + " " + nomUnite);
				dataList.add(df.format(totalSommeVendu) + "  " + Data.getDevise());
				dataList.add(df.format(totalBenefice) + "  " + Data.getDevise());

				row.setData(dataList);
				row.setId(produit.getIdProduit());
				if (quantiteVendu > 0) {
					rows.add(row);
				}

				quantiteVenduGlobal += (quantiteVendu / diviseur);
				totalSommeVenduGlobal += totalSommeVendu;
				totalBeneficeGlobal += totalBenefice;
			}
		}

		List<String> dataList = new ArrayList<String>();
		RowTable row02 = new RowTable();
		dataList.add("Total");
		dataList.add("");
		dataList.add("");
		if (stockage != null) {
			dataList.add("");
		}
		dataList.add(df.format(quantiteVenduGlobal));
		dataList.add(df.format(totalSommeVenduGlobal) + "  " + Data.getDevise());
		dataList.add(df.format(totalBeneficeGlobal) + "  " + Data.getDevise());
		row02.setData(dataList);

		rows.add(row02);

		List<String> colunm = new ArrayList<String>();
		colunm.add("PRODUIT #");
		colunm.add("SKU");
		colunm.add("PERIODE");
		if (stockage != null) {
			colunm.add("STOCKAGE");
		}
		colunm.add("QUANTITÉ VENDU");
		colunm.add("SOMME TOTAL");
		colunm.add("BÉNÉFICE MOYEN");

		FormTable formTable = new FormTable();
		formTable.setColunm(colunm);
		formTable.setRows(rows);

		return formTable;
	}

	public FormTable generateRapportVenteClients(String filterClient, FormFilter formFilter) {

		if (formFilter == null) {
			formFilter = new FormFilter();
		}

		List<Client> listfClient = new ArrayList<Client>();
		List<RowTable> rows = new ArrayList<RowTable>();

		DecimalFormat df = new DecimalFormat("###,###,###,###.##");
		List<Date> dates = Methode.getIntevalDateForPeriode(formFilter.getPeriode());
		Date dateDebut = dates.get(0);
		Date dateFin = dates.get(1);

		Stockage stockage = null;
		if (stockageRepository.findById(formFilter.getIdStockage()).isPresent()) {
			stockage = stockageRepository.findById(formFilter.getIdStockage()).get();
		}

		if (filterClient != null) {
			filterClient = filterClient.trim();
			listfClient = clientRepository.findByNomStartingWithOrTelephoneStartingWithOrderByNomAsc(filterClient,
					filterClient);
		} else {
			listfClient = clientRepository.findAllByOrderByNomAsc();
		}

		float nombreFactureGlobal = 0;
		// float quantiteAcheteGlobal = 0;
		float totalSommeAcheteGlobal = 0;
		float totalCoutLivraisonGlobal = 0;
		float totalBeneficeGlobal = 0;
		for (Client client : listfClient) {

			if (client.getEtat().equals(Etat.getActif())) {
				RowTable row = new RowTable();
				List<String> dataList = new ArrayList<String>();
				dataList.add(client.getNom());
				dataList.add(client.getTelephone());

				dataList.add(Periode.getPeriodeName(formFilter.getPeriode()));

				if (stockage != null) {
					dataList.add(stockage.getNom());
				}
				float nombreFacture = 0;
				float quantiteAchete = 0;
				float totalSommeAchete = 0;
				float totalCoutLivraison = 0;
				float totalBenefice = 0;

				List<Facture> factures = client.getFactures();
				for (Facture facture : factures) {
					if (!Statut.getAnnuler().equals(facture.getStatut()) && facture.getDateCreation().after(dateDebut)
							&& facture.getDateCreation().before(dateFin)) {

						if (stockage != null) {

							if (facture.getStockage().getIdStockage() == stockage.getIdStockage()) {
								nombreFacture++;
								totalCoutLivraison += facture.getCoutLivraison();
								for (LigneFacture line : facture.getLigneFactures()) {
									totalSommeAchete = totalSommeAchete + Methode.getTotalLigneDocument(line);
									totalBenefice = totalBenefice + (Methode.getTotalLigneDocument(line)
											- (line.getCout_UMP() * line.getQuantite()));
									quantiteAchete = quantiteAchete
											+ (line.getQuantite() * line.getPack().getNombreUnite());
								}
							}
						} else {
							nombreFacture++;
							totalCoutLivraison += facture.getCoutLivraison();
							for (LigneFacture line : facture.getLigneFactures()) {
								totalSommeAchete = totalSommeAchete + Methode.getTotalLigneDocument(line);
								totalBenefice = totalBenefice + (Methode.getTotalLigneDocument(line)
										- (line.getCout_UMP() * line.getQuantite()));
								quantiteAchete = quantiteAchete
										+ (line.getQuantite() * line.getPack().getNombreUnite());
							}

						}
					}
				}

				dataList.add(df.format(nombreFacture));
				dataList.add(df.format(totalSommeAchete) + "  " + Data.getDevise());
				dataList.add(df.format(totalCoutLivraison) + "  " + Data.getDevise());
				dataList.add(df.format(totalBenefice) + "  " + Data.getDevise());

				row.setData(dataList);
				row.setId(client.getIdClient());
				rows.add(row);

				nombreFactureGlobal += nombreFacture;
				// quantiteAcheteGlobal += quantiteAchete;
				totalSommeAcheteGlobal += totalSommeAchete;
				totalCoutLivraisonGlobal += totalCoutLivraison;
				totalBeneficeGlobal += totalBenefice;
			}
		}

		List<String> dataList = new ArrayList<String>();
		RowTable row02 = new RowTable();
		dataList.add("Total");
		dataList.add("");
		dataList.add("");
		if (stockage != null) {
			dataList.add("");
		}
		dataList.add(df.format(nombreFactureGlobal));
		dataList.add(df.format(totalSommeAcheteGlobal) + "  " + Data.getDevise());
		dataList.add(df.format(totalCoutLivraisonGlobal) + "  " + Data.getDevise());
		dataList.add(df.format(totalBeneficeGlobal) + "  " + Data.getDevise());

		row02.setData(dataList);

		rows.add(row02);

		List<String> colunm = new ArrayList<String>();
		colunm.add("NOM CLIENT");
		colunm.add("Téléphone");
		colunm.add("PERIODE");
		if (stockage != null) {
			colunm.add("STOCKAGE");
		}
		colunm.add("Nbr. FACTURES");
		colunm.add("ACHAT TOTAL");
		colunm.add("FRAIS DE LIVRAISON");
		colunm.add("BÉNÉFICE MOYEN");

		FormTable formTable = new FormTable();
		formTable.setColunm(colunm);
		formTable.setRows(rows);

		return formTable;
	}

	public FormTable generateRapportVenteUtilisateur(String filter, FormFilter formFilter) {

		if (formFilter == null) {
			formFilter = new FormFilter();
		}

		List<Utilisateur> listfUser = new ArrayList<Utilisateur>();
		List<RowTable> rows = new ArrayList<RowTable>();

		DecimalFormat df = new DecimalFormat("###,###,###,###.##");
		List<Date> dates = Methode.getIntevalDateForPeriode(formFilter.getPeriode());
		Date dateDebut = dates.get(0);
		Date dateFin = dates.get(1);

		Stockage stockage = null;
		if (stockageRepository.findById(formFilter.getIdStockage()).isPresent()) {
			stockage = stockageRepository.findById(formFilter.getIdStockage()).get();
		}

		if (filter != null) {
			filter = filter.trim();
			listfUser = utilisateurRepository
					.findByNomStartingWithOrNomAffichageStartingWithOrTelephoneStartingWithOrderByNomAsc(filter, filter,
							filter);
			if (listfUser.size() < 1) {
				listfUser = utilisateurRepository.findByStatutStartingWithOrderByNomAsc(filter);
			}
		} else {
			listfUser = utilisateurRepository.findAllByOrderByNomAsc();
		}

		float nombreFactureGlobal = 0;
		// float quantiteAcheteGlobal = 0;
		float totalSommeAcheteGlobal = 0;
		float totalCoutLivraisonGlobal = 0;
		float totalBeneficeGlobal = 0;
		for (Utilisateur user : listfUser) {
			if (!Etat.getDelete().equals(user.getEtat())) {
				RowTable row = new RowTable();
				List<String> dataList = new ArrayList<String>();
				dataList.add(user.getNom());
				dataList.add(user.getTelephone());

				dataList.add(Periode.getPeriodeName(formFilter.getPeriode()));

				if (stockage != null) {
					dataList.add(stockage.getNom());
				}
				float nombreFacture = 0;
				float quantiteAchete = 0;
				float totalSommeAchete = 0;
				float totalCoutLivraison = 0;
				float totalBenefice = 0;

				List<Facture> factures = user.getFactures();
				for (Facture facture : factures) {
					if (!Statut.getAnnuler().equals(facture.getStatut()) && facture.getDateCreation().after(dateDebut)
							&& facture.getDateCreation().before(dateFin)) {

						if (stockage != null) {

							if (facture.getStockage().getIdStockage() == stockage.getIdStockage()) {
								nombreFacture++;
								totalCoutLivraison += facture.getCoutLivraison();
								for (LigneFacture line : facture.getLigneFactures()) {
									totalSommeAchete = totalSommeAchete + Methode.getTotalLigneDocument(line);
									totalBenefice = totalBenefice + (Methode.getTotalLigneDocument(line)
											- (line.getCout_UMP() * line.getQuantite()));
									quantiteAchete = quantiteAchete
											+ (line.getQuantite() * line.getPack().getNombreUnite());

								}
							}
						} else {
							nombreFacture++;
							totalCoutLivraison += facture.getCoutLivraison();
							for (LigneFacture line : facture.getLigneFactures()) {
								totalSommeAchete = totalSommeAchete + Methode.getTotalLigneDocument(line);
								totalBenefice = totalBenefice + (Methode.getTotalLigneDocument(line)
										- (line.getCout_UMP() * line.getQuantite()));
								quantiteAchete = quantiteAchete
										+ (line.getQuantite() * line.getPack().getNombreUnite());
							}

						}
					}
				}

				dataList.add(df.format(nombreFacture));
				dataList.add(df.format(totalSommeAchete) + "  " + Data.getDevise());
				dataList.add(df.format(totalCoutLivraison) + "  " + Data.getDevise());
				dataList.add(df.format(totalBenefice) + "  " + Data.getDevise());

				row.setData(dataList);
				row.setId(String.valueOf(user.getId()));
				rows.add(row);

				nombreFactureGlobal += nombreFacture;
				// quantiteAcheteGlobal += quantiteAchete;
				totalSommeAcheteGlobal += totalSommeAchete;
				totalCoutLivraisonGlobal += totalCoutLivraison;
				totalBeneficeGlobal += totalBenefice;
			}
		}

		List<String> dataList = new ArrayList<String>();
		RowTable row02 = new RowTable();
		dataList.add("Total");
		dataList.add("");
		dataList.add("");
		if (stockage != null) {
			dataList.add("");
		}
		dataList.add(df.format(nombreFactureGlobal));
		dataList.add(df.format(totalSommeAcheteGlobal) + "  " + Data.getDevise());
		dataList.add(df.format(totalCoutLivraisonGlobal) + "  " + Data.getDevise());
		dataList.add(df.format(totalBeneficeGlobal) + "  " + Data.getDevise());

		row02.setData(dataList);

		rows.add(row02);

		List<String> colunm = new ArrayList<String>();
		colunm.add("NOM UTILISATEUR");
		colunm.add("Téléphone");
		colunm.add("PERIODE");
		if (stockage != null) {
			colunm.add("STOCKAGE");
		}
		colunm.add("Nbr. FACTURES");
		colunm.add("VENTE TOTAL");
		colunm.add("FRAIS DE LIVRAISON");
		colunm.add("BÉNÉFICE MOYEN");

		FormTable formTable = new FormTable();
		formTable.setColunm(colunm);
		formTable.setRows(rows);

		return formTable;
	}

	public FormTable generateRapporVentePeriode(String filterdata, FormFilter formFilter) {
		if (formFilter == null) {
			formFilter = new FormFilter();
			formFilter.setPeriode(2);
		}

		List<RowTable> rows = new ArrayList<RowTable>();
		DecimalFormat df = new DecimalFormat("###,###,###,###.##");
		// List<Date> dates = Methode.getIntevalDateForPeriode(formFilter.getPeriode());
		// Date dateDebut = dates.get(0);
		// Date dateFin = dates.get(1);

		Stockage stockage = null;

		if (stockageRepository.findById(formFilter.getIdStockage()).isPresent()) {
			stockage = stockageRepository.findById(formFilter.getIdStockage()).get();
		}

		Date firstdate = new Date();
		Calendar firstCalendar = Calendar.getInstance();
		firstCalendar.setTime(firstdate);

		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		// int day = calendar.get(Calendar.DAY_OF_MONTH);
		// int month = calendar.get(Calendar.MONTH) + 1;
		// int year = calendar.get(Calendar.YEAR);

		List<Facture> Allfactures = (List<Facture>) factureRepository.findAll();
		Facture firstFacture;
		if (Allfactures.size() > 0) {
			firstFacture = Allfactures.get(0);
			firstdate = firstFacture.getDateCreation();
			if (firstdate == null) {
				firstdate = new Date();
			}
		} else {
			return new FormTable();
		}

		// int firstday = firstCalendar.get(Calendar.DAY_OF_MONTH);
		// int firstmonth = firstCalendar.get(Calendar.MONTH) + 1;
		// int firstyear = firstCalendar.get(Calendar.YEAR);

		int maxToBack = 0;

		Boolean isYear = false;
		Boolean isMonth = false;
		Boolean isDay = false;

		SimpleDateFormat formater = new SimpleDateFormat("dd MMMM yyyy");
		if (formFilter.getPeriode() == 1) {
			isYear = true;
			maxToBack = 15;
			formater = new SimpleDateFormat("yyyy");
		} else if (formFilter.getPeriode() == 2 || formFilter.getPeriode() != 3) {
			isMonth = true;
			maxToBack = 36;
			formater = new SimpleDateFormat("MMMM yyyy");
		} else if (formFilter.getPeriode() == 3) {
			isDay = true;
			maxToBack = 800;
			formater = new SimpleDateFormat("dd MMMM yyyy");
		}

		float nombreFactureGlobal = 0;
		// float quantiteAcheteGlobal = 0;
		float totalSommeAcheteGlobal = 0;
		float totalCoutLivraisonGlobal = 0;
		float totalBeneficeGlobal = 0;

		Date dateControl = new Date();
		int i = 0;
		do {
			RowTable row = new RowTable();
			List<String> dataList = new ArrayList<String>();

			if (isDay) {
				dataList.add(formater.format(Methode.getDateDayBefore(i)));
				dateControl = Methode.getDateDayBefore(i);
			} else if (isMonth) {
				dataList.add(Methode.upperCaseFirst(formater.format(Methode.getDateMonthBefore(i))));
				dateControl = Methode.getDateMonthBefore(i);
			} else if (isYear) {
				dataList.add(formater.format(Methode.getDateYearBefore(i)));
				dateControl = Methode.getDateYearBefore(i);
			}

			if (stockage != null) {
				dataList.add(stockage.getNom());
			}

			float nombreFacture = 0;
			float quantiteAchete = 0;
			float totalSommeAchete = 0;
			float totalCoutLivraison = 0;
			float totalBenefice = 0;

			List<Facture> factures = new ArrayList<Facture>();

			if (isDay) {
				factures = factureRepository.findAllByDateCreationBetween(Methode.getDateDayBefore(i),
						Methode.getDateDayBefore(i - 1));
			} else if (isMonth) {
				factures = factureRepository.findAllByDateCreationBetween(Methode.getDateMonthBefore(i),
						Methode.getDateMonthBefore(i - 1));
			} else if (isYear) {
				factures = factureRepository.findAllByDateCreationBetween(Methode.getDateYearBefore(i),
						Methode.getDateYearBefore(i - 1));
			}

			for (Facture facture : factures) {
				if (!Statut.getAnnuler().equals(facture.getStatut())) {
					if (stockage != null) {
						if (facture.getStockage().getIdStockage() == stockage.getIdStockage()) {
							nombreFacture++;
							totalCoutLivraison += facture.getCoutLivraison();
							for (LigneFacture line : facture.getLigneFactures()) {
								totalSommeAchete = totalSommeAchete + Methode.getTotalLigneDocument(line);
								totalBenefice = totalBenefice + (Methode.getTotalLigneDocument(line)
										- (line.getCout_UMP() * line.getQuantite()));
								quantiteAchete = quantiteAchete
										+ (line.getQuantite() * line.getPack().getNombreUnite());
							}
						}
					} else {
						nombreFacture++;
						totalCoutLivraison += facture.getCoutLivraison();
						for (LigneFacture line : facture.getLigneFactures()) {
							totalSommeAchete = totalSommeAchete + Methode.getTotalLigneDocument(line);
							totalBenefice = totalBenefice
									+ (Methode.getTotalLigneDocument(line) - (line.getCout_UMP() * line.getQuantite()));
							quantiteAchete = quantiteAchete + (line.getQuantite() * line.getPack().getNombreUnite());
						}

					}
				}
			}

			dataList.add(df.format(nombreFacture));
			dataList.add(df.format(totalSommeAchete) + "  " + Data.getDevise());
			dataList.add(df.format(totalCoutLivraison) + "  " + Data.getDevise());
			dataList.add(df.format(totalBenefice) + "  " + Data.getDevise());

			row.setData(dataList);
			rows.add(row);

			nombreFactureGlobal += nombreFacture;
			// quantiteAcheteGlobal += quantiteAchete;
			totalSommeAcheteGlobal += totalSommeAchete;
			totalCoutLivraisonGlobal += totalCoutLivraison;
			totalBeneficeGlobal += totalBenefice;

			if (firstdate.after(dateControl) || firstdate.equals(dateControl)) {
				i = maxToBack;
			}

			i++;
		} while (maxToBack > i);

		List<String> dataList = new ArrayList<String>();
		RowTable row02 = new RowTable();
		dataList.add("Total");
		if (stockage != null) {
			dataList.add("");
		}
		dataList.add(df.format(nombreFactureGlobal));
		dataList.add(df.format(totalSommeAcheteGlobal) + "  " + Data.getDevise());
		dataList.add(df.format(totalCoutLivraisonGlobal) + "  " + Data.getDevise());
		dataList.add(df.format(totalBeneficeGlobal) + "  " + Data.getDevise());

		row02.setData(dataList);

		rows.add(row02);

		List<String> colunm = new ArrayList<String>();
		colunm.add("Date");
		if (stockage != null) {
			colunm.add("STOCKAGE");
		}
		colunm.add("Nbr. FACTURES");
		colunm.add("VENTE TOTAL");
		colunm.add("FRAIS DE LIVRAISON");
		colunm.add("BÉNÉFICE MOYEN");

		FormTable formTable = new FormTable();
		formTable.setColunm(colunm);
		formTable.setRows(rows);

		return formTable;
	}

	public FormTable generateRapporDepensePeriode(String filterdata, FormFilter formFilter) {
		if (formFilter == null) {
			formFilter = new FormFilter();
			formFilter.setPeriode(2);
		}

		List<RowTable> rows = new ArrayList<RowTable>();
		DecimalFormat df = new DecimalFormat("###,###,###,###.##");
		// List<Date> dates = Methode.getIntevalDateForPeriode(formFilter.getPeriode());
		// Date dateDebut = dates.get(0);
		// Date dateFin = dates.get(1);

		Stockage stockage = null;

		if (stockageRepository.findById(formFilter.getIdStockage()).isPresent()) {
			stockage = stockageRepository.findById(formFilter.getIdStockage()).get();
		}

		Date firstdate = new Date();
		Calendar firstCalendar = Calendar.getInstance();
		firstCalendar.setTime(firstdate);

		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		// int day = calendar.get(Calendar.DAY_OF_MONTH);
		// int month = calendar.get(Calendar.MONTH) + 1;
		// int year = calendar.get(Calendar.YEAR);
		List<FicheDepense> allficheDepenses = (List<FicheDepense>) ficheDepenseRepository.findAll();
		FicheDepense firstFicheDepense;
		if (allficheDepenses.size() > 0) {
			firstFicheDepense = allficheDepenses.get(0);
			firstdate = firstFicheDepense.getDate();
			if (firstdate == null) {
				firstdate = new Date();
			}
		} else {
			return new FormTable();
		}

		// int firstday = firstCalendar.get(Calendar.DAY_OF_MONTH);
		// int firstmonth = firstCalendar.get(Calendar.MONTH) + 1;
		// int firstyear = firstCalendar.get(Calendar.YEAR);

		int maxToBack = 0;

		Boolean isYear = false;
		Boolean isMonth = false;
		Boolean isDay = false;

		SimpleDateFormat formater = new SimpleDateFormat("dd MMMM yyyy");
		if (formFilter.getPeriode() == 1) {
			isYear = true;
			maxToBack = 15;
			formater = new SimpleDateFormat("yyyy");
		} else if (formFilter.getPeriode() == 2 || formFilter.getPeriode() != 3) {
			isMonth = true;
			maxToBack = 36;
			formater = new SimpleDateFormat("MMMM yyyy");
		} else if (formFilter.getPeriode() == 3) {
			isDay = true;
			maxToBack = 800;
			formater = new SimpleDateFormat("dd MMMM yyyy");
		}

		float nombreFicheDepenseGlobal = 0;
		float totalSommeFicheDepenseGlobal = 0;

		Date dateControl = new Date();
		int i = 0;
		do {
			RowTable row = new RowTable();
			List<String> dataList = new ArrayList<String>();

			if (isDay) {
				dataList.add(formater.format(Methode.getDateDayBefore(i)));
				dateControl = Methode.getDateDayBefore(i);
			} else if (isMonth) {
				dataList.add(Methode.upperCaseFirst(formater.format(Methode.getDateMonthBefore(i))));
				dateControl = Methode.getDateMonthBefore(i);
			} else if (isYear) {
				dataList.add(formater.format(Methode.getDateYearBefore(i)));
				dateControl = Methode.getDateYearBefore(i);
			}

			if (stockage != null) {
				dataList.add(stockage.getNom());
			}

			float nombreFicheDepense = 0;
			float totalSommeDepense = 0;

			List<FicheDepense> ficheDepenses = new ArrayList<FicheDepense>();

			if (isDay) {
				ficheDepenses = ficheDepenseRepository.findAllByDateBetween(Methode.getDateDayBefore(i),
						Methode.getDateDayBefore(i - 1));
			} else if (isMonth) {
				ficheDepenses = ficheDepenseRepository.findAllByDateBetween(Methode.getDateMonthBefore(i),
						Methode.getDateMonthBefore(i - 1));
			} else if (isYear) {
				ficheDepenses = ficheDepenseRepository.findAllByDateBetween(Methode.getDateYearBefore(i),
						Methode.getDateYearBefore(i - 1));
			}

			for (FicheDepense ficheDepense : ficheDepenses) {
				if (!Statut.getAnnuler().equals(ficheDepense.getStatut())) {
					if (stockage != null) {
						if (ficheDepense.getStockage().getIdStockage() == stockage.getIdStockage()) {
							nombreFicheDepense++;
							totalSommeDepense += ficheDepenseService.getTotalFicheDepense(ficheDepense);
						}
					} else {
						nombreFicheDepense++;
						totalSommeDepense += ficheDepenseService.getTotalFicheDepense(ficheDepense);
					}
				}
			}

			dataList.add(df.format(nombreFicheDepense));
			dataList.add(df.format(totalSommeDepense) + "  " + Data.getDevise());

			row.setData(dataList);
			rows.add(row);

			nombreFicheDepenseGlobal += nombreFicheDepense;
			totalSommeFicheDepenseGlobal += totalSommeDepense;

			if (firstdate.after(dateControl) || firstdate.equals(dateControl)) {
				i = maxToBack;
			}

			i++;
		} while (maxToBack > i);

		List<String> dataList = new ArrayList<String>();
		RowTable row02 = new RowTable();
		dataList.add("Total");
		if (stockage != null) {
			dataList.add("");
		}
		dataList.add(df.format(nombreFicheDepenseGlobal));
		dataList.add(df.format(totalSommeFicheDepenseGlobal) + "  " + Data.getDevise());

		row02.setData(dataList);

		rows.add(row02);

		List<String> colunm = new ArrayList<String>();
		colunm.add("Date");
		if (stockage != null) {
			colunm.add("STOCKAGE");
		}
		colunm.add("Nbr. FICHE DEPENSE");
		colunm.add("DEPENSE TOTAL");

		FormTable formTable = new FormTable();
		formTable.setColunm(colunm);
		formTable.setRows(rows);

		return formTable;
	}

	public FormTable generateRapportVenteService(String filterService, FormFilter formFilter) {

		if (formFilter == null) {
			formFilter = new FormFilter();
		}

		List<RowTable> rows = new ArrayList<RowTable>();
		List<Produit> listProduits;
		DecimalFormat df = new DecimalFormat("###,###,###,###.##");
		List<Date> dates = Methode.getIntevalDateForPeriode(formFilter.getPeriode());
		Date dateDebut = dates.get(0);
		Date dateFin = dates.get(1);

		Stockage stockage = null;

		if (stockageRepository.findById(formFilter.getIdStockage()).isPresent()) {
			stockage = stockageRepository.findById(formFilter.getIdStockage()).get();
		}

		if (filterService != null) {
			filterService = filterService.trim();
			listProduits = produitRepository.findByNomStartingWithOrSkuStartingWithOrderByNomAsc(filterService,
					filterService);
			if (listProduits.size() < 1) {
				listProduits = produitRepository.findByCategorieNomStartingWithOrderByNomAsc(filterService);
			}

			if (listProduits.size() < 1 && filterService.length() >= 3) {
				listProduits = produitRepository.findByNomContainingOrSkuContainingOrCodeBarreContaining(filterService,
						filterService, filterService);
			}

		} else {
			listProduits = produitRepository.findAllByOrderByNomAsc();
		}

		float quantiteVenduGlobal = 0;
		float totalSommeVenduGlobal = 0;
		float totalBeneficeGlobal = 0;
		for (Produit produit : listProduits) {
			if (!produit.getEtat().equals(Etat.getDelete()) && produit.getType() != null) {
				List<String> dataList = new ArrayList<String>();
				String nomUnite = produit.getNomUnite();
				float diviseur = 1;
				RowTable row = new RowTable();
				dataList.add(produit.getNom());
				dataList.add(produit.getSku());
				dataList.add(Periode.getPeriodeName(formFilter.getPeriode()));

				if (stockage != null) {
					dataList.add(stockage.getNom());
				}
				float quantiteVendu = 0;
				float totalSommeVendu = 0;
				float totalBenefice = 0;
				List<Pack> packs = produit.getPacks();
				for (Pack pack : packs) {

					if ((Statut.getPackprincipal().equals(pack.getStatut())) && (pack.getNombreUnite() != 0)) {
						diviseur = pack.getNombreUnite();
						nomUnite = pack.getNom();
					}
					List<LigneFacture> ligneFactures = pack.getLigneFactures();
					for (LigneFacture line : ligneFactures) {
						Facture facture = line.getFacture();
						if (!Statut.getAnnuler().equals(facture.getStatut())
								&& facture.getDateCreation().after(dateDebut)
								&& facture.getDateCreation().before(dateFin)) {

							if (stockage != null) {
								if (facture.getStockage().getIdStockage() == stockage.getIdStockage()) {
									totalSommeVendu = totalSommeVendu + Methode.getTotalLigneDocument(line);
									totalBenefice = totalBenefice + (Methode.getTotalLigneDocument(line)
											- (line.getCout_UMP() * line.getQuantite()));
									quantiteVendu = quantiteVendu + (line.getQuantite() * pack.getNombreUnite());
								}
							} else {
								totalSommeVendu = totalSommeVendu + Methode.getTotalLigneDocument(line);
								totalBenefice = totalBenefice + (Methode.getTotalLigneDocument(line)
										- (line.getCout_UMP() * line.getQuantite()));
								quantiteVendu = quantiteVendu + (line.getQuantite() * pack.getNombreUnite());
							}
						}
					}
				}

				dataList.add(df.format(quantiteVendu / diviseur) + " " + nomUnite);
				dataList.add(df.format(totalSommeVendu) + "  " + Data.getDevise());
				dataList.add(df.format(totalBenefice) + "  " + Data.getDevise());

				row.setData(dataList);
				row.setId(produit.getIdProduit());
				rows.add(row);

				quantiteVenduGlobal += (quantiteVendu / diviseur);
				totalSommeVenduGlobal += totalSommeVendu;
				totalBeneficeGlobal += totalBenefice;
			}
		}

		List<String> dataList = new ArrayList<String>();
		RowTable row02 = new RowTable();
		dataList.add("Total");
		dataList.add("");
		dataList.add("");
		if (stockage != null) {
			dataList.add("");
		}
		dataList.add(df.format(quantiteVenduGlobal));
		dataList.add(df.format(totalSommeVenduGlobal) + "  " + Data.getDevise());
		dataList.add(df.format(totalBeneficeGlobal) + "  " + Data.getDevise());
		row02.setData(dataList);

		rows.add(row02);

		List<String> colunm = new ArrayList<String>();
		colunm.add("SERVICE #");
		colunm.add("SKU");
		colunm.add("PERIODE");
		if (stockage != null) {
			colunm.add("STOCKAGE");
		}
		colunm.add("QUANTITÉ VENDU");
		colunm.add("SOMME TOTAL");
		colunm.add("BÉNÉFICE MOYEN");

		FormTable formTable = new FormTable();
		formTable.setColunm(colunm);
		formTable.setRows(rows);

		return formTable;
	}

	public FormTable generateRapportFactureNonPaye(String filter, FormFilter formFilter) {

		if (formFilter == null) {
			formFilter = new FormFilter();
		}

		// List<Utilisateur> listfUser = new ArrayList<Utilisateur>();
		List<RowTable> rows = new ArrayList<RowTable>();

		List<Facture> factures = new ArrayList<Facture>();
		DecimalFormat df = new DecimalFormat("###,###,###,###.##");
		SimpleDateFormat formater = new SimpleDateFormat("dd MMMM yyyy");
		// List<Date> dates = Methode.getIntevalDateForPeriode(formFilter.getPeriode());
		// Date dateDebut = dates.get(0);
		// Date dateFin = dates.get(1);

		boolean allStockage = true;
		Stockage stockage = null;
		if (stockageRepository.findById(formFilter.getIdStockage()).isPresent()) {
			stockage = stockageRepository.findById(formFilter.getIdStockage()).get();
			allStockage = false;
		}

		if (filter != null) {

			factures = factureRepository.findByClientNomStartingWithOrderByIdFactureDesc(filter);
			if (factures.size() < 1) {
				factures = factureRepository
						.findByIdFactureStartingWithOrBonCommandeIdBonCommandeStartingWithOrUtilisateurNomStartingWithOrStatutStartingWithOrderByIdFactureDesc(
								filter, filter, filter, filter);
			}
			if (factures.size() < 1) {
				factures = factureRepository.findByStockageNomStartingWithOrderByIdFactureDesc(filter);
			}
		} else {
			factures = factureRepository.findAllByOrderByIdFactureDesc();
		}

		float total = 0;
		float totalRestant = 0;
		float nombreFacture = 0;
		for (Facture facture : factures) {
			if (Etat.getActif().equals(facture.getEtat()) && !Statut.getPaye().equals(facture.getStatut())
					&& !Statut.getAnnuler().equals(facture.getStatut())
					&& (allStockage || facture.getStockage().getIdStockage() == stockage.getIdStockage())) {
				RowTable row = new RowTable();
				List<String> dataList = new ArrayList<String>();
				dataList.add(facture.getIdFacture());
				dataList.add((facture.getClient() != null) ? facture.getClient().getNom() : null);
				dataList.add(facture.getStockage().getNom());
				dataList.add(formater.format(facture.getDateCreation()));
				dataList.add((facture.getDateEcheance() != null) ? formater.format(facture.getDateEcheance()) : "");
				dataList.add(df.format(factureService.montantTotalFacture(facture)) + "  " + Data.getDevise());
				dataList.add(df.format((factureService.getPaiementRestant(facture))) + "  " + Data.getDevise());
				// Total de facture et total restant a afficher
				total += factureService.montantTotalFacture(facture);
				totalRestant += factureService.getPaiementRestant(facture);
				nombreFacture++;
				// DecimalFormat.getCurrencyInstance().format( montantTotalFacture(facture))
				row.setData(dataList);
				row.setStatut(facture.getStatut());
				row.setColorStatut(ColorStatut.getColor(facture.getStatut()));
				row.setId(facture.getIdFacture());
				row.setIdUtilisateur((facture.getUtilisateur() != null) ? facture.getUtilisateur().getId() : 0);
				rows.add(row);
			}
		}

		List<String> dataList = new ArrayList<String>();
		RowTable row02 = new RowTable();
		dataList.add("Total");
		dataList.add(df.format(nombreFacture) + " Facts");
		dataList.add("");
		dataList.add("");
		dataList.add("");
		dataList.add(df.format(total) + "  " + Data.getDevise());
		dataList.add(df.format(totalRestant) + "  " + Data.getDevise());
		row02.setData(dataList);
		row02.setStatut("****");
		row02.setColorStatut("white");
		rows.add(row02);

		List<String> colunm = new ArrayList<String>();
		colunm.add("FACTURE #");
		colunm.add("CLIENT");
		colunm.add("LIEU DE STOCKAGE");
		colunm.add("DATE DE CRÉATION");
		colunm.add("ÉCHÉANCE");
		colunm.add("TOTAL");
		colunm.add("RESTANT À PAYER");

		FormTable formTable = new FormTable();
		formTable.setColunm(colunm);
		formTable.setRows(rows);

		return formTable;
	}

}
