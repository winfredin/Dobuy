package com.used.controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.member.model.MemberService;
import com.member.model.MemberVO;
import com.used.model.UsedService;
import com.used.model.UsedVO;

@Controller
@RequestMapping("/used")
public class UsedBuyController {

	@Autowired
	private MemberService memberService;
	@Autowired
	private UsedService usedSvc;

	@PostMapping("/usedBuyCheck")
	public String usedBuyCheck(@RequestParam("usedNo") String usedNo, @RequestParam("usedCount") String usedCount,
			HttpSession session, Model model) {

		UsedVO usedVO = usedSvc.getOneUsed(Integer.valueOf(usedNo));// 測試用 1 找到該商品目前資訊(價格,庫存 etc....)
		Integer stocks = usedVO.getUsedStocks();
		
		 
		if (session.getAttribute("memNo") == null) {// 若沒有登入 返回登入頁面
			return "front-end/member/login";
		} else if (Integer.valueOf(usedCount) > stocks) { // 檢查庫存量 若不足夠則返回前頁
//			System.out.println(usedVO.getUsedPics().size());	
			model.addAttribute("errorMessage", "庫存量不足！");
			model.addAttribute("usedVO", usedVO);
			return "front-end/used/shop_detail_used"; // 返回的 Thymeleaf 模板名稱
		} else { // 庫存量足夠也沒有被停止買權的話
			Integer memNo = Integer.valueOf((String) session.getAttribute("memNo"));
			Optional<MemberVO> memberVO = memberService.findById(1);// 測試用 1
			MemberVO mem = memberVO.get();
			String receiverAdr = mem.getMemAddress();// 預設 receiverAdr
			String receiverName = mem.getMemName();// 預設 receiverName
			String receiverPhone = mem.getMemPhone();// 預設 receiverPhone

			
			
			
			model.addAttribute("usedVO",usedVO);
			model.addAttribute("usedCount", Integer.valueOf(usedCount));
			model.addAttribute("receiverPhone", receiverPhone);
			model.addAttribute("receiverName", receiverName);
			model.addAttribute("receiverAdr", receiverAdr);
			return "front-end/used/UsedCheckout";
		}
	}

}
