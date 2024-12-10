package com.member.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.member.model.MemberService;
import com.member.model.MemberVO;


@Controller
@RequestMapping("/mem")
public class MemForgetController {
	
	@Autowired
	MemberService memberSvc;
	
	@GetMapping("/toForgetPassword")
	public String showRegisterPage(Model model) {
		model.addAttribute("memberVO", new MemberVO());
		return "front-end/member/forgetPassword";
	}
	
	
	@PostMapping("/updatePassword")
	public ResponseEntity<Map<String, String>> showLoginPage(@RequestBody Map<String, Object> requestBody) {
		String memEmail = (String)requestBody.get("memEmail");
		String memPassword = (String)requestBody.get("memPassword");
		
		MemberVO memberVO = memberSvc.findByMemEmail(memEmail);
		if(memberVO != null) {
			memberVO.setMemPassword(memPassword);
			memberSvc.updateMem(memberVO);
			Map<String, String> response = Map.of("success", "true", "message", "密碼更新成功，導向登入頁面");
			return ResponseEntity.ok(response);
		}
		
		Map<String, String> response = Map.of("success", "false", "message", "密碼更新失敗，請稍後再試");
		return ResponseEntity.ok(response);
		
	}
}
