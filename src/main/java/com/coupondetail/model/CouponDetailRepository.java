package com.coupondetail.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.coupon.model.CouponVO;

public interface CouponDetailRepository extends JpaRepository<CouponDetailVO, Integer> {

    // 使用原生 SQL 删除
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM CouponDetail WHERE couponDetailNo = ?1", nativeQuery = true)
    void deleteByCouponDetailNo(int couponDetailNo);

    // 自定義條件查詢
    @Query(value = "FROM CouponDetailVO WHERE couponNo = ?1 AND goodsNo = ?2 AND disRate = ?3 ORDER BY couponDetailNo")
    List<CouponDetailVO> findByOthers(int couponNo, int goodsNo, double disRate);



    // 櫃位列出自己的優惠券點詳情 可以查看優惠商品明細
    @Query("SELECT cd FROM CouponDetailVO cd WHERE cd.coupon.couponNo = :couponNo")
    List<CouponDetailVO> findByCoupon_CouponNo(@Param("couponNo") Integer couponNo);
    
    
    
    @Modifying
    @Query("DELETE FROM CouponDetailVO cd WHERE cd.couponDetailNo = :couponDetailNo")
    void deleteByCouponDetailNo(@Param("couponDetailNo") Integer couponDetailNo);

    
    @Transactional
    @Modifying
    @Query("DELETE FROM CouponDetailVO d WHERE d.coupon.couponNo = ?1")
    void deleteByCouponNo(Integer couponNo);

    @Query("SELECT d FROM CouponDetailVO d JOIN FETCH d.goodsVO g WHERE d.coupon.couponNo = :couponNo")
    List<CouponDetailVO> findByCouponNoWithGoods(@Param("couponNo") Integer couponNo);
    
}
