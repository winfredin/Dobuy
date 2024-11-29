package com.coupon.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CouponRepository extends JpaRepository<CouponVO, Integer> {

    // 使用原生 SQL 删除优惠券记录
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM CouponVO WHERE couponNo = ?1", nativeQuery = true)
    void deleteByCouponNo(int couponNo);

    // 自定义条件查询：根据櫃位編號、优惠券状态和审核状态查询优惠券
    @Query(value = "FROM CouponVO WHERE counterNo = ?1 AND couponStatus = ?2 AND checkStatus = ?3 ORDER BY couponNo")
    List<CouponVO> findByCounterAndStatusAndCheckStatus(int counterNo, int couponStatus, int checkStatus);

    //審核優惠券
    @Modifying
    @Query("UPDATE CouponVO c SET c.checkStatus = :checkStatus WHERE c.couponNo = :couponNo")
    int updateCheckStatusByCouponNo(@Param("checkStatus") int checkStatus, @Param("couponNo") int couponNo);

	CouponVO save(CouponVO couponVO);

}
