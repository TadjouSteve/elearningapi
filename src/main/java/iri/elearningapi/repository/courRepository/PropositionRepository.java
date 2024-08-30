package iri.elearningapi.repository.courRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import iri.elearningapi.model.courModel.Proposition;

public interface PropositionRepository extends JpaRepository<Proposition, Integer> {

	@Modifying
    @Transactional
    @Query("DELETE FROM Proposition l WHERE l.id = :id")
    void deleteEasy(int id);
}
