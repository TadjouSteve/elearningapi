package iri.elearningapi.repository.courRepository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import iri.elearningapi.model.courModel.Module;

@Repository
public interface ModuleRepository extends CrudRepository<Module, Integer> {
	List<Module> findAllByOrderByTitreAsc();	
}
