package com.example.board.domain.post.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.board.domain.post.dto.response.PostPageResponse;
import com.example.board.domain.post.dto.response.PostResponse;
import com.example.board.domain.post.entity.SortCondition;
import com.example.board.domain.post.mapper.PostMapper;
import com.example.board.global.component.PageHandler;
import com.example.board.global.component.TimeFormatter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostListService {
	private final PageHandler pageHandler;
	private final PostMapper postMapper;
	private final TimeFormatter timeFormatter;

	public static final int POST_PAGE_SIZE = 10;

	private void formatPostTimes(List<PostResponse> posts) {
		posts.forEach(post -> post.setFormattedCreatedAt(timeFormatter.formatTime(post.getCreatedAt())));
	}

	@Transactional(readOnly = true)
	public List<PostResponse> getAll(int page, String keyword, SortCondition sort) {
		// 게시글 목록 조회
		int offset = pageHandler.calculateOffset(page, POST_PAGE_SIZE);
		List<PostResponse> posts = postMapper.getAll(POST_PAGE_SIZE, offset, keyword, sort);

		// 시간 포매팅
		formatPostTimes(posts);

		return posts;
	}

	@Transactional(readOnly = true)
	public PostPageResponse getPageInfo(int page, String keyword) {
		int totalPosts = postMapper.countAllPosts(keyword);
		int startPage = pageHandler.calculateStartPage(page, POST_PAGE_SIZE);
		int lastPage = pageHandler.calculateLastPage(startPage, POST_PAGE_SIZE);
		int totalPages = pageHandler.calculateTotalPages(totalPosts, POST_PAGE_SIZE);

		return PostPageResponse.builder()
				.currentPage(page)
				.startPage(startPage)
				.lastPage(lastPage)
				.totalPages(totalPages)
				.build();
	}
}
