package iri.elearningapi.repository.courRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import iri.elearningapi.model.courModel.GammeEtudiantModule;

@Repository
public interface GammeEtudiantModuleRepository extends JpaRepository<GammeEtudiantModule, Integer> {
	@Modifying
	@Transactional
	@Query("DELETE FROM GammeEtudiantModule l WHERE l.id = ?1 ")
	void deleteEasy(int id);
}
