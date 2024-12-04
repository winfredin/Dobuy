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

    @GetMapping("/list")
    public String listAvailableCoupons(Model model) {
        List<CouponVO> coupons = couponService.getAllApprovedCoupons();
        model.addAttribute("coupons", coupons);
        return "front-end/coupon/frontListAllCoupon";
    }

    @GetMapping("/detail/{couponNo}")
    public String viewCouponDetail(@PathVariable Integer couponNo, Model model) {
        try {
            CouponVO coupon = couponService.getOneCouponWithDetails(couponNo);
            if (coupon == null) {
                return "redirect:/front-end/coupon/list";
            }
            model.addAttribute("coupon", coupon);
            return "front-end/coupon/frontCouponDetail";  // 要確保這個路徑對應到正確的模板文件
        } catch (Exception e) {
            return "redirect:/front-end/coupon/list";
        }
    }


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
            return "redirect:/mem/login49";
        }
        try {
            // 使用會員帳號獲取會員編號
            MemberVO member = memberService.findByMemAccount(memAccount);
            if (member == null) {
                redirectAttributes.addFlashAttribute("error", "會員資料不存在");
                return "redirect:/mem/login49";
            }

            memCouponService.claimCoupon(member.getMemNo(), couponNo);
            redirectAttributes.addFlashAttribute("message", "領取成功！");
            return "redirect:/memcoupon/memListAllCoupon"; 
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/memcoupon/memListAllCoupon"; 
        }
    }
    
    // 添加會員優惠券列表頁面

    @GetMapping("/member/list")
    public String listMemberCoupons(HttpSession session, Model model) {
        Integer memberNo = (Integer) session.getAttribute("memberNo");
        if (memberNo == null) {
            return "redirect:/mem/login49";  // 或其他登入頁面路徑
        }
        
        List<MemCouponVO> memberCoupons = memCouponService.getAllByMemNo(memberNo);
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
