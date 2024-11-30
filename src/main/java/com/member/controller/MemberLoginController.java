package com.member.controller;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
	public String loginCheck(@Validated(LoginGroup.class) @ModelAttribute MemberVO memberVO, BindingResult result, Model model
			,HttpSession session, HttpServletRequest req) {
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
		
		
		// 登錄成功，設置 session
		session.setAttribute("memAccount", memberVO.getMemAccount());
		session.setAttribute("memNo", memberVO.getMemNo());
		 // 檢查是否有原始請求
	    String originalRequest = (String) session.getAttribute("originalRequest");
	    if (originalRequest != null) {
	        session.removeAttribute("originalRequest"); // 移除原始請求
	        return "redirect:" + originalRequest.replaceFirst(req.getContextPath(), "");
	    }
		
		
	    // 如果沒有原始請求，跳轉到默認頁面
		return "front-end/member/loginSuccess"; // 重定向到成功页面
	}
	
}
