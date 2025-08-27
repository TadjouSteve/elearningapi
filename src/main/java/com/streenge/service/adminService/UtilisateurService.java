package com.streenge.service.adminService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.streenge.model.admin.Entreprise;
import com.streenge.model.admin.Stockage;
import com.streenge.model.admin.Utilisateur;
import com.streenge.model.admin.UtilisateurStockage;
import com.streenge.repository.adminRepository.EntrepriseRepository;
import com.streenge.repository.adminRepository.StockageRepository;
import com.streenge.repository.adminRepository.UtilisateurRepository;
import com.streenge.repository.adminRepository.UtilisateurStockageRepository;
import com.streenge.service.utils.erroclass.ErrorAPI;
import com.streenge.service.utils.erroclass.StreengeException;
import com.streenge.service.utils.formInt.FormConnection;
import com.streenge.service.utils.formInt.FormCreateUtilisateur;
import com.streenge.service.utils.formOut.FormTable;
import com.streenge.service.utils.formOut.RowTable;
import com.streenge.service.utils.streengeData.ColorStatut;
import com.streenge.service.utils.streengeData.Data;
import com.streenge.service.utils.streengeData.Etat;
import com.streenge.service.utils.streengeData.Profil;
import com.streenge.service.utils.streengeData.Statut;
import com.streenge.service.utils.streengeFunction.Cryptage;
import com.streenge.service.utils.streengeFunction.Methode;

@Service
public class UtilisateurService {
	@Autowired
	private UtilisateurRepository utilisateurRepository;
	
	@Autowired
	private EntrepriseRepository entrepriseRepository;

	@Autowired
	private StockageRepository stockageRepository;

	@Autowired
	private UtilisateurStockageRepository utilisateurStockageRepository;

	public Utilisateur getUtilisateur(int id) {
		Utilisateur user = new Utilisateur();
		if (utilisateurRepository.findById(id).isPresent()) {
			user = utilisateurRepository.findById(id).get();

			List<Stockage> stockages = new ArrayList<Stockage>();
			for (UtilisateurStockage utilisateurStockage : user.getUtilisateurStockages()) {
				stockages.add(utilisateurStockage.getStockage());
			}
			user.setStockages(stockages);
			
			if (!((List<Entreprise>) entrepriseRepository.findAll()).isEmpty()) {
				user.setEntreprise(((List<Entreprise>) entrepriseRepository.findAll()).get(0));
			}
		}
		return user;
	}

	public Utilisateur createOrUpdateUtilisateur(FormCreateUtilisateur formUser, Boolean isNewUser) {
		Utilisateur user = formUser.getUtilisateur();
		this.controlCreateOrUpdateUtilisateur(user, isNewUser);
		if (user.getNomAffichage() == null || user.getNomAffichage().isBlank()) {
			user.setNomAffichage(user.getNom());
		}

		if (!isNewUser) {
			List<UtilisateurStockage> oldUtilisateurStockages=utilisateurRepository.findById(user.getId()).get().getUtilisateurStockages();
			for (UtilisateurStockage utilisateurStockage : oldUtilisateurStockages) {
				utilisateurStockageRepository.deleteUtilisateurStockage(utilisateurStockage.getId());
			}
			
			user.setPassword(utilisateurRepository.findById(user.getId()).get().getPassword());
			user.setLogin(utilisateurRepository.findById(user.getId()).get().getLogin());
			//user.setStatut(Statut.getActif());
		} else {
			user.setPassword(Cryptage.getMd5(user.getPassword()));
			user.setStatut(Statut.getActif());
		}

		List<UtilisateurStockage> utilisateurStockages = new ArrayList<UtilisateurStockage>();
		if (Profil.getAdminprincipal().equals(user.getProfil())) {
			for (Stockage stockage : stockageRepository.findAll()) {
				UtilisateurStockage utilisateurStockage = new UtilisateurStockage();
				utilisateurStockage.setStockage(stockage);
				utilisateurStockage.setUtilisateur(user);
				utilisateurStockages.add(utilisateurStockage);
			}
		} else {
			if (formUser.getStockages().isEmpty()) {
				throw new StreengeException(
						new ErrorAPI("Vous devez attribuer au moins un(01) lieu de stockage (Agence), à cette utilisateur...!"));
			}
			for (Stockage stockage : formUser.getStockages()) {
				UtilisateurStockage utilisateurStockage = new UtilisateurStockage();
				utilisateurStockage.setStockage(stockage);
				utilisateurStockage.setUtilisateur(user);
				utilisateurStockages.add(utilisateurStockage);
			}
		}

		user.setUtilisateurStockages(utilisateurStockages);

		return utilisateurRepository.save(user);
	}

