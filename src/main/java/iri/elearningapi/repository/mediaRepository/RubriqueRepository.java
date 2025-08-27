package iri.elearningapi.repository.mediaRepository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import iri.elearningapi.model.mediaModel.Rubrique;

@Repository
public interface RubriqueRepository extends CrudRepository<Rubrique, Integer> {
	List<Rubrique> findAllByOrderByNomAsc();

	Boolean existsByNom(String Nom);

	Rubrique findByNom(String Nom);
	
	
	Page<Rubrique> findAllByOrderByNomAsc(Pageable pageable);
	Page<Rubrique> findByNomOrderByNomAsc(String nom,Pageable pageable);
	
}
