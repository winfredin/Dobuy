package com.ecpay.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.counter.model.CounterVO;
import com.counterorder.model.CounterOrderService;
import com.counterorder.model.CounterOrderVO;
import com.ecpay.demo.service.OrderService;

@RestController
public class EcpayController {

    @Autowired
    private OrderService orderService;  // 注入 OrderService

    @Autowired
    CounterOrderService counterOrderSvc;
    @PostMapping("/ecpayCheckout")
    public String ecpayCheckout(@RequestParam("orderTotalprice") Integer price,
                                 @RequestParam("goodsNum") Integer goodsNum,
                                 @RequestParam("goodsName") String goodsName,
                                 @RequestParam("goodsNo") Integer goodsNo,
                                 @RequestParam("counterNo") Integer counterNo,
                                 @RequestParam("memNo") Integer memNo,
                                 @RequestParam("name") String name,
                                 @RequestParam("address") String address,
                                 @RequestParam("phone") String phone
                                 ) {
    	CounterOrderVO counterOrderVO=new CounterOrderVO();
    	counterOrderVO.setReceiverAdr(address);
    	counterOrderVO.setOrderTotalAfter(price);
    	counterOrderVO.setOrderTotalBefore(price);
    	counterOrderVO.setReceiverName(name);
    	counterOrderVO.setReceiverPhone(phone);
    	counterOrderVO.setCounterNo(counterNo);
    	counterOrderVO.setMemNo(memNo);
    	counterOrderVO.setOrderStatus(0);
    	counterOrderSvc.addCounterOrder(counterOrderVO);
    	Integer counterOrderNo =counterOrderSvc.getone(memNo);
        String aioCheckOutALLForm = orderService.generateEcpayNum(price, goodsNum, goodsName,goodsNo,counterOrderNo);
        return aioCheckOutALLForm;
    }
    
}