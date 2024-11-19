package iri.elearningapi.repository.userRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iri.elearningapi.model.userModel.Candidature;

@Repository
public interface CandidatureRepository extends JpaRepository<Candidature, Integer> {

}
