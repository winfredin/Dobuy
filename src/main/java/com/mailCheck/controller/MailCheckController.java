package com.mailCheck.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mailCheck.EmailVerificationUtil;
import com.mailCheck.model.MailService;
import com.mailCheck.model.MailVO;
import com.member.model.MemberService;

@Controller
@RequestMapping("/email")
public class MailCheckController {

	@Autowired
	MailService mailSvc;

	@Autowired
	MemberService memberSvc;

	@PostMapping("/sent")
	public ResponseEntity<Map<String, String>> isVerified(@RequestBody Map<String, Object> requestBody) {
		// Step 1: 生成驗證碼
		String email = (String) requestBody.get("email");
		String verificationCode = EmailVerificationUtil.generateVerificationCode();

		// Step 2: 發送郵件

		// 確認信箱是否被註冊過
		if (memberSvc.findByMemEmail(email) != null) {
			Map<String, String> response = Map.of("success", "false", "message", "此信箱已被註冊");
			return ResponseEntity.ok(response);
		}

		mailSvc.sendHtmlEmail(email, verificationCode);
		MailVO mailVO = new MailVO(email, verificationCode, 0);
		mailSvc.insert(mailVO);

		Map<String, String> response = Map.of("success", "true", "message", "驗證碼已傳送");
		return ResponseEntity.ok(response);
	}

	@PostMapping("/verify")
	public ResponseEntity<Map<String, String>> verify(@RequestBody Map<String, Object> requestBody) {
		String code = (String) requestBody.get("code");
		String email = (String) requestBody.get("email");

		// 確認驗證碼是否正確
		if (code.equals(mailSvc.getTheNewest(email).getVerificationCode())) {
			MailVO mailVO = new MailVO(email, code, 1);
			mailVO.setId(mailSvc.getTheNewest(email).getId());
			mailSvc.updateStatus(mailVO);// 更新mail狀態 0 > 1
			mailSvc.deleteUnverified(email);// 刪除此mail的其他狀態為0的資料
			Map<String, String> response = Map.of("success", "true", "message", "驗證成功");
			return ResponseEntity.ok(response);
		} else {
			Map<String, String> response = Map.of("success", "false", "message", "驗證碼輸入錯誤，請稍後再試");
			return ResponseEntity.ok(response);
		}

	}

	@PostMapping("/forget")
	public ResponseEntity<Map<String, String>> forget(@RequestBody Map<String, Object> requestBody) {
		// Step 1: 生成驗證碼
		String email = (String) requestBody.get("email");
		String verificationCode = EmailVerificationUtil.generateVerificationCode();

		// Step 2: 發送郵件

		// 確認信箱是否有註冊過
		if (memberSvc.findByMemEmail(email) == null) {
			Map<String, String> response = Map.of("success", "false", "message", "此信箱未被註冊!");
			return ResponseEntity.ok(response);
		}

		mailSvc.sendEmail(email, verificationCode); // 發送Mail
		MailVO mailVO = new MailVO(email, verificationCode, 0); // 建VO
		mailSvc.insert(mailVO); // 插入
		Map<String, String> response = Map.of("success", "true", "message", "驗證碼已傳送");

		return ResponseEntity.ok(response);

	}
	
	@PostMapping("/verify/forget")
	public ResponseEntity<Map<String, String>> verifyforget(@RequestBody Map<String, Object> requestBody) {
		String code = (String) requestBody.get("code");
		String email = (String) requestBody.get("email");

		// 確認驗證碼是否正確
		if (code.equals(mailSvc.getTheNewest(email).getVerificationCode())) {
			MailVO mailVO = new MailVO(email, code, 1);
			mailVO.setId(mailSvc.getTheNewest(email).getId());
			mailSvc.updateStatus(mailVO);// 更新mail狀態 0 > 1
			mailSvc.deleteUnverified(email);// 刪除此mail的其他狀態為0的資料
			Map<String, String> response = Map.of("success", "true", "message", "驗證成功", "memAccount",
					memberSvc.findByMemEmail(email).getMemAccount());
			return ResponseEntity.ok(response);
		} else {
			Map<String, String> response = Map.of("success", "false", "message", "驗證碼輸入錯誤，請稍後再試");
			return ResponseEntity.ok(response);
		}

	}

}
