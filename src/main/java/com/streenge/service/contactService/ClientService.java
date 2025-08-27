package com.streenge.service.contactService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.streenge.model.admin.Entreprise;
import com.streenge.model.contact.Client;
import com.streenge.model.vente.facture.Facture;
import com.streenge.model.vente.facture.MethodePaiement;
import com.streenge.model.vente.facture.Paiement;
import com.streenge.repository.adminRepository.EntrepriseRepository;
import com.streenge.repository.contactRepo.ClientRepository;
import com.streenge.repository.venteRepo.factureRepo.FactureRepository;
import com.streenge.repository.venteRepo.factureRepo.MethodePaiementRepository;
import com.streenge.repository.venteRepo.factureRepo.PaiementRepository;
import com.streenge.service.utils.erroclass.ErrorAPI;
import com.streenge.service.utils.erroclass.StreengeException;
import com.streenge.service.utils.formInt.FormCreateContact;
import com.streenge.service.utils.formOut.FormClient;
import com.streenge.service.utils.formOut.FormTable;
import com.streenge.service.utils.formOut.RowTable;
import com.streenge.service.utils.streengeData.Data;
import com.streenge.service.utils.streengeData.Etat;
import com.streenge.service.utils.streengeData.Statut;
import com.streenge.service.venteService.FactureService;

@Service
public class ClientService {

	@Autowired
	private FactureService factureService;

	@Autowired
	private ClientRepository clientRepository;

	// @Autowired
	// private HistoriqueSoldeClientRepository historiqueSoldeClientRepository;

	// @Autowired
	// private HistoriqueCaisseRepository historiqueCaisseRepository;

	@Autowired
	private MethodePaiementRepository methodePaiementRepository;

	@Autowired
	private PaiementRepository paiementRepository;

	@Autowired
	private FactureRepository factureRepository;

	@Autowired
	private EntrepriseRepository entrepriseRepository;

	public FormClient getClient(String id) {
		FormClient form = new FormClient();
		Optional<Client> optionalClient = clientRepository.findById(id);
		if (optionalClient.isPresent()) {
			Client client = optionalClient.get();
			form.setDateAjout(client.getDateAjout());
			form.setDescription(client.getDescription());
			form.setEmail(client.getEmail());
			form.setNom(client.getNom());
			form.setId(client.getIdClient());
			form.setTelephone(client.getTelephone());
			
			form.setCni(client.getCni());
			form.setNiu(client.getNiu());
			form.setRccm(client.getRccm());
			form.setBoitePostal(client.getBoitePostal());
			
			String[] listAdresse = client.getAdresse().split("::", -1);
			form.setPays(listAdresse[0]);
			form.setAdresse1(listAdresse[1]);
			form.setAdresse2(listAdresse[2]);
			form.setVille(listAdresse[3]);
			form.setSolde(client.getSolde());
			

			if (!((List<Entreprise>) entrepriseRepository.findAll()).isEmpty()) {
				form.setEntreprise(((List<Entreprise>) entrepriseRepository.findAll()).get(0));
			}

			form.setNombreFacture(client.getFactures().size());

			for (Facture facture : client.getFactures()) {
				if (facture.getEtat().equals(Etat.getActif())) {
					if (Statut.getPaye().equals(facture.getStatut())) {
						form.setNombreFacturePaye(1 + form.getNombreFacturePaye());
					} else if (Statut.getNonpaye().equals(facture.getStatut())) {
						form.setNombreFactureNonPaye(1 + form.getNombreFactureNonPaye());
					} else if (Statut.getPartiellementpaye().equals(facture.getStatut())) {
						form.setNombreFacturePartiellementPaye(1 + form.getNombreFacturePartiellementPaye());
					} else if (Statut.getAnnuler().equals(facture.getStatut())) {
						form.setNombreFactureAnnule(1 + form.getNombreFactureAnnule());
					}
				}
			}

			return form;
		} else {
			throw new StreengeException(new ErrorAPI("Le client rechercher n'existe pas...!"));
		}
	}

	public FormTable getAllClient(String filter) {
		return this.generateListClients(filter);
	}

	public Client createClient(FormCreateContact form) {
		controlClient(form, false);
		Client client = new Client();

		client.setIdClient(createID((int) clientRepository.count()));

		client.setNom(form.getNom().trim());
		client.setDateAjout((new Date()));
		client.setDescription(form.getDescription().trim());
		client.setEmail(form.getEmail().trim());
		client.setTelephone(form.getTelephone().trim());
		
		client.setCni(form.getCni().trim().toUpperCase());
		client.setNiu(form.getNiu().trim().toUpperCase());
		client.setRccm(form.getRccm().trim().toUpperCase());
		client.setBoitePostal(form.getBoitePostal().trim());
		
		client.setEtat(Etat.getActif());
		String adresse = form.getPays().trim() + "::" + form.getAdresse1().trim() + "::" + form.getAdresse2().trim()
				+ "::" + form.getVille().trim() + "::";
		client.setAdresse(adresse);
		return clientRepository.save(client);
	}

