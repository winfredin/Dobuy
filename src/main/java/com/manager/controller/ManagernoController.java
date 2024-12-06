package com.manager.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.auth.model.AuthService;
import com.auth.model.AuthVO;
import com.manager.model.ManagerService;
import com.manager.model.ManagerVO;
import com.managerauth.model.ManagerAuthService;
import com.managerauth.model.ManagerAuthVO;


@Controller
@Validated
@RequestMapping("/manager")
public class ManagernoController {
	
	@Autowired
	ManagerService managerSvc;
	@Autowired
    ManagerAuthService managerAuthSvc;
	@Autowired
	AuthService authSvc;
	/*
	 * This method will be called on select_page.html form submission, handling POST
	 * request It also validates the user input
	 */
	@GetMapping("manager")
	public String getManager_For_Display(
		/***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
		@NotEmpty(message="管理員編號: 請勿空白")
		@RequestParam("managerNo") String managerno,HttpSession session,
		ModelMap model) {
		
	
		List<AuthVO> alist = authSvc.getAll();
		ManagerVO managerVO = managerSvc.getOneManager(Integer.valueOf(managerno));
		List<ManagerVO> list = managerSvc.getAll();
		model.addAttribute("managerListData", list);     // for select_page.html 第97 109行用
		List<ManagerAuthVO> one = managerAuthSvc.getOne(Integer.valueOf(managerno));
    	
		if (managerVO == null) {
			model.addAttribute("errorMessage", "查無資料");
			return "back-end/manager/select_page";
		}
		
		/***************************3.查詢完成,準備轉交(Send the Success view)*****************/
		model.addAttribute("one",one);
		model.addAttribute("managerVO", managerVO);
		model.addAttribute("getOne_For_Display", "true"); // 旗標getOne_For_Display見select_page.html的第156行 -->
		model.addAttribute("alist",alist);
		return "back-end/manager/manager";  // 查詢完成後轉交listOneEmp.html
//		return "back-end/manager/select_page"; // 查詢完成後轉交select_page.html由其第158行insert listOneEmp.html內的th:fragment="listOneEmp-div
	}
	
}