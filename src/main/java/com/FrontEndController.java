package com;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.counter.model.CounterService;
import com.counter.model.CounterVO;
import com.counterorder.model.CounterOrderService;
import com.counterorder.model.CounterOrderVO;
import com.counterorderdetail.model.CounterOrderDetailService;
import com.counterorderdetail.model.CounterOrderDetailVO;
import com.coupon.model.CouponService;
import com.coupon.model.CouponVO;
import com.goods.model.GoodsService;
import com.goods.model.GoodsVO;
import com.goodstype.model.GoodsTypeService;
import com.goodstype.model.GoodsTypeVO;
import com.member.model.MemberService;
import com.member.model.MemberVO;
import com.used.model.UsedService;
import com.used.model.UsedVO;
import com.usedpic.model.UsedPicVO;

@Controller
public class FrontEndController {
	
	@Autowired
	GoodsService goodsSvc;
	@Autowired
	GoodsTypeService goodstSvc;
	@Autowired
	CounterService counterSvc;
	@Autowired
	CounterOrderService counterOrderSvc;
	@Autowired
	CounterOrderDetailService counterOrderDetailSvc;
	@Autowired
	CouponService couponSvc;
	
	
	@Autowired
	MemberService memSvc;
	@Autowired
	UsedService usedSvc;
	
	
	@GetMapping("")
    public String index() {
	 
        return "loading"; 
    }
	
	 @GetMapping("member")
	    public String getMemberPage(HttpSession session, Model model) {
		 	
		 	if(session.getAttribute("memNo")==null) {
		 		
		 		return "redirect:/mem/login";
		 	}else{
		 		String memNo = (String)session.getAttribute("memNo");
		 		List<CounterOrderVO> membersbuyorder=counterOrderSvc.ListfindByMemNoAndStatusNot4(Integer.valueOf(memNo));
//		 		System.out.println(membersbuyorder.size());
		 		if(membersbuyorder.size()==0) {
		 			return "front-end/normalpage/member";
		 		}
		 		List<CounterOrderVO> newlist = new ArrayList<>();
		 		List<GoodsVO> goodsNamelist = new ArrayList<>();
		 		for(CounterOrderVO counterOrderVO:membersbuyorder) {
		 			Integer eachOrderNo = counterOrderVO.getCounterOrderNo();
		 			List<CounterOrderDetailVO> detailList= counterOrderDetailSvc.getDetailsByOrderNo(eachOrderNo);
//		 			System.out.println(detailList.size());
		 			
		 			for(CounterOrderDetailVO counterOrderDetailVO:detailList) {
		 				GoodsVO goodsVO=goodsSvc.getOneGoods(counterOrderDetailVO.getGoodsNo());
		 				goodsNamelist.add(goodsVO);
		 			}
		 			
		 			
		 			counterOrderVO.setCounterOrderDatailVO(detailList);
		 			newlist.add(counterOrderVO);		 		
		 		}
		 		
		 		List<CouponVO> couponList=couponSvc.getAll();	 	
		 		List<CounterVO> counterList=counterSvc.getAll();	
		 		
		 		model.addAttribute("goodsNamelist",goodsNamelist);
		 		model.addAttribute("couponList",couponList);
		 		model.addAttribute("counterList",counterList);
		 		model.addAttribute("orders",newlist);
		 	}
	        return "front-end/normalpage/member"; 
	    }
	 @GetMapping("home")
	    public String getHomePage() {
	        return "front-end/normalpage/homepage"; 
	    }
	 @GetMapping("goodspage")
	    public String getgoodspagePage(Model model) {
		 List<GoodsVO> list = goodsSvc.getAll();
		 List<GoodsTypeVO> glist = goodstSvc.getAll();
		
		 model.addAttribute("list",list);
		 model.addAttribute("glist",glist);
	        return "front-end/normalpage/goodspage"; 
	    }
	 @GetMapping("usedgoodspage")
	    public String getusedgoodspagePage(Model model) {
		 List<UsedVO> list = usedSvc.getAll();
		 List<GoodsTypeVO> glist = goodstSvc.getAll();
		
		 model.addAttribute("list",list);
		 model.addAttribute("glist",glist);
	        return "front-end/normalpage/usedgoodspage"; 
	    }

	 
	 @GetMapping("/goods/filter")
	 @ResponseBody
	 public List<GoodsVO> filterGoodsByType(@RequestParam("goodstNo") String goodstNo,
	         Model model) {
		 
		    List<GoodsVO> alist = goodsSvc.getAll();
		    int goodstNoInt ;
		    
		    try {
		    	goodstNoInt = Integer.parseInt(goodstNo); 
		    } catch (NumberFormatException e) {
    
		        return new ArrayList<>();
		    }   
		    List<GoodsVO> filteredgoodst = new ArrayList<>();
		    for (GoodsVO goods : alist) {
		        
		        if (goods.getGoodsTypeVO() != null && goods.getGoodsTypeVO().getGoodstNo() != null) {
		           
		            if (goods.getGoodsTypeVO().getGoodstNo() == goodstNoInt) {
		            	filteredgoodst.add(goods);
		            }
		        }
		    }
		    return filteredgoodst; 
		}
	 
