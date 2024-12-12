package com.ecpay.demo.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ShoppingCartList.model.ShoppingCartListService;
import com.ShoppingCartList.model.ShoppingCartListVO;
import com.counter.model.CounterVO;
import com.counterorder.model.CounterOrderService;
import com.counterorder.model.CounterOrderVO;
import com.counterorderdetail.model.CounterOrderDetailService;
import com.counterorderdetail.model.CounterOrderDetailVO;
import com.ecpay.demo.service.OrderService;


@RestController
public class EcpayController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CounterOrderService counterOrderSvc;
    @Autowired
    private ShoppingCartListService shoppingSvc;
    @Autowired
    private CounterOrderDetailService counterOrderDetailSvc;

    @PostMapping("/necpayCheckout")
    public String ecpayCheckout(
            @RequestParam("name") String name,
            @RequestParam("address") String address,
            @RequestParam("phone") String phone,
            @RequestParam("memNo") Integer memNo,
            @RequestParam("counterNo") Integer counterNo,
            HttpSession session) {
        
       
        @SuppressWarnings("unchecked")
        List<ShoppingCartListVO> cartItems = (List<ShoppingCartListVO>) session.getAttribute("cartItems");
        if (cartItems == null || cartItems.isEmpty()) {
            return "redirect:/shoppingcartlist"; // 若購物車為空，重定向回購物車頁面
        }

        // 計算總金額
        int totalPrice = cartItems.stream()
                                  .mapToInt(item -> item.getGoodsNum() * item.getGoodsPrice())
                                  .sum();

        // 創建訂單
        CounterOrderVO counterOrderVO = new CounterOrderVO();
        counterOrderVO.setReceiverAdr(address);
        counterOrderVO.setOrderTotalAfter(totalPrice);
        counterOrderVO.setOrderTotalBefore(totalPrice);
        counterOrderVO.setReceiverName(name);
        counterOrderVO.setReceiverPhone(phone);
        counterOrderVO.setCounterNo(counterNo);
        counterOrderVO.setMemNo(memNo);
        counterOrderVO.setOrderStatus(0);
        
        counterOrderSvc.addCounterOrder(counterOrderVO);

        // 獲取訂單號
        Integer counterOrderNo = counterOrderSvc.getone(memNo);

        // 插入訂單明細
        List<CounterOrderDetailVO> details = cartItems.stream()
                .map(cartItem -> {
                    CounterOrderDetailVO detail = new CounterOrderDetailVO();
                    detail.setGoodsNo(cartItem.getGoodsNo());
                    detail.setGoodsNum(cartItem.getGoodsNum());
                    detail.setProductPrice(cartItem.getGoodsPrice());
                    detail.setProductDisPrice(cartItem.getOrderTotalprice());
                    detail.setCounterOrderNo(counterOrderNo);
                    counterOrderDetailSvc.addCounterOrderDetail(detail);
                    return detail;
                   
                }).toList();
       
        

        // 生成 ECPay 表單
        try {
            List<String> itemNames = cartItems.stream()
                                              .map(item -> item.getGoodsName() + " x" + item.getGoodsNum())
                                              .toList();
            String aioCheckOutALLForm = orderService.generateEcpayNum(totalPrice, itemNames, counterOrderNo);
            session.removeAttribute("cartItems"); // 清空購物車
            return aioCheckOutALLForm;
        } catch (Exception e) {
            e.printStackTrace();
            return "付款過程中發生錯誤，請稍後再試";
        }
    }
}
