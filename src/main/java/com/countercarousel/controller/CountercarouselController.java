package com.countercarousel.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.countercarousel.model.CountercarouselRepository;
import com.countercarousel.model.CountercarouselVO;

@Controller
@RequestMapping("/carousel")
public class CountercarouselController {

	@Autowired
	CountercarouselRepository countercarouselRepository;
	
	@GetMapping("/add")
	public String showRegisterPage(Model model) {
		model.addAttribute("countercarouselVO", new CountercarouselVO());
		return "vendor-end/front-end-carousel/addCarousel";
	}
	
	@PostMapping("insert")
	public String insert(@ModelAttribute CountercarouselVO countercarouselVO) {
		try {
			MultipartFile file = countercarouselVO.getUpFile();
			countercarouselVO.setCarouselPic(file.getBytes()); // 將字節數據存儲到持久化字段
			countercarouselRepository.save(countercarouselVO);
			System.out.println("File uploaded successfully!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error";
		}
		return "vendor-end/front-end-carousel/addSuccess";
	}

	// 顯示圖片列表
	@GetMapping("all")
	public String getAllImages(ModelMap model) {
		List<CountercarouselVO> carouselList = countercarouselRepository.findAll();
		for (CountercarouselVO i : carouselList) {
			i.convertToBase64();
		}

		model.addAttribute("carouselList", carouselList);
		return "/back-end-carousel/ALLCarousel";
	}

//	@GetMapping("/api/pic")
//	@ResponseBody
//	public List<String> getProducts() {
//		List<CountercarouselVO> carouselList = countercarouselRepository.findNewest3();
//		List<String> picList = new ArrayList<String>();
//		for (CountercarouselVO j : carouselList) {
//			j.convertToBase64();
//			picList.add(j.getBase64Image());
//		}
//		return picList;
//	}
}
