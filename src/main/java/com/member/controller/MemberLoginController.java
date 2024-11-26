package com.member.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.member.model.MemberService;
import com.member.model.MemberVO;
import com.member.model.MemberVO.LoginGroup;

@Controller
@RequestMapping("/mem")
public class MemberLoginController {
	@Autowired
	MemberService memberSvc;
	
	@PostMapping("/loginCheck")
	public String loginCheck(@Validated(LoginGroup.class) @ModelAttribute MemberVO memberVO, BindingResult result, Model model) {
		List<String> errorMsgs = new LinkedList<String>();
		if (result.hasErrors()) {
			return "front-end/member/login";
		}
		
		if (!memberSvc.isAccountExists(memberVO.getMemAccount())) {
			errorMsgs.add("帳號不存在");
			model.addAttribute("errorMsgs", errorMsgs);
			return "front-end/member/login";
		}
		
		if(!memberSvc.validateLogin(memberVO.getMemAccount(), memberVO.getMemPassword())) {
			errorMsgs.add("密碼錯誤");
			model.addAttribute("errorMsgs", errorMsgs);
			return "front-end/member/login";
		}
		return "front-end/member/loginSuccess"; // 重定向到成功页面
	}
	
}
