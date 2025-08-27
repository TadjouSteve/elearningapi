package iri.elearningapi.repository.userRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iri.elearningapi.model.userModel.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

	List<Admin> findAllByOrderByNomAsc();

	List<Admin> findByNomOrderByNomAsc(String nom);

	//Admin findByEmailOrTelephoneAndPassword(String email, String telephone, String password);
	Admin findByEmailOrTelephone(String email,String telephone);

	Admin findByPassword(String password);

	Admin findByEmail(String email);

	Admin findByTelephone(String telephone);
}
