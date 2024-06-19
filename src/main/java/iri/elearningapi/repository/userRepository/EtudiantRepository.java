package iri.elearningapi.repository.userRepository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iri.elearningapi.model.userModel.Etudiant;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Integer> {
	List<Etudiant> findAllByOrderByNomAsc();
	List<Etudiant> findByNomOrderByNomAsc(String nom);
	
	Boolean existsByMatricule(String matricule);
	boolean existsByTelephone(String telephone);
	boolean existsByEmail(String email);
	
	//List<Etudiant> findNomStartingWithOrderByNomAsc(String nom);
	
	Page<Etudiant> findAllByOrderByNomAsc(Pageable pageable);
	Page<Etudiant> findByNomOrderByNomAsc(String nom,Pageable pageable);
	
	Page<Etudiant> findByNomStartingWithOrPrenomStartingWithOrRegionStartingWithOrEmailStartingWithOrNomEntrepriseStartingWithOrderByNomAsc(String nom,String prenom,String region,String email,String nomEntreprise,Pageable pageable);
	
	Page<Etudiant> findByNomContainingOrPrenomContainingOrRegionContainingOrEmailContainingOrNomEntrepriseContainingOrderByNomAsc(String nom, String prenom, String region, String email, String nomEntreprise, Pageable pageable);

	Etudiant findByEmailOrTelephoneAndPassword(String email,String telephone,String password);
	Etudiant findByEmailOrTelephone(String email,String telephone);
	Etudiant findByPassword(String password);
	
	
	Etudiant findByEmail(String email);

	Etudiant findByTelephone(String telephone);

	Etudiant findByMatricule(String matricule);
	
	
}
