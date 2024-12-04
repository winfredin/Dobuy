package com.coupon.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CouponRepository extends JpaRepository<CouponVO, Integer> {

    // 使用原生 SQL 删除優惠券
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM CouponVO WHERE couponNo = ?1", nativeQuery = true)
    void deleteByCouponNo(int couponNo);

    // 自定義條件
    @Query(value = "FROM CouponVO WHERE counterNo = ?1 AND couponStatus = ?2 AND checkStatus = ?3 ORDER BY couponNo")
    List<CouponVO> findByCounterAndStatusAndCheckStatus(int counterNo, int couponStatus, int checkStatus);

//  後台審核優惠券
    @Modifying
    @Query("UPDATE CouponVO c SET c.checkStatus = :checkStatus WHERE c.couponNo = :couponNo")
    int updateCheckStatusByCouponNo(@Param("checkStatus") int checkStatus, @Param("couponNo") int couponNo);
    
    
//  櫃位優惠券要顯示在前台領取櫃位優惠券頁面
    @Query("SELECT c FROM CouponVO c WHERE c.checkStatus = ?1 AND c.couponStatus = ?2")
    List<CouponVO> findByCheckStatusAndCouponStatus(Integer checkStatus, Integer couponStatus);
    
	CouponVO save(CouponVO couponVO);

}
