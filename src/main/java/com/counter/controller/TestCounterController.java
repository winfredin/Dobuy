package com.counter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.counter.model.CounterService;
import com.counter.model.CounterVO;
import com.counterType.model.CounterTypeService;
import com.counterType.model.CounterTypeVO;

import java.util.*;

@Controller //ch2 P65 ch3 P77 ch8 p139
public class TestCounterController {
	
	@Autowired
	CounterService counterSvc;
	
	@Autowired
	CounterTypeService counterTyperSvc;
	
	@GetMapping("counter/test") //ch2 P65 ch3 P77 ch8 p139
	public String myMethod() {
		return "vendor-end/counter/index1"; //檔案位置 src\main\resources\templates\index1.html  //P137  
	}
	
	@GetMapping("counter/index2") //ch2 P65 ch3 P77 ch8 p139
	public String myMethod2() {
		return "vendor-end/counter/index2"; //檔案位置 src\main\resources\templates\index2.html  //P137  
	}
	
	@GetMapping("counter/index3") //ch2 P65 ch3 P77 ch8 p139
	@ResponseBody
	public String myMethod3() {
		return "<font color=green><b> index3 ?????   <b></font>";   
	}
	
	@GetMapping("/counter/allcounter") //ch2 P65 ch3 P77 ch8 p139
	public String test(Model model) {
		return "vendor-end/counter/allcounter"; //檔案位置 src\main\resources\templates\counter\allcounter  //P137  
	}
	
	   @ModelAttribute("counterListData")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
		protected List<CounterVO> referenceListData(Model model) {
			
	    	List<CounterVO> list = counterSvc.getAll();
			return list;
		}
	   
	   @ModelAttribute("counterTypeListData") // for select_page.html 第135行用
	   protected List<CounterTypeVO> referenceListData_CounterType(Model model) {
	       model.addAttribute("counterTypeVO", new CounterTypeVO()); // for select_page.html 第133行用
	       List<CounterTypeVO> list = counterTyperSvc.getAll(); // 假設 counterTypeSvc 是處理 CounterType 的服務
	       return list;
	   }

}
