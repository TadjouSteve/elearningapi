package iri.elearningapi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import iri.elearningapi.model.mediaModel.Rubrique;
import iri.elearningapi.repository.mediaRepository.RubriqueRepository;
import iri.elearningapi.utils.elearningData.Statut;

@RestController
@CrossOrigin
@RequestMapping("/metadata")
public class MetaDataController {
	@Autowired
	private RubriqueRepository rubriqueRepository;
	
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
}
