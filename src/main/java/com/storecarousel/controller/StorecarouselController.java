package com.storecarousel.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
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

import com.storecarousel.model.StoreCarouselService;
import com.storecarousel.model.StoreCarouselVO;

@Controller
@RequestMapping("/storecarousel")
public class StorecarouselController {

    @Autowired
    StoreCarouselService storeCarouselService;

    // 進入新增頁面
    @GetMapping("/back-end/storecarousel/addStoreCarousel")
    public String addStoreCarousel(ModelMap model) {
    	StoreCarouselVO storeCarouselVO = new StoreCarouselVO();
        model.addAttribute("storeCarouselVO", storeCarouselVO);
        return "back-end/storecarousel/addStoreCarousel";
    }

    // 新增資料處理
    @PostMapping("insert")
    public String insert(@Valid StoreCarouselVO storeCarouselVO, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            return "back-end/storecarousel/addStoreCarousel";
        }
        
        storeCarouselService.addStoreCarousel(storeCarouselVO);
        model.addAttribute("success", "- (新增成功)");
        return "redirect:/storecarousel/listAllStorecarousel";
    }

    // 查詢所有輪播資訊
    @GetMapping("listAllStorecarouseltest")
    public String listAll(ModelMap model) {
        List<StoreCarouselVO> list = storeCarouselService.getAll();
        System.out.println("=======");
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
        
        storeCarouselService.updateStoreCarousel(storeCarouselVO);
        model.addAttribute("success", "- (更新成功)");
        return "redirect:/storecarousel/listAllStorecarousel";
    }

    // 刪除資料處理
    @PostMapping("delete")
    public String delete(@RequestParam("storeCarouselNo") Integer storeCarouselNo, ModelMap model) {
        storeCarouselService.deleteStoreCarousel(storeCarouselNo);
        model.addAttribute("success", "- (刪除成功)");
        return "redirect:/storecarousel/listAllStorecarousel";
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
}
