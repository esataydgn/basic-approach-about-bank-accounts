package com.sunhill.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.sunhill.app.exception.business.BusinessException;
import com.sunhill.app.exception.validation.ValidationException;
import com.sunhill.app.response.ErrorResponse;

@RestControllerAdvice
public class AccountControllerAdvice extends ResponseEntityExceptionHandler {

	@ExceptionHandler({ ValidationException.class })
	public ResponseEntity validationException(ValidationException exception) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setMessage(exception.getMessage());

		return new ResponseEntity(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler({ BusinessException.class })
	public ResponseEntity businessExceptions(BusinessException exception) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setMessage(exception.getMessage());

		return new ResponseEntity(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
	}

}
