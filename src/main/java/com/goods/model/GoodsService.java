package com.goods.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goods.controller.HibernateUtil_CompositeQuery_Goods;

@Service("goodsService")
public class GoodsService {

    @Autowired
    GoodsRepository repository;

    @Autowired
    private SessionFactory sessionFactory;

//	新增商品
    public void addGoods(GoodsVO goodsVO) {
        repository.save(goodsVO);
    }

//  更新商品
    public void updateGoods(GoodsVO goodsVO) {
        repository.save(goodsVO);
    }

//  刪除商品
    public void deleteGoods(Integer goodsNo) {
        if (repository.existsById(goodsNo)) 
            repository.deleteByGoodsno(goodsNo);
    }

//  據商品編號取得單一商品
    public GoodsVO getOneGoods(Integer goodsNo) {
        Optional<GoodsVO> optional = repository.findById(goodsNo);
        return optional.orElse(null); // 如果不存在，返回 null
    }

//  取得所有商品
    public List<GoodsVO> getAll() {
        return repository.findAll();
    }

//  使用條件查詢取得商品
    public List<GoodsVO> getAll(Map<String, String[]> map) {
        return HibernateUtil_CompositeQuery_Goods.getAllC(map, sessionFactory.openSession());
    }
    
//  據櫃位編號取得全部商品
    public List<GoodsVO> getOneCounter35(Integer counterNo) {
        return repository.getOneCounter35(counterNo); // 如果不存在，返回 null
    }
}
