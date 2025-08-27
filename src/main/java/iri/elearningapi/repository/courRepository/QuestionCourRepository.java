package iri.elearningapi.repository.courRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iri.elearningapi.model.courModel.QuestionCour;

@Repository
public interface QuestionCourRepository extends JpaRepository<QuestionCour, Integer> {
	
}
