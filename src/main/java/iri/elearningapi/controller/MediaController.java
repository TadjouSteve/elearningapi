package iri.elearningapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import iri.elearningapi.model.mediaModel.Article;
import iri.elearningapi.model.mediaModel.Rubrique;
import iri.elearningapi.service.MediaService;

@RestController
@CrossOrigin
public class MediaController {
	@Autowired
	private MediaService mediaService;
	
	@GetMapping("/media/rubriques/{pageNumer}")
	public Page<Rubrique> getPageRubrique(@PathVariable("pageNumer") int pageNumer){
		System.out.println("Cest  bien arrrver");
		return mediaService.getListRubrique(null, pageNumer);
	}
	
	@GetMapping("/media/rubriques/{pageNumer}/{filter}")
	public Page<Rubrique> getPageRubrique(@PathVariable("pageNumer") int pageNumer,@PathVariable("filter") String filter){
		return mediaService.getListRubrique(filter, pageNumer);
	}
	
	@GetMapping("/media/rubrique/{idRubrique}")
	public Rubrique getRubrique(@PathVariable("idRubrique") int idRubrique){
		return mediaService.getRubrique(idRubrique);
	}
	
	@GetMapping("/media/articles/{pageNumer}")
	public Page<Article> getPageArticle(@PathVariable("pageNumer") int pageNumer){
		return mediaService.getListArticle(null, pageNumer);
	}
	
	@GetMapping("/media/articles/{pageNumer}/{filter}")
	public Page<Article> getPageArticle(@PathVariable("pageNumer") int pageNumer,@PathVariable("filter") String filter){
		return mediaService.getListArticle(filter, pageNumer);
	}
	
	@GetMapping("/media/article/{idArticle}")
	public Article getArticle(@PathVariable("idArticle") int idArticle){
		return mediaService.getArticle(idArticle);
	}
	
	@GetMapping("/media/{nomRubrique}")
	public Rubrique getRubriqueByNom(@PathVariable("nomRubrique") String nomRubrique ) {
		return mediaService.getRubriqueByNom(nomRubrique);
	}
	
		
	@GetMapping("/media/article/{nomRubrique}/{titreArticle}")
	public Article getArticleByRubriqueAndTitre(@PathVariable("nomRubrique") String nomRubrique,@PathVariable("titreArticle") String titreArticle ) {
		return mediaService.getArticleByRubriqueAndTitre(titreArticle, nomRubrique);
	}
	
	@GetMapping("/media/listarticlerubrique/{idRubrique}/{pageNumber}")
	public Page<Article> getAllArticleRubrique(@PathVariable("idRubrique") int idRubrique, @PathVariable("pageNumber") int pageNumber){
		return mediaService.getAllArticleRubrique(idRubrique, pageNumber);
	}
	
	@GetMapping("/public/media/article/{lein}")
	public Article getArticleByLien(@PathVariable("lein") String lein) {
		return mediaService.getArticleByLien(lein);
	}
	
}
