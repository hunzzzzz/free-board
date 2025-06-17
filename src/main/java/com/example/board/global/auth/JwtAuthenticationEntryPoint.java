package com.example.board.global.auth;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.board.global.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
    		HttpServletRequest request, 
    		HttpServletResponse response,
    		AuthenticationException authException
	) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); 
        response.setContentType("application/json;charset=UTF-8"); 
        
        ErrorResponse error = ErrorResponse.of("접근 권한이 없습니다. 로그인이 필요합니다.");
        
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
