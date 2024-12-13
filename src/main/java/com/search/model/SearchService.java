package com.search.model;


import com.goods.model.GoodsRepository;
import com.goods.model.GoodsVO;
import com.used.model.UsedRepository;
import com.used.model.UsedVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private static final String REDIS_PREFIX = "search:";

    @Autowired
    @Qualifier("redisTemplateDb7")
    private RedisTemplate<String, List<GoodsVO>> redisTemplate;  // 使用 String 序列化類型，因為儲存的是 JSON 字串
    @Autowired
    @Qualifier("redisTemplateDb6")
    private RedisTemplate<String, List<UsedVO>> redisTemplate1;
    
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private UsedRepository usedRepository;

    public List<GoodsVO> searchProducts(String query) {
        // Redis 的鍵
    	String redisKey = REDIS_PREFIX + "goods:" + query; // 用於商品
    	

        // 1. 檢查 Redis 是否有快取
        List<GoodsVO> goods = redisTemplate.opsForValue().get(redisKey);
        if (goods != null) {
            System.out.println("從 Redis 快取獲取數據");
            return goods;
        }

        // 2. 如果 Redis 沒有，查詢資料庫
        goods = goodsRepository.findByGoodsNameContaining(query);
        List<GoodsVO> simplifiedGoods = new ArrayList<>();  // 初始化 simplifiedGoods

        if (goods.isEmpty()) {
            System.out.println("資料庫查詢結果為空");
        }
            System.out.println("從資料庫查詢數據，查詢結果: ");
            goods.forEach(g -> System.out.println("商品編號: " + g.getGoodsNo() + ", 商品名稱: " + g.getGoodsName()));

            simplifiedGoods = goods.stream()
                .map(g -> new GoodsVO(g.getGoodsNo(), g.getGoodsName()))  // 只保留需要的資料
                .collect(Collectors.toList());
            redisTemplate.opsForValue().set(redisKey, simplifiedGoods, Duration.ofMinutes(5));  // 儲存簡化資料到 Redis
            System.out.println("儲存到 Redis 成功: " + redisKey);
        

        return simplifiedGoods;  // 返回結果
    }
    
    public List<UsedVO> searchUsed(String query) {
        // Redis 的鍵
    	String redisKey = REDIS_PREFIX + "used:" + query; 

        // 1. 檢查 Redis 是否有快取
        List<UsedVO> used = redisTemplate1.opsForValue().get(redisKey);
        if (used != null) {
            System.out.println("從 Redis 快取獲取數據");
            return used;
        }

        // 2. 如果 Redis 沒有，查詢資料庫
        used = usedRepository.findByUsedNameContaining(query);
        List<UsedVO> simplifiedGoods = new ArrayList<>();  // 初始化 simplifiedGoods

        if (used.isEmpty()) {
            System.out.println("資料庫查詢結果為空");
        }
            System.out.println("從資料庫查詢數據，查詢結果: ");
            used.forEach(g -> System.out.println("商品編號: " + g.getUsedNo() + ", 商品名稱: " + g.getUsedName()));

              simplifiedGoods = used.stream()
                .map(g -> new UsedVO(g.getUsedNo(), g.getUsedName())) // 只保留需要的資料
                .collect(Collectors.toList());
            redisTemplate1.opsForValue().set(redisKey, simplifiedGoods, Duration.ofMinutes(5));  // 儲存簡化資料到 Redis
            System.out.println("儲存到 Redis 成功: " + redisKey);
        

        return simplifiedGoods;  // 返回結果
    }
    
    
}
