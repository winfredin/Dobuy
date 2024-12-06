package com;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import com.goods.model.GoodsService;
import com.goods.model.GoodsVO;
import com.goodstype.model.GoodsTypeService;
import com.goodstype.model.GoodsTypeVO;
import com.member.model.MemberService;
import com.member.model.MemberVO;

@Controller
public class FrontEndController {
	
	@Autowired
	GoodsService goodsSvc;
	@Autowired
	GoodsTypeService goodstSvc;
	@Autowired
	CounterService counterSvc;
	@Autowired
	MemberService memSvc;
	
	
	 @GetMapping("member")
	    public String getMemberPage() {
		 
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
 	  
 	    session.invalidate();
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
