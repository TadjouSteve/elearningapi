package iri.elearningapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import iri.elearningapi.service.ImageService;

@RestController
@CrossOrigin
public class ImageController {
	
	@Autowired
	private ImageService imageService;
	
	@GetMapping("/image/article/{idImage}")
	public ResponseEntity<?> downloadImage(@PathVariable("idImage") int idImage) {
		byte[] imageData = imageService.downloadImageArticle(idImage);
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(imageData);
	}
}
