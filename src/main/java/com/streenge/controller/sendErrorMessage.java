package com.streenge.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.streenge.service.utils.erroclass.StreengeException;

@RestControllerAdvice
public class sendErrorMessage {

	@ExceptionHandler
	public ResponseEntity<Object> sendError(StreengeException exception){
		return new ResponseEntity<>(exception.getErrorAPI(),HttpStatus.NOT_ACCEPTABLE);//erreur 406 generer
	}
}
