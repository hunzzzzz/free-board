package com.example.board.domain.user.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.board.domain.user.dto.request.CheckEmailRequest;
import com.example.board.domain.user.dto.request.SignupRequest;
import com.example.board.domain.user.service.SignupService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class SignupController {
	private final SignupService signupService;

	@GetMapping("/signup")
	String showSignupForm() {
		return "signup";
	}

	@PostMapping("/api/signup")
	ResponseEntity<UUID> signup(@Valid @RequestBody SignupRequest request) {
		UUID userId = signupService.signup(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(userId);
	}

	@PostMapping("/api/signup/email/check")
	ResponseEntity<Object> checkEmail(@Valid @RequestBody CheckEmailRequest request) {
		signupService.checkEmailDuplication(request.getEmail());
		
		return ResponseEntity.ok().body(null);
	}
}
