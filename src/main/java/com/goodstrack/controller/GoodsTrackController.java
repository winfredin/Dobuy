package com.goodstrack.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.goods.model.GoodsLightVO;
import com.goods.model.GoodsService;

@Controller
@RequestMapping("/goodsTrack")
public class GoodsTrackController {

	// 以下昱夆新增
	@Autowired
	@Qualifier("redisTemplateDb10")
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	GoodsService goodsSvc;

	@PostMapping("/add")
	public ResponseEntity<Map<String, String>> addToMyFavoriteList(@RequestBody Map<String, Object> requestBody,
			HttpSession session) {
		String goodsNo = (String) requestBody.get("goodsNo");
		if (goodsNo == null) {
			Map<String, String> response = Map.of("success", "false", "message", "收藏失敗，請稍後再試");
			return ResponseEntity.ok(response);
		}
		String memNo = (String) session.getAttribute("memNo"); // 用 session 獲取用戶 ID
		String MyListKey = "myList:" + memNo; // 組合成key
		redisTemplate.opsForSet().add(MyListKey, goodsNo);
		String setSize = redisTemplate.opsForSet().size(MyListKey).toString();
		Map<String, String> response = Map.of("success", "true", "message", "收藏成功", "currentSize", setSize);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/remove")
	public ResponseEntity<Map<String, String>> removeGoods(@RequestBody Map<String, Object> requestBody,
			HttpSession session) {
		String goodsNo = (String) requestBody.get("goodsNo");
		if (goodsNo == null) {
			Map<String, String> response = Map.of("success", "false", "message", "移除失敗");
			return ResponseEntity.ok(response);
		}
		String memNo = (String) session.getAttribute("memNo"); // 用 session 獲取用戶 ID
		String MyListKey = "myList:" + memNo; // 組合成key
		redisTemplate.opsForSet().remove(MyListKey, goodsNo);

		String setSize = redisTemplate.opsForSet().size(MyListKey).toString();
		Map<String, String> response = Map.of("success", "true", "message", "移除成功", "currentSize", setSize);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/getFavorites")
	public String getMyFavoriteList(HttpSession session, Model model) {
		String memNo = (String) session.getAttribute("memNo"); // 从 session 获取用户 ID
		String myListKey = "myList:" + memNo; // 组合成 key

		// 从 Redis 中获取集合
		Set<String> goodsSet = redisTemplate.opsForSet().members(myListKey);
		List<GoodsLightVO> goodsLightVO = new ArrayList<GoodsLightVO>();
		for (String goodsNo : goodsSet) {
			goodsLightVO.add(new GoodsLightVO(goodsSvc.getOneGoods(Integer.parseInt(goodsNo))));
		}
		
		model.addAttribute("goodsLightVO", goodsLightVO);
		return "front-end/member/myFavorite";
	}
	
	@GetMapping("/getFavoritesfragment")
	public String getMyFavoriteListfragment(HttpSession session, Model model) {
		String memNo = (String) session.getAttribute("memNo"); // 从 session 获取用户 ID
		String myListKey = "myList:" + memNo; // 组合成 key
		
		// 从 Redis 中获取集合
		Set<String> goodsSet = redisTemplate.opsForSet().members(myListKey);
		List<GoodsLightVO> goodsLightVO = new ArrayList<GoodsLightVO>();
		for (String goodsNo : goodsSet) {
			goodsLightVO.add(new GoodsLightVO(goodsSvc.getOneGoods(Integer.parseInt(goodsNo))));
		}
		
		model.addAttribute("goodsLightVO", goodsLightVO);
		return "front-end/member/myFavorite :: myFavoriteFragment";
	}
	
	
}
