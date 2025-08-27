package iri.elearningapi.repository.userRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import iri.elearningapi.model.userModel.GammeEtudiantProfesseur;

@Repository
public interface GammeEtudiantProfesseurRepository extends JpaRepository<GammeEtudiantProfesseur, Integer> {
	@Modifying
	@Transactional
	@Query("DELETE FROM GammeEtudiantProfesseur l WHERE l.id = ?1 ")
	void deleteEasy(int id);
}
