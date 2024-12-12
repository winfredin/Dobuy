package com.counterHome.cartTest.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.counter.model.CounterService;
import com.counterHome.cartTest.model.CartListVO;
import com.counterHome.cartTest.model.CartServiceTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/cart")
public class CartControllerTest {

	@Autowired
	@Qualifier("redisTemplateDb8")
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	CartServiceTest cartServiceTestSvc;

	@Autowired
	CounterService counterSvc;

	// 統一整理錯誤訊息
	private ResponseEntity<String> buildErrorResponse(HttpStatus status, String message) {
		return ResponseEntity.status(status).body(message);
	}

	@PostMapping("/add")
	public ResponseEntity<String> addToCart(@RequestBody Map<String, Object> requestBody, HttpSession session) {

		String goodsNo = (String) requestBody.get("goodsNo");
		// 從 Map 中取出 "goodsNo" 的值
		// 這裡的 goodsNo 對應到前端 JSON 中的鍵值對 { "goodsNo": "12345" }
		// 這裡的 requestBody.get("goodsNo")會返回"12345"
		String goodsName = (String) requestBody.get("goodsName");
		String goodsPriceStr = (String)requestBody.get("goodsPrice");
		String counterNoStr =(String) requestBody.get("counterNo");
		String base64Image = (String) requestBody.get("base64Image");

		if (goodsName == null || goodsNo == null || counterNoStr == null || goodsPriceStr == null) {
			return ResponseEntity.badRequest().body("請求參數缺失");
		}

		Integer goodsPrice = Integer.parseInt(requestBody.get("goodsPrice").toString());

		String memNo = (String) session.getAttribute("memNo"); // 用 session 獲取用戶 ID

		if (memNo == null) {
			return buildErrorResponse(HttpStatus.UNAUTHORIZED, "請先登入");
		}
		String cartKey = "cart:" + memNo; // 組合成key
		String imgKey = "img:" + memNo; // 組合成key

		// 从 Redis 中获取当前商品信息
		Map<Object, Object> cart = redisTemplate.opsForHash().entries(cartKey); // (counterNo, cartListVO)

		List<CartListVO> cartList;// (存不同counterNo的List)

		ObjectMapper objectMapper = new ObjectMapper(); // 轉換格式用

		// 检查 counterNo 是否存在
		if (cart.containsKey(counterNoStr)) {
			// 如果存在，反序列化 JSON 为 List<CartListVO>
			try {
				String json = (String) cart.get(counterNoStr);
				cartList = objectMapper.readValue(json, new TypeReference<List<CartListVO>>() {
				});
			} catch (Exception e) {
				e.printStackTrace();
				return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "讀取購物車失敗");
			}
		} else {
			// 如果不存在，初始化一个新的商品列表
			cartList = new ArrayList<>();
		}

		// 呼叫判断方法，確認商品是否已存在，若已存在，直接在方法裡面更新
		boolean itemFound = cartServiceTestSvc.IsExistingItem(cartList, goodsNo);

		// 如果回傳false 表示沒有找到重複的商品
		if (!itemFound) {
			CartListVO cartVO = new CartListVO();

			cartVO.setMemNo(Integer.parseInt(memNo));
			cartVO.setGoodsNo(Integer.parseInt(goodsNo));
			cartVO.setGoodsName(goodsName);
			cartVO.setGoodsPrice(goodsPrice);
			cartVO.setGoodsNum(1);
			cartVO.setOrderTotalPrice(1 * goodsPrice);

			cartList.add(cartVO);
			
			redisTemplate.opsForHash().put(imgKey, goodsNo, base64Image);//沒有找到商品才存圖片，要存會員編號?
		}

		// 將商品的list轉成json格式
		String json = null;

