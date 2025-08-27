package com.streenge.repository.adminRepository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streenge.model.admin.Utilisateur;

@Repository
public interface UtilisateurRepository extends CrudRepository<Utilisateur, Integer> {
	public List<Utilisateur> findByNom(String nom);
	public List<Utilisateur> findByNomAffichage(String nom);
	
	public List<Utilisateur> findByLogin(String login);
	
	public List<Utilisateur> findByLoginAndPassword(String login,String password);
	
	public List<Utilisateur> findAllByOrderByNomAsc();
	
	public List<Utilisateur> findByNomStartingWithOrderByNomAsc(String prefixNom);
	public List<Utilisateur> findByNomStartingWithOrNomAffichageStartingWithOrTelephoneStartingWithOrderByNomAsc(String prefixNom, String prefixNonAffichage,String prefixTelephone);
	public List<Utilisateur> findByStatutStartingWithOrderByNomAsc(String prefixStatut);
}
