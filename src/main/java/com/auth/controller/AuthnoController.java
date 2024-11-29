package com.auth.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
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

import com.auth.model.AuthService;
import com.auth.model.AuthVO;




@Validated
@RequestMapping("/auth")
@Controller
public class AuthnoController {
	
	@Autowired
	AuthService authSvc;
	


	/*
	 * This method will be called on select_page.html form submission, handling POST
	 * request It also validates the user input
	 */
	@PostMapping("getAuth_For_Display")
	public String getAuth_For_Display(
		/***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
		@NotEmpty(message="權限編號: 請勿空白")
		@RequestParam("authNo") String authNo,
		ModelMap model) {
		
		System.out.println("Received authNo: " + authNo); 
		/***************************2.開始查詢資料*********************************************/
//		EmpService empSvc = new EmpService();
		AuthVO authVO = authSvc.getOneAuth(Integer.valueOf(authNo));
		
		List<AuthVO> list = authSvc.getAll();
		model.addAttribute("authListData", list);     // for select_page.html 第97 109行用

		if (authVO == null) {
			model.addAttribute("errorMessage", "查無資料");
			return "back-end/manager/select_page";
		}
		
		/***************************3.查詢完成,準備轉交(Send the Success view)*****************/
		model.addAttribute("authVO", authVO);
		model.addAttribute("getAuth_For_Display", "true"); // 旗標getOne_For_Display見select_page.html的第156行 -->
		
		return "back-end/manager/listOneAuth";  // 查詢完成後轉交listOneEmp.html
//		return "back-end/manager/select_page"; // 查詢完成後轉交select_page.html由其第158行insert listOneEmp.html內的th:fragment="listOneEmp-div
	}
	
}