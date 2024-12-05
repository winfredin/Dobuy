package com.member.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mem")
public class MemberLogoutController {

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		// 清除 session
		session.invalidate();
		// 重定向到登入頁面
		return "redirect:/mem/login";
	}
	
	
	
//	winfred===================================================================================以下	
	@GetMapping("/logout49")
	public String logout49(HttpSession session) {
		// 清除 session
		session.invalidate();
		// 重定向到登入頁面
		return "redirect:/mem/login49";
	}
//	winfred===================================================================================以上

}
