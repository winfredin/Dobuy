package com.faq.controller;

import java.util.ArrayList;
import java.util.LinkedList;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.counter.model.CounterService;
import com.counter.model.CounterVO;
import com.faq.model.FaqService;
import com.faq.model.FaqVO;

@Controller
@RequestMapping("/faq")
public class FaqController {

	@Autowired
	FaqService faqsvc;

	@Autowired
	CounterService counterSvc;

	
//	櫃位FAQ管理(任國)
    @GetMapping("/listCounterFaq")
    public String listAllFaq(HttpSession session, Model model) {
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
        model.addAttribute("counterFaqListData", list); 
        return "vendor-end/faq/listCounterFaq";
    }
    
//	櫃位FAQ管理(任國)


////	櫃位FAQ管理
//	櫃位FAQ管理
//	@GetMapping("/listCounterFaq")
//	public String listAllCoupon(HttpSession session, Model model) {
//		// 櫃位優惠券登錄確認
//		CounterVO counter = (CounterVO) session.getAttribute("counter");
//		if (counter == null) {
//			return "redirect:/counter/login";
//		} else {
//			// 其他邏輯
//			model.addAttribute("counter", counterSvc.getOneCounter(counter.getCounterNo()));
//			model.addAttribute("counterFaqListData", faqsvc.getOneCounterFaq(counter.getCounterNo()));
//			return "vendor-end/faq/listCounterFaq";
//		}
//	}

//	@ModelAttribute("counterFaqListData")
//	protected List<FaqVO> CounterReferenceListData(HttpSession session, Model model) {
//		CounterVO counter = (CounterVO) session.getAttribute("counter");
//		if (counter != null) {
//			List<FaqVO> list = faqsvc.getOneCounterFaq(counter.getCounterNo());
//			return list;
//		} else {
//			// 如果counter為null，返回一個空列表或處理錯誤
//			model.addAttribute("error", "未登錄或Session信息遺失");
//			return new ArrayList<>(); // 或者其他適當的錯誤處理
//		}
//	}
//
//	@PostMapping("listCounterFaq_ByCompositeQuery")
//	public String listCounterFaq(HttpSession session, HttpServletRequest req, Model model) {
//		CounterVO counter = (CounterVO) session.getAttribute("counter");
//		List<FaqVO> list = faqsvc.getOneCounterFaq(counter.getCounterNo());
//		model.addAttribute("counterFaqListData", list); // for listAllEmp.html 第85行用
//		return "vendor-end/faq/listCounterFaq";
//	}

//	櫃位FAQ管理
	//以下昱夆新增
	
	@PostMapping("/getOneUpdate")
	public String getOneFaq(@RequestParam("faqNo") String faqNo, HttpSession session, Model model) {
		CounterVO counter = (CounterVO) session.getAttribute("counter");
		model.addAttribute("counter", counterSvc.getOneCounter(counter.getCounterNo()));
		model.addAttribute("faqVO", faqsvc.getOneFaq(faqNo));
		return "vendor-end/faq/updateFAQ";
	}

	

	@PostMapping("/update")
	public String updateFaq(@ModelAttribute FaqVO faqVO, HttpSession session, Model model) {
		CounterVO counter = (CounterVO) session.getAttribute("counter");
		model.addAttribute("counter", counterSvc.getOneCounter(counter.getCounterNo()));
		List<String> errorMsgs = new LinkedList<String>();
		if (faqVO.getQues() == null || faqVO.getQues().trim().isEmpty()) {
			errorMsgs.add("問題不能空白");
		}
		if (faqVO.getAns() == null || faqVO.getAns().trim().isEmpty()) {
			errorMsgs.add("答案不能空白");
		}

		if (!errorMsgs.isEmpty()) {
			model.addAttribute("faqVO", faqVO);
			model.addAttribute("errorMsgs", errorMsgs);
			return "vendor-end/faq/updateFAQ";
		}

		faqsvc.updateFaq(faqVO);
		model.addAttribute("faqVO", new FaqVO());
		return "redirect:/faq/listCounterFaq";
	}
	
	@GetMapping("/add")
	public String showLoginPage(Model model, HttpSession session){
		CounterVO counter = (CounterVO) session.getAttribute("counter");
		model.addAttribute("counter", counterSvc.getOneCounter(counter.getCounterNo()));
		model.addAttribute("faqVO", new FaqVO());
		
		return "vendor-end/faq/addFAQ"; // 指向 Thymeleaf 模板路径
	}
	
	@PostMapping("/insert")
	public String addFAQ(@ModelAttribute FaqVO faqVO, HttpSession session, Model model) {
		CounterVO counter = (CounterVO) session.getAttribute("counter");
		faqVO.setCounterNo(counter.getCounterNo());
		model.addAttribute("counter", counterSvc.getOneCounter(counter.getCounterNo()));
		List<String> errorMsgs = new LinkedList<String>();
		if (faqVO.getQues() == null || faqVO.getQues().trim().isEmpty()) {
			errorMsgs.add("問題不能空白");
		}
		if (faqVO.getAns() == null || faqVO.getAns().trim().isEmpty()) {
			errorMsgs.add("答案不能空白");
		}

		if (!errorMsgs.isEmpty()) {
			model.addAttribute("faqVO", faqVO);
			model.addAttribute("errorMsgs", errorMsgs);
			return "vendor-end/faq/add";
		}
		faqsvc.addFaq(faqVO);
		model.addAttribute("faqVO", new FaqVO());
		return "redirect:/faq/listCounterFaq";
		
	}
	
	
	@PostMapping("/delete")
	public String deleteFAQ(@RequestParam("faqNo") String faqNo) {
		faqsvc.deleteFaq(Integer.valueOf(faqNo));
		return "redirect:/faq/listCounterFaq";
	}
	
	//以上昱夆新增
}
