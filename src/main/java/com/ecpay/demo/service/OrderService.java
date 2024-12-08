package com.ecpay.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecpay.payment.integration.AllInOne;
import com.ecpay.payment.integration.domain.AioCheckOutALL;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class OrderService {

    
    private String merchantId;

    
    private String hashKey;

   
    private String hashIV;

    @Transactional
    public String generateEcpayNum(Integer total,Integer count,String name) {
        // 建立 ECPay SDK 的 AllInOne 物件
        AllInOne all = new AllInOne(""); // 初始化 AllInOne，這裡用實際的商店代號、哈希金鑰、哈希IV

        // 使用 UUID 生成唯一的訂單號
        String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);

        
        AioCheckOutALL obj = new AioCheckOutALL();
        obj.setMerchantTradeNo(uuId);  // 使用 UUID 作為交易號
        obj.setMerchantTradeDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));  // 設定交易時間
        obj.setTotalAmount(Integer.toString(total));  // 設定總金額
        obj.setTradeDesc("Order Payment");  // 設定交易描述
        obj.setItemName("商品:"+name+""+"數量:"+count);  // 設定商品名稱
        obj.setReturnURL("https://localhost:8080/ecpay/notify");  // 設定回傳通知的 URL
        obj.setNeedExtraPaidInfo("N");  // 設定是否需要額外付費資訊

        // 生成並回傳 ECPay 付款表單
        return all.aioCheckOut(obj, null);
    }
}
