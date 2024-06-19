package iri.elearningapi.repository.courRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import iri.elearningapi.model.courModel.ProfesseurModule;

@Repository
public interface ProfesseurModuleRepository extends CrudRepository<ProfesseurModule, Integer> {
	@Modifying
	@Transactional
	@Query("DELETE FROM ProfesseurModule l WHERE l.id = ?1 ")
	void deleteEasy(int id);
}
