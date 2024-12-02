package com.memcoupon.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

import com.memcoupon.model.MemCouponVO;
import com.member.model.MemberService;
import com.member.model.MemberVO;
import com.memcoupon.model.MemCouponService;

@Controller
@RequestMapping("/memcoupon")
public class MemCouponController {

	@Autowired
	MemCouponService memCouponSvc;

	@Autowired
	MemberService memberSvc;

    
    
    
    @GetMapping("/memListAllCoupon")
    public String listMemberCoupons(HttpSession session, Model model) {
        String memAccount = (String) session.getAttribute("memAccount");
        if (memAccount == null) {
            return "redirect:/mem/login49";
        }

        try {
            MemberVO member = memberSvc.findByMemAccount(memAccount);
            if (member == null) {
                return "redirect:/mem/login49";
            }

            List<MemCouponVO> memCoupons = memCouponSvc.getAllByMemNo(member.getMemNo());
            model.addAttribute("memCoupons", memCoupons);
            model.addAttribute("memAccount", memAccount);  

            return "front-end/memcoupon/memListAllCoupon";  
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "front-end/memcoupon/memListAllCoupon";
        }
    }
    
        
    /*
     * This method will serve as addMemCoupon.html handler.
     */
    @GetMapping("addMemCoupon")
    public String addMemCoupon(ModelMap model) {
        MemCouponVO memCouponVO = new MemCouponVO();
        model.addAttribute("memCouponVO", memCouponVO);
        return "vendor-end/memcoupon/addMemCoupon";
    }

    // 靜態用
    @GetMapping("/memcoupon/addMemCoupon")
    public String addMemCoupon(Model model) {
        return "vendor-end/memcoupon/addMemCoupon";
    }

    /*
     * This method will be called on addMemCoupon.html form submission, handling POST request
     * It also validates the user input
     */
    @PostMapping("insert")
    public String insert(@Valid MemCouponVO memCouponVO, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            return "vendor-end/memcoupon/addMemCoupon";
        }

        /*************************** 2.開始新增資料 *****************************************/
        memCouponSvc.addMemCoupon(memCouponVO);

        /*************************** 3.新增完成,準備轉交(Send the Success view) **************/
        List<MemCouponVO> list = memCouponSvc.getAll();
        model.addAttribute("memCouponListData", list);
        model.addAttribute("success", "- (新增成功)");
        return "redirect:/memcoupon/listAllMemCoupon";
    }

    /*
     * This method will be called on listAllMemCoupon.html form submission, handling POST request
     */
    @PostMapping("getOne_For_Update")
    public String getOne_For_Update(@RequestParam("memCouponNo") String memCouponNo, ModelMap model) {
        /*************************** 2.開始查詢資料 *****************************************/
        MemCouponVO memCouponVO = memCouponSvc.getOneMemCoupon(Integer.valueOf(memCouponNo));

        /*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
        model.addAttribute("memCouponVO", memCouponVO);
        return "vendor-end/memcoupon/update_memCoupon_input";
    }

    /*
     * This method will be called on update_memCoupon_input.html form submission, handling POST request
     * It also validates the user input
     */
    @PostMapping("update")
    public String update(@Valid MemCouponVO memCouponVO, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            return "vendor-end/memcoupon/update_memCoupon_input";
        }

        /*************************** 2.開始修改資料 *****************************************/
        memCouponSvc.updateMemCoupon(memCouponVO);

        /*************************** 3.修改完成,準備轉交(Send the Success view) **************/
        model.addAttribute("success", "- (修改成功)");
        memCouponVO = memCouponSvc.getOneMemCoupon(Integer.valueOf(memCouponVO.getMemCouponNo()));
        model.addAttribute("memCouponVO", memCouponVO);
        return "vendor-end/memcoupon/listOneMemCoupon";
    }

    /*
     * This method will be called on listAllMemCoupon.html form submission, handling POST request
     */
    @PostMapping("delete")
    public String delete(@RequestParam("memCouponNo") String memCouponNo, ModelMap model) {
        /*************************** 2.開始刪除資料 *****************************************/
        memCouponSvc.deleteMemCoupon(Integer.valueOf(memCouponNo));

        /*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
        List<MemCouponVO> list = memCouponSvc.getAll();
        model.addAttribute("memCouponListData", list);
        model.addAttribute("success", "- (刪除成功)");
        return "vendor-end/memcoupon/listAllMemCoupon";
    }

    /*
     * This method will be called on select_page.html form submission, handling POST request
     */
    @PostMapping("listMemCoupon_ByCompositeQuery")
    public String listAllMemCoupon(HttpServletRequest req, Model model) {
        Map<String, String[]> map = req.getParameterMap();
        List<MemCouponVO> list = memCouponSvc.getAll(map);
        model.addAttribute("memCouponListData", list);
        return "vendor-end/memcoupon/listAllMemCoupon";
    }

    // 去除BindingResult中某個欄位的FieldError紀錄
    public BindingResult removeFieldError(MemCouponVO memCouponVO, BindingResult result, String removedFieldname) {
        List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
                .filter(fieldname -> !fieldname.getField().equals(removedFieldname))
                .collect(Collectors.toList());
        result = new BeanPropertyBindingResult(memCouponVO, "memCouponVO");
        for (FieldError fieldError : errorsListToKeep) {
            result.addError(fieldError);
        }
        return result;
    }
    
    
    
}
