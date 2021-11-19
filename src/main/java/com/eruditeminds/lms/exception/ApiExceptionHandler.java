package com.eruditeminds.lms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * ApiExceptionHandler aop based exception handling.
 * 
 * @author Shreya S Jalihal
 */
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	/*
	 * Method to handle ValidationException
	 * 
	 * @param ValidationException validationException
	 * 
	 * @throws WebRequest request
	 * 
	 * @return ResponseEntity with ErrorResponse
	 */
	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponse> handleValidationException(ValidationException validationException) {
		return buildErrorResponse(validationException, validationException.getMessage(), HttpStatus.BAD_REQUEST);
	}

	/*
	 * Method to handle ResourceNotFoundException
	 * 
	 * @param ResourceNotFoundException resourceNotFoundException
	 * 
	 * @throws WebRequest request
	 * 
	 * @return ResponseEntity with ErrorResponse
	 */
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ErrorResponse> handleInvalidJsonFileException(
			ResourceNotFoundException resourceNotFoundException) {
		return buildErrorResponse(resourceNotFoundException, resourceNotFoundException.getMessage(),
				HttpStatus.NOT_FOUND);
	}

	/*
	 * Method to build ErrorResponse
	 * 
	 * @param Exception exception
	 * 
	 * @param String message
	 * 
	 * @param HttpStatus httpStatus
	 * 
	 * @param WebRequest request
	 * 
	 * @return ResponseEntity responseEntitty with ErrorResponse
	 */
	private ResponseEntity<ErrorResponse> buildErrorResponse(Exception exception, String message,
			HttpStatus httpStatus) {
		ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());
		return ResponseEntity.status(httpStatus).body(errorResponse);
	}

}
