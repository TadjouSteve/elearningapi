package iri.elearningapi.service;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import iri.elearningapi.model.mediaModel.Article;
import iri.elearningapi.model.mediaModel.ImageArticle;
import iri.elearningapi.repository.mediaRepository.ArticleRepository;
import iri.elearningapi.repository.mediaRepository.ImageArticleRepository;
import iri.elearningapi.utils.elearningFunction.ImageUtils;
import iri.elearningapi.utils.errorClass.ElearningException;
import iri.elearningapi.utils.errorClass.ErrorAPI;

@Service
public class ImageService {
	@Autowired
	private ImageArticleRepository imageArticleRepository;
	
	@Autowired
	private ArticleRepository articleRepository;
	
	public void ajoutTitreImageArticle(int idImage,String titre) {
		if (imageArticleRepository.existsById(idImage)) {
			if (titre!=null) {
				if (titre.startsWith("\"")) {
					titre = titre.substring(1);
		        }

		        // Supprimer les guillemets doubles en fin de chaîne
		        if (titre.endsWith("\"")) {
		        	titre = titre.substring(0, titre.length() - 1);
		        }
			}
			
			if (titre==null || titre.trim().length()<=9) {
				throw new ElearningException(new ErrorAPI("Le titre de l'image doit contenir au moins 10 carractere...!"));
			}
			
			ImageArticle imageArticle=imageArticleRepository.findById(idImage).get();
			imageArticle.setTitre(titre.trim());
			imageArticleRepository.save(imageArticle);
		}else {
			throw new ElearningException(new ErrorAPI("L'image index n'existe pas...!"));
		}
	}

	public String uploadImageArticle(MultipartFile file, int idArticle) throws IOException {
		long bytes = file.getSize();
		long kilobytes = (bytes / 1024);
		long megabytes = (kilobytes / 1024);
		System.out.println("Taille du fichier: " + megabytes + " Mo");
		if (megabytes > 3) {
			throw new ElearningException(
					new ErrorAPI("L'image envoyer est trop lourde...! Talle maximale 3 Mo (MegaOctect)"));
		}

		if (articleRepository.existsById(idArticle)) {
			Article article = articleRepository.findById(idArticle).get();

			if (article.getImageArticles().size() >= 4) {
				throw new ElearningException(
						new ErrorAPI("Vous ne pouvez pas enregistre plus de 4 images par article."));
			}
			
			if (article.getImageArticles()!=null && !article.getImageArticles().isEmpty()) {
				int idImageDelete=article.getImageArticles().get(0).getId();
				imageArticleRepository.deleteEasy(idImageDelete);
			}

			ImageArticle imageArticle = new ImageArticle();
			imageArticle.setNom("Art-" + String.valueOf(idArticle) + "-" + file.getOriginalFilename()
					+ String.valueOf((new Date()).hashCode()));
			imageArticle.setType(file.getContentType());
			imageArticle.setImageData(ImageUtils.compressImage(file.getBytes()));
			imageArticle.setArticle(article);
			imageArticle.setDate(new Date());
			imageArticle = imageArticleRepository.save(imageArticle);

			if (imageArticle != null) {
				return "file uploaded successfully : " + file.getOriginalFilename();
			} else {
				throw new ElearningException(new ErrorAPI("Impossible d'enregistre l'image"));
			}

		} else {
			throw new ElearningException(new ErrorAPI("L'article selectionner pour contenir l'image n'existe pas...!"));
		}
	}
	
	
	public byte[] downloadImageArticle(int idImage) {
		if (imageArticleRepository.existsById(idImage)) {
			Optional<ImageArticle> dbImageData = imageArticleRepository.findById(idImage);
			byte[] images = ImageUtils.decompressImage(dbImageData.get().getImageData());
			return images;
		}else {
			throw new ElearningException(
					new ErrorAPI("image introuvable."));
		}
		
	}

	public void deleteImageArticle(int idImage) {
		imageArticleRepository.deleteEasy(idImage);
	}
}
