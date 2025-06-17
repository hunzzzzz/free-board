package com.example.board.domain.user.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponse {
	private String atk;
	
	private String rtk;

	public TokenResponse(String atk, String rtk) {
		this.atk = atk;
		this.rtk = rtk;
	}
	
}
