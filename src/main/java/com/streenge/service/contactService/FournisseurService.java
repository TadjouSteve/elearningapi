package com.streenge.service.contactService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.streenge.model.contact.Fournisseur;
import com.streenge.repository.contactRepo.FournisseurRepository;
import com.streenge.service.utils.erroclass.ErrorAPI;
import com.streenge.service.utils.erroclass.StreengeException;
import com.streenge.service.utils.formInt.FormCreateContact;
import com.streenge.service.utils.formOut.FormTable;
import com.streenge.service.utils.formOut.RowTable;
import com.streenge.service.utils.streengeData.Etat;

@Service
public class FournisseurService {
	@Autowired
	private FournisseurRepository fournisseurRepository;

	public FormTable getAllFournisseurs(String filter) {
		return generateListFournissuers(filter);
	}

	public FormCreateContact getFournissuer(String id) {
		FormCreateContact form = new FormCreateContact();
		Optional<Fournisseur> optionalFournisseur = fournisseurRepository.findById(id);
		if (optionalFournisseur.isPresent()) {
			Fournisseur fournisseur = optionalFournisseur.get();
			form.setDateAjout(fournisseur.getDateAjout());
			form.setDescription(fournisseur.getDescription());
			form.setEmail(fournisseur.getEmail());
			form.setNom(fournisseur.getNom());
			form.setId(fournisseur.getIdFournisseur());
			form.setTelephone(fournisseur.getTelephone());
			String[] listAdresse = fournisseur.getAdresse().split("::", -1);
			form.setPays(listAdresse[0]);
			form.setAdresse1(listAdresse[1]);
			form.setAdresse2(listAdresse[2]);
			form.setVille(listAdresse[3]);
			return form;
		} else {
			return null;
		}
	}

	public Fournisseur createFournisseur(FormCreateContact form) {
		controlFormFournisseur(form, false);
		Fournisseur fournisseur = new Fournisseur();

		fournisseur.setIdFournisseur(createID((int) fournisseurRepository.count()));

		fournisseur.setNom(form.getNom().trim());
		fournisseur.setDateAjout(new Date());
		fournisseur.setDescription(form.getDescription().trim());
		fournisseur.setEmail(form.getEmail().trim());
		fournisseur.setTelephone(form.getTelephone().trim());
		fournisseur.setEtat(Etat.getActif());
		String adresse = form.getPays().trim() + "::" + form.getAdresse1().trim() + "::" + form.getAdresse2().trim()
				+ "::" + form.getVille().trim() + "::";
		fournisseur.setAdresse(adresse);
		return fournisseurRepository.save(fournisseur);
	}

	public FormTable generateListFournissuers(String filter) {
		List<Fournisseur> listfFournisseurs;
		List<RowTable> rows = new ArrayList<RowTable>();

		if (filter != null) {
			filter = filter.trim();
			listfFournisseurs = fournisseurRepository.findByNomStartingWithOrderByNomAsc(filter);
		} else {
			// listfFournisseurs=fournisseurRepository.findAllByOrderByNomAsc();
			listfFournisseurs = fournisseurRepository.findAllByOrderByNomAsc();
		}

		for (Fournisseur fournisseur : listfFournisseurs) {

			if (fournisseur.getEtat().equals(Etat.getActif())) {
				RowTable row = new RowTable();
				List<String> dataList = new ArrayList<String>();
				dataList.add(fournisseur.getNom());
				dataList.add(fournisseur.getTelephone());
				dataList.add(fournisseur.getEmail());
				String[] adresse = fournisseur.getAdresse().split("::", -1);
				dataList.add(adresse[1] + ", " + adresse[3]);

				row.setData(dataList);
				row.setId(fournisseur.getIdFournisseur());
				rows.add(row);
			}

		}

		List<String> colunm = new ArrayList<String>();
		colunm.add("NOM");
		colunm.add("Téléphone");
		colunm.add("Email");
		colunm.add("Adresse");

		FormTable formTable = new FormTable();
		formTable.setColunm(colunm);
		formTable.setRows(rows);

		return formTable;
	}

	private String createID(int number) {
		String id = "FN";
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
		} else {
			ErrorAPI errorAPI = new ErrorAPI();
			errorAPI.setMessage(
					"Problème Majeur...! l'application est Saturer, vous avez atteint la limite Autorisée...! Contactez L'informaticien...!");
			throw new StreengeException(errorAPI);
		}

		Optional<Fournisseur> fournisseurTest = this.fournisseurRepository.findById(id);

		if (fournisseurTest.isPresent()) {
			int nbr = number + 1;
			String id2 = "";
			do {
				nbr++;
				if (nbr < 10) {
					id2 = "PR000" + nbr;
				} else if (nbr < 100) {
					id2 = "PR00" + nbr;
				} else if (nbr < 1000) {
					id2 = "PR0" + nbr;
				} else if (nbr < 200000) {
					id2 = "PR" + nbr;
				} else {
					ErrorAPI errorAPI = new ErrorAPI();
					errorAPI.setMessage(
							"Problème Majeur...! l'application est Saturer, vous avez atteint la limite Autorisée...! Contactez L'informaticien...!");
					throw new StreengeException(errorAPI);
				}

				id = id2;
				fournisseurTest = this.fournisseurRepository.findById(id);
			} while (fournisseurTest.isPresent());
		}

		return id;
	}

	public void controlFormFournisseur(FormCreateContact form, Boolean alter) {
		if (form.getNom().trim().length() < 3)
			throw new StreengeException(new ErrorAPI(
					"Le Non du Founisseur ne doit pas êre vide, ou contenir moins de trois(03) Caractères"));
		if (form.getNom().trim().length() > 50)
			throw new StreengeException(
					new ErrorAPI("Le Non du Fournisseur est trop long...! il ne doit pas depasser 50 craractères"));
		if (!fournisseurRepository.findByNom(form.getNom()).isEmpty() && !alter.equals(true))
			throw new StreengeException(new ErrorAPI("Vous Avez deja enregistrer un Fournissuer avec ce Nom"));

		if (!fournisseurRepository.findByNom(form.getNom()).isEmpty() && alter.equals(true)) {

			List<Fournisseur> list = fournisseurRepository.findByNom(form.getNom());
			Fournisseur fournisseur = list.get(0);
			if (!fournisseur.getIdFournisseur().equals(form.getId().trim()))
				throw new StreengeException(new ErrorAPI("Vous Avez deja enregistrer un Fournissuer avec ce Nom"));
		}

		if (alter.equals(true) && fournisseurRepository.findById(form.getId().trim()).isEmpty())
			throw new StreengeException(new ErrorAPI("Le fournisseur que vous essayer de modifier n'existe pas"));

	}

	public Fournisseur alterFournisseur(FormCreateContact form) {
		controlFormFournisseur(form, true);
		Fournisseur fournisseur = fournisseurRepository.findById(form.getId()).get();

		fournisseur.setNom(form.getNom().trim());
		//fournisseur.setDateAjout(form.getDateAjout());
		fournisseur.setDescription(form.getDescription().trim());
		fournisseur.setEmail(form.getEmail().trim());
		fournisseur.setTelephone(form.getTelephone().trim());
		fournisseur.setEtat(Etat.getActif());
		String adresse = form.getPays().trim() + "::" + form.getAdresse1().trim() + "::" + form.getAdresse2().trim()
				+ "::" + form.getVille().trim() + "::";
		fournisseur.setAdresse(adresse);
		return fournisseurRepository.save(fournisseur);
	}

}
