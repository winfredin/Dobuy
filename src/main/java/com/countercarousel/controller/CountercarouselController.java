package com.countercarousel.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.counter.model.CounterService;
import com.counter.model.CounterVO;
import com.countercarousel.model.CountercarouselRepository;
import com.countercarousel.model.CountercarouselService;
import com.countercarousel.model.CountercarouselVO;
import com.msg.model.MsgService;

@Controller
@RequestMapping("/front-end-carousel")
public class CountercarouselController {

	@Autowired
	CountercarouselRepository countercarouselRepository;
	
	//----------------定紘--------------------------------
	@Autowired
	CountercarouselService countercarouselService;
	//----------------定紘--------------------------------
	@Autowired
	CounterService counterService;
	
	
    @Autowired
    MsgService msgSvc;
	
	@GetMapping("/addCarousel")
	public String showRegisterPage(HttpSession session, Model model) {
		
		 CountercarouselVO countercarouselVO = new CountercarouselVO();
		Integer counterNo = (Integer) session.getAttribute("counterNo");
		CounterVO counterVO= counterService.getOneCounter(counterNo);
		countercarouselVO.setCounterNo(counterNo);
				
		model.addAttribute("counterVO",counterVO);
		model.addAttribute("countercarouselVO",countercarouselVO);
		return "vendor-end/front-end-carousel/addCarousel";
	}
	
//	@PostMapping("/insert")
//	public String insert(@ModelAttribute CountercarouselVO countercarouselVO) {
//		try {
//			MultipartFile file = countercarouselVO.getUpFile();
//			countercarouselVO.setCarouselPic(file.getBytes()); // 將字節數據存儲到持久化字段
//			countercarouselRepository.save(countercarouselVO);
//			System.out.println("File uploaded successfully!");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return "error";
//		}
//		return "vendor-end/front-end-carousel/ALLCarousel";
//	}
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


	
	
	//-----------------------以下顯示特定櫃位輪播資訊--------------------
	
	@GetMapping("/ALLCarousel")
	public String getCounterCarousel(HttpSession session, ModelMap model) {
	    // 確認 Counter 資訊是否存在於 session 中
	    CounterVO counter = (CounterVO) session.getAttribute("counter");
	    if (counter == null) {
	        return "redirect:/counter/login"; // 如果未登錄，則重定向到登錄頁面
	    }

	    // 查詢該櫃位的輪播資訊
	    List<CountercarouselVO> carouselList = countercarouselService.getByCounterNo(counter.getCounterNo());
	    
	    // 將輪播圖片轉換為 Base64 格式，供前端顯示
	    for (CountercarouselVO i : carouselList) {
	        i.convertToBase64();
	    }

	    System.out.println("櫃位 " + counter.getCounterNo() + " 的輪播資訊查詢完成");

	    // 將數據添加到 Model 中，供前端檢視使用
	    model.addAttribute("counterCarouselListData", carouselList);
	    model.addAttribute("counter", counter); // 確保 Counter 資訊被添加到模型中
	    model.addAttribute("msgSvc", msgSvc);
	    
	    return "/vendor-end/front-end-carousel/ALLCarousel";
	}
	
	//-----------------------以上顯示特定櫃位輪播資訊--------------------
	
	// --------------------定紘---------------------------------
	
	    // 更新資料頁面
//	    @PostMapping("/getOneForUpdate")
//	    public String getOneForUpdate(@RequestParam("counterCarouselNo") Integer counterCarouselNo, ModelMap model) {
//	    	CountercarouselVO counterCarouselVO = countercarouselService.getOneCounterCarousel(counterCarouselNo);
//	        model.addAttribute("counterCarouselVO", counterCarouselVO);
//	        return "vendor-end/countercarousel/update-CounterStorecarousel-Input";
//	    }

	    // 更新資料處理
//	    @PostMapping("/update")
//	    public String update(@Valid CountercarouselVO counterCarouselVO, BindingResult result, ModelMap model) {
//	        if (result.hasErrors()) {
//	            return "vendor-end/countercarousel/update-CounterStorecarousel-Input";
//	        }
//	        countercarouselService.updateCounterCarousel(counterCarouselVO);
//	        model.addAttribute("success", "- (更新成功)");
//	        return "redirect:/countercarousel/ALLCarousel";
//	    }

