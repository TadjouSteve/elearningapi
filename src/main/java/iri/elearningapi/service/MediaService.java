package iri.elearningapi.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import iri.elearningapi.model.mediaModel.Article;
import iri.elearningapi.model.mediaModel.Rubrique;
import iri.elearningapi.repository.mediaRepository.ArticleRepository;
import iri.elearningapi.repository.mediaRepository.RubriqueRepository;
import iri.elearningapi.utils.elearningData.Etat;
import iri.elearningapi.utils.elearningData.Statut;
import iri.elearningapi.utils.elearningFunction.Methode;
import iri.elearningapi.utils.errorClass.ElearningException;
import iri.elearningapi.utils.errorClass.ErrorAPI;

@Service
public class MediaService {
	@Autowired
	RubriqueRepository rubriqueRepository;

	@Autowired
	ArticleRepository articleRepository;

	public Rubrique createRubrique(Rubrique rubrique) {
		controlRubrique(rubrique);
		rubrique.setDate(new Date());
		rubrique.setStatut(Statut.OUVERT.name());
		rubrique.setEtat(Etat.ACTIF.getValeur());
		rubrique = rubriqueRepository.save(rubrique);
		return rubrique;
	}

	public Article createArticle(Article article, int idRubrique) {
		if (rubriqueRepository.existsById(idRubrique)) {
			controlArticle(article);
			article.setDate(new Date());
			article.setRubrique(rubriqueRepository.findById(idRubrique).get());
			article.setStatut(Statut.EN_ATTENTE.name());
			article.setEtat(Etat.ACTIF.getValeur());
			String lien = "";
			do {
				lien = Methode.generateRandomString();
			} while (articleRepository.existsByLien(lien));
			article.setLien(lien);
			article = articleRepository.save(article);
			return article;
		} else {
			throw new ElearningException(new ErrorAPI("La rubrique de l'article n'est pas definie....!"));
		}
	}

	public Rubrique getRubrique(int idRubrique) {
		// Rubrique rubrique=new Rubrique();
		if (rubriqueRepository.existsById(idRubrique)) {
			Rubrique rubrique = rubriqueRepository.findById(idRubrique).get();
			rubrique.setNombreArticle(rubrique.getArticles() != null ? rubrique.getArticles().size() : 0);
			return rubrique;
		} else {
			throw new ElearningException(new ErrorAPI("Aucune rubrique journalistique trouver....!"));
		}
	}

	public Article getArticle(int idArticle) {
		// Rubrique rubrique=new Rubrique();
		if (articleRepository.existsById(idArticle)) {
			return articleRepository.findById(idArticle).get();
		} else {
			throw new ElearningException(new ErrorAPI("Aucun article journalistique trouver....!"));
		}
	}

	public Article getArticleByLien(String lien) {
		if (articleRepository.existsByLien(lien)) {
			Article article = articleRepository.findByLien(lien);
			if (!Statut.PUBLIER.name().equals(article.getStatut())) {
				throw new ElearningException(new ErrorAPI("Cet article n'est pas accessible au public"));
			}

			return article;
		} else {
			throw new ElearningException(new ErrorAPI("Aucun article trouver pour ce lien....!"));
		}
	}

	public Page<Rubrique> getListRubrique(String filter, int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, 50);

		Page<Rubrique> pageRubrique;

