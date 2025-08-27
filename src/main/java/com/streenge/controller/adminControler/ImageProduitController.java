package com.streenge.controller.adminControler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.streenge.service.produitService.ImageProduitService;
import com.streenge.service.utils.URLStreenge;

@RestController
@CrossOrigin
public class ImageProduitController {
	@Autowired
	private ImageProduitService service;

	@PostMapping("/image/{idProduit}")
	public URLStreenge uploadImage(@PathVariable("idProduit") String idProduit,
			@RequestParam("image") MultipartFile file) throws IOException {
		service.uploadImage(file, idProduit);
		URLStreenge urlStreenge = new URLStreenge();
		urlStreenge.setUrl("/produits/");
		urlStreenge.setData(true);
		System.out.println("enregistrement terminer");
		return urlStreenge;
	}

	@GetMapping("/image/{idImage}")
	public ResponseEntity<?> downloadImage(@PathVariable("idImage") int idImage) {
		byte[] imageData = service.downloadImage(idImage);
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(imageData);
	}

	@DeleteMapping("/image/{idImage}")
	public URLStreenge deleteImageProduit(@PathVariable("idImage") int idImage) {
		service.deleteImage(idImage);
		return new URLStreenge();
	}
}
