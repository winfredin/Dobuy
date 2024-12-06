package com.auth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.auth.model.AuthService;
import com.auth.model.AuthVO;


@Controller
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	AuthService authSvc;
	
	@GetMapping("addAuth")
	public String addAuth(ModelMap model) {
		AuthVO authVO = new AuthVO();
		model.addAttribute("authVO", authVO);
		return "back-end/manager/addAuth";
	}
	@PostMapping("insert")
	public String insert(@Valid AuthVO authVO, BindingResult result, ModelMap model)
			 throws IOException {
		// EmpService empSvc = new EmpService();
		authSvc.addAuth(authVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<AuthVO> list = authSvc.getAll();
		model.addAttribute("authListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/manager/listAllAuth"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
	}
	@PostMapping("getAuth_For_Update")
	public String getAuth_For_Update(@RequestParam("authNo") String authNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService();
		AuthVO authVO = authSvc.getOneAuth(Integer.valueOf(authNo));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("authVO", authVO);
		return "back-end/manager/update_auth_input"; // 查詢完成後轉交update_emp_input.html
	}
	@PostMapping("update")
	public String update(@Valid AuthVO authVO, BindingResult result, ModelMap model) 
			throws IOException {
		authSvc.updateAuth(authVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		authVO = authSvc.getOneAuth(Integer.valueOf(authVO.getAuthNo()));
		model.addAttribute("authVO", authVO);
		return "redirect:/manager/listAllAuth";
	}
	
	@PostMapping("delete")
	public String delete(@RequestParam("authNo") String authNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		// EmpService empSvc = new EmpService();
		authSvc.deleteAuth(Integer.valueOf(authNo));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<AuthVO> list = authSvc.getAll();
		model.addAttribute("authListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "redirect:/manager/listAllAuth"; // 刪除完成後轉交listAllEmp.html
	}
	
	
}

