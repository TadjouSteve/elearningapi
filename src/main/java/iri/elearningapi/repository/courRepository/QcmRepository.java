package iri.elearningapi.repository.courRepository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import iri.elearningapi.model.courModel.Qcm;

@Repository
public interface QcmRepository extends JpaRepository<Qcm, Integer> {
	@Modifying
    @Transactional
    @Query("DELETE FROM Qcm l WHERE l.id = :id")
    void deleteEasy(int id);
}
