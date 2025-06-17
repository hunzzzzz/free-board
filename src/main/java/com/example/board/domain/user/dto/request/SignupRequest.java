package com.example.board.domain.user.dto.request;

import java.util.UUID;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.board.domain.user.entity.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
	@NotBlank(message = "이메일은 필수 입력 항목입니다.")
	@Pattern(
			regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",
			message = "올바른 이메일 형식이 아닙니다."
	)
	private String email;
	
	@NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
	@Pattern(
			regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,16}$", 
			message="비밀번호는 8~16자이며, 대문자, 소문자, 특수문자를 각각 1개 이상 포함해야 합니다."
	)
	private String password;
	
	@NotBlank(message = "이름은 필수 입력 항목입니다.")
	@Length(max = 10)
	private String name;
	
	public User toEntity(UUID userId, PasswordEncoder passwordEncoder) {
		String encodedPassword = passwordEncoder.encode(password);
		
		return new User(userId, email, encodedPassword, name);
	}
}
