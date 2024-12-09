package com.usedecpay.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.DigestUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecpay.payment.integration.AllInOne;
import com.goodstype.model.GoodsTypeService;
import com.member.model.MemberService;
import com.used.model.UsedService;
import com.used.model.UsedVO;
import com.usedecpay.model.UsedEcpayService;
import com.usedorder.model.UsedOrderService;
import com.usedorder.model.UsedOrderVO;

import ecpay.payment.integration.ecpayOperator.EcpayFunction;




@RestController
@RequestMapping("/used")
public class UsedEcpayController {
	
	private final String HashKey="pwFHCqoQZGmho4w6";
	private final String HashIV="EkRm7iFT261dpevs";

	@Autowired
	UsedEcpayService ecpayService;

	@Autowired
	MemberService memberService;

	@Autowired
	UsedOrderService usedOrderService;
	
	@Autowired
	UsedService usedService;
	
	@Autowired
	private GoodsTypeService goodsTypeService;
	
	 
	@PostMapping("/ecpayCheckout")
	public ResponseEntity<String> ecpayCheckout(
								@RequestParam("usedNo") String usedNo,
								@RequestParam("usedName") String usedName,
								@RequestParam("usedCount") String usedCount,
								@RequestParam("usedPrice") String usedPrice,
								@RequestParam("receiverName") String receiverName,
								@RequestParam("receiverPhone") String receiverPhone,
								@RequestParam("receiverAdr") String receiverAdr,
								@RequestParam("totalAmount") String totalAmount,
								HttpSession session,Model model) {
		try {
		//提取商品目前資訊(價格,庫存 etc....)二次確認
		UsedVO usedVO = usedService.getOneUsed(Integer.valueOf(usedNo));
		Integer stocksOnSales=  usedVO.getUsedStocks();
		if(stocksOnSales <  Integer.valueOf(usedCount)) {//Integer.valueOf(usedCount)
			//若是失敗 轉導回首頁或是會員頁面
			String redirectScript = 
				    "<!DOCTYPE html>" +
				    "<html lang='zh-TW'>" +
				    "<head>" +
				    "    <meta charset='UTF-8'>" +
				    "</head>" +
				    "<body>" +
				    "    <script>" +
				    "        alert('結帳時，商品庫存量不足！請稍後再嘗試');" +
				    "        window.location.href='http://localhost:8080/used';" +
				    "    </script>" +
				    "</body>" +
				    "</html>";
			        
			        HttpHeaders headers = new HttpHeaders();
			        headers.setContentType(MediaType.TEXT_HTML);
			        headers.set("Content-Encoding", "UTF-8"); // 確保編碼

			        return new ResponseEntity<>(redirectScript, headers, HttpStatus.OK);
		}else {
			//建立訂單VO以供後續建立訂單(未付款)
			UsedOrderVO usedOrderVO = new UsedOrderVO();
			Integer afterwithholding = stocksOnSales - Integer.valueOf(usedCount) ;
			//預扣庫存
			usedService.withholdingStock(afterwithholding, Integer.valueOf(usedNo));
			//預扣庫存後 開始建立訂單 並轉交資料給api方法
			
			String memNo=(String) session.getAttribute("memNo");
			usedOrderVO.setBuyerNo(Integer.valueOf(memNo));//設定買家編號
			usedOrderVO.setDeliveryStatus((byte) 6);//未付款為 六 byte型態 付款成功後會更改狀態為5 或付款未成功 或超過10 分鐘 會刪除訂單
			usedOrderVO.setReceiverAdr(receiverAdr);//收件地址
			usedOrderVO.setReceiverName(receiverName);//收件人名稱
			usedOrderVO.setReceiverPhone(receiverPhone);//收件人電話
			usedOrderVO.setUsedNo(usedVO.getUsedNo());//物品編號
			usedOrderVO.setUsedPrice(usedVO.getUsedPrice());//物品單價
			usedOrderVO.setUsedCount(Integer.valueOf(usedCount));//物品數量
			usedOrderVO.setUsedTotalPrice(Integer.valueOf(totalAmount));//交易總額
			Timestamp now = Timestamp.valueOf(LocalDateTime.now());
			usedOrderVO.setUsedOrderTime(now);//現在時間
			Integer usedOrderNo = usedOrderService.addUsedOrderwhileCheckOut(usedOrderVO);
			String aioCheckOutALLForm = ecpayService.genAioCheckOutOneTime(usedNo,usedName,usedCount,now,totalAmount,usedOrderNo);
			
			
			
			String countdownScript = 
					"<!DOCTYPE html>" +
				    "<html lang='zh-TW'>" +
				    "<head>" +
				    "    <meta charset='UTF-8'>" +
				    "</head>" +
				    "<body>" +
			        "<div style='text-align:center; color:red; font-weight:bold; margin-bottom:20px;'>" +
			        "請在 <span id='countdown'>600</span> 秒內完成交易以避免訂單被系統刪除" +
			        "</div>" +
			        "<script>" +
			        "let timeLeft = 600;" +
			        "const countdownElement = document.getElementById('countdown');" +
			        "const timer = setInterval(() => {" +
			        "    timeLeft -= 1;" +
			        "    countdownElement.textContent = timeLeft;" +
			        "    if (timeLeft <= 0) {" +
			        "        clearInterval(timer);" +
			        "        alert('交易時間已過期，訂單已被系統取消！');" +
			        "        window.location.href='/front-end/used/shop_detail_used';" +
			        "    }" +
			        "}, 1000);" +
			        "</script>"+
					"</body>" +
				    "</html>";

			    String finalResponse = countdownScript + aioCheckOutALLForm;
			
			
			
			
	        return ResponseEntity.status(HttpStatus.OK)
	                             .contentType(MediaType.TEXT_HTML)
	                             .body(finalResponse);
		}
	}catch(Exception e){//若是失敗 轉導回首頁或是會員頁面
		System.out.println(e.getMessage());
		System.out.println(e.getStackTrace());
		String redirectScript = 
			    "<!DOCTYPE html>" +
			    "<html lang='zh-TW'>" +
			    "<head>" +
			    "    <meta charset='UTF-8'>" +
			    "</head>" +
			    "<body>" +
			    "    <script>" +
			    "        alert('結帳時，系統發生錯誤！請稍後再嘗試');" +
			    "        window.location.href='http://localhost:8080/used';" +
			    "    </script>" +
			    "</body>" +
			    "</html>";
		        
		        HttpHeaders headers = new HttpHeaders();
		        headers.setContentType(MediaType.TEXT_HTML);
		        headers.set("Content-Encoding", "UTF-8"); // 確保編碼

		        return new ResponseEntity<>(redirectScript, headers, HttpStatus.OK);
	}
	}
	
