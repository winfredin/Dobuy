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
    
//    private double calculateDiscountAmount(List<ShoppingCartListVO> cartItems, CouponVO coupon) {
//        double totalDiscount = 0;
//        
//        // 根據商品編號分組計算總金額
//        Map<Integer, Double> goodsTotalMap = cartItems.stream()
//            .collect(Collectors.groupingBy(
//                ShoppingCartListVO::getGoodsNo,
//                Collectors.summingDouble(item -> item.getGoodsPrice() * item.getGoodsNum())
//            ));
//        
//        for (Map.Entry<Integer, Double> entry : goodsTotalMap.entrySet()) {
//            Integer goodsNo = entry.getKey();
//            Double goodsTotal = entry.getValue();
//            
//            Optional<CouponDetailVO> detail = coupon.getCouponDetails().stream()
//                .filter(d -> d.getGoodsNo().equals(goodsNo))
//                .findFirst();
//            
//            if (detail.isPresent()) {
//                try {
//                    String thresholdStr = detail.get().getCounterContext();
//                    if (thresholdStr == null || thresholdStr.trim().isEmpty()) {
//                        continue;
//                    }
//                    
//                    // 安全地轉換門檻金額
//                    double threshold = Double.parseDouble(thresholdStr.trim());
//                    
//                    if (goodsTotal >= threshold) {
//                        double discount = goodsTotal * (1 - detail.get().getDisRate());
//                        totalDiscount += discount;
//                        
//                        System.out.println("商品 " + goodsNo + " 折扣計算:");
//                        System.out.println("商品總額: " + goodsTotal);
//                        System.out.println("門檻金額: " + threshold);
//                        System.out.println("折扣比率: " + detail.get().getDisRate());
//                        System.out.println("折扣金額: " + discount);
//                    }
//                } catch (NumberFormatException e) {
//                    System.err.println("門檻金額轉換錯誤: " + detail.get().getCounterContext());
//                    continue; // 跳過此商品，繼續處理其他商品
//                }
//            }
//        }
//        
//        return totalDiscount;
//    }
//    
	
}
