package com.counterHome.counterOrderTest.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.counter.model.CounterService;
import com.counterHome.cartTest.model.CartListVO;
import com.counterHome.counterOrderDetailTest.model.NewCounterOrderDetailService;
import com.counterHome.counterOrderDetailTest.model.NewCounterOrderDetailVO;
import com.counterHome.counterOrderTest.model.NewCounterOrderService;
import com.counterHome.counterOrderTest.model.NewCounterOrderVO;
import com.counterHome.couponTest.model.NewCouponsService;
import com.counterHome.couponTest.model.NewCouponsVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/cart/test")
public class NewCounterOrderController {

	@Autowired
	NewCouponsService newCouponsSvc;

	@Autowired
	CounterService counterSvc;

	@Autowired
	NewCounterOrderService newCounterOrderSvc;
	
	@Autowired
	NewCounterOrderDetailService newCounterOrderDetailSvc;

	@Autowired
	@Qualifier("redisTemplateDb8")
	private RedisTemplate<String, Object> redisTemplate;

	@PostMapping("toConfirm")
	public String getAfterTotal(
			@RequestParam("counterCname")String counterCname,
            @RequestParam("totalAmountBefore") int totalAmountBefore,
            @RequestParam("totalAmountAfter") int totalAmountAfter,
            @RequestParam("couponNo") int couponNo, 
            HttpSession session, Model model) {

		String memNo = (String) session.getAttribute("memNo");	
		String counterNo = counterSvc.getCounterNoByCounterCname(counterCname);
		NewCouponsVO newCouponsVO = newCouponsSvc.findCouponsByCouponNo(couponNo);
		String key = "cart:" + memNo;
		String imgkey = "img:" + memNo;
		
		ObjectMapper objectMapper = new ObjectMapper(); // 轉換格式用
		
		List<CartListVO> cartList = new ArrayList<CartListVO>();
		Map<Object, Object> imgMap = redisTemplate.opsForHash().entries(imgkey);
		Map<String, String> base64Map = new HashMap<>(); //用於儲存後轉換的img
		
		for (Map.Entry<Object, Object> entry : imgMap.entrySet()) {
		    String goodsNo = (String) entry.getKey();
		    String base64Image = (String) entry.getValue();

		    // 存入新的 Map
		    base64Map.put(goodsNo, base64Image);    
		}
		
		try {
			// 反序列化 JSON 为 List<CartListVO>
			String json  = redisTemplate.opsForHash().get(key, counterNo).toString();
			cartList = objectMapper.readValue(json, new TypeReference<List<CartListVO>>() {
			});
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.addAttribute("counterCname", counterCname);
		model.addAttribute("totalAmountBefore", totalAmountBefore);
		model.addAttribute("totalAmountAfter", totalAmountAfter);
		model.addAttribute("newCouponsVO", newCouponsVO);
		model.addAttribute("cartList", cartList);
		model.addAttribute("base64Map", base64Map);
		
		return "/front-end/cartTest/confirm";
	}

	@PostMapping("generateOrderAndDetail")
	public String generateOrderAndDetail(
			@RequestParam("counterCname")String counterCname,
			@RequestParam("totalAmountBefore") int totalAmountBefore,
			@RequestParam("totalAmountAfter") int totalAmountAfter,
			@RequestParam("couponNo") int couponNo, 
			@RequestParam("recipientName") String recipientName,
			@RequestParam("recipientAddress") String recipientAddress,
			@RequestParam("recipientPhone") String recipientPhone,
			HttpSession session, Model model) {
		
		String memNo = (String) session.getAttribute("memNo");
		Integer memNoInt = Integer.parseInt(memNo);
		String counterNoStr = counterSvc.getCounterNoByCounterCname(counterCname);
		Integer counterNo = Integer.parseInt(counterNoStr);
		String key = "cart:" + memNo;
		
		NewCounterOrderVO newCounterOrderVO = new NewCounterOrderVO();
		newCounterOrderVO.setMemNo(memNoInt);
		newCounterOrderVO.setCounterNo(counterNo);
		newCounterOrderVO.setOrderTotalPriceBefore(totalAmountBefore);
		newCounterOrderVO.setOrderTotalPriceAfter(totalAmountAfter);
		newCounterOrderVO.setCouponNo(couponNo);
		newCounterOrderVO.setReceiverName(recipientName);
		newCounterOrderVO.setReceiverAdr(recipientAddress);
		newCounterOrderVO.setReceiverPhone(recipientPhone);
		newCounterOrderVO.setOrderStatus(0);
		
		newCounterOrderVO = newCounterOrderSvc.savedOrder(newCounterOrderVO);
		System.out.println("編號:======" + newCounterOrderVO.getcOrderNo());
		ObjectMapper objectMapper = new ObjectMapper(); // 轉換格式用
		
		List<CartListVO> cartList = new ArrayList<CartListVO>();
		
		try {
			// 反序列化 JSON 为 List<CartListVO>
			String json  = redisTemplate.opsForHash().get(key, counterNoStr).toString();
			cartList = objectMapper.readValue(json, new TypeReference<List<CartListVO>>() {
			});
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<NewCounterOrderDetailVO> detailList = new ArrayList<NewCounterOrderDetailVO>();
		for(CartListVO cartListVO : cartList) {
			NewCounterOrderDetailVO detail = new NewCounterOrderDetailVO(cartListVO);
			detail.setCounterOrder(newCounterOrderVO.getcOrderNo());
			detailList.add(detail);
		}
		
		newCounterOrderDetailSvc.saveOrderDetails(detailList);
		redisTemplate.opsForHash().delete(key, counterNoStr);
		
		return "/front-end/cartTest/success";
	}

}
