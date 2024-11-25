package com.coupon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.coupon.model.CouponService;
import com.coupon.model.CouponVO;

import java.util.*;

@Controller
@RequestMapping("/coupon")
public class IndexController2_inSpringBoot_Coupon {
    
    // 自动装配 CouponService
    @Autowired
    CouponService couponSvc;



    // 主页映射
    @GetMapping("/inindex")
    public String index(Model model) {
        return "vendor-end/coupon/inindex"; // 对应视图名称
    }


    // 提供查询页面（绝对路径）
    @GetMapping("/select_page")
    public String selectPage(Model model) {
        return "vendor-end/coupon/select_page";
    }

    // 提供所有优惠券列表页面
    @GetMapping("/listAllCoupon")
    public String listAllCoupon(Model model) {
        return "vendor-end/coupon/listAllCoupon";
    }

	// 提供CouponVO列表，供 Thymeleaf 使用
    @ModelAttribute("couponListData") // for select_page.html 和 listAllCoupon.html
    protected List<CouponVO> referenceListData(Model model) {
        List<CouponVO> list = couponSvc.getAll();
        return list;
    }


}