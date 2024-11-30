package com.member.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.member.model.MemberService;
import com.member.model.MemberVO;
import com.member.model.MemberVO.RegisterGroup;

@Controller
@RequestMapping("/mem")
public class MemberCRUDController {
	@Autowired
	MemberService memberSvc;

	@GetMapping("/register")
	public String showRegisterPage(Model model) {
		model.addAttribute("memberVO", new MemberVO());
		return "front-end/member/register";
	}

	@GetMapping("/login")
	public String showLoginPage(Model model) {
		model.addAttribute("memberVO", new MemberVO());
		return "front-end/member/login"; // 指向 Thymeleaf 模板路径
	}

	@PostMapping("/insert")
	public String insert(@Validated(RegisterGroup.class) @ModelAttribute MemberVO memberVO, BindingResult result,
			Model model) {
		List<String> errorMsgs = new LinkedList<String>();

		if (result.hasErrors()) {
			model.addAttribute("memberVO", memberVO);
			return "front-end/member/register"; // 返回注册页面，并显示错误信息
		}

		if (memberSvc.isAccountExists(memberVO.getMemAccount())) {

			errorMsgs.add("帳號已存在");
			memberVO.setMemAccount(null);
			
			if (!memberVO.isPasswordConfirmed()) {
				errorMsgs.add("密碼不一致");
			}
			
			model.addAttribute("errorMsgs", errorMsgs);
			model.addAttribute("memberVO", memberVO);
			return "front-end/member/register";
		}

		if (!memberVO.isPasswordConfirmed()) {
			errorMsgs.add("密碼不一致");
			model.addAttribute("errorMsgs", errorMsgs);
			model.addAttribute("memberVO", memberVO);
			return "front-end/member/register";
		}

		// 保存会员信息
		System.out.println("成功");
		memberSvc.addMem(memberVO);
		return "front-end/member/registerSuccess"; // 重定向到成功页面
	}
}
