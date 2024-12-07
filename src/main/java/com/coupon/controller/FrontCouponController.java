package com.coupon.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.coupon.model.CouponService;
import com.coupon.model.CouponVO;
import com.member.model.MemberService;
import com.member.model.MemberVO;
import com.memcoupon.model.MemCouponService;
import com.memcoupon.model.MemCouponVO;
import org.springframework.ui.Model;  // 正確的 Model 導入


@Controller
@RequestMapping("/front-end/coupon")
public class FrontCouponController {
    
    @Autowired
    private CouponService couponService;
    
    @Autowired
    private MemCouponService memCouponService;
    
    @Autowired
    private MemberService memberService;

    
//  前台領取櫃位優惠券頁面
    @GetMapping("/list")
    public String listAvailableCoupons(HttpSession session, Model model) {
        // 檢查會員是否已登入
        String memAccount = (String) session.getAttribute("memAccount");
        System.out.println(memAccount);
        
        // 如果未登入,保存當前請求路徑並重定向到登入頁面
//        if (memAccount == null) {
//            session.setAttribute("originalRequest", "/front-end/coupon/list");
//            return "redirect:/mem/login";
//        }
        
        // 已登入則繼續執行原有邏輯
        List<CouponVO> coupons = couponService.getAllApprovedCoupons();
        model.addAttribute("coupons", coupons);
        
        // 將會員帳號加入模型,供頁面顯示
        model.addAttribute("memAccount", memAccount);
        
        return "front-end/coupon/frontListAllCoupon";
    }
    
//  前台領取櫃位優惠券頁面點查看詳情可以看到明細
//    @GetMapping("/detail/{couponNo}")
//    public String viewCouponDetail(@PathVariable Integer couponNo, 
//                                 HttpSession session, 
//                                 Model model) {
//        // 第一步：檢查會員登入狀態
//        String memAccount = (String) session.getAttribute("memAccount");
//        
//        // 如果會員未登入,我們需要:
//        // 1. 保存他們想查看的優惠券詳情頁面路徑
//        // 2. 將他們重定向到登入頁面
//        if (memAccount == null) {
//            // 保存原始請求路徑,包含優惠券編號,這樣登入後可以直接查看想看的優惠券
//            session.setAttribute("originalRequest", "/front-end/coupon/detail/" + couponNo);
//            return "redirect:/mem/login";
//        }
//        
//        // 第二步：如果會員已登入,繼續執行原有的優惠券詳情查詢邏輯
//        try {
//            CouponVO coupon = couponService.getOneCouponWithDetails(couponNo);
//            
//            if (coupon == null) {
//                // 找不到優惠券時的處理
//                return "redirect:/front-end/coupon/list";
//            }
//            
//            // 將優惠券資訊和會員資訊都加入模型
//            model.addAttribute("coupon", coupon);
//            model.addAttribute("memAccount", memAccount);
//            
//            return "front-end/coupon/frontCouponDetail";
//            
//        } catch (Exception e) {
//            // 發生異常時的處理
//            return "redirect:/front-end/coupon/list";
//        }
//    }