		try {
			json = objectMapper.writeValueAsString(cartList);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "保留商品數據發生異常");
		}
		// 将商品信息存入 Redis
		redisTemplate.opsForHash().put(cartKey, counterNoStr, json);

		return ResponseEntity.ok("商品已加入購物車");
	}

	@PostMapping("/update")
	public ResponseEntity<Map<String, Object>> updateCart(@RequestBody Map<String, Object> requestBody,
			HttpSession session) {
		String counterCname = (String) requestBody.get("counterCname");
		Integer goodsNo = Integer.parseInt(requestBody.get("goodsNo").toString());
		Integer delta = (Integer) requestBody.get("delta");
		Integer goodsPrice = Integer.parseInt(requestBody.get("goodsPrice").toString());

		String counterNo = counterSvc.getCounterNoByCounterCname(counterCname);

		String memNo = (String) session.getAttribute("memNo");
		if (memNo == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "請先登入！"));
		}

		String key = "cart:" + memNo;
		Map<Object, Object> cart = redisTemplate.opsForHash().entries(key);

		if (cart == null || cart.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("success", false, "message", "購物車為空！"));
		}

		ObjectMapper objectMapper = new ObjectMapper(); // 轉換格式用
		String json = (String) redisTemplate.opsForHash().get(key, counterNo);

		try {
			List<CartListVO> cartList = objectMapper.readValue(json, new TypeReference<List<CartListVO>>() {
			});
			for (CartListVO cartVO : cartList) {
				if (cartVO.getGoodsNo().equals(goodsNo)) {
					// 獲取當前數量
					Integer currentQuantity = cartVO.getGoodsNum();
					Integer newQuantity = currentQuantity + delta;
					// 判斷是否刪除
					if (newQuantity <= 0) {
						cartList.remove(cartVO); // 刪除該商品
						if (cartList.isEmpty()) {
							// 如果櫃位沒有商品了，從 cart 中移除
							redisTemplate.opsForHash().delete(key, counterNo);
							return ResponseEntity.ok(Map.of("success", true, "newQuantity", newQuantity, "newTotal", 0));
							// 直接返回，避免後續覆蓋操作
						}
					} else {
						// 更新數量與總價
						Integer newTotal = newQuantity * goodsPrice;
						cartVO.setGoodsNum(newQuantity);
						cartVO.setOrderTotalPrice(newTotal);
					}

					// 序列化並存回 cart
					String updatedJson = objectMapper.writeValueAsString(cartList);
					redisTemplate.opsForHash().put(key, counterNo, updatedJson);
					// 返回成功響應
					return ResponseEntity.ok(Map.of("success", true, "newQuantity", newQuantity > 0 ? newQuantity : 0,
							"newTotal", newQuantity > 0 ? newQuantity * goodsPrice : 0));
				}
			}
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 如果沒有找到商品
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "商品未找到"));

	}

	@PostMapping("/remove")
	public ResponseEntity<Map<String, Object>> removeFromCart(@RequestBody Map<String, Object> requestBody,
			HttpSession session) {
		Integer goodsNo = Integer.parseInt(requestBody.get("goodsNo").toString());
		String counterCname = (String) requestBody.get("counterCname");
		String memNo = (String) session.getAttribute("memNo");
		String key = "cart:" + memNo;

		String counterNo = counterSvc.getCounterNoByCounterCname(counterCname);

		ObjectMapper objectMapper = new ObjectMapper(); // 轉換格式用
		// 獲取指定櫃位的商品列表 JSON
		String json = (String) redisTemplate.opsForHash().get(key, counterNo);
		if (json == null) {
			return ResponseEntity.ok(Map.of("success", false, "message", "櫃位不存在"));
		}

		// 反序列化為商品列表
		try {
			List<CartListVO> cartList = objectMapper.readValue(json, new TypeReference<List<CartListVO>>() {
			});
			for (CartListVO cartVO : cartList) {
				if (cartVO.getGoodsNo() == goodsNo) {
					cartList.remove(cartVO); // 刪除該商品
					if (cartList.isEmpty()) {
						// 如果櫃位沒有商品了，從 cart 中移除
						redisTemplate.opsForHash().delete(key, counterNo);
						return ResponseEntity.ok(Map.of("success", true)); // 直接返回，避免後續覆蓋操作
					}
					// 序列化並存回 cart
					String updatedJson = objectMapper.writeValueAsString(cartList);
					redisTemplate.opsForHash().put(key, counterNo, updatedJson);
					break;
				}

			}
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok(Map.of("success", true));
	}

	@GetMapping("/list35")
	public String listCart(Model model, HttpSession session) {

		String memNo = (String) session.getAttribute("memNo");
		String cartkey = "cart:" + memNo;
		String imgkey = "img:" + memNo;

		Map<Object, Object> imgMap = redisTemplate.opsForHash().entries(imgkey);
		Map<String, String> base64Map = new HashMap<>(); //用於儲存後轉換的img
		Map<Object, Object> cartMap = redisTemplate.opsForHash().entries(cartkey);
		Map<String, List<CartListVO>> convertedCart = new HashMap<>(); // 用於存儲轉換後的數據
		
		ObjectMapper objectMapper = new ObjectMapper(); // 轉換格式用
		
		for (Map.Entry<Object, Object> entry : imgMap.entrySet()) {
		    String goodsNo = (String) entry.getKey(); // 商品编号
		    String base64Image = (String) entry.getValue(); // 图片的 byte[]

		    // 存入新的 Map
		    base64Map.put(goodsNo, base64Image);
		    
		}

		
		try {
			// 遍历 Redis 中的购物车数据
			for (Map.Entry<Object, Object> entry : cartMap.entrySet()) {
				String counterNoStr = entry.getKey().toString(); // 获取 key（櫃位编号）
				String json = entry.getValue().toString(); // 获取 value（JSON 格式的 List<CartListVO>）

				String counterCname = counterSvc.getCounterCnameByCounterNo(counterNoStr); // 用櫃位編號找出櫃位名稱
				// 反序列化 JSON 为 List<CartListVO>
				List<CartListVO> cartList = objectMapper.readValue(json, new TypeReference<List<CartListVO>>() {
				});
				convertedCart.put(counterCname, cartList); // 存入反序列化后的 Map
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.addAttribute("base64Map", base64Map);
		model.addAttribute("cart", convertedCart);
		return "front-end/cartTest/myCartList"; // 對應購物車頁面模板名
	}

}
