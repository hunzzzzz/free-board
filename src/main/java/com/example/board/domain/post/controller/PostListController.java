package com.example.board.domain.post.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.board.domain.post.dto.response.PostPageResponse;
import com.example.board.domain.post.dto.response.PostResponse;
import com.example.board.domain.post.entity.SortCondition;
import com.example.board.domain.post.service.PostListService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PostListController {
	private final PostListService postListService;

	@GetMapping({ "/", "/api/posts" })
	String getAll(Model model, @RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false) String keyword,
			@RequestParam(required = false, defaultValue = "LATEST") String sort) {
		// 게시글 정보 조회
		List<PostResponse> posts = postListService.getAll(page, keyword, SortCondition.valueOf(sort));
		
		// 페이지 정보 조회
		PostPageResponse pageResponse = postListService.getPageInfo(page, keyword);

		model.addAttribute("posts", posts);
		model.addAttribute("page", pageResponse);
		if (keyword != null)
			model.addAttribute("keyword", keyword);

		return "posts";
	}
}