	 @GetMapping("/usedgoods/filter")
	 @ResponseBody
	 public List<Map<String, Object>> usedfilterGoodsByType(@RequestParam("goodstNo") String goodstNo) {
	     List<UsedVO> alist = usedSvc.getAll();
	     int goodstNoInt;
	     try {
	         goodstNoInt = Integer.parseInt(goodstNo); 
	     } catch (NumberFormatException e) {
	         return new ArrayList<>();
	     }   

	     List<Map<String, Object>> filteredGoods = new ArrayList<>();
	     for (UsedVO goods : alist) {
	         if (goods.getClassNo() != null && goods.getClassNo() == goodstNoInt) {
	             Map<String, Object> goodsMap = new HashMap<>();
	             goodsMap.put("usedNo", goods.getUsedNo());
	             goodsMap.put("usedName", goods.getUsedName());
	             goodsMap.put("usedProDesc", goods.getUsedProDesc());
	             goodsMap.put("usedPrice", goods.getUsedPrice());

	             // 提取圖片資料，將圖片編號獨立存儲
	             List<Integer> usedPics = new ArrayList<>();
	             for (UsedPicVO pic : goods.getUsedPics()) {
	                 usedPics.add(pic.getUsedPicNo());
	             }
	             goodsMap.put("usedPics", usedPics);  // 傳遞圖片編號資料

	             filteredGoods.add(goodsMap);
	         }
	     }
	     return filteredGoods;
	 }


	 
	 
	 
	 
	 
	 
	 @PostMapping("updatemem")
	   public String updatemem(@Valid MemberVO memberVO, BindingResult result, ModelMap model,HttpSession session) 
				throws IOException {
				 Object memNoObj = session.getAttribute("memNo");
				    	Integer memNo = Integer.parseInt( memNoObj.toString());
			    	    memSvc.findOne(memNo) ;
			    	    memberVO.setMemNo(memNo);
			    	    memSvc.updateMem(memberVO);
					memberVO = memSvc.findOne(memNo) ;
					
					model.addAttribute("memberVO",memberVO);
		return "front-end/normalpage/member";
		   
	   }
	 
