package com.example.board.global.component;

import org.springframework.stereotype.Component;

@Component
public class PageHandler {
	/**
	 * 현재 페이지 번호와 페이지당 항목 수를 기반으로 데이터베이스 쿼리에 필요한 오프셋을 계산하는 메서드 (※ 오프셋 = 건너뛸 레코드의 수)
	 *
	 * @param currentPage 현재 페이지 번호 (1부터 시작)
	 * @param pageSize    한 페이지에 표시될 항목의 수
	 * @return 데이터베이스 쿼리에 사용할 오프셋 값
	 */
	public int calculateOffset(int currentPage, int pageSize) {
		return (currentPage - 1) * pageSize;
	}

	/**
	 * 현재 페이지 번호와 페이지당 항목 수를 기반으로 현재 페이지 블록의 시작 페이지 번호를 계산하는 메서드
	 *
	 * @param currentPage 현재 페이지 번호 (1부터 시작)
	 * @param pageSize    한 페이지에 표시될 항목의 수
	 * @return 현재 페이지 블록의 시작 페이지 번호
	 */
	public int calculateStartPage(int currentPage, int pageSize) {
		return ((currentPage - 1) / pageSize) * pageSize + 1;
	}
	
	/**
	 * 시작 페이지 번호와 페이지당 항목 수를 기반으로 현재 페이지 블록의 마지막 페이지 번호를 계산하는 메서드
	 *
	 * @param startPage 현재 페이지 블록의 시작 페이지 번호
	 * @param pageSize  한 페이지에 표시될 항목의 수
	 * @return 현재 페이지 블록의 마지막 페이지 번호
	 */
	public int calculateLastPage(int startPage, int pageSize) {
		return startPage + pageSize - 1;
	}

	/**
	 * 전체 항목 수와 페이지당 항목 수를 기반으로 총 페이지 수를 계산하는 메서드
	 *
	 * @param total    전체 항목의 수
	 * @param pageSize 한 페이지에 표시될 항목의 수
	 * @return 계산된 전체 페이지 수
	 */
	public int calculateTotalPages(int total, int pageSize) {
		return (int) Math.ceil((double) total / pageSize);
	}
}
