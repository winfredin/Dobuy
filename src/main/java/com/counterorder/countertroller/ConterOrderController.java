package com.counterorder.countertroller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.counterorder.model.CounterOrderService;
import com.counterorder.model.CounterOrderVO;


@Controller
@RequestMapping("/counterorder")
public class ConterOrderController {

	@Autowired
	CounterOrderService counterOrderSvc;



	/*
	 * This method will serve as addEmp.html handler.
	 */
	@GetMapping("addCounterOrder")
	public String addEmp(ModelMap model) {
		CounterOrderVO counterOrderVO = new CounterOrderVO();
		model.addAttribute("counterOrderVO", counterOrderVO);
		return "vendor-end/counterorder/addCounterOrder";
	}

	/*
	 * This method will be called on addEmp.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("insert")
	public String insert(@Valid CounterOrderVO counterOrderVO, BindingResult result, ModelMap model
			) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		counterOrderVO.setOrderStatus(1);
		/*************************** 2.開始新增資料 *****************************************/
		// EmpService empSvc = new EmpService();
		counterOrderSvc.addCounterOrder(counterOrderVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<CounterOrderVO> list = counterOrderSvc.getAll();
		model.addAttribute("counterOrderListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/counterorder/listAllCounterOrder"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("counterOrderNo") String counterOrderNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService();
		CounterOrderVO counterOrderVO = counterOrderSvc.getOneCounterOrder(Integer.valueOf(counterOrderNo));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("counterOrderVO",counterOrderVO);
		return "vendor-end/counterorder/update_counterorder_input"; // 查詢完成後轉交update_emp_input.html
	}

	/*
	 * This method will be called on update_emp_input.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("update")
	public String update(@Valid CounterOrderVO counterOrderVO, BindingResult result, ModelMap model
			) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		
		/*************************** 2.開始修改資料 *****************************************/
		
		counterOrderSvc.updateCounterOrder(counterOrderVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		counterOrderVO = counterOrderSvc.getOneCounterOrder(Integer.valueOf(counterOrderVO.getCounterOrderNo()));
		model.addAttribute("counterOrderVO", counterOrderVO);
		return "vendor-end/counterorder/listOneCounterOrder"; // 修改成功後轉交listOneEmp.html
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("delete")
	public String delete(@RequestParam("counterOrderNo") String counterOrderNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		// EmpService empSvc = new EmpService();
		counterOrderSvc.deleteCounterOrder(Integer.valueOf(counterOrderNo));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<CounterOrderVO> list = counterOrderSvc.getAll();
		model.addAttribute("counterOrderListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "vendor-end/counterorder/listAllCounterOrder"; // 刪除完成後轉交listAllEmp.html
	}

	/*
	 * 第一種作法 Method used to populate the List Data in view. 如 : 
	 * <form:select path="deptno" id="deptno" items="${deptListData}" itemValue="deptno" itemLabel="dname" />
	 */

	/*
	 * 【 第二種作法 】 Method used to populate the Map Data in view. 如 : 
	 * <form:select path="deptno" id="deptno" items="${depMapData}" />
	 */


	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(CounterOrderVO counterOrderVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
				.collect(Collectors.toList());
		result = new BeanPropertyBindingResult(counterOrderVO, "counterOrderVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}
	
	/*
	 * This method will be called on select_page.html form submission, handling POST request
	 */


}