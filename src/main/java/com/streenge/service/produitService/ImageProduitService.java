package com.streenge.service.produitService;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.streenge.model.produit.Image;
import com.streenge.model.produit.Produit;
import com.streenge.repository.produitRepo.ImageRepository;
import com.streenge.repository.produitRepo.ProduitRepository;
import com.streenge.service.utils.erroclass.ErrorAPI;
import com.streenge.service.utils.erroclass.StreengeException;
import com.streenge.service.utils.streengeFunction.ImageUtils;

@Service
public class ImageProduitService {
	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private ProduitRepository produitRepository;

	public String uploadImage(MultipartFile file, String idProduit) throws IOException {

		long bytes = file.getSize();
		long kilobytes = (bytes / 1024);
		long megabytes = (kilobytes / 1024);
		System.out.println("Taille du fichier: " + megabytes + " Mo");
		if (megabytes > 3) {
			throw new StreengeException(
					new ErrorAPI("L'image envoyer est trop lourde...! Talle maximale 3 Mo (MegaOctect)"));
		}

		Produit produit = produitRepository.findById(idProduit).get();
		
		if (produit.getImages().size()>=4) {
			throw new StreengeException(
					new ErrorAPI("Vous ne pouvez pas enregistre plus de 4 images par produit."));
		}
		
		Image imageData = new Image();
		imageData.setNom(idProduit + file.getOriginalFilename() + String.valueOf((new Date()).hashCode()));
		imageData.setType(file.getContentType());
		imageData.setImageData(ImageUtils.compressImage(file.getBytes()));
		imageData.setProduit(produit);
		imageData.setDateAjout(new Date());
		imageData = imageRepository.save(imageData);

		/*
		 * Image imageData = repository.save(Image.builder()
		 * .name(file.getOriginalFilename()) .type(file.getContentType())
		 * .imageData(ImageUtils.compressImage(file.getBytes())).build());
		 */
		if (imageData != null) {
			return "file uploaded successfully : " + file.getOriginalFilename();
		} else {
			throw new StreengeException(new ErrorAPI("Impossible d'enregistre l'image"));
		}

	}

	public byte[] downloadImage(int idImage) {
		if (imageRepository.existsById(idImage)) {
			Optional<Image> dbImageData = imageRepository.findById(idImage);
			byte[] images = ImageUtils.decompressImage(dbImageData.get().getImageData());
			return images;
		}else {
			throw new StreengeException(
					new ErrorAPI("image introuvable."));
		}
		
	}

	public void deleteImage(int idImage) {
		imageRepository.deleteImaget(idImage);
	}

}
