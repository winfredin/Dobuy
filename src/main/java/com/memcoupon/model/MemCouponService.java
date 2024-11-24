package com.memcoupon.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.memcoupon.controller.HibernateUtil_CompositeQuery_MemCoupon;

@Service("memCouponService")
public class MemCouponService {

    @Autowired
    MemCouponRepository repository;

    @Autowired
    private SessionFactory sessionFactory;

    // 新增會員優惠券
    public void addMemCoupon(MemCouponVO memCouponVO) {
        repository.save(memCouponVO);
    }

    // 更新會員優惠券
    public void updateMemCoupon(MemCouponVO memCouponVO) {
        repository.save(memCouponVO);
    }

    // 刪除會員優惠券
    public void deleteMemCoupon(Integer memCouponNo) {
        if (repository.existsById(memCouponNo))
            repository.deleteByMemCouponNo(memCouponNo);
    }

    // 查詢單個會員優惠券
    public MemCouponVO getOneMemCoupon(Integer memCouponNo) {
        Optional<MemCouponVO> optional = repository.findById(memCouponNo);
        return optional.orElse(null);
    }

    // 查詢所有會員優惠券
    public List<MemCouponVO> getAll() {
        return repository.findAll();
    }

    // 查詢特定會員的所有優惠券
    public List<MemCouponVO> getAllByMemNo(Integer memNo) {
        return repository.findByMemNo(memNo);
    }

    // 根據條件查詢會員優惠券
    public List<MemCouponVO> getAll(Map<String, String[]> map) {
        return HibernateUtil_CompositeQuery_MemCoupon.getAllC(map, sessionFactory.openSession());
    }
}
