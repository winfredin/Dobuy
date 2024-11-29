document.addEventListener("DOMContentLoaded", function() {
    // 获取输入字段
    const memAccount = document.getElementById("memAccount");
    const memPassword = document.getElementById("memPassword");
    const confirmPassword = document.getElementById("confirmPassword");

    // 添加事件监听器，直接阻止非法字符输入
    function restrictInput(event) {
        // 允许的键：英文字符（大小写）和数字
        const allowedKeys = /^[a-zA-Z0-9]$/;

        // 获取当前按下的键
        const key = event.key;

        // 如果按键不匹配正则，则阻止默认行为
        if (!allowedKeys.test(key)) {
            event.preventDefault();
        }
    }

    // 绑定事件到输入框
    memAccount.addEventListener("keypress", restrictInput);
    memPassword.addEventListener("keypress", restrictInput);
    confirmPassword.addEventListener("keypress", restrictInput);
});
