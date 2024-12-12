package com.discount.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import com.discount.model.DiscountVO;
import com.discount.model.DiscountService;

@Controller
@Validated
@RequestMapping("/discount")
public class DisNoController {

    @Autowired
    DiscountService discountService;

    /*
     * This method will be called on select_page.html form submission, handling POST request
     * It also validates the user input
     */
    @PostMapping("getOne_For_Display")
    public String getOne_For_Display(
        /***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
        @NotEmpty(message = "優惠編號: 請勿空白")
        @Digits(integer = 5, fraction = 0, message = "優惠編號: 請填數字-請勿超過{integer}位數")
        @Min(value = 1, message = "優惠編號: 不能小於{value}")
        @Max(value = 99999, message = "優惠編號: 不能超過{value}")
        @RequestParam("disNo") String disNo, ModelMap model) {

        /***************************2.開始查詢資料*********************************************/
        DiscountVO discountVO = discountService.getOneDiscount(Integer.valueOf(disNo));

        List<DiscountVO> list = discountService.getAll();
        model.addAttribute("discountListData", list); // for select_page.html 下拉選單
        if (discountVO == null) {
            model.addAttribute("errorMessage", "查無資料");
            return "back-end/discount/select_page";
        }

        /***************************3.查詢完成,準備轉交(Send the Success view)*****************/
        model.addAttribute("discountVO", discountVO);
        model.addAttribute("getOne_For_Display", "true"); // Flag for displaying specific data
        return "back-end/discount/select_page";
    }

    /*
     * This method handles exceptions from constraint violations during form submissions.
     */
    @ExceptionHandler(value = { ConstraintViolationException.class })
    public ModelAndView handleError(HttpServletRequest req, ConstraintViolationException e, Model model) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            strBuilder.append(violation.getMessage()).append("<br>");
        }

        //==== 处理时重新加载页面所需数据 ====
        List<DiscountVO> list = discountService.getAll();
        model.addAttribute("discountListData", list); // for select_page.html 下拉選單
        String message = strBuilder.toString();
        return new ModelAndView("back-end/discount/select_page", "errorMessage", "請修正以下錯誤:<br>" + message);
    }
}