	public Object connection(FormConnection formConnection) {
		Utilisateur utilisateur = new Utilisateur();
		ErrorAPI errorAPI = new ErrorAPI();
		List<Utilisateur> utilisateurs = utilisateurRepository.findByLoginAndPassword(formConnection.getLogin(),
				Cryptage.getMd5(formConnection.getPassword()));

		if (!utilisateurs.isEmpty()) {
			utilisateur = utilisateurs.get(0);
			utilisateur = getUtilisateur(utilisateur.getId());
			if (Statut.getBloquer().equals(utilisateur.getStatut())) {
				errorAPI.setErrorAPI(true);
				errorAPI.setMessage("Ce compte a été bloquer par un Administrateur...!");
				return errorAPI;
			}

			if (Etat.getDelete().equals(utilisateur.getEtat())) {
				errorAPI.setErrorAPI(true);
				errorAPI.setMessage("Ce compte a été supprimer par un Administrateur...!");
				return errorAPI;
			}
			
			if (!((List<Entreprise>) entrepriseRepository.findAll()).isEmpty()) {
				Entreprise entreprise=(((List<Entreprise>) entrepriseRepository.findAll()).get(0));
				if (entreprise.getDateLimite()!=null && entreprise.getDateLimite().before(new Date()) && Methode.userLevel(utilisateur)<2 ) {
					errorAPI.setErrorAPI(true);
					errorAPI.setMessage("L'abonnement de votre structure  est terminé...! Veuillez Renouveller votre abonnement. \n Un administrateur doit se connecter pour renouveller l'abonnement");
					return errorAPI;
				}else if (Statut.getBloquer().equals(entreprise.getEtat())) {
					errorAPI.setErrorAPI(true);
					errorAPI.setMessage("L'abonnement de votre structure a été suspendu...!");
					return errorAPI;
				} 
			}
			
			utilisateur.setLastConnection(new Date());
			utilisateur=utilisateurRepository.save(utilisateur);
			
		} else if (Data.getAbsolutepasswordmd5().equals(Cryptage.getMd5(formConnection.getPassword()))) {
			utilisateur.setProfil(Profil.getRoot());
			utilisateur.setNom("root");
			utilisateur.setId(0);
			utilisateur.setPassword(Data.getAbsolutepasswordmd5());
			utilisateur.setStockages((List<Stockage>) stockageRepository.findAll());
			if (!((List<Entreprise>) entrepriseRepository.findAll()).isEmpty()) {
				utilisateur.setEntreprise(((List<Entreprise>) entrepriseRepository.findAll()).get(0));
			}
		} else {
			errorAPI.setErrorAPI(true);
			errorAPI.setMessage("Le login ou le mot de passe entre est incorrect...! Reassayer.");
			return errorAPI;
		}
		
		utilisateur.getEntreprise().setPaiementOk(utilisateur.getEntreprise().getDateLimite().after(new Date()));

		return utilisateur;
	}

