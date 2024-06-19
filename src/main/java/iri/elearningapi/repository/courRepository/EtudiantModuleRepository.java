package iri.elearningapi.repository.courRepository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import iri.elearningapi.model.courModel.EtudiantModule;


@Repository
public interface EtudiantModuleRepository extends CrudRepository<EtudiantModule, Integer> {

}
