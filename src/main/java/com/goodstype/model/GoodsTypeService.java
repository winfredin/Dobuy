package com.goodstype.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goodstype.controller.HibernateUtil_CompositeQuery_GoodsType;

@Service("goodsTypeService")
public class GoodsTypeService {

    @Autowired
    GoodsTypeRepository repository;

    @Autowired
    private SessionFactory sessionFactory;
    
    // 新增商品類別
    public void addGoodsType(GoodsTypeVO goodsTypeVO) {
        repository.save(goodsTypeVO);
    }

    // 更新商品類別
    public void updateGoodsType(GoodsTypeVO goodsTypeVO) {
        repository.save(goodsTypeVO);
    }

    // 刪除商品類別
    public void deleteGoodsType(Integer goodstNo) {
        if (repository.existsById(goodstNo))
            repository.deleteByGoodstNo(goodstNo);
    }

    // 據商品類別編號取得單一商品類別
    public GoodsTypeVO getOneGoodsType(Integer goodstNo) {
        Optional<GoodsTypeVO> optional = repository.findById(goodstNo);
        return optional.orElse(null); // 如果不存在，返回 null
    }

    // 取得所有商品類別
    public List<GoodsTypeVO> getAll() {
        return repository.findAll();
    }
    // 使用條件查詢取得商品類別
	public List<GoodsTypeVO> getAll(Map<String, String[]> map) {
		return HibernateUtil_CompositeQuery_GoodsType.getAllC(map,sessionFactory.openSession());
	}
}
