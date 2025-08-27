package com.streenge.service.adminService;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.streenge.model.admin.Utilisateur;
import com.streenge.model.admin.money.Caisse;
import com.streenge.model.admin.money.HistoriqueCaisse;
import com.streenge.model.admin.money.MouvementCaisse;
import com.streenge.model.vente.facture.Facture;
import com.streenge.repository.adminRepository.StockageRepository;
import com.streenge.repository.adminRepository.UtilisateurRepository;
import com.streenge.repository.adminRepository.moneyRepository.CaisseRepository;
import com.streenge.repository.adminRepository.moneyRepository.HistoriqueCaisseRepository;
import com.streenge.repository.adminRepository.moneyRepository.MouvementCaisseRepository;
import com.streenge.service.utils.erroclass.ErrorAPI;
import com.streenge.service.utils.erroclass.StreengeException;
import com.streenge.service.utils.formInt.FormDepot;
import com.streenge.service.utils.formOut.FormCaisse;
import com.streenge.service.utils.formOut.FormTable;
import com.streenge.service.utils.formOut.RowTable;
import com.streenge.service.utils.streengeData.ColorStatut;
import com.streenge.service.utils.streengeData.Data;
import com.streenge.service.utils.streengeData.Statut;
import com.streenge.service.utils.streengeData.Type;
import com.streenge.service.utils.streengeFunction.Methode;

@Service
public class CaisseService {

	@Autowired
	private CaisseRepository caisseRepository;

	@Autowired
	private UtilisateurRepository utilisateurRepository;

	@Autowired
	private HistoriqueCaisseRepository historiqueCaisseRepository;

	@Autowired
	private MouvementCaisseRepository mouvementCaisseRepository;

	@Autowired
	private StockageRepository stockageRepository;

	public FormCaisse getFormCaisse(int idCaisse) {
		FormCaisse formCaisse = new FormCaisse();
		Caisse caisse = null;
		if (caisseRepository.findById(idCaisse).isPresent()) {
			caisse = caisseRepository.findById(idCaisse).get();

			formCaisse.setId(caisse.getId());
			formCaisse.setDescription(caisse.getDescription());
			formCaisse.setSolde(caisse.getSolde());
			formCaisse.setStockage(caisse.getStockage());

			formCaisse.setHistoriqueCaisses(historiqueCaisseRepository.findByCaisseIdOrderByIdDesc(caisse.getId()));
			formCaisse.setMouvementCaisse(this.generateListMouvementCaisse(caisse.getId()));

			for (Facture facture : caisse.getStockage().getFactures()) {
				formCaisse.setNombreFacture(formCaisse.getNombreFacture()+1);
				if (Statut.getPaye().equals(facture.getStatut())) {
					formCaisse.setNombreFacturePaye(1 + formCaisse.getNombreFacturePaye());
				} else if (Statut.getNonpaye().equals(facture.getStatut())) {
					formCaisse.setNombreFactureNonPaye(1 + formCaisse.getNombreFactureNonPaye());
				} else if (Statut.getPartiellementpaye().equals(facture.getStatut())) {
					formCaisse.setNombreFacturePartiellementPaye(1 + formCaisse.getNombreFacturePartiellementPaye());
				} else if (Statut.getAnnuler().equals(facture.getStatut())) {
					formCaisse.setNombreFactureAnnule(1 + formCaisse.getNombreFactureAnnule());
				}
			}

		} else {
			throw new StreengeException(new ErrorAPI("La caisse recherchée est indisponible ou inexistante...!"));
		}

		return formCaisse;
	}

