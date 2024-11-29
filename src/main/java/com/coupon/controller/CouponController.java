package com.coupon.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional; 
import javax.validation.Valid;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

import com.coupon.model.CouponVO;
import com.coupondetail.model.CouponDetailService;
import com.coupondetail.model.CouponDetailVO;
import com.coupon.model.CouponService;

@Controller
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    CouponService couponSvc;

    @Autowired
    CouponDetailService coupondetailSvc;
    /*
     * This method will serve as addCoupon.html handler.
     */
    @GetMapping("addCoupon")
    public String addCoupon(ModelMap model) {
        CouponVO couponVO = new CouponVO();
        model.addAttribute("couponVO", couponVO);
        return "vendor-end/coupon/addCoupon";
    }
    
    @PostMapping("/addCouponDetail")
    public ResponseEntity<String> addCouponDetail(@RequestParam Integer couponNo,
                                                  @RequestBody CouponDetailVO couponDetailVO) {
        try {
        	couponSvc.addCouponDetail(couponNo, couponDetailVO);
            return ResponseEntity.ok("Coupon detail added successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @PostMapping("/insert")
    public String insertCouponWithDetails(
            @ModelAttribute("couponVO") CouponVO couponVO,
            RedirectAttributes redirectAttributes) {
        try {
            // 輸出調試信息
            System.out.println("接收到的數據：");
            System.out.println("優惠券標題: " + couponVO.getCouponTitle());
            System.out.println("明細數量: " + 
                (couponVO.getCouponDetails() != null ? 
                 couponVO.getCouponDetails().size() : 0));
            
            // 添加驗證
            if (couponVO.getCouponDetails() == null || 
                couponVO.getCouponDetails().isEmpty()) {
                throw new IllegalArgumentException("至少需要一個優惠券明細");
            }
            
            // 調用服務保存數據
            CouponVO savedCoupon = couponSvc.addCouponWithDetails(couponVO);
            
            // 設置成功消息
            redirectAttributes.addFlashAttribute("success", 
                String.format("優惠券 %s 新增成功！", savedCoupon.getCouponTitle()));
            return "redirect:/coupon/listAllCoupon";
            
        } catch (Exception e) {
            // 設置錯誤消息
            redirectAttributes.addFlashAttribute("error", "新增失敗：" + e.getMessage());
            // 保留輸入的數據
            redirectAttributes.addFlashAttribute("couponVO", couponVO);
            return "redirect:/coupon/addCoupon";
        }
    }

    
    
    @PostMapping("/addCoupon")
    public String addCoupon(
        @RequestParam("couponDetail") List<CouponDetailVO> couponDetail,
        @ModelAttribute("couponVO") CouponVO couponVO
    ) {
        // 業務邏輯
        return "redirect:/success";
    }
    
    
    
    /*
     * This method will be called on listAllCoupons.html form submission, handling POST request
     */
    @PostMapping("getOne_For_Update")
    public String getOne_For_Update(@RequestParam("couponNo") String couponNo, Model model) {
        try {
            // 獲取優惠券數據
            CouponVO couponVO = couponSvc.getOneCouponWithDetails(Integer.valueOf(couponNo));
            
            if (couponVO == null) {
                model.addAttribute("error", "找不到指定的優惠券");
                return "redirect:/coupon/listAllCoupon";
            }
            
            // 確保明細列表不為 null
            if (couponVO.getCouponDetails() == null) {
                couponVO.setCouponDetails(new ArrayList<>());
            }
            
            // 將數據添加到模型
            model.addAttribute("couponVO", couponVO);
            System.out.println("載入優惠券數據：" + couponVO.getCouponTitle());
            System.out.println("明細數量：" + couponVO.getCouponDetails().size());
            
            return "vendor-end/coupon/update_coupon_input";
            
        } catch (Exception e) {
            model.addAttribute("error", "載入數據時發生錯誤：" + e.getMessage());
            return "redirect:/coupon/listAllCoupon";
        }
    }

    /*
     * This method will be called on update_coupon_input.html form submission, handling POST request
     * It also validates the user input
     */
    @PostMapping("update")
    public String update(@Valid CouponVO couponVO, 
                         BindingResult result, 
                         Model model) {
        System.out.println("Received update request for coupon: " + couponVO.getCouponNo());
        
        if (result.hasErrors()) {
            System.out.println("Validation errors found:");
            for (FieldError error : result.getFieldErrors()) {
                System.out.println(error.getField() + ": " + error.getDefaultMessage());
            }
            return "vendor-end/coupon/update_coupon_input";
        }

        try {
            // 更新優惠券和明細
            CouponVO updatedCoupon = couponSvc.updateCouponWithDetails(couponVO);
            model.addAttribute("success", "修改成功！");
            model.addAttribute("couponVO", updatedCoupon);
            // 直接返回顯示更新後結果的頁面，而不是重定向，確保能顯示更新後的數據
            return "vendor-end/coupon/listOneCoupon";
        } catch (Exception e) {
            System.out.println("Error updating coupon: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "修改失敗：" + e.getMessage());
            model.addAttribute("couponVO", couponVO);
            return "vendor-end/coupon/update_coupon_input";
        }
    }

    // 添加這個方法處理 GET 請求
    @GetMapping("update")
    public String handleUpdateGet(@RequestParam(required = false) Integer couponNo, Model model) {
        // 如果有 couponNo，重定向到修改頁面
        if (couponNo != null) {
            return "redirect:/coupon/getOne_For_Update?couponNo=" + couponNo;
        }
        // 否則重定向到列表頁面
        return "redirect:/coupon/listAllCoupon";
    }
    /*
     * This method will be called on listAllCoupons.html form submission, handling POST request
     */
    @PostMapping("delete")
    public String delete(@RequestParam("couponNo") String couponNo, ModelMap model) {
        /*************************** 2.開始刪除資料 *****************************************/
        couponSvc.deleteCoupon(Integer.valueOf(couponNo));

        /*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
        List<CouponVO> list = couponSvc.getAll();
        model.addAttribute("couponListData", list);
        model.addAttribute("success", "- (刪除成功)");
        return "vendor-end/coupon/listAllCoupon";
    }

    /*
     * This method will be called on listAllCoupons.html form submission, handling POST request
     */
    @PostMapping("listCoupon_ByCompositeQuery")
    public String listAllCoupon(HttpServletRequest req, Model model) {
        Map<String, String[]> map = req.getParameterMap();
        List<CouponVO> list = couponSvc.getAll(map);
        model.addAttribute("couponListData", list);
        return "vendor-end/coupon/listAllCoupon";
    }
    
    
//    審核優惠券
    @PostMapping("/approve")
    public String approveCoupon(@RequestParam("couponNo") int couponNo, RedirectAttributes redirectAttributes) {
        boolean isApproved = couponSvc.approveCoupon(couponNo);
        if (isApproved) {
            redirectAttributes.addFlashAttribute("message", "審核成功！");
        } else {
            redirectAttributes.addFlashAttribute("message", "審核失敗！");
        }
        return "redirect:/couponcheck"; // 審核完成後重定向到優惠券列表頁
    }    
//    靜態資源無法載入
//    @PostMapping("/approve")
//    public String approveCoupon(@RequestParam("couponNo") int couponNo, Model model) {
//        boolean isApproved = couponSvc.approveCoupon(couponNo);
//        if (isApproved) {
//            model.addAttribute("message", "審核成功！");
//        } else {
//            model.addAttribute("message", "審核失敗！");
//        }
//        // 直接返回對應的頁面名稱
//        return "back-end/coupon/couponcheck"; // 假設這是優惠券審核結果頁面
//    }
    


    
    
    
    
}
