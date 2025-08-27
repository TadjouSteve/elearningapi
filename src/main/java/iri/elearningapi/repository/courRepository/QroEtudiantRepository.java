package iri.elearningapi.repository.courRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iri.elearningapi.model.courModel.Qro;
import iri.elearningapi.model.courModel.QroEtudiant;
import iri.elearningapi.model.userModel.Etudiant;

@Repository
public interface QroEtudiantRepository extends JpaRepository<QroEtudiant, Integer> {
	Boolean existsByEtudiantAndQro(Etudiant etudiant,Qro qro);
}
