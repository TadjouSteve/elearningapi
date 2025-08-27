package com.streenge.repository.contactRepo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.streenge.model.contact.Message;

@Repository
public interface messageRepository extends CrudRepository<Message, Integer> {
	@Modifying
	@Transactional
	@Query("DELETE FROM Message l WHERE l.id = ?1 ")
	void deleteMessage(int id);
}
