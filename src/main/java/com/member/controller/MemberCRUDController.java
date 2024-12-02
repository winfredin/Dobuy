package com.member.controller;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

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
import com.memcoupon.model.MemCouponService;
import com.memcoupon.model.MemCouponVO;

@Controller
@RequestMapping("/mem")
public class MemberCRUDController {
	@Autowired
	MemberService memberSvc;
	
//	winfred
    @Autowired
    private MemCouponService memCouponSvc;
	
	
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
//	winfred===================================================================================以下	
    // 新的方法顯示會員優惠券列表
    @GetMapping("/listMemCoupons") //1130
    public String listMemCoupons(HttpSession session, Model model) {
        // 從 session 獲取會員帳號
        String memAccount = (String) session.getAttribute("memAccount");
        if (memAccount == null) {
            return "redirect:/mem/login49";
        }

        try {
            // 獲取會員資訊
            MemberVO member = memberSvc.findByMemAccount(memAccount);
            if (member == null) {
                return "redirect:/memb/login49";
            }

            // 獲取該會員的優惠券列表
            List<MemCouponVO> memCoupons = memCouponSvc.getAllByMemNo(member.getMemNo());
            model.addAttribute("memCoupons", memCoupons);
            
            return "front-end/memcoupon/memListAllCoupon";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "front-end/memcoupon/memListAllCoupon";
        }
    }
//	winfred===================================================================================以上

	
}
