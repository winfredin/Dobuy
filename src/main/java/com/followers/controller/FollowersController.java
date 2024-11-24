package com.followers.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.followers.model.FollowersService;
import com.followers.model.FollowersVO;

@Controller
@RequestMapping("/followers")
public class FollowersController {

    @Autowired
    FollowersService followersService;

    @GetMapping("addFollowers")
    public String addFollowers(ModelMap model) {
        FollowersVO followersVO = new FollowersVO();
        model.addAttribute("followersVO", followersVO);
        return "back-end/followers/addFollowers";
    }

    @PostMapping("insert")
    public String insert(@Valid FollowersVO followersVO, BindingResult result, ModelMap model) throws IOException {
        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        
        /*************************** 2.開始新增資料 *****************************************/
        followersService.addFollowers(followersVO);
        /*************************** 3.新增完成,準備轉交(Send the Success view) **************/
        List<FollowersVO> list = followersService.getAll();
        model.addAttribute("followersListData", list);
        model.addAttribute("success", "- (新增成功)");
        return "redirect:/followers/listAllFollowers";
    }

    @PostMapping("getOneForUpdate")
    public String getOneForUpdate(@RequestParam("trackListNo") String trackListNo, ModelMap model) {
        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        /*************************** 2.開始查詢資料 *****************************************/
        FollowersVO followersVO = followersService.getOneFollowers(Integer.valueOf(trackListNo));
        /*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
        model.addAttribute("followersVO", followersVO);
        return "back-end/followers/updateFollowersInput";
    }

    @PostMapping("update")
    public String update(@Valid FollowersVO followersVO, BindingResult result, ModelMap model) throws IOException {
        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        
        /*************************** 2.開始修改資料 *****************************************/
        followersService.updateFollowers(followersVO);
        /*************************** 3.修改完成,準備轉交(Send the Success view) **************/
        model.addAttribute("success", "- (修改成功)");
        followersVO = followersService.getOneFollowers(Integer.valueOf(followersVO.getTrackListNo()));
        model.addAttribute("followersVO", followersVO);
        return "back-end/followers/listOneFollowers";
    }

    @PostMapping("delete")
    public String delete(@RequestParam("trackListNo") String trackListNo, ModelMap model) {
        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        /*************************** 2.開始刪除資料 *****************************************/
        followersService.deleteFollowers(Integer.valueOf(trackListNo));
        /*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
        List<FollowersVO> list = followersService.getAll();
        model.addAttribute("followersListData", list);
        model.addAttribute("success", "- (刪除成功)");
        return "back-end/followers/listAllFollowers";
    }

    // 去除BindingResult中某個欄位的FieldError紀錄
    public BindingResult removeFieldError(FollowersVO followersVO, BindingResult result, String removedFieldName) {
        List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
                .filter(fieldName -> !fieldName.getField().equals(removedFieldName))
                .collect(Collectors.toList());
        result = new BeanPropertyBindingResult(followersVO, "followersVO");
        for (FieldError fieldError : errorsListToKeep) {
            result.addError(fieldError);
        }
        return result;
    }
}
