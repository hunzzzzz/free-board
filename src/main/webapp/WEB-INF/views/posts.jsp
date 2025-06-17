<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시글 목록</title>
    <style>
        body {
            font-family: 'Inter', sans-serif;
            background-color: #f3f4f6;
            display: flex;
            justify-content: center;
            padding: 20px;
            box-sizing: border-box;
            min-height: 100vh;
        }

        .container {
            background-color: #fff;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 1200px; /* 게시글 목록 컨테이너 너비를 1200px로 유지 */
        }

        h1 {
            font-size: 2.5rem;
            font-weight: bold;
            text-align: center;
            color: #333;
            margin-bottom: 30px;
        }

        .header-actions {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            flex-wrap: wrap; /* 반응형을 위해 줄바꿈 허용 */
            gap: 15px; /* 요소들 간의 간격 */
        }

        .search-form {
            display: flex;
            gap: 8px;
            flex-grow: 1; /* 검색 폼이 공간을 차지하도록 */
        }

        .search-form input[type="text"] {
            flex-grow: 1;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 8px;
            font-size: 1rem;
            box-sizing: border-box;
        }

        .search-form button, .action-button {
            padding: 10px 15px;
            background-color: #4f46e5;
            color: #fff;
            font-weight: 600;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        .search-form button:hover, .action-button:hover {
            background-color: #4338ca;
        }
        
        .sort-select {
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 8px;
            font-size: 1rem;
            background-color: #fff;
            cursor: pointer;
            box-sizing: border-box;
        }

        .post-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        .post-table th, .post-table td {
            border: 1px solid #e0e0e0;
            padding: 12px 15px;
            text-align: left; /* 기본 왼쪽 정렬 */
        }

        /* 가운데 정렬이 필요한 컬럼 */
        .post-table th:nth-child(1), /* 번호 */
        .post-table th:nth-child(3), /* 작성자 */
        .post-table th:nth-child(6) { /* 작성일 */
            text-align: center;
        }
        .post-table td:nth-child(1), /* 번호 */
        .post-table td:nth-child(3), /* 작성자 */
        .post-table td:nth-child(6) { /* 작성일 */
            text-align: center;
        }

        .post-table th {
            background-color: #f8f8f8;
            font-weight: bold;
            color: #555;
            white-space: nowrap; /* 제목 줄바꿈 방지 */
        }

        .post-table tr:nth-child(even) {
            background-color: #fcfcfc;
        }

        .post-table tr:hover {
            background-color: #f0f0f0;
        }

        .post-table td a {
            color: #2563eb;
            text-decoration: none;
            font-weight: 500;
            transition: color 0.2s ease;
        }

        .post-table td a:hover {
            color: #1d4ed8;
            text-decoration: underline;
        }
        
        .post-table .view-count, .post-table .like-count {
            text-align: center;
        }

        .pagination {
            display: flex;
            justify-content: center;
            align-items: center;
            margin-top: 30px;
            gap: 10px;
            flex-wrap: wrap;
        }

        .pagination a, .pagination span {
            padding: 8px 12px;
            border: 1px solid #ddd;
            border-radius: 6px;
            text-decoration: none;
            color: #555;
            background-color: #fff;
            transition: all 0.2s ease;
            white-space: nowrap;
        }

        .pagination a:hover {
            background-color: #e0e0e0;
            border-color: #ccc;
            color: #333;
        }

        .pagination .current-page {
            background-color: #4f46e5;
            color: white;
            border-color: #4f46e5;
            font-weight: bold;
            cursor: default;
        }

        .pagination .disabled {
            background-color: #f0f0f0;
            color: #aaa;
            cursor: not-allowed;
        }
    </style>
    <link rel="icon" href="/icons/favicon.ico" type="image/x-icon">
</head>
<body>
    <div class="container">
        <h1>게시글 목록</h1>

        <div class="header-actions">
        	<!-- 검색창 -->
            <form id="searchForm" class="search-form" action="/api/posts" method="GET">
                <input type="text" id="keyword" name="keyword" placeholder="제목 검색" value="${keyword ne null ? keyword : ''}">
                <button type="submit">검색</button>
            </form>

			<!-- 정렬 박스 -->
            <select id="sortSelect" class="sort-select">
                <option value="LATEST" <c:if test="${param.sort eq 'LATEST' or param.sort eq null}">selected</c:if>>최신순</option>
                <option value="VIEW_COUNT" <c:if test="${param.sort eq 'VIEW_COUNT'}">selected</c:if>>조회수순</option>
                <option value="LIKE_COUNT" <c:if test="${param.sort eq 'LIKE_COUNT'}">selected</c:if>>추천순</option>
            </select>

			<!-- '새 글 작성' 버튼 -->
            <a href="/posts/new" class="action-button">새 글 작성</a>
        </div>

		<!-- 게시판 -->
        <table class="post-table">
            <thead>
                <tr>
                    <th>번호</th>
                    <th>제목</th>
                    <th>작성자</th>
                    <th class="view-count">조회수</th>
                    <th class="like-count">추천수</th>
                    <th>작성일</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty posts}">
                        <c:forEach var="post" items="${posts}">
                            <tr>
                                <td><c:out value="${post.postId}" /></td>
                                <td>
                                    <a href="/posts/${post.postId}"><c:out value="${post.title}" /></a>
                                </td>
                                <td><c:out value="${post.author}" /></td>
                                <td class="view-count"><c:out value="${post.viewCount}" /></td>
                                <td class="like-count"><c:out value="${post.likeCount}" /></td>
                                <td><c:out value="${post.formattedCreatedAt}" /></td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="6" style="text-align: center; padding: 30px;">게시글이 없습니다.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>

        <div class="pagination">
        	<!-- '이전' 버튼을 클릭했을 때 발생하는 URL -->
            <c:url var="prevPageUrl" value="/api/posts">
                <c:param name="page" value="${page.currentPage - 1}"/>
                <c:param name="keyword" value="${keyword}"/>
                <c:param name="sort" value="${param.sort}"/>
            </c:url>
            <!-- '다음' 버튼을 클릭했을 때 발생하는 URL -->
            <c:url var="nextPageUrl" value="/api/posts">
                <c:param name="page" value="${page.currentPage + 1}"/>
                <c:param name="keyword" value="${keyword}"/>
                <c:param name="sort" value="${param.sort}"/>
            </c:url>

            <!-- '이전' 버튼 출력 조건 -->
            <c:if test="${page.currentPage > 1}">
                <a href="${prevPageUrl}">이전</a>
            </c:if>
            <c:if test="${page.currentPage <= 1}">
                <span class="disabled">이전</span>
            </c:if>

            <!-- 페이지네이션 구현 -->
            <c:forEach var="i" begin="${page.startPage}" end="${page.lastPage}">
                <!-- 해당 페이지를 클릭했을 때 발생하는 URL -->
                <c:url var="pageUrl" value="/api/posts">
                    <c:param name="page" value="${i}"/>
                    <c:param name="keyword" value="${keyword}"/>
                    <c:param name="sort" value="${param.sort}"/>
                </c:url>
                <!-- a 태그 -->
                <a href="${pageUrl}" class="<c:if test="${i eq page.currentPage}">current-page</c:if>">
                    <c:out value="${i}"/>
                </a>
            </c:forEach>

			<!-- '다음' 버튼 출력 조건 -->
            <c:if test="${page.currentPage < page.totalPages}">
                <a href="${nextPageUrl}">다음</a>
            </c:if>
            <c:if test="${page.currentPage >= page.totalPages}">
                <span class="disabled">다음</span>
            </c:if>
        </div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const searchForm = document.getElementById('searchForm');
            const keywordInput = document.getElementById('keyword');
            const sortSelect = document.getElementById('sortSelect');

            // 정렬 방식 변경 시 폼 제출
            sortSelect.addEventListener('change', function() {
                const currentKeyword = keywordInput.value;
                const selectedSort = this.value;
                // 현재 페이지는 1로 초기화 (정렬 기준이 바뀌면 보통 첫 페이지로 이동)
                window.location.href = '/api/posts?page=1&keyword=' + encodeURIComponent(currentKeyword) + '&sort=' + encodeURIComponent(selectedSort);
            });

            // 검색 폼 제출 시
            searchForm.addEventListener('submit', function(event) {
                event.preventDefault(); // 기본 제출 방지

                const currentKeyword = keywordInput.value;
                const currentSort = sortSelect.value;
                // 검색 시 현재 페이지는 1로 초기화
                window.location.href = '/api/posts?page=1&keyword=' + encodeURIComponent(currentKeyword) + '&sort=' + encodeURIComponent(currentSort);
            });

            // 페이지네이션 링크 클릭 시
            document.querySelectorAll('.pagination a').forEach(link => {
                link.addEventListener('click', function(event) {
                    // C:url 태그가 이미 올바른 URL을 생성하므로 기본 동작만 허용
                    // 여기서는 추가적인 JS 로직이 필요하지 않음
                });
            });
        });
    </script>
</body>
</html>