	@PostMapping("/ecpay/notify")
	public String handleECPayNotify(@RequestParam Map<String, String> params) {
	    try {
	        // 提取綠界發送的 CheckMacValue
	        String receivedCheckMacValue = params.get("CheckMacValue");
//	        System.out.println(params);
	        String usedOrderNo= params.get("CustomField1");
	        
	        String RtnCode = params.get("RtnCode");
	        String calculatedCheckMacValue = EcpayFunction.genCheckMacValue( HashKey, HashIV,new Hashtable<>(params));

	        // 驗證 CheckMacValue
	        if (calculatedCheckMacValue.equals(receivedCheckMacValue)) {
	            // 通知有效，處理您的業務邏輯
	        	//將預存訂單永續
	        	if( "1".equals(RtnCode)){
	        	usedOrderService.changeStatusByUsedOrderNo((byte)5, Integer.valueOf(usedOrderNo));
	        	System.out.println("更改訂單狀態成功");
	        	}
	        	System.out.println("o");
	            return "1|OK";
	        } else {
	            // 通知無效，丟棄請求
	        	System.out.println("x");
	            return "0|ERROR";
	        }
	    } catch (Exception e) {
	        // 捕獲其他異常
	        return "0|ERROR: " + e.getMessage();
	    }
	}

	

	

	
}