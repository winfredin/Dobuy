package com.counterHome.couponDetailTest.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("NewCouponsDetailService")
public class NewCouponsDetailService {
	
	@Autowired
	NewCouponsDetailsRepository newCouponsDetailsRepository;
	
	public NewCouponsDetailsVO findCouponsDetail(String couponNo, double totalAmount){
		return newCouponsDetailsRepository.findCouponsBycouponNo(couponNo, totalAmount);
	}
	
	public int calFinalPrice(String couponNo, double totalAmount) {
		NewCouponsDetailsVO newCouponsDetailsVO = newCouponsDetailsRepository.findCouponsBycouponNo(couponNo, totalAmount);
		Double finalPrice = totalAmount * newCouponsDetailsVO.getDiscountRate() - newCouponsDetailsVO.getDiscountAmount();
		int roundedFinalPrice = (int) Math.round(finalPrice);
		return roundedFinalPrice;
	}
}