	 @PostMapping("address")
	   public String changeadd(@RequestParam("memAddress")String memAddress, ModelMap model,HttpSession session) 
				throws IOException {
		
				 Object memNoObj = session.getAttribute("memNo");
			    	    Integer memNo = Integer.parseInt( memNoObj.toString()); 
			    	    memSvc.upAdd(memNo,memAddress);
			    MemberVO memberVO = memSvc.findOne(memNo) ;
					model.addAttribute("memberVO",memberVO);
		return "front-end/normalpage/member";
		   
	   }
	 @PostMapping("deleteac")
	   public String deleteac(ModelMap model,HttpSession session) 
				throws IOException {
		 Object memNoObj = session.getAttribute("memNo");
 	    Integer memNo = Integer.parseInt( memNoObj.toString()); 
 	   MemberVO memberVO = memSvc.findOne(memNo) ;
 	  memberVO.setMemStatus(0);
 	   memSvc.updateMem(memberVO);
 	  
 	  session.removeAttribute("memAccount");
		session.removeAttribute("memNo"); // 用memAccount去找memNo
		session.removeAttribute("memStatus");
	 return "front-end/normalpage/member";
	 }
	 @PostMapping("changepas")
	   public String changepas(@RequestParam("memPassword")String memPassword, @RequestParam("confirmPassword")String confirmPassword,ModelMap model,HttpSession session) 
				throws IOException {
		
				 Object memNoObj = session.getAttribute("memNo");
			    	    Integer memNo = Integer.parseInt( memNoObj.toString()); 
			    	    if(!memPassword.equals(confirmPassword) ) {
			    	    	model.addAttribute("error","確認密碼輸入錯誤");
			    	    	
			    	    }
			    	    memSvc.updatePass(memNo,memPassword);
			    MemberVO memberVO = memSvc.findOne(memNo) ;
					model.addAttribute("memberVO",memberVO);
		return "front-end/normalpage/member";
		   
	   }
	 
	   
	    @GetMapping("content/credit")
	    public String getcreditPage() {
	        return "content/credit"; // 對應 templates/content/profile.html
	    }
	    @GetMapping("content/changeps")
	    public String getchangepsPage(HttpSession session,Model model) {
	    	Object memNoObj = session.getAttribute("memNo");
    	    Integer memNo = Integer.parseInt( memNoObj.toString());	    
    	MemberVO memberVO;
    	memberVO = memSvc.findOne(memNo);
    	memberVO.setMemPassword("");
    	model.addAttribute("memberVO",memberVO);
	        return "content/changeps"; // 對應 templates/content/profile.html
	    }
	    @GetMapping("content/delete")
	    public String getdeletePage(Model model,HttpSession session) {
	    	Object memNoObj = session.getAttribute("memNo");
	    	Integer memNo = Integer.parseInt( memNoObj.toString());	   
	    	MemberVO memberVO;
	    	memberVO = memSvc.findOne(memNo);
	    	model.addAttribute("memberVO",memberVO);
	        return "content/delete"; // 對應 templates/content/profile.html
	    }
	    
	    @GetMapping("content/add")
	    public String getaddPage(HttpSession session,Model model) {
	    Object memNoObj = session.getAttribute("memNo");
    	Integer memNo = Integer.parseInt( memNoObj.toString());	    
    	MemberVO memberVO;
    	memberVO = memSvc.findOne(memNo);
    	model.addAttribute("memberVO",memberVO);
	        return "content/add"; // 對應 templates/content/profile.html
	    }
	    @GetMapping("content/profileup")
	    public String getprofileupPage(Model model,HttpSession session) {
	    	Object memNoObj = session.getAttribute("memNo");
    	    Integer memNo = Integer.parseInt( memNoObj.toString());	    
    	MemberVO memberVO;
    	memberVO = memSvc.findOne(memNo);
    	model.addAttribute("memberVO",memberVO);
	        return "content/profileup"; // 對應 templates/content/profile.html
	    }
	    @GetMapping("content/profile")
	    public String getProfilePage(Model model,HttpSession session) {
	    	Object memNoObj = session.getAttribute("memNo");
	    	    Integer memNo = Integer.parseInt( memNoObj.toString());	    
	    	MemberVO memberVO;
	    	memberVO = memSvc.findOne(memNo);
	    	model.addAttribute("memberVO",memberVO);
	     	return "content/profile"; 
	    }
	    

}
