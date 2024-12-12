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
import com.manager.model.ManagerService;
//import com.dept.model.DeptVO;
import com.manager.model.ManagerVO;

import java.util.*;

import javax.servlet.http.HttpSession;

@Controller // ch2-p65 ch3-77 ch8-139
//@RequestMapping("/coupondetail")
//@RequestMapping("/")
public class Controller_Coupondetail {

	@Autowired
	CouponDetailService couponDetailSvc;

	@Autowired
	CouponService couponSvc;
	
	@Autowired
	ManagerService managerService;
    

	
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
	@GetMapping("/back-end-homepage")
	public String adminHome(HttpSession session, Model model) {
	    // 從 Session 中獲取 managerNo
	    Integer managerNo = (Integer) session.getAttribute("managerNo");
	    
	    if (managerNo != null) {
	        // 使用 getOneManager 方法來獲取 ManagerVO
	        ManagerVO manager = managerService.getOneManager(managerNo);
	        if (manager != null) {
	            session.setAttribute("managerName", manager.getManagerName()); // 設置到 session
	            model.addAttribute("managerName", manager.getManagerName());   // 設置到 model
	        } else {
	            // 如果查詢不到管理員，則清除 session 並重導向登入頁面
	            session.invalidate();
	            return "redirect:/login/Login";
	        }
	    } else {
	        // managerNo 不存在於 session 中，重導向登入頁面
	        return "redirect:/login/Login";
	    }

	    return "back-end/back-end-home/back-end-homepage"; // 返回 Thymeleaf 模板名稱
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
	
	


}
