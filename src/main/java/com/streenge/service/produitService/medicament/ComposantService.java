package com.streenge.service.produitService.medicament;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.streenge.model.produit.medicament.Composant;
import com.streenge.model.produit.medicament.ComposantMedicament;
import com.streenge.repository.produitRepo.medicamentRepo.ComposantMedicamentRepository;
import com.streenge.repository.produitRepo.medicamentRepo.ComposantRepository;
import com.streenge.service.utils.erroclass.ErrorAPI;
import com.streenge.service.utils.erroclass.StreengeException;
import com.streenge.service.utils.streengeFunction.Methode;

@Service
public class ComposantService {

	@Autowired
	private ComposantRepository composantRepository;

	@Autowired
	private ComposantMedicamentRepository composantMedicamentRepository;

	public Composant createOrUpdate(Composant composant) {
		return createOrUpdate(composant, true);
	}

	
	public Composant createOrUpdate(Composant composant, boolean isNew) {
		if (composant != null) {
			composant.setNom(Methode.upperCaseFirst(composant.getNom()));
			controlForm(composant);
			composant = composantRepository.save(composant);
		}
		return composant;
	}
	
	public void delete(Composant composant) {
		if (composant!=null) {
			delete(composant.getId());
		}
	}

	public void delete(int idComposant) {
		if (composantRepository.findById(idComposant).isPresent()) {
			Composant composant = composantRepository.findById(idComposant).get();
			List<ComposantMedicament> listcompoMed = composant.getComposantMedicaments();
			for (ComposantMedicament composantMedicament : listcompoMed) {
				composantMedicamentRepository.deleteComposantMedicament(composantMedicament.getId());
			}

			composantRepository.deleteById(composant.getId());
		}
	}

	private void controlForm(Composant composant) {
		if (composant.getNom() == null || composant.getNom().isBlank() || composant.getNom().length() < 3) {
			throw new StreengeException(new ErrorAPI(
					"Le Non du composant ne doit pas êre vide, ou contenir moins de trois(03) Caractères"));
		}

		if (composant.getNom() == null || composant.getNom().isBlank() || composant.getNom().length() > 50) {
			throw new StreengeException(new ErrorAPI("Le Non du composant est trop long, maximun 50 Caracteres"));
		}

		if (!composantRepository.findByNom(composant.getNom()).isEmpty()) {
			Composant composant2 = composantRepository.findByNom(composant.getNom()).get(0);
			if (composant2.getNom().equals(composant.getNom()) && composant2.getId() != composant.getId()) {
				throw new StreengeException(new ErrorAPI("Un autre Composant existe deja avec ce nom...!"));
			}
		}
	}
}