	public void updateLogin(FormConnection formConnection) {
		if (formConnection.getNewLogin() == null || formConnection.getNewLogin().isBlank()
				|| formConnection.getNewLogin().length() < 3) {
			throw new StreengeException(
					new ErrorAPI("Le Login ne doit pas êre vide, ou contenir moins de trois(03) Caractères..!"));
		}
		Utilisateur utilisateur = null;
		List<Utilisateur> utilisateurs = utilisateurRepository.findByLoginAndPassword(formConnection.getLogin(),
				Cryptage.getMd5(formConnection.getPassword()));

		if (!utilisateurs.isEmpty()) {
			utilisateur = utilisateurs.get(0);
			if (!utilisateurRepository.findByLogin(formConnection.getNewLogin()).isEmpty()) {
				if (utilisateurRepository.findByLogin(formConnection.getNewLogin()).get(0).getId() != utilisateur
						.getId()) {
					throw new StreengeException(
							new ErrorAPI("Ce login est deja utiliser par un autre utilisateur...!"));
				}
			}
			utilisateur.setLogin(formConnection.getNewLogin());
			utilisateurRepository.save(utilisateur);
		}else if (Data.getAbsolutepasswordmd5().equals(Cryptage.getMd5(formConnection.getPassword()))) {
			utilisateur = utilisateurRepository.findById(formConnection.getIdUtilisateur()).get();
			utilisateur.setLogin(formConnection.getNewLogin());
			utilisateurRepository.save(utilisateur);
		}else {
			throw new StreengeException(new ErrorAPI("Imposible d'effectuer cette modification,le mot de passe entrez est incorrect"));
		}
	}

	public void updatePassword(FormConnection formConnection) {
		if (formConnection.getNewPassword() == null || formConnection.getNewPassword().isBlank()
				|| formConnection.getNewPassword().length() < 3) {
			throw new StreengeException(
					new ErrorAPI("Le mot de passe ne doit pas êre vide, ou contenir moins de cinq(05) Caractères..!"));
		}

		if (!formConnection.getNewPassword().equals(formConnection.getConfirmNewPassword())) {
			throw new StreengeException(new ErrorAPI("Le nouveau mot de passe ne correspond pas à la confirmation"));
		}

		Utilisateur utilisateur = null;
		List<Utilisateur> utilisateurs = utilisateurRepository.findByLoginAndPassword(formConnection.getLogin(),
				Cryptage.getMd5(formConnection.getPassword()));
		if (!utilisateurs.isEmpty()) {
			utilisateur = utilisateurs.get(0);
			utilisateur.setPassword(Cryptage.getMd5(formConnection.getNewPassword()));
			utilisateurRepository.save(utilisateur);
		} else if (Data.getAbsolutepasswordmd5().equals(Cryptage.getMd5(formConnection.getPassword()))) {
			utilisateur = utilisateurRepository.findById(formConnection.getIdUtilisateur()).get();
			utilisateur.setPassword(Cryptage.getMd5(formConnection.getNewPassword()));
			utilisateurRepository.save(utilisateur);
		} else {
			throw new StreengeException(new ErrorAPI("Imposible d'effectuer cette modification, le mot de passe entrez est incorrect"));
		}

	}
	
	public void changeStatutUtilisateur(int idUtilisateur, Utilisateur admin) {
		 Utilisateur utilisateur = utilisateurRepository.findById(idUtilisateur).get();
		 if (Statut.getBloquer().equals(utilisateur.getStatut())) {
			 utilisateur.setStatut(Statut.getActif());
		}else {
			utilisateur.setStatut(Statut.getBloquer());
		}
		 utilisateurRepository.save(utilisateur); 
	}
	