 // 第二個方法：顯示優惠券詳情
    @GetMapping("/detail/{couponNo}")
    public String viewCouponDetail(@PathVariable Integer couponNo, 
                                 HttpSession session, 
                                 Model model) {
        // 不需要檢查登入狀態（過濾器會處理），但需要取得會員資訊供頁面使用
        String memAccount = (String) session.getAttribute("memAccount");
        
        try {
            CouponVO coupon = couponService.getOneCouponWithDetails(couponNo);
            if (coupon == null) {
                return "redirect:/front-end/coupon/list";
            }
            
            // 將資料加入模型
            model.addAttribute("coupon", coupon);
            model.addAttribute("memAccount", memAccount);
            
            return "front-end/coupon/frontCouponDetail";
        } catch (Exception e) {
            return "redirect:/front-end/coupon/list";
        }
    }
    
    
    
    
 // 第三個方法：顯示優惠券詳情
//    @GetMapping("/detail/{couponNo}")
//    public String viewCouponDetail(@PathVariable Integer couponNo, Model model) {
//        // 只保留核心功能：獲取並顯示優惠券詳情
//        try {
//            CouponVO coupon = couponService.getOneCouponWithDetails(couponNo);
//            if (coupon == null) {
//                return "redirect:/front-end/coupon/list";
//            }
//            model.addAttribute("coupon", coupon);
//            return "front-end/coupon/frontCouponDetail";
//        } catch (Exception e) {
//            return "redirect:/front-end/coupon/list";
//        }
//    }
//    
    
    

//    前台領取櫃位優惠券後顯示在我的優惠券
    @PostMapping("/claim")
    public String claimCoupon(@RequestParam Integer couponNo,
                             HttpSession session,
                             HttpServletRequest request,
                             RedirectAttributes redirectAttributes) {
        // 檢查是否登入
        String memAccount = (String) session.getAttribute("memAccount");
        if (memAccount == null) {
            // 修改這裡：確保路徑格式正確
            String originalRequest = "/claim?couponNo=" + couponNo;
            System.out.println("Saving original request: " + originalRequest); // 添加日誌
            session.setAttribute("originalRequest", originalRequest);
            return "redirect:/mem/login";
        }
        try {
            // 使用會員帳號獲取會員編號
            MemberVO member = memberService.findByMemAccount(memAccount);
            if (member == null) {
                redirectAttributes.addFlashAttribute("error", "會員資料不存在");
                return "redirect:/mem/login"; //加控制器前綴mem + mapping("")
            }

            memCouponService.claimCoupon(member.getMemNo(), couponNo);
            redirectAttributes.addFlashAttribute("message", "領取成功！");
            return "redirect:/memcoupon/memListAllCoupon"; 
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/memcoupon/memListAllCoupon"; 
        }
    }
    
//    test不使用login49
//    @GetMapping("/claim")
//    public String claimCoupon(@RequestParam Integer couponNo,
//                             HttpSession session,
//                             HttpServletRequest request,
//                             RedirectAttributes redirectAttributes) {
//    	Integer membNo = Integer.valueOf((String) session.getAttribute("memNo"));
//       
//        try {
//            memCouponService.claimCoupon(membNo, couponNo);
//            redirectAttributes.addFlashAttribute("message", "領取成功！");
//            System.out.println("領取成功");
//            return "redirect:/memcoupon/memListAllCoupon"; 
//        } catch (Exception e) {
//        	System.out.println("領取失敗");
//            redirectAttributes.addFlashAttribute("error", e.getMessage());
//            return "redirect:/memcoupon/memListAllCoupon"; 
//        }
//    }


//  前台查看會員優惠券列表頁面
    @GetMapping("/member/list")
    public String listMemberCoupons(HttpSession session, Model model) {
        Integer memNo = Integer.valueOf((String) session.getAttribute("memNo"));
        if (memNo == null) {
            return "redirect:/mem/login";  // 或其他登入頁面路徑
        }
        
        String memAccount = (String) session.getAttribute("memAccount");
        if (memAccount == null) {
            return "redirect:/mem/login";
        }
        List<MemCouponVO> memberCoupons = memCouponService.getAllByMemNo(memNo);
        model.addAttribute("memberCoupons", memberCoupons);
        return "front-end/coupon/memListAllCoupon";
    }
    
    //以下昱夆新增
    @GetMapping("/member/list35")
    public String listMemCoupons(HttpSession session, Model model) {
        Integer membNo = Integer.valueOf((String) session.getAttribute("memNo"));
      
        List<MemCouponVO> memberCoupons = memCouponService.getAllByMemNo(membNo);
        model.addAttribute("memberCoupons", memberCoupons);
        return "front-end/memcoupon/memListAllCoupon";
    }
    
    
    @GetMapping("/claim35")
    public String claimCoupon35(@RequestParam Integer couponNo,
                             HttpSession session,
                             HttpServletRequest request,
                             RedirectAttributes redirectAttributes) {
    	Integer membNo = Integer.valueOf((String) session.getAttribute("memNo"));
       
        try {
            memCouponService.claimCoupon(membNo, couponNo);
            redirectAttributes.addFlashAttribute("message", "領取成功！");
            System.out.println("領取成功");
            return "redirect:/memcoupon/memListAllCoupon"; 
        } catch (Exception e) {
        	System.out.println("領取失敗");
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/memcoupon/memListAllCoupon"; 
        }
    }
  //以上昱夆新增
    

//    @GetMapping("/member/list")
//    public String listMemberCoupons(HttpSession session, Model model) {
//        Integer memberNo = (Integer) session.getAttribute("memberNo");
//        if (memberNo == null) {
//            return "redirect:/mem/login49";  // 或其他登入頁面路徑
//        }
//        
//        List<MemCouponVO> memberCoupons = memCouponService.getAllByMemNo(memberNo);
//        model.addAttribute("memberCoupons", memberCoupons);
//        return "front-end/coupon/memListAllCoupon";
//    }

}
