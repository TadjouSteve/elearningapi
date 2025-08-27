package com.streenge.service.contactService;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.streenge.model.admin.Entreprise;
import com.streenge.model.admin.money.Caisse;
import com.streenge.model.admin.money.HistoriqueCaisse;
import com.streenge.model.admin.money.HistoriqueSoldeClient;
import com.streenge.model.admin.money.MouvementSoldeClient;
import com.streenge.model.contact.Client;
import com.streenge.repository.adminRepository.EntrepriseRepository;
import com.streenge.repository.adminRepository.StockageRepository;
import com.streenge.repository.adminRepository.moneyRepository.CaisseRepository;
import com.streenge.repository.adminRepository.moneyRepository.HistoriqueCaisseRepository;
import com.streenge.repository.adminRepository.moneyRepository.HistoriqueSoldeClientRepository;
import com.streenge.repository.adminRepository.moneyRepository.MouvementSoldeClientRepository;
import com.streenge.repository.contactRepo.ClientRepository;
import com.streenge.service.utils.erroclass.ErrorAPI;
import com.streenge.service.utils.erroclass.StreengeException;
import com.streenge.service.utils.formInt.FormDepot;
import com.streenge.service.utils.formOut.FormTable;
import com.streenge.service.utils.formOut.RowTable;
import com.streenge.service.utils.streengeData.ColorStatut;
import com.streenge.service.utils.streengeData.Data;
import com.streenge.service.utils.streengeData.Type;
import com.streenge.service.utils.streengeFunction.Methode;

@Service
public class MouvementSoldeClientService {

	@Autowired
	private MouvementSoldeClientRepository mouvementSoldeClientRepository;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private ClientService clientService;

	@Autowired
	private HistoriqueSoldeClientRepository historiqueSoldeClientRepository;

	@Autowired
	private HistoriqueCaisseRepository historiqueCaisseRepository;

	@Autowired
	private StockageRepository stockageRepository;

	@Autowired
	private CaisseRepository caisseRepository;

	@Autowired
	private EntrepriseRepository entrepriseRepository;

	public FormDepot getFormDepot(String idDepot) {
		MouvementSoldeClient mouvementSoldeClient = mouvementSoldeClientRepository.findById(idDepot).get();
		FormDepot formDepot = new FormDepot();

		formDepot.setIdDepot(mouvementSoldeClient.getIdMouvementSoldeClient());
		formDepot.setType(mouvementSoldeClient.getType());
		formDepot.setDate(mouvementSoldeClient.getDate());
		formDepot.setContact(clientService.getClient(mouvementSoldeClient.getClient().getIdClient()));
		formDepot.setDescription(mouvementSoldeClient.getDescription());
		// Choix de l'entreprise a afficher sur les documents
		if (!((List<Entreprise>) entrepriseRepository.findAll()).isEmpty()) {
			formDepot.setEntreprise(((List<Entreprise>) entrepriseRepository.findAll()).get(0));
		}
		formDepot.setIdClient(mouvementSoldeClient.getClient().getIdClient());
		formDepot.setIdStockage((mouvementSoldeClient.getCaisse() != null)
				? mouvementSoldeClient.getCaisse().getStockage().getIdStockage()
				: 0);
		formDepot.setMontant(mouvementSoldeClient.getMontant());
		formDepot.setMotif(mouvementSoldeClient.getMotif());
		formDepot.setStockage(
				(mouvementSoldeClient.getCaisse() != null) ? mouvementSoldeClient.getCaisse().getStockage() : null);
		formDepot.setUtilisateur(mouvementSoldeClient.getUtilisateur());

		return formDepot;
	}

