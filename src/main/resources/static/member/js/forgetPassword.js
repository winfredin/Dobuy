
//發送驗證碼
function sendVerificationCode() {
	const email = document.getElementById('memEmail').value.trim();
	const firstButton = document.getElementById('firstButton');
	
	if (!email) {
		alert('請輸入信箱');
		return;
	}

	fetch('/email/forget', {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify({ email })
	})
		.then(response => response.json())
		.then(data => {
			if (data.success === "true") {
				alert(data.message); // 顯示返回的成功消息

				// 顯示驗證碼輸入框
				document.getElementById('verification-section').style.display = 'block';
				disableButtonWithCountdown(firstButton, 2 * 5);
			} else if (data.success === "false") {
				alert(data.message);
			} else {
				alert('發送失敗，請稍後再試！');
			}

		})
		.catch(error => {
			console.error('錯誤:', error);
			alert('發送失敗，請稍後再試！');
		});

}


// 禁用按鈕並倒數
function disableButtonWithCountdown(button, seconds) {
	button.disabled = true; // 禁用按鈕
	let remainingTime = seconds;

	// 設置按鈕文字為剩餘時間
	const intervalId = setInterval(() => {
		remainingTime--;
		button.textContent = `請稍等 ${remainingTime} 秒`;

		if (remainingTime <= 0) {
			clearInterval(intervalId); // 清除計時器
			button.disabled = false; // 啟用按鈕
			button.textContent = '發送驗證碼'; // 恢復原文字
		}
	}, 1000); // 每秒更新一次
}


// 驗證驗證碼
function verifyCode() {
	const verificationCode = document.getElementById('memEmailConfirm').value.trim();
	const email = document.getElementById('memEmail').value.trim();

	if (!verificationCode) {
		alert('請輸入驗證碼');
		return;
	}

	// 模擬驗證邏輯
	fetch('/email/verify/forget', {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify({
			code: verificationCode,
			email: email
		})
	})
		.then(response => response.json())
		.then(data => {

			if (data.success === "true") {
				alert(data.message);

				document.getElementById('verification-section').style.display = 'none';
				document.querySelectorAll('.form-group').forEach(element => {
					element.style.display = 'block'; // 显示这些元素
				});
				document.querySelector('.form-button').style.display = 'block';

				document.getElementById("memAccount").value = data.memAccount;

				document.getElementById("memAccount").setAttribute('readonly', true);
				//輸入文字框readonly
				document.getElementById('memEmail').setAttribute('readonly', true);
				//鎖發送驗證碼按紐
				document.getElementById('firstButton').setAttribute('disabled', true);
			} else if (data.success === "false") {
				alert(data.message);
			}
		})
		.catch(error => {
			console.error('錯誤:', error);
			alert('發生錯誤，請稍後再試！');
		});
}


document.querySelector('.form-button').addEventListener('click', function (event) {
	event.preventDefault(); // 防止表單默認提交行為
	// 收集需要提交的数据
	const memEmail = document.getElementById('memEmail').value.trim();
	const memPassword = document.getElementById('memPassword').value.trim();
	const confirmMemPassword = document.getElementById('confirmMemPassword').value.trim();

	// 校验数据是否为空
	if (!memAccount || !memPassword || !confirmMemPassword) {
		alert('所有字段都是必填的');
		return;
	}

	// 校验密码是否一致
	if (memPassword !== confirmMemPassword) {
		alert('密碼輸入不一致');
		return;
	}

	// 发送 fetch 请求
	fetch('/mem/updatePassword', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({
			memPassword: memPassword,
			memEmail : memEmail
		})
	})
		.then(response => response.json()) // 解析 JSON 响应
		.then(data => {
			if (data.success === "true") {
				alert(data.message); 
				// 根据需要更新页面或跳转
				window.location.href = '/mem/login'; // 示例跳转
			} else {
				alert(data.message); 
			}
		})
		.catch(error => {
			console.error('發生錯誤:', error);
			alert('更新失敗，請稍後再試');
		});
})

// 切换密码可见性
document.addEventListener("DOMContentLoaded", () => {
    // 切换会员密码输入框
    const toggleMemPassword = document.getElementById("toggleMemPassword");
    const memPasswordInput = document.getElementById("memPassword");
    toggleMemPassword.addEventListener("click", () => {
        const icon = toggleMemPassword.querySelector("i");
        if (memPasswordInput.type === "password") {
            memPasswordInput.type = "text";
            icon.classList.remove("fa-eye");
            icon.classList.add("fa-eye-slash");
        } else {
            memPasswordInput.type = "password";
            icon.classList.remove("fa-eye-slash");
            icon.classList.add("fa-eye");
        }
    });

    // 切换确认密码输入框
    const toggleConfirmPassword = document.getElementById("toggleConfirmPassword");
    const confirmPasswordInput = document.getElementById("confirmMemPassword");
    toggleConfirmPassword.addEventListener("click", () => {
        const icon = toggleConfirmPassword.querySelector("i");
        if (confirmPasswordInput.type === "password") {
            confirmPasswordInput.type = "text";
            icon.classList.remove("fa-eye");
            icon.classList.add("fa-eye-slash");
        } else {
            confirmPasswordInput.type = "password";
            icon.classList.remove("fa-eye-slash");
            icon.classList.add("fa-eye");
        }
    });
});
