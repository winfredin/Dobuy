package com.counterHome.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
public class CartControllerTest {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@PostMapping("/add")
	public ResponseEntity<String> addToCart(@RequestBody Map<String, Object> requestBody, HttpSession session) {

		String goodsName = (String)requestBody.get("goodsName");
		Integer goodsPrice = Integer.parseInt(requestBody.get("goodsPrice").toString());
		String goodsNo = (String)requestBody.get("goodsNo");
		// 從 Map 中取出 "goodsNo" 的值
		// 這裡的 goodsNo 對應到前端 JSON 中的鍵值對 { "goodsNo": "12345" }
		// 這裡的 requestBody.get("goodsNo")會返回"12345"

		String memNo = (String) session.getAttribute("memNo"); // 用 session 獲取用戶 ID

		if (memNo == null) {
			System.out.println("沒有帳號");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("請先登入！");
		}

		String key = "cart:" + memNo;
		
		 // 从 Redis 中获取当前商品信息
	    Map<Object, Object> cart = redisTemplate.opsForHash().entries(key); //(goodsNo, goodsInfo)
	    Map<String, Object> goodsInfo;										//(goodsName, xxx) (goodsPrice, xxx)
	    
	    
	    if (cart.containsKey(goodsNo)) {
	        // 如果商品已存在，取出并更新数量
	        goodsInfo = (Map<String, Object>) cart.get(goodsNo); //(goodsNo, goodsInfo) 用 goodsNo(key)取出 goodsInfo << 他是map<String, Object>
	        Integer currentQuantity = (Integer) goodsInfo.get("quantity");
	        goodsInfo.put("quantity", currentQuantity + 1);
	        goodsInfo.put("total", (currentQuantity + 1) * goodsPrice);
	    } else {
	    	// 如果商品不存在，创建新的商品信息
	        goodsInfo = new HashMap<>();
	        goodsInfo.put("goodsName", goodsName);
	        goodsInfo.put("goodsPrice", goodsPrice);
	        goodsInfo.put("quantity", 1);
	        goodsInfo.put("total", goodsPrice);
	    }
//		// 获取当前数量
//		Integer currentQuantity = (Integer) goodsInfo.get("quantity");
//		goodsInfo.put("quantity", currentQuantity == null ? 1 : currentQuantity + 1);

		// 将商品信息存入 Redis
		redisTemplate.opsForHash().put(key, goodsNo, goodsInfo);

		return ResponseEntity.ok("商品已加入購物車");
	}

	@PostMapping("/update")
	public ResponseEntity<Map<String, Object>> updateCart(@RequestBody Map<String, Object> requestBody, HttpSession session) {
	    String goodsNo = (String) requestBody.get("goodsNo");
	    Integer delta = (Integer) requestBody.get("delta");
	    Integer goodsPrice = Integer.parseInt(requestBody.get("goodsPrice").toString());
	    
	    String memNo = (String) session.getAttribute("memNo");
	    
	    if (memNo == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "請先登入！"));
	    }

	    String key = "cart:" + memNo;
	    Map<Object, Object> cart = redisTemplate.opsForHash().entries(key);

	    if (cart == null || cart.isEmpty()) {
	        return ResponseEntity.badRequest().body(Map.of("success", false, "message", "購物車為空！"));
	    }

	    Map<String, Object> goodsInfo = (Map<String, Object>) cart.get(goodsNo);
	    if (goodsInfo == null) {
	        return ResponseEntity.badRequest().body(Map.of("success", false, "message", "商品不存在！"));
	    }

	    Integer currentQuantity = (Integer) goodsInfo.get("quantity");
	    if (currentQuantity + delta <= 0) {
	        redisTemplate.opsForHash().delete(key, goodsNo);
	        return ResponseEntity.ok(Map.of("success", true, "newQuantity", 0));
	    } else {
	    	int newQuantity = currentQuantity + delta;
	        int newTotal = newQuantity * goodsPrice; // 计算总价
	        goodsInfo.put("quantity", newQuantity);
	        goodsInfo.put("total", newTotal);
	        redisTemplate.opsForHash().put(key, goodsNo, goodsInfo);
	        return ResponseEntity.ok(Map.of("success", true, "newQuantity", newQuantity, "newTotal", newTotal));
	    }
	}

	@PostMapping("/remove")
    public ResponseEntity<Map<String, Object>> removeFromCart(@RequestBody String goodsNo, HttpSession session) {
        String memNo = (String) session.getAttribute("memNo");
        String key = "cart:" + memNo;

        redisTemplate.opsForHash().delete(key, goodsNo);
        return ResponseEntity.ok(Map.of("success", true));
    }

    
	@GetMapping("/list35")
	public String listCart(Model model, HttpSession session) {
		String memNo = (String) session.getAttribute("memNo");
		String key = "cart:" + memNo;

		Map<Object, Object> cart = redisTemplate.opsForHash().entries(key);
		model.addAttribute("cart", cart);
		System.out.println(cart);

		return "front-end/cartTest/myCartList"; // 對應購物車頁面模板名
	}

}
