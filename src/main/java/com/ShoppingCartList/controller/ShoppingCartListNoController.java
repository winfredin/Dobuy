package com.ShoppingCartList.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ShoppingCartList.model.ShoppingCartListService;
import com.ShoppingCartList.model.ShoppingCartListVO;
import com.goods.model.GoodsService;
import com.goods.model.GoodsVO;
import com.member.model.MemberService;

@Controller
@Validated
@RequestMapping("/shoppingcartlist")
public class ShoppingCartListNoController {

    @Autowired
    ShoppingCartListService shoppingCartListSvc;

    /*
     * 當使用者在 select_page.html 提交表單時會調用此方法，並處理 POST 請求
     * 此方法也會對使用者的輸入進行驗證
     */
    @PostMapping("getOne_For_Display")
    public String getOne_For_Display(
        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 *************************/
        @NotEmpty(message="購物車編號: 請勿空白")
        @Digits(integer = 4, fraction = 0, message = "購物車編號: 請填數字-請勿超過{integer}位數")
        @Min(value = 1, message = "購物車編號: 不能小於{value}")
        @RequestParam("shoppingcartListNo") String shoppingcartListNo,
        ModelMap model) {

        /*************************** 2.開始查詢資料 ********************************************/
        ShoppingCartListVO shoppingCartListVO = shoppingCartListSvc.getOneShoppingCartList(Integer.valueOf(shoppingcartListNo));

        List<ShoppingCartListVO> list = shoppingCartListSvc.getAll();
        model.addAttribute("shoppingCartListListData", list);     // 用於 select_page.html 顯示所有資料

        if (shoppingCartListVO == null) {
            model.addAttribute("errorMessage", "查無資料");
            return "front-end/shoppingcartlist/select_page";
        }

        /*************************** 3.查詢完成,準備轉交(Send the Success view) *****************/
        model.addAttribute("shoppingCartListVO", shoppingCartListVO);
        model.addAttribute("getOne_For_Display", "true"); // 標示查詢狀態，方便在 select_page.html 顯示

        return "front-end/shoppingcartlist/select_page"; // 查詢完成後轉交 select_page.html
    }

    @ExceptionHandler(value = { ConstraintViolationException.class })
    //@ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ModelAndView handleError(HttpServletRequest req, ConstraintViolationException e, Model model) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations ) {
            strBuilder.append(violation.getMessage() + "<br>");
        }

        // 將錯誤訊息顯示到 select_page.html
        List<ShoppingCartListVO> list = shoppingCartListSvc.getAll();
        model.addAttribute("shoppingCartListListData", list);     // 用於 select_page.html 顯示所有資料
        String message = strBuilder.toString();
        return new ModelAndView("front-end/shoppingcartlist/select_page", "errorMessage", "請修正以下錯誤:<br>" + message);
    }
}
