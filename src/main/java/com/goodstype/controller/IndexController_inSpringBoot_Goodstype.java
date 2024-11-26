package com.goodstype.controller;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.goodstype.model.GoodsTypeService;
import com.goodstype.model.GoodsTypeVO;

@Controller
public class IndexController_inSpringBoot_Goodstype {

    // 自動裝配 GoodsTypeService 以便獲取商品類別資料
    @Autowired
    GoodsTypeService goodsTypeSvc;

    // 注入資料（例如從 application.properties 獲取配置）
    @Value("${welcome.message}")
    private String message;

	private List<String> myList = Arrays.asList("Spring Boot Quickstart 官網 : https://start.spring.io", "IDE 開發工具",
			"直接使用(匯入)官方的 Maven Spring-Boot-demo Project + pom.xml",
			"直接使用官方現成的 @SpringBootApplication + SpringBootServletInitializer 組態檔",
			"依賴注入(DI) HikariDataSource (官方建議的連線池)", "Thymeleaf",
			"Java WebApp (<font color=red>快速完成 Spring Boot Web MVC</font>)");

	@GetMapping("/goodstype")
	public String index(Model model) {
		model.addAttribute("message", message);
		model.addAttribute("myList", myList);
		return "back-end/goodstype/goodstype"; // view
	}

	// http://......../hello?name=peter1
	@GetMapping("/hello3")
	public String indexWithParam(@RequestParam(name = "name", required = false, defaultValue = "") String name,
			Model model) {
		model.addAttribute("message", name);
		return "goodstype"; // view
	}
	
	// =========== 以下第63~75行是提供給
	// /src/main/resources/templates/back-end/emp/select_page.html 與 listAllEmp.html
	// 要使用的資料 ===================
    // 顯示商品類別資料的頁面
    @GetMapping("/goodstype/select_page")
    public String select_page(Model model) {
        return "back-end/goodstype/select_page"; // 返回商品類別選擇頁
    }
    
    // 顯示所有商品類別資料的頁面
    @GetMapping("/goodstype/listAllGoodsType")
    public String listAllGoodsType(Model model) {
    	return "back-end/goodstype/listAllGoodsType"; // 返回所有商品類別列表頁
    }

    // 提供商品類別資料（對應select_page.html和listAllGoodsType.html的數據）
    @ModelAttribute("goodsTypeListData") // 用於select_page.html和listAllGoodsType.html
    protected List<GoodsTypeVO> referenceListData(Model model) {
        List<GoodsTypeVO> list = goodsTypeSvc.getAll(); // 獲取所有商品類別資料
        return list;
    }
}