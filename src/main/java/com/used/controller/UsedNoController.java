package com.used.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Digits;
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

import com.goodstype.model.GoodsTypeService;
import com.goodstype.model.GoodsTypeVO;
import com.used.model.UsedService;
import com.used.model.UsedVO;
import com.usedpic.model.UsedPicService;
import com.usedpic.model.UsedPicVO;

@Controller
@Validated
@RequestMapping("/used")
public class UsedNoController {
	
	@Autowired
	UsedService usedSvc;
	
	@Autowired
	GoodsTypeService goodsTypeService;
	
	@Autowired
	UsedPicService usedPicSvc;
	
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		@NotEmpty(message="二手商品編號: 請勿空白") 
		@Digits(integer = 10, fraction = 0, message = "二手商品編號: 請填數字")
		@Min(value = 1, message = "二手商品編號: 不能小於{value}")
		@RequestParam("usedNo") String usedNo, ModelMap model) {
		
		/*************************** 2.開始查詢資料 *****************************************/
		// UsedService UsedSvc = new UsedService();
		UsedVO usedVO = usedSvc.getOneUsed(Integer.valueOf(usedNo));
		if (usedVO == null) {
			model.addAttribute("errorMessage", "查無資料");
			return "front-end/normalpage/member";
		}

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		List<GoodsTypeVO> goodsTypeList= goodsTypeService.getAll();
		
		model.addAttribute("goodsTypeList", goodsTypeList);
		model.addAttribute("usedVO", usedVO);
		return "front-end/used/update_used_input"; // 查詢完成後轉交update_Used_input.html
	}
	
	@ExceptionHandler(value = { ConstraintViolationException.class })
	//@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ModelAndView handleError(HttpServletRequest req,ConstraintViolationException e,Model model) {
	    Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
	    StringBuilder strBuilder = new StringBuilder();
	    for (ConstraintViolation<?> violation : violations ) {
	          strBuilder.append(violation.getMessage() + "<br>");
	    }
	    //==== 以下第92~96行是當前面第77行返回 /src/main/resources/templates/front-end/emp/select_page.html用的 ====   
	 
		String message = strBuilder.toString();
        	
	    return new ModelAndView("front-end/used/select_page", "errorMessage", "請修正以下錯誤:<br>"+message);
        
	}

}
