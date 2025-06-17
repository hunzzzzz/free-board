package com.example.board.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.board.domain.user.dto.request.LoginRequest;
import com.example.board.domain.user.dto.response.TokenResponse;
import com.example.board.domain.user.service.LoginService;
import com.example.board.global.component.JwtProvider;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {
	private final LoginService loginService;
	
	@GetMapping("/login")
	String showLoginForm() {
		return "login";
	}

	@PostMapping("/api/login")
	ResponseEntity<String> login(
			@Valid @RequestBody LoginRequest request,
			HttpServletResponse response
	) {
		TokenResponse tokens = loginService.login(request);
		
		// 리프레쉬 토큰을 Http-Only 쿠키에 담는다.
		Cookie rtkCookie = new Cookie("refreshToken", tokens.getRtk());
		rtkCookie.setHttpOnly(true);    // 자바스크립트에서 접근 불가능하게 하여 XSS 공격을 방어할 수 있다.
		rtkCookie.setPath("/");
		rtkCookie.setMaxAge((int) (JwtProvider.RTK_EXPIRATION_TIME / 1000));
		// TODO: 추후 운영 환경에서는 아래 속성을 추가한다.
		// rtkCookie.setSecure(true);
		// rtkCookie.setAttribute("SameSite", "Lax");
		response.addCookie(rtkCookie);
		
		return ResponseEntity.ok().body(tokens.getAtk());
	}
}
