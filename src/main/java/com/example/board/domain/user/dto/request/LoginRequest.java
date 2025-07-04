package com.example.board.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
	@NotBlank(message = "이메일은 필수 입력 항목입니다.")
	private String email;

	@NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
	private String password;
}
