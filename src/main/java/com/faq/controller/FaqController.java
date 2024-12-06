package com.faq.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.counter.model.CounterService;
import com.counter.model.CounterVO;
import com.coupon.model.CouponVO;
import com.faq.model.FaqService;
import com.faq.model.FaqVO;



@Controller
@RequestMapping("/faq")
public class FaqController {
	
	
    
	@Autowired
	FaqService faqsvc;
	
	
	@Autowired
	CounterService counterSvc;
	
//	櫃位FAQ管理
    @GetMapping("/listCounterFaq")
    public String listAllCoupon(HttpSession session, Model model) {
    	//櫃位優惠券登錄確認
        CounterVO counter = (CounterVO) session.getAttribute("counter");
        if (counter == null) {
            return "redirect:/counter/login";
        } else {
            // 其他邏輯
            model.addAttribute("counter", counterSvc.getOneCounter(counter.getCounterNo()));
            model.addAttribute("counterFaqListData", faqsvc.getOneCounterFaq(counter.getCounterNo()));
            return "vendor-end/faq/listCounterFaq";
        }
    }
    
    @ModelAttribute("counterFaqListData")
    protected List<FaqVO> CounterReferenceListData(HttpSession session, Model model) {
        CounterVO counter = (CounterVO) session.getAttribute("counter");
        if (counter != null) {
        	List<FaqVO> list = faqsvc.getOneCounterFaq(counter.getCounterNo());
            return list;
        } else {
            // 如果counter為null，返回一個空列表或處理錯誤
            model.addAttribute("error", "未登錄或Session信息遺失");
            return new ArrayList<>(); // 或者其他適當的錯誤處理
        }
    }
    
    @PostMapping("listCounterFaq_ByCompositeQuery")
    public String listCounterFaq(HttpSession session ,HttpServletRequest req, Model model) {
        CounterVO counter = (CounterVO) session.getAttribute("counter");
        List<FaqVO> list = faqsvc.getOneCounterFaq(counter.getCounterNo());
        model.addAttribute("counterFaqListData", list); // for listAllEmp.html 第85行用
        return "vendor-end/faq/listCounterFaq";
    }
    
//	櫃位FAQ管理

}
