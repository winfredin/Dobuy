package com.ecpay.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecpay.payment.integration.AllInOne;
import com.ecpay.payment.integration.domain.AioCheckOutALL;
import com.ecpay.payment.integration.domain.AioCheckOutOneTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class OrderService {


    @Transactional
    public String generateEcpayNum(Integer total,Integer count,String name, Integer goodsNo,Integer conterOrderNo) {
        // 建立 ECPay SDK 的 AllInOne 物件
        AllInOne all = new AllInOne(""); // 初始化 AllInOne，這裡用實際的商店代號、哈希金鑰、哈希IV

        // 使用 UUID 生成唯一的訂單號
        String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);

        
        AioCheckOutOneTime obj = new AioCheckOutOneTime();
        obj.setMerchantTradeNo(uuId);  // 使用 UUID 作為交易號
        obj.setMerchantTradeDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));  // 設定交易時間
        obj.setTotalAmount(Integer.toString(total));  // 設定總金額
        obj.setTradeDesc("Order Payment");  // 設定交易描述
        obj.setItemName(name);  // 設定商品名稱
        obj.setCustomField1(Integer.toString(count));
        obj.setCustomField2(Integer.toString(goodsNo));
        obj.setCustomField3(Integer.toString(conterOrderNo));
        obj.setReturnURL("https://放置ngrok所供應網址/counterOrderDetail/addCounterOrderDetail");  
        obj.setClientBackURL("http://localhost:8080/member");
        obj.setNeedExtraPaidInfo("N");  // 設定是否需要額外付費資訊

        // 生成並回傳 ECPay 付款表單
        return all.aioCheckOut(obj, null);
    }
}
