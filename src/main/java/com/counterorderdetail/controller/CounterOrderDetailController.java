package com.counterorderdetail.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult; 
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

import com.counterorderdetail.model.CounterOrderDetailVO;
import com.ecpay.payment.integration.ecpayOperator.EcpayFunction;
import com.goods.model.GoodsService;
import com.counterorder.model.CounterOrderService;
import com.counterorder.model.CounterOrderVO;
import com.counterorderdetail.model.CounterOrderDetailService;

@Controller
@RequestMapping("/counterOrderDetail")
public class CounterOrderDetailController {

   @Autowired
   CounterOrderDetailService counterOrderDetailService;
   @Autowired
   CounterOrderService counterOrderSvc;
   
   @Autowired
   GoodsService goodsSvc;
   
   /*
    * 處理新增訂單明細的表單請求
    */
   @PostMapping("/addCounterOrderDetail")
   public String addCounterOrderDetail(@RequestParam Map<String, String> params) {
       String counterOrderNo = params.get("CustomField1");
       
       // 更新訂單狀態為已付款
       CounterOrderVO order = counterOrderSvc.getOneCounterOrder(Integer.parseInt(counterOrderNo));
       order.setOrderStatus(1);
       counterOrderSvc.updateCounterOrder(order);

       return "redirect:/member"; // 結帳完成後重定向到會員中心或其他頁面
   }


   /*
    * 處理新增訂單明細的表單提交，包含驗證用戶輸入
    */
   @PostMapping("insert")
   public String insert(@Valid CounterOrderDetailVO counterOrderDetailVO, BindingResult result, ModelMap model) {

       // 1.驗證輸入格式
       if (result.hasErrors()) {
           return "vendor-end/counterOrderDetail/addCounterOrderDetail";
       }

       // 2.執行新增資料
       counterOrderDetailService.addCounterOrderDetail(counterOrderDetailVO);

       // 3.新增完成後的轉導處理
       List<CounterOrderDetailVO> list = counterOrderDetailService.getAll();
       model.addAttribute("counterOrderDetailListData", list);
       model.addAttribute("success", "- (新增成功)");
       return "redirect:/counterOrderDetail/listAllCounterOrderDetail";
   }

   /*
    * 處理取得單筆訂單明細資料以供修改的請求
    */
   @PostMapping("getOne_For_Update")
   public String getOne_For_Update(@RequestParam("counterOrderDetailNo") String counterOrderDetailNo, ModelMap model) {
       // 查詢資料
       CounterOrderDetailVO counterOrderDetailVO = counterOrderDetailService.getOneCounterOrderDetail(Integer.valueOf(counterOrderDetailNo));

       // 準備轉交資料
       model.addAttribute("counterOrderDetailVO", counterOrderDetailVO);
       return "vendor-end/counterOrderDetail/update_counterOrderDetail_input";
   }

   /*
    * 處理更新訂單明細資料的表單提交，包含驗證用戶輸入
    */
   @PostMapping("update")
   public String update(@Valid CounterOrderDetailVO counterOrderDetailVO, BindingResult result, ModelMap model) {

       // 1.驗證輸入格式
       if (result.hasErrors()) {
           return "vendor-end/counterOrderDetail/update_counterOrderDetail_input";
       }

       // 2.執行修改資料
       counterOrderDetailService.updateCounterOrderDetail(counterOrderDetailVO);

       // 3.修改完成後的轉導處理
       model.addAttribute("success", "- (修改成功)");
       counterOrderDetailVO = counterOrderDetailService.getOneCounterOrderDetail(counterOrderDetailVO.getCounterOrderDetailNo());
       model.addAttribute("counterOrderDetailVO", counterOrderDetailVO);
       return "vendor-end/counterOrderDetail/listOneCounterOrderDetail";
   }

   /*
    * 處理刪除訂單明細資料的請求
    */
   @PostMapping("delete")
   public String delete(@RequestParam("counterOrderDetailNo") String counterOrderDetailNo, ModelMap model) {
       // 執行刪除資料
       counterOrderDetailService.deleteCounterOrderDetail(Integer.valueOf(counterOrderDetailNo));

       // 刪除完成後的轉導處理
       List<CounterOrderDetailVO> list = counterOrderDetailService.getAll();
       model.addAttribute("counterOrderDetailListData", list);
       model.addAttribute("success", "- (刪除成功)");
       return "vendor-end/counterOrderDetail/listAllCounterOrderDetail";
   }
}