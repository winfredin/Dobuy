package com.counter.controller;

import java.io.IOException;

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
import com.counter.model.CounterService;
import com.counter.model.CounterVO;
import com.counterType.model.CounterTypeService;
import com.counterType.model.CounterTypeVO;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/counter")
public class CounterController {

    @Autowired
    CounterService counterSvc;
//
    @Autowired
    CounterTypeService counterTypeSvc;

    /*
     * This method will serve as addCounter.html handler.
     */
    @GetMapping("addCounter")
    public String addCounter(ModelMap model) {
        CounterVO counterVO = new CounterVO();
        model.addAttribute("counterVO", counterVO);
        return "vendor-end/counter/addCounter";
    }

    /*
     * This method will be called on addCounter.html form submission, handling POST request It also validates the user input
     */

    @PostMapping("insert")
    public String insert(
            @Valid CounterVO counterVO, 
            BindingResult result, 
            ModelMap model, 
            @RequestParam(value = "counterPic", required = false) MultipartFile[] parts) throws IOException {

        // 檢查櫃位帳號和統一編號是否已存在

        
        if (counterSvc.isCounterUbnExists(counterVO.getCounterUbn()) ) {
            model.addAttribute("ubnError", "櫃位統一編號已存在，請使用其他編號。");
//            return "counter/addCounter";
        }
        if (counterSvc.isCounterAccountExists(counterVO.getCounterAccount())) {
            model.addAttribute("accountError", "櫃位帳號已存在，請使用其他帳號。");
//            return "counter/addCounter";
        }
        
        // 移除與圖片相關的字段錯誤（如果有）
        result = removeFieldError(counterVO, result, "counterPic");

        // 圖片處理：允許圖片為空值
        if (parts != null && parts.length > 0 && !parts[0].isEmpty()) {
            MultipartFile multipartFile = parts[0];
            counterVO.setCounterPic(multipartFile.getBytes());
        }

        // 如果存在任何錯誤訊息，返回表單頁面
        if (model.containsAttribute("accountError") || model.containsAttribute("ubnError") || result.hasErrors()) {
            return "vendor-end/counter/addCounter";
        }
        // 開始新增資料
        counterSvc.addCounter(counterVO);

        // 返回成功頁面
        model.addAttribute("success", "櫃位資料新增成功！");
        return "redirect:/counter/allcounter";
    }
    
	@GetMapping("/login")
	public String showLoginPage(Model model) {
		model.addAttribute("loginForm", new CounterVO());
		return "vendor-end/counter/CounterLogin"; // 指向 Thymeleaf 模板路径
	}

    /*
     * This method will be called on listAllCounter.html form submission, handling POST request
     */
    @PostMapping("getOne_For_Update")
    public String getOne_For_Update(@RequestParam("counterNo") Integer counterNo, ModelMap model) {
        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        /*************************** 2.開始查詢資料 *****************************************/
        CounterVO counterVO = counterSvc.getOneCounter(counterNo);

        /*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
        model.addAttribute("counterVO", counterVO);
        return "vendor-end/counter/update_counter_input"; // 查詢完成後轉交update_counter_input.html
    }
    
    

    /*
     * This method will be called on update_counter_input.html form submission, handling POST request It also validates the user input
//     */
//    @PostMapping("update")
//    public String update(@Valid CounterVO counterVO, BindingResult result, ModelMap model,
//                         @RequestParam("upFiles") MultipartFile[] parts) throws IOException {
//
//        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
//        // 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
//        result = removeFieldError(counterVO, result, "upFiles");
//
//        if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
//            byte[] counterPic = counterSvc.getOneCounter(counterVO.getCounterNo()).getCounterPic();
//            counterVO.setCounterPic(counterPic);
//        } else {
//            for (MultipartFile multipartFile : parts) {
//                byte[] counterPic = multipartFile.getBytes();
//                counterVO.setCounterPic(counterPic);
//            }
//        }
//        if (result.hasErrors()) {
//            return "back-end/counter/update_counter_input";
//        }
//        /*************************** 2.開始修改資料 *****************************************/
//        counterSvc.updateCounter(counterVO);
//
//        /*************************** 3.修改完成,準備轉交(Send the Success view) **************/
//        model.addAttribute("success", "- (修改成功)");
//        counterVO = counterSvc.getOneCounter(counterVO.getCounterNo());
//        model.addAttribute("counterVO", counterVO);
//        return "back-end/counter/listOneCounter"; // 修改成功後轉交listOneCounter.html
//    }

    /*
     * This method will be called on listAllCounter.html form submission, handling POST request
     */
    @PostMapping("update")
    public String update(
            @Valid CounterVO counterVO, 
            BindingResult result, 
            ModelMap model, 
            @RequestParam(value = "counterPic", required = false) MultipartFile[] parts) throws IOException {

        // 移除圖片相關字段的錯誤
        result = removeFieldError(counterVO, result, "counterPic");

        // 圖片處理：允許圖片為空值
        if (parts != null && parts.length > 0 && !parts[0].isEmpty()) {
            try {
                counterVO.setCounterPic(parts[0].getBytes());
            } catch (IOException e) {
                model.addAttribute("error", "圖片處理時發生錯誤，請重試！");
                return "vendor-end/counter/update_counter_input";
            }
        } else {
            // 保留原始圖片
            CounterVO existingCounter = counterSvc.getOneCounter(counterVO.getCounterNo());
            counterVO.setCounterPic(existingCounter.getCounterPic());
        }
        // 如果驗證有錯誤，返回輸入頁面
        if (result.hasErrors()) {
            return "vendor-end/counter/update_counter_input";
        }

        try {
            // 嘗試更新資料
            counterSvc.updateCounter(counterVO);
        } catch (IllegalArgumentException ex) {
            // 捕獲自訂錯誤並返回錯誤訊息
            model.addAttribute("error", ex.getMessage());
            return "vendor-end/counter/update_counter_input";
        }

        // 返回成功頁面
        model.addAttribute("success", "櫃位資料修改成功！");
        counterVO = counterSvc.getOneCounter(counterVO.getCounterNo());
        return "redirect:/counter/allcounter";
    }
    

    
    
    
	public BindingResult removeFieldError(CounterVO counterVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
				.collect(Collectors.toList());
		result = new BeanPropertyBindingResult(counterVO, "counterVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}

    /*
     * This method will be called on select_page.html form submission, handling POST request
     */
	@ModelAttribute("counterTypeListData")
	protected List<CounterTypeVO> referenceListData() {
		// DeptService deptSvc = new DeptService();
		List<CounterTypeVO> list = counterTypeSvc.getAll();
		return list;
	}
	
	
	
    @PostMapping("listCounters_ByCompositeQuery")
    public String test(HttpServletRequest req, Model model) {
//        Map<String, String[]> map = req.getParameterMap();
        List<CounterVO> list = counterSvc.getAll();
        model.addAttribute("counterListData", list);
        return "/counter/allCounter";
    }
}