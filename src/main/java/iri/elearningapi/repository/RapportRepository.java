package iri.elearningapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iri.elearningapi.model.Rapport;
import iri.elearningapi.model.userModel.Etudiant;


@Repository
public interface RapportRepository extends JpaRepository<Rapport, Integer> {
	boolean existsByEtudiant(Etudiant etudiant);
	List<Rapport> findByEtudiant(Etudiant etudiant);
}
