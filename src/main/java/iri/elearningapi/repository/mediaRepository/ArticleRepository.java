package iri.elearningapi.repository.mediaRepository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import iri.elearningapi.model.mediaModel.Article;
import iri.elearningapi.model.mediaModel.Rubrique;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Integer> {
	List<Article> findAllByOrderByIdAsc();
	List<Article> findByTitreOrderByIdAsc(String titre);
	List<Article> findAllByDateBetweenOrderByDateAsc(Date dateDebut,Date dateFin);
	
	List<Article> findByRubriqueOrderByIdAsc(Rubrique rubrique);
	
	boolean existsByTitre(String titre);
	boolean existsByLien(String lien);
	
	Article findByTitre(String titre);
	Article findByLien(String lien);
	
	
	Page<Article> findAllByOrderByDateAsc(Pageable pageable);
	Page<Article> findByTitreOrderByDateAsc(String titre,Pageable pageable);
	Page<Article> findByRubriqueOrderByIdAsc(Rubrique rubrique,Pageable pageable);
	
	Page<Article> findByTitreContainingOrderByDateAsc(String titre,Pageable pageable);
	Page<Article> findBySousTitreContainingOrTexteContainingOrderByDateAsc(String soustitre,String texte,Pageable pageable);
	
	//Page<Article> findAllByOrderByDateAsc(Pageable pageable);
	Page<Article>findAllByDateBetween(Date dateDebut,Date dateFin,Pageable pageable);
}
