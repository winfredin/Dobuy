package com.faq.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.counter.model.CounterService;
import com.counter.model.CounterVO;
import com.faq.model.FaqService;



@Controller
@RequestMapping("/faq")
public class FaqController {
	
	
    
//	@Autowired
//	FaqService faqsvc;
//	
//	
//	@Autowired
//	CounterService counterSvc;
//	
//	TEST
//    @GetMapping("/listCounterFaq")
//    public String listAllCoupon(HttpSession session, Model model) {
//    	//櫃位優惠券登錄確認
//        CounterVO counter = (CounterVO) session.getAttribute("counter");
//        if (counter == null) {
//            return "redirect:/counter/login";
//        } else {
//            // 其他邏輯
//            model.addAttribute("counter", counterSvc.getOneCounter(counter.getCounterNo()));
//            model.addAttribute("counterCouponListData", faqsvc.getOneCounterFaq(counter.getCounterNo()));
//            return "vendor-end/coupon/listCounterFaq";
//        }
//    }

}
