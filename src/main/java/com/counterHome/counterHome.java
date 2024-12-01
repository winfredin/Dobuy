package com.counterHome;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.counter.model.CounterService;
import com.counter.model.CounterVO;
import com.countercarousel.model.CountercarouselService;
import com.countercarousel.model.CountercarouselVO;
import com.coupon.model.CouponService;
import com.coupon.model.CouponVO;
import com.faq.model.FaqService;
import com.faq.model.FaqVO;
import com.goods.model.GoodsLightVO;
import com.goods.model.GoodsService;
import com.goods.model.GoodsVO;
import com.goodstype.model.GoodsTypeService;
import com.goodstype.model.GoodsTypeVO;

@Controller
@RequestMapping("/counter")
public class counterHome {

	@Autowired
	GoodsService goodsSvc;

	@Autowired
	CountercarouselService carouselSvc;

	@Autowired
	CouponService couponSvc;

	@Autowired
	GoodsTypeService goodsTypeSvc;

	@Autowired
	CounterService counterSvc;

	@Autowired
	FaqService faqSvc;

	@GetMapping("getOneCounter_For_Display") // 用get 因為session要抓呼叫前的路徑
	public String getOne_For_Display(@RequestParam("counterNo") String counterNo, ModelMap model) {
		List<GoodsVO> goodsVO = goodsSvc.getOneCounter35(Integer.valueOf(counterNo));// 用櫃位編號抓商品資訊，只抓需要的，去repository看
		List<GoodsLightVO> goodsLightVO = new ArrayList<GoodsLightVO>(); // 建一個輕量級的VO，把抓到的資料轉成前端要的格式(base64)
		for (GoodsVO goods : goodsVO) {
			goodsLightVO.add(new GoodsLightVO(goods));
		}

		// 其他
		List<CountercarouselVO> carouselImages = carouselSvc.getPic(Integer.valueOf(counterNo));// 抓輪播圖圖片(依櫃位編號)
		List<CouponVO> coupons = couponSvc.getCounterCoupon35(Integer.valueOf(counterNo));// 抓coupon(依櫃位編號)
		List<GoodsTypeVO> goodsType = goodsTypeSvc.getAll();// 抓商品種類(全部)
		for (CountercarouselVO img : carouselImages) {
			img.convertToBase64();
		}

		CounterVO counterVO = counterSvc.getOneCounter(Integer.valueOf(counterNo));// 依櫃位編號抓相關資訊

		// 添加模型数据
		model.addAttribute("goodsLightVO", goodsLightVO);
		model.addAttribute("carouselImages", carouselImages);
		model.addAttribute("coupons", coupons);
		model.addAttribute("goodsType", goodsType);
		model.addAttribute("counterVO", counterVO);

		return "/vendor-end/counterHome/counterHomePage"; // 返回 Thymeleaf 模板名
	}

	@GetMapping("getOneGoods") // 11/28 測試點商品後帶到商品詳情頁面
	public String getOneGoods(@RequestParam("goodsNo") String goodsNo, ModelMap model) {
		GoodsVO goods = goodsSvc.getOneGoods(Integer.valueOf(goodsNo)); // 查詢到回傳的是一個物件
		List<String> goodsImg = goodsSvc.getOneGoodsImg(goods);// 將查到的物件中的圖片轉成base64，在渲染到前端
		model.addAttribute("goods", goods);
		model.addAttribute("goodsImg", goodsImg);
		return "front-end/shop-detail/shop-detail";
	}

	@GetMapping("getFaq") 
	public String getFaq(@RequestParam("counterNo") String counterNo, ModelMap model) {
		List<FaqVO> faqVO = faqSvc.getOneCounterFaq(Integer.valueOf(counterNo));// 查詢到回傳的是一個物件
		model.addAttribute("faqVO", faqVO);
		return "/vendor-end/counterHome/counterFaq";
	}

}
