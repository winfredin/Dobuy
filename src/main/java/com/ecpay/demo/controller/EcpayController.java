
package com.ecpay.demo.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ShoppingCartList.model.ShoppingCartListService;
import com.counter.model.CounterService;
import com.counterHome.cartTest.model.CartListVO;
import com.counterHome.counterOrderDetailTest.model.NewCounterOrderDetailService;
import com.counterHome.counterOrderDetailTest.model.NewCounterOrderDetailVO;
import com.counterHome.counterOrderTest.model.NewCounterOrderService;
import com.counterHome.counterOrderTest.model.NewCounterOrderVO;
import com.counterorder.model.CounterOrderService;
import com.counterorderdetail.model.CounterOrderDetailService;
import com.ecpay.demo.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goods.model.GoodsService;
import com.goods.model.GoodsVO;

@RestController
public class EcpayController {
	
	@Autowired
	@Qualifier("redisTemplateDb8")
	private RedisTemplate<String, Object> redisTemplate;
	
	
	@Autowired
	GoodsService goodsSvc;
	@Autowired
	NewCounterOrderService newCounterOrderSvc;
	
	@Autowired
	NewCounterOrderDetailService newCounterOrderDetailSvc;
	@Autowired
	CounterService counterSvc;
	@Autowired
	private OrderService orderService;
	@Autowired
	private CounterOrderService counterOrderSvc;
	@Autowired
	private ShoppingCartListService shoppingSvc;
	@Autowired
	private CounterOrderDetailService counterOrderDetailSvc;

	@PostMapping("/necpayCheckout")
	public String ecpayCheckout
	( 
			@RequestParam("counterCname")String counterCname,
			@RequestParam("totalAmountBefore") int totalAmountBefore,
			@RequestParam("totalAmountAfter") int totalAmountAfter,
			@RequestParam("couponNo") int couponNo, 
			@RequestParam("recipientName") String recipientName,
			@RequestParam("recipientAddress") String recipientAddress,
			@RequestParam("recipientPhone") String recipientPhone,
			HttpSession session) {
		
		String memNo = (String) session.getAttribute("memNo");
		Integer memNoInt = Integer.parseInt(memNo);
		String counterNoStr = counterSvc.getCounterNoByCounterCname(counterCname);
		Integer counterNo = Integer.parseInt(counterNoStr);
		String key = "cart:" + memNo;
		
		
		List<String> errorMsgs = new ArrayList<String>();
		List<CartListVO> cartList = new ArrayList<CartListVO>();
		ObjectMapper objectMapper = new ObjectMapper();//轉換格式用
		// 反序列化 JSON 为 List<CartListVO>
		try {
			String json = redisTemplate.opsForHash().get(key, counterNoStr).toString();
			cartList = objectMapper.readValue(json, new TypeReference<List<CartListVO>>() {
			});
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		NewCounterOrderVO newCounterOrderVO = new NewCounterOrderVO();
		newCounterOrderVO.setMemNo(memNoInt);
		newCounterOrderVO.setCounterNo(counterNo);
		newCounterOrderVO.setOrderTotalPriceBefore(totalAmountBefore);
		newCounterOrderVO.setOrderTotalPriceAfter(totalAmountAfter);
		newCounterOrderVO.setCouponNo(couponNo);
		newCounterOrderVO.setReceiverName(recipientName);
		newCounterOrderVO.setReceiverAdr(recipientAddress);
		newCounterOrderVO.setReceiverPhone(recipientPhone);
		newCounterOrderVO.setOrderStatus(5);
		
		newCounterOrderVO = newCounterOrderSvc.savedOrder(newCounterOrderVO);
		
		
		List<NewCounterOrderDetailVO> detailList = new ArrayList<NewCounterOrderDetailVO>();
		for(CartListVO CartListVO : cartList) {
			GoodsVO goodsVO = goodsSvc.getOneGoods(CartListVO.getGoodsNo());
			if(goodsVO.getGoodsAmount() - CartListVO.getGoodsNum() < 0) {
				errorMsgs.add(CartListVO.getGoodsName() + "庫存只剩" +
						goodsVO.getGoodsAmount() + "個，請重新下單");
				
			}
			goodsVO.setGoodsAmount(goodsVO.getGoodsAmount() - CartListVO.getGoodsNum());
			NewCounterOrderDetailVO detail = new NewCounterOrderDetailVO(CartListVO);
			detail.setCounterOrder(newCounterOrderVO.getcOrderNo());
			detailList.add(detail);
		}
		newCounterOrderDetailSvc.saveOrderDetails(detailList);
		System.out.println("=======================");
//		// 計算總金額
//		int totalPrice = cartItems.stream().mapToInt(item -> item.getGoodsNum() * item.getGoodsPrice()).sum();

//		// 創建訂單
//		CounterOrderVO counterOrderVO = new CounterOrderVO();
//		counterOrderVO.setReceiverAdr(address);
//		String cleanedAfterNo = afterNo.replaceAll("[^\\d]", "");
//		counterOrderVO.setOrderTotalAfter(Integer.valueOf(cleanedAfterNo));
//		counterOrderVO.setOrderTotalBefore(totalPrice);
//		counterOrderVO.setReceiverName(name);
//		counterOrderVO.setReceiverPhone(phone);
//		counterOrderVO.setCounterNo(counterNo);
//		counterOrderVO.setMemNo(memNo);
//		counterOrderVO.setOrderStatus(0);
//
//		counterOrderSvc.addCounterOrder(counterOrderVO);

//		// 獲取訂單號
//		Integer counterOrderNo = counterOrderSvc.getone(memNo);
//
//		// 插入訂單明細
//		List<CounterOrderDetailVO> details = cartItems.stream().map(cartItem -> {
//			CounterOrderDetailVO detail = new CounterOrderDetailVO();
//			detail.setGoodsNo(cartItem.getGoodsNo());
//			detail.setGoodsNum(cartItem.getGoodsNum());
//			detail.setProductPrice(cartItem.getGoodsPrice());
//			detail.setProductDisPrice(cartItem.getOrderTotalprice());
//			detail.setCounterOrderNo(counterOrderNo);
//			counterOrderDetailSvc.addCounterOrderDetail(detail);
//			return detail;
//
//		}).toList();
		// 生成 ECPay 表單
		try {
//			List<String> itemNames = cartList.stream().map(item -> item.getGoodsName() + " x" + item.getGoodsNum())
//					.toList();
			
			List<String> itemNames = new ArrayList<String>();
			for(CartListVO item : cartList) {
				itemNames.add(item.getGoodsName());
			}
			System.out.println(totalAmountAfter);
			System.out.println(itemNames);
			System.out.println(newCounterOrderVO.getcOrderNo());
			String aioCheckOutALLForm = orderService.generateEcpayNum(totalAmountAfter, itemNames, newCounterOrderVO.getcOrderNo());
			session.removeAttribute("cartItems"); // 清空購物車
			return aioCheckOutALLForm;
		} catch (Exception e) {
			e.printStackTrace();
			return "付款過程中發生錯誤，請稍後再試";
		}
	}
}