		if (filter != null) {
			pageRubrique = rubriqueRepository.findByNomOrderByNomAsc(filter, pageable);
		} else {
			pageRubrique = rubriqueRepository.findAllByOrderByNomAsc(pageable);
		}
		System.out.println("liste obtenue == " + pageRubrique.getNumber());
		return pageRubrique;
	}

	public Page<Article> getListArticle(String filter, int pageNumber) {

		Pageable pageable = PageRequest.of(pageNumber, 20);

		Page<Article> pageArticle;

		if (filter != null) {
			pageArticle = articleRepository.findByTitreOrderByDateAsc(filter, pageable);
		} else {
			pageArticle = articleRepository.findAllByOrderByDateAsc(pageable);
		}

		return pageArticle;
	}

	private void controlRubrique(Rubrique rubrique) {
		if (rubrique.getNom() == null || rubrique.getNom().length() < 5) {
			throw new ElearningException(new ErrorAPI(
					"Le nom de la  rubrique ne doit pas être vide, ou contenir moins de trois(03) caractères", 0));
		}

		if (rubrique.getNom() != null && rubrique.getNom().length() > 30) {
			throw new ElearningException(
					new ErrorAPI("Le nom de la rubrique est trop long...! Il ne doit pas dépasser 50 caractères", 0));
		}
	}

	private void controlArticle(Article article) {
		if (article.getTitre() == null || article.getTitre().length() < 10) {
			throw new ElearningException(new ErrorAPI(
					"Le Titre de l'article ne doit pas être vide, ou contenir moins de dix(10) caractères", 0));
		}

		if (article.getTitre() != null && article.getTitre().length() > 150) {
			throw new ElearningException(
					new ErrorAPI("Le Titre de l'article est trop long...! Il ne doit pas dépasser 150 caractères", 0));
		}

		if (article.getTexte() == null || article.getTexte().length() < 30) {
			throw new ElearningException(new ErrorAPI(
					"Le Texte de l'article ne doit pas être vide, ou contenir moins de trente(30) caractères", 0));
		}

		if (article.getAuteur() == null || article.getAuteur().length() < 10) {
			throw new ElearningException(new ErrorAPI(
					"Le Nom de l'auteur ne doit pas être vide, ou contenir moins de dix(10) caractères", 0));
		}

	}

	public Rubrique getRubriqueByNom(String nomRubrique) {
		if (rubriqueRepository.existsByNom(nomRubrique)) {
			Rubrique rubrique = rubriqueRepository.findByNom(nomRubrique);
			rubrique.setNombreArticle(rubrique.getArticles() != null ? rubrique.getArticles().size() : 0);
			return rubrique;
		} else {
			throw new ElearningException(new ErrorAPI("Aucune rubrique journalistique trouver....!"));
		}
	}

	public Article getArticleByRubriqueAndTitre(String titreArticle, String nomRubrique) {
		if (rubriqueRepository.existsByNom(nomRubrique)) {
			if (articleRepository.existsByTitre(titreArticle)) {
				return articleRepository.findByTitre(titreArticle);
			} else {
				throw new ElearningException(new ErrorAPI("L'article recherché n'existe pas....!"));
			}
		} else {
			throw new ElearningException(new ErrorAPI("La rubrique visée n'existe pas....!"));
		}
	}

	public Page<Article> getAllArticleRubrique(int idRubrique, int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, 50);
		Page<Article> pageArticle;

		if (rubriqueRepository.existsById(idRubrique)) {
			pageArticle = articleRepository.findByRubriqueOrderByIdAsc(rubriqueRepository.findById(idRubrique).get(),
					pageable);
			return pageArticle;
		} else {
			throw new ElearningException(new ErrorAPI("La rubrique visée n'existe pas....!"));
		}

	}

	public Boolean changeStatutArticle(int idArticle) {
		if (articleRepository.existsById(idArticle)) {
			// Statut statut;
			Article article = articleRepository.findById(idArticle).get();

			if (Statut.EN_ATTENTE.name().equals(article.getStatut())) {
				article.setStatut(Statut.PUBLIER.name());
				article.setDatePublication(new Date());
			} else if (Statut.PUBLIER.name().equals(article.getStatut())) {
				article.setStatut(Statut.SUSPENDU.name());
			} else if (Statut.SUSPENDU.name().equals(article.getStatut())) {
				article.setStatut(Statut.PUBLIER.name());
				article.setDatePublication(new Date());
			}
			article = articleRepository.save(article);
			return true;
		} else {
			return false;
		}
	}

}
