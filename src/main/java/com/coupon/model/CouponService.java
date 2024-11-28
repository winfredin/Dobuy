package com.coupon.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coupon.controller.HibernateUtil_CompositeQuery_Coupon;
import com.coupondetail.model.CouponDetailRepository;
import com.coupondetail.model.CouponDetailVO;

@Service("couponService")
public class CouponService {

    @Autowired
    CouponRepository repository;

    @Autowired
    CouponDetailRepository couponDetailRepository;
    
    @Autowired
    private SessionFactory sessionFactory;
    
    @Transactional(rollbackOn = Exception.class)  // 使用 rollbackOn
    public CouponVO addCouponWithDetails(CouponVO couponVO) {
        try {
            // 1. 設置基本數據
            if (couponVO.getCouponStart() == null) {
                couponVO.setCouponStart(new Date());
            }
            
            // 2. 備份明細列表並清空原有關聯
            List<CouponDetailVO> details = new ArrayList<>(couponVO.getCouponDetails());
            couponVO.getCouponDetails().clear();
            
            // 3. 先保存優惠券主體
            CouponVO savedCoupon = repository.save(couponVO);
            
            // 4. 為每個明細設置關聯並添加到優惠券
            for (CouponDetailVO detail : details) {
                detail.setCoupon(savedCoupon);
                detail.setCreatedAt(new Date());
                detail.setUpdatedAt(new Date());
                savedCoupon.getCouponDetails().add(detail);
            }
            
            // 5. 再次保存以更新關聯
            return repository.save(savedCoupon);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存優惠券和明細失敗：" + e.getMessage());
        }
    }
    
    
    
    @Transactional
    public void addCouponDetail(Integer couponNo, CouponDetailVO couponDetailVO) {
        // 查找优惠券是否存在
        CouponVO coupon = repository.findById(couponNo).orElseThrow(() -> new RuntimeException("Coupon not found"));

        // 关联优惠券
        couponDetailVO.setCoupon(coupon);

        // 保存优惠券明细
        couponDetailRepository.save(couponDetailVO);
    }
    

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
