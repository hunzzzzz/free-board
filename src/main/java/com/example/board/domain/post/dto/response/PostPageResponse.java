package com.example.board.domain.post.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostPageResponse {
	private int currentPage;
	
	private int startPage;
	
	private int lastPage;
	
	private int totalPages;
}
