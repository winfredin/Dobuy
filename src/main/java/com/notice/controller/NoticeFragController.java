package com.notice.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.notice.model.NoticeService;
import com.notice.model.NoticeVO;


@Controller
@RequestMapping("/notice")
public class NoticeFragController {
	
	@Autowired 
	NoticeService noticeSvc;
	
	
	//從會員頁面查詢通知======================================================
	@PostMapping("listAllNoticeFragment")
	public String getAllNoticeFragment(HttpSession session, ModelMap model) {
	    // 從 session 中取得 memNo
	    if(session.getAttribute("memNo") == null) {
	        return "front-end/notice/ListAllNoticeFragment :: listallnoticeFragment";
	    }

	    Integer memNo = Integer.valueOf((String)session.getAttribute("memNo"));    
	        
	    // 根據 memNo 從資料庫中查詢該會員的通知
	    List<NoticeVO> noticeList = noticeSvc.getNoticesByMemNo(memNo);
	    if(noticeList.isEmpty()) {
	        return "front-end/notice/ListAllNoticeFragment :: listallnoticeFragment";
	    }

	    // 將數據放到模型中
	    model.addAttribute("noticeListData", noticeList);
	    // 返回Fragment所在的模板，並指定Fragment名稱
	    return "front-end/notice/ListAllNoticeFragment :: listallnoticeFragment";  
	}
	
	
	 

    
  

}
