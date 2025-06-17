<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Map.Entry" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>로그인</title>
    <style>
        body {
            font-family: 'Inter', sans-serif;
            background-color: #f3f4f6; /* 회색 배경 */
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh; /* 전체 화면 높이 */
            margin: 0;
            padding: 20px; /* 전체적인 패딩 */
            box-sizing: border-box; /* 패딩 포함 크기 계산 */
        }

        .container {
            background-color: #fff;
            padding: 40px;
            border-radius: 12px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 420px; /* 컨테이너 최대 너비 */
        }

        h2 {
            font-size: 2.75rem; /* 44px - 텍스트 크기를 키움 */
            font-weight: bold;
            text-align: center; /* 가운데 정렬 */
            color: #333;
            margin-bottom: 30px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        label {
            display: block;
            font-size: 0.875rem; /* 14px */
            font-weight: 500;
            color: #555;
            margin-bottom: 8px;
        }

        input[type="email"],
        input[type="password"] {
            width: 100%;
            padding: 12px;
            border: 1px solid #ccc;
            border-radius: 8px;
            font-size: 1rem;
            box-shadow: inset 0 1px 2px rgba(0, 0, 0, 0.05);
            box-sizing: border-box;
        }

        input:focus {
            outline: none;
            border-color: #3b82f6; /* 파란색 포커스 */
            box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.25);
        }

        button[type="submit"] {
            width: 100%;
            background-color: #4f46e5; /* 보라색 */
            color: #fff;
            font-weight: 600;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            padding: 12px 18px;
            transition: background-color 0.3s ease;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        button[type="submit"]:hover {
            background-color: #4338ca; /* 더 진한 보라색 */
        }

        .error-message {
            color: #ef4444; /* 빨간색 */
            font-size: 0.875rem; /* 14px */
            margin-top: 5px;
        }

        .link-text {
            margin-top: 30px;
            text-align: center;
            font-size: 0.875rem; /* 14px */
            color: #666;
        }

        .link-text a {
            color: #4f46e5; /* 보라색 링크 */
            text-decoration: none;
            font-weight: 500;
        }

        .link-text a:hover {
            text-decoration: underline;
        }

        /* 서버 메시지 스타일 */
        #serverMessage {
            padding: 10px;
            margin-bottom: 15px;
            border-radius: 8px;
            text-align: center;
            font-weight: bold;
        }

        #serverMessage.success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        #serverMessage.error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>로그인</h2>

        <!-- 서버 메시지가 출력되는 공간 -->
        <div id="serverMessage" style="display: none;"></div>

		<!-- 로그인 폼 -->
        <form id="loginForm" method="post">
            <!-- 이메일 -->
            <div class="form-group">
                <label for="email">이메일</label>
                <input
                    type="email"
                    id="email"
                    name="email"
                    required
                    placeholder="이메일을 입력해주세요."
                >
                <p id="emailError" class="error-message"></p>
            </div>

            <!-- 비밀번호 -->
            <div class="form-group">
                <label for="password">비밀번호</label>
                <input
                    type="password"
                    id="password"
                    name="password"
                    required
                    placeholder="비밀번호를 입력해주세요."
                >
                <p id="passwordError" class="error-message"></p>
            </div>

            <!-- 로그인 버튼 -->
            <button type="submit">로그인</button>
        </form>

        <p class="link-text">
            아직 계정이 없으신가요? <a href="/signup">회원가입</a>
        </p>
    </div>

    <script>
		// 페이지 로드 시 수행되어야 할 작업들
        document.addEventListener('DOMContentLoaded', function() {
            const loginForm = document.getElementById('loginForm');
            const emailInput = document.getElementById('email');
            const passwordInput = document.getElementById('password');

    	    // ***** 에러 메시지 삭제를 위한 함수 *****
            function clearErrors() {
                document.querySelectorAll('.error-message').forEach(el => {
                    el.textContent = '';
                    el.style.display = 'none';
                });
            }

            function clearServerMessage() {
                const serverMessageDiv = document.getElementById('serverMessage');
                if (serverMessageDiv) {
                    serverMessageDiv.textContent = '';
                    serverMessageDiv.className = '';
                    serverMessageDiv.style.display = 'none';
                }
            }

    	    // ***** 에러 메시지 출력을 위한 함수 *****
            function displayFieldErrors(errors) {
                for (const fieldName in errors) {
                    const errorDiv = document.getElementById(fieldName + 'Error');
                    if (errorDiv) {
                        errorDiv.textContent = errors[fieldName];
                        errorDiv.style.display = 'block';
                    }
                }
            }

            function displayServerMessage(message, type) {
                const serverMessageDiv = document.getElementById('serverMessage');
                if (serverMessageDiv) {
                    serverMessageDiv.textContent = message;
                    serverMessageDiv.className = type;
                    serverMessageDiv.style.display = 'block';
                }
            }

    		// ***** 에러 메시지 초기화 *****
            clearErrors();
            clearServerMessage();

            // ***** 로그인 버튼을 클릭한 경우 ******
            loginForm.addEventListener('submit', async function(event) {
                event.preventDefault();

                clearErrors();
                clearServerMessage();

                const email = emailInput.value;
                const password = passwordInput.value;

                const payload = {
                    email: email,
                    password: password
                };

                // API 요청 (/api/login)
                try {
                    const response = await fetch('/api/login', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(payload)
                    });

                    const atk = await response.text();

                    // 200 OK
                    if (response.ok) {
                    	// ATK 저장
                        localStorage.setItem('accessToken', atk);

    					displayServerMessage('로그인이 완료되었습니다!', 'success');
    					
    					// 메인 페이지로 리다이렉트
                        setTimeout(() => {
                            window.location.href = '/'; 
                        }, 1500);
                    } else { // HTTP 4xx, 5xx
    					displayServerMessage(result.message || '입력값에 오류가 발생했습니다.', 'error');
                    
                        if (response.status === 400 && result.errors) {
                            displayFieldErrors(result.errors);
                        }
                    }
                } catch (error) {
                    console.error('로그인 중 네트워크 오류:', error);
                    displayServerMessage('네트워크 오류 또는 서버 응답 처리 중 문제가 발생했습니다.', 'error');
                }
            });
        });
    </script>
</body>
</html>
