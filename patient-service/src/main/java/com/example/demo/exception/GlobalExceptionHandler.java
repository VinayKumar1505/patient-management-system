package com.example.demo.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger logger= LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex){
		Map<String, String> errors= new HashMap<>();
		
		ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
		
		return ResponseEntity.badRequest().body(errors);
	}
	
	@ExceptionHandler(PatientAlreadyExistsException.class)
	public ResponseEntity<Map<String, String>> handleEmailAlreadyExistsException(PatientAlreadyExistsException ex){
		Map<String, String> errors= new HashMap<>();
		
		logger.warn("the email with patient already exist {}", ex.getMessage());		
		errors.put("message", "The Patient with this email is already exists");
		return ResponseEntity.badRequest().body(errors);//returned as response to api
	}
	
	@ExceptionHandler(PatientNotFoundException.class)
	public ResponseEntity<Map<String, String>> handlePatientNotFoundException(PatientNotFoundException ex){
		Map<String, String> errors= new HashMap<>();
		
		logger.warn("the patient does not exist ", ex.getMessage());
		errors.put("message", "Patient Does not exist");
		return ResponseEntity.badRequest().body(errors);
	}
}
