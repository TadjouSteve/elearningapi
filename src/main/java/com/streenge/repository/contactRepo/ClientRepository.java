package com.streenge.repository.contactRepo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streenge.model.contact.Client;

@Repository
public interface ClientRepository extends CrudRepository<Client, String> {

	public List<Client> findByNom(String nom);
	public List<Client> findByIdClientStartingWithOrderByNomAsc(String prefix);

	public List<Client> findAllByOrderByNomAsc();
	public List<Client> findByNomStartingWithOrderByNomAsc(String prefix);
	
	public List<Client> findByNomStartingWithOrTelephoneStartingWithOrderByNomAsc(String prefixNom,String prefixphone);
	
	public List<Client> findByNomContainingOrTelephoneContainingOrderByNomAsc(String prefixNom,String prefixphone);
	
	public List<Client> findByNomContainingOrTelephoneContainingOrAdresseContainingOrderByNomAsc(String prefixNom,String prefixphone,String prefixadresse);

}
