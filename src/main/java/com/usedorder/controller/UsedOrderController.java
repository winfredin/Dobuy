package com.usedorder.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import com.usedorder.model.UsedOrderVO;
import com.notice.model.NoticeVO;
import com.used.model.UsedVO;
import com.usedorder.model.UsedOrderService;
import com.notice.model.NoticeService;


@Controller
@RequestMapping("/usedorder")
public class UsedOrderController {

	@Autowired
	UsedOrderService usedOrderSvc;
	@Autowired
	NoticeService noticeSvc;
	
	@PostMapping("updateDeliveryStatus")
	@ResponseBody
	public Map<String, Object> updateDeliveryStatus(@RequestParam("usedOrderNo") Integer usedOrderNo,
	                                                @RequestParam("deliveryStatus") Byte deliveryStatus) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	        System.out.println("Received usedOrderNo: " + usedOrderNo);
	        System.out.println("Received deliveryStatus: " + deliveryStatus);

	        UsedOrderVO usedOrderVO = usedOrderSvc.getOneUsedOrder(usedOrderNo);
	        if (usedOrderVO == null) {
	            response.put("success", false);
	            response.put("error", "訂單不存在");
	            return response;
	        }

	        System.out.println("Order before update: " + usedOrderVO);

	        usedOrderVO.setDeliveryStatus(deliveryStatus);
	        usedOrderSvc.updateUsedOrder(usedOrderVO);

	        // 發送通知消息
	        String deliveryStatusText = getDeliveryStatusText(deliveryStatus);
	        NoticeVO noticeVO = new NoticeVO();
	        noticeVO.setNoticeContent("訂單號 " + usedOrderNo + " 訂單狀態更新為" + deliveryStatusText);
	        noticeVO.setNoticeDate(new Timestamp(System.currentTimeMillis()));
	        
	        // 獲取 buyerNo 並設置到 noticeVO 的 memNo
	        Integer buyerNo = usedOrderVO.getBuyerNo();
	        noticeVO.setMemNo(buyerNo);

	        noticeSvc.save(noticeVO);

	        response.put("success", true);
	        System.out.println("Update successful");
	        return response;
	    } catch (Exception e) {
	        e.printStackTrace(); // 這樣可以在日誌中查看具體的異常堆棧信息
	        response.put("success", false);
	        response.put("error", "發生錯誤: " + e.getMessage());
	        return response;
	    }
	}
	private String getDeliveryStatusText(Byte deliveryStatus) {
		switch (deliveryStatus) {
		case 0: return "未出貨"; 
		case 1: return "已出貨"; 
		case 2: return "待領件"; 
		case 3: return "已領貨"; 
		case 4: return "已取消"; 
		case 5: return "已付款";
		case 6: return "未付款";
		default: return "已付款"; } }

	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(UsedOrderVO usedOrderVO, BindingResult result, String removedFieldname) {
	    List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
	            .filter(fieldname -> !fieldname.getField().equals(removedFieldname))
	            .collect(Collectors.toList());
	    result = new BeanPropertyBindingResult(usedOrderVO, "usedOrderVO");
	    for (FieldError fieldError : errorsListToKeep) {
	        result.addError(fieldError);
	    }
	    return result;
	}

	@GetMapping("listAllUsedOrder")
	public String getAllOrder(ModelMap model) {
	    List<UsedOrderVO> list = usedOrderSvc.getAll(); 
	    // 過濾掉 deliveryStatus = 6 的訂單
	    List<UsedOrderVO> filteredList = list.stream()
	                                         .filter(order -> order.getDeliveryStatus() != 6)
	                                         .collect(Collectors.toList());
	    model.addAttribute("usedorderListData", filteredList);
	    return "front-end/usedorder/listAllUsedOrder";
	}


	// 買家訂單管理
	@GetMapping("listAllUsedOrder2")
	public String getAllOrder2(ModelMap model) {
	    List<UsedOrderVO> list = usedOrderSvc.getAll();
	 // 過濾掉 deliveryStatus = 6 的訂單
	    List<UsedOrderVO> filteredList = list.stream()
	                                         .filter(order -> order.getDeliveryStatus() != 6)
	                                         .collect(Collectors.toList());
	    model.addAttribute("usedorderListData", filteredList);
	    return "front-end/usedorder/listAllUsedOrder2";
	}

   
	
    @PostMapping("sendComplaintEmail")
    @ResponseBody
    public Map<String, Object> sendComplaintEmail(@RequestParam("orderNo") Integer orderNo,
                                                  @RequestParam("subject") String subject,
                                                  @RequestParam("content") String content) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 發送郵件的邏輯
            // 這裡你可以使用之前分享的 JavaMail API 代碼來發送郵件

            // 假設這是你的發送郵件邏輯
            MailSender.sendEmail("rubylin2000@gmail.com", subject, content);

            response.put("success", true);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "發生錯誤: " + e.getMessage());
        }
        return response;
    }

	

	
}





    