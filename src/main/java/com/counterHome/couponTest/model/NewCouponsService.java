package com.counterHome.couponTest.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("NewCouponsService")
public class NewCouponsService {
	
	@Autowired
	NewCouponsRepository newCouponsRepository;
	
	public List<NewCouponsVO> findCouponsBycounterNo(String counterNo) {
		return newCouponsRepository.findCouponsBycounterNo(counterNo, "1");
	}
	
	public NewCouponsVO findCouponsByCouponNo(Integer couponNo) {
		return newCouponsRepository.findCouponsByCouponNo(couponNo);
	}
	
}
