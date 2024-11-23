//package com.coupondetail.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import com.coupondetail.model.CouponDetailService;
//import com.coupondetail.model.CouponDetailVO;
////import com.dept.model.DeptVO;
//
//import java.util.*;
//
//@Controller("couponDetailIndexController") //ch2-p65 ch3-77 ch8-139
////@RequestMapping("/coupondetail")
////@RequestMapping("/")
//public class IndexController1_inSpringBoot {
//	
//    // 自動裝配CouponDetailService
//    @Autowired
//    CouponDetailService couponDetailSvc;
//
//    // inject(注入資料) via application.properties
//    @Value("${welcome.message}")
//    private String message;
//
//    private List<String> myList = Arrays.asList(
//            "Spring Boot Quickstart 官網 : https://start.spring.io",
//            "Thymeleaf",
//            "Java WebApp (<font color=red>快速完成 Spring Boot Web MVC</font>)",
//            "Spring Data JPA",
//            "Hibernate ORM");
//
//	
//    @GetMapping("/inindex")
//    public String index(Model model) {
//        model.addAttribute("message", message);
//        model.addAttribute("myList", myList);
//        return "inindex"; // 對應視圖名稱
//    }
//
//    // http://......../hello?name=peter1
//    @GetMapping("/hello1")
//    public String indexWithParam(
//            @RequestParam(name = "name", required = false, defaultValue = "") String name, Model model) {
//        model.addAttribute("message", name);
//        return "inindex"; //view
//    }
//    
////     提供查詢頁面 絕對路徑 Page1 Page2
//    @GetMapping("/coupondetail/select_page")
//    public String selectPage(Model model) {
//        return "back-end/coupondetail/select_page";
//    }
//
////   相對路徑 Page3 Page4 要註解@RequestMapping("/coupondetail")
//    @GetMapping("select_page")
//    public String select_page(ModelMap model) {
//        CouponDetailVO couponDetailVO = new CouponDetailVO();
//        model.addAttribute("couponDetailVO", couponDetailVO);
//        return "back-end/coupondetail/select_page";
//    }
//    
//    
//    
//    
//    
//    
//    
//    
//    // 提供所有優惠券列表頁面
//    @GetMapping("/coupondetail/listAllCouponDetail")
//    public String listAllCoupon(Model model) {
//        return "back-end/coupondetail/listAllCouponDetail";
//    }
//
//    // 提供優惠券資料清單，供 Thymeleaf 使用
//    @ModelAttribute("couponDetailListData") // for select_page.html 和 listAllCoupon.html
//    protected List<CouponDetailVO> referenceListData(Model model) {
//        List<CouponDetailVO> list = couponDetailSvc.getAll();
//        return list;
//    }
//    
//    
//	@GetMapping("/index3")//ch2-p65 ch3-77 ch8-139
//	public String index3() {
//		return "index3";
//	} 
//    
//	@GetMapping("/fragmentheader")
//	public String fragmentheader() {
//		return "fragmentheader";
//	} 
//	
//	@GetMapping("/test1")
//	public String test1() {
//		return "test1";
//	} 
//	
//    @GetMapping("test2")
//    public String test2() {
//        return "back-end/coupondetail/test2";
//    }
//    
////    @GetMapping("test2")
////    public String test2(ModelMap model) {
////        CouponDetailVO couponDetailVO = new CouponDetailVO();
////        model.addAttribute("couponDetailVO", couponDetailVO);
////        return "back-end/coupondetail/test2";
////    }
//    
//    
////	@ModelAttribute("deptListData") // for select_page.html 第135行用
////	protected List<DeptVO> referenceListData_Dept(Model model) {
////		model.addAttribute("deptVO", new DeptVO()); // for select_page.html 第133行用
////		List<DeptVO> list = deptSvc.getAll();
////		return list;
////	}
//
//    
//    
//
////	@GetMapping("/")//ch2-p65 ch3-77 ch8-139
////	public String myMethod() {
////		return "index"; //src\main\resources\templates\index1.html  //p137(p265)
////	}
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
//}
