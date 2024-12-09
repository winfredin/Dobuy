package com.counterHome.couponDetailTest.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NewCouponsDetailsRepository extends  JpaRepository<NewCouponsDetailsVO, Integer>{
	
	
	@Query(value = "SELECT * FROM Newcoupondetails WHERE couponNo =?1 && thresholdAmount <= ?2 ", nativeQuery = true)
	NewCouponsDetailsVO findCouponsBycouponNo(String couponNo, double totalAmount);
	
}
