package com.counterHome.model;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CartServiceTest {
	@Autowired
    private RedisTemplate<String, Object> redisTemplate;
	
	// 添加或更新購物車商品
	public void addToCart(String userId, String productId, int quantity) {
	    String key = "cart:" + userId;
	    redisTemplate.opsForHash().put(key, productId, quantity);
	}
	
	// 獲取購物車中所有商品
	public Map<Object, Object> getCart(String userId) {
	    String key = "cart:" + userId;
	    return redisTemplate.opsForHash().entries(key);
	}
	
	// 刪除購物車中的某個商品
	public void removeFromCart(String userId, String productId) {
	    String key = "cart:" + userId;
	    redisTemplate.opsForHash().delete(key, productId);
	}

	// 清空購物車
	public void clearCart(String userId) {
	    String key = "cart:" + userId;
	    redisTemplate.delete(key);
	}
	
	
}