	public MouvementSoldeClient createDepotOrRetraitCompteClient(FormDepot form, Boolean isDepot) {

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
		HistoriqueSoldeClient historiqueSoldeClient = new HistoriqueSoldeClient();

		MouvementSoldeClient mouvementSoldeClient = new MouvementSoldeClient();
		mouvementSoldeClient
				.setIdMouvementSoldeClient(Methode.createID(mouvementSoldeClientRepository, (isDepot ? "DEP" : "RET")));

		mouvementSoldeClient.setDate(new Date());
		mouvementSoldeClient.setMotif(Methode.upperCaseFirst(form.getMotif().trim()));
		mouvementSoldeClient.setDescription(
				!(form.getDescription().trim().isBlank()) ? Methode.upperCaseFirst(form.getDescription().trim()) : "");
		mouvementSoldeClient.setClient(clientRepository.findById(form.getIdClient()).get());
		if (!isDepot && mouvementSoldeClient.getClient().getSolde() < form.getMontant()) {
			throw new StreengeException(
					new ErrorAPI("Ce retrait n'est possible car la somme debité est suprerieur au solde du client"));
		}
		mouvementSoldeClient.setUtilisateur(form.getUtilisateur());
		mouvementSoldeClient.setMontant(form.getMontant());
		mouvementSoldeClient.setType((isDepot ? Type.DEPOT : Type.RETRAIT));

		if (stockageRepository.findById(form.getIdStockage()).isPresent()) {
			if (stockageRepository.findById(form.getIdStockage()).get().getCaisses().isEmpty()) {
				Caisse caisse01 = new Caisse();
				caisse01.setSolde(0);
				caisse01.setStockage(stockageRepository.findById(form.getIdStockage()).get());
				caisseRepository.save(caisse01);
			}
			Caisse caisse = stockageRepository.findById(form.getIdStockage()).get().getCaisses().get(0);
			mouvementSoldeClient.setCaisse(caisse);

			historiqueCaisse.setDate(new Date());
			historiqueCaisse.setIdDocument(mouvementSoldeClient.getIdDocument());
			historiqueCaisse.setCaisse(caisse);
			historiqueCaisse.setUtilisateur(mouvementSoldeClient.getUtilisateur());
			historiqueCaisse.setContext((isDepot ? "Depot" : "Retrait") + " en compte du client "
					+ mouvementSoldeClient.getClient().getNom() + "(id: "
					+ mouvementSoldeClient.getClient().getIdClient() + ")");
			historiqueCaisse.setAjustement((isDepot ? 1 : -1) * mouvementSoldeClient.getMontant());
			historiqueCaisse.setSolde(caisse.getSolde() + historiqueCaisse.getAjustement());
			caisse.setSolde(historiqueCaisse.getSolde());
			caisseRepository.save(caisse);
			historiqueCaisseRepository.save(historiqueCaisse);
		}

		Client client = mouvementSoldeClient.getClient();
		historiqueSoldeClient.setUtilisateur(mouvementSoldeClient.getUtilisateur());
		historiqueSoldeClient.setDate(new Date());
		historiqueSoldeClient.setIdDocument(mouvementSoldeClient.getIdMouvementSoldeClient());
		historiqueSoldeClient.setClient(client);
		historiqueSoldeClient.setContext(
				(isDepot ? "Depot" : "Retrait") + " en compte du client " + mouvementSoldeClient.getClient().getNom()
						+ "(id: " + mouvementSoldeClient.getClient().getIdClient() + ")");

		historiqueSoldeClient.setAjustement((isDepot ? 1 : -1) * mouvementSoldeClient.getMontant());
		historiqueSoldeClient.setSolde(client.getSolde() + historiqueSoldeClient.getAjustement());
		client.setSolde(historiqueSoldeClient.getSolde());

		historiqueSoldeClientRepository.save(historiqueSoldeClient);
		clientRepository.save(client);
		if (isDepot) {
			clientService.balancementCompteClient(client, mouvementSoldeClient.getMontant());
		}

		return mouvementSoldeClientRepository.save(mouvementSoldeClient);
	}

	public FormTable generateListMouvementSoldeClient(String idClient) {
		List<MouvementSoldeClient> mouvementSoldeClients = new ArrayList<>();
		List<RowTable> rows = new ArrayList<RowTable>();
		SimpleDateFormat formater = new SimpleDateFormat("dd MMMM yyyy");
		DecimalFormat df = new DecimalFormat("###,###,###,###.##");

		mouvementSoldeClients = mouvementSoldeClientRepository
				.findByClientIdClientOrderByDateDesc(idClient);

		for (MouvementSoldeClient mouvementSoldeClient : mouvementSoldeClients) {
			RowTable row = new RowTable();
			List<String> dataList = new ArrayList<String>();
			dataList.add(mouvementSoldeClient.getIdMouvementSoldeClient());
			dataList.add(
					(mouvementSoldeClient.getUtilisateur() != null) ? mouvementSoldeClient.getUtilisateur().getNom()
							: "");
			dataList.add(
					(mouvementSoldeClient.getCaisse() != null) ? mouvementSoldeClient.getCaisse().getStockage().getNom()
							: "Compte externe");
			dataList.add(formater.format(mouvementSoldeClient.getDate()));
			dataList.add(mouvementSoldeClient.getMotif());
			dataList.add(df.format(mouvementSoldeClient.getMontant()) + "  " + Data.getDevise());

			row.setData(dataList);
			row.setStatut(mouvementSoldeClient.getType());
			row.setColorStatut(ColorStatut.getColor(mouvementSoldeClient.getType()));
			row.setId(mouvementSoldeClient.getIdMouvementSoldeClient());
			row.setIdUtilisateur(
					(mouvementSoldeClient.getUtilisateur() != null) ? mouvementSoldeClient.getUtilisateur().getId()
							: 0);
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

}
