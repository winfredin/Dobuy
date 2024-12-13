package com.ecpay.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecpay.payment.integration.AllInOne;
import com.ecpay.payment.integration.domain.AioCheckOutALL;
import com.ecpay.payment.integration.domain.AioCheckOutOneTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    
    @Transactional
    public String generateEcpayNum(Integer total, List<String> itemNames, Integer counterOrderNo) {
        AllInOne all = new AllInOne("");

        try {
            String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
            String itemName = String.join("#", itemNames);

            // 使用 AioCheckOutALL
            AioCheckOutALL obj = new AioCheckOutALL();
            
            // 設定基本必要參數
            obj.setMerchantID("3002607");
            obj.setMerchantTradeNo(uuId);
            obj.setMerchantTradeDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
            obj.setTotalAmount(total.toString());
            obj.setTradeDesc("DO BUY商品訂單");
            obj.setItemName(itemName);
            
            // 設定付款結果通知參數
            obj.setReturnURL("https://9c64-111-249-27-13.ngrok-free.app/ecpay/callback");
            obj.setOrderResultURL("http://localhost:8080/member");
            obj.setClientBackURL("http://localhost:8080/member");
            // 設定其他必要參數
            obj.setNeedExtraPaidInfo("N");
            
            // 直接使用 aioCheckOut 方法，不要呼叫 getRedirectHtml
            return all.aioCheckOut(obj, null);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("生成綠界支付表單時發生錯誤: " + e.getMessage());
        }
    }
}