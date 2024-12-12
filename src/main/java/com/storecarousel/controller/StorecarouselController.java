package com.storecarousel.controller;

import java.io.IOException;
import java.sql.Timestamp;
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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.storecarousel.model.StoreCarouselService;
import com.storecarousel.model.StoreCarouselVO;
import com.storecarousel.model.StorecarouselRepository;

@Controller
@RequestMapping("/storecarousel")
public class StorecarouselController {

    @Autowired
    StoreCarouselService storeCarouselService;
    
    @Autowired
    StorecarouselRepository StorecarouselRepository;
    // 進入新增頁面
    @GetMapping("/addStorecarousel")
    public String addStoreCarousel(ModelMap model) {
    	StoreCarouselVO storeCarouselVO = new StoreCarouselVO();
        model.addAttribute("storeCarouselVO", storeCarouselVO);
        return "back-end/storecarousel/addStoreCarousel";
    }
    
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // 當表單值是空字符串時，將其轉換為 null
        binder.registerCustomEditor(String.class, new org.springframework.beans.propertyeditors.StringTrimmerEditor(true));
    }
    // 新增資料處理
//    @PostMapping("insert")
//    public String insert(@Valid StoreCarouselVO storeCarouselVO, BindingResult result, ModelMap model) {
//        if (result.hasErrors()) {
//            return "back-end/storecarousel/addStoreCarousel";
//        }
//        
//        storeCarouselService.addStoreCarousel(storeCarouselVO);
//        model.addAttribute("success", "- (新增成功)");
//        return "redirect:/storecarousel/listAllStorecarouseltest";
//    }
    
    @PostMapping("insert")
    public String insert(@ModelAttribute StoreCarouselVO storeCarouselVO) {
        try {
        	
        	if (storeCarouselVO.getCarouselTime() == null) {
                storeCarouselVO.setCarouselTime(new Timestamp(System.currentTimeMillis()));
            }
        	storeCarouselService.setDefaultCarouselTime(storeCarouselVO);
            MultipartFile file = storeCarouselVO.getUpFile();
            storeCarouselVO.setCarouselPic(file.getBytes()); // 將字節數據存儲到持久化字段
            storeCarouselService.addStoreCarousel(storeCarouselVO);
            System.out.println("File uploaded successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
        return "back-end/storecarousel/listAllStorecarouseltest";
    }


    // 查詢所有輪播資訊
    @GetMapping("/listAllStorecarouseltest")
    public String listAll(ModelMap model) {
        List<StoreCarouselVO> list = StorecarouselRepository.findAll();
        for (StoreCarouselVO i : list) {
			i.convertToBase64();
		}
        System.out.println("首頁輪播");
        model.addAttribute("storeCarouselList", list);
        return "back-end/storecarousel/listAllStorecarouseltest";
    }

    // 更新資料頁面
    @PostMapping("getOneForUpdate")
    public String getOneForUpdate(@RequestParam("storeCarouselNo") Integer storeCarouselNo, ModelMap model) {
    	StoreCarouselVO storeCarouselVO = storeCarouselService.getOneStoreCarousel(storeCarouselNo);
        model.addAttribute("storeCarouselVO", storeCarouselVO);
        return "back-end/storecarousel/update-StoreCarousel-Input";
    }

    // 更新資料處理
    @PostMapping("update")
    public String update(@Valid StoreCarouselVO storeCarouselVO, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            return "back-end/storecarousel/update-StoreCarousel-Input";
        }
        
        if (storeCarouselVO.getCarouselTime() == null) {
            storeCarouselVO.setCarouselTime(new Timestamp(System.currentTimeMillis()));
        }

        storeCarouselService.setDefaultCarouselTime(storeCarouselVO);
        storeCarouselService.updateStoreCarousel(storeCarouselVO);
        model.addAttribute("success", "- (更新成功)");
        return "redirect:/storecarousel/listAllStorecarouseltest";
    }


    // 刪除資料處理
    @PostMapping("delete")
    public String delete(@RequestParam("storeCarouselNo") Integer storeCarouselNo, ModelMap model) {
        storeCarouselService.deleteStoreCarousel(storeCarouselNo);
        model.addAttribute("success", "- (刪除成功)");
        return "redirect:/storecarousel/listAllStorecarouseltest";
    }

//    // 條件查詢
//    @PostMapping("listByCompositeQuery")
//    public String listByCompositeQuery(HttpServletRequest req, Model model) {
//        Map<Integer, Integer[]> map = req.getParameterMap();
//        List<storeCarouselVO> list = storeCarouselService.getAll(map);
//        model.addAttribute("storeCarouselList", list);
//        return "back-end/storecarousel/listAllStorecarousel";
//    }

    // 下拉選單資料
    @ModelAttribute("storeCarouselMapData")
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
    public BindingResult removeFieldError(StoreCarouselVO storeCarouselVO, BindingResult result, String removedFieldName) {
        List<FieldError> errorsToKeep = result.getFieldErrors().stream()
                .filter(fieldError -> !fieldError.getField().equals(removedFieldName))
                .collect(Collectors.toList());
        result = new BeanPropertyBindingResult(storeCarouselVO, "storeCarouselVO");
        for (FieldError fieldError : errorsToKeep) {
            result.addError(fieldError);
        }
        return result;
    }
    
    @GetMapping("selectPageStorecarousel")
    public String selectPage(Model model) {
        // 取得所有輪播資訊資料供下拉式選單使用
        List<StoreCarouselVO> list = storeCarouselService.getAll();
        model.addAttribute("storeCarouselData", list);
        
        // 新增一個空的 StoreCarouselVO 物件供表單綁定使用	
        StoreCarouselVO storeCarouselVO = new StoreCarouselVO();
        model.addAttribute("storeCarouselVO", storeCarouselVO);
        
        return "back-end/storecarousel/selectPageStorecarousel";
    }

}
