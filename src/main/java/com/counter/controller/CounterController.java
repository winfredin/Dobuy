package com.counter.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import com.counter.model.CounterService;
import com.counter.model.CounterVO;
import com.counter.model.CounterVO.RegisterGroup;
import com.counter.model.CounterVO.UpdateGroup;
import com.counterType.model.CounterTypeService;
import com.counterType.model.CounterTypeVO;



//test test

@Controller
@RequestMapping("/counter")
public class CounterController {

    @Autowired
    CounterService counterSvc;
//
    @Autowired
    CounterTypeService counterTypeSvc;
    
    
    //註冊成功後發信
    @Autowired
    private JavaMailSender mailSender;

    private void sendEmailtoC(String emailTo) {
    	  try {
    	        MimeMessage message = mailSender.createMimeMessage();
    	        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
    	        helper.setFrom("rin020255@gmail.com");
    	        helper.setTo(emailTo);
    	        helper.setSubject("DoBuy櫃位註冊成功確認");
    	        helper.setText("<html><body>"
    	                + "<h1>您好，歡迎加入DoBuy！</h1>"
    	                + "<p>您已經成功註冊成為DoBuy的櫃位。</p>"
    	                + "<p>感謝您加入我們的大家庭！</p>"
    	                + "<p>請<a href='http://localhost:8080/counter/login'>點擊這裡</a>進行登錄的操作。</p>"
    	                + "</body></html>", true);
    	        
    	        mailSender.send(message);
    	    } catch (MessagingException e) {
    	        e.printStackTrace();
    	        // 你可能想要在這裡添加更多的錯誤處理
    	    }
    	}
    
    

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
    		@Validated(RegisterGroup.class)  @ModelAttribute CounterVO counterVO, 
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
        
     // 發送郵件
        try {
        	sendEmailtoC(counterVO.getCounterEmail());
        } catch (Exception e) {
            // 處理發送郵件失敗的情況
            model.addAttribute("error", "發送確認郵件時發生錯誤，請稍後再試。");
        }
        

        // 返回成功頁面
//        model.addAttribute("success", "櫃位資料新增成功！");
        return "vendor-end/counter/counterRegisterSuccess";
    }
    

    
	@GetMapping("/login")
	public String showLoginPage(Model model) {
		model.addAttribute("loginForm", new CounterVO());
		return "vendor-end/counter/CounterLogin"; // 指向 Thymeleaf 模板路径
	}

    
    //櫃位資料管理
    @GetMapping("/vendor-end/manage")
    public String showCurrentCounterInfo(HttpSession session, Model model) {
        CounterVO counter = (CounterVO) session.getAttribute("counter");
        if (counter != null) {
        	 model.addAttribute("counter", counterSvc.getOneCounter(counter.getCounterNo()));
            return "vendor-end/counter/counter_management";
        } else {
            // 處理未登入的情況
            return "redirect:/counter/login";
        }
    }
    
    @PostMapping("vendor-end/getOne_For_Counter_Update")
    public String getOne_For_Counter_Update(HttpSession session ,@RequestParam("counterNo") Integer counterNo, ModelMap model) {
    	CounterVO counter = (CounterVO) session.getAttribute("counter");
    	model.addAttribute("counter", counterSvc.getOneCounter(counter.getCounterNo()));
        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        /*************************** 2.開始查詢資料 *****************************************/
        CounterVO counterVO = counterSvc.getOneCounter(counterNo);

        /*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
        model.addAttribute("counterVO", counterVO);
        return "vendor-end/counter/updateForCounter"; // 查詢完成後轉交update_counter_input.html
    }
    
    @PostMapping("vendor-end/update")
    public String CounterUpdate(
    		HttpSession session ,
    		@Validated(UpdateGroup.class) CounterVO counterVO, 
            BindingResult result,
            RedirectAttributes redirectAttributes,
            ModelMap model, 
            @RequestParam(value = "counterPic", required = false) MultipartFile[] parts) throws IOException {
    	
    	CounterVO counter = (CounterVO) session.getAttribute("counter");
    	model.addAttribute("counter", counterSvc.getOneCounter(counter.getCounterNo()));
        // 移除圖片相關字段的錯誤
        result = removeFieldError(counterVO, result, "counterPic");

        // 圖片處理：允許圖片為空值
        if (parts != null && parts.length > 0 && !parts[0].isEmpty()) {
            try {
                counterVO.setCounterPic(parts[0].getBytes());
            } catch (IOException e) {
                model.addAttribute("error", "圖片處理時發生錯誤，請重試！");
                return "vendor-end/counter/updateForCounter";
            }
        } else {
            // 保留原始圖片
            CounterVO existingCounter = counterSvc.getOneCounter(counterVO.getCounterNo());
            counterVO.setCounterPic(existingCounter.getCounterPic());
        }
        // 如果驗證有錯誤，返回輸入頁面
        if (result.hasErrors()) {
            return "vendor-end/counter/updateForCounter";
        }

        try {
            // 嘗試更新資料
            counterSvc.updateCounter(counterVO);
        } catch (IllegalArgumentException ex) {
            // 捕獲自訂錯誤並返回錯誤訊息
            model.addAttribute("error", ex.getMessage());
            return "vendor-end/counter/updateForCounter";
        }

        // 返回成功頁面
        if (!result.hasErrors()) {
        	redirectAttributes.addFlashAttribute("message", "櫃位資料修改成功！");
        }
       	 counterVO = counterSvc.getOneCounter(counterVO.getCounterNo());
       	 return "redirect:/counter/vendor-end/manage";
       
//        model.addAttribute("success", "櫃位資料修改成功！");
//        counterVO = counterSvc.getOneCounter(counterVO.getCounterNo());
//        return "redirect:/counter/vendor-end/manage";
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