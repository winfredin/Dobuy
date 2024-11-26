package com.goodstype.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
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

import com.goodstype.model.GoodsTypeService;
import com.goodstype.model.GoodsTypeVO;


@Controller
@Validated
@RequestMapping("/goodstype")
public class GoodstypeNoController {

    @Autowired
    GoodsTypeService goodsTypeSvc;

    /*
     * This method will be called on select_page.html form submission, handling POST
     * request It also validates the user input
     */
    @PostMapping("getOne_For_Display")
    public String getOne_For_Display(
        /***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
        @NotEmpty(message="商品類別編號: 請勿空白")
        @Digits(integer = 2, fraction = 0, message = "商品類別編號: 請填數字-請勿超過{integer}位數")
        @Min(value = 1, message = "商品類別編號: 不能小於{value}")
        @Max(value = 99, message = "商品類別編號: 不能超過{value}")
        @RequestParam("goodstNo") String goodstNo,
        ModelMap model) {
        
        /***************************2.開始查詢資料*********************************************/
//        GoodsTypeService goodsTypeSvc = new GoodsTypeService();
        GoodsTypeVO goodsTypeVO = goodsTypeSvc.getOneGoodsType(Integer.valueOf(goodstNo));
        
        List<GoodsTypeVO> list = goodsTypeSvc.getAll();
        model.addAttribute("goodsTypeListData", list);     // for select_page.html 第97 109行用
        
        if (goodsTypeVO == null) {
            model.addAttribute("errorMessage", "查無資料");
            return "back-end/goodstype/select_page";
        }
        
        /***************************3.查詢完成,準備轉交(Send the Success view)*****************/
        model.addAttribute("goodsTypeVO", goodsTypeVO);
        model.addAttribute("getOne_For_Display", "true"); // 旗標getOne_For_Display見select_page.html的第156行 -->
        
//        return "back-end/goods/listOneGoodsType";  // 查詢完成後轉交listOneGoodsType.html
        return "back-end/goodstype/select_page"; // 查詢完成後轉交select_page.html由其第158行insert listOneGoodsType.html內的th:fragment="listOneGoodsType-div
    }

    @ExceptionHandler(value = { ConstraintViolationException.class })
    //@ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ModelAndView handleError(HttpServletRequest req, ConstraintViolationException e, Model model) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            strBuilder.append(violation.getMessage() + "<br>");
        }
        //==== 以下是當前面返回 /src/main/resources/templates/back-end/goods/select_page.html用的 ====
        List<GoodsTypeVO> list = goodsTypeSvc.getAll();
        model.addAttribute("goodsTypeListData", list);     // for select_page.html 第97 109行用
        String message = strBuilder.toString();
        return new ModelAndView("back-end/goodstype/select_page", "errorMessage", "請修正以下錯誤:<br>" + message);
    }
}
