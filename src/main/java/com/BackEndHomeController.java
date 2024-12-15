package com;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.goods.model.GoodsService;
import com.goods.model.GoodsVO;
import com.goodstype.model.GoodsTypeService;
import com.goodstype.model.GoodsTypeVO;
import com.used.model.UsedService;
import com.used.model.UsedVO;
import com.usedpic.model.UsedPicService;
import com.usedpic.model.UsedPicVO;


@Controller
@RequestMapping("/used")
public class BackEndHomeController {
	
	@Autowired
	private UsedService usedSvc;

	@Autowired
	private UsedPicService usedPicSvc;
	
	@Autowired
	private GoodsTypeService goodsTypeService;
	
	@Autowired
	private EntityManager EntityManager;
	
	@Autowired
	private GoodsService goodsService;
	
	
	@GetMapping("/listAllUsed49")
	public String showAllUsedListPage(Model model) {
	    // 從 Service 獲取數據
	    List<UsedVO> usedListData = usedSvc.getAll();
	    List<GoodsTypeVO> goodsTypeList = goodsTypeService.getAll();

	    // 將數據放到模型中
	    model.addAttribute("usedListData", usedListData);
	    model.addAttribute("goodsTypeList", goodsTypeList);

	    // 返回完整的 Thymeleaf 頁面
	    return "/back-end/back-end-home/listAllUsed49"; // 此處需確保模板名稱與文件對應
	}
	
//	//管理員搜尋所有二手商品
//	@PostMapping("/getAllSellerUsedListFragment")
//    public String getAllUsedListFragment( Model model) {
//        
//        
//        List<UsedVO> usedListData = usedSvc.getAll();
//        List<GoodsTypeVO> goodsTypeList= goodsTypeService.getAll();
//		
//        // 將數據放到模型中
//        model.addAttribute("usedListData", usedListData);
//        model.addAttribute("goodsTypeList", goodsTypeList);
//
//        // 返回Fragment所在的模板，並指定Fragment名稱
//        return "front-end/used/listAllUsed :: usedListFragment";
//    }
	

}
