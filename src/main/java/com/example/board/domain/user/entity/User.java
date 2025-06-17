package com.example.board.domain.user.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
	private UUID userId;
	
	private UserRole role;
	
	private String email;
	
	private String password;
	
	private String name;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;

	public User(UUID userId, String email, String password, String name) {
		this.userId = userId;
		this.email = email;
		this.password = password;
		this.name = name;
	}
	
}
