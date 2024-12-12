package com.used.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.goods.model.GoodsService;
import com.goods.model.GoodsVO;
import com.goodstype.model.GoodsTypeService;
import com.goodstype.model.GoodsTypeVO;
import com.used.model.UsedService;
import com.used.model.UsedVO;
import com.usedpic.model.UsedPicService;
import com.usedpic.model.UsedPicVO;

//Chapt2-65 ch3-77 ch8-139
@Controller
@RequestMapping("/used")
public class UsedController {
	
	@Autowired
	private UsedService usedSvc;

	@Autowired
	private UsedPicService usedPicSvc;
	
	@Autowired
	private GoodsTypeService goodsTypeService;
	
	@Autowired
	private EntityManager EntityManager;
	
	@Autowired
	private GoodsService goodsService;
	
	/*
	 * This method will serve as addUsed.html handler.
	 */
	//用於一鍵轉售
	@PostMapping("/oneButtontoSellUsed")
	public String addUsed(
			@RequestParam("goodsNo") String goodsNo,
			ModelMap model,
			HttpSession session) {
		UsedVO usedVO = new UsedVO();
		 Integer goodsNoWantsSell= Integer.valueOf(goodsNo); //goodsNo
		 
		 GoodsVO goodsVO=goodsService.getOneGoods(goodsNoWantsSell);
		 usedVO.setUsedName(goodsVO.getGoodsName());
		 usedVO.setUsedProDesc(goodsVO.getGoodsDescription());
		 usedVO.setUsedPrice(goodsVO.getGoodsPrice());
		 
		 
		 
		 
		//session提取memNo為sellerNo
		 Integer sellerNo = Integer.valueOf((String)session.getAttribute("memNo"));
		usedVO.setSellerNo(sellerNo);
		
		model.addAttribute("goodsVO", goodsVO);
		model.addAttribute("usedVO", usedVO);
		List<GoodsTypeVO> goodsTypeList= goodsTypeService.getAll();
		model.addAttribute("goodsTypeList", goodsTypeList);
		return "front-end/used/addUsed";
	}
	
	@PostMapping("/getOneUsed")
	public String getOneUsed( @RequestParam("usedNo") String usedNo, Model model) {
		 
		UsedVO usedVO = usedSvc.getOneUsed(Integer.valueOf(usedNo));
		List<GoodsTypeVO> goodsTypeList= goodsTypeService.getAll();
		model.addAttribute("usedVO", usedVO);
		model.addAttribute("goodsTypeList", goodsTypeList);
		return "front-end/used/listOneUsed";
	}
	
	@GetMapping("/getOneUsedOnDetail")
	public String getOneUsedOnDetail( @RequestParam("usedNo") String usedNo, Model model) {
		 
		UsedVO usedVO = usedSvc.getOneUsed(Integer.valueOf(usedNo));

		List<GoodsTypeVO> goodsTypeList= goodsTypeService.getAll();
		
		
		model.addAttribute("usedVO", usedVO);
		model.addAttribute("goodsTypeList", goodsTypeList);

		return "front-end/used/shop_detail_used";
	}

	@PostMapping("/getSellerUsedListFragment")
    public String getUsedListFragment(HttpSession session, Model model) {
        // 從 session 中取得 memNo

        Integer memNo = Integer.valueOf((String)session.getAttribute("memNo"));

        if (memNo == null) {
            // 如果沒有 memNo，處理錯誤情況，這裡可以返回空片段或錯誤信息
            return "front-end/used/memberAllUsed :: usedListFragment";
        }


        // 根據 memNo 從資料庫中查詢二手商品列表
        List<UsedVO> usedListData = usedSvc.memberSelectBySellerNo(memNo);//測試使用2
        List<GoodsTypeVO> goodsTypeList= goodsTypeService.getAll();
        // 將數據放到模型中
        model.addAttribute("usedListData", usedListData);
        model.addAttribute("goodsTypeList", goodsTypeList);
        // 返回Fragment所在的模板，並指定Fragment名稱
        return "front-end/used/memberAllUsed :: usedListFragment";
    }
	

	//管理員搜尋所有二手商品
	@PostMapping("/getAllSellerUsedListFragment")
    public String getAllUsedListFragment( Model model) {
        
        
        List<UsedVO> usedListData = usedSvc.getAll();
        List<GoodsTypeVO> goodsTypeList= goodsTypeService.getAll();
		
        // 將數據放到模型中
        model.addAttribute("usedListData", usedListData);
        model.addAttribute("goodsTypeList", goodsTypeList);

        // 返回Fragment所在的模板，並指定Fragment名稱
        return "front-end/used/listAllUsed :: usedListFragment";
    }

	
	

