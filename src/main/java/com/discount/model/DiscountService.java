package com.discount.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.discount.controller.HibernateUtil_CompositeQuery_Discount;

@Service("discountService")
public class DiscountService {

    @Autowired
    DiscountRepository repository;

    @Autowired
    private SessionFactory sessionFactory;

    // 新增平台优惠
    public void addDiscount(DiscountVO discountVO) {
        repository.save(discountVO);
    }

    // 更新平台优惠
    public void updateDiscount(DiscountVO discountVO) {
        repository.save(discountVO);
    }

    // 删除平台优惠
    public void deleteDiscount(Integer disNo) {
        if (repository.existsById(disNo))
            repository.deleteByDisNo(disNo);
    }

    // 查询单个平台优惠
    public DiscountVO getOneDiscount(Integer disNo) {
        Optional<DiscountVO> optional = repository.findById(disNo);
        return optional.orElse(null);
    }

    // 查询所有平台优惠
    public List<DiscountVO> getAll() {
        return repository.findAll();
    }

    // 根据条件查询平台优惠
//    public List<DiscountVO> getAll(Map<String, String[]> map) {
//        return HibernateUtil_CompositeQuery_Discount.getAllD(map, sessionFactory.openSession());
//    }
}
