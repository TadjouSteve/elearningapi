package iri.elearningapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import iri.elearningapi.utils.errorClass.ElearningException;



@RestControllerAdvice
public class sendErrorMessage {

	@ExceptionHandler
	public ResponseEntity<Object> sendError(ElearningException exception){
		return new ResponseEntity<>(exception.getErrorAPI(),HttpStatus.NOT_ACCEPTABLE);//erreur 406 generer
	}
}
