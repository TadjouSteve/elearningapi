package iri.elearningapi.repository.courRepository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import iri.elearningapi.model.courModel.Chapitre;

@Repository
public interface ChapitreRepository extends CrudRepository<Chapitre, Integer> {
	List<Chapitre> findAllByOrderByTitreAsc();	
}