	/*
	 * This method will be called on addUsed.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("/insert")
	public String insert(
	        @Valid UsedVO usedVO,
	        BindingResult result,
	        ModelMap model,
	        @RequestParam("upfiles") MultipartFile[] parts,
	        RedirectAttributes redirectAttributes,HttpSession session) throws IOException {

		
		List<MultipartFile> validPictures=filterEmptyFiles(parts);
							
		usedVO.setSellerNo(Integer.valueOf((String)session.getAttribute("memNo")));
		
	    if (result.hasErrors() || validPictures.isEmpty()) {
//	    	System.out.println(result.getFieldErrorCount());
//	    	System.out.println(result.getFieldError());
	    	List<GoodsTypeVO> goodsTypeList= goodsTypeService.getAll();	
	    	model.addAttribute("goodsTypeList", goodsTypeList);
	        model.addAttribute("errorMessage", "請輸入正確的商品資訊，並上傳至少一張照片");
	        return "front-end/used/addUsed";
	    }

	    // 轉換 MultipartFile 到 UsedPicVO
	    List<UsedPicVO> usedPicsList = new ArrayList<>();
	    for (MultipartFile multipartFile : validPictures) {
	        UsedPicVO usedPicVO = new UsedPicVO();
	        usedPicVO.setUsedPicName(multipartFile.getOriginalFilename());
	        usedPicVO.setUsedPics(multipartFile.getBytes());
	        usedPicsList.add(usedPicVO);
	    }

	    // 保存資料
	    usedVO = usedSvc.saveUsedWithPics(usedVO, usedPicsList);
//	    System.out.println(usedNo);
//	    usedVO = usedSvc.getOneUsed(usedNo);
//	    System.out.println(usedVO.getUsedPics().isEmpty());
	    
	    
	    List<GoodsTypeVO> goodsTypeList= goodsTypeService.getAll();	
		
		model.addAttribute("goodsTypeList", goodsTypeList);
	    model.addAttribute("usedVO",usedVO);
	    redirectAttributes.addFlashAttribute("success", "新增成功！");
	    return "front-end/used/listOneUsed";
	}


	
	

	/*
	 * This method will be called on update_Used_input.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("/update")
	public String update(@Valid UsedVO usedVO, BindingResult result,HttpSession session , ModelMap model,
			@RequestParam("upfiles") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		if (result.hasErrors()) {
//			System.out.println(result.getFieldError());
				List<UsedPicVO> usedPics= usedPicSvc.findAllPicsByUsedNo(usedVO.getUsedNo());
				usedVO.setUsedPics(usedPics);
				
				List<GoodsTypeVO> goodsTypeList= goodsTypeService.getAll();	
				model.addAttribute("goodsTypeList", goodsTypeList);
				
				return "front-end/used/update_used_input";
			}
		List<MultipartFile> validPictures=filterEmptyFiles(parts);
		
		if (validPictures.isEmpty()) { // 使用者未選擇要上傳的新圖片時 
			//檢查資料庫有無照片 若無就返回 並提示警告
			if((usedPicSvc.findAllPicsByUsedNo(usedVO.getUsedNo()).size())==0) {
				
				model.addAttribute("errorMessage", "請至少上傳一張照片");
				
				List<GoodsTypeVO> goodsTypeList= goodsTypeService.getAll();	
				model.addAttribute("goodsTypeList", goodsTypeList);
				
				return "front-end/used/update_used_input";
			}
//			else {
//			//檢查資料庫照片 若有就提取舊照片
//			List<UsedPicVO> oldUsedPics = usedSvc.getOneUsed(usedVO.getUsedNo()).getUsedPics();
//			usedVO.setUsedPics(oldUsedPics);
//			}
			
		} else {
			//將新增的圖片跑迴圈後塞入list以便轉交
			List<UsedPicVO> usedPicsList = new ArrayList<>();
		    for (MultipartFile multipartFile : validPictures) {
		        UsedPicVO usedPicVO = new UsedPicVO();
		        usedPicVO.setUsedVO(usedVO);
		        usedPicVO.setUsedPicName(multipartFile.getOriginalFilename());
		        usedPicVO.setUsedPics(multipartFile.getBytes());
		        usedPicsList.add(usedPicVO);
		    }
			usedVO.setUsedPics(usedPicsList);
		}	
		/*************************** 2.開始修改資料 *****************************************/
		Integer usedNo=usedSvc.updateUsed(usedVO);
		
		EntityManager.clear();
		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		
		
		UsedVO newUsedVO= usedSvc.getOneUsed(usedNo);

		
		List<GoodsTypeVO> goodsTypeList= goodsTypeService.getAll();	
		
		model.addAttribute("goodsTypeList", goodsTypeList);
		model.addAttribute("success", " (修改成功)");


		model.addAttribute("usedVO", newUsedVO);
		return "front-end/used/listOneUsed"; // 修改成功後轉交listOneUsed.html
	}

	

	@PostMapping("/admindelete")
	public String admindelete(@RequestParam("usedNo") String usedNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		// UsedService UsedSvc = new UsedService();
		
		usedSvc.deleteUsed(Integer.valueOf(usedNo));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<UsedVO> list = usedSvc.getAll();
		
		model.addAttribute("usedListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "front-end/used/managertest"; // 刪除完成後轉交listAllUsed.html
	}
	
	@PostMapping("/deleteUsed")//ajax DATATABLE刪除用
	@ResponseBody
	public Map<String, Object> usedDelete(@RequestBody Map<String, String> request) {
	    String usedNo = request.get("usedNo"); // 从 JSON 请求体中获取参数
	    Map<String, Object> response = new HashMap<>();

	    try {
	        // 调用删除逻辑
	        usedSvc.deleteUsed(Integer.valueOf(usedNo));
	        response.put("success", true);
	        response.put("message", "刪除成功");
	    } catch (Exception e) {
	        response.put("success", false);
	        response.put("message", "刪除失敗：" + e.getMessage());
	    }

	    return response;
	}
	
	
	
	
	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(UsedVO usedVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
				.collect(Collectors.toList());
		result = new BeanPropertyBindingResult(usedVO, "usedVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}
	
	
	
	//檢查上傳照片資料是否為空
	public List<MultipartFile> filterEmptyFiles(MultipartFile[] parts) {
	    List<MultipartFile> uploadedFiles = new ArrayList<>();
	    
	    for (MultipartFile file : parts) {
	        if (!file.isEmpty()) {
	            uploadedFiles.add(file);  // 只將非空檔案加入列表
	        }
	    }

	    // 回傳 list 可以進行後續處理
		return uploadedFiles;
	}

}