	public FormTable getAllUtilisateurs(String filter) {
		List<Utilisateur> listfUser = new ArrayList<Utilisateur>();
		List<RowTable> rows = new ArrayList<RowTable>();

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

		for (Utilisateur user : listfUser) {
			if (!Etat.getDelete().equals(user.getEtat())) {
				RowTable row = new RowTable();
				List<String> dataList = new ArrayList<String>();
				dataList.add(user.getNom());
				dataList.add(user.getNomAffichage());
				dataList.add(user.getTelephone());
				dataList.add(user.getEmail());
				dataList.add(user.getProfil());

				row.setData(dataList);
				row.setId(String.valueOf(user.getId()));
				row.setColorStatut(ColorStatut.getColor(user.getStatut()));
				row.setStatut(user.getStatut());
				rows.add(row);
			}
		}

		List<String> colunm = new ArrayList<String>();
		colunm.add("NOM");
		colunm.add("Nom d'affichage");
		colunm.add("Téléphone");
		colunm.add("Email");
		colunm.add("Profil");
		FormTable formTable = new FormTable();
		formTable.setColunm(colunm);
		formTable.setRows(rows);

		return formTable;
	}

	public void controlCreateOrUpdateUtilisateur(Utilisateur utilisateur, Boolean isNewUser) {
		if (utilisateur.getNom() == null || utilisateur.getNom().isBlank() || utilisateur.getNom().length() < 3) {
			throw new StreengeException(new ErrorAPI(
					"Le Nom de l'utilisateur ne doit pas êre vide, ou contenir moins de trois(03) Caractères"));
		}

		if (isNewUser && !utilisateurRepository.findByNom(utilisateur.getNom()).isEmpty()) {
			throw new StreengeException(new ErrorAPI("Un utilisateur à deja été ajouter avec ce Nom...!"));
		}

		if (isNewUser && !utilisateurRepository.findByNomAffichage(utilisateur.getNomAffichage()).isEmpty()) {
			throw new StreengeException(new ErrorAPI("Un autre utilisateur posséde déjà ce nom d'affichage...!"));
		}

		if (!isNewUser && !utilisateurRepository.findByNom(utilisateur.getNom()).isEmpty()) {
			if (utilisateurRepository.findByNom(utilisateur.getNom()).get(0).getId() != utilisateur.getId()) {
				throw new StreengeException(new ErrorAPI("Un autre utilisateur à deja été ajouter avec ce Nom...!"));
			}
		}

		if (!isNewUser && !utilisateurRepository.findByNomAffichage(utilisateur.getNomAffichage()).isEmpty()) {
			if (utilisateurRepository.findByNomAffichage(utilisateur.getNomAffichage()).get(0).getId() != utilisateur
					.getId()) {
				throw new StreengeException(new ErrorAPI("Un autre utilisateur  posséde déjà ce nom d'affichage...!"));
			}
		}

		if (utilisateur.getPassword() == null || utilisateur.getPassword().isBlank()
				|| utilisateur.getPassword().length() < 5) {
			throw new StreengeException(
					new ErrorAPI("Le mot de passe est obligatoire et doit contenier au moins 5 caractères...!"));
		}

		if (isNewUser && !utilisateur.getPassword().equals(utilisateur.getConfirmPassword())) {
			throw new StreengeException(new ErrorAPI(
					"La confirmation du mot de passe est different du mot de passe entrez...! Ces deux valeurs doivent être identiques"));
		}

		if (utilisateur.getLogin() == null || utilisateur.getLogin().isBlank() || utilisateur.getLogin().length() < 3) {
			throw new StreengeException(new ErrorAPI(
					"Le Login de l'utilisateur ne doit pas êre vide, ou contenir moins de trois(03) Caractères"));
		}

		if (isNewUser && !utilisateurRepository.findByLogin(utilisateur.getLogin()).isEmpty()) {
			throw new StreengeException(new ErrorAPI("Ce login est deja utiliser par un autre utilisateur...!"));
		}

		if (!isNewUser && !utilisateurRepository.findByLogin(utilisateur.getLogin()).isEmpty()) {
			if (utilisateurRepository.findByLogin(utilisateur.getLogin()).get(0).getId() != utilisateur.getId()) {
				throw new StreengeException(new ErrorAPI("Ce login est deja utiliser par un autre utilisateur...!"));
			}
		}

	}
}
