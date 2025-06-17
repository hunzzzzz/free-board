<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Map.Entry"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>회원가입</title>
<style>
body {
	font-family: 'Inter', sans-serif;
	background-color: #f3f4f6;
	display: flex;
	justify-content: center;
	align-items: center;
	min-height: 100vh;
	margin: 0;
	padding: 20px;
	box-sizing: border-box;
}

.container {
	background-color: #fff;
	padding: 40px;
	border-radius: 12px;
	box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
	width: 100%;
	max-width: 500px;
}

h2 {
	font-size: 2.75rem;
	font-weight: bold;
	text-align: center;
	color: #333;
	margin-bottom: 30px;
}

.form-group {
	margin-bottom: 20px;
}

label {
	display: block;
	font-size: 0.875rem;
	font-weight: 500;
	color: #555;
	margin-bottom: 8px;
}

.input-group {
	display: flex;
	gap: 8px;
	width: 100%;
	box-sizing: border-box;
}

input[type="email"], input[type="password"], input[type="text"] {
	width: 100%;
	padding: 12px;
	border: 1px solid #ccc;
	border-radius: 8px;
	font-size: 1rem;
	box-shadow: inset 0 1px 2px rgba(0, 0, 0, 0.05);
	box-sizing: border-box;
}

.input-group input[type="email"] {
	flex-grow: 1;
	width: auto;
}

.input-group button {
	flex-shrink: 0;
	width: 100px;
	padding: 12px 10px;
}

input:focus {
	outline: none;
	border-color: #3b82f6;
	box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.25);
}

button {
	background-color: #2563eb;
	color: #fff;
	font-weight: 600;
	border: none;
	border-radius: 8px;
	cursor: pointer;
	transition: background-color 0.3s ease;
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

button:hover {
	background-color: #1d4ed8;
}

button[type="submit"] {
	width: 100%;
	background-color: #4f46e5;
	padding: 12px 18px;
}

button[type="submit"]:hover {
	background-color: #4338ca;
}

.error-message {
	color: #ef4444;
	font-size: 0.875rem;
	margin-top: 5px;
}

.link-text {
	margin-top: 30px;
	text-align: center;
	font-size: 0.875rem;
	color: #666;
}

.link-text a {
	color: #4f46e5;
	text-decoration: none;
	font-weight: 500;
}

.link-text a:hover {
	text-decoration: underline;
}

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
<link rel="icon" href="/icons/favicon.ico" type="image/x-icon">
</head>
<body>
	<div class="container">
		<h2>회원가입</h2>

		<!-- 서버 메시지가 출력되는 공간 -->
		<div id="serverMessage" style="display: none;"></div>

		<!-- 회원가입 폼 -->
		<form id="signupForm" method="post">
			<!-- 이메일 -->
			<div class="form-group">
				<label for="email">이메일</label>
				<div class="input-group">
					<input type="email" id="email" name="email" required
						placeholder="이메일을 입력해주세요.">
					<button type="button" id="checkEmailBtn">중복확인</button>
				</div>
				<p id="emailError" class="error-message"></p>
			</div>

			<!-- 비밀번호 -->
			<div class="form-group">
				<label for="password">비밀번호</label> <input type="password"
					id="password" name="password" required
					placeholder="8~16자, 대문자, 소문자, 특수문자 포함">
				<p id="passwordError" class="error-message"></p>
			</div>

			<!-- 이름 -->
			<div class="form-group">
				<label for="name">이름</label> <input type="text" id="name"
					name="name" required placeholder="이름을 입력해주세요. (최대 10자)"
					maxlength="10">
				<p id="nameError" class="error-message"></p>
			</div>

			<!-- 회원가입 버튼 -->
			<button type="submit">회원가입</button>
		</form>

		<!-- 이미 계정이 있으신가요? -->
		<p class="link-text">
			이미 계정이 있으신가요?<a href="/login">로그인</a>
		</p>
	</div>

	<script>
	// 페이지 로드 시 수행되어야 할 작업들
	document.addEventListener('DOMContentLoaded', function() {
		var didEmailCheck = false;
		const signupForm = document.getElementById('signupForm');
		const emailInput = document.getElementById('email');
		const passwordInput = document.getElementById('password');
		const nameInput = document.getElementById('name');
		const checkEmailBtn = document.getElementById('checkEmailBtn');
		
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

		// ***** '중복확인' 버튼을 클릭한 경우 *****
		checkEmailBtn.addEventListener('click', async function() {
			// 에러 메시지 초기화
			clearErrors();
			clearServerMessage();

			const email = emailInput.value;
			const emailError = document.getElementById('emailError');

			if (!email) {
				emailError.textContent = '이메일을 입력해주세요.';
				emailInput.focus();
				return;
			}

			// API 요청 (/api/signup/email/check)
			try {
				const response = await fetch('/api/signup/email/check', {
					method: 'POST',
					headers: {
						'Content-Type': 'application/json'
					},
					body: JSON.stringify({ email: email })
				});

				// 200 OK
				if (response.ok) {
					didEmailCheck = true;
					displayServerMessage('사용 가능한 이메일입니다.', 'success');
				} else {
					const result = await response.json();
					
					// 이메일 중복 에러
					if (result.message) {
						displayServerMessage(result.message, 'error');
					} else {
						// 필드 에러
						displayServerMessage('이메일 중복 확인 중 오류가 발생했습니다.', 'error');
						if (result.errors) {
							displayFieldErrors(result.errors);
						}
					}
				}
			} catch (error) {
				displayServerMessage('이메일 중복 확인 중 네트워크 오류가 발생했습니다.', 'error');
			}
		});

		// ***** '회원가입' 버튼을 클릭한 경우 *****
		signupForm.addEventListener('submit', async function(event) {
			event.preventDefault();

			clearErrors();
			clearServerMessage();
			
			// 이메일 중복확인을 하지 않은 경우
			if (!didEmailCheck) {
				displayServerMessage('이메일 중복확인을 먼저 진행해주세요.', 'error');
				emailInput.focus();
				return;
			}

			const email = emailInput.value;
			const password = passwordInput.value;
			const name = nameInput.value;

			const payload = {
				email: email,
				password: password,
				name: name
			};

			// API 요청 (/api/signup)
			try {
				const response = await fetch('/api/signup', {
					method: 'POST',
					headers: {
						'Content-Type': 'application/json'
					},
					body: JSON.stringify(payload)
				});

				const result = await response.json();

				// 200 OK
				if (response.ok) {
					displayServerMessage('회원가입이 성공적으로 완료되었습니다!', 'success');
					setTimeout(() => {
						window.location.href = '/login'; // 로그인 페이지로 리다이렉트
					}, 1500);
				} else {
					displayServerMessage(result.message || '입력값에 오류가 발생했습니다.', 'error');
					
					if (response.status === 400 && result.errors) {
						displayFieldErrors(result.errors); // Field-specific error messages
					}
				}
			} catch (error) {
				console.error('회원가입 중 네트워크 오류:', error);
				displayServerMessage('네트워크 오류 또는 서버 응답 처리 중 문제가 발생했습니다.', 'error');
			}
		});
	});
    </script>
</body>
</html>
