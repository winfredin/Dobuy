package com.notice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
	 
	 
	
	 
}




    



   




   



    



   

