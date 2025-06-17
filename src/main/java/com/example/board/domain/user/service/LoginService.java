package com.example.board.domain.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.board.domain.user.dto.request.LoginRequest;
import com.example.board.domain.user.dto.response.TokenResponse;
import com.example.board.domain.user.dto.response.UserLoginResponse;
import com.example.board.domain.user.mapper.UserMapper;
import com.example.board.global.component.JwtProvider;
import com.example.board.global.exception.LoginException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LoginService {
	private final JwtProvider jwtProvider;
	private final PasswordEncoder passwordEncoder;
	private final UserMapper userMapper;
	
	private TokenResponse createTokens(UserLoginResponse loginInfo) {
		String atk = jwtProvider.createAtk(loginInfo);
		String rtk = jwtProvider.createRtk(loginInfo);
		
		return new TokenResponse(atk, rtk);
	}

	@Transactional(readOnly = true)
	public TokenResponse login(LoginRequest request) {
		// 사용자 입력 정보 검증
		UserLoginResponse loginInfo = userMapper.getLoginInfo(request.getEmail());

		if (loginInfo == null || !passwordEncoder.matches(request.getPassword(), loginInfo.getPassword()))
			throw new LoginException("이메일 혹은 패스워드가 일치하지 않습니다.");
		
		// 토큰 생성
		return createTokens(loginInfo);
	}
}
