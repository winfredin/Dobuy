package com.ShoppingCartList.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ShoppingCartList.model.ShoppingCartListService;
import com.ShoppingCartList.model.ShoppingCartListVO;
import com.goods.model.GoodsService;
import com.goods.model.GoodsVO;



@Controller
public class IndexController_inSpringBoot_ShoppingCartList {

    // 注入 ShoppingCartListService 用來查詢購物車資料
    @Autowired
    ShoppingCartListService shoppingCartListSvc;

    @Autowired 
    GoodsService goodsSvc;
    // 注入資料 (application.properties中的設置)
    @Value("${welcome.message}")
    private String message;
    
    private List<String> myList = Arrays.asList(
        "Spring Boot Quickstart 官網 : https://start.spring.io", 
        "IDE 開發工具", 
        "直接使用(匯入)官方的 Maven Spring-Boot-demo Project + pom.xml", 
        "直接使用官方現成的 @SpringBootApplication + SpringBootServletInitializer 組態檔", 
        "依賴注入(DI) HikariDataSource (官方建議的連線池)", 
        "Thymeleaf", 
        "Java WebApp (<font color=red>快速完成 Spring Boot Web MVC</font>)"
    );

    // 設定首頁
    @GetMapping("/shoppingcartlist")
    public String index(Model model) {
        model.addAttribute("message", message);
        model.addAttribute("myList", myList);
        return "front-end/shoppingcartlist/shoppingcartlist"; // 返回index頁面
    }

    // http://......../hello?name=peter1
    @GetMapping("/helloS")
    public String indexWithParam(
            @RequestParam(name = "name", required = false, defaultValue = "") String name, Model model) {
        model.addAttribute("message", name);
        return "shoppingcartlist"; // 返回index頁面
    }

    // 購物車頁面展示
    @GetMapping("/shoppingcartlist/select_page")
    public String select_page(Model model) {
        return "front-end/shoppingcartlist/select_page"; // 返回選擇購物車頁面
    }

    // 顯示所有購物車列表
    @GetMapping("/shoppingcartlist/listAllShoppingCartList")
    public String listAllShoppingCart(Model model) {
        return "front-end/shoppingcartlist/listAllShoppingCartList"; // 返回顯示所有購物車的頁面
    }
    
    // 購物車結帳畫面
    @GetMapping("/shoppingcartlist/ShoppingCartListCheckout")
    public String ShoppingCartListCheckout(Model model, HttpSession session) {
       
        List<ShoppingCartListVO> list = shoppingCartListSvc.getAll();
        for(ShoppingCartListVO a : list) {
       GoodsVO ab = goodsSvc.getOneGoods(a.getGoodsNo());
       ab.setGoodsAmount(ab.getGoodsAmount()-a.getGoodsNum());
      
       if(ab.getGoodsAmount()<0) {
    	   model.addAttribute("error","庫存不足");
    	   return "front-end/shoppingcartlist/listAllShoppingCartList";
       }else {
    	   model.addAttribute("ab",ab);
    	   goodsSvc.updateGoodsAmount(a.getGoodsNo(),ab.getGoodsAmount());
       }
      
      
        }
        
        model.addAttribute("carlist", list);
        return "front-end/shoppingcartlist/ShoppingCartListCheckout";
    }


    // 為select_page.html提供購物車資料
    @ModelAttribute("shoppingCartListListData")
    protected List<ShoppingCartListVO> referenceListData(Model model) {
        List<ShoppingCartListVO> list = shoppingCartListSvc.getAll();
        return list; // 返回所有購物車列表
    }

}
