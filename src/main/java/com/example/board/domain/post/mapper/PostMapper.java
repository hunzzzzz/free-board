package com.example.board.domain.post.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.board.domain.post.dto.response.PostResponse;
import com.example.board.domain.post.entity.SortCondition;

@Mapper
public interface PostMapper {
	/*
	 * 게시글 전체 조회
	 */
	List<PostResponse> getAll(int pageSize, int offset, String keyword, SortCondition sort);
	
	int countAllPosts(String keyword);

}
