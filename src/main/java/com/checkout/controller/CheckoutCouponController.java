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
import com.counter.model.CounterVO;
import com.counterorder.model.CounterOrderService;
import com.counterorder.model.CounterOrderVO;
import com.coupon.model.CouponService;
import com.coupon.model.CouponVO;
import com.coupondetail.model.CouponDetailService;
import com.coupondetail.model.CouponDetailVO;
import com.goods.model.GoodsService;
import com.goods.model.GoodsVO;
import com.memcoupon.model.MemCouponService;
import com.memcoupon.model.MemCouponVO;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.HashMap;
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
    
    @Autowired
    GoodsService goodsService;
	
    
    
    // 顯示結帳頁面
    @GetMapping("/shoppingcartlist/ShoppingCartListCheckout49")
    public String showCheckoutPage(HttpSession session, Model model) {
        Object memNoObj = session.getAttribute("memNo");
        
        try {
            if (memNoObj == null) {
                return "redirect:/login";
            }
            
            Integer memNo = (memNoObj instanceof Integer) ? (Integer) memNoObj : 
                    Integer.parseInt((String) memNoObj);

            // 獲取購物車商品
            List<ShoppingCartListVO> cartItems = shoppingCartListService.getCartItemsByMemNo49(memNo);
            
            // 按櫃位分組商品
            Map<Integer, List<ShoppingCartListVO>> cartItemsByCounter = new HashMap<>();
            
         // 為每個購物車項目獲取對應的商品資訊並分組
            for (ShoppingCartListVO cartItem : cartItems) {
                GoodsVO goods = goodsService.getOneGoods(cartItem.getGoodsNo());
                if (goods != null && goods.getCounterVO() != null) {
                    Integer counterNo = goods.getCounterVO().getCounterNo(); // 從 CounterVO 取得櫃位編號
                    cartItemsByCounter
                        .computeIfAbsent(counterNo, k -> new ArrayList<>())
                        .add(cartItem);
                }
            }
            
            // 計算每個櫃位的小計
            Map<Integer, Integer> counterTotals = new HashMap<>();
            for (Map.Entry<Integer, List<ShoppingCartListVO>> entry : cartItemsByCounter.entrySet()) {
                int counterTotal = entry.getValue().stream()
                    .mapToInt(item -> item.getGoodsPrice() * item.getGoodsNum())
                    .sum();
                counterTotals.put(entry.getKey(), counterTotal);
            }
            
            // 計算總金額
            int totalAmount = counterTotals.values().stream()
                .mapToInt(Integer::intValue)
                .sum();

            // 獲取會員可用的優惠券
            List<MemCouponVO> availableCoupons = memCouponService.getAvailableCoupons(memNo);

            model.addAttribute("cartItemsByCounter", cartItemsByCounter);
            model.addAttribute("counterTotals", counterTotals);
            model.addAttribute("totalAmount", totalAmount);
            model.addAttribute("availableCoupons", availableCoupons);

            return "front-end/shoppingcartlist/ShoppingCartListCheckout49";
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "載入結帳頁面時發生錯誤");
            return "error";
        }
    }


    // 計算折扣金額
    @PostMapping("/shoppingcartlist/calculateDiscount")
    @ResponseBody
    public Map<String, Object> calculateDiscount(
            @RequestParam("couponNo") String couponNoStr,
            @RequestParam("counterId") String counterIdStr,
            HttpSession session) {
        try {
            // session 處理
            Object memNoObj = session.getAttribute("memNo");
            Integer memNo;
            if (memNoObj instanceof Integer) {
                memNo = (Integer) memNoObj;
            } else if (memNoObj instanceof String) {
                memNo = Integer.parseInt((String) memNoObj);
            } else if (memNoObj == null) {
                return Map.of("error", "請先登入");
            } else {
                return Map.of("error", "會員資訊錯誤");
            }

            Integer couponNo = Integer.parseInt(couponNoStr);
            Integer counterId = Integer.parseInt(counterIdStr);

            // 獲取並過濾購物車商品
            List<ShoppingCartListVO> cartItems = shoppingCartListService.getCartItemsByMemNo49(memNo)
                .stream()
                .filter(item -> {
                    GoodsVO goods = goodsService.getOneGoods(item.getGoodsNo());
                    return goods != null && 
                           goods.getCounterVO() != null && 
                           goods.getCounterVO().getCounterNo().equals(counterId);
                })
                .collect(Collectors.toList());

            // 計算原始總金額
            double originalTotal = cartItems.stream()
                    .mapToDouble(item -> item.getGoodsPrice() * item.getGoodsNum())
                    .sum();

            CouponVO coupon = couponService.getOneCouponWithDetails(couponNo);
            if (coupon == null) {
                return Map.of("error", "優惠券不存在");
            }

            // 檢查是否達到優惠門檻
            double discount = 0.0;
            boolean thresholdMet = false;
            
            // 計算該櫃位的折扣
            for (CouponDetailVO detail : coupon.getCouponDetails()) {
                try {
                    double threshold = Double.parseDouble(detail.getCounterContext());
                    if (originalTotal >= threshold) {
                        discount = originalTotal * (1 - detail.getDisRate());
                        thresholdMet = true;
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("解析折扣門檻錯誤: " + detail.getCounterContext());
                }
            }

            if (!thresholdMet) {
                return Map.of(
                    "error", "未達到優惠門檻",
                    "discount", 0,
                    "newTotal", originalTotal
                );
            }

            // 計算最終金額
            double newTotal = Math.max(0, originalTotal - discount);

            // 輸出計算過程，方便調試
            System.out.println("原始金額: " + originalTotal);
            System.out.println("折扣金額: " + discount);
            System.out.println("最終金額: " + newTotal);

            return Map.of(
                "discount", discount,
                "newTotal", newTotal,
                "description", coupon.getCouponContext() != null ? coupon.getCouponContext() : "",
                "originalTotal", originalTotal
            );

        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("error", "計算折扣時發生錯誤: " + e.getMessage());
        }
    }
    
    //套用優惠在訂單上
    @PostMapping("/shoppingcartlist/applyDiscount")
    @ResponseBody
    public Map<String, Object> applyOrderDiscount(
            @RequestParam Integer counterOrderNo,
            @RequestParam Integer memCouponNo,
            HttpSession session) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 獲取訂單資訊
            CounterOrderVO order = counterOrderService.getOneCounterOrder(counterOrderNo);
            if (order == null) {
                return Map.of("error", "找不到指定訂單");
            }

            // 驗證會員權限
            Object memNoObj = session.getAttribute("memNo");
            if (memNoObj == null || !order.getMemNo().equals(
                memNoObj instanceof Integer ? (Integer) memNoObj : 
                Integer.parseInt((String) memNoObj))) {
                return Map.of("error", "沒有權限操作此訂單");
            }

            // 獲取會員優惠券資訊
            MemCouponVO memCoupon = memCouponService.getOneMemCoupon(memCouponNo);
            if (memCoupon == null) {
                return Map.of("error", "找不到指定優惠券");
            }

            // 直接從會員優惠券獲取優惠券資訊
            CouponVO coupon = memCoupon.getCoupon();
            if (coupon == null) {
                return Map.of("error", "優惠券資訊不完整");
            }

            // 計算優惠前總金額
            double originalTotal = order.getOrderTotalBefore();

            // 計算優惠金額
            double discount = 0.0;
            String discountDescription = "";

            // 檢查優惠券條件並計算折扣
            for (CouponDetailVO detail : coupon.getCouponDetails()) {
                try {
                    double threshold = Double.parseDouble(detail.getCounterContext());
                    // 通過商品獲取櫃位資訊進行比對
                    if (originalTotal >= threshold && 
                        detail.getGoodsVO() != null && 
                        detail.getGoodsVO().getCounterVO().getCounterNo().equals(order.getCounterNo())) {
                        
                        discount = originalTotal * detail.getDisRate();
                        discountDescription = coupon.getCouponContext();
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("解析優惠券門檻發生錯誤: " + e.getMessage());
                    continue;
                }
            }

            if (discount == 0) {
                return Map.of("error", "此訂單不符合優惠券使用條件");
            }

            // 計算優惠後金額
            double finalTotal = Math.max(0, originalTotal - discount);

            // 更新訂單資訊
            order.setOrderTotalAfter((int) finalTotal);
            order.setMemCouponNo(memCouponNo);
            counterOrderService.updateCounterOrder(order);

            // 更新優惠券使用狀態
            memCoupon.setStatus(1); // 假設 1 代表已使用
            memCouponService.updateMemCoupon(memCoupon);

            // 構建回應
            response.put("success", true);
            response.put("originalTotal", originalTotal);
            response.put("discount", discount);
            response.put("finalTotal", finalTotal);
            response.put("discountDescription", discountDescription);
            
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("error", "套用優惠券時發生錯誤: " + e.getMessage());
        }
    }
    
    
    
	
}
