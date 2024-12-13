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
import com.counterorderdetail.model.CounterOrderDetailService;
import com.counterorderdetail.model.CounterOrderDetailVO;
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
    private ShoppingCartListService shoppingCartListService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponDetailService couponDetailService;
    @Autowired
    private MemCouponService memCouponService;
    @Autowired
    private CounterOrderService counterOrderService;
    @Autowired
    private CounterOrderDetailService counterOrderDetailService;
    @Autowired
    private GoodsService goodsService;
    
    
//    ===============================結帳流程
    @PostMapping("/shoppingcartlist/checkout")
    @ResponseBody
    @Transactional
    public Map<String, Object> processCheckout(@RequestBody CheckoutRequest checkoutRequest, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 1. 驗證會員權限
            Object memNoObj = session.getAttribute("memNo");
            if (memNoObj == null) {
                response.put("success", false);
                response.put("error", "請先登入");
                return response;
            }
            
            // 安全地處理 memNo 的轉換
            Integer memNo = convertMemNo(memNoObj);
            if (memNo == null) {
                response.put("success", false);
                response.put("error", "會員編號格式不正確");
                return response;
            }

            // 2. 檢查訂單資料
            Map<Integer, Integer> counterCoupons = checkoutRequest.getCounterCoupons();
            if (counterCoupons == null || counterCoupons.isEmpty()) {
                response.put("success", false);
                response.put("error", "沒有找到要處理的訂單");
                return response;
            }

            // 3. 處理每個櫃位的訂單
            for (Map.Entry<Integer, Integer> entry : counterCoupons.entrySet()) {
                Integer counterNo = entry.getKey();
                Integer couponNo = entry.getValue();
                
                // 找到對應的訂單
                CounterOrderVO order = counterOrderService.findByCounterNoAndMemNo(counterNo, memNo);
                if (order == null) continue;

                // 如果有選擇優惠券
                if (couponNo != null) {
                    MemCouponVO memCoupon = memCouponService.getOneMemCoupon(couponNo);
                    if (memCoupon != null && memCoupon.getStatus() != 1) {
                        // 處理優惠券折扣
                        processOrderDiscount(order, memCoupon);
                    }
                }
            }
            System.out.println("收到的櫃位與優惠券資料：" + checkoutRequest.getCounterCoupons());

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

    private Integer convertMemNo(Object memNoObj) {
        try {
            if (memNoObj instanceof Integer) {
                return (Integer) memNoObj;
            } else if (memNoObj instanceof String) {
                return Integer.parseInt((String) memNoObj);
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return null;
    }
//    套用(修改)訂單與訂單明細金額 ===============折扣計算
    private void processOrderDiscount(CounterOrderVO order, MemCouponVO memCoupon) {
        try {
            CouponVO coupon = memCoupon.getCoupon();
            List<CounterOrderDetailVO> orderDetails = counterOrderDetailService.getDetailsByOrderNo(order.getCounterOrderNo());
            
            System.out.println("\n開始處理訂單號: " + order.getCounterOrderNo() + " 的折扣計算");
            
            // 建立優惠券商品映射
            Map<Integer, CouponDetailVO> couponDetailMap = new HashMap<>();
            for (CouponDetailVO couponDetail : coupon.getCouponDetails()) {
                couponDetailMap.put(couponDetail.getGoodsNo(), couponDetail);
                System.out.println("商品 " + couponDetail.getGoodsNo() + " 有優惠券設定");
            }
            
            // 處理每個明細的折扣
            for (CounterOrderDetailVO detail : orderDetails) {
                int goodsNo = detail.getGoodsNo();
                int originalPrice = detail.getProductPrice();
                int discountedPrice = originalPrice;
                
                CouponDetailVO couponDetail = couponDetailMap.get(goodsNo);
                if (couponDetail != null) {
                    double threshold = Double.parseDouble(couponDetail.getCounterContext());
                    double itemTotal = originalPrice * detail.getGoodsNum();  // 該商品總金額
                    
                    System.out.println("商品 " + goodsNo + 
                                     " 單價: " + originalPrice + 
                                     " 數量: " + detail.getGoodsNum() +
                                     " 小計: " + itemTotal + 
                                     " 門檻: " + threshold);
                    
                    if (itemTotal >= threshold) {
                        double discountRate = couponDetail.getDisRate();
                        discountedPrice = (int) Math.round(originalPrice * discountRate);
                        System.out.println("商品 " + goodsNo + 
                                         " 達到門檻，原價 " + originalPrice + 
                                         " 打" + (discountRate * 100) + "折後 = " + discountedPrice);
                    } else {
                        System.out.println("商品 " + goodsNo + " 未達門檻，維持原價");
                    }
                } else {
                    System.out.println("商品 " + goodsNo + " 無優惠設定，維持原價");
                }
                
                detail.setProductDisPrice(discountedPrice);
                detail.setMemCouponNo(memCoupon.getMemCouponNo());
                counterOrderDetailService.updateCounterOrderDetail(detail);
            }
            
            // 計算總金額
            int finalTotal = 0;
            System.out.println("\n最終金額計算：");
            for (CounterOrderDetailVO detail : orderDetails) {
                int subtotal = detail.getProductDisPrice() * detail.getGoodsNum();
                finalTotal += subtotal;
                System.out.println("商品 " + detail.getGoodsNo() + 
                                 " 單價: " + detail.getProductDisPrice() + 
                                 " × 數量: " + detail.getGoodsNum() + 
                                 " = " + subtotal);
            }
            System.out.println("訂單總金額: " + finalTotal);
            
            // 更新主訂單
            order.setOrderTotalAfter(finalTotal);
            order.setMemCouponNo(memCoupon.getMemCouponNo());
            counterOrderService.updateCounterOrder49(order);
            
            // 更新優惠券狀態
            memCoupon.setStatus(1);
            memCouponService.updateMemCoupon(memCoupon);
            
        } catch (Exception e) {
            System.err.println("處理訂單折扣時發生錯誤: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}