package com.memcoupon.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface MemCouponRepository extends JpaRepository<MemCouponVO, Integer> {

    // 使用原生 SQL 删除會員優惠券記錄
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM MemCoupon WHERE memCouponNo = ?1", nativeQuery = true)
    void deleteByMemCouponNo(int memCouponNo);

    // 自定义条件查询：通过會員編號和優惠券編號查詢
    @Query(value = "FROM MemCouponVO WHERE memNo = ?1 AND couponNo = ?2 ORDER BY memCouponNo")
    List<MemCouponVO> findByMemNoAndCouponNo(int memNo, int couponNo);

    // 查詢某一會員的所有優惠券
    @Query(value = "FROM MemCouponVO WHERE memNo = ?1 ORDER BY memCouponNo")
    List<MemCouponVO> findByMemNo(int memNo);
}
