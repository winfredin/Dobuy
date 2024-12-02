package com.counterHome;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.countercarousel.model.CountercarouselVO;
import com.goods.model.GoodsService;
import com.goods.model.GoodsVO;
import com.product35.model.ProductVO35;

@Controller
public class RootController35 {

	@Autowired
	GoodsService goodsSvc;
	
	@GetMapping("/")
	public String redirectToHomepage(ModelMap model) {
		model.addAttribute("goodsVO", new GoodsVO());
		return "/back-end/homePage35";
	}

	@GetMapping("addCarousel_page")
	public String redirecToAddCarousel(ModelMap model) {
		model.addAttribute("countercarouselVO", new CountercarouselVO()); // 创建一个空的 CountercarouselVO 对象
		return "vendor-end/front-end-carousel/addCarousel";
	}

	@GetMapping("addProduct_page")
	public String redirectToAddProduct(ModelMap model) {
		model.addAttribute("productVO", new ProductVO35()); // 创建一个空的 ProductVO 对象
		return "vendor-end/front-end-product/addProduct";
	}
	
	@GetMapping("shop-detail")
	public String redirectToShopDetail(ModelMap model) {
		model.addAttribute("goodsVO", new GoodsVO()); // 创建一个空的 goods 对象
		return "front-end/shop-detail/shop-detail";
	}
	
	@GetMapping("cart_test")
	public String redirectToCartTest(ModelMap model) {
		List<GoodsVO> goods = goodsSvc.getAll();
		model.addAttribute("goodsList", goods);
		return "front-end/cartTest/allGoods";
	}
	
	

}
