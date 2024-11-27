package com.counter.controller;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.counter.model.CounterService;
import com.counter.model.CounterVO;
import com.counter.model.CounterVO.LoginGroup;

@Controller
@RequestMapping("/counter")
public class CounterLoginController {
    @Autowired
    CounterService counterService;
    
    
    @PostMapping("/loginERO")
    public String loginCheck(@Validated (LoginGroup.class) @ModelAttribute("loginForm") CounterVO counterVO, BindingResult bindingResult, Model model,
                             HttpSession session, HttpServletRequest req) {
        List<String> errorMsgs = new LinkedList<>();
        if (bindingResult.hasErrors()) {
            return "vendor-end/counter/CounterLogin";
        }

        if (!counterService.isCounterAccountExists(counterVO.getCounterAccount())) {
            errorMsgs.add("櫃位帳號不存在");
        }
        
        CounterVO authenticatedCounter = counterService.authenticate(counterVO.getCounterAccount(), counterVO.getCounterPassword());
        if (authenticatedCounter == null) {
            errorMsgs.add("帳號或密碼錯誤");
        }

        if (!errorMsgs.isEmpty()) {
            model.addAttribute("errorMsgs", errorMsgs);
            return "vendor-end/counter/CounterLogin";
        }

 
     // 登入成功，設置 session
        session.setAttribute("counter", authenticatedCounter); // 在這裡設置 Session 屬性
        
        // 檢查是否有原始請求
        String originalRequest = (String) session.getAttribute("originalRequest");
        if (originalRequest != null) {
            session.removeAttribute("originalRequest"); // 移除原始請求
            return "redirect:" + originalRequest.replaceFirst(req.getContextPath(), "");
        }

        // 如果沒有原始請求，跳轉到默認頁面
        return "redirect:/counter/Counterindex";
    }
    
    @GetMapping("/Counterindex")
    public String showIndexPage(HttpSession session, Model model) {
    	CounterVO counter = (CounterVO) session.getAttribute("counter"); // 在這裡從 Session 中獲取櫃位信息 
        if (counter != null) {
            model.addAttribute("counter", counter);
        }
        return "vendor-end/counter/Counterindex";
    }
    
}