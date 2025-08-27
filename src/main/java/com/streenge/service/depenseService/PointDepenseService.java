package com.streenge.service.depenseService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.streenge.model.admin.Utilisateur;
import com.streenge.model.depense.PointDepense;
import com.streenge.repository.adminRepository.UtilisateurRepository;
import com.streenge.repository.depense.PointDepenseRepository;
import com.streenge.service.utils.erroclass.ErrorAPI;
import com.streenge.service.utils.erroclass.StreengeException;
import com.streenge.service.utils.formInt.FormFilter;
import com.streenge.service.utils.formOut.FormTable;
import com.streenge.service.utils.formOut.RowTable;
import com.streenge.service.utils.streengeData.Data;
import com.streenge.service.utils.streengeData.Etat;
import com.streenge.service.utils.streengeData.Profil;
import com.streenge.service.utils.streengeFunction.Methode;

@Service
public class PointDepenseService {

	@Autowired
	private PointDepenseRepository pointDepenseRepository;
	
	@Autowired
	private UtilisateurRepository utilisateurRepository;

	public PointDepense createOrUpdatePointDepense(PointDepense pointDepense, Boolean isNew) {
		if (pointDepense != null) {

			if (pointDepense.getNom() == null || pointDepense.getNom().isBlank()
					|| pointDepense.getNom().trim().length() <= 5) {
				throw new StreengeException(new ErrorAPI(
						"Le Non du Point de depense ne doit pas êre vide, ou contenir moins de cinq(05) Caractères"));
			} else if (pointDepense.getNom().length() > 50) {
				throw new StreengeException(
						new ErrorAPI("Le Non du Point de depense est trop long...! Maximum 50 Caractères"));
			} 
			
			if (pointDepense.getReference() != null && pointDepense.getReference().length() > 15) {
				throw new StreengeException(
						new ErrorAPI("La réference du Point de depense est trop long...! Maximum 15 Caractères"));
			}

			if (!pointDepenseRepository.findByNom((pointDepense.getNom().trim())).isEmpty()) {

				if (isNew || pointDepenseRepository.findByNom((pointDepense.getNom().trim())).get(0)
						.getId() != pointDepense.getId()) {
					throw new StreengeException(new ErrorAPI("Un point de depense à déjà été créer avec ce nom...!"));
				}
			}
			if (pointDepense.getReference()!=null && !pointDepenseRepository.findByReference(pointDepense.getReference().trim()).isEmpty()) {
				if (isNew || pointDepenseRepository.findByReference(pointDepense.getReference().trim()).get(0)
						.getId() != pointDepense.getId()) {
					throw new StreengeException(new ErrorAPI("La reference \" " + pointDepense.getReference()
							+ "\" et déja associer à un autre point de depense...!"));
				}
			}

			pointDepense.setNom(Methode.upperCaseFirst(pointDepense.getNom().trim()));
			if (pointDepense.getReference() != null) {
				pointDepense.setReference(pointDepense.getReference().toUpperCase());
			}

			if (pointDepense.getReference() != null && pointDepense.getReference().isBlank()) {
				pointDepense.setReference(null);
			}else if (pointDepense.getReference() != null) {
				pointDepense.setReference(pointDepense.getReference().trim().toUpperCase());
			}
			
			if (isNew) {
				pointDepense.setDateAjout(new Date());
				pointDepense.setEtat(Etat.getActif());
			}
			

			pointDepense = pointDepenseRepository.save(pointDepense);
		}
		return pointDepense;
	}

	public FormTable getListPointDepeense(String filter, int userLevel) {
		DecimalFormat df = new DecimalFormat("###,###,###,###.##");
		List<RowTable> rows = new ArrayList<RowTable>();

		List<PointDepense> pointDepenses = new ArrayList<>();

		if (filter != null) {
			filter = filter.trim();
			pointDepenses = pointDepenseRepository.findByNomStartingWithOrReferenceStartingWithOrderByNomAsc(filter,
					filter);
			if (pointDepenses.size() < 1 && filter.length() > 2) {
				pointDepenses = pointDepenseRepository.findByNomContainingOrReferenceContainingOrderByNomAsc(filter,
						filter);
			}
		} else {
			pointDepenses = pointDepenseRepository.findAllByOrderByNomAsc();
		}

		for (PointDepense pointDepense : pointDepenses) {
			// Utilisateur userTest=new Utilisateur();
			if (!Etat.getDelete().equals(pointDepense.getEtat()) && userLevel >= pointDepense.getUserLevel()) {
				List<String> dataList = new ArrayList<String>();

				RowTable row = new RowTable();
				dataList.add(pointDepense.getNom());
				dataList.add(pointDepense.getReference());
				
				if (pointDepense.getDescription()!=null &&  pointDepense.getDescription().trim().length()>=55) {
					dataList.add(pointDepense.getDescription().trim().substring(0, 54)+" ...");
				}else {
					dataList.add(pointDepense.getDescription());
				}
				
				dataList.add(df.format(pointDepense.getCoutMoyen()) + " " + Data.devise);

				row.setData(dataList);
				row.setStatut(Methode.getProfil(pointDepense.getUserLevel()));

				String colorStatut = "blue";
				if (pointDepense.getUserLevel() > 1) {
					colorStatut = "green";
				}
				if (pointDepense.getUserLevel() > 2) {
					colorStatut = "maroon";
				}
				row.setColorStatut(colorStatut);
				row.setId(String.valueOf(pointDepense.getId()));
				rows.add(row);
			}
		}

		List<String> colunm = new ArrayList<String>();
		colunm.add("NOM#");
		colunm.add("RÉFERNCE");
		colunm.add("DESCRIPTION");
		colunm.add("COÛT MOYEN");

		FormTable formTable = new FormTable();
		formTable.setColunm(colunm);
		formTable.setRows(rows);

		return formTable;
	}

	public PointDepense getPointDepense(int idPointDepense) {
		PointDepense pointDepense = new PointDepense();
		if (pointDepenseRepository.findById(idPointDepense).isPresent()) {
			pointDepense = pointDepenseRepository.findById(idPointDepense).get();
			pointDepense.setProfilMin(Profil.vendeur);
			if (pointDepense.getUserLevel() > 1) {
				pointDepense.setProfilMin(Profil.adminSecondaire);
			}
			if (pointDepense.getUserLevel() > 2) {
				pointDepense.setProfilMin(Profil.adminPrincipal);
			}
		}
		return pointDepense;
	}

	public void deletePointDepense(FormFilter formFilter,int idPointDepense) {
		
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
					new ErrorAPI("Impossible de supprimer ce point de depense...! Utilisateur Non defini..!"));
		}
		
		PointDepense pointDepense = new PointDepense();
		if (pointDepenseRepository.findById(idPointDepense).isPresent() &&formFilter != null 
				&& Methode.userLevel(formFilter.getUtilisateur()) > 2) {
			pointDepense = pointDepenseRepository.findById(idPointDepense).get();
			pointDepense.setEtat(Etat.getDelete());
			if (pointDepense.getReference() != null) {
				pointDepense.setReference("XX-X-" + pointDepense.getReference() + "-X-XX"+pointDepense.getId()+"X");
			}
			pointDepenseRepository.save(pointDepense);  
		}else {
			throw new StreengeException(new ErrorAPI("Vous n'êtes pas abiliter à effectuer cette manipulation...!"));
		}
		// return pointDepense; 
	}

}
