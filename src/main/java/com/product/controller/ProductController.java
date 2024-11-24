package com.product.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.countercarousel.model.CountercarouselService;
import com.countercarousel.model.CountercarouselVO;
import com.product.model.ProductRepository;
import com.product.model.ProductVO;

@Controller
@RequestMapping("/product")
public class ProductController {
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	CountercarouselService carouselSvc;

	// 處理表單提交
	@PostMapping("insert")
	public String insert(@ModelAttribute ProductVO productVO) {
		try {
			MultipartFile file = productVO.getUpFiles();
			productVO.setImg(file.getBytes()); // 將字節數據存儲到持久化字段
			productRepository.save(productVO);
			System.out.println("File uploaded successfully!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error";
		}
		return "/back-end-product/addSuccess"; // 保存後跳轉到 getAll 頁面
	}

	// 顯示圖片列表
	@GetMapping("all")
	public String getAllImages(ModelMap model) {
		List<ProductVO> productList = productRepository.findAll();
		for (ProductVO i : productList) {
			i.convertToBase64();
		}
		model.addAttribute("productList", productList);
		return "/back-end-product/AllProduct"; // 返回模板 getAll.html
	}

//	// 提供所有商品資料的 API
//	@GetMapping("/api/product")
//	@ResponseBody
//	public List<ProductVO> getProducts() {
//		List<ProductVO> productList = productRepository.findAll();
//		for (ProductVO j : productList) {
//			j.convertToBase64();
//		}
//		return productList;
//	}

	@GetMapping("/product")
	public String getProducts(Model model) {
	    List<ProductVO> productList = productRepository.findAll();
	    for (ProductVO product : productList) {
//	    	product.setImg(Base64.getEncoder().encodeToString(product.getImg()))   ; // 转换图片数据
	    	product.convertToBase64(); // 转换图片数据
	    }
	    List<CountercarouselVO> carouselImages = carouselSvc.getPic();
	    for(CountercarouselVO img : carouselImages) {
	    	img.convertToBase64();
	    }
	    model.addAttribute("products", productList); // 一次性传递所有商品数据
	    model.addAttribute("carouselImages", carouselImages); // 一次性传递所有商品数据	    
	    return "/back-end-product/product"; // 返回 Thymeleaf 模板名
	}



}
