package com.example.board.global.component;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.board.domain.user.dto.response.UserLoginResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {
	private SecretKey secretKey;
	private static final String JWT_ISSUER = "free_board";
	public static final Long ATK_EXPIRATION_TIME = 1000 * 60 * 30L; // 30분
	public static final Long RTK_EXPIRATION_TIME = 1000 * 60 * 60 * 24L; // 24시간

	public JwtProvider(@Value("${jwt.secret.key}") String secretKey) {
		this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
	}

	private String createToken(UserLoginResponse user, Long expirationMillis) {
		// Claims 생성
		Claims claims = Jwts.claims().add("roles", List.of(user.getRole())).add("email", user.getEmail()).build();

		// JWT 생성
		String jwt = Jwts.builder()
				.subject(user.getUserId().toString())
				.claims(claims)
				.expiration(new Date(System.currentTimeMillis() + expirationMillis))
				.issuedAt(new Date())
				.issuer(JWT_ISSUER)
				.signWith(secretKey)
				.compact();

		return jwt;
	}

	public String createAtk(UserLoginResponse user) {
		return "Bearer " + createToken(user, ATK_EXPIRATION_TIME);
	}

	public String createRtk(UserLoginResponse user) {
		return createToken(user, RTK_EXPIRATION_TIME);
	}

	public String substringToken(String authorizationHeaderValue) {
		return authorizationHeaderValue.substring("Bearer ".length());
	}

	public Claims getUserInfoFromToken(String token) {
		return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
	}
}
