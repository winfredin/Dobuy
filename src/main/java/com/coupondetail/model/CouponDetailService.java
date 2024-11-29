package com.coupondetail.model;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coupon.model.CouponRepository;
import com.coupon.model.CouponVO;
import com.coupondetail.controller.HibernateUtil_CompositeQuery_CouponDetail;

@Service("couponDetailService")
public class CouponDetailService {

    @Autowired
    CouponDetailRepository repository;
    
    @Autowired
    CouponRepository couponRepository;
    
    @Autowired
    private SessionFactory sessionFactory;
    
    @Transactional(rollbackOn = Exception.class)  // 明確指定回滾條件
    public CouponVO addCouponWithDetails(CouponVO couponVO) {
        try {
            // 設置明細的關聯
            if (couponVO.getCouponDetails() != null) {
                for (CouponDetailVO detail : couponVO.getCouponDetails()) {
                    detail.setCoupon(couponVO);
                    // 設置創建和更新時間
                    detail.setCreatedAt(new Date());
                    detail.setUpdatedAt(new Date());
                }
            }
            
            // 一次性保存優惠券和所有明細
            return repository.save(couponVO);
            
        } catch (Exception e) {
            throw new RuntimeException("保存優惠券和明細失敗: " + e.getMessage());
        }
    }
    
    
    
    
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
//    public List<CouponDetailVO> getAll() {
//        return repository.findAll();
//    }
    
    // 根据条件查询优惠券明细
    public List<CouponDetailVO> getAll(Map<String, String[]> map) {
        return HibernateUtil_CompositeQuery_CouponDetail.getAllC(map, sessionFactory.openSession());
    }
    
    // 根據優惠券編號查詢明細
    public List<CouponDetailVO> getByCouponNo(Integer couponNo) {
        try {
            List<CouponDetailVO> details = repository.findByCoupon_CouponNo(couponNo);
            // 記錄查詢結果
            System.out.println("查詢到 " + details.size() + " 筆明細記錄");
            return details;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("查詢優惠券明細失敗: " + e.getMessage());
        }
    }
    
    public List<CouponDetailVO> getAll() {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("FROM CouponDetailVO");
        return query.list();
    }
    

    
}
