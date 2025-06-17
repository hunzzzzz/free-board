package com.example.board.domain.post.entity;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Post {
	private long postId;
	private PostStatus status;
	private String title;
	private String content;
	private String author;
	private int viewCount;
	private int likeCount;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public Post(String title, String content, String author) {
		this.title = title;
		this.content = content;
		this.author = author;
	}

}
