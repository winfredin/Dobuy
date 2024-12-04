package com.coupondetail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.coupon.model.CouponService;
import com.coupon.model.CouponVO;
import com.coupondetail.model.CouponDetailService;
import com.coupondetail.model.CouponDetailVO;
//import com.dept.model.DeptVO;

import java.util.*;

@Controller // ch2-p65 ch3-77 ch8-139
//@RequestMapping("/coupondetail")
//@RequestMapping("/")
public class Controller_Coupondetail {

	// 自動裝配CouponDetailService
	@Autowired
	CouponDetailService couponDetailSvc;

//  // 自动装配 CouponService
	@Autowired
	CouponService couponSvc;
	
	
	// inject(注入資料) via application.properties
	@Value("${welcome.message}")
	private String message;

	private List<String> myList = Arrays.asList("Spring Boot Quickstart 官網 : https://start.spring.io", "Thymeleaf",
			"Java WebApp (<font color=red>快速完成 Spring Boot Web MVC</font>)", "Spring Data JPA", "Hibernate ORM");

	@GetMapping("/inindex")
	public String index(Model model) {
		model.addAttribute("message", message);
		model.addAttribute("myList", myList);
		return "vendor-end/coupondetail/inindex"; // 對應視圖名稱
	}

	// http://......../hello?name=peter1
	@GetMapping("/hello1")
	public String indexWithParam(@RequestParam(name = "name", required = false, defaultValue = "") String name,
			Model model) {
		model.addAttribute("message", name);
		return "vendor-end/coupondetail/inindex"; // view
	}

//     提供查詢頁面 絕對路徑 Page1 Page2
	@GetMapping("/coupondetail/select_page")
	public String selectPage(Model model) {
		return "vendor-end/coupondetail/select_page";
	}

//   相對路徑 Page3 Page4 要註解@RequestMapping("/coupondetail")
	@GetMapping("select_page")
	public String select_page(ModelMap model) {
		CouponDetailVO couponDetailVO = new CouponDetailVO();
		model.addAttribute("couponDetailVO", couponDetailVO);
		return "vendor-end/coupondetail/select_page";
	}

	// 提供所有優惠券列表頁面
	@GetMapping("/coupondetail/listAllCouponDetail")
	public String listAllCoupon(Model model) {
		return "vendor-end/coupondetail/listAllCouponDetail";
	}

	// 提供CouponDetailVO清單，供 Thymeleaf 使用
	@ModelAttribute("couponDetailListData") // for select_page.html 和 listAllCoupon.html
	protected List<CouponDetailVO> referenceListData1(Model model) {
		List<CouponDetailVO> list = couponDetailSvc.getAll();
		return list;
	}

	// 提供CouponVO列表，供 Thymeleaf 使用
	@ModelAttribute("couponListData") // for select_page.html 和 listAllCoupon.html
	protected List<CouponVO> referenceListData2(Model model) {
		List<CouponVO> list = couponSvc.getAll();
		return list;
	}

//	後台首頁
	@GetMapping("/back-end-homepage") // ch2-p65 ch3-77 ch8-139
	public String index3() {
		return "back-end/back-end-home/back-end-homepage";
	}

//	後台樣板原檔
	@GetMapping("/fragmentheader")
	public String fragmentheader() {
		return "back-end/back-end-home/fragmentheader";
	}

//	後台樣板
	@GetMapping("test2")
	public String test2() {
		return "back-end/back-end-home/test2";
	}
//後台審核頁面
	@GetMapping("couponcheck")
	public String couponcheck() {
		return "back-end/coupon/couponcheck";
	}

}
