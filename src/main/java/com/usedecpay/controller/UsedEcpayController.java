package com.usedecpay.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.member.model.MemberService;
import com.usedecpay.model.UsedEcpayService;



@RestController
@RequestMapping("/used")
public class UsedEcpayController {
	
	@Autowired
	UsedEcpayService ecpayService;
	
	@Autowired
	MemberService memberService;
	
	

	@PostMapping("/ecpayCheckout")
	public String ecpayCheckout() {
		String aioCheckOutALLForm = ecpayService.genAioCheckOutOneTime();

		return aioCheckOutALLForm;
	}
}