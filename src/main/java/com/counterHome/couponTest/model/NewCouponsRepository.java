package com.counterHome.couponTest.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NewCouponsRepository extends JpaRepository<NewCouponsVO, Integer>{
	
	@Query(value = "SELECT * FROM NewCoupons WHERE counterNo = ?1 && couponStatus = ?2", nativeQuery = true)
	List<NewCouponsVO> findCouponsBycounterNo(String counterNo, String couponStatus);
	
	
	@Query(value = "SELECT * FROM NewCoupons WHERE couponNo = ?1 && couponStatus = 1", nativeQuery = true)
	NewCouponsVO findCouponsByCouponNo(Integer couponNo);

	
	
}
