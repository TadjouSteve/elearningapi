package iri.elearningapi.repository.courRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iri.elearningapi.model.courModel.Chapitre;
import iri.elearningapi.model.courModel.EtudiantChapitre;
import iri.elearningapi.model.userModel.Etudiant;

@Repository
public interface EtudiantChapitreRepository extends JpaRepository<EtudiantChapitre, Integer> {
	EtudiantChapitre findByEtudiantAndChapitre(Etudiant Etudiant,Chapitre Chapitre);

}
