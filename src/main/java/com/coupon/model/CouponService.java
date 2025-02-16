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
            // 1. 參數驗證
            if (couponVO == null) {
                throw new IllegalArgumentException("優惠券對象不能為空");
            }

            // 2. 設置基本數據
            Date now = new Date();  // 創建一個時間實例重複使用
            if (couponVO.getCouponStart() == null) {
                couponVO.setCouponStart(now);
            }
            
            // 設置優惠券狀態（如果需要）
            if (couponVO.getCouponStatus() == null) {
                couponVO.setCouponStatus(1);  // 假設 1 代表有效
            }

            // 3. 處理明細
            List<CouponDetailVO> details = new ArrayList<>();
            if (couponVO.getCouponDetails() != null) {
                details.addAll(couponVO.getCouponDetails());
            }
            couponVO.getCouponDetails().clear();

            // 4. 保存優惠券主體
            CouponVO savedCoupon = repository.save(couponVO);

            // 5. 處理明細
            for (CouponDetailVO detail : details) {
                // 驗證明細數據
                if (detail.getDisRate() == null || detail.getDisRate() <= 0) {
                    throw new IllegalArgumentException("折扣率必須大於0");
                }

                detail.setCoupon(savedCoupon);
                detail.setCreatedAt(now);  // 使用同一個時間實例
                detail.setUpdatedAt(now);
                savedCoupon.getCouponDetails().add(detail);
            }

            // 6. 最終保存
            return repository.save(savedCoupon);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存優惠券和明細失敗：" + e.getMessage(), e);  // 加入原始異常
        }
    }
    
    // 前台領取櫃位優惠券頁面點查看詳情可以看到明細
    @Transactional
    public CouponVO getOneCouponWithDetails(Integer couponNo) {
        try {
            Optional<CouponVO> couponOpt = repository.findByIdWithDetails(couponNo);
            
            if (!couponOpt.isPresent()) {
                throw new RuntimeException("找不到優惠券編號：" + couponNo);
            }
            
            CouponVO coupon = couponOpt.get();
            
            // 檢查明細是否存在
            if (coupon.getCouponDetails() == null) {
                coupon.setCouponDetails(new ArrayList<>());
            }
            
            // 調試日誌
            System.out.println("明細數量: " + coupon.getCouponDetails().size());
            for (CouponDetailVO detail : coupon.getCouponDetails()) {
                System.out.println("明細ID: " + detail.getGoodsNo());
            }
            
            return coupon;
        } catch (Exception e) {
            e.printStackTrace();
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
            existingCoupon.setCounter(couponVO.getCounter());
            
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
    @Transactional
    public void deleteCoupon(Integer couponNo) {
        // 先刪除所有關聯的明細記錄
        couponDetailRepository.deleteByCouponNo(couponNo);
        repository.deleteById(couponNo);
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
    
//    結帳用
	public int applyDiscount(int totalBefore, CouponVO couponVO) {
		// TODO Auto-generated method stub
		return 0;
	}
//  後台審核優惠券get方法
	public List<CouponVO> getAllPendingCoupons() {
		// TODO Auto-generated method stub
		return null;
	}

    // 减少優惠券可領取次數
    @Transactional
    public void decreaseUsageLimit(Integer couponNo) {
        CouponVO coupon = repository.findById(couponNo)
            .orElseThrow(() -> new RuntimeException("找不到優惠券：" + couponNo));
            
        if (coupon.getUsageLimit() > 0) {
            coupon.setUsageLimit(coupon.getUsageLimit() - 1);
            repository.save(coupon);
        } else {
            throw new RuntimeException("此優惠券已達領取上限");
        }
    }
    
    
    
}
