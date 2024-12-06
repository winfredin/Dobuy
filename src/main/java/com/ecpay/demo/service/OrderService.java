package com.ecpay.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecpay.payment.integration.AllInOne;
import com.ecpay.payment.integration.domain.AioCheckOutALL;

import java.util.UUID;

@Service
public class OrderService {
    
    private String returnHttps;

    @Transactional
    public String generateEcpayNum(Integer orderId,String productName,Integer total){

        AllInOne all = new AllInOne("");
        String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
        AioCheckOutALL obj = new AioCheckOutALL();
        obj.setMerchantTradeNo(uuId);
        obj.setMerchantTradeDate("2024/12/18 11:54:23");
        obj.setTotalAmount(Integer.toString(total));
        obj.setTradeDesc("test Description");
        obj.setItemName(productName);
        // 交易結果回傳網址，只接受 https 開頭的網站 ;
        obj.setNeedExtraPaidInfo("N");
        // 商店轉跳網址 (Optional)
        obj.setReturnURL(returnHttps+"/successPay/"+orderId);
        String form = all.aioCheckOut(obj, null);
        obj.getMerchantTradeNo();

        return form;
    }

}
