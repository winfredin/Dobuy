package com.used.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.used.model.UsedService;
import com.used.model.UsedVO;
import com.usedpic.model.UsedPicService;
import com.usedpic.model.UsedPicVO;

//Chapt2-65 ch3-77 ch8-139
@Controller
@Validated
@RequestMapping("/used")
public class UsedController {
	
	@Autowired
	UsedService usedSvc;

	@Autowired
	UsedPicService usedPicSvc;
	
	
	/*
	 * This method will serve as addUsed.html handler.
	 */
	@GetMapping("addUsed")
	public String addUsed(ModelMap model) {
		UsedVO usedVO = new UsedVO();
		model.addAttribute("usedVO", usedVO);
		return "back-end/used/addUsed";
	}

	/*
	 * This method will be called on addUsed.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("insert")
	public String insert(@Valid UsedVO usedVO, BindingResult result, ModelMap model,
			@RequestParam("upFiles") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(usedVO, result, "upFiles");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
			
			model.addAttribute("errorMessage", "商品照片: 請上傳照片");
			
		} else {
			//new新物件用於放置新的照片
			List<UsedPicVO> usedPicsList = new ArrayList<UsedPicVO>();
			UsedPicVO usedPicVO = new UsedPicVO();
			//跑迴圈 將每張照片設定 並放進list<UsedPic>
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				usedPicVO.setUsedVO(usedVO);
				usedPicVO.setUsedPicName(multipartFile.getOriginalFilename());
				usedPicVO.setUsedPics(buf);
				usedPicsList.add(usedPicVO);
			}
			usedVO.setUsedPics(usedPicsList);
		}
		
		if (result.hasErrors() || parts[0].isEmpty()) {
			return "back-end/used/addUsed";
		}
		
		/*************************** 2.開始新增資料 *****************************************/
		// UsedService UsedSvc = new UsedService();
		usedSvc.addUsed(usedVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<UsedVO> list = usedSvc.getAll();
		model.addAttribute("UsedListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/used/listAllUsed"; // 新增成功後重導至IndexController_inSpringBoot.java的第50行@GetMapping("/Used/listAllUsed")
	}

	/*
	 * This method will be called on listAllUsed.html form submission, handling POST request
	 */
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
			return "back-end/used/select_page";
		}

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("usedVO", usedVO);
		return "back-end/used/update_used_input"; // 查詢完成後轉交update_Used_input.html
	}

	/*
	 * This method will be called on update_Used_input.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("update")
	public String update(@Valid UsedVO usedVO, BindingResult result, ModelMap model,
			@RequestParam("usedPics") MultipartFile[] parts) throws ConstraintViolationException, IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(usedVO, result, "usedPics");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時 提取出舊有圖片
			// UsedService UsedSvc = new UsedService();
			List<UsedPicVO> oldUsedPics = usedSvc.getOneUsed(usedVO.getUsedNo()).getUsedPics();
			usedVO.setUsedPics(oldUsedPics);
		} else {
			//將新增的圖片跑迴圈後塞入list以便轉交
			List<UsedPicVO> newUsedPics = new ArrayList<UsedPicVO>();
			UsedPicVO usedPicVO = new UsedPicVO();
			usedPicVO.setUsedVO(usedVO);
			for (MultipartFile multipartFile : parts) {
				byte[] upFiles = multipartFile.getBytes();
				usedPicVO.setUsedPics(upFiles);
				usedPicVO.setUsedPicName(multipartFile.getOriginalFilename());
				newUsedPics.add(usedPicVO);
			}
			usedVO.setUsedPics(newUsedPics);
		}
		
		if (result.hasErrors()) {
			return "back-end/used/update_used_input";
		}
		/*************************** 2.開始修改資料 *****************************************/
		// UsedService UsedSvc = new UsedService();
		//先刪除舊有圖片 之後再新增
		usedPicSvc.deleteUsedPic(usedVO.getUsedNo());
		Integer usedNo=usedSvc.updateUsed(usedVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		usedVO = usedSvc.getOneUsed(usedNo);
		model.addAttribute("usedVO", usedVO);
		return "back-end/used/listOneUsed"; // 修改成功後轉交listOneUsed.html
	}

	/*
	 * This method will be called on listAllUsed.html form submission, handling POST request
	 */
	@PostMapping("delete")
	public String delete(@RequestParam("usedNo") String usedNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		// UsedService UsedSvc = new UsedService();
		usedSvc.deleteUsed(Integer.valueOf(usedNo));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<UsedVO> list = usedSvc.getAll();
		model.addAttribute("usedListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "back-end/Used/listAllUsed"; // 刪除完成後轉交listAllUsed.html
	}

	/*
	 * 第一種作法 Method used to populate the List Data in view. 如 : 
	 * <form:select path="usedPicno" id="usedPicno" items="${usedPicListData}" itemValue="usedPicno" itemLabel="dname" />
	 */
//	@ModelAttribute("usedPicListData")
//	protected List<UsedPicVO> referenceListData() {
//		// usedPicService usedPicSvc = new usedPicService();
//		List<UsedPicVO> list = usedPicSvc.getAll();
//		return list;
//	}

	/*
	 * 【 第二種作法 】 Method used to populate the Map Data in view. 如 : 
	 * <form:select path="usedPicno" id="usedPicno" items="${depMapData}" />
	 */
//	@ModelAttribute("usedPicMapData") //
//	protected Map<Integer, String> referenceMapData() {
//		Map<Integer, String> map = new LinkedHashMap<Integer, String>();
//		map.put(10, "財務部");
//		map.put(20, "研發部");
//		map.put(30, "業務部");
//		map.put(40, "生管部");
//		return map;
//	}
//
	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(UsedVO usedVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
				.collect(Collectors.toList());
		result = new BeanPropertyBindingResult(usedVO, "Used");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}
	
	@ExceptionHandler(value = { ConstraintViolationException.class })
	//@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ModelAndView handleError(HttpServletRequest req,ConstraintViolationException e,Model model) {
	    Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
	    StringBuilder strBuilder = new StringBuilder();
	    for (ConstraintViolation<?> violation : violations ) {
	          strBuilder.append(violation.getMessage() + "<br>");
	    }
	    //==== 以下第92~96行是當前面第77行返回 /src/main/resources/templates/back-end/emp/select_page.html用的 ====   
//	    model.addAttribute("empVO", new EmpVO());
//    	EmpService empSvc = new EmpService();
//		List<UsedVO> list = usedSvc.getAll();
//		model.addAttribute("usedListData", list);     // for select_page.html 第97 109行用	
		String message = strBuilder.toString();
	    return new ModelAndView("back-end/used/update_used_input", "errorMessage", "請修正以下錯誤:<br>"+message);
	}
	
//	@GetMapping("/") //Chapt2-65 ch3-77 ch8-139
//	public String myMethod() {
//		return "index1"; //-->\src\main\resources\tUsedlates\index1.html 
//		//--> p137 265 //不要加副檔名  會抱錯 500
//	}
//	@GetMapping("/index2") //Chapt2-65 ch3-77 ch8-139
//	public String myMethod2() {
//		return "index2"; //-->\src\main\resources\tUsedlates\index1.html 
//		//--> p137 265 //不要加副檔名  會抱錯 500
//	}
//	@GetMapping("/index3") //Chapt2-65 ch3-77 ch8-139
//	@ResponseBody //變成網頁輸出
//	public String myMethod3() {
//		return "<font color=red><b>.......index3........</b></font><br>"
//				+ "<img src=\"images/曹操.jpg\">"; //-->\src\main\resources\tUsedlates\index1.html 
//		//--> p137 265 //不要加副檔名  會抱錯 500
//	}
	
}
