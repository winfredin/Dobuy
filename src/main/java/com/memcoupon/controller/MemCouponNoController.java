package com.memcoupon.controller;

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
import com.memcoupon.model.MemCouponVO;
import com.memcoupon.model.MemCouponService;

@Controller
@Validated
@RequestMapping("/memcoupon")
public class MemCouponNoController {

    @Autowired
    MemCouponService memCouponSvc;

    /*
     * This method will be called on select_page.html form submission, handling POST request
     * It also validates the user input
     */
    @PostMapping("getOne_For_Display")
    public String getOne_For_Display(
        /***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
        @NotEmpty(message = "會員優惠券編號: 請勿空白")
        @Digits(integer = 5, fraction = 0, message = "會員優惠券編號: 請填數字-請勿超過{integer}位數")
        @Min(value = 1, message = "會員優惠券編號: 不能小於{value}")
        @Max(value = 99999, message = "會員優惠券編號: 不能超過{value}")
        @RequestParam("memCouponNo") String memCouponNo, ModelMap model) {

        /***************************2.開始查詢資料*********************************************/
        MemCouponVO memCouponVO = memCouponSvc.getOneMemCoupon(Integer.valueOf(memCouponNo));

        List<MemCouponVO> list = memCouponSvc.getAll();
        model.addAttribute("memCouponListData", list); // for select_page.html 下拉選單
        if (memCouponVO == null) {
            model.addAttribute("errorMessage", "查無資料");
            return "vendor-end/memcoupon/select_page";
        }

        /***************************3.查詢完成,準備轉交(Send the Success view)*****************/
        model.addAttribute("memCouponVO", memCouponVO);
        model.addAttribute("getOne_For_Display", "true"); // Flag for displaying specific data
        return "vendor-end/memcoupon/select_page";
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
        List<MemCouponVO> list = memCouponSvc.getAll();
        model.addAttribute("memCouponListData", list); // for select_page.html 下拉選單
        String message = strBuilder.toString();
        return new ModelAndView("vendor-end/memcoupon/select_page", "errorMessage", "請修正以下錯誤:<br>" + message);
    }
}
