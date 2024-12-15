package com.msg.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.counter.model.CounterService;
import com.counter.model.CounterVO;
import com.faq.model.FaqVO;
import com.msg.model.MsgService;
import com.msg.model.MsgVO;
import com.notice.model.NoticeService;
import com.notice.model.NoticeVO;
import com.member.model.MemberVO;
import com.member.model.MemberRepository;
import com.member.model.MemberService;

@Controller
@RequestMapping("/msg")
public class MsgController {

    @Autowired
    MsgService msgSvc;
    @Autowired
    NoticeService noticeSvc;
    
    @Autowired
    CounterService counterSvc;
    
    @Autowired 
    private MemberRepository memberRepository;
    
    @Autowired
    private MemberService memberSvc;
    
    
    @GetMapping("addMsg")
    public String addEmp(HttpSession session,ModelMap model) {
    	 CounterVO counter = (CounterVO) session.getAttribute("counter");
         if (counter == null) {
             // 處理沒有 CounterVO 的情況
             return "redirect:/counter/login"; // 假設有一個登錄頁面
         }
        MsgVO msgVO = new MsgVO();
        model.addAttribute("memberList", memberSvc.getAll());
        model.addAttribute("msgVO", msgVO);
        model.addAttribute("counter", counterSvc.getOneCounter(counter.getCounterNo()));
        model.addAttribute("msgSvc", msgSvc);
        return "vendor-end/msg/addMsg";
    }
    
    
    @PostMapping("insert")
    public String insert(HttpSession session, @Valid MsgVO msgVO, BindingResult result, ModelMap model,
                         @RequestParam("informMsg") String informMsg) {
    	
        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        // 從 session 中獲取 CounterVO
        CounterVO counter = (CounterVO) session.getAttribute("counter");
        
        if (counter == null) {
            // 處理沒有 CounterVO 的情況
            return "redirect:/counter/login"; // 假設有一個登錄頁面
        }
        msgVO.setCounterNo(counter.getCounterNo()); // 設置 counterNo 到 msgVO 中
        msgVO.setInformMsg(informMsg); // 設置訊息內文

        if (result.hasErrors()) {
            model.addAttribute("counter", counterSvc.getOneCounter(counter.getCounterNo()));
            model.addAttribute("msgSvc", msgSvc);
            model.addAttribute("memberList", memberRepository.findAll());
            return "vendor-end/msg/addMsg";
        }

        /*************************** 2.開始新增資料 *****************************************/
        msgSvc.addMsg(msgVO);

        // 同步新增通知信息到 Notice 表
        NoticeVO noticeVO = new NoticeVO();
        noticeVO.setNoticeContent(informMsg);
        noticeVO.setNoticeDate(new Timestamp(System.currentTimeMillis()));
        noticeVO.setMemNo(msgVO.getMemNo()); // 設置 memNo

        noticeSvc.save(noticeVO); // 使用注入的 NoticeService 保存通知信息

        /*************************** 3.新增完成,準備轉交(Send the Success view) **************/
        model.addAttribute("success", "- (新增成功)");
        return "redirect:/msg/listAllMsg"; // 新增成功後重導至顯示所有訊息的頁面
    }


    
    @PostMapping("getOne_For_Update")
    public String getOne_For_Update(HttpSession session,@RequestParam("counterInformNo") String counterInformNo, ModelMap model) {
    	CounterVO counter = (CounterVO) session.getAttribute("counter");
        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        /*************************** 2.開始查詢資料 *****************************************/
        MsgVO msgVO = msgSvc.getOneMsg(Integer.valueOf(counterInformNo));

        /*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
        model.addAttribute("counter", counterSvc.getOneCounter(counter.getCounterNo()));
        model.addAttribute("msgSvc", msgSvc);
        model.addAttribute("memberList", memberRepository.findAll());
        model.addAttribute("msgVO", msgVO);
        return "vendor-end/msg/update_msg_input"; // 查詢完成後轉交update_msg_input.html
    }
    
    
    
    @PostMapping("update")
    public String update(HttpSession session,@Valid MsgVO msgVO, BindingResult result, ModelMap model,
                         @RequestParam("informMsg") String informMsg) {
    	CounterVO counter = (CounterVO) session.getAttribute("counter");
        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        msgVO.setInformMsg(informMsg); // 設置訊息內文

        if (result.hasErrors()) {
            model.addAttribute("counter", counterSvc.getOneCounter(counter.getCounterNo()));
            model.addAttribute("msgSvc", msgSvc);
            model.addAttribute("memberList", memberRepository.findAll());
            return "vendor-end/msg/update_msg_input";
        }

        /*************************** 2.開始修改資料 *****************************************/
        // 更新消息内容
        MsgVO existingMsgVO = msgSvc.getMsgById(msgVO.getCounterInformNo());
        if (existingMsgVO == null) {
            result.rejectValue("informMsg", "error.msgVO", "消息不存在");
            return "vendor-end/msg/update_msg_input";
        }
        existingMsgVO.setInformMsg(informMsg);
        
        // 自动更新时间和已读未读状态
        existingMsgVO.setInformDate(new Timestamp(System.currentTimeMillis()));
        existingMsgVO.setInformRead((byte) 0); // 设置为未读状态

        msgSvc.updateMsg(existingMsgVO);

        // 同步更新或新增通知信息到Notice表
        NoticeVO existingNoticeVO = noticeSvc.getNoticeById(msgVO.getCounterInformNo());
        if (existingNoticeVO == null) {
            // 如果通知记录不存在，则新增一条通知
            NoticeVO newNoticeVO = new NoticeVO();
            newNoticeVO.setNoticeContent("櫃位更新了訊息: " + informMsg);
            newNoticeVO.setNoticeDate(new Timestamp(System.currentTimeMillis()));
            noticeSvc.save(newNoticeVO);
        } else {
            // 如果通知记录存在，则更新通知内容
            existingNoticeVO.setNoticeContent("櫃位更新了訊息: " + informMsg);
            existingNoticeVO.setNoticeDate(new Timestamp(System.currentTimeMillis()));
            noticeSvc.updateNotice(existingNoticeVO);
        }

        /*************************** 3.修改完成,準備轉交(Send the Success view) **************/
        model.addAttribute("success", "修改成功！");
        return "redirect:/msg/listAllMsg"; 
    }

 


    
    @PostMapping("delete")
    public String delete(HttpSession session,@RequestParam("counterInformNo") String counterInformNo, ModelMap model) {
        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        /*************************** 2.開始刪除資料 *****************************************/
        msgSvc.deleteMsg(Integer.valueOf(counterInformNo));
        /*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
        model.addAttribute("success", "- (刪除成功)");
        return "redirect:/msg/listAllMsg"; // 刪除完成後轉交listAllMsg.html
    }
   
    
//	櫃位通知管理(任國)
//    @GetMapping("listAllMsg")
//    public String listAllmsg(HttpSession session, Model model) {
//    	//櫃位優惠券登錄確認
//        CounterVO counter = (CounterVO) session.getAttribute("counter");
//        if (counter == null) {
//            return "redirect:/counter/login";
//        } else {
//            // 其他邏輯
//            model.addAttribute("counter", counterSvc.getOneCounter(counter.getCounterNo()));
//            model.addAttribute("counterMsgListData", msgSvc.getOneCounterMsg(counter.getCounterNo()));
//            return "vendor-end/msg/listAllMsg";
//        }
//    }
    
    @GetMapping("listAllMsg")
    public String listAllmsg(HttpSession session, Model model) {
        CounterVO counter = (CounterVO) session.getAttribute("counter");
        if (counter == null) {
            return "redirect:/counter/login";
        } else {
            List<MsgVO> list = msgSvc.getOneCounterMsg(counter.getCounterNo());
            // 將點進訊息通知後所有訊息設置為已讀
            for (MsgVO msg : list) {
                msg.setInformRead((byte)1);
                msgSvc.updateMsg(msg); 
            }
            model.addAttribute("counter", counterSvc.getOneCounter(counter.getCounterNo()));
            model.addAttribute("counterMsgListData", list);
            model.addAttribute("msgSvc", msgSvc);
            return "vendor-end/msg/listAllMsg";
        }
    }
    
    
    
    
    
    @ModelAttribute("counterMsgListData")
    protected List<MsgVO> CounterReferenceListData(HttpSession session, Model model) {
        CounterVO counter = (CounterVO) session.getAttribute("counter");
        if (counter != null) {
        	List<MsgVO> list =  msgSvc.getOneCounterMsg(counter.getCounterNo());
            return list;
        } else {
            // 如果counter為null，返回一個空列表或處理錯誤
            model.addAttribute("error", "未登錄或Session信息遺失");
            return new ArrayList<>(); // 或者其他適當的錯誤處理
        }
    }
    
    @PostMapping("listCounterMsg_ByCompositeQuery")
    public String listCounterMsg(HttpSession session ,HttpServletRequest req, Model model) {
        CounterVO counter = (CounterVO) session.getAttribute("counter");
        List<MsgVO> list =  msgSvc.getOneCounterMsg(counter.getCounterNo());
        model.addAttribute("counterMsgListData", list); 
        return "vendor-end/msg/listAllMsg";
    }
    
    
        
    }

        


