package iri.elearningapi.repository.userRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iri.elearningapi.model.userModel.GammeEtudiant;


@Repository
public interface GammeEtudiantRepository extends JpaRepository<GammeEtudiant, Integer> {
	List<GammeEtudiant> findAllByOrderByNomAsc();
}
