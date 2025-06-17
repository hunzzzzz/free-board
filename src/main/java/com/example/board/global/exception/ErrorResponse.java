package com.example.board.global.exception;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorResponse {
	private String message;
	private Map<String, String> errors;

	public static ErrorResponse of(String message) {
		return ErrorResponse.builder().message(message).build();
	}

	public static ErrorResponse of(String message, BindingResult bindingResult) {
		Map<String, String> errors = bindingResult.getFieldErrors().stream()
				.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

		return ErrorResponse.builder()
				.message(message)
				.errors(errors)
				.build();
	}
}
