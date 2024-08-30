package iri.elearningapi.repository.courRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import iri.elearningapi.model.courModel.Qro;

@Repository
public interface QroRepository extends JpaRepository<Qro, Integer> {

	
	@Modifying
    @Transactional
    @Query("DELETE FROM Qro l WHERE l.id = :id")
    void deleteEasy(int id);
}
