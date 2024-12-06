package com.ecpay.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ecpay.demo.dto.OrderRequest;
import com.ecpay.demo.dto.OrderResponse;
import com.ecpay.demo.service.OrderService;

@RestController
public class EcpayController {
    @Autowired
    OrderService orderService;
    @PostMapping("/test/ecpay")
    public ResponseEntity<OrderResponse> createOrede(@PathVariable Integer orderId, @RequestBody OrderRequest orderRequest){
       String form= orderService.generateEcpayNum(orderId,orderRequest.getProductName(),orderRequest.getPrice());
        System.out.println(form);
       OrderResponse response=new OrderResponse();
        response.setOrderId(orderId);
        response.setForm(form);
        return  ResponseEntity.status(HttpStatus.CREATED).body(response);

    }
   
}
