package iri.elearningapi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import iri.elearningapi.model.Region;
import iri.elearningapi.model.mediaModel.Rubrique;
import iri.elearningapi.model.userModel.GammeEtudiant;
import iri.elearningapi.repository.RegionRepository;
import iri.elearningapi.repository.mediaRepository.RubriqueRepository;
import iri.elearningapi.repository.userRepository.GammeEtudiantRepository;
import iri.elearningapi.utils.elearningData.Statut;


@RestController
@CrossOrigin
@RequestMapping("/metadata")
public class MetaDataController {
	@Autowired
	private RubriqueRepository rubriqueRepository;
	
	@Autowired
	private GammeEtudiantRepository gammeEtudiantRepository;
	
	@Autowired
	private RegionRepository regionRepository;
	
	@GetMapping("/rubriques")
	public List<Rubrique> getListRubrique() {
		List<Rubrique> rubriques=new ArrayList<>();
		List<Rubrique> rubriques02=new ArrayList<>();
		rubriques=rubriqueRepository.findAllByOrderByNomAsc();
		for (Rubrique rubrique : rubriques) {
			if (Statut.OUVERT.name().equals(rubrique.getStatut())) {
				rubriques02.add(rubrique);
			}
		}
		return rubriques02;
	}
	
	@GetMapping("/inscription/")
	public MetaData getMethodName() {
		MetaData metaData = new MetaData(); 
		metaData.setGammeEtudiants(gammeEtudiantRepository.findAll());
		metaData.setRegions(regionRepository.findAllByOrderByNomAsc());
		return metaData;
	}
		
	
	
	
	class MetaData{
		private List<Region> regions=new ArrayList<Region>();
		private List<GammeEtudiant> gammeEtudiants=new ArrayList<GammeEtudiant>();
		
		public List<Region> getRegions() {
			return regions;
		}
		public void setRegions(List<Region> regions) {
			this.regions = regions;
		}
		public List<GammeEtudiant> getGammeEtudiants() {
			return gammeEtudiants;
		}
		public void setGammeEtudiants(List<GammeEtudiant> gammeEtudiants) {
			this.gammeEtudiants = gammeEtudiants;
		}
	}
}
