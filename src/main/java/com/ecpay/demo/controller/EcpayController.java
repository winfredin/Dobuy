package com.ecpay.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecpay.demo.service.OrderService;

@RestController
public class EcpayController {

    @Autowired
    private OrderService orderService;  // 注入 OrderService

    @PostMapping("/ecpayCheckout")
    public String ecpayCheckout(@RequestParam("orderTotalprice") Integer price,
                                 @RequestParam("goodsNum") Integer goodsNum,
                                 @RequestParam("goodsName") String goodsName) {
        String aioCheckOutALLForm = orderService.generateEcpayNum(price, goodsNum, goodsName);
        return aioCheckOutALLForm;
    }

}