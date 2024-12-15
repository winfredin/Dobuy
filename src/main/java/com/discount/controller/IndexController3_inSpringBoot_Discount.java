package com.discount.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.discount.model.DiscountService;
import com.discount.model.DiscountVO;

import java.util.*;

import javax.servlet.http.HttpSession;

@Controller("discountIndexController") 
@RequestMapping("/discount")
public class IndexController3_inSpringBoot_Discount {

    // 自動裝配DiscountService
    @Autowired
    DiscountService discountSvc;

    // inject(注入資料) via application.properties
    @Value("${welcome.message}")
    private String message;

    private List<String> myList = Arrays.asList(
            "Spring Boot Quickstart 官網 : https://start.spring.io",
            "Thymeleaf",
            "Java WebApp (<font color=red>快速完成 Spring Boot Web MVC</font>)",
            "Spring Data JPA",
            "Hibernate ORM");

    @GetMapping("/inindex")
    public String index(Model model) {
        model.addAttribute("message", message);
        model.addAttribute("myList", myList);
        return "back-end/discount/inindex"; // 對應視圖名稱
    }

    // http://......../hello?name=peter1
    @GetMapping("/hello1")
    public String indexWithParam(
            @RequestParam(name = "name", required = false, defaultValue = "") String name, Model model) {
        model.addAttribute("message", name);
        return "inindex"; //view
    }

    // 提供查詢頁面 絕對路徑 Page1 Page2
    @GetMapping("/discount/select_page")
    public String selectPage(Model model) {
        return "back-end/discount/select_page";
    }

    // 相對路徑 Page3 Page4
    @GetMapping("select_page")
    public String select_page(ModelMap model) {
        DiscountVO discountVO = new DiscountVO();
        model.addAttribute("discountVO", discountVO);
        return "back-end/discount/select_page";
    }

    // 提供所有優惠活動列表頁面
    @GetMapping("/listAllDiscount")
    public String listAllDiscount(Model model,HttpSession session) {
    	if(session.getAttribute("managerNo")==null) {
			return "redirect:/login/Login";
		}
	
        return "back-end/discount/listAllDiscount";
    }

    // 提供優惠活動資料清單，供 Thymeleaf 使用
    @ModelAttribute("discountListData") // for select_page.html 和 listAllDiscount.html
    protected List<DiscountVO> referenceListData(Model model) {
        List<DiscountVO> list = discountSvc.getAll();
        return list;
    }

 
}
