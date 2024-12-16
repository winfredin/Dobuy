package com.customerservice.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.multipart.MultipartFile;

import com.notice.model.NoticeService;
import com.notice.model.NoticeVO;
import com.usedorder.model.UsedOrderVO;
import com.counterorder.model.CounterOrderService;
import com.customerservice.model.CustomerServiceService;
import com.customerservice.model.CustomerServiceVO;
import com.msg.model.MsgService;
import com.msg.model.MsgVO;
import com.counter.model.*;



@Controller
@RequestMapping("/customerservice")
public class CustomerServiceController {
    
    @Autowired
    CustomerServiceService customerServiceSvc;  
    @Autowired
    CounterOrderService counterOrderSvc;
    @Autowired
    NoticeService noticeSvc;
    @Autowired
    CounterService counterSvc;
    @Autowired
    CounterRepository counterRepository;
    @Autowired
    MsgService msgSvc;
    
    

    
    @GetMapping("/addComplaint")
    public String addComplaint(HttpSession session, ModelMap model) {
        Object memNoObj = session.getAttribute("memNo");
        if (memNoObj == null) {
            return "redirect:/mem/login";
        }

        CustomerServiceVO customerServiceVO = new CustomerServiceVO();
        List<CounterVO> counters = counterSvc.getAll();
        model.addAttribute("counters", counters);
        model.addAttribute("counterList", counterSvc.getAll());
        model.addAttribute("customerServiceVO", customerServiceVO);
        return "front-end/complaint/addcomplaint";
    }
    
    
    @PostMapping("/insert")
    public String insert(HttpSession session, 
                         @Valid @ModelAttribute("customerServiceVO") CustomerServiceVO customerServiceVO, 
                         BindingResult result, ModelMap model,
                         @RequestParam("complaintReason") String complaintReason, 
                         @RequestParam("counterNo") Integer counterNo, 
                         @RequestParam("counterOrderNo") Integer counterOrderNo) {

        Object memNoObj = session.getAttribute("memNo");
        if (memNoObj == null) {
            return "redirect:/mem/login";
        }
        
        Integer memNo = (memNoObj instanceof Integer) ? 
            (Integer) memNoObj : Integer.parseInt((String) memNoObj);

        // 設置表單提交的數據
        customerServiceVO.setMemNo(memNo);  // 設置會員編號
        customerServiceVO.setCounterNo(counterNo);
        customerServiceVO.setComplaintReason(complaintReason);
        customerServiceVO.setCounterOrderNo(counterOrderNo);

        // 處理驗證錯誤
        if (result.hasErrors()) {
            model.addAttribute("counterList", counterSvc.getAll());
            return "redirect:/member";
        }

        // 新增資料
        customerServiceSvc.addComplaint(customerServiceVO);

        // 新增完成，返回顯示所有客訴的視圖
        model.addAttribute("success", "新增成功");
        return "redirect:/member";  // 重定向到已登錄的會員頁面
    }


    
    

    
    

    
//    @PostMapping("/submitReply")
//    @ResponseBody
//    public Map<String, Object> submitReply(@RequestParam("counterComplaintNo") Integer counterComplaintNo,
//                                           @RequestParam("complaintReply") String complaintReply) {
//        Map<String, Object> response = new HashMap<>();
//        try {
//            System.out.println("Received counterComplaintNo: " + counterComplaintNo);
//            System.out.println("Received complaintReply: " + complaintReply);
//
//            CustomerServiceVO customerServiceVO = customerServiceSvc.getOneComplaint(counterComplaintNo);
//            if (customerServiceVO == null) {
//                response.put("success", false);
//                response.put("error", "客訴不存在");
//                return response;
//            }
//
//            System.out.println("Complaint before reply: " + customerServiceVO);
//
//            // 保存回覆到 Notice 表
//            NoticeVO noticeVO = new NoticeVO();
//            noticeVO.setNoticeContent(complaintReply);
//            noticeVO.setNoticeDate(new Timestamp(System.currentTimeMillis()));
//            noticeVO.setMemNo(customerServiceVO.getMemNo()); // 設置會員編號
//
//            noticeSvc.save(noticeVO);
//
//            // 更新客訴狀態為處理完畢
//            customerServiceVO.setComplaintStatus((byte) 1); // 1 表示處理完畢
//            customerServiceSvc.updateComplaint(customerServiceVO);
//
//            response.put("success", true);
//            System.out.println("Reply submitted and status updated successfully");
//        } catch (Exception e) {
//            e.printStackTrace(); // 這樣可以在日誌中查看具體的異常堆棧信息
//            response.put("success", false);
//            response.put("error", "發生錯誤: " + e.getMessage());
//        }
//        return response;
//    }
    
    
    @PostMapping("/submitReply")
    @ResponseBody
    public Map<String, Object> submitReply(@RequestParam("counterComplaintNo") Integer counterComplaintNo,
                                           @RequestParam("complaintReply") String complaintReply) {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("Received counterComplaintNo: " + counterComplaintNo);
            System.out.println("Received complaintReply: " + complaintReply);

            CustomerServiceVO customerServiceVO = customerServiceSvc.getOneComplaint(counterComplaintNo);
            if (customerServiceVO == null) {
                response.put("success", false);
                response.put("error", "客訴不存在");
                return response;
            }

            System.out.println("Complaint before reply: " + customerServiceVO);

            // 保存回覆到 Notice 表
            NoticeVO noticeVO = new NoticeVO();
            noticeVO.setNoticeContent("訂單編號: " + customerServiceVO.getCounterOrderNo() + "，客訴回覆: " + complaintReply);
            noticeVO.setNoticeDate(new Timestamp(System.currentTimeMillis()));
            noticeVO.setMemNo(customerServiceVO.getMemNo()); // 使用 customerServiceVO 裡的 memNo

            noticeSvc.save(noticeVO);

            // 更新客訴狀態為處理完畢
            customerServiceVO.setComplaintStatus((byte) 1); // 1 表示處理完畢
            customerServiceSvc.updateComplaint(customerServiceVO);

            response.put("success", true);
            System.out.println("Reply submitted and status updated successfully");
        } catch (Exception e) {
            e.printStackTrace(); // 這樣可以在日誌中查看具體的異常堆棧信息
            response.put("success", false);
            response.put("error", "發生錯誤: " + e.getMessage());
        }
        return response;
    }





   
    

    
    
    
