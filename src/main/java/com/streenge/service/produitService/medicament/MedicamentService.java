package com.streenge.service.produitService.medicament;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.streenge.model.admin.Entreprise;
import com.streenge.model.produit.Produit;
import com.streenge.model.produit.medicament.ComposantMedicament;
import com.streenge.model.produit.medicament.Medicament;
import com.streenge.repository.adminRepository.EntrepriseRepository;
import com.streenge.repository.produitRepo.ProduitRepository;
import com.streenge.repository.produitRepo.medicamentRepo.ComposantMedicamentRepository;
import com.streenge.repository.produitRepo.medicamentRepo.ComposantRepository;
import com.streenge.repository.produitRepo.medicamentRepo.MedicamentRepository;
import com.streenge.service.utils.formData.formMedicament.FormComposant;
import com.streenge.service.utils.formData.formMedicament.FormMedicament;

@Service
public class MedicamentService {
	@Autowired
	private MedicamentRepository medicamentRepository;

	@Autowired
	ProduitRepository produitRepository;

	@Autowired
	private ComposantRepository composantRepository;

	@Autowired
	private ComposantMedicamentRepository composantMedicamentRepository;

	@Autowired
	private EntrepriseRepository entrepriseRepository;

	// @SuppressWarnings("unused")
	public Medicament createMedicament(FormMedicament formMedicament, Produit produit) {
		
		if (!((List<Entreprise>) entrepriseRepository.findAll()).isEmpty()
				&& ((List<Entreprise>) entrepriseRepository.findAll()).get(0).getLevel() == 5 && formMedicament != null
				&& produit != null && produitRepository.existsById(produit.getIdProduit())) {
			//formController(formMedicament);
			produit = produitRepository.findById(produit.getIdProduit()).get();
			Medicament medicament = new Medicament();
			if (produit.getMedicament() != null) {
				medicament = produit.getMedicament();
			}
			medicament.setNomCommercial(formMedicament.getNomCommercial());
			medicament.setNomLaboratoire(formMedicament.getNomLaboratoire());
			medicament.setAvertissement(formMedicament.getAvertissement());
			medicament.setDescription(formMedicament.getDescription());
			medicament.setPosologie(formMedicament.getPosologie());
			medicament.setPack(formMedicament.getPack());

			if (medicament.getComposantMedicaments() != null) {
				List<ComposantMedicament> composantMedicaments = medicament.getComposantMedicaments();
				for (ComposantMedicament composantMedicament : composantMedicaments) {
					composantMedicamentRepository.deleteComposantMedicament(composantMedicament.getId());
				}
			}

			medicament = medicamentRepository.save(medicament);
			produit.setMedicament(medicament);
			produitRepository.save(produit);
			AddComposantToMedicament(formMedicament.getComposants(), medicament);

			return medicamentRepository.findById(medicament.getId()).get();
		}
		return null;
	}

	private void AddComposantToMedicament(List<FormComposant> FormComposants, Medicament medicament) {
		// List<FormComposant> formComposantsUnique=new ArrayList<>();

		for (FormComposant formComposant : FormComposants) {
			if (composantRepository.existsById(formComposant.getId())) {
				ComposantMedicament composantMedicament = new ComposantMedicament();
				
				composantMedicament.setComposant(composantRepository.findById(formComposant.getId()).get());
				composantMedicament.setMedicament(medicament);
				composantMedicament.setDosage(formComposant.getDosage());
				composantMedicamentRepository.save(composantMedicament);
			}
		}
	}

	/*private void formController(FormMedicament formMedicament) {
		if (formMedicament.getNomLaboratoire() != null && (formMedicament.getNomLaboratoire().isBlank()
				|| formMedicament.getNomLaboratoire().length() < 3)) {
			throw new StreengeException(new ErrorAPI(
					"Le Non du laboratoire du medicament ne doit pas êre vide, ou contenir moins de trois(03) Caractères"));
		}

	}*/
	
	public FormMedicament getFormMedicament(int idMedicament) {
		if (medicamentRepository.existsById(idMedicament)) {
			Medicament medicament=medicamentRepository.findById(idMedicament).get();
			FormMedicament formMedicament=new FormMedicament();
			
			formMedicament.setId(medicament.getId());
			formMedicament.setNomCommercial(medicament.getNomCommercial());
			formMedicament.setNomLaboratoire(medicament.getNomLaboratoire());
			formMedicament.setPack(medicament.getPack());
			formMedicament.setPosologie(medicament.getPosologie());
			formMedicament.setAvertissement(medicament.getAvertissement());
			formMedicament.setDescription(medicament.getDescription());
			formMedicament.setComposants(new ArrayList<>());
			
			if (medicament.getComposantMedicaments()!=null) {
				for (ComposantMedicament composantMedicament : medicament.getComposantMedicaments()) {
					FormComposant formComposant=new FormComposant();
					formComposant.setId(composantMedicament.getComposant().getId());
					formComposant.setDescription(composantMedicament.getComposant().getDescription());
					formComposant.setNom(composantMedicament.getComposant().getNom());
					formComposant.setIndication(composantMedicament.getComposant().getIndication());
					formComposant.setPrecaution(composantMedicament.getComposant().getPrecaution());
					formComposant.setDosage(composantMedicament.getDosage());
					
					formMedicament.getComposants().add(formComposant);
				}
			}
			//System.out.println("ok c'est bon par ic");
			return formMedicament;
		}
		return null;
	}

}
