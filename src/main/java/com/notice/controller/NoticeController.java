package com.notice.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.counter.model.CounterVO;
import com.msg.model.MsgVO;
import com.notice.model.NoticeService;
import com.notice.model.NoticeVO;


@Controller
@RequestMapping("/notice")
public class NoticeController {
	
	
	@Autowired
    NoticeService noticeSvc;
	
	
	@GetMapping("listAllNotice")
    public String getAllNotice(ModelMap model) {
    	List<NoticeVO> list = noticeSvc.getAll();
    	model.addAttribute("noticeListData", list);
    	return "front-end/notice/listAllNotice";
    }
	
	 @PostMapping("clearAll")
	    public String clearAllNotices(ModelMap model) {
	        noticeSvc.deleteAll();
	        model.addAttribute("success", "所有通知已清空！");
	        return "redirect:/notice/listAllNotice";
	    }
	 
//		會員通知總攬
//	    @GetMapping("listAllNotice")
//	    public String listAllnotice(HttpSession session, Model model) {
//	    	//櫃位優惠券登錄確認
//	        MemberVO member = (MemberVO) session.getAttribute("member");
//	        if (member == null) {
//	            return "redirect:/member/login";
//	        } else {
//	            // 其他邏輯
//	            model.addAttribute("member", memberSvc.getOneCounter(member.getMemNo()));
//	            model.addAttribute("memberNoticeListData", noticeSvc.getOneCounterMsg(member.getMemNo()));
//	            return "front-end/notice/listAllNotice";
//	        }
//	    }
//	    
//	    @ModelAttribute("memberNoticeListData")
//	    protected List<NoticeVO> MemberReferenceListData(HttpSession session, Model model) {
//	        MemberVO member = (MemberVO) session.getAttribute("member");
//	        if (member != null) {
//	        	List<MsgVO> list =  msgSvc.getOneCounterMsg(counter.getCounterNo());
//	            return list;
//	        } else {
//	            // 如果counter為null，返回一個空列表或處理錯誤
//	            model.addAttribute("error", "未登錄或Session信息遺失");
//	            return new ArrayList<>(); // 或者其他適當的錯誤處理
//	        }
//	    }
//	    
//	    @PostMapping("listCounterMsg_ByCompositeQuery")
//	    public String listCounterMsg(HttpSession session ,HttpServletRequest req, Model model) {
//	        CounterVO counter = (CounterVO) session.getAttribute("counter");
//	        List<MsgVO> list =  msgSvc.getOneCounterMsg(counter.getCounterNo());
//	        model.addAttribute("counterMsgListData", list); 
//	        return "vendor-end/msg/listAllMsg";
//	    }
//	    
	 
	 
	
	 
}




    



   




   



    



   

