package iri.elearningapi.repository.userRepository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iri.elearningapi.model.userModel.Professeur;

@Repository
public interface ProfesseurRepository extends JpaRepository<Professeur, Integer> {
	List<Professeur> findAllByOrderByNomAsc();

	Boolean existsByMatricule(String matricule);

	Boolean existsByEmail(String matricule);

	Boolean existsByTelephone(String Telephone);

	List<Professeur> findByNomOrderByNomAsc(String nom);

	Professeur findByEmailOrTelephoneAndPassword(String email, String telephone, String password);

	Professeur findByEmailOrTelephone(String email, String telephone);

	Professeur findByPassword(String password);

	Page<Professeur> findAllByOrderByNomAsc(Pageable pageable);

	Page<Professeur> findByNomOrderByNomAsc(String nom, Pageable pageable);

	Page<Professeur> findByNomStartingWithOrPrenomStartingWithOrProfessionStartingWithOrEmailStartingWithOrderByNomAsc(
			String nom, String prenom, String profession, String email, Pageable pageable);

	Page<Professeur> findByNomContainingOrPrenomContainingOrProfessionContainingOrEmailContainingOrderByNomAsc(
			String nom, String prenom, String profession, String email, Pageable pageable);

	Professeur findByEmail(String email);

	Professeur findByTelephone(String telephone);
	
	Professeur findByMatricule(String matricule);
}
