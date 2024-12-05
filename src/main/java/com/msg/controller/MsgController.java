package com.msg.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.msg.model.MsgService;
import com.msg.model.MsgVO;
import com.notice.model.NoticeService;
import com.notice.model.NoticeVO;

@Controller
@RequestMapping("/msg")
public class MsgController {

    @Autowired
    MsgService msgSvc;
    @Autowired
    NoticeService noticeSvc;
    
    @GetMapping("addMsg")
    public String addEmp(ModelMap model) {
        MsgVO msgVO = new MsgVO();
        model.addAttribute("msgVO", msgVO);
        return "back-end/msg/addMsg";
    }
    
    @PostMapping("insert")
    public String insert(@Valid MsgVO msgVO, BindingResult result, ModelMap model,
                         @RequestParam("informMsg") String informMsg) {

        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        msgVO.setInformMsg(informMsg); // 設置訊息內文

        if (result.hasErrors()) {
            return "back-end/msg/addMsg";
        }

        /*************************** 2.開始新增資料 *****************************************/
        msgSvc.addMsg(msgVO);

        // 同步新增通知信息到Notice表
        NoticeVO noticeVO = new NoticeVO();
        noticeVO.setNoticeContent(informMsg);
        noticeVO.setNoticeDate(new Timestamp(System.currentTimeMillis()));
        noticeSvc.save(noticeVO); // 使用注入的NoticeService保存通知信息

        /*************************** 3.新增完成,準備轉交(Send the Success view) **************/
        List<MsgVO> list = msgSvc.getAll();
        model.addAttribute("msgListData", list);
        model.addAttribute("success", "- (新增成功)");
        return "back-end/msg/listAllMsg"; // 新增成功後重導至顯示所有訊息的頁面
    }


    
    @PostMapping("getOne_For_Update")
    public String getOne_For_Update(@RequestParam("counterInformNo") String counterInformNo, ModelMap model) {
        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        /*************************** 2.開始查詢資料 *****************************************/
        MsgVO msgVO = msgSvc.getOneMsg(Integer.valueOf(counterInformNo));

        /*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
        model.addAttribute("msgVO", msgVO);
        return "back-end/msg/update_msg_input"; // 查詢完成後轉交update_msg_input.html
    }
    
    
   

    
//    @PostMapping("update")
//    public String update(@Valid MsgVO msgVO, BindingResult result, ModelMap model,
//                         @RequestParam("informMsg") String informMsg) {
//
//        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
//        msgVO.setInformMsg(informMsg); // 設置訊息內文
//
//        if (result.hasErrors()) {
//            return "back-end/msg/update_msg_input";
//        }
//
//        /*************************** 2.開始修改資料 *****************************************/
//        // 更新消息内容
//        MsgVO existingMsgVO = msgSvc.getMsgById(msgVO.getCounterInformNo());
//        if (existingMsgVO == null) {
//            result.rejectValue("informMsg", "error.msgVO", "消息不存在");
//            return "back-end/msg/update_msg_input";
//        }
//        existingMsgVO.setInformMsg(informMsg);
//        
//        // 自动更新时间和已读未读状态
//        existingMsgVO.setInformDate(new Timestamp(System.currentTimeMillis()));
//        existingMsgVO.setInformRead((byte) 0); // 设置为未读状态
//
//        msgSvc.updateMsg(existingMsgVO);
//
//        /*************************** 3.修改完成,準備轉交(Send the Success view) **************/
//        model.addAttribute("success", "修改成功！");
//
//        // 修改此處為返回列表頁
//        return "redirect:/msg/listAllMsg"; 
//    }
    
    @PostMapping("update")
    public String update(@Valid MsgVO msgVO, BindingResult result, ModelMap model,
                         @RequestParam("informMsg") String informMsg) {

        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        msgVO.setInformMsg(informMsg); // 設置訊息內文

        if (result.hasErrors()) {
            return "back-end/msg/update_msg_input";
        }

        /*************************** 2.開始修改資料 *****************************************/
        // 更新消息内容
        MsgVO existingMsgVO = msgSvc.getMsgById(msgVO.getCounterInformNo());
        if (existingMsgVO == null) {
            result.rejectValue("informMsg", "error.msgVO", "消息不存在");
            return "back-end/msg/update_msg_input";
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
    public String delete(@RequestParam("counterInformNo") String counterInformNo, ModelMap model) {
        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        /*************************** 2.開始刪除資料 *****************************************/
        msgSvc.deleteMsg(Integer.valueOf(counterInformNo));
        /*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
        List<MsgVO> list = msgSvc.getAll();
        model.addAttribute("msgListData", list);
        model.addAttribute("success", "- (刪除成功)");
        return "back-end/msg/listAllMsg"; // 刪除完成後轉交listAllMsg.html
    }
   
    @GetMapping("listAllMsg")
    public String getAllMsg(ModelMap model) {
        List<MsgVO> list = msgSvc.getAll(); // 假設 msgSvc.getAll() 可取得所有訊息
        model.addAttribute("msgListData", list);
        return "back-end/msg/listAllMsg";
    }
    
   
    @GetMapping("listAllMsg2")
    public String getAllMsg2(ModelMap model) {
    	List<MsgVO> list = msgSvc.getAll();
    	model.addAttribute("msgListData", list);
    	return "front-end/msg/listAllMsg2";
    }
    
        
    }

        


