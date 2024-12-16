package com.counterorder.countertroller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.counter.model.CounterVO;
import com.counterorder.model.CounterOrderService;
import com.counterorder.model.CounterOrderVO;
import com.msg.model.MsgService;



//@PropertySource("classpath:application.properties") // 於https://start.spring.io建立Spring Boot專案時, application.properties文件預設已經放在我們的src/main/resources 目錄中，它會被自動檢測到
@Controller
public class CounterIndexController {
	
	// @Autowired (●自動裝配)(Spring ORM 課程)
	// 目前自動裝配了EmpService --> 供第66使用
	@Autowired
	CounterOrderService counterOrderSvc;
	
    @Autowired
    MsgService msgSvc;
	

	
    // inject(注入資料) via application.properties
    @Value("${welcome.message}")
    private String message;
	
    private List<String> myList = Arrays.asList("Spring Boot Quickstart 官網 : https://start.spring.io", "IDE 開發工具", "直接使用(匯入)官方的 Maven Spring-Boot-demo Project + pom.xml", "直接使用官方現成的 @SpringBootApplication + SpringBootServletInitializer 組態檔", "依賴注入(DI) HikariDataSource (官方建議的連線池)", "Thymeleaf", "Java WebApp (<font color=red>快速完成 Spring Boot Web MVC</font>)");
//    @GetMapping("/")
//    public String index(Model model) {
//    	model.addAttribute("message", message);
//        model.addAttribute("myList", myList);
//        return "index"; //view
//    }
    
    // http://......../hello?name=peter1
//    @GetMapping("/hello")
//    public String indexWithParam(
//            @RequestParam(name = "name", required = false, defaultValue = "") String name, Model model) {
//        model.addAttribute("message", name);
//        return "index"; //view
//    }
    @PostMapping("updateOrder")
	public String updateOrderStatus(@RequestParam("orderStatus") Integer orderStatus,
	                                  @RequestParam("counterOrderNo") Integer counterOrderNo) {
	
		counterOrderSvc.updateCounterStatus(counterOrderNo, orderStatus);
	    return "redirect:/counter/listOneCounter"; 
	}
    
    @PostMapping("memOrder")
	public String memOrderStatus(@RequestParam("counterOrderNo") Integer counterOrderNo,
	                                  HttpSession session) {
    	
    	
    	  counterOrderSvc.updateCounterStatus(counterOrderNo, 2);
		

	    return "redirect:/member"; 
	}
  
    //=========== 以下第63~75行是提供給 /src/main/resources/templates/back-end/emp/select_page.html 與 listAllEmp.html 要使用的資料 ===================   
    @GetMapping("/counterorder/select_page")
	public String select_page(Model model) {
		return "vendor-end/counterorder/select_page";
	}
    
    @GetMapping("/counter/listAllCounterOrder")
	public String listAllCounter(HttpSession session,Model model) {
    	//任國櫃位管理
    	CounterVO counter = (CounterVO) session.getAttribute("counter");
        if (counter == null) {
            return "redirect:/counter/login";
        }
    	List<CounterOrderVO> alist = counterOrderSvc.getAllOrdersByCounter(counter.getCounterNo());
		model.addAttribute("alist",alist);
        model.addAttribute("counter", counter);
        model.addAttribute("msgSvc", msgSvc);
		return "vendor-end/counterorder/listAllCounterOrder";
	}
    @GetMapping("/counter/listOneCounter")
    public String listOneCounter(HttpSession session,Model model) {
    	CounterVO counter = (CounterVO) session.getAttribute("counter");
        if (counter == null) {
            return "redirect:/counter/login";
        }
        List<CounterOrderVO> alist=counterOrderSvc.getAllOrdersByCounter((Integer) session.getAttribute("counterNo"));
        model.addAttribute("alist",alist);
        model.addAttribute("counter", counter);
        model.addAttribute("msgSvc", msgSvc);
        return "vendor-end/counterorder/listOneCounterOrder";
    }
    
    @ModelAttribute("counterOrderListData")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
	protected List<CounterOrderVO> referenceListData(Model model) {
		
    	List<CounterOrderVO> list = counterOrderSvc.getAll();
		return list;
	}
    
	

}