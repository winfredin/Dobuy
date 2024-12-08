package com.checkout.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ShoppingCartList.model.ShoppingCartListService;
import com.ShoppingCartList.model.ShoppingCartListVO;
import com.counterorder.model.CounterOrderService;
import com.counterorder.model.CounterOrderVO;
import com.coupon.model.CouponService;
import com.coupon.model.CouponVO;
import com.coupondetail.model.CouponDetailService;
import com.coupondetail.model.CouponDetailVO;
import com.memcoupon.model.MemCouponService;
import com.memcoupon.model.MemCouponVO;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
//@RequestMapping("/checkout")
public class CheckoutCouponController {

    @Autowired
    ShoppingCartListService shoppingCartListService;

    @Autowired
    CouponService couponService;
    
    @Autowired
    CouponDetailService couponDetailService;

    @Autowired
    MemCouponService memCouponService;
    
    @Autowired
    CounterOrderService counterOrderService;
	
    
    
    // 顯示結帳頁面
    @GetMapping("/shoppingcartlist/ShoppingCartListCheckout49")
    public String showCheckoutPage(HttpSession session, Model model) {
        // 安全地取得會員編號
        Object memNoObj = session.getAttribute("memNo");
        Integer memNo = null;
        
        try {
            if (memNoObj == null) {
                return "redirect:/login";
            }
            
            // 處理不同類型的轉換
            if (memNoObj instanceof Integer) {
                memNo = (Integer) memNoObj;
            } else if (memNoObj instanceof String) {
                memNo = Integer.parseInt((String) memNoObj);
            } else {
                // 處理其他可能的類型
                return "redirect:/login";
            }

            // 獲取購物車商品
            List<ShoppingCartListVO> cartItems = shoppingCartListService.getCartItemsByMemNo49(memNo);
            
            // 計算原始總金額
            int totalAmount = cartItems.stream()
                    .mapToInt(item -> item.getGoodsPrice() * item.getGoodsNum())
                    .sum();

            // 獲取會員可用的優惠券
            List<MemCouponVO> availableCoupons = memCouponService.getAvailableCoupons(memNo);

            model.addAttribute("cartItems", cartItems);
            model.addAttribute("totalAmount", totalAmount);
            model.addAttribute("availableCoupons", availableCoupons);

            return "front-end/shoppingcartlist/ShoppingCartListCheckout49";
            
        } catch (NumberFormatException e) {
            // 數字格式轉換錯誤
            return "redirect:/login";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "載入結帳頁面時發生錯誤");
            return "error";
        }
    }

    // 計算折扣金額
    @PostMapping("/shoppingcartlist/calculateDiscount")
    @ResponseBody
    public Map<String, Object> calculateDiscount(@RequestParam("couponNo") String couponNoStr, HttpSession session) {
        try {
            // 先轉換優惠券編號
            Integer couponNo = Integer.parseInt(couponNoStr);
            
            // 取得會員編號並確保型別正確
            Object memNoObj = session.getAttribute("memNo");
            Integer memNo;
            if (memNoObj instanceof Integer) {
                memNo = (Integer) memNoObj;
            } else if (memNoObj instanceof String) {
                memNo = Integer.parseInt((String) memNoObj);
            } else {
                return Map.of("error", "會員資訊錯誤");
            }

            List<ShoppingCartListVO> cartItems = shoppingCartListService.getCartItemsByMemNo49(memNo);
            CouponVO coupon = couponService.getOneCouponWithDetails(couponNo);
            
            if (coupon == null) {
                return Map.of("error", "優惠券不存在");
            }

            // 計算折扣金額
            double discountAmount = calculateDiscountAmount(cartItems, coupon);
            
            // 計算最終金額
            int originalTotal = cartItems.stream()
                    .mapToInt(item -> item.getGoodsPrice() * item.getGoodsNum())
                    .sum();
                    
            double finalTotal = Math.max(0, originalTotal - discountAmount);

            return Map.of(
                "discount", discountAmount,
                "newTotal", finalTotal,
                "description", coupon.getCouponContext() != null ? coupon.getCouponContext() : ""
            );
            
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Map.of("error", "數據格式錯誤");
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("error", "計算折扣時發生錯誤");
        }
    }

    private double calculateDiscountAmount(List<ShoppingCartListVO> cartItems, CouponVO coupon) {
        double totalDiscount = 0;
        
        // 根據商品編號分組計算總金額
        Map<Integer, Double> goodsTotalMap = cartItems.stream()
            .collect(Collectors.groupingBy(
                ShoppingCartListVO::getGoodsNo,
                Collectors.summingDouble(item -> item.getGoodsPrice() * item.getGoodsNum())
            ));
        
        for (Map.Entry<Integer, Double> entry : goodsTotalMap.entrySet()) {
            Integer goodsNo = entry.getKey();
            Double goodsTotal = entry.getValue();
            
            Optional<CouponDetailVO> detail = coupon.getCouponDetails().stream()
                .filter(d -> d.getGoodsNo().equals(goodsNo))
                .findFirst();
            
            if (detail.isPresent()) {
                try {
                    String thresholdStr = detail.get().getCounterContext();
                    if (thresholdStr == null || thresholdStr.trim().isEmpty()) {
                        continue;
                    }
                    
                    // 安全地轉換門檻金額
                    double threshold = Double.parseDouble(thresholdStr.trim());
                    
                    if (goodsTotal >= threshold) {
                        double discount = goodsTotal * (1 - detail.get().getDisRate());
                        totalDiscount += discount;
                        
                        System.out.println("商品 " + goodsNo + " 折扣計算:");
                        System.out.println("商品總額: " + goodsTotal);
                        System.out.println("門檻金額: " + threshold);
                        System.out.println("折扣比率: " + detail.get().getDisRate());
                        System.out.println("折扣金額: " + discount);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("門檻金額轉換錯誤: " + detail.get().getCounterContext());
                    continue; // 跳過此商品，繼續處理其他商品
                }
            }
        }
        
        return totalDiscount;
    }
    
//  @PostMapping("/confirm49")
//  public String checkout49(HttpSession session, Model model) {
//     1.檢查並計算折扣=========================================柏翔    

    
    
//     2.計算訂單金額和折扣=========================================柏翔    
  
  
  
  

//   3.應用優惠券（如果有）=========================================柏翔  
 
  
	  
//}
    
	
}
