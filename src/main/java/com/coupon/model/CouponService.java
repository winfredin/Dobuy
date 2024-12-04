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

//import com.coupon.controller.HibernateUtil_CompositeQuery_Coupon;
import com.coupondetail.model.CouponDetailRepository;
import com.coupondetail.model.CouponDetailVO;
import com.goods.model.GoodsVO;

@Service("couponService")
public class CouponService {

    @Autowired
    CouponRepository repository;

    @Autowired
    CouponDetailRepository couponDetailRepository;
    
    @Autowired
    private SessionFactory sessionFactory;
    
    
    
//  櫃位優惠券要顯示在前台領取櫃位優惠券頁面
    public List<CouponVO> getAllApprovedCoupons() {
        return repository.findByCheckStatusAndCouponStatus(1, 1);  // 1: 已審核, 1: 時效內
    }
    
//    同時新增優惠券與明細
    @Transactional(rollbackOn = Exception.class)  
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
    
    // 獲取優惠券及其明細的完整數據
    public CouponVO getOneCouponWithDetails(Integer couponNo) {
        try {
            // 從資料庫獲取優惠券
            Optional<CouponVO> couponOpt = repository.findById(couponNo);
            
            if (!couponOpt.isPresent()) {
                throw new RuntimeException("找不到優惠券編號：" + couponNo);
            }
            
            CouponVO coupon = couponOpt.get();
            
            // 確保明細列表已初始化
            if (coupon.getCouponDetails() == null) {
                coupon.setCouponDetails(new ArrayList<>());
            }
            
            return coupon;
            
        } catch (Exception e) {
            throw new RuntimeException("獲取優惠券數據時發生錯誤：" + e.getMessage());
        }
    }
    
    
//  櫃位新增優惠券 可以同時設定優惠商品明細
    @Transactional(rollbackOn = Exception.class)
    public CouponVO updateCouponWithDetails(CouponVO couponVO) {
        try {
            // 印接收到的數據
            System.out.println("Updating coupon: " + couponVO.getCouponNo());
            System.out.println("Details count: " + 
                (couponVO.getCouponDetails() != null ? couponVO.getCouponDetails().size() : 0));
            
            // 獲取原有數據
            CouponVO existingCoupon = getOneCouponWithDetails(couponVO.getCouponNo());
            
            // 更新基本資料
            existingCoupon.setCouponTitle(couponVO.getCouponTitle());
            existingCoupon.setCouponContext(couponVO.getCouponContext());
            existingCoupon.setCouponStart(couponVO.getCouponStart());
            existingCoupon.setCouponEnd(couponVO.getCouponEnd());
            existingCoupon.setCouponStatus(couponVO.getCouponStatus());
            existingCoupon.setUsageLimit(couponVO.getUsageLimit());
            existingCoupon.setCheckStatus(couponVO.getCheckStatus());
            existingCoupon.setCounterNo(couponVO.getCounterNo());
            
            // 處理明細
            if (couponVO.getCouponDetails() != null && !couponVO.getCouponDetails().isEmpty()) {
                // 先清除現有明細
                existingCoupon.getCouponDetails().clear();
                
                // 新增明細
                for (CouponDetailVO detail : couponVO.getCouponDetails()) {
                    detail.setCoupon(existingCoupon);  // 設置關聯
                    detail.setUpdatedAt(new Date());
                    if (detail.getCreatedAt() == null) {
                        detail.setCreatedAt(new Date());
                    }
                    existingCoupon.getCouponDetails().add(detail);
                }
            }
            
            // 保存並返回更新後的數據
            return repository.save(existingCoupon);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("更新優惠券失敗：" + e.getMessage());
        }
    }
    
    
    
    
    @Transactional
    public void addCouponDetail(Integer couponNo, CouponDetailVO couponDetailVO) {
        // 查找優惠券是否存在
        CouponVO coupon = repository.findById(couponNo).orElseThrow(() -> new RuntimeException("Coupon not found"));

        // 关联优惠券
        couponDetailVO.setCoupon(coupon);

        // 保存优惠券明细
        couponDetailRepository.save(couponDetailVO);
    }
    

    // 新增
    public void addCoupon(CouponVO couponVO) {
        repository.save(couponVO);
    }

    // 更新
    public void updateCoupon(CouponVO couponVO) {
        repository.save(couponVO);
    }

    // 删除
    public void deleteCoupon(Integer couponNo) {
        if (repository.existsById(couponNo))
            repository.deleteByCouponNo(couponNo);
    }

    // 查询單筆
    public CouponVO getOneCoupon(Integer couponNo) {
        Optional<CouponVO> optional = repository.findById(couponNo);
        return optional.orElse(null);
    }

    // 查询所有
    public List<CouponVO> getAll() {
        return repository.findAll();
    }

    // 根据条件查询
//    public List<CouponVO> getAll(Map<String, String[]> map) {
//        return HibernateUtil_CompositeQuery_Coupon.getAllC(map, sessionFactory.openSession());
//    }
    
//  後台審核優惠券
    @Transactional
    public boolean approveCoupon(int couponNo) {
        int updatedRows = repository.updateCheckStatusByCouponNo(1, couponNo); // 1 表示已審核
        return updatedRows > 0; // 返回是否更新成功
    }
    
    //根據櫃位編號查詢優惠券
    public List<CouponVO> getCounterCoupon35(int counterNo){ //  11/27
    	return repository.findByCounterAndStatusAndCheckStatus(counterNo, 1, 1);
    }
    
    //任國櫃位優惠管理
    public List<CouponVO> getOneCounter46(Integer counterNo) {
        return repository.getOneCounter46(counterNo); // 如果不存在，返回 null
    }
    
    
    
}