	public FormTable genereteListCaisse(int idUtilisateur, boolean isRoot) {

		List<RowTable> rows = new ArrayList<RowTable>();
		DecimalFormat df = new DecimalFormat("###,###,###,###.##");

		Utilisateur utilisateur = null;
		if (utilisateurRepository.findById(idUtilisateur).isPresent()) {
			utilisateur = utilisateurRepository.findById(idUtilisateur).get();
		}

		List<Caisse> caisses = (List<Caisse>) caisseRepository.findAll();

		for (Caisse caisse : caisses) {
			if (isRoot || (utilisateur != null
					&& Methode.stockageAccess(utilisateur, caisse.getStockage().getIdStockage()))) {
				RowTable row = new RowTable();
				List<String> dataList = new ArrayList<String>();
				dataList.add("Caisse: " + caisse.getStockage().getNom());
				dataList.add(df.format(caisse.getSolde()) + " " + Data.devise);
				row.setData(dataList);
				row.setId(String.valueOf(caisse.getId()));
				row.setStatut((caisse.getSolde() < 0) ? Statut.getDebiteur() : Statut.getCreditteur());
				row.setColorStatut(ColorStatut.getColor(row.getStatut()));
				rows.add(row);
			}
		}

		List<String> colunm = new ArrayList<String>();
		colunm.add("NOM#");
		colunm.add("SOLDE");

		FormTable formTable = new FormTable();
		formTable.setColunm(colunm);
		formTable.setRows(rows);

		return formTable;
	}

	public FormTable generateListMouvementCaisse(int idCaisse) {
		List<MouvementCaisse> mouvementCaisses = new ArrayList<>();
		List<RowTable> rows = new ArrayList<RowTable>();
		SimpleDateFormat formater = new SimpleDateFormat("dd MMMM yyyy");
		DecimalFormat df = new DecimalFormat("###,###,###,###.##");

		
		mouvementCaisses = mouvementCaisseRepository.findByCaisseIdOrderByDateDesc(idCaisse);

		for (MouvementCaisse mouvementCaisse : mouvementCaisses) {
			RowTable row = new RowTable();
			List<String> dataList = new ArrayList<String>();
			dataList.add(mouvementCaisse.getIdMouvementCaisse());
			dataList.add((mouvementCaisse.getUtilisateur() != null) ? mouvementCaisse.getUtilisateur().getNom() : "");
			dataList.add((mouvementCaisse.getCaisse() != null) ? mouvementCaisse.getCaisse().getStockage().getNom()
					: "Compte externe");
			dataList.add(formater.format(mouvementCaisse.getDate()));
			dataList.add(mouvementCaisse.getMotif());
			dataList.add(df.format(mouvementCaisse.getMontant()) + "  " + Data.getDevise());

			row.setData(dataList);
			row.setStatut(mouvementCaisse.getType());
			row.setColorStatut(ColorStatut.getColor(mouvementCaisse.getType()));
			row.setId(mouvementCaisse.getIdMouvementCaisse());
			row.setIdUtilisateur(
					(mouvementCaisse.getUtilisateur() != null) ? mouvementCaisse.getUtilisateur().getId() : 0);
			rows.add(row);
		}

		List<String> colunm = new ArrayList<String>();
		colunm.add("IDENTIFIANT#");
		colunm.add("UTILISATEUR");
		colunm.add("CAISSE");
		colunm.add("DATE");
		colunm.add("MOTIF");
		colunm.add("MONTANT");

		FormTable formTable = new FormTable();
		formTable.setColunm(colunm);
		formTable.setRows(rows);

		return formTable;
	}

