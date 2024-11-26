package com.goods.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.countercarousel.model.CountercarouselService;
import com.countercarousel.model.CountercarouselVO;
import com.goods.model.GoodsService;
import com.goods.model.GoodsVO;

@Controller
@Validated
@RequestMapping("/goods")
public class GoodsNoController {

    @Autowired
    GoodsService goodsSvc;
    
    @Autowired
	CountercarouselService carouselSvc;

    /*
     * This method will be called on select_page.html form submission, handling POST request
     * It also validates the user input
     */
    @PostMapping("getOne_For_Display")
    public String getOne_For_Display(
        /***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
        @NotEmpty(message = "商品編號: 請勿空白")
        @Digits(integer = 5, fraction = 0, message = "商品編號: 請填數字-請勿超過{integer}位數")
        @Min(value = 1, message = "商品編號: 不能小於{value}")
        @Max(value = 99999, message = "商品編號: 不能超過{value}")
        @RequestParam("goodsNo") String goodsNo, ModelMap model) {

        /***************************2.開始查詢資料*********************************************/
        GoodsVO goodsVO = goodsSvc.getOneGoods(Integer.valueOf(goodsNo));
        
        List<GoodsVO> list = goodsSvc.getAll();
        model.addAttribute("goodsListData", list); // for select_page.html 下拉選單
        if (goodsVO == null) {
            model.addAttribute("errorMessage", "查無資料");
            return "vendor-end/goods/select_page"; // 回傳給用戶查無資料的提示頁面
        }

        /***************************3.查詢完成,準備轉交(Send the Success view)*****************/
        model.addAttribute("goodsVO", goodsVO);
        model.addAttribute("getOne_For_Display", "true"); // Flag for displaying specific data
        return "vendor-end/goods/select_page";
    }

    /*
     * This method handles exceptions from constraint violations during form submissions.
     */
    @ExceptionHandler(value = { ConstraintViolationException.class })
    public ModelAndView handleError(HttpServletRequest req, ConstraintViolationException e, Model model) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            strBuilder.append(violation.getMessage() + "<br>");
        }

        //==== 处理时重新加载页面所需数据 ====
        List<GoodsVO> list = goodsSvc.getAll();
        model.addAttribute("goodsListData", list); // for select_page.html 下拉選單
        String message = strBuilder.toString();
        return new ModelAndView("vendor-end/goods/select_page","errorMessage", "請修正以下錯誤:<br>"+message); // 回傳錯誤訊息並讓用戶修正
    }
    
    @GetMapping("getOne_For_Display35")
    public String getOne_For_Display35(
        @RequestParam("counterNo") String counterNo, ModelMap model) {
        List<GoodsVO> goodsVO = goodsSvc.getOneCounter35(Integer.valueOf(counterNo));
        List<CountercarouselVO> carouselImages = carouselSvc.getPic(Integer.valueOf(counterNo));// 抓輪播圖圖片(依櫃位編號)
        for(CountercarouselVO img:carouselImages) {
        	img.convertToBase64();
        }
        model.addAttribute("carouselImages", carouselImages);
        model.addAttribute("goodsVO", goodsVO);
        return "/vendor-end/front-end-product/product"; // 返回 Thymeleaf 模板名
    }
    
    @PostMapping("getAll35") //  11/26
    public String getAll35(ModelMap model) {
    	List<GoodsVO> goodsVO = goodsSvc.getAll();
    	model.addAttribute("goodsVO", goodsVO);
    	return "/vendor-end/front-end-product/product";
    }
}