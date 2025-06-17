package com.example.board.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckEmailRequest {
	@NotBlank(message = "이메일은 필수 입력 항목입니다.")
	@Pattern(
			regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", 
			message = "올바른 이메일 형식이 아닙니다."
	)
	private String email;
}
