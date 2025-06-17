package com.example.board.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	private ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		ErrorResponse error = ErrorResponse.of(null, e.getBindingResult());

		return ResponseEntity.badRequest().body(error);
	}

	@ExceptionHandler(EmailDuplicateException.class)
	private ResponseEntity<ErrorResponse> handleEmailDuplicateException(EmailDuplicateException e) {
		ErrorResponse error = ErrorResponse.of(e.getMessage());

		return ResponseEntity.badRequest().body(error);
	}

	@ExceptionHandler(LoginException.class)
	private ResponseEntity<ErrorResponse> handleLoginException(LoginException e) {
		ErrorResponse error = ErrorResponse.of(e.getMessage());

		return ResponseEntity.badRequest().body(error);
	}

}
