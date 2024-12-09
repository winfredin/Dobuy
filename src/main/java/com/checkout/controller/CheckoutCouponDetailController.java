package com.checkout.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
public class CheckoutCouponDetailController {
	
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
    
    @PostMapping("/shoppingcartlist/checkout")
    @ResponseBody
    public Map<String, Object> processCheckout(@RequestBody CheckoutRequest checkoutRequest, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 驗證會員權限
            Object memNoObj = session.getAttribute("memNo");
            if (memNoObj == null) {
                response.put("success", false);
                response.put("error", "請先登入");
                return response;
            }
            
            Integer memNo = (memNoObj instanceof Integer) ? (Integer) memNoObj : 
                    Integer.parseInt((String) memNoObj);

            // 檢查訂單資料
            Map<Integer, Integer> counterCoupons = checkoutRequest.getCounterCoupons();
            if (counterCoupons == null || counterCoupons.isEmpty()) {
                response.put("success", false);
                response.put("error", "沒有找到要處理的訂單");
                return response;
            }

            // 重要：處理每個櫃位的訂單優惠
            for (Map.Entry<Integer, Integer> entry : counterCoupons.entrySet()) {
                Integer counterNo = entry.getKey();
                Integer couponNo = entry.getValue();

                // 根據櫃位號碼和會員編號查找訂單
                CounterOrderVO order = counterOrderService.findByCounterNoAndMemNo(counterNo, memNo);
                
                if (order != null && couponNo != null) {
                    // 獲取會員優惠券
                    MemCouponVO memCoupon = memCouponService.getOneMemCoupon(couponNo);
                    
                    if (memCoupon != null && memCoupon.getStatus() != 1) {
                        // 使用優惠券更新訂單金額
                        updateOrderWithDiscount(order, memCoupon);
                        
                        // 更新訂單狀態
                        order.setOrderStatus(0); // 設置為0 後續綠界付款完改狀態
                        counterOrderService.updateCounterOrder(order);
                    }
                }
            }

            response.put("success", true);
            response.put("message", "結帳成功");
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", "結帳過程發生錯誤: " + e.getMessage());
            return response;
        }
    }



    // 輔助方法：更新訂單優惠金額
    private void updateOrderWithDiscount(CounterOrderVO order, MemCouponVO memCoupon) {
        try {
            CouponVO coupon = memCoupon.getCoupon();
            double originalTotal = order.getOrderTotalBefore();
            double discount = 0.0;

            // 計算折扣金額
            for (CouponDetailVO detail : coupon.getCouponDetails()) {
                double threshold = Double.parseDouble(detail.getCounterContext());
                if (originalTotal >= threshold) {
                    double discountRate = detail.getDisRate();
                    discount = originalTotal * (1 - discountRate);
                    break;
                }
            }

            // 更新訂單金額
            int finalTotal = (int) Math.max(0, originalTotal - discount);
            
            order.setOrderTotalAfter(finalTotal);
            
            order.setMemCouponNo(memCoupon.getMemCouponNo());

            counterOrderService.updateCounterOrder(order);

            // 更新優惠券狀態
            memCoupon.setStatus(1);
            memCouponService.updateMemCoupon(memCoupon);

            // 記錄更新結果
            System.out.println("訂單更新成功 - 訂單號: " + order.getCounterOrderNo() + 
                             ", 優惠前金額: " + (int)originalTotal +
                             ", 折扣金額: " + (int)discount +
                             ", 優惠後金額: " + finalTotal);
        } catch (Exception e) {
            System.err.println("更新訂單優惠金額時發生錯誤: " + e.getMessage());
            throw e;
        }
    }
    
	

}
