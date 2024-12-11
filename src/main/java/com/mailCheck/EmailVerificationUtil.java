package com.mailCheck;

import java.util.UUID;

public class EmailVerificationUtil {

    // 生成驗證碼的方法
    public static String generateVerificationCode() {
    	 return UUID.randomUUID().toString().substring(0, 6); // 生成6位驗證碼
    }
}
