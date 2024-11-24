//package com.memcoupon.controller;
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
//import com.memcoupon.model.MemCouponService;
//import com.memcoupon.model.MemCouponVO;
//
//import java.util.*;
//
//@Controller("memCouponIndexController") // Controller for MemCoupon
////@RequestMapping("/memcoupon")
////@RequestMapping("/")
//public class IndexController4_inSpringBoot_MemCoupon {
//
//    // 自動裝配MemCouponService
//    @Autowired
//    MemCouponService memCouponSvc;
//
//    // Inject via application.properties
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
//    @GetMapping("/inindex")
//    public String index(Model model) {
//        model.addAttribute("message", message);
//        model.addAttribute("myList", myList);
//        return "vendor-end/memcoupon/inindex"; // 對應視圖名稱
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
//    // 提供查詢頁面 (絕對路徑)
//    @GetMapping("/memcoupon/select_page")
//    public String selectPage(Model model) {
//        return "vendor-end/memcoupon/select_page";
//    }
//
//    // 提供查詢頁面 (相對路徑)
//    @GetMapping("select_page")
//    public String select_page(ModelMap model) {
//        MemCouponVO memCouponVO = new MemCouponVO();
//        model.addAttribute("memCouponVO", memCouponVO);
//        return "vendor-end/memcoupon/select_page";
//    }
//
//    // 提供所有會員優惠券列表頁面
//    @GetMapping("/memcoupon/listAllMemCoupon")
//    public String listAllMemCoupon(Model model) {
//        return "vendor-end/memcoupon/listAllMemCoupon";
//    }
//
//    // 提供會員優惠券資料清單，供 Thymeleaf 使用
//    @ModelAttribute("memCouponListData") // for select_page.html 和 listAllMemCoupon.html
//    protected List<MemCouponVO> referenceListData(Model model) {
//        List<MemCouponVO> list = memCouponSvc.getAll();
//        return list;
//    }
//
//    @GetMapping("/back-end-homepage") // Back-end homepage
//    public String index3() {
//        return "back-end/back-end-home/back-end-homepage";
//    }
//
//    @GetMapping("/fragmentheader")
//    public String fragmentheader() {
//        return "back-end/back-end-home/fragmentheader";
//    }
//
//    @GetMapping("/test1")
//    public String test1() {
//        return "back-end/back-end-home/test1";
//    }
//
//    @GetMapping("test2")
//    public String test2() {
//        return "back-end/back-end-home/test2";
//    }
//}