//    @GetMapping("listAllComplaint")
//    public String getAllComplaint(ModelMap model) {
//        try {
//            List<CustomerServiceVO> list = customerServiceSvc.getAll(); 
//            if (list == null) {
//                list = new ArrayList<>();
//            }
//            model.addAttribute("customerserviceListData", list);
//            return "vendor-end/customerservice/listAllComplaint";
//        } catch (Exception e) {
//            // 處理例外狀況並記錄錯誤
//            model.addAttribute("error", "無法獲取客訴資料");
//            e.printStackTrace();  // 或者使用日誌框架記錄錯誤信息
//            return "error";
//        }
//    }
    
    @GetMapping("listCounterComplaint")
    public String listAllCustomerService(HttpSession session, Model model) {
        CounterVO counter = (CounterVO) session.getAttribute("counter");
        if (counter == null) {
            return "redirect:/counter/login";
        } else {
            List<CustomerServiceVO> list = customerServiceSvc.getOneCounterCustomerService(counter.getCounterNo());

            model.addAttribute("counter", counterSvc.getOneCounter(counter.getCounterNo()));
            model.addAttribute("counterCustomerServiceListData", list);
            model.addAttribute("customerServiceSvc", customerServiceSvc);
            model.addAttribute("msgSvc", msgSvc);
            return "vendor-end/customerService/listAllComplaint";
        }
    }
//
//    
//
//    @ModelAttribute("counterCustomerServiceListData")
//    protected List<CustomerServiceVO> CounterCustomerServiceListData(HttpSession session, Model model) {
//        CounterVO counter = (CounterVO) session.getAttribute("counter");
//        if (counter != null) {
//            List<CustomerServiceVO> list = customerServiceSvc.getOneCounterCustomerService(counter.getCounterNo());
//            return list;
//        } else {
//            // 如果counter為null，返回一個空列表或處理錯誤
//            model.addAttribute("error", "未登錄或Session信息遺失");
//            return new ArrayList<>(); // 或者其他適當的錯誤處理
//        }
//    }
//
//    @PostMapping("listCounterCustomerService_ByCompositeQuery")
//    public String listCounterCustomerService(HttpSession session, HttpServletRequest req, Model model) {
//        CounterVO counter = (CounterVO) session.getAttribute("counter");
//        if (counter == null) {
//            return "redirect:/counter/login";
//        }
//        List<CustomerServiceVO> list = customerServiceSvc.getOneCounterCustomerService(counter.getCounterNo());
//        model.addAttribute("counterCustomerServiceListData", list); 
//        return "vendor-end/customerService/listAllCustomerService";
//    }


}




   



    

    
    
    
    
   
    



    



    


   

   
    
        
    


    



	
	
	



