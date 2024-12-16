
package com.ecpay.demo.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.counter.model.CounterService;
import com.counterHome.cartTest.model.CartListVO;
import com.counterHome.counterOrderDetailTest.model.NewCounterOrderDetailService;
import com.counterHome.counterOrderDetailTest.model.NewCounterOrderDetailVO;
import com.counterHome.counterOrderTest.model.NewCounterOrderService;
import com.counterHome.counterOrderTest.model.NewCounterOrderVO;
import com.counterHome.couponTest.model.NewCouponsService;
import com.counterHome.couponTest.model.NewCouponsVO;
import com.counterorder.model.CounterOrderService;
import com.counterorder.model.CounterOrderVO;
import com.counterorderdetail.model.CounterOrderDetailService;
import com.counterorderdetail.model.CounterOrderDetailVO;
import com.ecpay.demo.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goods.model.GoodsService;
import com.goods.model.GoodsVO;

@Controller
public class EcpayController {

	@Autowired
	@Qualifier("redisTemplateDb8")
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	NewCouponsService newCouponsSvc;
	
	@Autowired
	CounterOrderService counterOrderSvc;
	
	@Autowired
	CounterOrderDetailService counterOrderDetailSvc;
	
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
	
	@PostMapping("/necpayCheckout")
	@ResponseBody // 確保返回的是響應正文，而不是模板名稱
	public Object ecpayCheckout(@RequestParam("counterCname") String counterCname,
			@RequestParam("totalAmountBefore") int totalAmountBefore,
			@RequestParam("totalAmountAfter") int totalAmountAfter, @RequestParam("couponNo") int couponNo,
			@RequestParam("recipientName") String recipientName,
			@RequestParam("recipientAddress") String recipientAddress,
			@RequestParam("recipientPhone") String recipientPhone, HttpSession session, Model model) {

		String memNo = (String) session.getAttribute("memNo");
		Integer memNoInt = Integer.parseInt(memNo);
		String counterNoStr = counterSvc.getCounterNoByCounterCname(counterCname);
		Integer counterNo = Integer.parseInt(counterNoStr);
		String key = "cart:" + memNo;
		String imgkey = "img:" + memNo;

		boolean switchNo = true;

		NewCouponsVO newCouponsVO = newCouponsSvc.findCouponsByCouponNo(couponNo);
		if (newCouponsVO == null) {
			newCouponsVO = new NewCouponsVO();
			newCouponsVO.setCouponNo(0); // 设置默认值
		}

		Map<Object, Object> imgMap = redisTemplate.opsForHash().entries(imgkey);
		Map<String, String> base64Map = new HashMap<>(); // 用於儲存後轉換的img

		for (Map.Entry<Object, Object> entry : imgMap.entrySet()) {
			String goodsNo = (String) entry.getKey();
			String base64Image = (String) entry.getValue();
			// 存入新的 Map
			base64Map.put(goodsNo, base64Image);
		}
		List<String> errorMsgs = new ArrayList<String>();
		List<CartListVO> cartList = new ArrayList<CartListVO>();
		ObjectMapper objectMapper = new ObjectMapper();// 轉換格式用
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
		
		CounterOrderVO counterOrderVO = new CounterOrderVO(newCounterOrderVO);//建一個counterorderVO
		
		System.out.println("recipientName" + recipientName);

		List<NewCounterOrderDetailVO> detailList = new ArrayList<NewCounterOrderDetailVO>();
		List<CounterOrderDetailVO> olddetailList = new ArrayList<CounterOrderDetailVO>();

		// 驗證階段
		for (CartListVO CartListVO : cartList) {
			GoodsVO goodsVO = goodsSvc.getOneGoods(CartListVO.getGoodsNo());
			if ((goodsVO.getGoodsAmount() - CartListVO.getGoodsNum()) < 0) {
				errorMsgs.add(CartListVO.getGoodsName() + "庫存只剩" + goodsVO.getGoodsAmount() + "個，請重新下單");
			}
		}

		// 如果驗證失敗，直接返回錯誤訊息
		if (errorMsgs.size() != 0) {
			model.addAttribute("errorMsgs", errorMsgs);
			model.addAttribute("counterCname", counterCname);
			model.addAttribute("totalAmountBefore", totalAmountBefore);
			model.addAttribute("totalAmountAfter", totalAmountAfter);
			model.addAttribute("recipientName", recipientName);
			model.addAttribute("recipientAddress", recipientAddress);
			model.addAttribute("recipientPhone", recipientPhone);
			model.addAttribute("newCouponsVO", newCouponsVO);
			model.addAttribute("cartList", cartList);
			model.addAttribute("base64Map", base64Map);
			return new ModelAndView("/front-end/cartTest/confirm", model.asMap());
		}

		for (CartListVO CartListVO : cartList) {
			GoodsVO goodsVO = goodsSvc.getOneGoods(CartListVO.getGoodsNo());
			//先存order 後面在存detail時 可以用這個VO取的NO
			if (switchNo) {
				counterOrderVO = counterOrderSvc.insert(counterOrderVO);
				newCounterOrderVO = newCounterOrderSvc.savedOrder(newCounterOrderVO);
				switchNo = false;
			}
			//更新數量並儲存
			goodsVO.setGoodsAmount(goodsVO.getGoodsAmount() - CartListVO.getGoodsNum());
			goodsSvc.updateGoods(goodsVO);
			
			//創建一個detail的VO並加入detailList
			NewCounterOrderDetailVO detail = new NewCounterOrderDetailVO(CartListVO);
			
			//創建一個oldDetailVO
			CounterOrderDetailVO oldDetail = new CounterOrderDetailVO(detail);
			oldDetail.setCounterOrderNo(counterOrderVO.getCounterOrderNo());
			detail.setCounterOrder(newCounterOrderVO.getcOrderNo());
			olddetailList.add(oldDetail);
			detailList.add(detail);
		}
		
		//一次性保存detailList到detail表格
		newCounterOrderDetailSvc.saveOrderDetails(detailList);
		counterOrderDetailSvc.saveAll(olddetailList);
		
		try {

			List<String> itemNames = new ArrayList<String>();
			for (CartListVO item : cartList) {
				itemNames.add(item.getGoodsName());
			}
			String aioCheckOutALLForm = orderService.generateEcpayNum(totalAmountAfter, itemNames,
					newCounterOrderVO.getcOrderNo());
			redisTemplate.opsForHash().delete(key, counterNoStr); // 清空購物車
			return aioCheckOutALLForm;
		} catch (Exception e) {
			e.printStackTrace();
			return "付款過程中發生錯誤，請稍後再試";
		}
	}
}
