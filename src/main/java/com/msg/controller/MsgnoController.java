package com.msg.controller;

import javax.servlet.http.HttpServletRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;


import java.util.List;
import java.util.Set;
import com.msg.model.MsgService;
import com.msg.model.MsgVO;

@Controller
@Validated
@RequestMapping("/msg")
public class MsgnoController {
    
    @Autowired
    MsgService msgSvc;

    /*
     * 這個方法會在提交 select_page.html 表單時被調用，處理 POST 請求並驗證用戶輸入。
     */
    @PostMapping("getOne_For_Display")
    public String getOne_For_Display(
        /***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
        @NotEmpty(message="訊息編號: 請勿空白")
        @Digits(integer = 4, fraction = 0, message = "訊息編號: 請填數字-請勿超過{integer}位數")
        @RequestParam("counterInformNo") String counterInformNo,
        ModelMap model) {
        
        /***************************2.開始查詢資料*********************************************/
        MsgVO msgVO = msgSvc.getOneMsg(Integer.valueOf(counterInformNo));
        
        List<MsgVO> list = msgSvc.getAll();
        model.addAttribute("msgListData", list); // 為 select_page.html 提供數據
        
        if (msgVO == null) {
            model.addAttribute("errorMessage", "查無資料");
            return "back-end/msg/select_page";
        }
        
        /***************************3.查詢完成,準備轉交(Send the Success view)*****************/
        model.addAttribute("msgVO", msgVO);
        model.addAttribute("getOne_For_Display", "true"); // 標記已顯示的訊息
        
        return "back-end/msg/select_page"; // 查詢完成後轉交select_page.html
    }

    @ExceptionHandler(value = { ConstraintViolationException.class })
    public ModelAndView handleError(HttpServletRequest req, ConstraintViolationException e, Model model) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations ) {
            strBuilder.append(violation.getMessage() + "<br>");
        }
        List<MsgVO> list = msgSvc.getAll();
        model.addAttribute("msgListData", list); // 為 select_page.html 提供數據
        
        String message = strBuilder.toString();
        return new ModelAndView("back-end/msg/select_page", "errorMessage", "請修正以下錯誤:<br>"+message);
    }
}
