package iri.elearningapi.repository.courRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iri.elearningapi.model.courModel.QroEtudiant;

@Repository
public interface QroEtudiantRepository extends JpaRepository<QroEtudiant, Integer> {

}
