package com.usedorder.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.used.model.UsedService;
import com.used.model.UsedVO;
import com.usedorder.model.UsedOrderService;
import com.usedorder.model.UsedOrderVO;


@Controller
@RequestMapping("/usedorder")
public class UsedOrderFragController {
	
	@Autowired
	UsedOrderService usedOrderSvc;
	
	@Autowired
	UsedService usedSvc;
	
	//查詢身為賣家的訂單
	@PostMapping("/getSellerUsedOrderListFragment")
    public String getSellerUsedOrderListFragment(HttpSession session, Model model) {
        // 從 session 中取得 memNo
		if(session.getAttribute("memNo")==null) {
			return "front-end/usedorder/SellerUsedOrderFragment :: usedOrderFragment";
		}
		
        Integer memNo = Integer.valueOf((String)session.getAttribute("memNo"));    
        
     // 根據 memNo 從資料庫中查詢該位會員所販賣的二手商品
     		List<UsedVO> usedList = usedSvc.adminSelectBySellerNo(memNo);
     		if(usedList.isEmpty()) {
     			return "front-end/usedorder/SellerUsedOrderFragment :: usedOrderFragment";
     		}
     		//根據拿取到的usedList去搜尋該位賣家賣的商品所有訂單
     		//將商品編號提取出來放入新list
     		List<Integer> usedNoList = new ArrayList<>();
     		for(UsedVO usedVO:usedList) {
     			usedNoList.add(usedVO.getUsedNo());
     		}
     		//再將提取出來的商品編號List去搜尋該位賣家賣的商品所有訂單
     		List<UsedOrderVO> filteredList = usedOrderSvc.selectSellerOrderBySellerUsedNo(usedNoList);
     
        // 將數據放到模型中
        model.addAttribute("usedorderListData", filteredList);
        // 返回Fragment所在的模板，並指定Fragment名稱
        return "front-end/usedorder/SellerUsedOrderFragment :: usedOrderFragment";
    }
	
	
	//查詢身為買家的訂單
	@PostMapping("/getBuyerUsedOrderListFragment")
	public String getBuyerUsedOrderFragment(HttpSession session, Model model) {
		// 從 session 中取得 memNo
		if(session.getAttribute("memNo")==null) {
			return "front-end/usedorder/BuyerUsedOrderFragment :: usedOrderFragment";
		}
		Integer memNo = Integer.valueOf((String)session.getAttribute("memNo"));
		
		 List<UsedOrderVO> filteredList = usedOrderSvc.selectBuyerOrderByMemNo(memNo);
		// 將數據放到模型中
		model.addAttribute("usedorderListData", filteredList);
		// 返回Fragment所在的模板，並指定Fragment名稱
		return "front-end/usedorder/BuyerUsedOrderFragment :: usedOrderFragment";
	}
<<<<<<< Upstream, based on branch 'master' of https://github.com/he01314905/Dobuy.git
	
	
	
	
//================================================================	
	
	
	//查詢身為賣家的訂單
		@PostMapping("/getSellerUsedOrderListFragmenttest")
	    public String getSellerUsedOrderListFragmenttest(HttpSession session, Model model) {
	        // 從 session 中取得 memNo
			if(session.getAttribute("memNo")==null) {
				return "front-end/usedorder/SellerUsedOrderFragment";
			}
			
	        Integer memNo = Integer.valueOf((String)session.getAttribute("memNo"));    
	        
	     // 根據 memNo 從資料庫中查詢該位會員所販賣的二手商品
	     		List<UsedVO> usedList = usedSvc.adminSelectBySellerNo(memNo);
	     		if(usedList.isEmpty()) {
	     			return "front-end/usedorder/SellerUsedOrderFragment";
	     		}
	     		//根據拿取到的usedList去搜尋該位賣家賣的商品所有訂單
	     		//將商品編號提取出來放入新list
	     		List<Integer> usedNoList = new ArrayList<>();
	     		for(UsedVO usedVO:usedList) {
	     			usedNoList.add(usedVO.getUsedNo());
	     		}
	     		//再將提取出來的商品編號List去搜尋該位賣家賣的商品所有訂單
	     		List<UsedOrderVO> filteredList = usedOrderSvc.selectSellerOrderBySellerUsedNo(usedNoList);
	     
	        // 將數據放到模型中
	        model.addAttribute("usedorderListData", filteredList);
	        // 返回Fragment所在的模板，並指定Fragment名稱
	        return "front-end/usedorder/SellerUsedOrderFragment";
	    }
		
		
		//查詢身為買家的訂單
		@PostMapping("/getBuyerUsedOrderListFragmenttest")
		public String getBuyerUsedOrderFragmenttest(HttpSession session, Model model) {
			// 從 session 中取得 memNo
			if(session.getAttribute("memNo")==null) {
				return "front-end/usedorder/BuyerUsedOrderFragment";
			}
			Integer memNo = Integer.valueOf((String)session.getAttribute("memNo"));
			
			 List<UsedOrderVO> filteredList = usedOrderSvc.selectBuyerOrderByMemNo(memNo);
			// 將數據放到模型中
			model.addAttribute("usedorderListData", filteredList);
			// 返回Fragment所在的模板，並指定Fragment名稱
			return "front-end/usedorder/BuyerUsedOrderFragment";
		}
	
	
	
	
	
	
	
	
	
	
=======
>>>>>>> 3a970c8 push 二手訂單頁面
}