	public MouvementCaisse createDepotOrRetraitCaisse(FormDepot form, Boolean isDepot) {

		if (form.getMotif().trim().length() < 3)
			throw new StreengeException(new ErrorAPI("Le motif du " + (isDepot ? "depôt" : "retrait")
					+ " ne doit pas êre vide, ou contenir moins de trois(03) Caractères"));
		if (form.getMotif().trim().length() > 150)
			throw new StreengeException(new ErrorAPI(
					"Le motif du " + (isDepot ? "depôt" : "retrait") + " est trop long maximun 150 carracteres"));
		if (form.getDescription().trim().length() > 500)
			throw new StreengeException(new ErrorAPI(
					"La description du " + (isDepot ? "depôt" : "retrait") + " est trop long maximun 500 carracteres"));
		if (form.getMontant() <= 0)
			throw new StreengeException(new ErrorAPI(
					"Le Montant du " + (isDepot ? "depôt" : "retrait") + " ne peut pas être egale à zéro (0)"));

		HistoriqueCaisse historiqueCaisse = new HistoriqueCaisse();
		

		MouvementCaisse mouvementCaisse = new MouvementCaisse();
		mouvementCaisse
				.setIdMouvementCaisse(Methode.createID(mouvementCaisseRepository, (isDepot ? "DEPC" : "RETC")));
		mouvementCaisse.setDate(new Date());
		mouvementCaisse.setMotif(Methode.upperCaseFirst(form.getMotif().trim()));
		mouvementCaisse.setDescription(
				!(form.getDescription().trim().isBlank()) ? Methode.upperCaseFirst(form.getDescription().trim()) : "");

		
		mouvementCaisse.setUtilisateur(form.getUtilisateur());
		mouvementCaisse.setMontant(form.getMontant());
		mouvementCaisse.setType((isDepot ? Type.DEPOT : Type.RETRAIT));

		if (stockageRepository.findById(form.getIdStockage()).isPresent()) {
			if (stockageRepository.findById(form.getIdStockage()).get().getCaisses().isEmpty()) {
				Caisse caisse01 = new Caisse();
				caisse01.setSolde(0);
				caisse01.setStockage(stockageRepository.findById(form.getIdStockage()).get());
				caisseRepository.save(caisse01);
			}
			Caisse caisse = stockageRepository.findById(form.getIdStockage()).get().getCaisses().get(0);
			mouvementCaisse.setCaisse(caisse);
			
			if (!isDepot && mouvementCaisse.getCaisse().getSolde() < form.getMontant()) {
				throw new StreengeException(
						new ErrorAPI("Ce retrait n'est possible car la somme debité est suprerieur au solde de la caisse"));
			}

			historiqueCaisse.setDate(new Date());
			historiqueCaisse.setIdDocument(mouvementCaisse.getIdDocument());
			historiqueCaisse.setCaisse(caisse);
			historiqueCaisse.setUtilisateur(mouvementCaisse.getUtilisateur());
			historiqueCaisse.setContext((isDepot ? "Depot" : "Retrait") + " dans la caisse de l'agence: "
					+ mouvementCaisse.getCaisse().getStockage().getNom());
			historiqueCaisse.setAjustement((isDepot ? 1 : -1) * mouvementCaisse.getMontant());
			historiqueCaisse.setSolde(caisse.getSolde() + historiqueCaisse.getAjustement());
			caisse.setSolde(historiqueCaisse.getSolde());
			caisseRepository.save(caisse);
			historiqueCaisseRepository.save(historiqueCaisse);
		}

		return mouvementCaisseRepository.save(mouvementCaisse);
	}
	
	public FormDepot getFormDepotCaisse(String idDepot) {
		MouvementCaisse mouvementCaisse = mouvementCaisseRepository.findById(idDepot).get();
		FormDepot formDepot = new FormDepot();

		formDepot.setIdDepot(mouvementCaisse.getIdMouvementCaisse());
		formDepot.setType(mouvementCaisse.getType());
		formDepot.setDate(mouvementCaisse.getDate());
		formDepot.setDescription(mouvementCaisse.getDescription());
		
		formDepot.setIdCaisse((mouvementCaisse.getCaisse() != null)
				? mouvementCaisse.getCaisse().getId()
				: 0);
		formDepot.setIdStockage((mouvementCaisse.getCaisse() != null)
				? mouvementCaisse.getCaisse().getStockage().getIdStockage()
				: 0);
		formDepot.setMontant(mouvementCaisse.getMontant());
		formDepot.setMotif(mouvementCaisse.getMotif()); 
		formDepot.setStockage(
				(mouvementCaisse.getCaisse() != null) ? mouvementCaisse.getCaisse().getStockage() : null);
		formDepot.setUtilisateur(mouvementCaisse.getUtilisateur());

		return formDepot;
	}
	
}
