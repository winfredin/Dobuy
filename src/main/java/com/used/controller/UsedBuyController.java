package com.used.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.member.model.MemberService;
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
	public String usedBuyCheck(@RequestParam("usedNo") String usedNo,@RequestParam("usedCount") String usedCount,HttpSession session,Model model) {
		
		UsedVO usedVO =usedSvc.getOneUsed(Integer.valueOf(usedNo));//找到該商品目前資訊(價格,庫存 etc....)
		Integer stocks = usedVO.getUsedStocks();
		
		if(session.getAttribute("memNo")!=null) {//若沒有登入 返回登入頁面
			return "front-end/member/login";
		}else 
			if( Integer.valueOf(usedCount) > stocks){ //檢查庫存量 若不足夠則返回前頁
//			System.out.println(usedVO.getUsedPics().size());	
			model.addAttribute("errorMessage", "庫存量不足！");
			model.addAttribute("usedVO", usedVO);
	        return "front-end/used/shop_detail_used"; // 返回的 Thymeleaf 模板名稱	
		}else {
//		
//		
//			
//			
//			
//		
//		
//		
//		
//		
//		
//		
//		
		return "front-end/used/UsedCheckout";
		}
	}
	
}
