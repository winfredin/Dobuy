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

    // 新增
    public void addDiscount(DiscountVO discountVO) {
        try {
            System.out.println("準備新增優惠: " + discountVO.getDisTitle());
            DiscountVO saved = repository.save(discountVO);
            System.out.println("新增成功, ID: " + saved.getDisNo());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // 更新
    public void updateDiscount(DiscountVO discountVO) {
        repository.save(discountVO);
    }

    // 删除
    public void deleteDiscount(Integer disNo) {
        if (repository.existsById(disNo))
            repository.deleteByDisNo(disNo);
    }

    // 查询
    public DiscountVO getOneDiscount(Integer disNo) {
        Optional<DiscountVO> optional = repository.findById(disNo);
        return optional.orElse(null);
    }

    // 查询所有
    public List<DiscountVO> getAll() {
        return repository.findAll();
    }


}