	private String createID(int number) {
		String id = "Cl";
		if (number < 9) {
			id += "000" + (number + 1);
		} else if (number < 99) {
			id += "00" + (number + 1);
		} else if (number < 999) {
			id += "0" + (number + 1);
		} else if (number < 9999) {
			id += "" + (number + 1);
		} else if (number < 99999) {
			id += "" + (number + 1);
		}

		Optional<Client> clientTest = clientRepository.findById(id);

		if (clientTest.isPresent()) {
			int nbr = number + 1;
			String id2 = "";
			do {
				nbr++;
				if (nbr < 10) {
					id2 = "CL000" + nbr;
				} else if (nbr < 100) {
					id2 = "CL00" + nbr;
				} else if (nbr < 1000) {
					id2 = "CL0" + nbr;
				} else if (nbr < 100000) {
					id2 = "CL" + nbr;
				} else {
					ErrorAPI errorAPI = new ErrorAPI();
					errorAPI.setMessage(
							"Problème Majeur...! l'application est Saturer, vous avez atteint la limite Autorisée...! Contactez L'informaticien...!");
					throw new StreengeException(errorAPI);
				}

				id = id2;
				clientTest = clientRepository.findById(id);
			} while (clientTest.isPresent());
		}

		return id;
	}

	public FormTable generateListClients(String filter) {
		List<Client> listClient = new ArrayList<Client>();
		List<RowTable> rows = new ArrayList<RowTable>();

		if (filter != null) {
			filter = filter.trim();
			listClient = clientRepository.findByNomStartingWithOrTelephoneStartingWithOrderByNomAsc(filter, filter);
			if (listClient.size()<1 && filter.length()>1) {
				listClient = clientRepository.findByNomContainingOrTelephoneContainingOrderByNomAsc(filter, filter);
			}
			
			if (listClient.size()<1 && filter.length()>2) {
				listClient = clientRepository.findByNomContainingOrTelephoneContainingOrAdresseContainingOrderByNomAsc(filter, filter,filter);
			}
			
			if (listClient.size()<1 && filter.length()>2) {
				listClient = clientRepository.findByIdClientStartingWithOrderByNomAsc(filter);
			}
		} else {
			listClient = clientRepository.findAllByOrderByNomAsc();
		}

		for (Client client : listClient) {

			if (client.getEtat().equals(Etat.getActif())) {
				RowTable row = new RowTable();
				List<String> dataList = new ArrayList<String>();
				dataList.add(client.getNom());
				dataList.add(client.getTelephone());
				dataList.add(client.getEmail());
				String[] adresse = client.getAdresse().split("::", -1);
				dataList.add(adresse[1] + ", " + adresse[3]);
				dataList.add(client.getIdClient());
				row.setData(dataList);
				row.setId(client.getIdClient());
				row.setStatut((client.getSolde()<0)?"Debiteur":(client.getSolde()>0)?"Créditeur":"...");
				row.setColorStatut((client.getSolde()<0)?"red":(client.getSolde()>0)?"green":"white");
				rows.add(row);
			}
		}

		List<String> colunm = new ArrayList<String>();
		colunm.add("NOM");
		colunm.add("Téléphone");
		colunm.add("Email");
		colunm.add("Adresse");
		colunm.add("ID");

		FormTable formTable = new FormTable();
		formTable.setColunm(colunm);
		formTable.setRows(rows);

		return formTable;
	}

