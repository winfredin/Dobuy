package com.coupon.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import org.springframework.web.bind.annotation.PathVariable;
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
import com.goods.model.GoodsService;
import com.goods.model.GoodsVO;
import com.counter.model.CounterVO;
import com.coupon.model.CouponService;

@Controller
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    CouponService couponSvc;

    @Autowired
    CouponDetailService coupondetailSvc;
    
    @Autowired
    GoodsService goodsSvc;
    
    // 確保 counter 存在的公共方法
    @ModelAttribute("counter")
    public CounterVO populateCounter(HttpSession session) {
        CounterVO counter = (CounterVO) session.getAttribute("counter");
        if (counter == null) {
            counter = new CounterVO(); // 設置一個空的 CounterVO
            counter.setCounterNo(0);   // 設置合理的默認值，避免模板出錯
            counter.setCounterStatus(0); // 適配 Thymeleaf 的 counterStatus
            session.setAttribute("counter", counter); // 將 counter 放回 session
        }
        return counter;
    }
    
    @GetMapping("addCoupon")
//   從 session 獲取當前登入櫃位資訊
    public String addCoupon(HttpSession session, ModelMap model) {
        CounterVO counter = (CounterVO) session.getAttribute("counter");
        
        // 如果櫃位未登入，重定向到登入頁
        if (counter == null) {
            return "redirect:/counter/login";  // 請根據實際的櫃位登入路徑調整
        }
        
        List<GoodsVO> goodsList = goodsSvc.findByCounterVO_CounterNo(counter.getCounterNo());
        model.addAttribute("goodsList", goodsList);
        
        CouponVO couponVO = new CouponVO();
        couponVO.setCounter(counter);  // 設置 CounterVO
        couponVO.setCouponDetails(new ArrayList<>());
        couponVO.getCouponDetails().add(new CouponDetailVO()); // 添加一個空明細
        
        model.addAttribute("couponVO", couponVO);
        model.addAttribute("counter", counter);  // 加入這行，提供給 header 使用
        
        return "vendor-end/coupon/addCoupon";
    }
    

//    櫃位新增優惠券 可以同時設定優惠商品明細--送出
    @PostMapping("/insert")
    public String insertCouponWithDetails(
            @ModelAttribute("couponVO") CouponVO couponVO,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            // 從 Session 中獲取 CounterVO
            CounterVO counter = (CounterVO) session.getAttribute("counter");
            if (counter == null) {
                throw new IllegalStateException("未登入櫃位，無法新增優惠券");
            }
            
            // 設置 counter 到 couponVO
            couponVO.setCounter(counter);

            // 驗證優惠券明細
            if (couponVO.getCouponDetails() == null || couponVO.getCouponDetails().isEmpty()) {
                throw new IllegalArgumentException("至少需要一個優惠券明細");
            }

            // 保存數據
            couponSvc.addCouponWithDetails(couponVO);

            redirectAttributes.addFlashAttribute("success", "優惠券新增成功");
            return "redirect:/coupon/listAllCoupon";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "新增失敗：" + e.getMessage());
            redirectAttributes.addFlashAttribute("couponVO", couponVO);
            return "redirect:/coupon/addCoupon";
        }
    }

    
    
    
    
    
//    櫃位修改優惠券 可以同時修改優惠商品明細--進入
    @PostMapping("getOne_For_Update")
    public String getOne_For_Update(@RequestParam("couponNo") String couponNo, 
                                    Model model, 
                                    HttpSession session) {
        try {
            // 從 session 中獲取 counter 資料
            CounterVO counter = (CounterVO) session.getAttribute("counter");
            if (counter == null) {
                return "redirect:/counter/login"; // 如果沒有登入，重定向到登入頁面
            }
            model.addAttribute("counter", counter);

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
            
            return "vendor-end/coupon/updateCoupon";
            
        } catch (Exception e) {
            model.addAttribute("error", "載入數據時發生錯誤：" + e.getMessage());
            return "redirect:/coupon/listAllCoupon";
        }
    }

