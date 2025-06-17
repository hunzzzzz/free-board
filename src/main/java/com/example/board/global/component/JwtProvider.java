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
	public static final Long ATK_EXPIRATION_TIME = 1000 * 60 * 30L;         // 30분
	public static final Long RTK_EXPIRATION_TIME = 1000 * 60 * 60 * 24L;    // 24시간

	/**
	 * 생성자
	 * application.yml 로부터 JWT 서명에 사용할 Secret Key를 주입받아 초기화
	 *
	 * @param secretKey Base64 인코딩된 JWT Secret Key 문자열
	 */
	public JwtProvider(@Value("${jwt.secret.key}") String secretKey) {
		this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
	}

	/**
	 * JWT 토큰을 생성하는 내부 메서드
	 *
	 * @param user             토큰에 포함될 사용자 로그인 응답 정보
	 * @param expirationMillis 토큰의 만료 시간
	 * @return 생성된 JWT 문자열 (Bearer 접두사 없음)
	 */
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

	/**
	 * Access Token(ATK)을 생성하는 메서드
	 *
	 * @param user ATK에 포함될 사용자 로그인 응답 정보
	 * @return "Bearer " 접두사가 붙은 Access Token 문자열
	 */
	public String createAtk(UserLoginResponse user) {
		return "Bearer " + createToken(user, ATK_EXPIRATION_TIME);
	}

	/**
	 * Refresh Token(RTK)을 생성하는 메서드
	 *
	 * @param user RTK에 포함될 사용자 로그인 응답 정보
	 * @return Refresh Token 문자열 (Bearer 접두사 없음)
	 */
	public String createRtk(UserLoginResponse user) {
		return createToken(user, RTK_EXPIRATION_TIME);
	}

	/**
	 * Authorization 헤더 값에서 "Bearer " 접두사를 제거하고 순수한 토큰 값만 반환하는 메서드
	 *
	 * @param authorizationHeaderValue "Bearer [토큰]" 형식의 Authorization 헤더 값
	 * @return "Bearer " 접두사가 제거된 순수한 토큰 문자열
	 */
	public String substringToken(String authorizationHeaderValue) {
		return authorizationHeaderValue.substring("Bearer ".length());
	}

	/**
	 * 주어진 JWT 토큰을 검증하고, 토큰 내부에 포함된 클레임(사용자 정보)을 추출하는 메서드
	 *
	 * @param 정보를 추출할 JWT 문자열
	 * @return 토큰에 포함된 클레임(Claims) 객체
	 * @throws io.jsonwebtoken.ExpiredJwtException 토큰이 만료되었을 경우
	 * @throws io.jsonwebtoken.security.SignatureException 토큰 서명이 유효하지 않을 경우
	 * @throws io.jsonwebtoken.MalformedJwtException 토큰 형식이 잘못되었을 경우
	 */
	public Claims getUserInfoFromToken(String token) {
		return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
	}
}