	public void controlClient(FormCreateContact form, Boolean alter) {
		if (form.getNom().trim().length() < 3)
			throw new StreengeException(
					new ErrorAPI("Le Nom du Client ne doit pas êre vide, ou contenir moins de trois(03) Caractères"));
		if (form.getNom().trim().length() > 50)
			throw new StreengeException(
					new ErrorAPI("Le Nom du Client est trop long...! il ne doit pas depasser 50 craractères"));
		if (!clientRepository.findByNom(form.getNom()).isEmpty() && !alter.equals(true))
			throw new StreengeException(new ErrorAPI("Vous Avez deja enregistrer un Client avec ce Nom"));

		if (!clientRepository.findByNom(form.getNom()).isEmpty() && alter.equals(true)) {

			List<Client> list = clientRepository.findByNom(form.getNom());
			Client client = list.get(0);
			if (!client.getIdClient().equals(form.getId().trim()))
				throw new StreengeException(new ErrorAPI("Vous Avez deja enregistrer un client avec ce Nom"));
		}

		if (alter.equals(true) && clientRepository.findById(form.getId().trim()).isEmpty())
			throw new StreengeException(new ErrorAPI("Le Client que vous essayer de modifier n'existe pas"));
		
		if (form.getCni().trim().length() > 100)
			throw new StreengeException(
					new ErrorAPI("Le Numero de CNI est trop long..! Maximun 100 Caractères "));
		
		if (form.getNiu().trim().length() > 100)
			throw new StreengeException(
					new ErrorAPI("Le NIU est trop long..! Maximun 100 Caractères "));
		
		if (form.getRccm().trim().length() > 100)
			throw new StreengeException(
					new ErrorAPI("Le RCCM est trop long..! Maximun 100 Caractères "));
		
		if (form.getBoitePostal().trim().length() > 100)
			throw new StreengeException(
					new ErrorAPI("L'addresse de boite postal est trop longue..! Maximun 100 Caractères "));

	}

	public Client alterClient(FormCreateContact form) {
		controlClient(form, true);
		Client client = clientRepository.findById(form.getId()).get();

		client.setNom(form.getNom().trim());
		// fournisseur.setDateAjout(form.getDateAjout());
		client.setDescription(form.getDescription().trim());
		client.setEmail(form.getEmail().trim());
		client.setTelephone(form.getTelephone().trim());
		client.setCni(form.getCni().trim().toUpperCase());
		client.setNiu(form.getNiu().trim().toUpperCase());
		client.setRccm(form.getRccm().trim().toUpperCase());
		client.setBoitePostal(form.getBoitePostal().trim());
		client.setEtat(Etat.getActif());
		String adresse = form.getPays().trim() + "::" + form.getAdresse1().trim() + "::" + form.getAdresse2().trim()
				+ "::" + form.getVille().trim() + "::";
		client.setAdresse(adresse);
		return clientRepository.save(client);
	}

	// Methode utiliser lorsque le client vient de faire un depot en compte pour
	// solver ces facture impayer ou juste pour acheter un peu plus tard
	// cette methode est egalement utiliser lorsque un paiement de type compte
	// client est suprimer suite a une annulation de facture ou une reactribution de
	// facture, alors dans ce cas,le montant retourner en compte doit aider a regle
	// les autres facture non paye ou partiellement paye

	public void depotCompteClient() {

	}

	public void balancementCompteClient(Client client, float montant) {
		balancementCompteClient(client, montant, "Paiement enregistre à la suite d'un depôt en compte");
	}

	public void balancementCompteClient(Client client, float montant, String context) {
		if (methodePaiementRepository.findByNom(Data.COMPTE_CLIENT).isEmpty()) {
			MethodePaiement methodePaiement02 = new MethodePaiement();
			methodePaiement02.setNom(Data.COMPTE_CLIENT);
			methodePaiementRepository.save(methodePaiement02);
		}

		MethodePaiement methodePaiement = methodePaiementRepository.findByNom(Data.COMPTE_CLIENT).isEmpty() ? null
				: methodePaiementRepository.findByNom(Data.COMPTE_CLIENT).get(0);
		if (client != null && clientRepository.findById(client.getIdClient()).isPresent()) {
			client = clientRepository.findById(client.getIdClient()).get();
			for (int i = 0; i < client.getFactures().size(); i++) {
				Facture facture = client.getFactures().get(i);
				if (!(Statut.getAnnuler().equals(facture.getStatut()) || Statut.getPaye().equals(facture.getStatut()))
						&& montant > 0) {
					Paiement paiement = new Paiement();
					paiement.setDatePaiement(new Date());
					paiement.setMethodePaiement(methodePaiement);
					paiement.setCaisse(null);
					paiement.setFacture(facture);
					paiement.setDescription(context);

					if (montant >= factureService.getPaiementRestant(facture)) {
						paiement.setMontant(factureService.getPaiementRestant(facture));
						montant -= factureService.getPaiementRestant(facture);
						facture.setStatut(Statut.getPaye());
					} else if (montant > 0) {
						paiement.setMontant(montant);
						montant = 0;
						facture.setStatut(Statut.getPartiellementpaye());
					}
					paiementRepository.save(paiement);
					factureRepository.save(facture);

					if (montant <= 0) {
						break;
					}
				}
			}
		}
	}

}