//    櫃位修改優惠券 可以同時修改優惠商品明細--送出
    @PostMapping("update")
    public String update(@Valid CouponVO couponVO, 
                         BindingResult result, 
                         Model model,
                         HttpSession session) {
        System.out.println("Received update request for coupon: " + couponVO.getCouponNo());

        // 從 session 取得 counter 並添加到 model
        CounterVO counter = (CounterVO) session.getAttribute("counter");
        if (counter == null) {
            return "redirect:/counter/login"; // 如果未登入，重定向到登入頁面
        }
        model.addAttribute("counter", counter);

        // 確保 couponVO 的 counter 也正確設置
        if (couponVO.getCounter() == null) {
            couponVO.setCounter(counter);
        }

        if (result.hasErrors()) {
            System.out.println("Validation errors found:");
            model.addAttribute("couponVO", couponVO);
            return "vendor-end/coupon/updateCoupon";
        }

        try {
            // 更新優惠券和明細
            CouponVO updatedCoupon = couponSvc.updateCouponWithDetails(couponVO);
            model.addAttribute("success", "修改成功！");
            model.addAttribute("couponVO", updatedCoupon);
            return "vendor-end/coupon/listOneCoupon";
        } catch (Exception e) {
            System.out.println("Error updating coupon: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "修改失敗：" + e.getMessage());
            model.addAttribute("couponVO", couponVO);
            return "vendor-end/coupon/updateCoupon";
        }
    }


//    @GetMapping("update")
//    public String handleUpdateGet(@RequestParam(required = false) Integer couponNo, Model model) {
//        // 如果有 couponNo，重定向到修改頁面
//        if (couponNo != null) {
//            return "redirect:/coupon/getOne_For_Update?couponNo=" + couponNo;
//        }
//        // 否則重定向到列表頁面
//        return "redirect:/coupon/listAllCoupon";
//    }
    
    
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
//    @PostMapping("listCoupon_ByCompositeQuery")
//    public String listAllCoupon(HttpServletRequest req, Model model) {
//        Map<String, String[]> map = req.getParameterMap();
//        List<CouponVO> list = couponSvc.getAll(map);
//        model.addAttribute("couponListData", list);
//        return "vendor-end/coupon/listAllCoupon";
//    }
    
  //任國櫃位優惠券管理 櫃位列出自己的優惠券 
    @PostMapping("listCounterCoupons_ByCompositeQuery")
    public String listCounterCoupons(HttpSession session ,HttpServletRequest req, Model model) {
        CounterVO counter = (CounterVO) session.getAttribute("counter");
        List<CouponVO> list = couponSvc.getOneCounter46(counter.getCounterNo());
        model.addAttribute("counterCouponListDat", list); // for listAllEmp.html 第85行用
        return "vendor-end/coupon/listAllCoupon";
    }
    
    
    
 // 後台審核優惠券的 GET 方法
    @GetMapping("/approve")
    public String showApprovePage(Model model) {
        // 獲取所有優惠券列表
        List<CouponVO> list = couponSvc.getAll();
        // 將數據添加到模型中，注意這裡的屬性名要跟視圖中使用的一致
        model.addAttribute("couponListData", list);
        return "back-end/coupon/couponApprove";
    }
    
    
//   後台審核優惠券
    @PostMapping("/approve")
    public String approveCoupon(@RequestParam("couponNo") int couponNo, RedirectAttributes redirectAttributes) {
        boolean isApproved = couponSvc.approveCoupon(couponNo);
        if (isApproved) {
            redirectAttributes.addFlashAttribute("message", "審核成功！");
        } else {
            redirectAttributes.addFlashAttribute("message", "審核失敗！");
        }
        return "redirect:/coupon/approve"; // 修改為正確的路徑
    }    
    
    
 // 後台審核優惠券 可查看單個優惠券詳情
    @GetMapping("/approve/{couponNo}")
    public String viewCouponDetail(@PathVariable int couponNo, Model model) {
        CouponVO coupon = couponSvc.getOneCouponWithDetails(couponNo);
        
        // 先檢查優惠券是否存在
        if (coupon == null) {
            return "redirect:/coupon/approve";
        }

        // 使用優惠券的 CouponDetails 集合，而不是重新查詢
        List<CouponDetailVO> details = new ArrayList<>(coupon.getCouponDetails());


        model.addAttribute("coupon", coupon);
        model.addAttribute("couponDetails", details);
        
        return "back-end/coupondetail/couponDetail";
    }

}
