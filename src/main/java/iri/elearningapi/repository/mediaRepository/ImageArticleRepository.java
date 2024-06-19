package iri.elearningapi.repository.mediaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import iri.elearningapi.model.mediaModel.ImageArticle;

@Repository
public interface ImageArticleRepository extends CrudRepository<ImageArticle, Integer> {
	/*@Modifying
	@Transactional
	@Query("DELETE FROM ImageArticle l WHERE l.id = ?1 ")
	void deleteEasy(int id);
	*/
	
	@Modifying
    @Transactional
    @Query("DELETE FROM ImageArticle l WHERE l.id = :id")
    void deleteEasy(int id);
}
