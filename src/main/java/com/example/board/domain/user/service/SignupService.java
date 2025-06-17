package com.example.board.domain.user.service;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.board.domain.user.dto.request.SignupRequest;
import com.example.board.domain.user.entity.User;
import com.example.board.domain.user.mapper.UserMapper;
import com.example.board.global.exception.EmailDuplicateException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SignupService {
	private final PasswordEncoder passwordEncoder;
	private final UserMapper userMapper;

	@Transactional
	public UUID signup(SignupRequest request) {
		UUID userId = UUID.randomUUID();
		User user = request.toEntity(userId, passwordEncoder);

		userMapper.signup(user);

		return userId;
	}

	@Transactional
	public void checkEmailDuplication(String email) {
		int result = userMapper.isUsingEmail(email);

		if (result == 1)
			throw new EmailDuplicateException("이미 사용 중인 이메일입니다.");
	}
}
