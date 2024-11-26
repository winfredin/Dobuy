package com.coupon.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

import com.coupon.model.CouponVO;
import com.coupon.model.CouponService;

@Controller
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    CouponService couponSvc;

    /*
     * This method will serve as addCoupon.html handler.
     */
    @GetMapping("addCoupon")
    public String addCoupon(ModelMap model) {
        CouponVO couponVO = new CouponVO();
        model.addAttribute("couponVO", couponVO);
        return "vendor-end/coupon/addCoupon";
    }

    /*
     * This method will be called on addCoupon.html form submission, handling POST request
     * It also validates the user input
     */
    @PostMapping("insert")
    public String insert(@Valid CouponVO couponVO, BindingResult result, ModelMap model) {

        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        if (result.hasErrors()) {
            return "vendor-end/coupon/addCoupon";
        }

        /*************************** 2.開始新增資料 *****************************************/
        couponSvc.addCoupon(couponVO);

        /*************************** 3.新增完成,準備轉交(Send the Success view) **************/
        List<CouponVO> list = couponSvc.getAll();
        model.addAttribute("couponListData", list);
        model.addAttribute("success", "- (新增成功)");
//        return "redirect:/coupon/listAllCoupon";
        return "vendor-end/coupon/listAllCoupon";
    }

    /*
     * This method will be called on listAllCoupons.html form submission, handling POST request
     */
    @PostMapping("getOne_For_Update")
    public String getOne_For_Update(@RequestParam("couponNo") String couponNo, ModelMap model) {
        /*************************** 2.開始查詢資料 *****************************************/
        CouponVO couponVO = couponSvc.getOneCoupon(Integer.valueOf(couponNo));

        /*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
        model.addAttribute("couponVO", couponVO);
        return "vendor-end/coupon/update_coupon_input";
    }

    /*
     * This method will be called on update_coupon_input.html form submission, handling POST request
     * It also validates the user input
     */
    @PostMapping("update")
    public String update(@Valid CouponVO couponVO, BindingResult result, ModelMap model) {

        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        if (result.hasErrors()) {
            return "vendor-end/coupon/update_coupon_input";
        }

        /*************************** 2.開始修改資料 *****************************************/
        couponSvc.updateCoupon(couponVO);

        /*************************** 3.修改完成,準備轉交(Send the Success view) **************/
        model.addAttribute("success", "- (修改成功)");
        couponVO = couponSvc.getOneCoupon(Integer.valueOf(couponVO.getCouponNo()));
        model.addAttribute("couponVO", couponVO);
        return "vendor-end/coupon/listOneCoupon";
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
    
//成功訊息顯示在URL
//    @PostMapping("/approve")
//    public void approveCoupon(@RequestParam("couponNo") int couponNo, HttpServletResponse response) throws IOException {
//        boolean isApproved = couponSvc.approveCoupon(couponNo);
//        String message = isApproved ? "審核成功！" : "審核失敗！";
//        response.sendRedirect("/couponcheck?message=" + URLEncoder.encode(message, "UTF-8"));
//    }
    
    
    
}
