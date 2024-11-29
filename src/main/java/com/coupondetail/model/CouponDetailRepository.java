package com.coupondetail.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.coupon.model.CouponVO;

public interface CouponDetailRepository extends JpaRepository<CouponDetailVO, Integer> {

    // 使用原生 SQL 删除优惠券明细记录
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM CouponDetail WHERE couponDetailNo = ?1", nativeQuery = true)
    void deleteByCouponDetailNo(int couponDetailNo);

    // 自定义条件查询
//    @Query(value = "FROM CouponDetailVO WHERE couponNo = ?1 AND goodsNo = ?2 AND disRate = ?3 ORDER BY couponDetailNo")
//    List<CouponDetailVO> findByOthers(int couponNo, int goodsNo, double disRate);



	CouponVO save(CouponVO couponVO);

    // 根據優惠券編號查詢明細
    @Query("SELECT cd FROM CouponDetailVO cd WHERE cd.coupon.couponNo = :couponNo")
    List<CouponDetailVO> findByCoupon_CouponNo(@Param("couponNo") Integer couponNo);
    
    @Modifying
    @Query("DELETE FROM CouponDetailVO cd WHERE cd.couponDetailNo = :couponDetailNo")
    void deleteByCouponDetailNo(@Param("couponDetailNo") Integer couponDetailNo);


}
