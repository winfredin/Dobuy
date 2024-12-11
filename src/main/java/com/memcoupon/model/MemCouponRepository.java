package com.memcoupon.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.member.model.MemberVO;

public interface MemCouponRepository extends JpaRepository<MemCouponVO, Integer> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM MemCoupon WHERE memCouponNo = ?1", nativeQuery = true)
    void deleteByMemCouponNo(int memCouponNo);
    
    @Query("FROM MemCouponVO mc WHERE mc.member.memNo = ?1 AND mc.coupon.couponNo = ?2 ORDER BY mc.memCouponNo")
    List<MemCouponVO> findByMemNoAndCouponNo(int memNo, int couponNo);
    
    @Query("FROM MemCouponVO mc WHERE mc.member.memNo = ?1 ORDER BY mc.memCouponNo")
    List<MemCouponVO> findByMemNo(int memNo);
    
    // 修改查詢方法
    @Query("FROM MemCouponVO mc WHERE mc.member = ?1 AND mc.status = ?2")
    List<MemCouponVO> findByMemberAndStatus(MemberVO member, Integer status);
    
//  前台領取櫃位優惠券後顯示在我的優惠券
    @Query("SELECT COUNT(mc) FROM MemCouponVO mc WHERE mc.coupon.couponNo = :couponNo AND mc.member.memNo = :memberNo")
    long countByCouponAndMember(@Param("couponNo") Integer couponNo, @Param("memberNo") Integer memberNo);

    
//    購物車優惠顯示
    @Query("SELECT mc FROM MemCouponVO mc " +
            "JOIN FETCH mc.coupon c " +
            "WHERE mc.member.memNo = :memNo " +  // 修改這裡，使用正確的屬性路徑
            "AND mc.status = 0 " +              // 0 表示未使用
            "AND c.couponEnd >= CURRENT_DATE")
     List<MemCouponVO> findAvailableCouponsByMemNo(@Param("memNo") Integer memNo);
    
//    領取後數量減1
    boolean existsByMemberMemNoAndCouponCouponNo(Integer memNo, Integer couponNo);


}
