package iri.elearningapi.repository.userRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iri.elearningapi.model.userModel.GammeEtudiantProfesseur;

@Repository
public interface GammeEtudiantProfesseurRepository extends JpaRepository<GammeEtudiantProfesseur, Integer> {

}
