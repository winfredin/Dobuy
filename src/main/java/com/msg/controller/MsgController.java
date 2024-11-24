package com.msg.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.msg.model.MsgService;
import com.msg.model.MsgVO;

@Controller
@RequestMapping("/msg")
public class MsgController {

    @Autowired
    MsgService msgSvc;
    
    @GetMapping("addMsg")
    public String addEmp(ModelMap model) {
        MsgVO msgVO = new MsgVO();
        model.addAttribute("msgVO", msgVO);
        return "back-end/msg/addMsg";
    }
    
    @PostMapping("insert")
    public String insert(@Valid MsgVO msgVO, BindingResult result, ModelMap model,
                         @RequestParam("informMsg") String informMsg,
                         @RequestParam("informDate") String informDateStr) {

        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        msgVO.setInformMsg(informMsg); // 設置訊息內文

        // 使用 SimpleDateFormat 來解析字串並轉換為 Timestamp
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = dateFormat.parse(informDateStr);
            Timestamp timestamp = new Timestamp(parsedDate.getTime());
            LocalDateTime localDateTime = timestamp.toLocalDateTime();
            msgVO.setInformDate(Timestamp.valueOf(localDateTime)); // 設置發佈日期
        } catch (ParseException e) {
            result.rejectValue("informDate", "error.msgVO", "日期格式錯誤");
            return "back-end/msg/addMsg";
        }

        if (result.hasErrors()) {
            return "back-end/msg/addMsg";
        }

        /*************************** 2.開始新增資料 *****************************************/
        msgSvc.addMsg(msgVO);

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
    
    @PostMapping("update")
    public String update(@Valid MsgVO msgVO, BindingResult result, ModelMap model,
                         @RequestParam("informMsg") String informMsg,
                         @RequestParam("informDate") String informDateStr) {

        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        msgVO.setInformMsg(informMsg); // 設置訊息內文
        
        // 使用 SimpleDateFormat 來解析字串並轉換為 Timestamp
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = dateFormat.parse(informDateStr);
            Timestamp timestamp = new Timestamp(parsedDate.getTime());
            msgVO.setInformDate(timestamp); // 設置發佈日期
        } catch (ParseException e) {
            result.rejectValue("informDate", "error.msgVO", "日期格式錯誤");
            return "back-end/msg/update_msg_input";
        }

        if (result.hasErrors()) {
            return "back-end/msg/update_msg_input";
        }

        /*************************** 2.開始修改資料 *****************************************/
        msgSvc.updateMsg(msgVO);

        /*************************** 3.修改完成,準備轉交(Send the Success view) **************/
        model.addAttribute("success", "- (修改成功)");
        msgVO = msgSvc.getOneMsg(Integer.valueOf(msgVO.getCounterInformNo()));
        model.addAttribute("msgVO", msgVO);
        return "back-end/msg/listOneMsg"; // 修改成功後轉交listOneMsg.html
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