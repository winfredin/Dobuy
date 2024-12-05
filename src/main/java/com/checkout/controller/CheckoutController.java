package com.checkout.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ShoppingCartList.model.ShoppingCartListService;
import com.ShoppingCartList.model.ShoppingCartListVO;
import com.counterorder.model.CounterOrderService;
import com.counterorder.model.CounterOrderVO;
import com.coupon.model.CouponService;
import com.coupon.model.CouponVO;
import com.coupondetail.model.CouponDetailService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    ShoppingCartListService shoppingCartListService;

    @Autowired
    CouponService couponService;
    
    @Autowired
    CouponDetailService couponDetailService;

    @Autowired
    CounterOrderService counterOrderService;

//    @Autowired
//    CounterOrderDetailService counterOrderDetailService;

//    @PostMapping("/confirm")
//    public String checkout(HttpSession session, Model model) {
    
        // 1. 檢查會員是否登錄=========================================柏諭
    	
//        Integer memNo = (Integer) session.getAttribute("memNo");
//        if (memNo == null) {
//            return "redirect:/mem/login"; // 未登錄，跳轉至登錄頁面
//        }

        
        
        
        // 2. 獲取購物車資料=========================================柏諭
        
//        List<ShoppingCartListVO> cartItems = shoppingCartListService.getCartItemsByMemNo(memNo);
//        if (cartItems.isEmpty()) {
//            model.addAttribute("error", "購物車為空，無法結帳。");
//            return "redirect:/shoppingcartlist/listAllShoppingCartList";
//        }
//		return null;

        
        
        
        // 3. 檢查並計算折扣=========================================柏翔
        
//        Map<Integer, List<ShoppingCartListVO>> itemsByCounter = cartItems.stream()
//                .collect(Collectors.groupingBy(ShoppingCartListVO::getCounterNo));
//        Map<Integer, CouponVO> appliedCoupons = couponService.getApplicableCouponsForCart(memNo, cartItems);

        
        
        
        // 4. 創建訂單和訂單明細=========================================柏諭
        
//        for (Map.Entry<Integer, List<ShoppingCartListVO>> entry : itemsByCounter.entrySet()) {
//            Integer counterNo = entry.getKey();
//            List<ShoppingCartListVO> counterItems = entry.getValue();

            
            
            
            // 計算訂單金額和折扣=========================================柏翔
            
//            int totalBefore = counterItems.stream().mapToInt(item -> item.getGoodsPrice() * item.getGoodsNum()).sum();
//            int totalAfter = totalBefore;
            
            
            
            

            // 應用優惠券（如果有）=========================================柏翔
            
//            if (appliedCoupons.containsKey(counterNo)) {
//                totalAfter = couponService.applyDiscount(totalBefore, appliedCoupons.get(counterNo));
//            }
            
            
            
            

            // 創建訂單=========================================柏諭
            
//            CounterOrderVO order = counterOrderService.createOrder(memNo, counterNo, totalBefore, totalAfter);
            
            
            
            

            // 創建訂單明細=========================================柏諭
            
//            for (ShoppingCartListVO item : counterItems) {
//                counterOrderDetailService.createOrderDetail(order.getCounterOrderNo(), item, appliedCoupons.get(counterNo));
//            }
        

        
        
        
        
        // 5. 清空購物車=========================================
        
//        shoppingCartListService.clearCart(memNo);
//
//        return "redirect:/order/success";
    }
    
//}