	    // 刪除資料處理
	    @PostMapping("/delete")
	    public String delete(@RequestParam("id") Integer id, ModelMap model) {
	    	countercarouselService.deleteCounterCarousel(id);
	        model.addAttribute("success", "- (刪除成功)");
	        return "redirect:/front-end-carousel/ALLCarousel";
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
	    
	    //----------------------以下為updatetest-------------------------------------
	    
	    @PostMapping("/getOneForUpdate")
	    public String getOneForUpdate(@RequestParam("id") Integer id, Model model) {
	        CountercarouselVO countercarouselVO = countercarouselRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Carousel not found"));
	        countercarouselVO.convertToBase64(); // 轉換圖片為 Base64 以供顯示
	        model.addAttribute("countercarouselVO", countercarouselVO);
	        return "vendor-end/front-end-carousel/updateCarouseltest"; // 建立一個更新用的頁面
	    }
	    	
	    @PostMapping("/update")
	    public String update(@ModelAttribute CountercarouselVO countercarouselVO) {
	        try {
	            // 獲取原有的資料
	            CountercarouselVO original = countercarouselRepository.findById(countercarouselVO.getId())
	                    .orElseThrow(() -> new RuntimeException("Carousel not found"));
	            
	            // 如果有上傳新圖片，則更新圖片
	            MultipartFile file = countercarouselVO.getUpFile();
	            if (file != null && !file.isEmpty()) {
	                countercarouselVO.setCarouselPic(file.getBytes());
	            } else {
	                // 如果沒有上傳新圖片，保留原來的圖片
	                countercarouselVO.setCarouselPic(original.getCarouselPic());
	            }
	            
	            // 保存更新
	            countercarouselRepository.save(countercarouselVO);
	            
	            return "redirect:/front-end-carousel/ALLCarousel";
	        } catch (IOException e) {
	            e.printStackTrace();
	            return "error";
	        }
	    }
	    
//	    @PostMapping("/update")
//	    public String update(@Valid @ModelAttribute CountercarouselVO countercarouselVO,
//	                        BindingResult result,
//	                        RedirectAttributes redirectAttributes) {
//	        if (result.hasErrors()) {
//	            return "vendor-end/front-end-carousel/updateCarousel";
//	        }
//	        
//	        try {
//	            // 獲取原有的資料
//	            CountercarouselVO original = countercarouselRepository.findById(countercarouselVO.getId())
//	                    .orElseThrow(() -> new RuntimeException("Carousel not found"));
//	            
//	            // 確保 goodsNo 不為空
//	            if (countercarouselVO.getGoodsNo() == null || countercarouselVO.getGoodsNo().trim().isEmpty()) {
//	                redirectAttributes.addFlashAttribute("error", "商品編號不能為空");
//	                return "vendor-end/front-end-carousel/updateCarousel";
//	            }
//	            
//	            MultipartFile file = countercarouselVO.getUpFile();
//	            if (file != null && !file.isEmpty()) {
//	                countercarouselVO.setCarouselPic(file.getBytes());
//	            } else {
//	                countercarouselVO.setCarouselPic(original.getCarouselPic());
//	            }
//	            
//	            countercarouselRepository.save(countercarouselVO);
//	            redirectAttributes.addFlashAttribute("success", "更新成功");
//	            return "redirect:/front-end-carousel/ALLCarousel";
//	            
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	            redirectAttributes.addFlashAttribute("error", "檔案上傳失敗");
//	            return "vendor-end/front-end-carousel/updateCarousel";
//	        }
//	    }

	    //--------------------------以上為updatetest-------------------------

	    //--------------------------以下為insert測試-------------------------
	    @PostMapping("/insert")
	    public String insert(@Valid @ModelAttribute CountercarouselVO countercarouselVO, 
	                        BindingResult result, 
	                        RedirectAttributes redirectAttributes) {
	        if (result.hasErrors()) {
	            return "vendor-end/front-end-carousel/addCarousel";
	        }
	        
	        try {
	            MultipartFile file = countercarouselVO.getUpFile();
	            if (file != null && !file.isEmpty()) {
	                countercarouselVO.setCarouselPic(file.getBytes());
	            }
	            
	            // 確保 goodsNo 不為空
	            if (countercarouselVO.getGoodsNo() == null || countercarouselVO.getGoodsNo() <= 0) {
	                redirectAttributes.addFlashAttribute("error", "商品編號不能為空並且必須為正數");
	                return "vendor-end/front-end-carousel/addCarousel";
	            }
	            
	            countercarouselRepository.save(countercarouselVO);
	            redirectAttributes.addFlashAttribute("success", "新增成功");
	            return "redirect:/front-end-carousel/ALLCarousel";
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	            redirectAttributes.addFlashAttribute("error", "檔案上傳失敗");
	            return "vendor-end/front-end-carousel/addCarousel";
	        }
	    }
	    //--------------------------以上為insert測試-------------------------

}
