package com.manager.filter;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.manager.model.ManagerService;
import com.manager.model.ManagerVO;
import com.managerauth.model.ManagerAuthService;
import com.managerauth.model.ManagerAuthVO;

@Controller
public class LoginController {

    @Autowired
    private ManagerService managerSvc;
    @Autowired
    private ManagerAuthService managerAuthSvc;

    // 顯示登入頁面
    @GetMapping("/login/Login")
    public String showLoginPage() {
        return "back-end/login/Login";  // 顯示登入頁面，請確認 Login.html 存在於 resources/templates/login/
    }

    // 處理登入邏輯
    @PostMapping("/login/Login")
    public String login(@RequestParam("managerAccount") String managerAccount,
                        @RequestParam("managerPassword") String managerPassword,
                        HttpSession session, Model model) {
    	
    	if(managerAccount.trim()=="" || managerPassword=="") {
    		model.addAttribute("error", "帳號或密碼不得為空");
            return "back-end/login/Login";  
    	}
        
        ManagerVO managerVO = managerSvc.getAP(managerAccount, managerPassword);
    if(managerVO!=null) {
        if (managerAccount.equals(managerVO.getManagerAccount())&& managerPassword.equals(managerVO.getManagerPassword()) ) {
            // 登入成功，將管理者名稱存入 Session
        	session.setAttribute("managerNo", managerVO.getManagerNo());
            session.setAttribute("managerAccount", managerVO.getManagerAccount());
           session.setAttribute("auth", managerVO.getAuths());
            return "redirect:/back-end-homepage";  // 登入成功後，重定向到 Dashboard 頁面
        } 
    } 
        // 登入失敗，顯示錯誤訊息
        model.addAttribute("error", "帳號或密碼錯誤");
        return "back-end/login/Login";  
    
   
    }
    // 登出功能
    @GetMapping("logout")
    public String logout(HttpSession session) {
        session.removeAttribute("managerNo"); 
        session.removeAttribute("managerAccount");
        session.removeAttribute("auth");
        return "redirect:/login/Login";  // 登出後，重新導向到登入頁面
    }
}
