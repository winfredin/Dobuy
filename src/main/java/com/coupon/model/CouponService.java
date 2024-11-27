package com.coupon.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coupon.controller.HibernateUtil_CompositeQuery_Coupon;

@Service("couponService")
public class CouponService {

    @Autowired
    CouponRepository repository;
    
    @Autowired
    private SessionFactory sessionFactory;

    // 新增优惠券
    public void addCoupon(CouponVO couponVO) {
        repository.save(couponVO);
    }

    // 更新优惠券
    public void updateCoupon(CouponVO couponVO) {
        repository.save(couponVO);
    }

    // 删除优惠券
    public void deleteCoupon(Integer couponNo) {
        if (repository.existsById(couponNo))
            repository.deleteByCouponNo(couponNo);
    }

    // 查询单个优惠券
    public CouponVO getOneCoupon(Integer couponNo) {
        Optional<CouponVO> optional = repository.findById(couponNo);
        return optional.orElse(null);
    }

    // 查询所有优惠券
    public List<CouponVO> getAll() {
        return repository.findAll();
    }

    // 根据条件查询优惠券
    public List<CouponVO> getAll(Map<String, String[]> map) {
        return HibernateUtil_CompositeQuery_Coupon.getAllC(map, sessionFactory.openSession());
    }
    
    //審核優惠券
    @Transactional
    public boolean approveCoupon(int couponNo) {
        int updatedRows = repository.updateCheckStatusByCouponNo(1, couponNo); // 1 表示已審核
        return updatedRows > 0; // 返回是否更新成功
    }
    
    //根據櫃位編號查詢優惠券
    public List<CouponVO> getCounterCoupon35(int counterNo){ //  11/27
    	return repository.findByCounterAndStatusAndCheckStatus(counterNo, 1, 1);
    }
    
}
