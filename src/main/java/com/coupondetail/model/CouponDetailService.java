package com.coupondetail.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coupondetail.controller.HibernateUtil_CompositeQuery_CouponDetail;

@Service("couponDetailService")
public class CouponDetailService {

    @Autowired
    CouponDetailRepository repository;
    
    @Autowired
    private SessionFactory sessionFactory;
    // 新增优惠券明细
    public void addCouponDetail(CouponDetailVO couponDetailVO) {
        repository.save(couponDetailVO);
    }
    // 更新优惠券明细
    public void updateCouponDetail(CouponDetailVO couponDetailVO) {
        repository.save(couponDetailVO);
    }
    // 删除优惠券明细
    public void deleteCouponDetail(Integer couponDetailNo) {
        if (repository.existsById(couponDetailNo))
            repository.deleteByCouponDetailNo(couponDetailNo);
    }
    // 查询单个优惠券明细
    public CouponDetailVO getOneCouponDetail(Integer couponDetailNo) {
        Optional<CouponDetailVO> optional = repository.findById(couponDetailNo);
        return optional.orElse(null);
    }
    // 查询所有优惠券明细
    public List<CouponDetailVO> getAll() {
        return repository.findAll();
    }
    // 根据条件查询优惠券明细
    public List<CouponDetailVO> getAll(Map<String, String[]> map) {
        return HibernateUtil_CompositeQuery_CouponDetail.getAllC(map, sessionFactory.openSession());
    }
}
