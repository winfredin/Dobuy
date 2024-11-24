//package com.coupon.controller;
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
//import com.coupon.model.CouponService;
//import com.coupon.model.CouponVO;
//
//import java.util.*;
//
//@Controller("couponIndexController") 
//public class IndexController2_inSpringBoot_Coupon {
//    
//    // 自动装配 CouponService
//    @Autowired
//    CouponService couponSvc;
//
//    // inject(注入数据) via application.properties
//    @Value("${welcome.message}")
//    private String message;
//
//    private List<String> myList = Arrays.asList(
//            "Spring Boot Quickstart 官网 : https://start.spring.io",
//            "Thymeleaf",
//            "Java WebApp (<font color=red>快速完成 Spring Boot Web MVC</font>)",
//            "Spring Data JPA",
//            "Hibernate ORM");
//
//    // 主页映射
//    @GetMapping("/inindex")
//    public String index(Model model) {
//        model.addAttribute("message", message);
//        model.addAttribute("myList", myList);
//        return "vendor-end/coupon/inindex"; // 对应视图名称
//    }
//
//    // 根据参数显示欢迎信息
//    @GetMapping("/hello2")
//    public String indexWithParam(
//            @RequestParam(name = "name", required = false, defaultValue = "") String name, Model model) {
//        model.addAttribute("message", name);
//        return "inindex"; // 视图
//    }
//
//    // 提供查询页面（绝对路径）
//    @GetMapping("/coupon/select_page")
//    public String selectPage(Model model) {
//        return "vendor-end/coupon/select_page";
//    }
//
//    // 提供查询页面（相对路径）
//    @GetMapping("select_page")
//    public String select_page(ModelMap model) {
//        CouponVO couponVO = new CouponVO();
//        model.addAttribute("couponVO", couponVO);
//        return "vendor-end/coupon/select_page";
//    }
//
//    // 提供所有优惠券列表页面
//    @GetMapping("/coupon/listAllCoupon")
//    public String listAllCoupon(Model model) {
//        return "vendor-end/coupon/listAllCoupon";
//    }
//
//    // 提供优惠券数据列表，供 Thymeleaf 使用
//    @ModelAttribute("couponListData") // for select_page.html 和 listAllCoupon.html
//    protected List<CouponVO> referenceListData(Model model) {
//        List<CouponVO> list = couponSvc.getAll();
//        return list;
//    }
//
//    @GetMapping("/back-end-homepage") 
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