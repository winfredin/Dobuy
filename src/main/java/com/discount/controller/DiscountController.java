package com.discount.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

import com.discount.model.DiscountVO;
import com.discount.model.DiscountService;

@Controller
@RequestMapping("/discount")
public class DiscountController {

    @Autowired
    DiscountService discountService;
    
    // 新增檢查管理員登入的方法
    private boolean isManagerLoggedIn(HttpSession session) {
        return session.getAttribute("managerNo") != null;
    }
    
    
    

    @GetMapping("addDiscount")
    public String addDiscount(ModelMap model, HttpSession session) {
        // 檢查是否登入
        if (!isManagerLoggedIn(session)) {
            return "redirect:/login/Login"; // 導向登入頁
        }

        DiscountVO discountVO = new DiscountVO();
        model.addAttribute("discountVO", discountVO);
        return "back-end/discount/addDiscount";
    }

    
    
    
    @PostMapping("insert")  
    public String insert(@Valid DiscountVO discountVO, BindingResult result, 
                        ModelMap model, HttpSession session) {
        try {
            System.out.println("收到表單資料: " + discountVO.getDisTitle());
            System.out.println("Result has errors: " + result.hasErrors());
            if(result.hasErrors()) {
                result.getAllErrors().forEach(error -> {
                    System.out.println("Error: " + error.getDefaultMessage());
                });
                return "back-end/discount/addDiscount";
            }

            // 設置時間和狀態
            Date now = new Date();
            discountVO.setCreatedAt(now);
            discountVO.setUpdatedAt(now);
            discountVO.setDisStatus(0);
            
            discountService.addDiscount(discountVO);
            model.addAttribute("success", "新增成功");
            return "redirect:/discount/listAllDiscount";
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "新增失敗: " + e.getMessage());
            return "back-end/discount/addDiscount";
        }
    }
    



    // 前往修改頁面
    @PostMapping("getOne_For_Update")
    public String getOne_For_Update(@RequestParam("disNo") String disNo, 
                                  ModelMap model,
                                  HttpSession session) {
        // 檢查登入狀態
        if (!isManagerLoggedIn(session)) {
            return "redirect:/login";
        }
        
        try {
            // 查詢優惠資料
            DiscountVO discountVO = discountService.getOneDiscount(Integer.valueOf(disNo));
            
            if (discountVO == null) {
                model.addAttribute("error", "找不到指定的優惠資料");
                return "back-end/discount/listAllDiscount";
            }
            
            model.addAttribute("discountVO", discountVO);
            return "back-end/discount/update_discount_input";
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "查詢資料時發生錯誤: " + e.getMessage());
            return "back-end/discount/listAllDiscount";
        }
    }

    // 處理修改資料
    @PostMapping("update")
    public String update(@Valid DiscountVO discountVO, 
                        BindingResult result, 
                        ModelMap model,
                        HttpSession session) {
        // 檢查登入狀態                    
        if (!isManagerLoggedIn(session)) {
            return "redirect:/login";
        }

        try {
            // 驗證輸入資料
            if (result.hasErrors()) {
                return "back-end/discount/update_discount_input";
            }
            
            // 保存原有的創建時間
            DiscountVO originalDiscount = discountService.getOneDiscount(discountVO.getDisNo());
            if (originalDiscount != null) {
                discountVO.setCreatedAt(originalDiscount.getCreatedAt());
            }
            
            // 更新修改時間
            discountVO.setUpdatedAt(new Date());

            // 更新資料
            discountService.updateDiscount(discountVO);
            
            // 查詢更新後的資料
            discountVO = discountService.getOneDiscount(discountVO.getDisNo());
            model.addAttribute("discountVO", discountVO);
            model.addAttribute("success", "修改成功");
            
            return "redirect:/discount/listAllDiscount";
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "修改資料時發生錯誤: " + e.getMessage());
            return "back-end/discount/update_discount_input";
        }
    }

    
    @PostMapping("delete")
    public String delete(@RequestParam("disNo") String disNo, ModelMap model) {
        /*************************** 2.開始刪除資料 *****************************************/
        discountService.deleteDiscount(Integer.valueOf(disNo));

        /*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
        List<DiscountVO> list = discountService.getAll();
        model.addAttribute("discountListData", list);
        model.addAttribute("success", "- (刪除成功)");
        return "back-end/discount/listAllDiscount";
    }


}
