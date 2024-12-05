package com.usedecpay.model;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usedorder.model.UsedOrderService;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutOneTime;

@Service
public class EcpayService {
	
	@Autowired
	UsedOrderService usedOrderService;
	
	
	public static String genAioCheckOutOneTime(){
		
		String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
		
		AllInOne all = new AllInOne("");
		
		AioCheckOutOneTime obj = new AioCheckOutOneTime();
		
		//獲取當前交易時間 "2017/01/01 08:05:23"
		
		obj.setMerchantTradeNo(uuId);
		obj.setMerchantTradeDate("2017/01/01 08:05:23");
		obj.setTotalAmount("50");
		obj.setTradeDesc("test Description");
		obj.setItemName("TestItem");
		obj.setReturnURL("http://localhost:8080/used/select_page");
		obj.setOrderResultURL("http://localhost:8080/used/select_page");
		obj.setNeedExtraPaidInfo("N");
		obj.setRedeem("N");
		obj.setClientBackURL("http://localhost:8080/used/select_page");
		String form = all.aioCheckOut(obj, null);
		return form;
	}
}
