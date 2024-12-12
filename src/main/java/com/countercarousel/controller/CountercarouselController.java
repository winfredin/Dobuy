package com.countercarousel.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.countercarousel.model.CountercarouselRepository;
import com.countercarousel.model.CountercarouselService;
import com.countercarousel.model.CountercarouselVO;

@Controller
@RequestMapping("/front-end-carousel")
public class CountercarouselController {

	@Autowired
	CountercarouselRepository countercarouselRepository;
	
	//----------------定紘--------------------------------
	@Autowired
	CountercarouselService countercarouselService;
	//----------------定紘--------------------------------
	
	@GetMapping("/addCarousel")
	public String showRegisterPage(Model model) {
		model.addAttribute("countercarouselVO", new CountercarouselVO());
		return "vendor-end/front-end-carousel/addCarousel";
	}
	
	@PostMapping("/insert")
	public String insert(@ModelAttribute CountercarouselVO countercarouselVO) {
		try {
			MultipartFile file = countercarouselVO.getUpFile();
			countercarouselVO.setCarouselPic(file.getBytes()); // 將字節數據存儲到持久化字段
			countercarouselRepository.save(countercarouselVO);
			System.out.println("File uploaded successfully!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error";
		}
		return "vendor-end//front-end-carousel/ALLCarousel";
	}
	//-----------------------------------------------------------------
	
//	    @GetMapping("/uploadSuccess")
//	    public String uploadSuccess(Model model) {
//	        // 模擬圖片的動態 URL（實際中這可能是用戶上傳後的圖片 URL）
//	        String uploadedImageUrl = "/uploads/user1/image1.jpg";
//	        
//	        // 把這個變數傳遞到前端的 Thymeleaf 頁面
//	        model.addAttribute("uploadedImageUrl", uploadedImageUrl);
//	        
//	        // 返回視圖名，對應的 Thymeleaf 模板路徑
//	        return "carousel/addSuccess";
//	    }
	

	
	//----------------------------------------------------------------------------


	// 顯示圖片列表
	@GetMapping("/ALLCarousel")
	public String getAllImages(ModelMap model) {
		List<CountercarouselVO> carouselList = countercarouselRepository.findAll();
		for (CountercarouselVO i : carouselList) {
			i.convertToBase64();
		}
		System.out.println("櫃位輪播");

		model.addAttribute("carouselList", carouselList);
		return "vendor-end/front-end-carousel/ALLCarousel";
	}
	
	// --------------------定紘---------------------------------
	
	    // 更新資料頁面
	    @PostMapping("/getOneForUpdate")
	    public String getOneForUpdate(@RequestParam("counterCarouselNo") Integer counterCarouselNo, ModelMap model) {
	    	CountercarouselVO counterCarouselVO = countercarouselService.getOneCounterCarousel(counterCarouselNo);
	        model.addAttribute("counterCarouselVO", counterCarouselVO);
	        return "vendor-end/countercarousel/update-CounterStorecarousel-Input";
	    }

	    // 更新資料處理
	    @PostMapping("/update")
	    public String update(@Valid CountercarouselVO counterCarouselVO, BindingResult result, ModelMap model) {
	        if (result.hasErrors()) {
	            return "vendor-end/countercarousel/update-CounterStorecarousel-Input";
	        }

	        countercarouselService.updateCounterCarousel(counterCarouselVO);
	        model.addAttribute("success", "- (更新成功)");
	        return "redirect:/countercarousel/ALLCarousel";
	    }

	    // 刪除資料處理
	    @PostMapping("/delete")
	    public String delete(@RequestParam("counterCarouselNo") Integer counterCarouselNo, ModelMap model) {
	    	countercarouselService.deleteCounterCarousel(counterCarouselNo);
	        model.addAttribute("success", "- (刪除成功)");
	        return "redirect:/countercarousel/ALLCarousel";
	    }

	    // 下拉選單資料
	    @ModelAttribute("/counterCarouselMapData")
	    protected List<Map<String, Object>> referenceMapData() {
	        List<Map<String, Object>> list = new ArrayList<>();
	        Map<String, Object> option1 = new HashMap<>();
	        option1.put("counterNo", 1);
	        option1.put("counterName", "櫃位一");
	        list.add(option1);
	        // 可添加更多選項
	        return list;
	    }

	    // 去除 BindingResult 中某欄位的錯誤
	    public BindingResult removeFieldError(CountercarouselVO counterCarouselVO, BindingResult result, String removedFieldName) {
	        List<FieldError> errorsToKeep = result.getFieldErrors().stream()
	                .filter(fieldError -> !fieldError.getField().equals(removedFieldName))
	                .collect(Collectors.toList());
	        result = new BeanPropertyBindingResult(counterCarouselVO, "counterCarouselVO");
	        for (FieldError fieldError : errorsToKeep) {
	            result.addError(fieldError);
	        }
	        return result;
	    }
	    
	    @GetMapping("/selectPage")
	    public String selectPage(Model model) {
	        // 取得所有追蹤清單資料供下拉式選單使用
	        List<CountercarouselVO> list = countercarouselService.getAll();
	        model.addAttribute("counterCarouselData", list);
	        
	        // 新增一個空的 CounterCarouselVO 物件供表單綁定使用
	        CountercarouselVO counterCarouselVO = new CountercarouselVO();
	        model.addAttribute("counterCarouselVO", counterCarouselVO);
	        
	        return "vendor-end/front-end-carousel/selectPage";
	    }

	


}
