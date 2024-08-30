package iri.elearningapi.repository.userRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import iri.elearningapi.model.userModel.Etudiant;
import iri.elearningapi.model.userModel.Information;

@Repository
public interface InformationRepository extends JpaRepository<Information, Integer> {
	
	boolean existsByEtudiant(Etudiant etudiant);
	Information findByEtudiant(Etudiant etudiant);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM Information l WHERE l.id = ?1 ")
	void deleteEasy(int id);
}
