package com.example.board.domain.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.board.domain.user.dto.response.UserLoginResponse;
import com.example.board.domain.user.entity.User;

@Mapper
public interface UserMapper {
	/**
	 * 회원가입
	 */
	void signup(User user);
	
	int isUsingEmail(String email);
	
	/**
	 * 로그인
	 */
	UserLoginResponse getLoginInfo(String email);
}
