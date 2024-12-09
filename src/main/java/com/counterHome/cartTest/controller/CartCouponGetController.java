package com.counterHome.cartTest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.counter.model.CounterService;
import com.counterHome.couponDetailTest.model.NewCouponsDetailService;
import com.counterHome.couponDetailTest.model.NewCouponsDetailsVO;
import com.counterHome.couponTest.model.NewCouponsService;
import com.counterHome.couponTest.model.NewCouponsVO;

@RestController
@RequestMapping("/cart/test/coupons")
public class CartCouponGetController {

	@Autowired
	CounterService counterSvc;

	@Autowired
	NewCouponsService newCouponsSvc;

	@Autowired
	NewCouponsDetailService newCouponsDetailService;

	@PostMapping("/getByTotal")
	public ResponseEntity<List<NewCouponsVO>> getCoupon(@RequestBody Map<String, Object> requestBody) {
		String counterCname = (String) requestBody.get("counterCname");
		double totalAmount = Double.parseDouble(requestBody.get("totalAmount").toString());
		String counterNo = counterSvc.getCounterNoByCounterCname(counterCname);// 找出counterNo

		List<NewCouponsVO> allCoupons = newCouponsSvc.findCouponsBycounterNo(counterNo);
		List<NewCouponsDetailsVO> eligibleCouponDetails = new ArrayList<>();
		for (NewCouponsVO couponsVO : allCoupons) {
			// 用couponNo & totalAmount 去查couponDetail
			eligibleCouponDetails
					.add(newCouponsDetailService.findCouponsDetail(couponsVO.getCouponNo().toString(), totalAmount));
		}

		List<NewCouponsVO> eligibleCoupons = new ArrayList<>();
		for(NewCouponsDetailsVO detail : eligibleCouponDetails) {
			if( detail == null) {
				continue;
			}
			eligibleCoupons
				.add(newCouponsSvc.findCouponsByCouponNo(detail.getCouponNo()));
			
		}
		
		 // 返回符合条件的优惠券
	    return ResponseEntity.ok(eligibleCoupons);
	}
	
	
	@PostMapping("applyCoupon")
	public double getAfterTotal(@RequestBody Map<String, Object> requestBody) {
		String couponNo = (String)requestBody.get("couponNo");
		double totalAmount = Double.parseDouble((String)requestBody.get("totalAmount"));
		int finalPrice = newCouponsDetailService.calFinalPrice(couponNo, totalAmount);
		
		return finalPrice;
		
	}
	
	
	
	
}
