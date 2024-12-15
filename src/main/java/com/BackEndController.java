package com;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.counterorder.model.CounterOrderService;
import com.counterorder.model.CounterOrderVO;
import com.manager.model.ManagerService;
import com.manager.model.ManagerVO;
import com.member.model.MemberService;
import com.member.model.MemberVO;

@Controller
@RequestMapping("/back-end")
public class BackEndController {
	@Autowired
	MemberService memberSvc;
	@Autowired
	CounterOrderService counterOrderSvc;
	@Autowired
	ManagerService managerService;
	
	@GetMapping("member")
	public String member(ModelMap model,HttpSession session) {
		List<MemberVO> memlist = memberSvc.getAll();
		model.addAttribute("memlist",memlist);
		return "back-end/member/member";
	}
	@GetMapping("memberorder")
	public String memberorder(ModelMap model) {
		
		List<CounterOrderVO> alist = counterOrderSvc.getAll();
		model.addAttribute("alist",alist);
		return "back-end/member/memberorder";
	}
	
	@PostMapping("updateStatus")
	public String updateMemberStatus(@RequestParam("memStatus") Integer memStatus,
	                                  @RequestParam("memNo") Integer memNo) {
	
		    memberSvc.upStatus(memNo, memStatus);

	    return "redirect:/back-end/member"; 
	}
	@PostMapping("updateOrder")
	public String updateOrderStatus(@RequestParam("orderStatus") Integer orderStatus,
	                                  @RequestParam("memNo") Integer memNo) {
	
		counterOrderSvc.updateCounterStatus(memNo, orderStatus);
		

	    return "redirect:/counter/listOneCounter"; 
	}
	

}
