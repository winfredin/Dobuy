package com.counterorderdetail.controller;

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

import com.counterorderdetail.model.CounterOrderDetailVO;
import com.counterorderdetail.model.CounterOrderDetailService;

@Controller
@Validated
@RequestMapping("/counterOrderDetail")
public class CounterOrderDetailNoController {

   @Autowired
   CounterOrderDetailService counterOrderDetailService;

   /*
    * 處理查詢單筆訂單明細的請求，包含輸入驗證功能
    * 當用戶從選擇頁面提交表單時被呼叫
    */
   @PostMapping("getOne_For_Display")
   public String getOne_For_Display(
       // 參數驗證設定
       @NotEmpty(message = "訂單明細編號: 請勿空白")
       @Digits(integer = 10, fraction = 0, message = "訂單明細編號: 請填數字-請勿超過{integer}位數")
       @Min(value = 1, message = "訂單明細編號: 不能小於{value}")
       @Max(value = 9999999999L, message = "訂單明細編號: 不能超過{value}")
       @RequestParam("counterOrderDetailNo") String counterOrderDetailNo, ModelMap model) {
       
       // 開始查詢資料
       CounterOrderDetailVO counterOrderDetailVO = counterOrderDetailService.getOneCounterOrderDetail(Integer.valueOf(counterOrderDetailNo));
       
       // 取得所有訂單明細資料，用於下拉選單
       List<CounterOrderDetailVO> list = counterOrderDetailService.getAll();
       model.addAttribute("counterOrderDetailListData", list);
       
       // 若查無資料則返回錯誤訊息
       if (counterOrderDetailVO == null) {
           model.addAttribute("errorMessage", "查無資料");
           return "vendor-end/counterOrderDetail/select_page";
       }
       
       // 查詢成功，準備轉交資料
       model.addAttribute("counterOrderDetailVO", counterOrderDetailVO);
       model.addAttribute("getOne_For_Display", "true");
       return "vendor-end/counterOrderDetail/select_page";
   }

   /*
    * 處理表單提交時的驗證錯誤異常
    * 使用 ExceptionHandler 攔截 ConstraintViolationException
    */
   @ExceptionHandler(value = { ConstraintViolationException.class })
   public ModelAndView handleError(HttpServletRequest req, ConstraintViolationException e, Model model) {
       // 收集所有驗證錯誤訊息
       Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
       StringBuilder strBuilder = new StringBuilder();
       for (ConstraintViolation<?> violation : violations) {
           strBuilder.append(violation.getMessage()).append("<br>");
       }

       // 重新載入頁面所需資料
       List<CounterOrderDetailVO> list = counterOrderDetailService.getAll();
       model.addAttribute("counterOrderDetailListData", list);
       
       // 返回錯誤訊息
       String message = strBuilder.toString();
       return new ModelAndView("vendor-end/counterOrderDetail/select_page", "errorMessage", "請修正以下錯誤:<br>" + message);
   }
}