package com.counter.controller;

import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/counter")
public class CounterLogoutController {

    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        // 清除session中的用户信息
    	session.removeAttribute("counter");
        
        // 添加一個消息給用戶
        redirectAttributes.addFlashAttribute("message", "您已成功登出。");
        
        // 重定向到登錄頁面
        return "redirect:/counter/login";
    }
    
    @GetMapping("/logout")
    public String getLogout() {
        // GET請求的登出處理，通常不會用到，但為了安全起見，將GET請求重定向到登錄頁面
        return "redirect:/counter/login";
    }
}
