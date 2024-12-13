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
	        // 初始化 AllInOne
	        AllInOne all = new AllInOne("");

	        // 使用 UUID 生成唯一訂單號
	        String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);

	        // 整合商品資訊（名稱與數量）
	        StringBuilder itemNameBuilder = new StringBuilder();
	        for (String itemName : itemNames) {
	            itemNameBuilder.append(itemName).append("#"); // 多件商品用 `#` 分隔
	        }
	        String itemName = itemNameBuilder.toString();

	        // 設定 ECPay 結帳物件
	        AioCheckOutOneTime obj = new AioCheckOutOneTime();
	        obj.setMerchantTradeNo(uuId);  // 訂單編號
	        obj.setMerchantTradeDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())); // 訂單日期
	        obj.setTotalAmount(Integer.toString(total));  // 設定總金額
	        obj.setTradeDesc("Order Payment");  // 設定交易描述
	        obj.setItemName(itemName);  // 設定商品名稱
	        obj.setCustomField1(Integer.toString(counterOrderNo));  // 自定義欄位存放訂單編號
	        obj.setReturnURL("https://ba37-111-243-140-9.ngrok-free.app/counterOrderDetail/addCounterOrderDetail");  // ECPay 伺服器通知網址
	        obj.setClientBackURL("http://localhost:8080/member"); // 用戶返回網址
	        obj.setNeedExtraPaidInfo("N");  // 是否需要額外付費資訊

	        return all.aioCheckOut(obj, null);
	    }
}
