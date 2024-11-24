package com.discount.controller;

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

import com.discount.model.DiscountVO;
import com.discount.model.DiscountService;

@Controller
@RequestMapping("/discount")
public class DiscountController {

    @Autowired
    DiscountService discountService;

    /*
     * This method will serve as addDiscount.html handler.
     */
    @GetMapping("addDiscount")
    public String addDiscount(ModelMap model) {
        DiscountVO discountVO = new DiscountVO();
        model.addAttribute("discountVO", discountVO);
        return "vendor-end/discount/addDiscount";
    }

    /*
     * This method will be called on addDiscount.html form submission, handling POST request
     * It also validates the user input
     */
    @PostMapping("insert")
    public String insert(@Valid DiscountVO discountVO, BindingResult result, ModelMap model) {

        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        if (result.hasErrors()) {
            return "vendor-end/discount/addDiscount";
        }

        /*************************** 2.開始新增資料 *****************************************/
        discountService.addDiscount(discountVO);

        /*************************** 3.新增完成,準備轉交(Send the Success view) **************/
        List<DiscountVO> list = discountService.getAll();
        model.addAttribute("discountListData", list);
        model.addAttribute("success", "- (新增成功)");
        return "redirect:/discount/listAllDiscount";
    }

    /*
     * This method will be called on listAllDiscounts.html form submission, handling POST request
     */
    @PostMapping("getOne_For_Update")
    public String getOne_For_Update(@RequestParam("disNo") String disNo, ModelMap model) {
        /*************************** 2.開始查詢資料 *****************************************/
        DiscountVO discountVO = discountService.getOneDiscount(Integer.valueOf(disNo));

        /*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
        model.addAttribute("discountVO", discountVO);
        return "vendor-end/discount/update_discount_input";
    }

    /*
     * This method will be called on update_discount_input.html form submission, handling POST request
     * It also validates the user input
     */
    @PostMapping("update")
    public String update(@Valid DiscountVO discountVO, BindingResult result, ModelMap model) {

        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        if (result.hasErrors()) {
            return "vendor-end/discount/update_discount_input";
        }

        /*************************** 2.開始修改資料 *****************************************/
        discountService.updateDiscount(discountVO);

        /*************************** 3.修改完成,準備轉交(Send the Success view) **************/
        model.addAttribute("success", "- (修改成功)");
        discountVO = discountService.getOneDiscount(discountVO.getDisNo());
        model.addAttribute("discountVO", discountVO);
        return "vendor-end/discount/listOneDiscount";
    }

    /*
     * This method will be called on listAllDiscounts.html form submission, handling POST request
     */
    @PostMapping("delete")
    public String delete(@RequestParam("disNo") String disNo, ModelMap model) {
        /*************************** 2.開始刪除資料 *****************************************/
        discountService.deleteDiscount(Integer.valueOf(disNo));

        /*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
        List<DiscountVO> list = discountService.getAll();
        model.addAttribute("discountListData", list);
        model.addAttribute("success", "- (刪除成功)");
        return "vendor-end/discount/listAllDiscount";
    }

    /*
     * This method will be called on select_page.html form submission, handling POST request
     */
    @PostMapping("listDiscount_ByCompositeQuery")
    public String listAllDiscount(HttpServletRequest req, Model model) {
        Map<String, String[]> map = req.getParameterMap();
        List<DiscountVO> list = discountService.getAll(map);
        model.addAttribute("discountListData", list);
        return "vendor-end/discount/listAllDiscount";
    }
}
