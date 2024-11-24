package com.root.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.countercarousel.model.CountercarouselVO;
import com.product.model.ProductVO35;

@Controller
public class RootController35 {

	@GetMapping("/")
	public String redirectToHomepage() {
		return "/back-end/homePage";
	}

	@GetMapping("addCarousel_page")
	public String redirecToAddCarousel(ModelMap model) {
		model.addAttribute("countercarouselVO", new CountercarouselVO()); // 创建一个空的 CountercarouselVO 对象
		return "/back-end-carousel/addCarousel";
	}

	@GetMapping("addProduct_page")
	public String redirectToAddProduct(ModelMap model) {
		model.addAttribute("productVO", new ProductVO35()); // 创建一个空的 ProductVO 对象
		return "/back-end-product/addProduct";
	}

}
