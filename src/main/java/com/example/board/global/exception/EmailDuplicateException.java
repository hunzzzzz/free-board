package com.example.board.global.exception;

public class EmailDuplicateException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public EmailDuplicateException(String message) {
		super(message);
	}

}
